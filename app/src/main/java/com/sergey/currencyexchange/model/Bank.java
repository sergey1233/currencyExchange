package com.sergey.currencyexchange.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sergey on 04.05.2016.
 */
public class Bank implements Parcelable {
    private String icon = "bank_icon_";
    private String name;
    private String date;
    private double buy;
    private double changesBuy = 0;
    private double sell;
    private double changesSell = 0;

    public Bank() {}

    public Bank(String icon, String name)
    {
        this.name = name;
        this.icon += icon;
    }

    public Bank(String icon, String name, String date, double buy, double sell)
    {
        this.date = date;
        this.icon += icon;
        this.name = name;
        this.buy = buy;
        this.sell = sell;
    }

    public String getIcon() {
        return icon;
    }

    public double getBuy() {
        return buy;
    }

    public double getChangesBuy() {
        return changesBuy;
    }

    public double getSell() {
        return sell;
    }

    public double getChangesSell() {
        return changesSell;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public void setBuy(double buy) {
        this.buy = buy;
    }


    public double countBuyChanges(double buy)
    {
        return this.buy - buy;
    }

    public double countSellChanges(double sell)
    {
        return this.sell - sell;
    }

    /* Changes counted from new value to current value */
    public void countChangesBuy(double buy)
    {
        changesBuy = buy - this.buy;
    }

    public void countChangesSell(double sell)
    {
        changesSell = sell - this.sell;
    }

    public void setNewInformation(String date, double buy, double sell)
    {
        countChangesBuy(buy);
        countChangesSell(sell);
        this.date = date;
        this.buy = buy;
        this.sell = sell;
    }


    protected Bank(Parcel in) {
        icon = in.readString();
        name = in.readString();
        buy = in.readDouble();
        sell = in.readDouble();
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
        dest.writeDouble(buy);
        dest.writeDouble(sell);
    }
}

