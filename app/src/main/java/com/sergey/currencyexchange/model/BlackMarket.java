package com.sergey.currencyexchange.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sergey on 04.05.2016.
 */
public class BlackMarket implements Parcelable {
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
    private static final int DOLLAR = 0;
    private static final int EURO = 1;
    private static final int RB = 2;

    public BlackMarket(String date, double buyDollar, double sellDollar, double buyEuro, double sellEuro, double buyRb, double sellRb) {
        this.date = date;
        this.buyDollar = buyDollar;
        this.sellDollar = sellDollar;
        this.buyEuro = buyEuro;
        this.sellEuro = sellEuro;
        this.buyRb = buyRb;
        this.sellRb = sellRb;
    }

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

    /* Changes counted from new value to current value */
    public void countChanges(double buyDollar, double sellDollar, double buyEuro, double sellEuro, double buyRb, double sellRb) {
        changesBuyDollar = buyDollar - this.buyDollar;
        changesSellDollar = sellDollar - this.sellDollar;
        changesBuyEuro = buyEuro - this.buyEuro;
        changesSellEuro = sellEuro - this.sellEuro;
        changesBuyRb = buyRb - this.buyRb;
        changesSellRb = sellRb - this.sellRb;
    }

    public void setNewInformation(String date, double buyDollar, double sellDollar, double buyEuro, double sellEuro, double buyRb, double sellRb) {
        countChanges(buyDollar, sellDollar, buyEuro, sellEuro, buyRb, sellRb);
        this.date = date;
        this.buyDollar = buyDollar;
        this.sellDollar = sellDollar;
        this.buyEuro = buyEuro;
        this.sellEuro = sellEuro;
        this.buyRb = buyRb;
        this.sellRb = sellRb;
    }


    protected BlackMarket(Parcel in) {
        buyDollar = in.readDouble();
        sellDollar = in.readDouble();
        buyEuro = in.readDouble();
        sellEuro = in.readDouble();
        buyRb = in.readDouble();
        sellRb = in.readDouble();
    }

    public static final Creator<BlackMarket> CREATOR = new Creator<BlackMarket>() {
        @Override
        public BlackMarket createFromParcel(Parcel in) {
            return new BlackMarket(in);
        }

        @Override
        public BlackMarket[] newArray(int size) {
            return new BlackMarket[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(buyDollar);
        dest.writeDouble(sellDollar);
        dest.writeDouble(buyEuro);
        dest.writeDouble(sellEuro);
        dest.writeDouble(buyRb);
        dest.writeDouble(sellRb);
    }
}
