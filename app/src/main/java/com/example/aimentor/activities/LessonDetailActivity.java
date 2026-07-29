package com.example.aimentor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aimentor.R;

public class LessonDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programming);

        Button btnBack = findViewById(R.id.btnBack);
        TextView tvCourseTitle = findViewById(R.id.tvCourseTitle);
        TextView tvLessonTitle = findViewById(R.id.tvLessonTitle);
        TextView tvLessonContent = findViewById(R.id.tvLessonContent);

        Button btnQuiz = findViewById(R.id.btnSubmit);


        String courseTitle = getIntent().getStringExtra("COURSE_TITLE");
        String lessonTitle = getIntent().getStringExtra("LESSON_TITLE");
        String lessonContent = getIntent().getStringExtra("LESSON_CONTENT");

        int lessonId = getIntent().getIntExtra("LESSON_ID", -1);


        tvCourseTitle.setText(courseTitle);
        tvLessonTitle.setText(lessonTitle);
        tvLessonContent.setText(lessonContent);


        btnBack.setOnClickListener(v -> finish());


        btnQuiz.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LessonDetailActivity.this,
                    ProgrammingQuizAct.class
            );

            intent.putExtra(
                    "LESSON_ID",
                    lessonId
            );
            intent.putExtra(
                    "LESSON_TITLE",
                    lessonTitle
            );
            intent.putExtra(
                    "COURSE_TITLE",
                    courseTitle
            );

            startActivity(intent);

        });
    }
}