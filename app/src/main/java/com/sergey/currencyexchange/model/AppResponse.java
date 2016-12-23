package com.sergey.currencyexchange.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

public class AppResponse {

    @SerializedName("bankList")
    private ArrayList<Map<String, String>> bankListResponse;

    @SerializedName("currencyList")
    private ArrayList<Map<String, String>> currencyListResponse;

    public AppResponse() {
    }

    public ArrayList<Map<String, String>> getBankListResponse() {
        return  bankListResponse;
    }

    public ArrayList<Map<String, String>> getCurrencyListResponse() {
        return currencyListResponse;
    }

}
