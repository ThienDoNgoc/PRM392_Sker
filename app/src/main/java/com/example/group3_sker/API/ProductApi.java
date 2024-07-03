package com.example.group3_sker.API;

import com.example.group3_sker.Domain.Product;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductApi {
    @GET("product")
    public Call<List<Product>> getProducts();

    @GET("product/{id}")
    public Call<Product> getProductById(String id);
}
