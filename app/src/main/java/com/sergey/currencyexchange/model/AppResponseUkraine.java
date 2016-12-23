package com.sergey.currencyexchange.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;


public class AppResponseUkraine {

    @SerializedName("nbu")
    private Map<String, String> nbuResponse;

    @SerializedName("mBank")
    private Map<String, String> mBankResponse;

    @SerializedName("blackM")
    private Map<String, String> blackMResponse;

    @SerializedName("bankList")
    private ArrayList<Map<String, String>> bankListResponse;

    @SerializedName("currencyList")
    private ArrayList<Map<String, String>> currencyListResponse;

    public AppResponseUkraine() {
    }

    public Map<String, String> getNbuResponse() {
        return nbuResponse;
    }

    public Map<String, String> getMBankResponse() {
        return mBankResponse;
    }

    public Map<String, String> getBlackMResponse() {
        return blackMResponse;
    }

    public ArrayList<Map<String, String>> getBankListResponse() {
        return  bankListResponse;
    }

    public ArrayList<Map<String, String>> getCurrencyListResponse() {
        return currencyListResponse;
    }
}
