package com.example.group3_sker.API;

import android.content.Context;
import android.content.SharedPreferences;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RetrofitClient {
    private static final String BASE_URL = "https://www.sker-api.somee.com/api/";
    private static Retrofit retrofit;
    private static Context context;

    // Singleton pattern
    private RetrofitClient() {
    }

    public static void initialize(Context ctx) {
        context = ctx.getApplicationContext();
    }

    public static Retrofit get() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request.Builder requestBuilder = original.newBuilder();

                            // Get the JWT token from SharedPreferences
                            SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                            String token = sharedPreferences.getString("JWT_TOKEN", null);
                            if (token != null) {
                                requestBuilder.header("Authorization", "Bearer " + token);
                            }

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ProductApi getProductApi() {
        return get().create(ProductApi.class);
    }
    public static UserApi getUserApi() {
        return get().create(UserApi.class);
    }
}
