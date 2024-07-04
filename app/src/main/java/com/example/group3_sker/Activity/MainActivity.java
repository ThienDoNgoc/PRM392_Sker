package com.example.group3_sker.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group3_sker.API.ProductApi;
import com.example.group3_sker.API.RetrofitClient;
import com.example.group3_sker.Adapter.PopularListAdapter;
import com.example.group3_sker.Domain.Product;
import com.example.group3_sker.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPopular;
    private PopularListAdapter adapterPopular;
    private TextView seeAllTxt, headerTxt;
    private EditText searchKeywordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
        setupBottomNavigation();
        handleSearch();
        fetchProducts();
    }

    private void handleSearch() {
        seeAllTxt = findViewById(R.id.seeAllTxt);
        headerTxt = findViewById(R.id.headerTxt);
        searchKeywordTxt = findViewById(R.id.searchKeywordTxt);

        // Initialize RecyclerView and Adapter
        initRecyclerView();

        searchKeywordTxt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = searchKeywordTxt.getText().toString().trim();
                performSearch(query);
                hideKeyboard(); // Hide keyboard after search
                return true; // Consume the action
            }
            return false; // Return false if you don't consume the action
        });

        // Handle click on seeAllTxt
        seeAllTxt.setOnClickListener(v -> {
            if (headerTxt.getText().toString().equals("Popular Products")) {
                fetchProducts();
                headerTxt.setText("All Products");
                seeAllTxt.setText("See Popular");
            } else {
                initRecyclerView(); // Reset RecyclerView if needed
                fetchProducts(); // Fetch popular products
                headerTxt.setText("Popular Products");
                seeAllTxt.setText("See all");
            }
        });
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            fetchProducts(); // Fetch all products if query is empty
            headerTxt.setText("All Products");
            seeAllTxt.setText("See Popular");
        } else {
            changeRecyclerView(); // Change RecyclerView layout for search results
            fetchSearchProducts(query); // Fetch products based on search query
            headerTxt.setText("Search Results");
            seeAllTxt.setText("See All");
        }
        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchKeywordTxt.getWindowToken(), 0);
    }

    private void setupBottomNavigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);

        homeBtn.setOnClickListener(v -> {
            // No need to restart MainActivity, as it's already the current activity
        });

        cartBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        });
    }

    private void initRecyclerView() {
        recyclerViewPopular = findViewById(R.id.view1);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterPopular = new PopularListAdapter(new ArrayList<>());
        recyclerViewPopular.setAdapter(adapterPopular);
    }

    private void changeRecyclerView() {
        recyclerViewPopular.setLayoutManager(new GridLayoutManager(this, 2));
        adapterPopular = new PopularListAdapter(new ArrayList<>());
        recyclerViewPopular.setAdapter(adapterPopular);
    }

    private void fetchProducts() {
        ProductApi productApi = RetrofitClient.getProductApi();
        Call<List<Product>> call = productApi.getProducts(null, null);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body();
                    adapterPopular.setData(products);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchSearchProducts(String query) {
        ProductApi productApi = RetrofitClient.getProductApi();
        Call<List<Product>> call = productApi.getProducts(query, null);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body();
                    adapterPopular.setData(products);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network error. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
