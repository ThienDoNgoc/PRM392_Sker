package com.example.group3_sker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.group3_sker.API.RetrofitClient;
import com.example.group3_sker.API.UserApi;
import com.example.group3_sker.Model.User;
import com.example.group3_sker.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText, fullnameEditText, passwordEditText, confirmPasswordEditText, emailEditText, phoneEditText;
    private Button signUpButton;
    private TextView signInTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        usernameEditText = findViewById(R.id.et_userName);
        fullnameEditText = findViewById(R.id.et_name);
        emailEditText = findViewById(R.id.et_email);
        phoneEditText = findViewById(R.id.et_phone);
        passwordEditText = findViewById(R.id.et_password);
        confirmPasswordEditText = findViewById(R.id.et_passwordConfirm);
        signUpButton = findViewById(R.id.signUpBtn);
        signInTextView = findViewById(R.id.tv_signin);

        // Set click listener for sign-up button
        signUpButton.setOnClickListener(v -> handleSignUp());

        // Set click listener for sign-in text view
        signInTextView.setOnClickListener(v -> {
            // Navigate to sign-in activity
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void handleSignUp() {
        // Get user input
        String username = usernameEditText.getText().toString().trim();
        String fullname = fullnameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validate user input
        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError("Username is required");
            return;
        }
        if (TextUtils.isEmpty(fullname)) {
            fullnameEditText.setError("Full name is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }
        if (!isValidEmail(email)) {
            emailEditText.setError("Invalid email format");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            phoneEditText.setError("Phone number is required");
            return;
        }
        if (!isValidPhone(phone)) {
            phoneEditText.setError("Phone number must be 10 digits");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }
        if (!isValidPassword(password)) {
            passwordEditText.setError("Password must contain at least 1 number, 1 lowercase letter, and 1 uppercase letter");
            return;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return;
        }

        // Create a new user object
        User user = new User(username, password, email, fullname, "FPTU", phone, "string", "string");

        registerUser(user);
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private boolean isValidPhone(String phone) {
        return phone.length() == 10 && phone.matches("[0-9]+");
    }

    private boolean isValidPassword(String password) {
        // Password must have 6 letter and contain at least one digit, one lowercase letter, and one uppercase letter
        String passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}";
        return password.matches(passwordPattern);
    }


    private void registerUser(User user) {
        UserApi userApi = RetrofitClient.getUserApi();
        Call<Void> call = userApi.registerUser(user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle successful registration (even though response body is empty)
                    Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                    // Navigate to sign-in activity or perform other actions
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    // Handle unsuccessful registration
                    Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle network failure or other exceptions
                Toast.makeText(SignUpActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
