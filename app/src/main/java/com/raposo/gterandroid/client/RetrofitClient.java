package com.raposo.gterandroid.client;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;
public class RetrofitClient {

    private static final String URL_BASE = "https://ideal-barnacle-pjq7xrw69pjjh9664-8080.app.github.dev/gter/";
    //private static final String URL_BASE = "http://192.168.0.5:8080/gter/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if(retrofit == null){
            JsonAdapter<UUID> uuidAdapter = new JsonAdapter<UUID>() {
                @Override
                public UUID fromJson(JsonReader reader) throws IOException {
                    String value = reader.nextString();
                    return value != null ? UUID.fromString(value) : null;
                }

                @Override
                public void toJson(JsonWriter writer, UUID value) throws IOException {
                    writer.value(value != null ? value.toString() : null);
                }
            }.nullSafe();

            Moshi moshi = new Moshi.Builder()
                    .add(Date.class, new Rfc3339DateJsonAdapter())
                    .add(UUID.class, uuidAdapter)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build();
        }
        return retrofit;
    }
}
