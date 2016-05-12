package com.sergey.currencyexchange.model;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Sergey on 04.05.2016.
 */
public class MBank {

    private ArrayList<String> date;
    private ArrayList<Double> buy;
    private ArrayList<Double> sell;
    private double changesBuy = 0;
    private double changesSell = 0;


    public MBank(ArrayList<String> date, ArrayList<Double> buy, ArrayList<Double> sell)
    {
        this.date = date;
        this.buy = buy;
        this.sell = sell;
    }


    public double getBuy() {
        return buy.get(buy.size() - 1);
    }

    public double getSell() {
        return sell.get(sell.size() - 1);
    }

    public String getDate() {
        return date.get(date.size() - 1);
    }

    public ArrayList<Double> getBuyArray() {
        return buy;
    }

    public ArrayList<Double> getSellArray() {
        return sell;
    }

    public ArrayList<String> getDateArray() {
        return date;
    }

    public double getChangesBuy()
    {
        countChangesBuy();
        return changesBuy;
    }

    public double getChangesSell()
    {
        countChangesSell();
        return changesSell;
    }

    /* Changes counted from last value to first value of the day*/
    private double countChangesBuy()
    {
        if (buy.size() >= 2)
        {
            changesBuy = buy.get(buy.size() - 1) - buy.get(0);
        }
        return changesBuy;
    }

    private double countChangesSell()
    {
        if (sell.size() >= 2)
        {
            changesSell = sell.get(sell.size() - 1) - sell.get(0);
        }
        return changesSell;
    }

    public void setNewInformation(ArrayList<String> date, ArrayList<Double> buy, ArrayList<Double> sell)
    {
        this.date = date;
        this.buy = buy;
        this.sell = sell;
    }
}
