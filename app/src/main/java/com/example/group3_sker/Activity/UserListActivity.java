package com.example.group3_sker.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group3_sker.R;
import com.example.group3_sker.Model.User;
import com.example.group3_sker.Adapter.UserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView userList;
    private List<User> users;
    private UserAdapter adapter;
    private DatabaseReference mDatabase;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("USER_ID", "User");

        userList = findViewById(R.id.user_list);
        userList.setLayoutManager(new LinearLayoutManager(this));

        users = new ArrayList<>();
        adapter = new UserAdapter(this, users, userId -> {
            // Handle click event
            Intent intent = new Intent(UserListActivity.this, ChatActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
        userList.setAdapter(adapter);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        loadUsers();
    }

    private void loadUsers() {
        mDatabase.child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> userIds = new HashSet<>();
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    String senderId = messageSnapshot.child("senderId").getValue(String.class);
                    String receiverId = messageSnapshot.child("receiverId").getValue(String.class);

                    if (senderId.equals(currentUserId)) {
                        userIds.add(receiverId);
                    } else if (receiverId.equals(currentUserId)) {
                        userIds.add(senderId);
                    }
                }

                fetchUsersDetails(userIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void fetchUsersDetails(Set<String> userIds) {
        users.clear(); // Clear existing user list

        // Fetch details for each user ID (senderId and receiverId)
        for (String userId : userIds) {
            // Assuming senderId and receiverId are directly under messages node
            DatabaseReference senderRef = mDatabase.child("messages").child(userId).child("senderId");
            DatabaseReference receiverRef = mDatabase.child("messages").child(userId).child("receiverId");

            // Fetch sender details
            senderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String senderId = snapshot.getValue(String.class);
                    fetchUserDetails(senderId);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });

            // Fetch receiver details
            receiverRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String receiverId = snapshot.getValue(String.class);
                    fetchUserDetails(receiverId);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    // Method to fetch details of a user based on their ID
    private void fetchUserDetails(String userId) {
        DatabaseReference userRef = mDatabase.child("users").child(userId); // Adjust this path as per your database structure
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    users.add(user); // Add user to the list
                    adapter.notifyDataSetChanged(); // Update RecyclerView
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
