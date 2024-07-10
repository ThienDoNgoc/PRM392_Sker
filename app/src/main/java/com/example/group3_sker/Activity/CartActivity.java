package com.example.group3_sker.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group3_sker.Adapter.CartListAdapter;
import com.example.group3_sker.Helper.ChangeNumberItemsListener;
import com.example.group3_sker.Helper.ManagementCart;
import com.example.group3_sker.R;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ManagementCart managementCart;

    private TextView totalFeeTxt, taxTxt, deliveryTxt, totalTxt, emptyTxt, addressTxt, viewLocaionTv;
    private double tax;
    private ScrollView scrollView;
    private ImageView backBtn;
    private String userId, userAddress;
    private Button clearCartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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
        calculateCart();
    }


    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Initialize adapter with cart items and change listener
        adapter = new CartListAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void change() {
                // Update UI when cart items change
                calculateCart();
            }
        }, userId);
        recyclerView.setAdapter(adapter);

        // Show or hide empty text based on cart items
        if (managementCart.getListCart().isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(ScrollView.GONE);
        } else {
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(ScrollView.VISIBLE);
        }
    }

    private void calculateCart() {
        // Constants for tax and delivery fee
        double percentTax = 0.1;
        double deliveryFee = 3;

        // Calculate totals
        double total = Math.round((managementCart.getTotalFee() + tax + deliveryFee) * 100.0) / 100.0;
        tax = Math.round((managementCart.getTotalFee() * percentTax * 100.0) / 100.0);

        // Display calculated values in TextViews
        double itemTotal = Math.round(managementCart.getTotalFee() * 100.0) / 100.0;
        totalFeeTxt.setText("$" + itemTotal);
        taxTxt.setText("$" + tax);
        deliveryTxt.setText("$" + deliveryFee);
        totalTxt.setText("$" + total);
    }

    private void setVariable() {
        // Handle back button click to finish activity
        backBtn.setOnClickListener(v -> {
            finish();
        });

        viewLocaionTv.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, MapActivity.class);
            startActivity(intent);
        });

        clearCartBtn.setOnClickListener(v -> {
            managementCart.clearCart();
            initList();
        });
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


        addressTxt.setText(userAddress);

    }
}
