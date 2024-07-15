package com.example.group3_sker.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group3_sker.R;
import com.google.android.material.bottomappbar.BottomAppBar;

public class ProfileActivity extends AppCompatActivity {

    private TextView userName, userPhone, userEmail;
    private LinearLayout logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        userName = findViewById(R.id.userName);
        userPhone = findViewById(R.id.userPhone);
        userEmail = findViewById(R.id.userEmail);
        logOut = findViewById(R.id.logoutLn);

        // Load user data
        loadUserData();

        // Back button
        ImageView backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(v -> finish());

        logOut.setOnClickListener(v -> {
            // Handle Log Out click
            logOutUser();
        });

        // Handle Bottom Navigation
        findViewById(R.id.homeBtn).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainAdminActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.chatBtn).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, UserListActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.profileBtn).setOnClickListener(v -> {
            // Already in ProfileActivity, no need to navigate
        });
    }

    private void loadUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("USERNAME", "N/A");
        String phone = sharedPreferences.getString("PHONE", "N/A");
        String email = sharedPreferences.getString("EMAIL", "N/A");

        userName.setText(username);
        userPhone.setText(phone);
        userEmail.setText(email);
    }

    private void logOutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Navigate to LoginActivity
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
