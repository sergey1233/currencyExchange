package com.sergey.currencyexchange.model;

import java.util.ArrayList;

public class Country {

    private String name;
    private String flag;
    private int code;
    private CentralBank cBank;
    private MBank mBank;
    private BlackMarket blackMarket;
    private ArrayList<Bank> bankList;

    public Country(String flag, String name, int code) {
        this.flag = flag;
        this.name = name;
        this.code = code;
        cBank = new CentralBank(this.code);
        mBank = new MBank();
        blackMarket = new BlackMarket();
        bankList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getFlag() {
        return flag;
    }

    public int getCode() {
        return code;
    }

    public CentralBank getCBank() {
        return cBank;
    }

    public MBank getMBank() {
        return mBank;
    }

    public BlackMarket getBlackMarket() {
        return blackMarket;
    }

    public ArrayList<Bank> getBankList() {
        return bankList;
    }
}
