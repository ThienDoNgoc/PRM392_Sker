package com.example.group3_sker.Helper;

import android.app.Application;

import com.example.group3_sker.API.RetrofitClient;
import com.example.group3_sker.API.RetrofitStripe;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitClient.initialize(this);
        FirebaseApp.initializeApp(this);
    }
}
