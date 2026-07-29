package com.example.aimentor.network;

import com.example.aimentor.models.QuestionModel;
import com.example.aimentor.models.QuizResponse;
import com.example.aimentor.models.AnswerModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QuizApiClient {

    private static final String QUIZ_URL =
            "https://defile-gecko-renderer.ngrok-free.dev/api/quiz/";


    public QuizResponse fetchQuiz(int lessonId) throws Exception {


        URL url = new URL(QUIZ_URL + lessonId);


        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();


        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);


        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                connection.getInputStream(),
                                StandardCharsets.UTF_8
                        )
                );


        StringBuilder result = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null){
            result.append(line);
        }


        JSONObject json =
                new JSONObject(result.toString());


        JSONArray array =
                json.getJSONArray("questions");


        List<QuestionModel> list = new ArrayList<>();


        for(int i = 0; i < array.length(); i++){

            JSONObject q =
                    array.getJSONObject(i);


            JSONArray answers =
                    q.getJSONArray("answers");


            List<AnswerModel> answerList =
                    new ArrayList<>();


            for(int j = 0; j < answers.length(); j++){

                JSONObject a =
                        answers.getJSONObject(j);

                answerList.add(
                        new AnswerModel(
                                a.getString("key"),
                                a.getString("text")
                        )
                );
            }


            QuestionModel quiz =
                    new QuestionModel(
                            q.getString("question"),
                            answerList,
                            q.getString("correctAnswer"),
                            q.getString("explanation")
                    );

            list.add(quiz);
        }


        QuizResponse response =
                new QuizResponse();

        response.questions = list;


        return response;
    }
}