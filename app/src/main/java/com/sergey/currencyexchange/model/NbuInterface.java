package com.sergey.currencyexchange.model;


import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NbuInterface {
    @GET("{urlType}")
    Call<Object> request(@Path("urlType") String urlPath);

}
