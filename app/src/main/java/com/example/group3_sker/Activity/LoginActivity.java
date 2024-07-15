package com.example.group3_sker.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group3_sker.API.RetrofitClient;
import com.example.group3_sker.API.UserApi;
import com.example.group3_sker.Model.LoginRequest;
import com.example.group3_sker.Model.LoginResponse;
import com.example.group3_sker.Model.User;
import com.example.group3_sker.R;
import com.google.firebase.FirebaseApp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize RetrofitClient with context
        RetrofitClient.initialize(this);
        FirebaseApp.initializeApp(this);

        usernameEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.btn_signin);
        signUpTextView = findViewById(R.id.tv_signup);

        signUpTextView.setOnClickListener(v -> {
            // Navigate to sign-in activity
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                loginUser(username, password);
            }
        });
    }

    private void loginUser(String username, String password) {
        UserApi userApi = RetrofitClient.getUserApi();
        LoginRequest loginRequest = new LoginRequest(username, password);

        Call<LoginResponse> call = userApi.loginUser(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    saveToken(token);
                    getCurrentUser();  // Fetch current user details
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("JWT_TOKEN", token);
        editor.apply();
    }

    private void getCurrentUser() {
        UserApi userApi = RetrofitClient.getUserApi();
        Call<User> call = userApi.getUser();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    saveUserIdAndUsernameAndAddress(user.getId(), user.getUsername(), user.getAddress());
                    // Redirect to MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserIdAndUsernameAndAddress(String userId, String username, String address) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_ID", userId);
        editor.putString("USERNAME", username);
        editor.putString("ADDRESS", address);
        editor.apply();
    }
}
