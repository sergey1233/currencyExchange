package com.sergey.currencyexchange.model;


public class BlackMarket {

    private static final int DOLLAR = 0;
    private static final int EURO = 1;
    private static final int RB = 2;
    private static final String URLTYPE = "app_response_blackM.php";

    private String date;
    private double buyDollar;
    private double sellDollar;
    private double buyEuro;
    private double sellEuro;
    private double buyRb;
    private double sellRb;
    private double changesBuyDollar = 0;
    private double changesSellDollar = 0;
    private double changesBuyEuro = 0;
    private double changesSellEuro = 0;
    private double changesBuyRb = 0;
    private double changesSellRb = 0;

    public BlackMarket() {}

    public String getDate() {
        return date;
    }

    public double getBuy(int currencyId) {
        switch (currencyId)
        {
            case DOLLAR:
                return buyDollar;
            case EURO:
                return buyEuro;
            case RB:
                return buyRb;
        }
        return buyDollar;
    }

    public double[] getBuy() {
        double[] buys = {buyDollar, buyEuro, buyRb};
        return buys;
    }

    public double getSell(int currencyId) {
        switch (currencyId)
        {
            case DOLLAR:
                return sellDollar;
            case EURO:
                return sellEuro;
            case RB:
                return sellRb;
        }
        return sellDollar;
    }

    public double[] getSell() {
        double[] sells = {sellDollar, sellEuro, sellRb};
        return sells;
    }

    public double getChangesBuy(int currencyId) {
        switch (currencyId)
        {
            case DOLLAR:
                return changesBuyDollar;
            case EURO:
                return changesBuyEuro;
            case RB:
                return changesBuyRb;
        }
        return changesBuyDollar;
    }

    public double getChangesSell(int currencyId) {
        switch (currencyId)
        {
            case DOLLAR:
                return changesSellDollar;
            case EURO:
                return changesSellEuro;
            case RB:
                return changesSellRb;
        }
        return changesSellDollar;
    }

    public void setNewInformation(String date, double buyDollar, double sellDollar, double buyEuro, double sellEuro, double buyRb, double sellRb, double changesBuyD, double changesSellD, double changesBuyE, double changesSellE, double changesBuyR, double changesSellR) {
        this.date = date;
        this.buyDollar = buyDollar;
        this.sellDollar = sellDollar;
        this.buyEuro = buyEuro;
        this.sellEuro = sellEuro;
        this.buyRb = buyRb;
        this.sellRb = sellRb;
        this.changesBuyDollar = changesBuyD;
        this.changesSellDollar = changesSellD;
        this.changesBuyEuro = changesBuyE;
        this.changesSellEuro = changesSellE;
        this.changesBuyRb = changesBuyR;
        this.changesSellRb = changesSellR;
    }

    public static String getUrlType() {
        return URLTYPE;
    }
}
