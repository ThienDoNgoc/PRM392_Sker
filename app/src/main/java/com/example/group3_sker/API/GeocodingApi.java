package com.example.group3_sker.API;

import com.example.group3_sker.Model.GeocodingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodingApi {
    @GET("geocode/json")
    Call<GeocodingResponse> getGeocode(@Query("address") String address, @Query("key") String apiKey);
}