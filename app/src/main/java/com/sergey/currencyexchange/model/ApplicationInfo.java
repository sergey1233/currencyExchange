package com.sergey.currencyexchange.model;

import java.util.ArrayList;

public class ApplicationInfo {

    private static final ApplicationInfo instance = new ApplicationInfo();
    private int currencyId;
    private Nbu nbu;
    private MBank mBank;
    private BlackMarket blackMarket;
    private ArrayList<Bank> bankList;

    private ApplicationInfo(){
        nbu = new Nbu();
        mBank = new MBank();
        blackMarket = new BlackMarket();
        bankList = new ArrayList<>();
    }

    public static ApplicationInfo getInstance(){
        return instance;
    }

    public void setNbu(Nbu nbu) {
        this.nbu = nbu;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public Nbu getNbu() {
        return nbu;
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

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }
}
