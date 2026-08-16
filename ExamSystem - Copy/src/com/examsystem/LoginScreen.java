package com.examsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * LoginScreen.java
 * ---------------------------------------------
 * This is the FIRST screen the student sees.
 * Student enters Name + Roll Number here.
 * On clicking "Start Exam", it:
 *   1) Saves/finds the student in the database
 *   2) Opens the ExamWindow (the actual test)
 *
 * Explained by: STUDENT 2 (Login / GUI part)
 * ---------------------------------------------
 */
public class LoginScreen extends JFrame {

    private JTextField nameField;
    private JTextField rollField;
    private DatabaseService dbService;

    public LoginScreen() {
        dbService = new DatabaseService();

        setTitle("Online Exam System - Student Login");
        setSize(420, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Online Exam System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        add(new JLabel("Student Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        add(nameField, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        add(new JLabel("Roll Number:"), gbc);
        gbc.gridx = 1;
        rollField = new JTextField(15);
        add(rollField, gbc);

        JButton startButton = new JButton("Start Exam");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        add(startButton, gbc);

        // When button is clicked -> validate input -> register student -> open exam
        startButton.addActionListener(e -> startExam());

        setVisible(true);
    }

    private void startExam() {
        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();

        if (name.isEmpty() || roll.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Name and Roll Number.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int studentId = dbService.registerStudent(name, roll);
            this.dispose(); // close login window
            new ExamWindow(studentId, name); // open the actual exam
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database Connection Failed.\n\nError: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
