package com.example.mcd;

import android.widget.Toast;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class RetBuild {
    private static Retrofit retrofit;
    public static Retrofit getRetrofitInstance(){
        if(retrofit==null){
            retrofit=new retrofit2.Retrofit.Builder()
                    .baseUrl("http://142.93.14.153:8081/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }

}
