package com.example.aimentor.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aimentor.R;
import com.example.aimentor.activities.MenuActivity;

public class HomeFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;



    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView tvWelcome = view.findViewById(R.id.tvWelcome);

        SharedPreferences sharedPf = getActivity().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        String username = sharedPf.getString("USERNAME_USER", "user"); 

        tvWelcome.setText("Welcom " + username);

        // My Subjects Button - Redirects to Category Tab (Index 1)
        Button btnMySubjects = view.findViewById(R.id.btnMySubjects);
        btnMySubjects.setOnClickListener(v -> {
            if (getActivity() instanceof MenuActivity) {
                ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
                if (viewPager != null) {
                    viewPager.setCurrentItem(1);
                }
            }
        });

        // Chat with AI Button - Redirects to Quiz Tab (Index 2)
        Button btnChatBot = view.findViewById(R.id.btnChatBot);
        btnChatBot.setOnClickListener(v -> {
            if (getActivity() instanceof MenuActivity) {
                ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
                if (viewPager != null) {
                    viewPager.setCurrentItem(2);
                }
            }
        });

        return view;
    }
}