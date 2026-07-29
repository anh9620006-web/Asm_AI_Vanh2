package com.example.aimentor.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QuizResponse {

    @SerializedName("quizId")
    private String quizId;

    @SerializedName("questions")
    public List<QuestionModel> questions;

    public String getQuizId() {
        return quizId;
    }

    public List<QuestionModel> getQuestions() {
        return questions;
    }
}