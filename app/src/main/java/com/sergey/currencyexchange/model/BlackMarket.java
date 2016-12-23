package com.sergey.currencyexchange.model;


public class BlackMarket {


    private String date;
    private double buyUSD = 0;
    private double sellUSD = 0;
    private double buyEUR = 0;
    private double sellEUR = 0;
    private double buyRUB = 0;
    private double sellRUB = 0;
    private double buyUAH = 1;
    private double sellUAH = 1;
    private double changesBuyUSD = 0;
    private double changesSellUSD = 0;
    private double changesBuyEUR = 0;
    private double changesSellEUR = 0;
    private double changesBuyRUB = 0;
    private double changesSellRUB = 0;

    public String getDate() {
        return date;
    }

    public double[] getRatesConv(int fromCurrency, int toCurrency) {
        double[] rates = new double[2];

        switch (fromCurrency) {
            case Utils.usd:
                rates[0] = buyUSD;
                break;
            case Utils.eur:
                rates[0] = buyEUR;
                break;
            case Utils.rb:
                rates[0] = buyRUB;
                break;
            case Utils.uah:
                rates[0] = buyUAH;
                break;
            default:
                rates[0] = buyUSD;
                break;
        }

        switch (toCurrency) {
            case Utils.usd:
                rates[1] = sellUSD;
                break;
            case Utils.eur:
                rates[1] = sellEUR;
                break;
            case Utils.rb:
                rates[1] = sellRUB;
                break;
            case Utils.uah:
                rates[1] = sellUAH;
                break;
            default:
                rates[1] = sellUSD;
                break;
        }

        return rates;
    }

    public double getBuy(int currencyId) {
        switch (currencyId)
        {
            case Utils.usd:
                return buyUSD;
            case Utils.eur:
                return buyEUR;
            case Utils.rb:
                return buyRUB;
            case Utils.uah:
                return buyUAH;
        }
        return buyUSD;
    }

    public double[] getBuy() {
        double[] buys = {buyUSD, buyEUR, buyRUB};
        return buys;
    }

    public double getSell(int currencyId) {
        switch (currencyId)
        {
            case Utils.usd:
                return sellUSD;
            case Utils.eur:
                return sellEUR;
            case Utils.rb:
                return sellRUB;
            case Utils.uah:
                return sellUAH;
        }
        return sellUSD;
    }

    public double[] getSell() {
        double[] sells = {sellUSD, sellEUR, sellRUB};
        return sells;
    }

    public double getChangesBuy(int currencyId) {
        switch (currencyId)
        {
            case Utils.usd:
                return changesBuyUSD;
            case Utils.eur:
                return changesBuyEUR;
            case Utils.rb:
                return changesBuyRUB;
        }
        return changesBuyUSD;
    }

    public double getChangesSell(int currencyId) {
        switch (currencyId)
        {
            case Utils.usd:
                return changesSellUSD;
            case Utils.eur:
                return changesSellEUR;
            case Utils.rb:
                return changesSellRUB;
        }
        return changesSellUSD;
    }

    public void setNewInformation(String date, double buyDollar, double sellDollar, double buyEuro, double sellEuro, double buyRb, double sellRb, double changesBuyD, double changesSellD, double changesBuyE, double changesSellE, double changesBuyR, double changesSellR) {
        this.date = date;
        this.buyUSD = buyDollar;
        this.sellUSD = sellDollar;
        this.buyEUR = buyEuro;
        this.sellEUR = sellEuro;
        this.buyRUB = buyRb;
        this.sellRUB = sellRb;
        this.changesBuyUSD = changesBuyD;
        this.changesSellUSD = changesSellD;
        this.changesBuyEUR = changesBuyE;
        this.changesSellEUR = changesSellE;
        this.changesBuyRUB = changesBuyR;
        this.changesSellRUB = changesSellR;
    }
}
