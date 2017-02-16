package com.sergey.currencyexchange.model;


public class Currency {

    private String countryFlag;
    private String icon;
    private String name;
    private String country;
    private String description;
    private double rate;
    private double rateSell;
    private String date;
    private int currencyId;

    //For selectCurrencyList
    public Currency(String countryFlag, String icon, String country, String name, int currencyId) {
        this.countryFlag = countryFlag;
        this.icon = icon;
        this.country = country;
        this.name = name;
        this.currencyId = currencyId;
    }

    public Currency(String countryFlag, String icon, String country, int currencyId) {
        this.countryFlag = countryFlag;
        this.icon = icon;
        this.country = country;
        this.currencyId = currencyId;
    }

    public Currency(String countryFlag, String name, String description, String country) {
        this.countryFlag = countryFlag;
        this.name = name;
        this.description = description;
        this.country = country;
    }

    //int type needs only to have different constructor with the same types of attributes
    public Currency(String countryFlag, String icon, String name, int currencyId, int type) {
        this.countryFlag = countryFlag;
        this.currencyId = currencyId;
        this.icon = icon;
        this.name = name;
    }

    public Currency(String name) {
        this.name = name;
    }

    public String getCountryFlag() {
        return  this.countryFlag;
    }

    public String getIconCurrency() {
        return  this.icon;
    }

    public String getDate() {
        return this.date;
    }

    public String getName() {
        return this.name;
    }

    public String getCountry() {
        return this.country;
    }

    public int getCurrencyId() {
        return this.currencyId;
    }


    public double getRate() {
        return this.rate;
    }

    public String getDescription() {
        return this.description;
    }

    public void setNewInformation(double rate, String date) {
        this.rate = rate;
        this.date = date;
    }
}
