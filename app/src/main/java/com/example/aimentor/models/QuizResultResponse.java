package com.example.aimentor.models;

import java.util.List;

public class QuizResultResponse {


    private int score;

    private int total;

    private double percentage;

    private String feedback;

    private List<WrongAnswer> wrongAnswers;



    public int getScore() {
        return score;
    }


    public int getTotal() {
        return total;
    }


    public double getPercentage() {
        return percentage;
    }


    public String getFeedback() {
        return feedback;
    }


    public List<WrongAnswer> getWrongAnswers() {
        return wrongAnswers;
    }

}