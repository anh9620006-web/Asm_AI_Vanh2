package com.example.aimentor.models;

public class UserAnswer {

    private int questionIndex;
    private String selectedAnswer;

    public UserAnswer() {
    }

    public UserAnswer(int questionIndex, String selectedAnswer) {
        this.questionIndex = questionIndex;
        this.selectedAnswer = selectedAnswer;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }
}