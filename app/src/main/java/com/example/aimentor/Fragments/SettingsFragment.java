package com.example.aimentor.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aimentor.R;
import com.example.aimentor.activities.LoginActivity;
import com.example.aimentor.models.UserModel;
import com.example.aimentor.repository.UserRepository;
import com.google.android.material.textfield.TextInputLayout;

public class SettingsFragment extends Fragment {

    private TextView tvUsername, tvEmail, tvPhone, tvPassword;
    private Button btnToggleChangePassword, btnUpdatePassword, btnLogout;
    private LinearLayout layoutChangePassword;
    private EditText edtOldPassword, edtNewPassword;
    private TextInputLayout tilOldPassword, tilNewPassword;
    private UserRepository userRepository;
    private int userId;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize views
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvPassword = view.findViewById(R.id.tvPassword);
        btnToggleChangePassword = view.findViewById(R.id.btnToggleChangePassword);
        btnUpdatePassword = view.findViewById(R.id.btnUpdatePassword);
        btnLogout = view.findViewById(R.id.btnLogout);
        layoutChangePassword = view.findViewById(R.id.layoutChangePassword);
        
        edtOldPassword = view.findViewById(R.id.edtOldPassword);
        edtNewPassword = view.findViewById(R.id.edtNewPassword);
        tilOldPassword = view.findViewById(R.id.tilOldPassword);
        tilNewPassword = view.findViewById(R.id.tilNewPassword);

        userRepository = new UserRepository(getContext());
        
        if (getActivity() != null) {
            SharedPreferences sharedPf = getActivity().getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
            userId = sharedPf.getInt("ID_USER", -1);

            loadUserInfo();

            btnToggleChangePassword.setOnClickListener(v -> {
                if (layoutChangePassword.getVisibility() == View.VISIBLE) {
                    layoutChangePassword.setVisibility(View.GONE);
                } else {
                    layoutChangePassword.setVisibility(View.VISIBLE);
                }
            });

            // Clear errors when typing
            edtOldPassword.addTextChangedListener(new SimpleTextWatcher(tilOldPassword));
            edtNewPassword.addTextChangedListener(new SimpleTextWatcher(tilNewPassword));

            btnUpdatePassword.setOnClickListener(v -> {
                String oldPass = edtOldPassword.getText().toString().trim();
                String newPass = edtNewPassword.getText().toString().trim();

                tilOldPassword.setError(null);
                tilNewPassword.setError(null);

                if (TextUtils.isEmpty(oldPass)) {
                    tilOldPassword.setError("Current password is required");
                    return;
                }
                if (TextUtils.isEmpty(newPass)) {
                    tilNewPassword.setError("New password is required");
                    return;
                }
                if (newPass.length() < 6) {
                    tilNewPassword.setError("Password must be at least 6 characters");
                    return;
                }

                UserModel user = userRepository.getUserById(userId);
                if (user != null && user.getId() > 0) {
                    if (!user.getPassword().equals(oldPass)) {
                        tilOldPassword.setError("Incorrect current password");
                    } else {
                        if (userRepository.updatePassword(userId, newPass)) {
                            Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                            edtOldPassword.setText("");
                            edtNewPassword.setText("");
                            layoutChangePassword.setVisibility(View.GONE);
                            loadUserInfo();
                        } else {
                            Toast.makeText(getContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Error: User not found", Toast.LENGTH_SHORT).show();
                }
            });

            btnLogout.setOnClickListener(v -> {
                SharedPreferences.Editor editor = sharedPf.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            });
        }

        return view;
    }

    private void loadUserInfo() {
        if (userId != -1) {
            UserModel user = userRepository.getUserById(userId);
            if (user != null && user.getId() > 0) {
                tvUsername.setText(user.getUsername());
                tvEmail.setText(user.getEmail());
                tvPhone.setText(user.getPhone() != null ? user.getPhone() : "N/A");
                tvPassword.setText("********");
            } else {
                Toast.makeText(getContext(), "Error loading profile info", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static class SimpleTextWatcher implements TextWatcher {
        private final TextInputLayout layout;

        public SimpleTextWatcher(TextInputLayout layout) {
            this.layout = layout;
        }

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
            layout.setError(null);
        }
        @Override public void afterTextChanged(Editable s) {}
    }
}
