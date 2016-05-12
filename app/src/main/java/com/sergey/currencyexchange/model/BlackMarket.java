package com.sergey.currencyexchange.model;

/**
 * Created by Sergey on 04.05.2016.
 */
public class BlackMarket {
    private String date;
    private double buy;
    private double sell;
    private double changesBuy = 0;
    private double changesSell = 0;

    public BlackMarket(String date, double buy, double sell)
    {
        this.date = date;
        this.buy = buy;
        this.sell = sell;
    }


    public double getBuy() {
        return buy;
    }

    public double getSell() {
        return sell;
    }

    public double getChangesBuy()
    {
        return changesBuy;
    }

    public double getChangesSell()
    {
        return changesSell;
    }

    public String getDate() {
        return date;
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

}
