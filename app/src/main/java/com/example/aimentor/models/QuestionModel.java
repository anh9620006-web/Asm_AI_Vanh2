package com.example.aimentor.models;

import java.util.List;

public class QuestionModel {

    public String question;

    public List<AnswerModel> answers;

    public String correctAnswer;

    public String explanation;


    // Constructor rỗng cho Gson
    public QuestionModel() {

    }


    public QuestionModel(
            String question,
            List<AnswerModel> answers,
            String correctAnswer,
            String explanation
    ){
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }

}