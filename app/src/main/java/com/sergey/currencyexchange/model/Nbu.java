package com.sergey.currencyexchange.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sergey on 04.05.2016.
 */
public class Nbu implements Parcelable {

    private double rate;
    private String date;
    private double changes = 0;

    public Nbu (String date, double rate)
    {
        this.date = date;
        this.rate = rate;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRate(double currency) {
        this.rate = currency;
    }

    public double getRate() {
        return rate;
    }

    public String getDate() {
        return date;
    }

    public double getChanges()
    {
        return changes;
    }

    /* Changes counted from new value to current value */
    public void countChanges(double currency)
    {
        changes = currency - this.rate;
    }

    public void setNewInformation(String date, double rate)
    {
        countChanges(rate);
        this.rate = rate;
        this.date = date;
    }


    protected Nbu(Parcel in) {
        rate = in.readDouble();
    }

    public static final Creator<Nbu> CREATOR = new Creator<Nbu>() {
        @Override
        public Nbu createFromParcel(Parcel in) {
            return new Nbu(in);
        }

        @Override
        public Nbu[] newArray(int size) {
            return new Nbu[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(rate);
    }
}
