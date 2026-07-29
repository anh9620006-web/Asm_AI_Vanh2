package com.example.aimentor.network;

import com.example.aimentor.models.ChatRequest;
import com.example.aimentor.models.ChatResponse;
import com.example.aimentor.models.QuizResponse;
import com.example.aimentor.models.QuizResultResponse;
import com.example.aimentor.models.SubmitRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("api/chat")
    Call<ChatResponse> chat(@Body ChatRequest request);

    @GET("api/quiz/{lessonId}")
    Call<QuizResponse> getQuiz(
            @Path("lessonId") int lessonId
    );

    @POST("api/quiz/submit")
    Call<QuizResultResponse> submitQuiz(@Body SubmitRequest request);

}