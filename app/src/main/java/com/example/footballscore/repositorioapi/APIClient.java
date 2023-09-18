package com.example.footballscore.repositorioapi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static final String BASE_URL = "https://api-football-v1.p.rapidapi.com/v3/";
    private static final String API_KEY = "5b372f399amshb4514c44c8be65ep123ea3jsn7e198cf6b6d9";
    private static final String API_HOST = "api-football-v1.p.rapidapi.com";

    private static Retrofit retrofit;
    private static OkHttpClient httpClient;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static OkHttpClient getOkHttpClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request request = original.newBuilder()
                                    .addHeader("X-RapidAPI-Key", API_KEY)
                                    .addHeader("X-RapidAPI-Host", API_HOST)
                                    .method(original.method(), original.body())
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .build();
        }
        return httpClient;
    }
}
