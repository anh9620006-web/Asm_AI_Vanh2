package com.example.aimentor.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.aimentor.R;
import com.example.aimentor.models.WrongAnswer;


import java.util.List;


public class WrongAnswerAdapter
        extends RecyclerView.Adapter<WrongAnswerAdapter.ViewHolder>{


    private List<WrongAnswer> list;


    public WrongAnswerAdapter(
            List<WrongAnswer> list){

        this.list = list;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType){


        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_wrong_answer,
                                parent,
                                false
                        );


        return new ViewHolder(view);

    }



    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position){


        WrongAnswer item =
                list.get(position);


        holder.question.setText(
                "Question: "
                        + item.getQuestion()
        );


        holder.student.setText(
                "Your answer: "
                        + item.getStudentAnswer()
        );


        holder.correct.setText(
                "Correct answer: "
                        + item.getCorrectAnswer()
        );


    }



    @Override
    public int getItemCount(){

        return list.size();

    }



    static class ViewHolder
            extends RecyclerView.ViewHolder{


        TextView question;
        TextView student;
        TextView correct;


        public ViewHolder(View itemView){

            super(itemView);


            question =
                    itemView.findViewById(
                            R.id.txtWrongQuestion
                    );


            student =
                    itemView.findViewById(
                            R.id.txtStudentAnswer
                    );


            correct =
                    itemView.findViewById(
                            R.id.txtCorrectAnswer
                    );

        }

    }

}