package com.example.mcd;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrInt {
    @GET("/price/{currency}")
    Call<String> getExchangeCurrency(@Path("currency") String currency);

    @GET("/lastUpdate")
    Call<JsonObject> getDateLastUpdate();
}
