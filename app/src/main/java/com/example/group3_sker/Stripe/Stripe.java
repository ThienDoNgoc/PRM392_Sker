package com.example.group3_sker.Stripe;

import android.app.Application;
import com.stripe.android.PaymentConfiguration;

public class Stripe extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51PWYf1FlKO9DUXup3pZcE1Mys2b6g2fzgUuNSWVoaii9loGJf35iWvrzWOZUCLe1bpwtyx8OHD2ZGy5G60yOSaI600e0Lm3s4X"
        );
    }
}
