package com.example.group3_sker.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.group3_sker.R;
import com.example.group3_sker.Model.Message;
import com.example.group3_sker.Adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private RecyclerView messageList;
    private EditText messageInput;
    private Button sendButton;
    private List<Message> messages;
    private MessageAdapter adapter;

    private String username;

    private String currentUserId = "current_user_id";
    //private String chatWithUserId = "156444ac-b2eb-4242-6a73-08dc9f5f3802";

    private String chatWithUserId = "2b2a5cad-fb48  -497c-973d-08dc9f167606";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("USER_ID", "User");
        username = sharedPreferences.getString("USERNAME", "User");

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI elements
        messageList = findViewById(R.id.message_list);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);

        // Initialize message list and adapter
        messages = new ArrayList<>();
        adapter = new MessageAdapter(this, messages, currentUserId);
        messageList.setLayoutManager(new LinearLayoutManager(this));
        messageList.setAdapter(adapter);

        // Set up send button click listener
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // Load messages from Firebase
        loadMessages();
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (!text.isEmpty()) {
            Message message = new Message(username, currentUserId, chatWithUserId, text);
            mDatabase.child("messages").push().setValue(message);
            messageInput.setText("");
        }
    }

    private void loadMessages() {
        mDatabase.child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Message message = postSnapshot.getValue(Message.class);
                    if ((message.getSenderId().equals(currentUserId) && message.getReceiverId().equals(chatWithUserId)) ||
                            (message.getSenderId().equals(chatWithUserId) && message.getReceiverId().equals(currentUserId))) {
                        messages.add(message);
                    }
                }
                adapter.notifyDataSetChanged();
                messageList.scrollToPosition(messages.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
