package com.sergey.currencyexchange.model;

import java.util.ArrayList;

public class Utils {

    public static final int usd = 0;
    public static final int eur = 1;
    public static final int rb = 2;
    public static final int gbp = 3; //funt
    public static final int chf = 4; //switzerland frank
    public static final int tyr = 5; // turkish
    public static final int cad = 6; // canada dollar
    public static final int pln = 7; // poland zlotuy
    public static final int ils = 8; // israel shekel
    public static final int cny = 9; // china yuan
    public static final int czk = 10; //czhech crona
    public static final int sek = 11; // sweden crona
    public static final int jpy = 12; // japan yena
    public static final int uah = 13;
    public static int currencyId = 0;

    public static final int MAINACTIVITYTYPE = 0;
    public static final int CONVERTERTYPE = 1;
    public static final int SELECTCURRENCYTYPE = 2;
    public static final int SELECTCONVERTCURRENCYTYPE = 3;
    public static final int CBANKRATETYPE = 4;
    public static final int POPUPTYPE = 5;

    public static final int USA_CODE = 1;
    public static final String USA_NAME = "USA";
    public static final int EUROPE_CODE = 2;
    public static final String EUROPE_NAME = "Eurozone";
    public static final int UK_CODE = 3;
    public static final String UK_NAME = "United Kingdom";
    public static final int POLAND_CODE = 4;
    public static final String POLAND_NAME = "Poland";
    public static final int TURKEY_CODE = 5;
    public static final String TURKEY_NAME = "Turkey";
    public static final int RUSSIA_CODE = 6;
    public static final String RUSSIA_NAME = "Russia";
    public static final int UKRAINE_CODE = 7;
    public static final String UKRAINE_NAME = "Ukraine";

    public static int country_code = 0;

    public static final int DBVERSION = 24;
    public static final String NBU_DB_NAME = "nbu";
    public static final String MB_DB_NAME = "mb";
    public static final String BLACKM_DB_NAME = "blackM";
    public static final String BANKS_DB_NAME = "banks";

    public static final String TABLE_NAME_CURRENCIES_Ukraine = "currencies_ua";
    public static final String TABLE_NAME_CURRENCIES_Usa = "currencies_usa";
    public static final String TABLE_NAME_CURRENCIES_Europe = "currencies_eu";
    public static final String TABLE_NAME_CURRENCIES_Uk = "currencies_uk";
    public static final String TABLE_NAME_CBANK_UK = "cBank_uk";
    public static final String TABLE_NAME_BANKS_UK = "banks_uk";
    public static final String TABLE_NAME_CURRENCIES_Poland = "currencies_pl";
    public static final String TABLE_NAME_CBANK_POLAND = "cBank_pl";
    public static final String TABLE_NAME_BANKS_POLAND = "banks_pl";
    public static final String TABLE_NAME_CURRENCIES_Turkey = "currencies_tr";
    public static final String TABLE_NAME_CBANK_TURKEY = "cBank_tr";
    public static final String TABLE_NAME_BANKS_TURKEY = "banks_tr";
    public static final String TABLE_NAME_CURRENCIES_Russia = "currencies_ru";
    public static final String TABLE_NAME_CBANK_Russia = "cBank_ru";
    public static final String TABLE_NAME_BANKS_RUSSIA = "banks_ru";

    public static double roundResut(double number) {
        double result = number * 10000;
        int i = (int)Math.round(result);
        result = (double)i/10000;
        return result;
    }

    public static boolean isDigit(String string) {
        try {
            Double.parseDouble(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String getOfficialCurrencyTableName() {
        switch (Utils.country_code) {
            case Utils.USA_CODE:
                return TABLE_NAME_CURRENCIES_Usa;
            case Utils.EUROPE_CODE:
                return TABLE_NAME_CURRENCIES_Europe;
            case Utils.UK_CODE:
                return TABLE_NAME_CURRENCIES_Uk;
            case Utils.POLAND_CODE:
                return TABLE_NAME_CURRENCIES_Poland;
            case Utils.TURKEY_CODE:
                return TABLE_NAME_CURRENCIES_Turkey;
            case Utils.RUSSIA_CODE:
                return TABLE_NAME_CURRENCIES_Russia;
            case Utils.UKRAINE_CODE:
                return TABLE_NAME_CURRENCIES_Ukraine;
            default:
                return TABLE_NAME_CURRENCIES_Uk;
        }
    }

    public static String getBanksTableName() {
        switch (Utils.country_code) {
            case Utils.UK_CODE:
                return TABLE_NAME_BANKS_UK;
            case Utils.POLAND_CODE:
                return TABLE_NAME_BANKS_POLAND;
            case Utils.TURKEY_CODE:
                return TABLE_NAME_BANKS_TURKEY;
            case Utils.RUSSIA_CODE:
                return TABLE_NAME_BANKS_RUSSIA;
            default:
                return TABLE_NAME_BANKS_UK;
        }
    }

    public static String getCBankTableName() {
        switch (Utils.country_code) {
            case Utils.UK_CODE:
                return TABLE_NAME_CBANK_UK;
            case Utils.POLAND_CODE:
                return TABLE_NAME_CBANK_POLAND;
            case Utils.TURKEY_CODE:
                return TABLE_NAME_CBANK_TURKEY;
            case Utils.RUSSIA_CODE:
                return TABLE_NAME_CBANK_Russia;
            default:
                return TABLE_NAME_CBANK_UK;
        }
    }
}
