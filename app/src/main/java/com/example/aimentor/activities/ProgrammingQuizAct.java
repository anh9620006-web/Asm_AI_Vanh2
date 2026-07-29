package com.example.aimentor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aimentor.R;
import com.example.aimentor.models.QuestionModel;
import com.example.aimentor.models.QuizResponse;
import com.example.aimentor.network.ApiService;
import com.example.aimentor.network.RetrofitClient;
import java.util.ArrayList;

import com.example.aimentor.models.UserAnswer;
import com.example.aimentor.models.SubmitRequest;
import com.example.aimentor.models.QuizResultResponse;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;


public class ProgrammingQuizAct extends AppCompatActivity {


    private Button btnBack;
    private Button btnSubmit;

    private LinearLayout quizContainer;
    private String quizId;

    private List<QuestionModel> questionList;

    private final List<View> questionViews = new ArrayList<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_quiz);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvSubject = findViewById(R.id.tvSubject);
        String quiztitle = getIntent().getStringExtra("LESSON_TITLE");
        String subjectName = getIntent().getStringExtra("COURSE_TITLE");
        tvTitle.setText(quiztitle);
        tvSubject.setText(subjectName);

        quizContainer = findViewById(R.id.quizContainer);
        btnSubmit = findViewById(R.id.btnSave);

        btnSubmit.setOnClickListener(v -> {

            List<UserAnswer> answers = new ArrayList<>();

            boolean allAnswered = true;


            for (int i = 0; i < questionViews.size(); i++) {

                View view = questionViews.get(i);


                RadioButton radA = view.findViewById(R.id.radA);
                RadioButton radB = view.findViewById(R.id.radB);
                RadioButton radC = view.findViewById(R.id.radC);
                RadioButton radD = view.findViewById(R.id.radD);


                String selected = "";


                if (radA.isChecked())
                    selected = "A";

                else if (radB.isChecked())
                    selected = "B";

                else if (radC.isChecked())
                    selected = "C";

                else if (radD.isChecked())
                    selected = "D";


                // Nếu câu này chưa chọn đáp án
                if (selected.equals("")) {

                    allAnswered = false;
                    break;

                }


                answers.add(
                        new UserAnswer(
                                i,
                                selected
                        )
                );

            }


            if (!allAnswered) {

                Toast.makeText(
                        ProgrammingQuizAct.this,
                        "Please answer all questions before submitting!",
                        Toast.LENGTH_SHORT
                ).show();

                return;

            }


            submitQuiz(answers);

        });







        int lessonId =
                getIntent().getIntExtra(
                        "LESSON_ID",
                        1
                );


        loadQuiz(lessonId);

    }



    private void loadQuiz(int lessonId){


        ApiService api =
                RetrofitClient
                        .getClient()
                        .create(ApiService.class);



        api.getQuiz(lessonId)
                .enqueue(new Callback<QuizResponse>() {


                    @Override
                    public void onResponse(
                            Call<QuizResponse> call,
                            Response<QuizResponse> response) {


                        if(response.isSuccessful()
                                && response.body()!=null){


                            QuizResponse quizResponse = response.body();

                            quizId = quizResponse.getQuizId();

                            questionList = quizResponse.getQuestions();

                            showQuiz(questionList);



                        }else{

                            Toast.makeText(
                                    ProgrammingQuizAct.this,
                                    "Không lấy được quiz",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    }



                    @Override
                    public void onFailure(
                            Call<QuizResponse> call,
                            Throwable t) {


                        Toast.makeText(
                                ProgrammingQuizAct.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();

                    }
                });



    }




    private void showQuiz(List<QuestionModel> list){


        quizContainer.removeAllViews();
        questionViews.clear();


        int number = 1;


        for(QuestionModel quiz : list){


            View view =
                    LayoutInflater.from(this)
                            .inflate(
                                    R.layout.item_quiz_question,
                                    quizContainer,
                                    false
                            );



            TextView txtQuestion =
                    view.findViewById(
                            R.id.txtQuestion
                    );


            RadioButton radA =
                    view.findViewById(R.id.radA);

            RadioButton radB =
                    view.findViewById(R.id.radB);

            RadioButton radC =
                    view.findViewById(R.id.radC);

            RadioButton radD =
                    view.findViewById(R.id.radD);



            txtQuestion.setText(
                    number + ". " + quiz.question
            );


            radA.setText(
                    "A. " + quiz.answers.get(0).text
            );


            radB.setText(
                    "B. " + quiz.answers.get(1).text
            );


            radC.setText(
                    "C. " + quiz.answers.get(2).text
            );


            radD.setText(
                    "D. " + quiz.answers.get(3).text
            );



            quizContainer.addView(view);
            questionViews.add(view);


            number++;

        }

    }

    private void submitQuiz(List<UserAnswer> answers) {

        ApiService api =
                RetrofitClient
                        .getClient()
                        .create(ApiService.class);


        SubmitRequest request =
                new SubmitRequest(
                        quizId,
                        answers
                );

        Log.d("SUBMIT",
                new Gson().toJson(request));
        api.submitQuiz(request)
                .enqueue(new Callback<QuizResultResponse>() {

                    @Override
                    public void onResponse(
                            Call<QuizResultResponse> call,
                            Response<QuizResultResponse> response) {


                        if (response.isSuccessful()
                                && response.body() != null) {


                            QuizResultResponse result =
                                    response.body();


                            Intent intent =
                                    new Intent(
                                            ProgrammingQuizAct.this,
                                            QuizResultAct.class
                                    );


                            intent.putExtra(
                                    "SCORE",
                                    result.getScore()
                            );


                            intent.putExtra(
                                    "TOTAL",
                                    result.getTotal()
                            );


                            intent.putExtra(
                                    "PERCENTAGE",
                                    result.getPercentage()
                            );


                            intent.putExtra(
                                    "FEEDBACK",
                                    result.getFeedback()
                            );


                            intent.putExtra(
                                    "WRONG",
                                    new ArrayList<>(result.getWrongAnswers())
                            );


                            startActivity(intent);


                        } else {


                            Toast.makeText(
                                    ProgrammingQuizAct.this,
                                    "Submit failed - Code: "
                                            + response.code(),
                                    Toast.LENGTH_SHORT
                            ).show();


                        }


                    }




                    @Override
                    public void onFailure(
                            Call<QuizResultResponse> call,
                            Throwable t) {


                        Toast.makeText(
                                ProgrammingQuizAct.this,
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();


                    }

                });

    }

}