package com.example.aimentor.Fragments;

import com.example.aimentor.models.ChatRequest;
import com.example.aimentor.models.ChatResponse;
import com.example.aimentor.network.ApiService;
import com.example.aimentor.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aimentor.R;
import com.example.aimentor.adapters.ChatAdapter;
import com.example.aimentor.models.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment {

    private ApiService apiService;

    private RecyclerView chatRecyclerView;
    private EditText editMessage;
    private ImageButton sendButton;

    private List<ChatMessage> messages;
    private ChatAdapter chatAdapter;

    public QuizFragment() {
        // Constructor rỗng bắt buộc cho Fragment
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.fragment_quiz,
                container,
                false
        );

        chatRecyclerView = view.findViewById(
                R.id.chatRecyclerView
        );

        editMessage = view.findViewById(
                R.id.editMessage
        );

        sendButton = view.findViewById(
                R.id.sendButton
        );

        setupChat();
        setupSendButton();
        apiService = RetrofitClient.getClient()
                .create(ApiService.class);

        return view;
    }

    private void setupChat() {
        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireContext());

        layoutManager.setStackFromEnd(true);

        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);

        addMessage(
                "Xin chào! Bạn muốn hỏi gì về bài học?",
                ChatMessage.TYPE_AI
        );
    }

    private void setupSendButton() {

        sendButton.setOnClickListener(view -> {

            String content = editMessage
                    .getText()
                    .toString()
                    .trim();


            if (content.isEmpty()) {

                editMessage.setError(
                        "Hãy nhập nội dung tin nhắn"
                );

                return;
            }


            // Hiển thị tin nhắn người dùng
            addMessage(
                    content,
                    ChatMessage.TYPE_USER
            );


            editMessage.setText("");


            // Gửi lên AI
            sendMessageToAI(content);

        });
    }

    private void sendMessageToAI(String message) {


        ChatRequest request =
                new ChatRequest(message);


        apiService.chat(request)
                .enqueue(new Callback<ChatResponse>() {


                    @Override
                    public void onResponse(
                            Call<ChatResponse> call,
                            Response<ChatResponse> response) {


                        Log.d("AI_STATUS", "Code: " + response.code());


                        if(response.isSuccessful() && response.body()!=null){

                            Log.d("AI_DATA",
                                    response.body().getResponse());


                            addMessage(
                                    response.body().getResponse(),
                                    ChatMessage.TYPE_AI
                            );

                        }
                        else {

                            try {
                                Log.e(
                                        "AI_ERROR",
                                        response.errorBody().string()
                                );
                            }
                            catch(Exception e){}


                            addMessage(
                                    "HTTP lỗi: " + response.code(),
                                    ChatMessage.TYPE_AI
                            );
                        }
                    }


                    @Override
                    public void onFailure(
                            Call<ChatResponse> call,
                            Throwable t) {


                        addMessage(
                                "Lỗi kết nối AI: " + t.getMessage(),
                                ChatMessage.TYPE_AI
                        );

                    }

                });

    }

    private void addMessage(String content, int type) {
        ChatMessage message =
                new ChatMessage(content, type);

        messages.add(message);

        int newPosition = messages.size() - 1;

        chatAdapter.notifyItemInserted(newPosition);
        chatRecyclerView.scrollToPosition(newPosition);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        chatRecyclerView = null;
        editMessage = null;
        sendButton = null;
        chatAdapter = null;
        messages = null;
    }
}