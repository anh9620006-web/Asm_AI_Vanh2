package com.example.aimentor.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimentor.R;
import com.example.aimentor.adapters.WrongAnswerAdapter;
import com.example.aimentor.models.WrongAnswer;

import java.util.ArrayList;

public class QuizResultAct extends AppCompatActivity {
    private TextView txtScore;
    private TextView txtPercentage;

    private Button btnBackLesson;
    private TextView txtFeedback;

    private RecyclerView wrongRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_awser_quiz);


        txtScore = findViewById(R.id.txtScore);
        txtPercentage = findViewById(R.id.txtPercentage);
        txtFeedback = findViewById(R.id.txtFeedback);

        btnBackLesson =
                findViewById(R.id.btnBackLesson);


        btnBackLesson.setOnClickListener(v -> {

            finish();

        });


        int score =
                getIntent().getIntExtra(
                        "SCORE",
                        0
                );


        int total =
                getIntent().getIntExtra(
                        "TOTAL",
                        0
                );


        double percentage =
                getIntent().getDoubleExtra(
                        "PERCENTAGE",
                        0
                );


        String feedback =
                getIntent().getStringExtra(
                        "FEEDBACK"
                );


        txtScore.setText(
                "Score: "
                        + score
                        + "/"
                        + total
        );


        txtPercentage.setText(
                "Percentage: "
                        + percentage
                        + "%"
        );


//        txtFeedback.setText(
//                feedback
//        );

        wrongRecyclerView =
                findViewById(R.id.wrongRecyclerView);


        ArrayList<WrongAnswer> wrongList =
                (ArrayList<WrongAnswer>)
                        getIntent()
                                .getSerializableExtra("WRONG");


        WrongAnswerAdapter adapter =
                new WrongAnswerAdapter(wrongList);


        wrongRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );


        wrongRecyclerView.setAdapter(adapter);

    }
}
