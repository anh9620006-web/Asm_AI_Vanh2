package com.example.aimentor.network;

import com.example.aimentor.models.LessonModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LessonApiClient {

    private static final String COURSES_URL =
            "https://defile-gecko-renderer.ngrok-free.dev/api/courses";

    public List<LessonModel> fetchLessons(int courseId) throws Exception {

        URL url = new URL(COURSES_URL + "/" + courseId + "/lessons");

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        try {

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IllegalStateException(
                        "API response: " + connection.getResponseCode()
                );
            }

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream(),
                                    StandardCharsets.UTF_8));

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            JSONArray jsonLessons =
                    new JSONArray(response.toString());

            List<LessonModel> lessons = new ArrayList<>();

            for(int i = 0; i < jsonLessons.length(); i++){

                JSONObject item = jsonLessons.getJSONObject(i);

                lessons.add(new LessonModel(
                        item.getInt("id"),
                        item.getInt("courseId"),
                        item.getString("title"),
                        item.getString("content"),
                        item.getInt("position"),
                        item.optString("updatedAt", "")
                ));
            }

            return lessons;

        } finally {
            connection.disconnect();
        }
    }
}
