package com.redditjokes.twistedjokes.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    public static Retrofit getPsychAPIClient() {


        RetrofitCache retrofitCache=new RetrofitCache();
        Constants constants = new Constants();

        return new Retrofit.Builder().
                baseUrl(constants.BASE_URL)
                .client(retrofitCache.okHttpClientPych())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //with this we can return flowable object from retrofit call
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
}
