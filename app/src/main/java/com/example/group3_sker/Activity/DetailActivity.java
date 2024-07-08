package com.example.group3_sker.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.group3_sker.Model.Product;
import com.example.group3_sker.Helper.ManagementCart;
import com.example.group3_sker.R;

public class DetailActivity extends AppCompatActivity {
    private Button addToCartBtn;
    private TextView titleTxt, feeTxt, descriptionTxt, reviewTxt, scoreTxt;
    private ImageView picItem, backBtn;
    private Product object;
    private int NumberOrder = 1;
    private ManagementCart managementCart;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", "User");

        initView();
        managementCart = new ManagementCart(this, userId);
        getBundle();
    }

    private void getBundle() {
        // Retrieve the Product object from Intent extras
        object = (Product) getIntent().getSerializableExtra("product");

        if (object != null) {
            // Load image using Glide from picUrl
            Glide.with(this)
                    .load(object.getPicUrl())
                    .into(picItem);

            // Set other views with Product details
            titleTxt.setText(object.getName());
            feeTxt.setText("$" + String.format("%.1f",object.getPrice()));
            descriptionTxt.setText(object.getDescription());
            reviewTxt.setText(String.valueOf(object.getReview()));
            scoreTxt.setText(String.format("%.1f", object.getScore())); // Format score to 1 decimal place

            // Add to Cart button click listener
            addToCartBtn.setOnClickListener(v -> {
                object.setNumberInCart(NumberOrder);
                managementCart.insertFood(object);
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
            });

            // Back button click listener
            backBtn.setOnClickListener(v -> {
                startActivity(new Intent(DetailActivity.this, MainActivity.class));
            });
        } else {
            Toast.makeText(this, "Failed to get product details", Toast.LENGTH_SHORT).show();
            finish(); // Finish activity if product details are not available
        }
    }

    private void initView() {
        addToCartBtn = findViewById(R.id.addToCartBtn);
        feeTxt = findViewById(R.id.priceTxt);
        titleTxt = findViewById(R.id.titleTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        picItem = findViewById(R.id.itemPic);
        reviewTxt = findViewById(R.id.reviewTxt);
        scoreTxt = findViewById(R.id.scoreTxt);
        backBtn = findViewById(R.id.backBtn);
    }
}
