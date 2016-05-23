package com.sergey.currencyexchange.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Sergey on 23.05.2016.
 */
public class ApplicationInfo {

    private static final ApplicationInfo instance = new ApplicationInfo();
    private int currencyId;
    private Nbu nbu;
    private MBank mBank;
    private BlackMarket blackMarket;
    private ArrayList<Bank> bankList;

    private ApplicationInfo(){}

    public static ApplicationInfo getInstance(){
        return instance;
    }

    /*public ApplicationInfo(int currencyId, Nbu nbu, MBank mBank, BlackMarket blackMarket, ArrayList<Bank> bankList) {
        this.currencyId = currencyId;
        this.nbu = nbu;
        this.mBank = mBank;
        this.blackMarket = blackMarket;
        this.bankList = bankList;
    }*/

    public int getCurrencyId() {
        return currencyId;
    }

    public Nbu getNbu() {
        return nbu;
    }

    public MBank getMBank() {
        return mBank;
    }

    public BlackMarket getBlackMarket() {
        return blackMarket;
    }

    public ArrayList<Bank> getBankList() {
        return bankList;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public void setNewApplicationInfo(int currencyId, Nbu nbu, MBank mBank, BlackMarket blackMarket, ArrayList<Bank> bankList) {
        this.currencyId = currencyId;
        this.nbu = nbu;
        this.mBank = mBank;
        this.blackMarket = blackMarket;
        this.bankList = bankList;
    }



   /* protected ApplicationInfo(Parcel in) {
        currencyId = in.readInt();
        nbu = in.readParcelable(Nbu.class.getClassLoader());
        mBank = in.readParcelable(MBank.class.getClassLoader());
        blackMarket = in.readParcelable(BlackMarket.class.getClassLoader());
        bankList = in.createTypedArrayList(Bank.CREATOR);
    }

    public static final Creator<ApplicationInfo> CREATOR = new Creator<ApplicationInfo>() {
        @Override
        public ApplicationInfo createFromParcel(Parcel in) {
            return new ApplicationInfo(in);
        }

        @Override
        public ApplicationInfo[] newArray(int size) {
            return new ApplicationInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(currencyId);
        dest.writeParcelable(nbu, flags);
        dest.writeParcelable(mBank, flags);
        dest.writeParcelable(blackMarket, flags);
        dest.writeTypedList(bankList);
    }*/
}
