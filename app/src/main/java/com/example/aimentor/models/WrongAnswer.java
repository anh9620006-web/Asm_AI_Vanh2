package com.example.aimentor.models;

import java.io.Serializable;

public class WrongAnswer implements Serializable {


    private String question;
    private String studentAnswer;
    private String correctAnswer;


    public String getQuestion() {
        return question;
    }


    public String getStudentAnswer() {
        return studentAnswer;
    }


    public String getCorrectAnswer() {
        return correctAnswer;
    }

}