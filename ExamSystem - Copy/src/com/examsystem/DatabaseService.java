package com.examsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseService.java
 * ---------------------------------------------
 * This class handles ALL communication with MySQL.
 * Every other class calls methods from here instead
 * of writing SQL directly. This is called the
 * "Service Layer" or "DAO (Data Access Object)" pattern.
 *
 * Explained by: STUDENT 1 (Database & Setup part)
 * ---------------------------------------------
 *
 * IMPORTANT (tell examiner):
 * - URL, USER, PASSWORD below MUST match your local MySQL.
 * - Database name must be exam_db (created by setup_database.sql)
 */
public class DatabaseService {

    // ---- EDIT THESE 3 LINES TO MATCH YOUR MYSQL SETUP ----
    private static final String URL = "jdbc:mysql://localhost:3306/exam_db";
    private static final String USER = "root";
    private static final String PASSWORD = "YOUR_MYSQL_PASSWORD"; // <-- change this
    // -------------------------------------------------------

    // Opens and returns a fresh connection to MySQL
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Registers a new student before the exam starts (or reuses
     * the student if the roll number already exists).
     * Returns the student_id to be used later while saving results.
     */
    public int registerStudent(String name, String rollNo) throws SQLException {
        String checkSql = "SELECT student_id FROM students WHERE roll_no = ?";
        String insertSql = "INSERT INTO students (name, roll_no) VALUES (?, ?)";

        try (Connection conn = getConnection()) {
            try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                check.setString(1, rollNo);
                ResultSet rs = check.executeQuery();
                if (rs.next()) {
                    return rs.getInt("student_id"); // already registered
                }
            }
            try (PreparedStatement insert = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insert.setString(1, name);
                insert.setString(2, rollNo);
                insert.executeUpdate();
                ResultSet keys = insert.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Could not register student.");
    }

    /**
     * Fetches ALL MCQ questions from the database.
     * This is what fills the exam paper.
     */
    public List<Question> getAllQuestions() throws SQLException {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM questions";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Question q = new Question(
                        rs.getInt("question_id"),
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_option")
                );
                list.add(q);
            }
        }
        return list;
    }

    /**
     * Saves the final exam result into the results table.
     * This is called ONCE, right after auto-grading finishes.
     */
    public void saveResult(int studentId, int totalQuestions, int correctAnswers, double score) throws SQLException {
        String sql = "INSERT INTO results (student_id, total_questions, correct_answers, score) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, totalQuestions);
            stmt.setInt(3, correctAnswers);
            stmt.setDouble(4, score);
            stmt.executeUpdate();
        }
    }
}
