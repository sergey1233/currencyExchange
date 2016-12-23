package com.sergey.currencyexchange.model;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AppResponseInterface {

    @GET("{urlType}")
    Call<AppResponse> request(@Path("urlType") String urlPath);

}
