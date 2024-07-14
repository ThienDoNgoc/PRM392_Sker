package com.example.group3_sker.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group3_sker.API.RetrofitClient;
import com.example.group3_sker.Adapter.UserAdapter;
import com.example.group3_sker.Model.User;
import com.example.group3_sker.R;
import com.example.group3_sker.API.UserApi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView userList;
    private List<User> users;
    private UserAdapter adapter;
    private DatabaseReference mDatabase;
    private String currentUserId;
    private UserApi userApi;
    private ImageView backBtn;
    private LinearLayout homeBtn, chatBtn, profileBtn;

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

        // Initialize Retrofit Client
        userApi = RetrofitClient.getUserApi();

        loadUsers();

        // Initialize and set listeners for new buttons
        backBtn = findViewById(R.id.backBtn);
        homeBtn = findViewById(R.id.homeBtn);
        chatBtn = findViewById(R.id.chatBtn);
        profileBtn = findViewById(R.id.profileBtn);

        backBtn.setOnClickListener(v -> onBackPressed());

        homeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(UserListActivity.this, MainAdminActivity.class);
            startActivity(intent);
            finish();
        });

        chatBtn.setOnClickListener(v -> {
            Intent intent = new Intent(UserListActivity.this, UserListActivity.class);
            startActivity(intent);
            finish();
        });

        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(UserListActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });
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
        users.clear();

        for (String userId : userIds) {
            fetchUserDetails(userId);
        }
    }

    private void fetchUserDetails(String userId) {
        Call<User> call = userApi.getUserById(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    users.add(user); // Add user to the list
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle error
            }
        });
    }
}
