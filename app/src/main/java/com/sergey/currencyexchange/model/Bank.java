package com.sergey.currencyexchange.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Bank implements Parcelable {

    private static final int DOLLAR = 0;
    private static final int EURO = 1;
    private static final int RB = 2;
    private static final String URLTYPE = "app_response_banks.php";

    private String icon;
    private String name;
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



    public Bank(String icon, String name) {
        this.name = name;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
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


    public static String getUrlType() {
        return URLTYPE;
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


    protected Bank(Parcel in) {
        icon = in.readString();
        name = in.readString();
        date = in.readString();
        buyDollar = in.readDouble();
        sellDollar = in.readDouble();
        buyEuro = in.readDouble();
        sellEuro = in.readDouble();
        buyRb = in.readDouble();
        sellRb = in.readDouble();
        changesBuyDollar = in.readDouble();
        changesSellDollar = in.readDouble();
        changesBuyEuro = in.readDouble();
        changesSellEuro = in.readDouble();
        changesBuyRb = in.readDouble();
        changesSellRb = in.readDouble();
    }

    public static final Creator<Bank> CREATOR = new Creator<Bank>() {
        @Override
        public Bank createFromParcel(Parcel in) {
            return new Bank(in);
        }

        @Override
        public Bank[] newArray(int size) {
            return new Bank[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(icon);
        dest.writeString(name);
        dest.writeString(date);
        dest.writeDouble(buyDollar);
        dest.writeDouble(sellDollar);
        dest.writeDouble(buyEuro);
        dest.writeDouble(sellEuro);
        dest.writeDouble(buyRb);
        dest.writeDouble(sellRb);
        dest.writeDouble(changesBuyDollar);
        dest.writeDouble(changesSellDollar);
        dest.writeDouble(changesBuyEuro);
        dest.writeDouble(changesSellEuro);
        dest.writeDouble(changesBuyRb);
        dest.writeDouble(changesSellRb);
    }
}

