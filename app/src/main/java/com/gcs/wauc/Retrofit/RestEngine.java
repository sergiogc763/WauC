package com.gcs.wauc.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestEngine {

    private final static String BASE_URL = "http://10.0.2.2:5000";
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit() {

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
