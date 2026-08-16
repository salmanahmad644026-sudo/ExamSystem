package com.examsystem;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * ResultService.java
 * ---------------------------------------------
 * This class contains the AUTO-GRADING LOGIC
 * (the most important feature of Project 8) and
 * the final Result screen (RESULT GENERATION).
 *
 * Explained by: STUDENT 4 (Auto-Grading Logic part)
 *           and STUDENT 5 (Result Generation part)
 * ---------------------------------------------
 */
public class ResultService {

    /**
     * Simple data holder to carry grading output back to ExamWindow.
     */
    public static class GradeResult {
        public int totalQuestions;
        public int correctAnswers;
        public double scorePercent;

        public GradeResult(int totalQuestions, int correctAnswers, double scorePercent) {
            this.totalQuestions = totalQuestions;
            this.correctAnswers = correctAnswers;
            this.scorePercent = scorePercent;
        }
    }

    /**
     * ============ AUTO-GRADING LOGIC (STUDENT 4 explains this) ============
     * For every question, compare the student's selected option
     * with the correct_option stored in the database.
     * If they match -> +1 correct answer.
     * Finally calculate percentage score.
     * =======================================================================
     */
    public GradeResult gradeExam(List<Question> questions, Map<Integer, String> selectedAnswers) {
        int total = questions.size();
        int correct = 0;

        for (Question q : questions) {
            String studentAnswer = selectedAnswers.get(q.getId()); // may be null if unanswered
            String correctAnswer = q.getCorrectOption();

            if (studentAnswer != null && studentAnswer.equalsIgnoreCase(correctAnswer)) {
                correct++;
            }
        }

        double percentage = (total == 0) ? 0 : ((double) correct / total) * 100.0;
        return new GradeResult(total, correct, percentage);
    }

    /**
     * ============ RESULT GENERATION (STUDENT 5 explains this) ============
     * Shows a clean summary window with:
     *   - Total questions
     *   - Correct answers
     *   - Score percentage
     *   - Pass / Fail status
     * ========================================================================
     */
    public void showResultScreen(String studentName, GradeResult result) {
        JFrame resultFrame = new JFrame("Exam Result");
        resultFrame.setSize(400, 300);
        resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resultFrame.setLocationRelativeTo(null);
        resultFrame.setLayout(new GridLayout(6, 1, 5, 5));

        JLabel title = new JLabel("Exam Completed!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel nameLabel = new JLabel("Student: " + studentName, SwingConstants.CENTER);
        JLabel totalLabel = new JLabel("Total Questions: " + result.totalQuestions, SwingConstants.CENTER);
        JLabel correctLabel = new JLabel("Correct Answers: " + result.correctAnswers, SwingConstants.CENTER);

        JLabel scoreLabel = new JLabel(String.format("Score: %.2f%%", result.scorePercent), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));

        boolean passed = result.scorePercent >= 40.0; // pass mark = 40%
        JLabel statusLabel = new JLabel(passed ? "Status: PASS" : "Status: FAIL", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setForeground(passed ? new Color(0, 150, 0) : Color.RED);

        resultFrame.add(title);
        resultFrame.add(nameLabel);
        resultFrame.add(totalLabel);
        resultFrame.add(correctLabel);
        resultFrame.add(scoreLabel);
        resultFrame.add(statusLabel);

        resultFrame.setVisible(true);
    }
}
