package com.example.group3_sker.API;

import com.example.group3_sker.Model.GeocodingResponse;
import com.stripe.model.PaymentIntent;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StripeApi {
    @POST("v1/payment_intents")
    @Headers("Authorization: Bearer sk_test_51PWYf1FlKO9DUXupQEZ2sdQLsk2RhMRCoSrpaEfH9KuZ2izxgQdApq2bBmigmVA6B2yopwKWQoosUYM0ixafflvK00TfQJBWAQ")
    Call<PaymentIntent> getPaymentIntent(
            @Query("amount") String amount,
            @Query("currency") String currency
    );

}
