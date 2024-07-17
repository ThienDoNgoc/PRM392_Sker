package com.example.group3_sker.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group3_sker.API.RetrofitStripe;
import com.example.group3_sker.API.StripeApi;
import com.example.group3_sker.Adapter.CartListAdapter;
import com.example.group3_sker.Helper.ChangeNumberItemsListener;
import com.example.group3_sker.Helper.ManagementCart;
import com.example.group3_sker.R;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.stripe.model.PaymentIntent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ManagementCart managementCart;
    private static final String TAG = "CartActivity";
    private TextView totalFeeTxt, taxTxt, deliveryTxt, totalTxt, emptyTxt, addressTxt, viewLocaionTv;
    private double tax;
    private ScrollView scrollView;
    private ImageView backBtn;
    private String userId, userAddress;
    private Button clearCartBtn;
    private Button order_btn;
    private PaymentSheet paymentSheet;
    private String paymentClientSecret;
    private String total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        order_btn = findViewById(R.id.ordBtn);
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", "User");
        userAddress = sharedPreferences.getString("ADDRESS", "Address");

        // Initialize ManagementCart to manage cart operations
        managementCart = new ManagementCart(this, userId);

        // Initialize UI elements
        initView();

        // Set click listener for back button
        setVariable();

        // Initialize RecyclerView and set up cart items
        initList();

        // Calculate cart totals initially
        total = calculateCart();

        // Initialize PaymentSheet
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        PaymentConfiguration.init(this, "pk_test_51PWYf1FlKO9DUXup3pZcE1Mys2b6g2fzgUuNSWVoaii9loGJf35iWvrzWOZUCLe1bpwtyx8OHD2ZGy5G60yOSaI600e0Lm3s4X");

        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Pay button clicked");
                if (paymentClientSecret != null && !paymentClientSecret.isEmpty()) {
                    presentPaymentSheet();
                } else {
                    Log.e(TAG, "Payment client secret is not set");
                    Toast.makeText(CartActivity.this, "Payment client secret is not set", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fetchPaymentIntent();
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Initialize adapter with cart items and change listener
        adapter = new CartListAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void change() {
                fetchPaymentIntent();
                total = calculateCart();
            }
        }, userId);
        recyclerView.setAdapter(adapter);

        // Show or hide empty text based on cart items
        if (managementCart.getListCart().isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void fetchPaymentIntent() {
        total = calculateCart();
        Log.d(TAG, "Total before API call: " + total);

        StripeApi stripeApi = RetrofitStripe.getStripeApi();
        Call<PaymentIntent> call = stripeApi.getPaymentIntent(total, "usd");
        call.enqueue(new Callback<PaymentIntent>() {
            @Override
            public void onResponse(Call<PaymentIntent> call, Response<PaymentIntent> response) {
                Log.d(TAG, "Retrofit onResponse called");
                if (response.isSuccessful() && response.body() != null) {
                    paymentClientSecret = response.body().getClientSecret();
                    Log.d(TAG, "Payment client secret received: " + response.body());
                } else {
                    Log.e(TAG, "Failed to retrieve payment intent. Response code: " + response.code());
                    Toast.makeText(CartActivity.this, "Failed to retrieve payment intent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentIntent> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage(), t);
                Toast.makeText(CartActivity.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String calculateCart() {
        // Constants for tax and delivery fee
        double percentTax = 0.1;
        double deliveryFee = 3;

        // Calculate tax
        tax = Math.round((managementCart.getTotalFee() * percentTax) * 100.0) / 100.0;

        // Calculate total
        double total = (managementCart.getTotalFee() + tax + deliveryFee) * 100.0;
        int totalInt = (int) Math.round(total);

        // Display calculated values in TextViews
        double itemTotal = Math.round(managementCart.getTotalFee() * 100.0) / 100.0;
        totalFeeTxt.setText("$" + itemTotal);
        taxTxt.setText("$" + tax);
        deliveryTxt.setText("$" + deliveryFee);
        totalTxt.setText("$" + (totalInt / 100.0));

        return Integer.toString(totalInt);
    }

    private void setVariable() {
        // Handle back button click to finish activity
        backBtn.setOnClickListener(v -> finish());

        viewLocaionTv.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, MapActivity.class);
            startActivity(intent);
        });

        clearCartBtn.setOnClickListener(v -> {
            try {
                managementCart.clearCart();
                adapter.notifyDataSetChanged();
                initList();
            } catch (Exception e) {
                Log.e(TAG, "Error clearing cart: " + e.getMessage());
                Toast.makeText(CartActivity.this, "Error clearing cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void presentPaymentSheet() {
        Log.d(TAG, "Presenting payment sheet with client secret: " + paymentClientSecret);
        paymentSheet.presentWithPaymentIntent(paymentClientSecret);
    }

    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d(TAG, "Payment canceled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e(TAG, "Payment failed: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Log.d(TAG, "Payment completed");
            showOrderConfirmationDialog();
            managementCart.clearCart();
            adapter.notifyDataSetChanged();
            initList();
        }
    }

    private void initView() {
        // Initialize UI elements from layout
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        taxTxt = findViewById(R.id.taxTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        totalTxt = findViewById(R.id.totalTxt);
        emptyTxt = findViewById(R.id.emptyTxt);
        scrollView = findViewById(R.id.scrollView3);
        backBtn = findViewById(R.id.backBtn);
        recyclerView = findViewById(R.id.view3);
        clearCartBtn = findViewById(R.id.clearCartBtn);
        addressTxt = findViewById(R.id.addressTxt);
        viewLocaionTv = findViewById(R.id.viewLocationTv);

        // Set the user address in the address TextView
        addressTxt.setText(userAddress);
    }
    private void showOrderConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Order Successfully");
        builder.setMessage("Your order has been successfully processed.");

        // Add a button to dismiss the dialog
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Handle button click (if needed)
                dialogInterface.dismiss(); // Dismiss the dialog
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
