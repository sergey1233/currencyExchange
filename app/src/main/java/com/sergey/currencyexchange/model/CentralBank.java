package com.sergey.currencyexchange.model;


import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

public class CentralBank {

    private double rateUSD = 0;
    private double rateEUR = 0;
    private double rateRUB = 0;
    private double rateGBP = 0;
    private double rateCHF = 0;
    private double rateTYR = 0;
    private double rateCAD = 0;
    private double ratePLN = 0;
    private double rateILS = 0;
    private double rateCNY = 0;
    private double rateCZK = 0;
    private double rateSEK = 0;
    private double rateJPY = 0;
    private double rateUAH = 0;

    private String date;

    private double changesUSD = 0;
    private double changesEUR = 0;
    private double changesRUB = 0;
    private double changesGBP = 0;
    private double changesCHF = 0;
    private double changesTYR = 0;
    private double changesCAD = 0;
    private double changesPLN = 0;
    private double changesILS = 0;
    private double changesCNY = 0;
    private double changesCZK = 0;
    private double changesSEK = 0;
    private double changesJPY = 0;
    private double changesUAH = 0;



    private ArrayList<Currency> currencyList;

    public CentralBank(int countryCode) {
        currencyList = new ArrayList<>();

        switch (countryCode) {
            case Utils.USA_CODE:
                this.rateUSD = 1;
                break;
            case Utils.EUROPE_CODE:
                this.rateEUR = 1;
                break;
            case Utils.UK_CODE:
                this.rateGBP = 1;
                break;
            case Utils.POLAND_CODE:
                this.ratePLN = 1;
                break;
            case Utils.TURKEY_CODE:
                this.rateTYR = 1;
                break;
            case Utils.RUSSIA_CODE:
                this.rateRUB = 1;
                break;
            case Utils.UKRAINE_CODE:
                this.rateUAH = 1;
                break;
            default:
                this.rateGBP = 1;
                break;
        }
    }

    public String getDate() {
        return date;
    }

    public double[] getAllRates() {
        double[] allRates = new double[14];
        allRates[0] = rateUSD;
        allRates[1] = rateEUR;
        allRates[2] = rateRUB;
        allRates[3] = rateGBP;
        allRates[4] = rateCHF;
        allRates[5] = rateTYR;
        allRates[6] = rateCAD;
        allRates[7] = ratePLN;
        allRates[8] = rateILS;
        allRates[9] = rateCNY;
        allRates[10] = rateCZK;
        allRates[11] = rateSEK;
        allRates[12] = rateJPY;
        allRates[13] = rateUAH;

        return allRates;
    }

    public double getRate(int currencyId) {
        switch (currencyId)
        {
            case Utils.usd:
                return rateUSD;
            case Utils.eur:
                return rateEUR;
            case Utils.rb:
                return rateRUB;
            case Utils.gbp:
                return rateGBP;
            case Utils.chf:
                return rateCHF;
            case Utils.tyr:
                return rateTYR;
            case Utils.cad:
                return rateCAD;
            case Utils.pln:
                return ratePLN;
            case Utils.ils:
                return rateILS;
            case Utils.cny:
                return rateCNY;
            case Utils.czk:
                return rateCZK;
            case Utils.sek:
                return rateSEK;
            case Utils.jpy:
                return rateJPY;
            case Utils.uah:
                return rateUAH;
            default:
                return rateUSD;
        }
    }

    public double[] getRatesConv(int fromCurrencyId, int toCurrencyId) {
        double[] rates = new double[2];

        switch (fromCurrencyId) {
            case Utils.usd:
                rates[0] = getRate(Utils.usd);
                break;
            case Utils.eur:
                rates[0] = getRate(Utils.eur);
                break;
            case Utils.rb:
                rates[0] = getRate(Utils.rb);
                break;
            case Utils.gbp:
                rates[0] = getRate(Utils.gbp);
                break;
            case Utils.chf:
                rates[0] = getRate(Utils.chf);
                break;
            case Utils.tyr:
                rates[0] = getRate(Utils.tyr);
                break;
            case Utils.cad:
                rates[0] = getRate(Utils.cad);
                break;
            case Utils.pln:
                rates[0] = getRate(Utils.pln);
                break;
            case Utils.ils:
                rates[0] = getRate(Utils.ils);
                break;
            case Utils.cny:
                rates[0] = getRate(Utils.cny);
                break;
            case Utils.czk:
                rates[0] = getRate(Utils.czk);
                break;
            case Utils.sek:
                rates[0] = getRate(Utils.sek);
                break;
            case Utils.jpy:
                rates[0] = getRate(Utils.jpy);
                break;
            case Utils.uah:
                rates[0] = getRate(Utils.uah);
                break;
            default:
                rates[0] = getRate(Utils.usd);
                break;
        }


        switch (toCurrencyId) {
            case Utils.usd:
                rates[1] = getRate(Utils.usd);
                break;
            case Utils.eur:
                rates[1] = getRate(Utils.eur);
                break;
            case Utils.rb:
                rates[1] = getRate(Utils.rb);
                break;
            case Utils.gbp:
                rates[1] = getRate(Utils.gbp);
                break;
            case Utils.chf:
                rates[1] = getRate(Utils.chf);
                break;
            case Utils.tyr:
                rates[1] = getRate(Utils.tyr);
                break;
            case Utils.cad:
                rates[1] = getRate(Utils.cad);
                break;
            case Utils.pln:
                rates[1] = getRate(Utils.pln);
                break;
            case Utils.ils:
                rates[1] = getRate(Utils.ils);
                break;
            case Utils.cny:
                rates[1] = getRate(Utils.cny);
                break;
            case Utils.czk:
                rates[1] = getRate(Utils.czk);
                break;
            case Utils.sek:
                rates[1] = getRate(Utils.sek);
                break;
            case Utils.jpy:
                rates[1] = getRate(Utils.jpy);
                break;
            case Utils.uah:
                rates[1] = getRate(Utils.uah);
                break;
            default:
                rates[1] = getRate(Utils.usd);
                break;
        }


        return rates;
    }

    public double getChanges(int currencyId) {
        switch (currencyId)
        {
            case Utils.usd:
                return changesUSD;
            case Utils.eur:
                return changesEUR;
            case Utils.rb:
                return changesRUB;
            case Utils.gbp:
                return changesGBP;
            case Utils.chf:
                return changesCHF;
            case Utils.tyr:
                return changesTYR;
            case Utils.cad:
                return changesCAD;
            case Utils.pln:
                return changesPLN;
            case Utils.ils:
                return changesILS;
            case Utils.cny:
                return changesCNY;
            case Utils.czk:
                return changesCZK;
            case Utils.sek:
                return changesSEK;
            case Utils.jpy:
                return changesJPY;
            case Utils.uah:
                return changesUAH;
            default:
                return changesUSD;
        }
    }

    public ArrayList<Currency> getCurrencyList() {
        return currencyList;
    }

    public void setNewInformationUkraine(String date, double rateDollar, double rateEuro, double rateRb, double changesDollar, double changesEuro, double changesRb) {
        this.rateUSD = rateDollar;
        this.rateEUR = rateEuro;
        this.rateRUB = rateRb;
        this.date = date;
        this.changesUSD = changesDollar;
        this.changesEUR = changesEuro;
        this.changesRUB = changesRb;
    }

    public void setNewInformation(String date, Map<Integer, Double> mainRates, Map<Integer, Double> mainChanges) {
        this.date = date;
        parseMap(mainRates, 0);
        parseMap(mainChanges, 1);
    }

    public void parseMap(Map<Integer, Double> mapMain, int type) { //0 - rates, 1 - changes
        for (Map.Entry map : mapMain.entrySet()) {
            switch ((int)map.getKey()) {
                case Utils.usd:
                    if (type == 0) {
                        this.rateUSD = (double)map.getValue();
                    }
                    else {
                        this.changesUSD = (double)map.getValue();
                    }
                    break;
                case Utils.eur:
                    if (type == 0) {
                        this.rateEUR = (double)map.getValue();
                    }
                    else {
                        this.changesEUR = (double)map.getValue();
                    }
                    break;
                case Utils.rb:
                    if (type == 0) {
                        this.rateRUB = (double)map.getValue();
                    }
                    else {
                        this.changesRUB = (double)map.getValue();
                    }
                    break;
                case Utils.gbp:
                    if (type == 0) {
                        this.rateGBP = (double)map.getValue();
                    }
                    else {
                        this.changesGBP = (double)map.getValue();
                    }
                    break;
                case Utils.chf:
                    if (type == 0) {
                        this.rateCHF = (double)map.getValue();
                    }
                    else {
                        this.changesCHF = (double)map.getValue();
                    }
                    break;
                case Utils.tyr:
                    if (type == 0) {
                        this.rateTYR = (double)map.getValue();
                    }
                    else {
                        this.changesTYR = (double)map.getValue();
                    }
                    break;
                case Utils.cad:
                    if (type == 0) {
                        this.rateCAD = (double)map.getValue();
                    }
                    else {
                        this.changesCAD = (double)map.getValue();
                    }
                    break;
                case Utils.pln:
                    if (type == 0) {
                        this.ratePLN = (double)map.getValue();
                    }
                    else {
                        this.changesPLN = (double)map.getValue();
                    }
                    break;
                case Utils.ils:
                    if (type == 0) {
                        this.rateILS = (double)map.getValue();
                    }
                    else {
                        this.changesILS = (double)map.getValue();
                    }
                    break;
                case Utils.cny:
                    if (type == 0) {
                        this.rateCNY = (double)map.getValue();
                    }
                    else {
                        this.changesCNY = (double)map.getValue();
                    }
                    break;
                case Utils.czk:
                    if (type == 0) {
                        this.rateCZK = (double)map.getValue();
                    }
                    else {
                        this.changesCZK = (double)map.getValue();
                    }
                    break;
                case Utils.sek:
                    if (type == 0) {
                        this.rateSEK = (double)map.getValue();
                    }
                    else {
                        this.changesSEK = (double)map.getValue();
                    }
                    break;
                case Utils.jpy:
                    if (type == 0) {
                        this.rateJPY = (double)map.getValue();
                    }
                    else {
                        this.changesJPY = (double)map.getValue();
                    }
                    break;
                default:
                    if (type == 0) {
                        this.rateUSD = (double)map.getValue();
                    }
                    else {
                        this.changesUSD = (double)map.getValue();
                    }
                    break;
            }
        }
    }
}
