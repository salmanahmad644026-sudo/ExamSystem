package com.examsystem;

/**
 * Question.java
 * ---------------------------------------------
 * This is a MODEL class. It simply holds the data
 * for ONE MCQ question fetched from the database.
 *
 * Explained by: STUDENT 3 (Exam Window & Timer part)
 * ---------------------------------------------
 */
public class Question {

    private int id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption; // "A", "B", "C", or "D"

    public Question(int id, String questionText, String optionA, String optionB,
                     String optionC, String optionD, String correctOption) {
        this.id = id;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
    }

    public int getId() { return id; }
    public String getQuestionText() { return questionText; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectOption() { return correctOption; }
}
