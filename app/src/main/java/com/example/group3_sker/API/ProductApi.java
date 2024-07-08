package com.example.group3_sker.API;

import com.example.group3_sker.Model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductApi {
    @GET("prm392")
    public Call<List<Product>> getProducts(@Query("searchKeyword") String searchKeyword, @Query("brandName") String brandName);

    @GET("prm392/{id}")
    public Call<Product> getProductById(String id);
}
