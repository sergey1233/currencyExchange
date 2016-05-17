package com.sergey.currencyexchange.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Sergey on 04.05.2016.
 */
public class MBank implements Parcelable{

    private ArrayList<String> dateArray;
    private ArrayList<Double> buyArray;
    private ArrayList<Double> sellArray;
    private double buy;
    private double sell;
    private double changesBuy = 0;
    private double changesSell = 0;


    public MBank(ArrayList<String> dateArray, ArrayList<Double> buyArray, ArrayList<Double> sellArray)
    {
        this.dateArray = dateArray;
        this.buyArray = buyArray;
        this.sellArray = sellArray;
        this.buy = buyArray.get(buyArray.size() - 1);
        this.sell = sellArray.get(sellArray.size() - 1);
    }


    public double getBuy() {
        return buy;
    }

    public double getSell() {
        return sell;
    }

    public String getDate() {
        return dateArray.get(dateArray.size() - 1);
    }

    public ArrayList<Double> getBuyArray() {
        return buyArray;
    }

    public ArrayList<Double> getSellArray() {
        return sellArray;
    }

    public ArrayList<String> getDateArray() {
        return dateArray;
    }

    public double getChangesBuy() {
        countChangesBuy();
        return changesBuy;
    }

    public double getChangesSell() {
        countChangesSell();
        return changesSell;
    }

    /* Changes counted from last value to first value of the day*/
    private double countChangesBuy() {
        if (buyArray.size() >= 2)
        {
            changesBuy = buyArray.get(buyArray.size() - 1) - buyArray.get(0);
        }
        return changesBuy;
    }

    private double countChangesSell() {
        if (sellArray.size() >= 2)
        {
            changesSell = sellArray.get(sellArray.size() - 1) - sellArray.get(0);
        }
        return changesSell;
    }

    public void setNewInformation(ArrayList<String> date, ArrayList<Double> buy, ArrayList<Double> sell) {
        this.dateArray = date;
        this.buyArray = buy;
        this.sellArray = sell;
    }


    protected MBank(Parcel in) {
        buy = in.readDouble();
        sell = in.readDouble();
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
        dest.writeDouble(buy);
        dest.writeDouble(sell);
    }
}
