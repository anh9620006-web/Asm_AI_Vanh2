package com.example.aimentor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aimentor.R;

public class ProgrammingActivity extends AppCompatActivity {
    private Button btnBack;

    private Button btnQuiz;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programming);

        btnBack = findViewById(R.id.btnBack);
        btnQuiz = findViewById(R.id.btnSubmit);
        int lessonId = getIntent().getIntExtra("LESSON_ID", -1);
        String lessonTitle = getIntent().getStringExtra("LESSON_TITLE");
        String courseTitle = getIntent().getStringExtra("COURSE_TITLE");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnQuiz.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProgrammingActivity.this,
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

//// phe roi mn
