package com.sergey.currencyexchange.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Sergey on 04.05.2016.
 */
public class MBank implements Parcelable{

    private String[] dateArray;
    private double[] buyArrayDollar;
    private double[] sellArrayDollar;
    private double[] buyArrayEuro;
    private double[] sellArrayEuro;
    private double[] buyArrayRb;
    private double[] sellArrayRb;
    private String date;
    private double buyDollar;
    private double buyEuro;
    private double buyRb;
    private double sellDollar;
    private double sellEuro;
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


    public MBank(String[] dateArray, double[] buyArrayDollar, double[] sellArrayDollar, double[] buyArrayEuro, double[] sellArrayEuro, double[] buyArrayRb, double[] sellArrayRb) {
        this.dateArray = dateArray;
        this.buyArrayDollar = buyArrayDollar;
        this.sellArrayDollar = sellArrayDollar;
        this.buyArrayEuro = buyArrayEuro;
        this.sellArrayEuro = sellArrayEuro;
        this.buyArrayRb = buyArrayRb;
        this.sellArrayRb = sellArrayRb;

        this.date = dateArray[dateArray.length - 1];
        this.buyDollar = buyArrayDollar[buyArrayDollar.length - 1];
        this.sellDollar = sellArrayDollar[sellArrayDollar.length - 1];
        this.buyEuro = buyArrayEuro[buyArrayEuro.length - 1];
        this.sellEuro = sellArrayEuro[sellArrayEuro.length - 1];
        this.buyRb = buyArrayRb[buyArrayRb.length - 1];
        this.sellRb = sellArrayRb[sellArrayRb.length - 1];
    }

    public String getDate() {
        return date;
    }

    public String[] getDateArray() {
        return dateArray;
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

    public double[] getBuyArray(int currencyId) {
        switch (currencyId)
        {
            case DOLLAR:
                return buyArrayDollar;
            case EURO:
                return buyArrayEuro;
            case RB:
                return buyArrayRb;
        }
        return buyArrayDollar;
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

    public double[] getSellArray(int currencyId) {
        switch (currencyId)
        {
            case DOLLAR:
                return sellArrayDollar;
            case EURO:
                return sellArrayEuro;
            case RB:
                return sellArrayRb;
        }
        return sellArrayDollar;
    }

    public double getChangesBuy(int currencyId) {
        countChangesBuy();
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
        countChangesSell();
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

    /* Changes counted from last value to first value of the day*/
    private void countChangesBuy() {
        if (buyArrayDollar.length >= 2)
        {
            changesBuyDollar = buyArrayDollar[buyArrayDollar.length - 1] - buyArrayDollar[0];
        }
        if (buyArrayEuro.length >= 2)
        {
            changesBuyEuro = buyArrayEuro[buyArrayEuro.length - 1] - buyArrayEuro[0];
        }
        if (buyArrayRb.length >= 2)
        {
            changesBuyRb = buyArrayRb[buyArrayRb.length - 1] - buyArrayRb[0];
        }
    }

    private void countChangesSell() {
        if (sellArrayDollar.length >= 2)
        {
            changesSellDollar = sellArrayDollar[sellArrayDollar.length - 1] - sellArrayDollar[0];
        }
        if (sellArrayEuro.length >= 2)
        {
            changesSellEuro = sellArrayEuro[sellArrayEuro.length - 1] - sellArrayEuro[0];
        }
        if (sellArrayRb.length >= 2)
        {
            changesSellRb = sellArrayRb[sellArrayRb.length - 1] - sellArrayRb[0];
        }
    }

    public void setNewInformation(String[] date, double[] buyArrayDollar, double[] sellArrayDollar, double[] buyArrayEuro, double[] sellArrayEuro, double[] buyArrayRb, double[] sellArrayRb) {
        this.dateArray = date;
        this.buyArrayDollar = buyArrayDollar;
        this.sellArrayDollar = sellArrayDollar;
        this.buyArrayEuro = buyArrayEuro;
        this.sellArrayEuro = sellArrayEuro;
        this.buyArrayRb = buyArrayRb;
        this.sellArrayRb = sellArrayRb;

        this.date = dateArray[dateArray.length - 1];
        this.buyDollar = buyArrayDollar[buyArrayDollar.length - 1];
        this.sellDollar = sellArrayDollar[sellArrayDollar.length - 1];
        this.buyEuro = buyArrayEuro[buyArrayEuro.length - 1];
        this.sellEuro = sellArrayEuro[sellArrayEuro.length - 1];
        this.buyRb = buyArrayRb[buyArrayRb.length - 1];
        this.sellRb = sellArrayRb[sellArrayRb.length - 1];
    }


    protected MBank(Parcel in) {
        date = in.readString();
        buyDollar = in.readDouble();
        sellDollar = in.readDouble();
        buyEuro = in.readDouble();
        sellEuro = in.readDouble();
        buyRb = in.readDouble();
        sellRb = in.readDouble();
        buyArrayDollar = in.createDoubleArray();
        sellArrayDollar = in.createDoubleArray();
        buyArrayEuro = in.createDoubleArray();
        sellArrayEuro = in.createDoubleArray();
        buyArrayRb = in.createDoubleArray();
        sellArrayRb = in.createDoubleArray();
    }

    public static final Creator<MBank> CREATOR = new Creator<MBank>() {
        @Override
        public MBank createFromParcel(Parcel in) {
            return new MBank(in);
        }

        @Override
        public MBank[] newArray(int size) {
            return new MBank[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeDouble(buyDollar);
        dest.writeDouble(sellDollar);
        dest.writeDouble(buyEuro);
        dest.writeDouble(sellEuro);
        dest.writeDouble(buyRb);
        dest.writeDouble(sellRb);
        dest.writeDoubleArray(buyArrayDollar);
        dest.writeDoubleArray(sellArrayDollar);
        dest.writeDoubleArray(buyArrayEuro);
        dest.writeDoubleArray(sellArrayEuro);
        dest.writeDoubleArray(buyArrayRb);
        dest.writeDoubleArray(sellArrayRb);
    }
}
