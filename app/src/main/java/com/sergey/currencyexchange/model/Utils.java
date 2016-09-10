package com.sergey.currencyexchange.model;

import java.util.ArrayList;

public class Utils {

    public static double roundResut(double number) {
        double result = number * 10000;
        int i = (int)Math.round(result);
        result = (double)i/10000;
        return result;
    }


    public static double[] toPrimitiveDoubleArray(ArrayList<Double> array) {
        double[] target = new double[array.size()];
        for (int i = 0; i < target.length; i++) {
            target[i] = array.get(i);
        }
        return target;
    }

    public static String[] toPrimitiveStringArray(ArrayList<String> array) {
        String[] target = new String[array.size()];
        for (int i = 0; i < target.length; i++) {
            target[i] = array.get(i);
        }
        return target;
    }

    public static boolean isDigit(String string) {
        try {
            Double.parseDouble(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
