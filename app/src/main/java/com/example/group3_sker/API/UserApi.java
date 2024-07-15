package com.example.group3_sker.API;

import com.example.group3_sker.Model.LoginRequest;
import com.example.group3_sker.Model.LoginResponse;
import com.example.group3_sker.Model.Product;
import com.example.group3_sker.Model.User;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
    @POST("users/authen")
    public Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
    @GET("users/me")
    public Call<User> getUser();
    @GET("users/{id}")
    Call<User> getUserById(@Path("id") String id);
    @POST("users/register")
    public Call<Void> registerUser(@Body User user);
}
