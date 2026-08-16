package com.examsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExamWindow.java
 * ---------------------------------------------
 * This is the MAIN exam screen. It:
 *   1) Loads all questions from the database
 *   2) Displays them as MCQs (radio buttons)
 *   3) Runs a COUNTDOWN TIMER (javax.swing.Timer)
 *   4) Auto-submits the exam when time runs out
 *   5) On submit, sends student's answers to ResultService
 *      for AUTO-GRADING
 *
 * Explained by: STUDENT 3 (Exam Window & Timer part)
 * ---------------------------------------------
 */
public class ExamWindow extends JFrame {

    private static final int EXAM_DURATION_SECONDS = 300; // 5 minutes - change as needed

    private int studentId;
    private String studentName;
    private List<Question> questions;

    // Stores the option the student picked for each question: questionId -> "A"/"B"/"C"/"D"
    private Map<Integer, String> selectedAnswers = new HashMap<>();

    private JLabel timerLabel;
    private Timer swingTimer;
    private int remainingSeconds = EXAM_DURATION_SECONDS;

    private DatabaseService dbService;
    private ResultService resultService;

    public ExamWindow(int studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.dbService = new DatabaseService();
        this.resultService = new ResultService();

        setTitle("Online Exam - " + studentName);
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ---- TOP PANEL: student name + timer ----
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel("  Student: " + studentName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timerLabel = new JLabel("Time Left: 05:00  ", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timerLabel.setForeground(Color.RED);
        topPanel.add(nameLabel, BorderLayout.WEST);
        topPanel.add(timerLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ---- CENTER PANEL: all questions in a scroll pane ----
        JPanel questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));

        try {
            questions = dbService.getAllQuestions();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not load questions from database.\n\nError: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            questions = new java.util.ArrayList<>();
        }

        int qNum = 1;
        for (Question q : questions) {
            questionsPanel.add(buildQuestionPanel(q, qNum));
            qNum++;
        }

        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // ---- BOTTOM PANEL: submit button ----
        JButton submitButton = new JButton("Submit Exam");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.addActionListener(e -> submitExam(false));
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(submitButton);
        add(bottomPanel, BorderLayout.SOUTH);

        startTimer();
        setVisible(true);
    }

    /**
     * Builds one question block: question text + 4 radio buttons (A-D)
     * grouped together so only ONE option can be selected per question.
     */
    private JPanel buildQuestionPanel(Question q, int qNum) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        JLabel qLabel = new JLabel("Q" + qNum + ". " + q.getQuestionText());
        qLabel.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(qLabel);

        ButtonGroup group = new ButtonGroup();

        JRadioButton optA = new JRadioButton("A. " + q.getOptionA());
        JRadioButton optB = new JRadioButton("B. " + q.getOptionB());
        JRadioButton optC = new JRadioButton("C. " + q.getOptionC());
        JRadioButton optD = new JRadioButton("D. " + q.getOptionD());

        // Every time student clicks an option, store it immediately in the map
        optA.addActionListener(e -> selectedAnswers.put(q.getId(), "A"));
        optB.addActionListener(e -> selectedAnswers.put(q.getId(), "B"));
        optC.addActionListener(e -> selectedAnswers.put(q.getId(), "C"));
        optD.addActionListener(e -> selectedAnswers.put(q.getId(), "D"));

        group.add(optA);
        group.add(optB);
        group.add(optC);
        group.add(optD);

        panel.add(optA);
        panel.add(optB);
        panel.add(optC);
        panel.add(optD);

        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    /**
     * Starts a javax.swing.Timer that ticks every 1000ms (1 second).
     * This is the TIMER SYSTEM feature required by the project.
     */
    private void startTimer() {
        swingTimer = new Timer(1000, e -> {
            remainingSeconds--;
            int minutes = remainingSeconds / 60;
            int seconds = remainingSeconds % 60;
            timerLabel.setText(String.format("Time Left: %02d:%02d  ", minutes, seconds));

            if (remainingSeconds <= 0) {
                swingTimer.stop();
                JOptionPane.showMessageDialog(this, "Time is up! Auto-submitting your exam.");
                submitExam(true); // auto-submit
            }
        });
        swingTimer.start();
    }

    /**
     * Called when student clicks "Submit" OR when the timer hits 0.
     * Passes the answers to ResultService for AUTO-GRADING.
     */
    private void submitExam(boolean autoSubmitted) {
        if (swingTimer != null) {
            swingTimer.stop();
        }

        try {
            ResultService.GradeResult result = resultService.gradeExam(questions, selectedAnswers);

            // Save result to database
            dbService.saveResult(studentId, result.totalQuestions, result.correctAnswers, result.scorePercent);

            this.dispose();
            resultService.showResultScreen(studentName, result);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not save result to database.\n\nError: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
