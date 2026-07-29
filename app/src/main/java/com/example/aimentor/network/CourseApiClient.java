package com.example.aimentor.network;

import com.example.aimentor.models.CourseModel;
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

public class CourseApiClient {
    // 10.0.2.2 la localhost cua may tinh khi chay tren Android Emulator.
    private static final String COURSES_URL = "https://defile-gecko-renderer.ngrok-free.dev/api/courses";

    public List<CourseModel> fetchCourses() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(COURSES_URL).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        try {
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IllegalStateException("API response: " + connection.getResponseCode());
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) response.append(line);
            }

            JSONArray jsonCourses = new JSONArray(response.toString());
            List<CourseModel> courses = new ArrayList<>();
            for (int i = 0; i < jsonCourses.length(); i++) {
                JSONObject item = jsonCourses.getJSONObject(i);
                courses.add(new CourseModel(
                        item.getInt("id"),
                        item.getString("title"),
                        item.isNull("description") ? null : item.getString("description"),
                        item.isNull("thumbnailUrl") ? null : item.getString("thumbnailUrl"),
                        item.optBoolean("isPublished", true),
                        item.optString("updatedAt", "")
                ));
            }
            return courses;
        } finally {
            connection.disconnect();
        }
    }
}


