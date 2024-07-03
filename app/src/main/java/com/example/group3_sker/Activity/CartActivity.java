package com.example.group3_sker.Activity;

import android.os.Bundle;
import android.view.View;
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

private TextView totalFeeTxt, taxTxt, deliveryTxt, totalTxt, emptyTxt;
private double tax;
private ScrollView scrollView;
private ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        managementCart = new ManagementCart(this);

        initView();
        setVariable();
        initList();
        calculateCart();

    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CartListAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener(){

            @Override
            public void change() {
                calculateCart();
            }
        });
        recyclerView.setAdapter(adapter);
        if (managementCart.getListCart().isEmpty()){
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(ScrollView.GONE);
        }else {
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(ScrollView.VISIBLE);
        }
    }

    private void calculateCart() {
        double percentTax = 0.1;
        double deliveryFee = 10000;
        double total = 0;
        tax = Math.round((managementCart.getTotalFee()*percentTax*100.0)/100.0);

        total = Math.round((managementCart.getTotalFee() + tax + deliveryFee)*100.0)/100.0;

        double itemTotal = Math.round(managementCart.getTotalFee()*100.0)/100.0;
        totalFeeTxt.setText("$"+itemTotal);
        taxTxt.setText("$"+tax);
        deliveryTxt.setText("$"+deliveryFee);
        totalTxt.setText("$"+total);

    }

    private void setVariable() {
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private void initView() {
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        taxTxt = findViewById(R.id.taxTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        totalTxt = findViewById(R.id.totalTxt);
        emptyTxt = findViewById(R.id.emptyTxt);
        scrollView = findViewById(R.id.scrollView3);
        backBtn = findViewById(R.id.backBtn);
        recyclerView = findViewById(R.id.view3);
    }
}