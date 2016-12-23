package com.sergey.currencyexchange.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AppResponseInterfaceUkraine {

    @GET("{urlType}")
    Call<AppResponseUkraine> request(@Path("urlType") String urlPath);
}
