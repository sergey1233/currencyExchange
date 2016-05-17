package com.sergey.currencyexchange.model;

/**
 * Created by Sergey on 16.05.2016.
 */
public class NumberUtils {

    public static double roundResut(double number)
    {
        double result = number * 100;
        int i = (int)Math.round(result);
        result = (double)i/100;
        return result;
    }

}
