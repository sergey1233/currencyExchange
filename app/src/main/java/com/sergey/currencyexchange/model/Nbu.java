package com.sergey.currencyexchange.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sergey on 04.05.2016.
 */
public class Nbu {

    private static final int DOLLAR = 0;
    private static final int EURO = 1;
    private static final int RB = 2;
    private static final String URLTYPE = "/app_response_nbu.php";

    private double rateDollar;
    private double rateEuro;
    private double rateRb;
    private String date;
    private double changesDollar = 0;
    private double changesEuro = 0;
    private double changesRb = 0;


    public Nbu (String date, double rateDollar, double rateEuro, double rateRb) {
        this.date = date;
        this.rateDollar = rateDollar;
        this.rateEuro = rateEuro;
        this.rateRb = rateRb;
    }

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

    /* Changes counted from new value to current value */
    public void countChanges(double rateDollar, double rateEuro, double rateRb) {
        changesDollar = rateDollar - this.rateDollar;
        changesEuro = rateEuro - this.rateEuro;
        changesRb = rateRb - this.rateRb;
    }

    public void setNewInformation(String date, double rateDollar, double rateEuro, double rateRb) {
        countChanges(rateDollar, rateEuro, rateRb);
        this.rateDollar = rateDollar;
        this.rateEuro = rateEuro;
        this.rateRb = rateRb;
        this.date = date;
    }

    public static String getUrlType() {
        return URLTYPE;
    }
}
