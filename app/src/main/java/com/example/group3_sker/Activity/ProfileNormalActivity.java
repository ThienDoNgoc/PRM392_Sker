package com.example.group3_sker.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.group3_sker.R;
import com.google.android.material.bottomappbar.BottomAppBar;

public class ProfileNormalActivity extends AppCompatActivity {

    private TextView userName, userPhone, userEmail;
    private LinearLayout logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_normal);

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
            Intent intent = new Intent(ProfileNormalActivity.this, MainActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.chatBtn).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileNormalActivity.this, ChatActivity.class);
            startActivity(intent);
        });


        findViewById(R.id.cartBtn).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileNormalActivity.this, CartActivity.class);
            startActivity(intent);
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
        Intent intent = new Intent(ProfileNormalActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}