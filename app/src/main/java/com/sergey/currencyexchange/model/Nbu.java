package com.sergey.currencyexchange.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sergey on 04.05.2016.
 */
public class Nbu implements Parcelable {

    private double currency;
    private String date;
    private double changes = 0;

    public Nbu() {}

    public Nbu (String date, double currency)
    {
        this.date = date;
        this.currency = currency;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCurrency(double currency) {
        this.currency = currency;
    }

    public double getCurrency() {
        return currency;
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
        changes = currency - this.currency;
    }

    public void setNewInformation(String date, double currency)
    {
        countChanges(currency);
        this.currency = currency;
        this.date = date;
    }


    protected Nbu(Parcel in) {
        currency = in.readDouble();
        date = in.readString();
        changes = in.readDouble();
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
        dest.writeDouble(currency);
        dest.writeString(date);
        dest.writeDouble(changes);
    }
}
