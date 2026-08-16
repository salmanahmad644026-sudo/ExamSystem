# Project 8: Online Exam System with Auto-Grading

## How to Run
1. Install MySQL and run `setup_database.sql` to create the `exam_db` database.
2. Download the MySQL JDBC Connector (mysql-connector-j) jar and add it to your project's classpath/libraries.
3. Open `DatabaseService.java` and update the PASSWORD field to your MySQL root password.
4. Compile and run `Main.java`.

## Project Structure
- `Question.java` - model class for one MCQ
- `DatabaseService.java` - all database (MySQL) operations
- `LoginScreen.java` - student login/registration screen
- `ExamWindow.java` - MCQ exam screen with timer
- `ResultService.java` - auto-grading logic + result screen
- `Main.java` - entry point
- `setup_database.sql` - database + sample questions

## Features
- MCQ exam portal (GUI built with Java Swing)
- Timer system (5-minute countdown, auto-submits on timeout)
- Automatic grading (compares student answers to correct answers in DB)
- Result generation (score %, pass/fail, saved to database)
