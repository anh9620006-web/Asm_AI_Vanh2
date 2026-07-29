package com.example.aimentor.models;

import java.util.List;

public class SubmitRequest {

    private String quizId;
    private List<UserAnswer> userAnswers;

    public SubmitRequest() {
    }

    public SubmitRequest(String quizId, List<UserAnswer> userAnswers) {
        this.quizId = quizId;
        this.userAnswers = userAnswers;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public List<UserAnswer> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(List<UserAnswer> userAnswers) {
        this.userAnswers = userAnswers;
    }
}