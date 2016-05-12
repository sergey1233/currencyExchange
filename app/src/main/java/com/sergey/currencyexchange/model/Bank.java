package com.sergey.currencyexchange.model;

/**
 * Created by Sergey on 04.05.2016.
 */
public class Bank
{
    private Integer iconId;
    private String name;
    private String date;
    private double buy;
    private Integer changeBuy;/* 1 = currency increase; -1 = currency decrease; 0 = no change*/
    private double sell;
    private Integer changeSell;/* 1 = currency increase; -1 = currency decrease; 0 = no change*/

    public Bank() {}

    public Bank(String name)
    {
        this.name = name;
    }

    public Bank(String date, Integer iconId, String name, double buy, double sell)
    {
        this.date = date;
        this.changeBuy = 0;
        this.changeSell = 0;
        this.iconId = iconId;
        this.name = name;
        this.buy = buy;
        this.sell = sell;
    }

    public Integer getIconId() {
        return iconId;
    }

    public double getBuy() {
        return buy;
    }

    public Integer getChangeBuy() {
        return changeBuy;
    }

    public double getSell() {
        return sell;
    }

    public Integer getChangeSell() {
        return changeSell;
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

    public void setSell(double sell) {
        this.sell = sell;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double countBuyChanges(double buy)
    {
        return this.buy - buy;
    }

    public double countSellChanges(double sell)
    {
        return this.sell - sell;
    }


    /* 1 = currency increase; -1 = currency decrease; 0 = no change*/
    public Integer changeBuyCount(double buy)
    {
        if (this.buy - buy > 0)
        {
            return 1;
        }
        else if (this.buy - buy < 0)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }

    /* 1 = currency increase; -1 = currency decrease; 0 = no change*/
    public Integer changeSellCount(double sell)
    {
        if (this.sell - sell > 0)
        {
            return 1;
        }
        else if (this.sell - sell < 0)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }

    public void setInformationOfBank(String date, double buy, double sell)
    {
        this.changeBuy = changeBuyCount(buy);
        this.changeSell = changeSellCount(sell);
        this.date = date;
        this.buy = buy;
        this.sell = sell;
    }
}

