package com.sergey.currencyexchange.model;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    private int id = 1;
    private int idTime = 10;
    private final String[] banksNameArray = {"ПриватБанк", "Ощадбанк", "СБЕРБАНК", "Райффайзен Банк Аваль", "Укрсоцбанк UniCredit Bank TM", "Альфа-Банк", "УкрСиббанк", "ПУМБ", "ВТБ Банк", "ОТП Банк", "Креди Агриколь Банк"};
    private String bankName;
    private static final String DB_NAME = "DB.db";
    private final String TABLE_NAME_NBU = "nbu";
    private final String TABLE_NAME_MB = "mb";
    private final String TABLE_NAME_BLACKM = "blackM";
    private final String TABLE_NAME_BANKS = "banks";

    private final String CREATE_TABLE_NBU = "CREATE TABLE " + TABLE_NAME_NBU + "(`id` INTEGER NOT NULL, `rateD` DOUBLE , `rateE` DOUBLE , `rateR` DOUBLE, `changesD` DOUBLE, `changesE` DOUBLE, `changesR` DOUBLE, `dateServer` DATETIME NOT NULL)";
    private final String CREATE_TABLE_MB = "CREATE TABLE " + TABLE_NAME_MB + "(`id` INTEGER NOT NULL, `idTime` INTEGER NOT NULL, `buyD` DOUBLE NOT NULL, `sellD` DOUBLE NOT NULL, `buyE` DOUBLE NOT NULL, `sellE` DOUBLE NOT NULL, `buyR` DOUBLE NOT NULL, `sellR` DOUBLE NOT NULL, `dateServer` DATETIME NOT NULL)";
    private final String CREATE_TABLE_BLACKM = "CREATE TABLE " + TABLE_NAME_BLACKM + "(`id` INTEGER NOT NULL , `buyD` DOUBLE NOT NULL , `sellD` DOUBLE NOT NULL , `buyE` DOUBLE NOT NULL, `sellE` DOUBLE NOT NULL, `buyR` DOUBLE NOT NULL, `sellR` DOUBLE NOT NULL, `changesBuyD` DOUBLE NOT NULL, `changesSellD`, `changesBuyE` DOUBLE NOT NULL, `changesSellE`, `changesBuyR` DOUBLE NOT NULL, `changesSellR`, `dateServer` DATETIME NOT NULL)";
    private final String CREATE_TABLE_BANKS = "CREATE TABLE " + TABLE_NAME_BANKS + "(`id` INTEGER NOT NULL , `name` TEXT NOT NULL, `buyD` DOUBLE NOT NULL , `sellD` DOUBLE NOT NULL , `buyE` DOUBLE NOT NULL, `sellE` DOUBLE NOT NULL, `buyR` DOUBLE NOT NULL, `sellR` DOUBLE NOT NULL, `changesBuyD` DOUBLE NOT NULL, `changesSellD`, `changesBuyE` DOUBLE NOT NULL, `changesSellE`, `changesBuyR` DOUBLE NOT NULL, `changesSellR`, `dateServer` DATETIME NOT NULL)";

    private final String DROP_TABLE_NBU = "DROP TABLE IF EXISTS " + TABLE_NAME_NBU;
    private final String DROP_TABLE_MB = "DROP TABLE IF EXISTS " + TABLE_NAME_MB;
    private final String DROP_TABLE_BLACKM = "DROP TABLE IF EXISTS " + TABLE_NAME_BLACKM;
    private final String DROP_TABLE_BANKS = "DROP TABLE IF EXISTS " + TABLE_NAME_BANKS;

    public DBHelper(Context context, int dbVer){
        super(context, DB_NAME, null, dbVer);
        //context.deleteDatabase(DB_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NBU);
        db.execSQL("INSERT INTO " + TABLE_NAME_NBU + "(`id`, `rateD`, `rateE`, `rateR`, `changesD`, `changesE`, `changesR`, `dateServer`) VALUES ('1', '', '', '', '', '', '', '')");

        db.execSQL(CREATE_TABLE_MB);
        while (idTime < 18) {
            db.execSQL("INSERT INTO " + TABLE_NAME_MB + "(`id`, `idTime`, `buyD`, `sellD`, `buyE`, `sellE`, `buyR`, `sellR`, `dateServer`) VALUES ('" + id + "', '" + idTime + "', '', '', '', '', '', '', '')");
            id++;
            idTime++;
        }
        db.execSQL(CREATE_TABLE_BLACKM);
        db.execSQL("INSERT INTO " + TABLE_NAME_BLACKM + "(`id`, `buyD`, `sellD`, `buyE`, `sellE`, `buyR`, `sellR`, `changesBuyD`, `changesSellE`, `changesBuyE`, `changesSellR`, `changesBuyR`, `changesSellD`, `dateServer`) VALUES ('1', '', '', '', '', '', '', '', '', '', '', '', '', '')");


        db.execSQL(CREATE_TABLE_BANKS);
        id = 1;
        for (String n : banksNameArray) {
            bankName = n;
            db.execSQL("INSERT INTO " + TABLE_NAME_BANKS + "(`id`, `name`, `buyD`, `sellD`, `buyE`, `sellE`, `buyR`, `sellR`, `changesBuyD`, `changesSellE`, `changesBuyE`, `changesSellR`, `changesBuyR`, `changesSellD`, `dateServer`) VALUES ('" + id + "', '" + bankName + "', '', '', '', '', '', '', '', '', '', '', '', '', '')");
            id++;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_NBU);
        db.execSQL(CREATE_TABLE_NBU);
        db.execSQL("INSERT INTO " + TABLE_NAME_NBU + "(`id`, `rateD`, `rateE`, `rateR`, `changesD`, `changesE`, `changesR`, `dateServer`) VALUES ('1', '', '', '', '', '', '', '')");

        db.execSQL(DROP_TABLE_MB);
        db.execSQL(CREATE_TABLE_MB);
        id = 1;
        idTime = 10;
        while (idTime < 18) {
            db.execSQL("INSERT INTO " + TABLE_NAME_MB + "(`id`, `idTime`, `buyD`, `sellD`, `buyE`, `sellE`, `buyR`, `sellR`, `dateServer`) VALUES ('" + id + "', '" + idTime + "', '', '', '', '', '', '', '')");
            id++;
            idTime++;

        }

        db.execSQL(DROP_TABLE_BLACKM);
        db.execSQL(CREATE_TABLE_BLACKM);
        db.execSQL("INSERT INTO " + TABLE_NAME_BLACKM + "(`id`, `buyD`, `sellD`, `buyE`, `sellE`, `buyR`, `sellR`, `changesBuyD`, `changesSellE`, `changesBuyE`, `changesSellR`, `changesBuyR`, `changesSellD`, `dateServer`) VALUES ('1', '', '', '', '', '', '', '', '', '', '', '', '', '')");

        db.execSQL(DROP_TABLE_BANKS);
        db.execSQL(CREATE_TABLE_BANKS);
        id = 1;
        for (String n : banksNameArray) {
            bankName = n;
            db.execSQL("INSERT INTO " + TABLE_NAME_BANKS + "(`id`, `name`, `buyD`, `sellD`, `buyE`, `sellE`, `buyR`, `sellR`, `changesBuyD`, `changesSellE`, `changesBuyE`, `changesSellR`, `changesBuyR`, `changesSellD`, `dateServer`) VALUES ('" + id + "', '" + bankName + "', '', '', '', '', '', '', '', '', '', '', '', '', '')");
            id++;
        }
    }

    public void setNbuInfo(SQLiteDatabase db, double rateD, double rateE, double rateR, String dateServer) {
        double changesD = 0;
        double changesE = 0;
        double changesR = 0;
        boolean changes = false;
        Cursor c = db.query(TABLE_NAME_NBU, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            int rateDIndex = c.getColumnIndex("rateD");
            int rateEIndex = c.getColumnIndex("rateE");
            int rateRIndex = c.getColumnIndex("rateR");
            int dateServerIndex = c.getColumnIndex("dateServer");

            //if days the same than not need to count changes of nbu rates
            do {
                if (!c.getString(dateServerIndex).isEmpty()) {
                    if (!dateServer.substring(0, 10).equals(c.getString(dateServerIndex).substring(0, 10))) {
                        changesD = rateD - c.getDouble(rateDIndex);
                        changesE = rateE - c.getDouble(rateEIndex);
                        changesR = rateR - c.getDouble(rateRIndex);
                        changes = true;
                    }
                }
            } while (c.moveToNext());
        }
        else {
            c.close();
        }
        if (changes) {
            db.execSQL("UPDATE " + TABLE_NAME_NBU + " SET rateD = '" + rateD + "', rateE = '" + rateE + "', rateR = '" + rateR + "', changesD = '" + changesD + "', changesE = '" + changesE + "', changesR = '" + changesR + "', dateServer = '" + dateServer + "' WHERE id = 1");
        }
        else {
            db.execSQL("UPDATE " + TABLE_NAME_NBU + " SET rateD = '" + rateD + "', rateE = '" + rateE + "', rateR = '" + rateR + "', dateServer = '" + dateServer + "' WHERE id = 1");
        }
    }

    public void setMBInfo(SQLiteDatabase db, int idTime, double buyD, double sellD, double buyE, double sellE, double buyR, double sellR, String dateServer) {
        db.execSQL("UPDATE " + TABLE_NAME_MB + " SET buyD = '" + buyD + "', sellD = '" + sellD + "', buyE = '" + buyE + "', sellE = '" + sellE + "', buyR = '" + buyR + "', sellR = '" + sellR + "', dateServer = '" + dateServer + "' WHERE idTime = " + idTime);
    }

    public void setBlackMInfo(SQLiteDatabase db, double buyD, double sellD, double buyE, double sellE, double buyR, double sellR, String dateServer) {
        double changesBuyD = 0;
        double changesSellD = 0;
        double changesBuyE = 0;
        double changesSellE = 0;
        double changesBuyR = 0;
        double changesSellR = 0;
        boolean changes = false;
        Cursor c = db.query(TABLE_NAME_BLACKM, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int buyDIndex = c.getColumnIndex("buyD");
            int sellDIndex = c.getColumnIndex("sellD");
            int buyEIndex = c.getColumnIndex("buyE");
            int sellEIndex = c.getColumnIndex("sellE");
            int buyRIndex = c.getColumnIndex("buyR");
            int sellRIndex = c.getColumnIndex("sellR");
            int dateServerIndex = c.getColumnIndex("dateServer");

            //if days the same than not need to count changes of nbu rates
            do {
                if (!c.getString(dateServerIndex).isEmpty()) {
                    if (!dateServer.substring(0, 10).equals(c.getString(dateServerIndex).substring(0, 10))) {
                        changesBuyD = buyD - c.getDouble(buyDIndex);
                        changesSellD = sellD - c.getDouble(sellDIndex);
                        changesBuyE = buyE - c.getDouble(buyEIndex);
                        changesSellE = sellE - c.getDouble(sellEIndex);
                        changesBuyR = buyR - c.getDouble(buyRIndex);
                        changesSellR = sellR - c.getDouble(sellRIndex);
                        changes = true;
                    }
                }
            } while (c.moveToNext());
        }
        else {
            c.close();
        }

        if (changes) {
            db.execSQL("UPDATE " + TABLE_NAME_BLACKM + " SET buyD = '" + buyD + "', sellD = '" + sellD + "', buyE = '" + buyE + "', sellE = '" + sellE + "', buyR = '" + buyR + "', sellR = '" + sellR + "', changesBuyD = '" + changesBuyD + "', changesSellD = '" + changesSellD + "', changesBuyE = '" + changesBuyE + "', changesSellE = '" + changesSellE + "', changesBuyR = '" + changesBuyR + "', changesSellR = '" + changesSellR + "', dateServer = '" + dateServer + "' WHERE id = 1");
        }
        else {
            db.execSQL("UPDATE " + TABLE_NAME_BLACKM + " SET buyD = '" + buyD + "', sellD = '" + sellD + "', buyE = '" + buyE + "', sellE = '" + sellE + "', buyR = '" + buyR + "', sellR = '" + sellR + "', dateServer = '" + dateServer + "' WHERE id = 1");
        }
    }

    public void setBankInfo(SQLiteDatabase db, String name, double buyD, double sellD, double buyE, double sellE, double buyR, double sellR, String dateServer) {
        double changesBuyD = 0;
        double changesSellD = 0;
        double changesBuyE = 0;
        double changesSellE = 0;
        double changesBuyR = 0;
        double changesSellR = 0;
        boolean changes = false;
        Cursor c = db.query(TABLE_NAME_BANKS, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int nameIndex = c.getColumnIndex("name");
            int buyDIndex = c.getColumnIndex("buyD");
            int sellDIndex = c.getColumnIndex("sellD");
            int buyEIndex = c.getColumnIndex("buyE");
            int sellEIndex = c.getColumnIndex("sellE");
            int buyRIndex = c.getColumnIndex("buyR");
            int sellRIndex = c.getColumnIndex("sellR");
            int dateServerIndex = c.getColumnIndex("dateServer");

            //if days the same than not need to count changes of nbu rates
            do {
                if (!c.getString(dateServerIndex).isEmpty()) {
                    if (!dateServer.substring(0, 10).equals(c.getString(dateServerIndex).substring(0, 10))) {
                        changesBuyD = buyD - c.getDouble(buyDIndex);
                        changesSellD = sellD - c.getDouble(sellDIndex);
                        changesBuyE = buyE - c.getDouble(buyEIndex);
                        changesSellE = sellE - c.getDouble(sellEIndex);
                        changesBuyR = buyR - c.getDouble(buyRIndex);
                        changesSellR = sellR - c.getDouble(sellRIndex);
                        changes = true;
                    }
                }
            } while (c.moveToNext());
        }
        else {
            c.close();
        }
        if (changes) {
            db.execSQL("UPDATE " + TABLE_NAME_BANKS + " SET buyD = '" + buyD + "', sellD = '" + sellD + "', buyE = '" + buyE + "', sellE = '" + sellE + "', buyR = '" + buyR + "', sellR = '" + sellR + "', changesBuyD = '" + changesBuyD + "', changesSellD = '" + changesSellD + "', changesBuyE = '" + changesBuyE + "', changesSellE = '" + changesSellE + "', changesBuyR = '" + changesBuyR + "', changesSellR = '" + changesSellR + "', dateServer = '" + dateServer + "' WHERE name = '" + name + "'");
        }
        else {
            db.execSQL("UPDATE " + TABLE_NAME_BANKS + " SET buyD = '" + buyD + "', sellD = '" + sellD + "', buyE = '" + buyE + "', sellE = '" + sellE + "', buyR = '" + buyR + "', sellR = '" + sellR + "', dateServer = '" + dateServer + "' WHERE name = '" + name + "'");
        }

    }
}
