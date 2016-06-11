package com.sergey.currencyexchange.model;


public class MBank {

    private static final String URLTYPE = "app_response_mb.php";
    private static final int[] idTimeArray = {10, 11, 12, 13, 14, 15, 16, 17};
    private String[] dateArray;
    private double[] buyArrayDollar;
    private double[] sellArrayDollar;
    private double[] buyArrayEuro;
    private double[] sellArrayEuro;
    private double[] buyArrayRb;
    private double[] sellArrayRb;
    private String date;
    private double buyDollar;
    private double buyEuro;
    private double buyRb;
    private double sellDollar;
    private double sellEuro;
    private double sellRb;
    private double changesBuyDollar = 0;
    private double changesSellDollar = 0;
    private double changesBuyEuro = 0;
    private double changesSellEuro = 0;
    private double changesBuyRb = 0;
    private double changesSellRb = 0;
    private static final int DOLLAR = 0;
    private static final int EURO = 1;
    private static final int RB = 2;

    public MBank() {
        this.dateArray = new String[0];
        this.buyArrayDollar = new double[0];
        this.sellArrayDollar = new double[0];
        this.buyArrayEuro = new double[0];
        this.sellArrayEuro = new double[0];
        this.buyArrayRb = new double[0];
        this.sellArrayRb = new double[0];
    }

    public String getDate() {
        return date;
    }


    public double getBuy(int currencyId) {
        switch (currencyId)
        {
            case DOLLAR:
                return buyDollar;
            case EURO:
                return buyEuro;
            case RB:
                return buyRb;
        }
        return buyDollar;
    }


    public double getSell(int currencyId) {
        switch (currencyId)
        {
            case DOLLAR:
                return sellDollar;
            case EURO:
                return sellEuro;
            case RB:
                return sellRb;
        }
        return sellDollar;
    }


    public double getChangesBuy(int currencyId) {
        countChangesBuy();
        switch (currencyId)
        {
            case DOLLAR:
                return changesBuyDollar;
            case EURO:
                return changesBuyEuro;
            case RB:
                return changesBuyRb;
        }
        return changesBuyDollar;
    }

    public double getChangesSell(int currencyId) {
        countChangesSell();
        switch (currencyId)
        {
            case DOLLAR:
                return changesSellDollar;
            case EURO:
                return changesSellEuro;
            case RB:
                return changesSellRb;
        }
        return changesSellDollar;
    }

    /* Changes counted from last value to first value of the day*/
    private void countChangesBuy() {
        if (buyArrayDollar.length >= 2)
        {
            changesBuyDollar = buyArrayDollar[buyArrayDollar.length - 1] - buyArrayDollar[0];
        }
        if (buyArrayEuro.length >= 2)
        {
            changesBuyEuro = buyArrayEuro[buyArrayEuro.length - 1] - buyArrayEuro[0];
        }
        if (buyArrayRb.length >= 2)
        {
            changesBuyRb = buyArrayRb[buyArrayRb.length - 1] - buyArrayRb[0];
        }
    }

    private void countChangesSell() {
        if (sellArrayDollar.length >= 2)
        {
            changesSellDollar = sellArrayDollar[sellArrayDollar.length - 1] - sellArrayDollar[0];
        }
        if (sellArrayEuro.length >= 2)
        {
            changesSellEuro = sellArrayEuro[sellArrayEuro.length - 1] - sellArrayEuro[0];
        }
        if (sellArrayRb.length >= 2)
        {
            changesSellRb = sellArrayRb[sellArrayRb.length - 1] - sellArrayRb[0];
        }
    }

    public void setNewInformation(String[] date, double[] buyArrayDollar, double[] sellArrayDollar, double[] buyArrayEuro, double[] sellArrayEuro, double[] buyArrayRb, double[] sellArrayRb) {
        this.dateArray = date;
        this.buyArrayDollar = buyArrayDollar;
        this.sellArrayDollar = sellArrayDollar;
        this.buyArrayEuro = buyArrayEuro;
        this.sellArrayEuro = sellArrayEuro;
        this.buyArrayRb = buyArrayRb;
        this.sellArrayRb = sellArrayRb;

        if (dateArray.length == 0) {
            this.date = "нет информации";
        }
        else if (dateArray.length < 2) {
            this.date = dateArray[0];
        }
        else {
            this.date = dateArray[dateArray.length - 1];
        }

        if (buyArrayDollar.length == 0) {
            this.buyDollar = 0;
        }
        else if (buyArrayDollar.length < 2) {
            this.buyDollar = buyArrayDollar[0];
        }
        else {
            this.buyDollar = buyArrayDollar[buyArrayDollar.length - 1];
        }

        if (sellArrayDollar.length == 0) {
            this.sellDollar = 0;
        }
        else if (sellArrayDollar.length < 2) {
            this.sellDollar = sellArrayDollar[0];
        }
        else {
            this.sellDollar = sellArrayDollar[sellArrayDollar.length - 1];
        }

        if (buyArrayEuro.length == 0) {
            this.buyEuro = 0;
        }
        else if (buyArrayEuro.length < 2) {
            this.buyEuro = buyArrayEuro[0];
        }
        else {
            this.buyEuro = buyArrayEuro[buyArrayEuro.length - 1];
        }

        if (sellArrayEuro.length == 0) {
            this.sellEuro = 0;
        }
        else if (sellArrayEuro.length < 2) {
            this.sellEuro = sellArrayEuro[0];
        }
        else {
            this.sellEuro = sellArrayEuro[sellArrayEuro.length - 1];
        }


        if (buyArrayRb.length == 0) {
            this.buyRb = 0;
        }
        else if (buyArrayRb.length < 2) {
            this.buyRb = buyArrayRb[0];
        }
        else {
            this.buyRb = buyArrayRb[buyArrayRb.length - 1];
        }

        if (sellArrayRb.length == 0) {
            this.sellRb = 0;
        }
        else if (sellArrayRb.length < 2) {
            this.sellRb = sellArrayRb[0];
        }
        else {
            this.sellRb = sellArrayRb[sellArrayRb.length - 1];
        }
    }

    public static String getUrlType() {
        return URLTYPE;
    }
}
