package com.ToxicBakery.app.tabsontallahassee.api.retrofit;

import com.ToxicBakery.app.tabsontallahassee.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

public class RetrofitService {

    private static RetrofitService instance;

    private TabsAPI service;

    private RetrofitService() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(Level.BODY);

        // Disable logging in production
        if (!BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(Level.NONE);
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("X-APIKEY", "3d9ca1a9-d88d-425c-acaa-b8e06f06c627")
                                .build();

                        return chain.proceed(request);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("https://tabsontallahassee.com/api/")
                .client(client)
                .build();
        
        service = retrofit.create(TabsAPI.class);
    }

    public static RetrofitService getInstance() {
        if (instance == null) {
            synchronized (RetrofitService.class) {
                if (instance == null) {
                    instance = new RetrofitService();
                }
            }
        }

        return instance;
    }

    public TabsAPI getTabsApi() {
        return service;
    }

}
