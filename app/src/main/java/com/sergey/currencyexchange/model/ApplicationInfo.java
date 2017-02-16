package com.sergey.currencyexchange.model;

import java.util.ArrayList;

public class ApplicationInfo {

    private static final ApplicationInfo instance = new ApplicationInfo();

    private static final String URL_TYPE_Ukraine = "app_response_ua.php";
    private static final String URL_TYPE_Usa = "app_response_usa.php";
    private static final String URL_TYPE_Uk = "app_response_uk.php";
    private static final String URL_TYPE_Europe = "app_response_eu.php";
    private static final String URL_TYPE_Poland = "app_response_pl.php";
    private static final String URL_TYPE_Turkey = "app_response_tr.php";
    private static final String URL_TYPE_Russia = "app_response_ru.php";

    private static ArrayList<Country> countries;

    private ApplicationInfo(){
        countries = new ArrayList<>();
    }

    public static ApplicationInfo getInstance(){
        return instance;
    }

    public static ArrayList<Country> getCountries() {
        return countries;
    }

    public static Country getCountry() {
        switch (Utils.country_code) {
            case Utils.USA_CODE:
                return countries.get(0);
            case Utils.EUROPE_CODE:
                return countries.get(1);
            case Utils.UK_CODE:
                return countries.get(2);
            case Utils.POLAND_CODE:
                return countries.get(3);
            case Utils.TURKEY_CODE:
                return countries.get(4);
            case Utils.RUSSIA_CODE:
                return countries.get(5);
            case Utils.UKRAINE_CODE:
                return countries.get(6);
            default:
                return countries.get(2);
        }
    }

    public static void setCountries(Country country) {
        countries.add(country);
    }

    public static void removeAllCountries() {
        countries.clear();
    }

    public static String getUrlType() {
        switch (Utils.country_code) {
            case Utils.USA_CODE:
                return URL_TYPE_Usa;
            case Utils.EUROPE_CODE:
                return URL_TYPE_Europe;
            case Utils.UK_CODE:
                return URL_TYPE_Uk;
            case Utils.POLAND_CODE:
                return URL_TYPE_Poland;
            case Utils.TURKEY_CODE:
                return URL_TYPE_Turkey;
            case Utils.RUSSIA_CODE:
                return URL_TYPE_Russia;
            case Utils.UKRAINE_CODE:
                return URL_TYPE_Ukraine;
            default:
                return URL_TYPE_Uk;
        }
    }
}
