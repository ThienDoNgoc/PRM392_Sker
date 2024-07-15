package com.example.group3_sker.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.group3_sker.API.RetrofitStripe;
import com.example.group3_sker.API.StripeApi;
import com.example.group3_sker.Helper.ManagementCart;
import com.example.group3_sker.R;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.stripe.model.PaymentIntent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutActivity extends AppCompatActivity {
    private static final String TAG = "CheckOutActivity";
    private PaymentSheet paymentSheet;
    private String paymentClientSecret, userId;
    private ManagementCart managementCart;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", "User");

        managementCart = new ManagementCart(this, userId);

        StripeApi stripeApi = RetrofitStripe.getStripeApi();
        button = findViewById(R.id.pay_button);

        // Initialize PaymentConfiguration and PaymentSheet
        PaymentConfiguration.init(this, "pk_test_51PWYf1FlKO9DUXup3pZcE1Mys2b6g2fzgUuNSWVoaii9loGJf35iWvrzWOZUCLe1bpwtyx8OHD2ZGy5G60yOSaI600e0Lm3s4X");
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        Call<PaymentIntent> call = stripeApi.getPaymentIntent("2001", "usd");
        call.enqueue(new Callback<PaymentIntent>() {
            @Override
            public void onResponse(Call<PaymentIntent> call, Response<PaymentIntent> response) {
                Log.d(TAG, "Retrofit onResponse called");
                if (response.isSuccessful() && response.body() != null) {
                    paymentClientSecret = response.body().getClientSecret();
                    Log.d(TAG, "Payment client secret received: " + response.body());
                } else {
                    Log.e(TAG, "Failed to retrieve payment intent. Response code: " + response.code());
                    Toast.makeText(CheckOutActivity.this, "Failed to retrieve payment intent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentIntent> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage(), t);
                Toast.makeText(CheckOutActivity.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Pay button clicked");
                if (paymentClientSecret != null && !paymentClientSecret.isEmpty()) {
                    presentPaymentSheet();
                } else {
                    Log.e(TAG, "Payment client secret is not set");
                    Toast.makeText(CheckOutActivity.this, "Payment client secret is not set", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d(TAG, "Payment canceled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e(TAG, "Payment failed: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Log.d(TAG, "Payment completed");
            // Display, for example, an order confirmation screen
        }
    }

    private void presentPaymentSheet() {
        Log.d(TAG, "Presenting payment sheet with client secret: " + paymentClientSecret);
        paymentSheet.presentWithPaymentIntent(paymentClientSecret);
    }
}
