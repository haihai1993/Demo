package com.example.mrfunny.demo;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {

    @GET("yql")
    Call<ResponseBody> getData(
            @Query("q") String q,
            @Query("format") String format,
            @Query("diagnostics") boolean diagnostics,
            @Query("env") String env,
            @Query("callback") String callback
            );
}
