package com.sergey.currencyexchange.model;



public class Nbu {

    private static final int DOLLAR = 0;
    private static final int EURO = 1;
    private static final int RB = 2;
    private static final String URLTYPE = "app_response_nbu.php";

    private double rateDollar;
    private double rateEuro;
    private double rateRb;
    private String date;
    private double changesDollar = 0;
    private double changesEuro = 0;
    private double changesRb = 0;

    public String getDate() {
        return date;
    }

    public double getRate(int currencyId) {
        switch (currencyId)
        {
            case DOLLAR:
                return rateDollar;
            case EURO:
                return rateEuro;
            case RB:
                return rateRb;
        }
        return rateDollar;
    }

    public double[] getRate() {
        double[] rates = {rateDollar, rateEuro, rateRb};
        return rates;
    }

    public double getChanges(int currencyId) {
        switch (currencyId)
        {
            case DOLLAR:
                return changesDollar;
            case EURO:
                return changesEuro;
            case RB:
                return changesRb;
        }
        return changesDollar;
    }

    public void setNewInformation(String date, double rateDollar, double rateEuro, double rateRb, double changesDollar, double changesEuro, double changesRb) {
        this.rateDollar = rateDollar;
        this.rateEuro = rateEuro;
        this.rateRb = rateRb;
        this.date = date;
        this.changesDollar = changesDollar;
        this.changesEuro = changesEuro;
        this.changesRb = changesRb;
    }

    public static String getUrlType() {
        return URLTYPE;
    }
}
