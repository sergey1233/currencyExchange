package com.sergey.currencyexchange.model;

import java.util.Map;


public class Bank  {

    private String icon;
    private String name;
    private String date;

    private double buyUSD = 0;
    private double sellUSD = 0;
    private double buyEUR = 0;
    private double sellEUR = 0;
    private double buyRUB = 0;
    private double sellRUB = 0;
    private double buyGBP = 0;
    private double sellGBP = 0;
    private double buyCHF = 0;
    private double sellCHF = 0;
    private double buyTYR = 0;
    private double sellTYR = 0;
    private double buyCAD = 0;
    private double sellCAD = 0;
    private double buyPLN = 0;
    private double sellPLN = 0;
    private double buyILS = 0;
    private double sellILS = 0;
    private double buyCNY = 0;
    private double sellCNY = 0;
    private double buyCZK = 0;
    private double sellCZK = 0;
    private double buySEK = 0;
    private double sellSEK = 0;
    private double buyJPY = 0;
    private double sellJPY = 0;
    private double buyUAH = 0;
    private double sellUAH = 0;

    private double changesBuyUSD = 0;
    private double changesSellUSD = 0;
    private double changesBuyEUR = 0;
    private double changesSellEUR = 0;
    private double changesBuyRUB = 0;
    private double changesSellRUB = 0;
    private double changesBuyGBP = 0;
    private double changesSellGBP = 0;
    private double changesBuyCHF = 0;
    private double changesSellCHF = 0;
    private double changesBuyTYR = 0;
    private double changesSellTYR = 0;
    private double changesBuyCAD = 0;
    private double changesSellCAD = 0;
    private double changesBuyPLN = 0;
    private double changesSellPLN = 0;
    private double changesBuyILS = 0;
    private double changesSellILS = 0;
    private double changesBuyCNY = 0;
    private double changesSellCNY = 0;
    private double changesBuyCZK = 0;
    private double changesSellCZK = 0;
    private double changesBuySEK = 0;
    private double changesSellSEK = 0;
    private double changesBuyJPY = 0;
    private double changesSellJPY = 0;
    private double changesBuyUAH = 0;
    private double changesSellUAH = 0;


    public Bank(String icon, String name) {
        this.name = name;
        this.icon = icon;
        setRatesCurrencyThisCountry();
    }

    public void setRatesCurrencyThisCountry() {
        switch (Utils.country_code) {
            case Utils.USA_CODE:
                buyUSD = 1;
                sellUSD = 1;
                break;
            case Utils.UK_CODE:
                buyGBP = 1;
                sellGBP = 1;
                break;
            case Utils.EUROPE_CODE:
                buyEUR = 1;
                sellEUR = 1;
                break;
            case Utils.POLAND_CODE:
                buyPLN = 1;
                sellPLN = 1;
                break;
            case Utils.TURKEY_CODE:
                buyTYR = 1;
                sellTYR = 1;
                break;
            case Utils.RUSSIA_CODE:
                buyRUB = 1;
                sellRUB = 1;
                break;
            case Utils.UKRAINE_CODE:
                buyUAH = 1;
                sellUAH = 1;
                break;
            default:
                buyGBP = 1;
                sellGBP = 1;
                break;
        }
    }

    public String getIcon() {
        return icon;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public double[] getRateConv(int fromCurrencyId, int toCurrencyId) {
        double[] rates = new double[2];

        //Different meaning in buy sell in Uk and Ukraine
        if (Utils.country_code == Utils.UK_CODE) {
            switch (fromCurrencyId) {
                case Utils.usd:
                    rates[0] = getSell(Utils.usd);
                    break;
                case Utils.eur:
                    rates[0] = getSell(Utils.eur);
                    break;
                case Utils.rb:
                    rates[0] = getSell(Utils.rb);
                    break;
                case Utils.gbp:
                    rates[0] = getSell(Utils.gbp);
                    break;
                case Utils.chf:
                    rates[0] = getSell(Utils.chf);
                    break;
                case Utils.tyr:
                    rates[0] = getSell(Utils.tyr);
                    break;
                case Utils.cad:
                    rates[0] = getSell(Utils.cad);
                    break;
                case Utils.pln:
                    rates[0] = getSell(Utils.pln);
                    break;
                case Utils.ils:
                    rates[0] = getSell(Utils.ils);
                    break;
                case Utils.cny:
                    rates[0] = getSell(Utils.cny);
                    break;
                case Utils.czk:
                    rates[0] = getSell(Utils.czk);
                    break;
                case Utils.sek:
                    rates[0] = getSell(Utils.sek);
                    break;
                case Utils.jpy:
                    rates[0] = getSell(Utils.jpy);
                    break;
                case Utils.uah:
                    rates[0] = getSell(Utils.uah);
                    break;
                default:
                    rates[0] = getSell(Utils.usd);
                    break;
            }

            switch (toCurrencyId) {
                case Utils.usd:
                    rates[1] = getBuy(Utils.usd);
                    break;
                case Utils.eur:
                    rates[1] = getBuy(Utils.eur);
                    break;
                case Utils.rb:
                    rates[1] = getBuy(Utils.rb);
                    break;
                case Utils.gbp:
                    rates[1] = getBuy(Utils.gbp);
                    break;
                case Utils.chf:
                    rates[1] = getBuy(Utils.chf);
                    break;
                case Utils.tyr:
                    rates[1] = getBuy(Utils.tyr);
                    break;
                case Utils.cad:
                    rates[1] = getBuy(Utils.cad);
                    break;
                case Utils.pln:
                    rates[1] = getBuy(Utils.pln);
                    break;
                case Utils.ils:
                    rates[1] = getBuy(Utils.ils);
                    break;
                case Utils.cny:
                    rates[1] = getBuy(Utils.cny);
                    break;
                case Utils.czk:
                    rates[1] = getBuy(Utils.czk);
                    break;
                case Utils.sek:
                    rates[1] = getBuy(Utils.sek);
                    break;
                case Utils.jpy:
                    rates[1] = getBuy(Utils.jpy);
                    break;
                case Utils.uah:
                    rates[1] = getBuy(Utils.uah);
                    break;
                default:
                    rates[1] = getBuy(Utils.usd);
                    break;
            }
        }
        else if (Utils.country_code == Utils.UKRAINE_CODE || Utils.country_code == Utils.POLAND_CODE || Utils.country_code == Utils.RUSSIA_CODE || Utils.country_code == Utils.TURKEY_CODE) {
            switch (fromCurrencyId) {
                case Utils.usd:
                    rates[0] = getBuy(Utils.usd);
                    break;
                case Utils.eur:
                    rates[0] = getBuy(Utils.eur);
                    break;
                case Utils.rb:
                    rates[0] = getBuy(Utils.rb);
                    break;
                case Utils.gbp:
                    rates[0] = getBuy(Utils.gbp);
                    break;
                case Utils.chf:
                    rates[0] = getBuy(Utils.chf);
                    break;
                case Utils.tyr:
                    rates[0] = getBuy(Utils.tyr);
                    break;
                case Utils.cad:
                    rates[0] = getBuy(Utils.cad);
                    break;
                case Utils.pln:
                    rates[0] = getBuy(Utils.pln);
                    break;
                case Utils.ils:
                    rates[0] = getBuy(Utils.ils);
                    break;
                case Utils.cny:
                    rates[0] = getBuy(Utils.cny);
                    break;
                case Utils.czk:
                    rates[0] = getBuy(Utils.czk);
                    break;
                case Utils.sek:
                    rates[0] = getBuy(Utils.sek);
                    break;
                case Utils.jpy:
                    rates[0] = getBuy(Utils.jpy);
                    break;
                case Utils.uah:
                    rates[0] = getBuy(Utils.uah);
                    break;
                default:
                    rates[0] = getBuy(Utils.usd);
                    break;
            }

            switch (toCurrencyId) {
                case Utils.usd:
                    rates[1] = getSell(Utils.usd);
                    break;
                case Utils.eur:
                    rates[1] = getSell(Utils.eur);
                    break;
                case Utils.rb:
                    rates[1] = getSell(Utils.rb);
                    break;
                case Utils.gbp:
                    rates[1] = getSell(Utils.gbp);
                    break;
                case Utils.chf:
                    rates[1] = getSell(Utils.chf);
                    break;
                case Utils.tyr:
                    rates[1] = getSell(Utils.tyr);
                    break;
                case Utils.cad:
                    rates[1] = getSell(Utils.cad);
                    break;
                case Utils.pln:
                    rates[1] = getSell(Utils.pln);
                    break;
                case Utils.ils:
                    rates[1] = getSell(Utils.ils);
                    break;
                case Utils.cny:
                    rates[1] = getSell(Utils.cny);
                    break;
                case Utils.czk:
                    rates[1] = getSell(Utils.czk);
                    break;
                case Utils.sek:
                    rates[1] = getSell(Utils.sek);
                    break;
                case Utils.jpy:
                    rates[1] = getSell(Utils.jpy);
                    break;
                case Utils.uah:
                    rates[1] = getSell(Utils.uah);
                    break;
                default:
                    rates[1] = getSell(Utils.usd);
                    break;
            }
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
            case Utils.gbp:
                return buyGBP;
            case Utils.chf:
                return buyCHF;
            case Utils.tyr:
                return buyTYR;
            case Utils.cad:
                return buyCAD;
            case Utils.pln:
                return buyPLN;
            case Utils.ils:
                return buyILS;
            case Utils.cny:
                return buyCNY;
            case Utils.czk:
                return buyCZK;
            case Utils.sek:
                return buySEK;
            case Utils.jpy:
                return buyJPY;
            case Utils.uah:
                return buyUAH;
            default:
                return buyUSD;
        }
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
            case Utils.gbp:
                return sellGBP;
            case Utils.chf:
                return sellCHF;
            case Utils.tyr:
                return sellTYR;
            case Utils.cad:
                return sellCAD;
            case Utils.pln:
                return sellPLN;
            case Utils.ils:
                return sellILS;
            case Utils.cny:
                return sellCNY;
            case Utils.czk:
                return sellCZK;
            case Utils.sek:
                return sellSEK;
            case Utils.jpy:
                return sellJPY;
            case Utils.uah:
                return sellUAH;
            default:
                return sellUSD;
        }
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
            case Utils.gbp:
                return changesBuyGBP;
            case Utils.chf:
                return changesBuyCHF;
            case Utils.tyr:
                return changesBuyTYR;
            case Utils.cad:
                return changesBuyCAD;
            case Utils.pln:
                return changesBuyPLN;
            case Utils.ils:
                return changesBuyILS;
            case Utils.cny:
                return changesBuyCNY;
            case Utils.czk:
                return changesBuyCZK;
            case Utils.sek:
                return changesBuySEK;
            case Utils.jpy:
                return changesBuyJPY;
            case Utils.uah:
                return changesBuyUAH;
            default:
                return changesBuyUSD;
        }
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
            case Utils.gbp:
                return changesSellGBP;
            case Utils.chf:
                return changesSellCHF;
            case Utils.tyr:
                return changesSellTYR;
            case Utils.cad:
                return changesSellCAD;
            case Utils.pln:
                return changesSellPLN;
            case Utils.ils:
                return changesSellILS;
            case Utils.cny:
                return changesSellCNY;
            case Utils.czk:
                return changesSellCZK;
            case Utils.sek:
                return changesSellSEK;
            case Utils.jpy:
                return changesSellJPY;
            case Utils.uah:
                return changesSellUAH;
            default:
                return changesSellUSD;
        }
    }

    public void setNewInformationUkraine(String date, double buyDollar, double sellDollar, double buyEuro, double sellEuro, double buyRb, double sellRb, double changesBuyD, double changesSellD, double changesBuyE, double changesSellE, double changesBuyR, double changesSellR) {
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

    public void setNewInformation(String date, Map<Integer, Double> buyRates, Map<Integer, Double> sellRates, Map<Integer, Double> buyChanges, Map<Integer, Double> sellChanges) {
        this.date = date;
        parseMap(buyRates, 0);
        parseMap(sellRates, 1);
        parseMap(buyChanges, 2);
        parseMap(sellChanges, 3);
    }

    public void parseMap(Map<Integer, Double> mapMain, int type) { //0 - buyRates, 1 - sellRates, 2 - buyChanges, 3 - sellChanges
        for (Map.Entry map : mapMain.entrySet()) {
            switch ((int)map.getKey()) {
                case Utils.usd:
                    switch (type) {
                        case 0:
                            this.buyUSD = (double)map.getValue();
                            break;
                        case 1:
                            this.sellUSD = (double)map.getValue();
                            break;
                        case 2:
                            this.changesBuyUSD = (double)map.getValue();
                            break;
                        case 3:
                            this.changesSellUSD = (double)map.getValue();
                            break;
                        default:
                            break;
                    }
                    break;
                case Utils.eur:
                    switch (type) {
                        case 0:
                            this.buyEUR = (double)map.getValue();
                            break;
                        case 1:
                            this.sellEUR = (double)map.getValue();
                            break;
                        case 2:
                            this.changesBuyEUR = (double)map.getValue();
                            break;
                        case 3:
                            this.changesSellEUR = (double)map.getValue();
                            break;
                        default:
                            break;
                    }
                    break;
                case Utils.rb:
                    switch (type) {
                        case 0:
                            this.buyRUB = (double)map.getValue();
                            break;
                        case 1:
                            this.sellRUB = (double)map.getValue();
                            break;
                        case 2:
                            this.changesBuyRUB = (double)map.getValue();
                            break;
                        case 3:
                            this.changesSellRUB = (double)map.getValue();
                            break;
                        default:
                            break;
                    }
                    break;
                case Utils.gbp:
                    switch (type) {
                        case 0:
                            this.buyGBP = (double)map.getValue();
                            break;
                        case 1:
                            this.sellGBP = (double)map.getValue();
                            break;
                        case 2:
                            this.changesBuyGBP = (double)map.getValue();
                            break;
                        case 3:
                            this.changesSellGBP = (double)map.getValue();
                            break;
                        default:
                            break;
                    }
                    break;
                case Utils.chf:
                    switch (type) {
                        case 0:
                            this.buyCHF = (double)map.getValue();
                            break;
                        case 1:
                            this.sellCHF = (double)map.getValue();
                            break;
                        case 2:
                            this.changesBuyCHF = (double)map.getValue();
                            break;
                        case 3:
                            this.changesSellCHF = (double)map.getValue();
                            break;
                        default:
                            break;
                    }
                    break;
                case Utils.tyr:
                    switch (type) {
                        case 0:
                            this.buyTYR = (double)map.getValue();
                            break;
                        case 1:
                            this.sellTYR = (double)map.getValue();
                            break;
                        case 2:
                            this.changesBuyTYR = (double)map.getValue();
                            break;
                        case 3:
                            this.changesSellTYR = (double)map.getValue();
                            break;
                        default:
                            break;
                    }
                    break;
                case Utils.cad:
                    switch (type) {
                        case 0:
                            this.buyCAD = (double)map.getValue();
                            break;
                        case 1:
                            this.sellCAD = (double)map.getValue();
                            break;
                        case 2:
                            this.changesBuyCAD = (double)map.getValue();
                            break;
                        case 3:
                            this.changesSellCAD = (double)map.getValue();
                            break;
                        default:
                            break;
                    }
                    break;
                case Utils.pln:
                    switch (type) {
                        case 0:
                            this.buyPLN = (double)map.getValue();
                            break;
                        case 1:
                            this.sellPLN = (double)map.getValue();
                            break;
                        case 2:
                            this.changesBuyPLN = (double)map.getValue();
                            break;
                        case 3:
                            this.changesSellPLN = (double)map.getValue();
                            break;
                        default:
                            break;
                    }
                    break;
                case Utils.ils:
                    switch (type) {
                        case 0:
                            this.buyILS = (double)map.getValue();
                            break;
                        case 1:
                            this.sellILS = (double)map.getValue();
                            break;
                        case 2:
                            this.changesBuyILS = (double)map.getValue();
                            break;
                        case 3:
                            this.changesSellILS = (double)map.getValue();
                            break;
                        default:
                            break;
                    }
                    break;
                case Utils.cny:
                    switch (type) {
                        case 0:
                            this.buyCNY = (double)map.getValue();
                            break;
                        case 1:
                            this.sellCNY = (double)map.getValue();
                            break;
                        case 2:
                            this.changesBuyCNY = (double)map.getValue();
                            break;
                        case 3:
                            this.changesSellCNY = (double)map.getValue();
                            break;
                        default:
                            break;
                    }
                    break;
                case Utils.czk:
                    switch (type) {
                        case 0:
                            this.buyCZK = (double)map.getValue();
                            break;
                        case 1:
                            this.sellCZK = (double)map.getValue();
                            break;
                        case 2:
                            this.changesBuyCZK = (double)map.getValue();
                            break;
                        case 3:
                            this.changesSellCZK = (double)map.getValue();
                            break;
                        default:
                            break;
                    }
                    break;
                case Utils.sek:
                    switch (type) {
                        case 0:
                            this.buySEK = (double)map.getValue();
                            break;
                        case 1:
                            this.sellSEK = (double)map.getValue();
                            break;
                        case 2:
                            this.changesBuySEK = (double)map.getValue();
                            break;
                        case 3:
                            this.changesSellSEK = (double)map.getValue();
                            break;
                        default:
                            break;
                    }
                    break;
                case Utils.jpy:
                    switch (type) {
                        case 0:
                            this.buyJPY = (double)map.getValue();
                            break;
                        case 1:
                            this.sellJPY = (double)map.getValue();
                            break;
                        case 2:
                            this.changesBuyJPY = (double)map.getValue();
                            break;
                        case 3:
                            this.changesSellJPY = (double)map.getValue();
                            break;
                        default:
                            break;
                    }
                    break;
                case Utils.uah:
                    switch (type) {
                        case 0:
                            this.buyUAH = (double)map.getValue();
                            break;
                        case 1:
                            this.sellUAH = (double)map.getValue();
                            break;
                        case 2:
                            this.changesBuyUAH = (double)map.getValue();
                            break;
                        case 3:
                            this.changesSellUAH = (double)map.getValue();
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    switch (type) {
                        case 0:
                            this.buyUSD = (double)map.getValue();
                            break;
                        case 1:
                            this.sellUSD = (double)map.getValue();
                            break;
                        case 2:
                            this.changesBuyUSD = (double)map.getValue();
                            break;
                        case 3:
                            this.changesSellUSD = (double)map.getValue();
                            break;
                        default:
                            break;
                    }
                    break;
            }
        }
    }
}

