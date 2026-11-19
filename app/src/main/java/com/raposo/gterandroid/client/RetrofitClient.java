package com.raposo.gterandroid.client;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;

import java.util.Date;

public class RetrofitClient {

    private static final String URL_BASE = "https://urban-train-r4v7jg6xwg59c5pp6-8080.app.github.dev/gter/";
    //private static final String URL_BASE = "http://192.168.0.5:8080/gter/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if(retrofit == null){

            Moshi moshi = new Moshi.Builder()
                    .add(Date.class, new Rfc3339DateJsonAdapter())
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build();
        }
        return retrofit;
    }
}
