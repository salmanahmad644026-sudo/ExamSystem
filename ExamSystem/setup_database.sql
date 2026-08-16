-- ============================================
-- PROJECT 8: ONLINE EXAM SYSTEM WITH AUTO-GRADING
-- Database Setup Script
-- ============================================

CREATE DATABASE IF NOT EXISTS exam_db;
USE exam_db;

-- Table 1: Stores student information
CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    roll_no VARCHAR(50) NOT NULL UNIQUE
);

-- Table 2: Stores MCQ questions, options, and the correct answer
CREATE TABLE IF NOT EXISTS questions (
    question_id INT AUTO_INCREMENT PRIMARY KEY,
    subject VARCHAR(50) NOT NULL,
    question_text VARCHAR(500) NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option CHAR(1) NOT NULL   -- 'A', 'B', 'C', or 'D'
);

-- Table 3: Stores the final result for every exam attempt
CREATE TABLE IF NOT EXISTS results (
    result_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    total_questions INT NOT NULL,
    correct_answers INT NOT NULL,
    score DOUBLE NOT NULL,
    exam_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id)
);

-- ============================================
-- SAMPLE MCQ QUESTIONS (Java subject)
-- ============================================
INSERT INTO questions (subject, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES
('Java', 'Which keyword is used to inherit a class in Java?', 'implements', 'extends', 'inherits', 'super', 'B'),
('Java', 'Which of these is NOT a primitive data type in Java?', 'int', 'float', 'String', 'boolean', 'C'),
('Java', 'Which method is the entry point of a Java program?', 'start()', 'main()', 'run()', 'init()', 'B'),
('Java', 'Which keyword prevents a class from being inherited?', 'static', 'private', 'final', 'protected', 'C'),
('Java', 'What is the default value of a boolean variable in Java?', 'true', 'false', '0', 'null', 'B'),
('Java', 'Which package contains the Scanner class?', 'java.io', 'java.util', 'java.lang', 'java.sql', 'B'),
('Java', 'Which of these is used for exception handling?', 'try-catch', 'if-else', 'for-loop', 'switch-case', 'A'),
('Java', 'What does JDBC stand for?', 'Java Database Connectivity', 'Java Direct Binary Code', 'Java Data Base Class', 'Java Driver Base Connector', 'A'),
('Java', 'Which collection class allows duplicate elements and maintains insertion order?', 'HashSet', 'ArrayList', 'TreeSet', 'HashMap', 'B'),
('Java', 'Which operator is used to compare two values in Java?', '=', '==', '===', '=>', 'B');

-- Verify data
SELECT * FROM questions;
