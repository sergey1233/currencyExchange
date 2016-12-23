package com.sergey.currencyexchange.model;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    private int id = 1;

    private final String[] banksNameArray = {"ПриватБанк", "Ощадбанк", "СБЕРБАНК", "Райффайзен Банк Аваль", "Укрсоцбанк", "Альфа-Банк", "УкрСиббанк", "ПУМБ", "ВТБ Банк", "ОТП Банк", "Креди Агриколь Банк", "Укргазбанк", "ТАСкомбанк", "КРЕДИТ ДНЕПР"};
    private final String[] banksNameArray_uk = {"DEBENHAMS", "Travelex", "ICE", "NATWEST", "RBS", "Virgin Atlantic", "MSBANK", "Eurochange", "ACE-FX", "GRIFFIN", "Sterling", "MoneyCorp"};
    private final String[] banksNameArray_pl = {"ALIOR BANK", "BANK BPS", "ZACHODNI WBK", "BOS BANK", "CREDIT AGRICOLE", "GETIN BANK", "ING BANK SLASKI", "DEUTSCHE BANK", "BGZ BNP PARIBAS SA", "Raiffeisen Polbank", "MBANK", "PKO Bank Polski"};
    private final String[] banksNameArray_ru = {"Сбербанк России", "Газпромбанк", "Россельхозбанк", "Альфа-Банк", "Московский Кредитный Банк", "Промсвязьбанк", "ЮниКредит Банк", "Бинбанк", "Райффайзенбанк", "Росбанк", "Банк Москвы", "Банк «Санкт-Петербург»", "Совкомбанк", "Русский Стандарт", "Московский Областной Банк", "Ситибанк", "Абсолют Банк", "Тинькофф Банк"};


    private final String[] curreciesNameArray = {"HKD", "HUF", "ISK", "INR", "IRR","IQD", "ILS", "GEL", "JPY", "UAH", "KZT", "KRW", "KWD", "KGS", "LBP", "LYD", "MXN", "MNT", "MDL", "NZD", "NOK", "PKR", "PEN", "RON", "SAR", "SGD", "RUB", "BYN", "SEK", "CHF", "SYP", "TRY", "TMT", "EGP", "GBP", "USD", "UZS", "TWD", "XOF", "XAU", "XAG", "XPT", "XPD", "EUR", "PLN", "BRL", "TJS", "AZN", "AUD", "AMD", "BGN", "CAD", "CLP", "CNY", "HRK", "CZK", "DKK", "MYR", "ZAR", "THB", "PHP", "IDR"};

    private String bankName;
    private static final String DB_NAME = "DB.db";
    private final String TABLE_NAME_NBU = "nbu";
    private final String TABLE_NAME_MB = "mb";
    private final String TABLE_NAME_BLACKM = "blackM";
    private final String TABLE_NAME_BANKS = "banks";
    private String TABLE_NAME_CURRENCIES = "test";

    private final String[] arrayTableNameCurrencies = {Utils.TABLE_NAME_CURRENCIES_Ukraine, Utils.TABLE_NAME_CURRENCIES_Usa, Utils.TABLE_NAME_CURRENCIES_Uk, Utils.TABLE_NAME_CURRENCIES_Europe, Utils.TABLE_NAME_CURRENCIES_Poland, Utils.TABLE_NAME_CURRENCIES_Turkey, Utils.TABLE_NAME_CURRENCIES_Russia};


    private final String CREATE_TABLE_NBU = "CREATE TABLE " + TABLE_NAME_NBU + "(`id` INTEGER NOT NULL, `rateD` DOUBLE , `rateE` DOUBLE , `rateR` DOUBLE, `changesD` DOUBLE, `changesE` DOUBLE, `changesR` DOUBLE, `dateServer` DATETIME NOT NULL)";
    private final String CREATE_TABLE_CBANK_UK = "CREATE TABLE " + Utils.TABLE_NAME_CBANK_UK + "(`id` INTEGER NOT NULL, `rateUSD` DOUBLE , `rateEUR` DOUBLE , `rateRUB` DOUBLE , `rateCAD` DOUBLE , `rateTYR` DOUBLE , `ratePLN` DOUBLE , `rateILS` DOUBLE , `rateCNY` DOUBLE , `rateCZK` DOUBLE , `rateSEK` DOUBLE , `rateCHF` DOUBLE, `rateJPY` DOUBLE, `changesUSD` DOUBLE, `changesEUR` DOUBLE, `changesRUB` DOUBLE, `changesCAD` DOUBLE, `changesTYR` DOUBLE, `changesPLN` DOUBLE, `changesILS` DOUBLE, `changesCNY` DOUBLE, `changesCZK` DOUBLE, `changesSEK` DOUBLE, `changesCHF` DOUBLE, `changesJPY` DOUBLE, `dateServer` DATETIME NOT NULL)";
    private final String CREATE_TABLE_CBANK_POLAND = "CREATE TABLE " + Utils.TABLE_NAME_CBANK_POLAND + "(`id` INTEGER NOT NULL, `rateUSD` DOUBLE, `rateEUR` DOUBLE, `rateGBP` DOUBLE , `rateCHF` DOUBLE, `changesUSD` DOUBLE, `changesEUR` DOUBLE, `changesGBP` DOUBLE, `changesCHF` DOUBLE, `dateServer` DATETIME NOT NULL)";
    private final String CREATE_TABLE_CBANK_RUSSIA = "CREATE TABLE " + Utils.TABLE_NAME_CBANK_Russia + "(`id` INTEGER NOT NULL, `rateUSD` DOUBLE, `rateEUR` DOUBLE, `changesUSD` DOUBLE, `changesEUR` DOUBLE, `dateServer` DATETIME NOT NULL)";

    private final String CREATE_TABLE_MB = "CREATE TABLE " + TABLE_NAME_MB + "(`id` INTEGER NOT NULL, `idTime` INTEGER NOT NULL, `buyD` DOUBLE NOT NULL, `sellD` DOUBLE NOT NULL, `buyE` DOUBLE NOT NULL, `sellE` DOUBLE NOT NULL, `buyR` DOUBLE NOT NULL, `sellR` DOUBLE NOT NULL, `changesBuyD` DOUBLE NOT NULL, `changesSellD`, `changesBuyE` DOUBLE NOT NULL, `changesSellE`, `changesBuyR` DOUBLE NOT NULL, `changesSellR`, `dateServer` DATETIME NOT NULL, `buyDFirstInDay` DOUBLE NOT NULL , `sellDFirstInDay` DOUBLE NOT NULL , `buyEFirstInDay` DOUBLE NOT NULL, `sellEFirstInDay` DOUBLE NOT NULL, `buyRFirstInDay` DOUBLE NOT NULL, `sellRFirstInDay` DOUBLE NOT NULL)";
    private final String CREATE_TABLE_BLACKM = "CREATE TABLE " + TABLE_NAME_BLACKM + "(`id` INTEGER NOT NULL , `buyD` DOUBLE NOT NULL , `sellD` DOUBLE NOT NULL , `buyE` DOUBLE NOT NULL, `sellE` DOUBLE NOT NULL, `buyR` DOUBLE NOT NULL, `sellR` DOUBLE NOT NULL, `changesBuyD` DOUBLE NOT NULL, `changesSellD`, `changesBuyE` DOUBLE NOT NULL, `changesSellE`, `changesBuyR` DOUBLE NOT NULL, `changesSellR`, `dateServer` DATETIME NOT NULL, `buyDFirstInDay` DOUBLE NOT NULL , `sellDFirstInDay` DOUBLE NOT NULL , `buyEFirstInDay` DOUBLE NOT NULL, `sellEFirstInDay` DOUBLE NOT NULL, `buyRFirstInDay` DOUBLE NOT NULL, `sellRFirstInDay` DOUBLE NOT NULL)";

    private final String CREATE_TABLE_BANKS = "CREATE TABLE " + TABLE_NAME_BANKS + "(`id` INTEGER NOT NULL , `name` TEXT NOT NULL, `buyUSD` DOUBLE NOT NULL , `sellUSD` DOUBLE NOT NULL , `buyEUR` DOUBLE NOT NULL, `sellEUR` DOUBLE NOT NULL, `buyRUB` DOUBLE NOT NULL, `sellRUB` DOUBLE NOT NULL, `changesBuyD` DOUBLE NOT NULL, `changesSellD`, `changesBuyE` DOUBLE NOT NULL, `changesSellE`, `changesBuyR` DOUBLE NOT NULL, `changesSellR`, `dateServer` DATETIME NOT NULL, `buyDFirstInDay` DOUBLE NOT NULL , `sellDFirstInDay` DOUBLE NOT NULL , `buyEFirstInDay` DOUBLE NOT NULL, `sellEFirstInDay` DOUBLE NOT NULL, `buyRFirstInDay` DOUBLE NOT NULL, `sellRFirstInDay` DOUBLE NOT NULL)";
    private final String CREATE_TABLES_BANKS_uk = "CREATE TABLE " + Utils.TABLE_NAME_BANKS_UK + "(`id` INTEGER NOT NULL, `name` TEXT NOT NULL,  `buyUSD` DOUBLE, `sellUSD` DOUBLE, `buyEUR` DOUBLE, `sellEUR` DOUBLE, `buyRUB` DOUBLE, `sellRUB` DOUBLE, `buyCAD` DOUBLE, `sellCAD` DOUBLE, `buyTYR` DOUBLE, `sellTYR` DOUBLE, `buyPLN` DOUBLE, `sellPLN` DOUBLE, `buyILS` DOUBLE, `sellILS` DOUBLE, `buyCNY` DOUBLE, `sellCNY` DOUBLE, `buyCZK` DOUBLE, `sellCZK` DOUBLE, `buySEK` DOUBLE, `sellSEK` DOUBLE, `buyCHF` DOUBLE, `sellCHF` DOUBLE, `buyJPY` DOUBLE, `sellJPY` DOUBLE, `changesBuyUSD` DOUBLE, `changesSellUSD` DOUBLE, `changesBuyEUR` DOUBLE, `changesSellEUR` DOUBLE, `changesBuyRUB` DOUBLE, `changesSellRUB` DOUBLE, `changesBuyCAD` DOUBLE, `changesSellCAD` DOUBLE, `changesBuyTYR` DOUBLE, `changesSellTYR` DOUBLE, `changesBuyPLN` DOUBLE, `changesSellPLN` DOUBLE, `changesBuyILS` DOUBLE, `changesSellILS` DOUBLE, `changesBuyCNY` DOUBLE, `changesSellCNY` DOUBLE, `changesBuyCZK` DOUBLE, `changesSellCZK` DOUBLE, `changesBuySEK` DOUBLE, `changesSellSEK` DOUBLE, `changesBuyCHF` DOUBLE, `changesSellCHF` DOUBLE, `changesBuyJPY` DOUBLE, `changesSellJPY` DOUBLE, `dateServer` DATETIME NOT NULL)";
    private final String CREATE_TABLES_BANKS_pl = "CREATE TABLE " + Utils.TABLE_NAME_BANKS_POLAND + "(`id` INTEGER NOT NULL, `name` TEXT NOT NULL,  `buyUSD` DOUBLE, `sellUSD` DOUBLE, `buyEUR` DOUBLE, `sellEUR` DOUBLE, `buyGBP` DOUBLE, `sellGBP` DOUBLE, `buyCHF` DOUBLE, `sellCHF` DOUBLE, `changesBuyUSD` DOUBLE, `changesSellUSD` DOUBLE, `changesBuyEUR` DOUBLE, `changesSellEUR` DOUBLE, `changesBuyGBP` DOUBLE, `changesSellGBP` DOUBLE, `changesBuyCHF` DOUBLE, `changesSellCHF` DOUBLE, `dateServer` DATETIME NOT NULL)";
    private final String CREATE_TABLES_BANKS_ru = "CREATE TABLE " + Utils.TABLE_NAME_BANKS_RUSSIA + "(`id` INTEGER NOT NULL, `name` TEXT NOT NULL,  `buyUSD` DOUBLE, `sellUSD` DOUBLE, `buyEUR` DOUBLE, `sellEUR` DOUBLE, `changesBuyUSD` DOUBLE, `changesSellUSD` DOUBLE, `changesBuyEUR` DOUBLE, `changesSellEUR` DOUBLE, `dateServer` DATETIME NOT NULL)";


    private final String DROP_TABLE_MB = "DROP TABLE IF EXISTS " + TABLE_NAME_MB;
    private final String DROP_TABLE_BLACKM = "DROP TABLE IF EXISTS " + TABLE_NAME_BLACKM;

    private final String DROP_TABLE_NBU = "DROP TABLE IF EXISTS " + TABLE_NAME_NBU;
    private final String DROP_TABLE_CBANK_UK = "DROP TABLE IF EXISTS " + Utils.TABLE_NAME_CBANK_UK;
    private final String DROP_TABLE_CBANK_POLAND = "DROP TABLE IF EXISTS " + Utils.TABLE_NAME_CBANK_POLAND;
    private final String DROP_TABLE_CBANK_RUSSIA = "DROP TABLE IF EXISTS " + Utils.TABLE_NAME_CBANK_Russia;


    private final String DROP_TABLE_BANKS = "DROP TABLE IF EXISTS " + TABLE_NAME_BANKS;
    private final String DROP_TABLE_BANKS_UK = "DROP TABLE IF EXISTS " + Utils.TABLE_NAME_BANKS_UK;
    private final String DROP_TABLE_BANKS_POLAND = "DROP TABLE IF EXISTS " + Utils.TABLE_NAME_BANKS_POLAND;
    private final String DROP_TABLE_BANKS_RUSSIA = "DROP TABLE IF EXISTS " + Utils.TABLE_NAME_BANKS_RUSSIA;




    public DBHelper(Context context, int dbVer){
        super(context, DB_NAME, null, dbVer);
//        if (dbVer <  17) {
//            context.deleteDatabase(DB_NAME);
//        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //--------------------------------CREATE table with the main official currencies-----------------------
        db.execSQL(CREATE_TABLE_NBU);
        db.execSQL("INSERT INTO " + TABLE_NAME_NBU + "(`id`, `rateD`, `rateE`, `rateR`, `changesD`, `changesE`, `changesR`, `dateServer`) VALUES ('1', '', '', '', '', '', '', '')");

        db.execSQL(CREATE_TABLE_CBANK_UK);
        db.execSQL("INSERT INTO " + Utils.TABLE_NAME_CBANK_UK + "(`id`, `rateUSD`, `rateEUR`, `rateRUB`, `rateCAD`, `rateTYR`, `ratePLN`, `rateILS`, `rateCNY`, `rateCZK`, `rateSEK`, `rateCHF`, `rateJPY`, `changesUSD`, `changesEUR`, `changesRUB`, `changesCAD`, `changesTYR`, `changesPLN`, `changesILS`, `changesCNY`, `changesCZK`, `changesSEK`, `changesCHF`, `changesJPY`, `dateServer`) VALUES ('1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '')");

        db.execSQL(CREATE_TABLE_CBANK_POLAND);
        db.execSQL("INSERT INTO " + Utils.TABLE_NAME_CBANK_POLAND + "(`id`, `rateUSD`, `rateEUR`, `rateGBP`, `rateCHF`, `changesUSD`, `changesEUR`, `changesGBP`, `changesCHF`, `dateServer`) VALUES ('1', '', '', '', '', '', '', '', '', '')");

        db.execSQL(CREATE_TABLE_CBANK_RUSSIA);
        db.execSQL("INSERT INTO " + Utils.TABLE_NAME_CBANK_Russia + "(`id`, `rateUSD`, `rateEUR`, `changesUSD`, `changesEUR`, `dateServer`) VALUES ('1', '', '', '', '', '')");


        //--------------------------------CREATE table with the main banks and their rates of currencies-----------------------
        db.execSQL(CREATE_TABLE_BANKS);
        id = 1;
        for (String n : banksNameArray) {
            bankName = n;
            db.execSQL("INSERT INTO " + TABLE_NAME_BANKS + "(`id`, `name`, `buyUSD`, `sellUSD`, `buyEUR`, `sellEUR`, `buyRUB`, `sellRUB`, `changesBuyD`, `changesSellE`, `changesBuyE`, `changesSellR`, `changesBuyR`, `changesSellD`, `dateServer`, `buyDFirstInDay`, `sellDFirstInDay`, `buyEFirstInDay`, `sellEFirstInDay`, `buyRFirstInDay`, `sellRFirstInDay`) VALUES ('" + id + "', '" + bankName + "', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '')");
            id++;
        }

        db.execSQL(CREATE_TABLES_BANKS_uk);
        id = 1;
        for (String n : banksNameArray_uk) {
            bankName = n;
            db.execSQL("INSERT INTO " + Utils.TABLE_NAME_BANKS_UK + "(`id`, `name`, `buyUSD`, `sellUSD`, `buyEUR`, `sellEUR`, `buyRUB`, `sellRUB`, `buyCAD`, `sellCAD`, `buyTYR`, `sellTYR`, `buyPLN`, `sellPLN`, `buyILS`, `sellILS`, `buyCNY`, `sellCNY`, `buyCZK`, `sellCZK`, `buySEK`, `sellSEK`, `buyCHF`, `sellCHF`, `buyJPY`, `sellJPY`, `changesBuyUSD`, `changesSellUSD`, `changesBuyEUR`, `changesSellEUR`, `changesBuyRUB`, `changesSellRUB`, `changesBuyCAD`, `changesSellCAD`, `changesBuyTYR`, `changesSellTYR`, `changesBuyPLN`, `changesSellPLN`, `changesBuyILS`, `changesSellILS`, `changesBuyCNY`, `changesSellCNY`, `changesBuyCZK`, `changesSellCZK`, `changesBuySEK`, `changesSellSEK`, `changesBuyCHF`, `changesSellCHF`, `changesBuyJPY`, `changesSellJPY`, `dateServer`) VALUES ('" + id + "', '" + bankName + "', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '')");
            id++;
        }

        db.execSQL(CREATE_TABLES_BANKS_pl);
        id = 1;
        for (String n : banksNameArray_pl) {
            bankName = n;
            db.execSQL("INSERT INTO " + Utils.TABLE_NAME_BANKS_POLAND + "(`id`, `name`, `buyUSD`, `sellUSD`, `buyEUR`, `sellEUR`, `buyGBP`, `sellGBP`, `buyCHF`, `sellCHF`, `changesBuyUSD`, `changesSellUSD`, `changesBuyEUR`, `changesSellEUR`, `changesBuyGBP`, `changesSellGBP`, `changesBuyCHF`, `changesSellCHF`, `dateServer`) VALUES ('" + id + "', '" + bankName + "', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '')");
            id++;
        }

        db.execSQL(CREATE_TABLES_BANKS_ru);
        id = 1;
        for (String n : banksNameArray_ru) {
            bankName = n;
            db.execSQL("INSERT INTO " + Utils.TABLE_NAME_BANKS_RUSSIA + "(`id`, `name`, `buyUSD`, `sellUSD`, `buyEUR`, `sellEUR`, `changesBuyUSD`, `changesSellUSD`, `changesBuyEUR`, `changesSellEUR`, `dateServer`) VALUES ('" + id + "', '" + bankName + "', '', '', '', '', '', '', '', '', '')");
            id++;
        }


        //--------------------------------CREATE table with the all currencies for each country-----------------------
        for (String tN : arrayTableNameCurrencies) {
            TABLE_NAME_CURRENCIES = tN;

            String CREATE_TABLE_CURRENCIES = "CREATE TABLE " + TABLE_NAME_CURRENCIES + "(`id` INTEGER NOT NULL , `name` TEXT NOT NULL, `country` TEXT NOT NULL , `description` TEXT NOT NULL , `rate` DOUBLE NOT NULL, `rateSell` DOUBLE NOT NULL, `date` TEXT NOT NULL)";
            db.execSQL(CREATE_TABLE_CURRENCIES);
            id = 1;
            for (String currencyName : curreciesNameArray) {
                db.execSQL("INSERT INTO " + TABLE_NAME_CURRENCIES + "(`id`, `name`, `country`, `description`, `rate`, `rateSell`, `date`) VALUES ('" + id + "', '" + currencyName + "', '', '', '', '', '')");
                id++;
            }
        }


        //--------------------------------UKRAINIAN Unique CREATE table og interbank and blackmarket-----------------------
        db.execSQL(CREATE_TABLE_MB);
        db.execSQL("INSERT INTO " + TABLE_NAME_MB + "(`id`, `idTime`, `buyD`, `sellD`, `buyE`, `sellE`, `buyR`, `sellR`, `changesBuyD`, `changesSellE`, `changesBuyE`, `changesSellR`, `changesBuyR`, `changesSellD`, `dateServer`, `buyDFirstInDay`, `sellDFirstInDay`, `buyEFirstInDay`, `sellEFirstInDay`, `buyRFirstInDay`, `sellRFirstInDay`) VALUES ('1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '')");

        db.execSQL(CREATE_TABLE_BLACKM);
        db.execSQL("INSERT INTO " + TABLE_NAME_BLACKM + "(`id`, `buyD`, `sellD`, `buyE`, `sellE`, `buyR`, `sellR`, `changesBuyD`, `changesSellE`, `changesBuyE`, `changesSellR`, `changesBuyR`, `changesSellD`, `dateServer`, `buyDFirstInDay`, `sellDFirstInDay`, `buyEFirstInDay`, `sellEFirstInDay`, `buyRFirstInDay`, `sellRFirstInDay`) VALUES ('1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '')");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //--------------------------------UPDATE table with the main official currencies-----------------------
        db.execSQL(DROP_TABLE_NBU);
        db.execSQL(CREATE_TABLE_NBU);
        db.execSQL("INSERT INTO " + TABLE_NAME_NBU + "(`id`, `rateD`, `rateE`, `rateR`, `changesD`, `changesE`, `changesR`, `dateServer`) VALUES ('1', '', '', '', '', '', '', '')");

        db.execSQL(DROP_TABLE_CBANK_UK);
        db.execSQL(CREATE_TABLE_CBANK_UK);
        db.execSQL("INSERT INTO " + Utils.TABLE_NAME_CBANK_UK + "(`id`, `rateUSD`, `rateEUR`, `rateRUB`, `rateCAD`, `rateTYR`, `ratePLN`, `rateILS`, `rateCNY`, `rateCZK`, `rateSEK`, `rateCHF`, `rateJPY`, `changesUSD`, `changesEUR`, `changesRUB`, `changesCAD`, `changesTYR`, `changesPLN`, `changesILS`, `changesCNY`, `changesCZK`, `changesSEK`, `changesCHF`, `changesJPY`, `dateServer`) VALUES ('1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '')");

        db.execSQL(DROP_TABLE_CBANK_POLAND);
        db.execSQL(CREATE_TABLE_CBANK_POLAND);
        db.execSQL("INSERT INTO " + Utils.TABLE_NAME_CBANK_POLAND + "(`id`, `rateUSD`, `rateEUR`, `rateGBP`, `rateCHF`, `changesUSD`, `changesEUR`, `changesGBP`, `changesCHF`, `dateServer`) VALUES ('1', '', '', '', '', '', '', '', '', '')");

        db.execSQL(DROP_TABLE_CBANK_RUSSIA);
        db.execSQL(CREATE_TABLE_CBANK_RUSSIA);
        db.execSQL("INSERT INTO " + Utils.TABLE_NAME_CBANK_Russia + "(`id`, `rateUSD`, `rateEUR`, `changesUSD`, `changesEUR`, `dateServer`) VALUES ('1', '', '', '', '', '')");


        //--------------------------------UPDATE table with the main banks and their rates of currencies-----------------------
        db.execSQL(DROP_TABLE_BANKS);
        db.execSQL(CREATE_TABLE_BANKS);
        id = 1;
        for (String n : banksNameArray) {
            bankName = n;
            db.execSQL("INSERT INTO " + TABLE_NAME_BANKS + "(`id`, `name`, `buyUSD`, `sellUSD`, `buyEUR`, `sellEUR`, `buyRUB`, `sellRUB`, `changesBuyD`, `changesSellE`, `changesBuyE`, `changesSellR`, `changesBuyR`, `changesSellD`, `dateServer`, `buyDFirstInDay`, `sellDFirstInDay`, `buyEFirstInDay`, `sellEFirstInDay`, `buyRFirstInDay`, `sellRFirstInDay`) VALUES ('" + id + "', '" + bankName + "', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '')");
            id++;
        }

        db.execSQL(DROP_TABLE_BANKS_UK);
        db.execSQL(CREATE_TABLES_BANKS_uk);
        id = 1;
        for (String n : banksNameArray_uk) {
            bankName = n;
            db.execSQL("INSERT INTO " + Utils.TABLE_NAME_BANKS_UK + "(`id`, `name`, `buyUSD`, `sellUSD`, `buyEUR`, `sellEUR`, `buyRUB`, `sellRUB`, `buyCAD`, `sellCAD`, `buyTYR`, `sellTYR`, `buyPLN`, `sellPLN`, `buyILS`, `sellILS`, `buyCNY`, `sellCNY`, `buyCZK`, `sellCZK`, `buySEK`, `sellSEK`, `buyCHF`, `sellCHF`, `buyJPY`, `sellJPY`, `changesBuyUSD`, `changesSellUSD`, `changesBuyEUR`, `changesSellEUR`, `changesBuyRUB`, `changesSellRUB`, `changesBuyCAD`, `changesSellCAD`, `changesBuyTYR`, `changesSellTYR`, `changesBuyPLN`, `changesSellPLN`, `changesBuyILS`, `changesSellILS`, `changesBuyCNY`, `changesSellCNY`, `changesBuyCZK`, `changesSellCZK`, `changesBuySEK`, `changesSellSEK`, `changesBuyCHF`, `changesSellCHF`, `changesBuyJPY`, `changesSellJPY`, `dateServer`) VALUES ('" + id + "', '" + bankName + "', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '')");
            id++;
        }

        db.execSQL(DROP_TABLE_BANKS_POLAND);
        db.execSQL(CREATE_TABLES_BANKS_pl);
        id = 1;
        for (String n : banksNameArray_pl) {
            bankName = n;
            db.execSQL("INSERT INTO " + Utils.TABLE_NAME_BANKS_POLAND + "(`id`, `name`, `buyUSD`, `sellUSD`, `buyEUR`, `sellEUR`, `buyGBP`, `sellGBP`, `buyCHF`, `sellCHF`, `changesBuyUSD`, `changesSellUSD`, `changesBuyEUR`, `changesSellEUR`, `changesBuyGBP`, `changesSellGBP`, `changesBuyCHF`, `changesSellCHF`, `dateServer`) VALUES ('" + id + "', '" + bankName + "', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '')");
            id++;
        }

        db.execSQL(DROP_TABLE_BANKS_RUSSIA);
        db.execSQL(CREATE_TABLES_BANKS_ru);
        id = 1;
        for (String n : banksNameArray_ru) {
            bankName = n;
            db.execSQL("INSERT INTO " + Utils.TABLE_NAME_BANKS_RUSSIA + "(`id`, `name`, `buyUSD`, `sellUSD`, `buyEUR`, `sellEUR`, `changesBuyUSD`, `changesSellUSD`, `changesBuyEUR`, `changesSellEUR`, `dateServer`) VALUES ('" + id + "', '" + bankName + "', '', '', '', '', '', '', '', '', '')");
            id++;
        }


        //--------------------------------UPDATE table with the all currencies for each country-----------------------
        for (String tN : arrayTableNameCurrencies) {
            TABLE_NAME_CURRENCIES = tN;

            String CREATE_TABLE_CURRENCIES = "CREATE TABLE " + TABLE_NAME_CURRENCIES + "(`id` INTEGER NOT NULL , `name` TEXT NOT NULL, `country` TEXT NOT NULL , `description` TEXT NOT NULL , `rate` DOUBLE NOT NULL, `rateSell` DOUBLE NOT NULL, `date` TEXT NOT NULL)";
            String DROP_TABLE_CURRENCIES = "DROP TABLE IF EXISTS " + TABLE_NAME_CURRENCIES;

            db.execSQL(DROP_TABLE_CURRENCIES);
            db.execSQL(CREATE_TABLE_CURRENCIES);
            id = 1;
            for (String currencyName : curreciesNameArray) {
                db.execSQL("INSERT INTO " + TABLE_NAME_CURRENCIES + "(`id`, `name`, `country`, `description`, `rate`, `rateSell`, `date`) VALUES ('" + id + "', '" + currencyName + "', '', '', '', '', '')");
                id++;
            }
        }


        //--------------------------------UKRAINIAN Unique UPDATE table og interbank and blackmarket-----------------------
        db.execSQL(DROP_TABLE_MB);
        db.execSQL(CREATE_TABLE_MB);
        db.execSQL("INSERT INTO " + TABLE_NAME_MB + "(`id`, `idTime`, `buyD`, `sellD`, `buyE`, `sellE`, `buyR`, `sellR`, `changesBuyD`, `changesSellE`, `changesBuyE`, `changesSellR`, `changesBuyR`, `changesSellD`, `dateServer`, `buyDFirstInDay`, `sellDFirstInDay`, `buyEFirstInDay`, `sellEFirstInDay`, `buyRFirstInDay`, `sellRFirstInDay`) VALUES ('1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '')");

        db.execSQL(DROP_TABLE_BLACKM);
        db.execSQL(CREATE_TABLE_BLACKM);
        db.execSQL("INSERT INTO " + TABLE_NAME_BLACKM + "(`id`, `buyD`, `sellD`, `buyE`, `sellE`, `buyR`, `sellR`, `changesBuyD`, `changesSellE`, `changesBuyE`, `changesSellR`, `changesBuyR`, `changesSellD`, `dateServer`, `buyDFirstInDay`, `sellDFirstInDay`, `buyEFirstInDay`, `sellEFirstInDay`, `buyRFirstInDay`, `sellRFirstInDay`) VALUES ('1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '')");

    }


    //--------------------------------------------------------------Ukraine----------------------------------------------------------------

//    public void setNbuInfo(SQLiteDatabase db, double rateD, double rateE, double rateR, String dateServer) {
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
        double changesBuyD = 0;
        double changesSellD = 0;
        double changesBuyE = 0;
        double changesSellE = 0;
        double changesBuyR = 0;
        double changesSellR = 0;
        boolean changesFirstDay = false;

        Cursor c = db.query(TABLE_NAME_MB, null, null, null, null, null, null);
        if (c.moveToFirst()) {
//            int idTimeIndex = c.getColumnIndex("idTime");
            int buyDIndex = c.getColumnIndex("buyD");
            int sellDIndex = c.getColumnIndex("sellD");
            int buyEIndex = c.getColumnIndex("buyE");
            int sellEIndex = c.getColumnIndex("sellE");
            int buyRIndex = c.getColumnIndex("buyR");
            int sellRIndex = c.getColumnIndex("sellR");
//            int dateServerIndex = c.getColumnIndex("dateServer");
            int buyDFirstIndex = c.getColumnIndex("buyDFirstInDay");
            int sellDFirstIndex = c.getColumnIndex("sellDFirstInDay");
            int buyEFirstIndex = c.getColumnIndex("buyEFirstInDay");
            int sellEFirstIndex = c.getColumnIndex("sellEFirstInDay");
            int buyRFirstIndex = c.getColumnIndex("buyRFirstInDay");
            int sellRFirstIndex = c.getColumnIndex("sellRFirstInDay");

            do {
                if (idTime != 10) {
                    changesFirstDay = true;
                    if (c.getDouble(buyDIndex) != 0) {
                        changesBuyD = buyD - c.getDouble(buyDIndex);
                    }
                    if (c.getDouble(sellDIndex) != 0) {
                        changesSellD = sellD - c.getDouble(sellDIndex);
                    }
                    if (c.getDouble(buyEIndex) != 0) {
                        changesBuyE = buyE - c.getDouble(buyEIndex);
                    }
                    if (c.getDouble(sellEIndex) != 0) {
                        changesSellE = sellE - c.getDouble(sellEIndex);
                    }
                    if (c.getDouble(buyRIndex) != 0) {
                        changesBuyR = buyR - c.getDouble(buyRIndex);
                    }
                    if (c.getDouble(sellRIndex) != 0) {
                        changesSellR = sellR - c.getDouble(sellRIndex);
                    }
                }
                else {
                    changesBuyD = buyD - c.getDouble(buyDFirstIndex);
                    changesSellD = sellD - c.getDouble(sellDFirstIndex);
                    changesBuyE = buyE - c.getDouble(buyEFirstIndex);
                    changesSellE = sellE - c.getDouble(sellEFirstIndex);
                    changesBuyR = buyR - c.getDouble(buyRFirstIndex);
                    changesSellR = sellR - c.getDouble(sellRFirstIndex);
                }

            } while (c.moveToNext());
        }
        else {
            c.close();
        }

        String dollarRequest = "";
        String euroRequest = "";
        String rubRequest = "";

        if (buyD != 0 && sellD != 0) {
            dollarRequest = "buyD = '" + buyD + "', sellD = '" + sellD + "', ";
        }
        if (buyE != 0 && sellE != 0) {
            euroRequest = "buyE = '" + buyE + "', sellE = '" + sellE + "', ";
        }
        if (buyR != 0 && sellR != 0) {
            rubRequest = "buyR = '" + buyR + "', sellR = '" + sellR + "', ";
        }

        if (changesFirstDay) {
            db.execSQL(
                    "UPDATE " + TABLE_NAME_MB + " SET " + dollarRequest + euroRequest + rubRequest +
                            "changesBuyD = '" + changesBuyD +
                            "', changesSellD = '" + changesSellD +
                            "', changesBuyE = '" + changesBuyE +
                            "', changesSellE = '" + changesSellE +
                            "', changesBuyR = '" + changesBuyR +
                            "', changesSellR = '" + changesSellR +
                            "', dateServer = '" + dateServer +
                            "', buyDFirstInDay = '" + buyD +
                            "', sellDFirstInDay = '" + sellD +
                            "', buyEFirstInDay = '" + buyE +
                            "', sellEFirstInDay = '" + sellE +
                            "', buyRFirstInDay = '" + buyR +
                            "', sellRFirstInDay = '" + sellR +
                            "' WHERE id = 1");
        }
        else {
            db.execSQL("UPDATE " + TABLE_NAME_MB + " SET " + dollarRequest + euroRequest + rubRequest +
                    "changesBuyD = '" + changesBuyD +
                    "', changesSellD = '" + changesSellD +
                    "', changesBuyE = '" + changesBuyE +
                    "', changesSellE = '" + changesSellE +
                    "', changesBuyR = '" + changesBuyR +
                    "', changesSellR = '" + changesSellR +
                    "', dateServer = '" + dateServer +
                    "' WHERE id = 1");
        }
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
            int buyDFirstIndex = c.getColumnIndex("buyDFirstInDay");
            int sellDFirstIndex = c.getColumnIndex("sellDFirstInDay");
            int buyEFirstIndex = c.getColumnIndex("buyEFirstInDay");
            int sellEFirstIndex = c.getColumnIndex("sellEFirstInDay");
            int buyRFirstIndex = c.getColumnIndex("buyRFirstInDay");
            int sellRFirstIndex = c.getColumnIndex("sellRFirstInDay");

            //if days the same than count changes fo first data of the day
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
                    else {
                        double buyDFirst = c.getDouble(buyDFirstIndex);
                        double sellDFirst = c.getDouble(sellDFirstIndex);
                        double buyEFirst = c.getDouble(buyEFirstIndex);
                        double sellEFirst = c.getDouble(sellEFirstIndex);
                        double buyRFirst = c.getDouble(buyRFirstIndex);
                        double sellRFirst = c.getDouble(sellRFirstIndex);

                        if (buyDFirst != 0) {
                            changesBuyD = buyD - buyDFirst;
                        }
                        if (sellDFirst != 0) {
                            changesSellD = sellD - sellDFirst;
                        }
                        if (buyEFirst != 0) {
                            changesBuyE = buyE - buyEFirst;
                        }
                        if (sellEFirst != 0) {
                            changesSellE = sellE - sellEFirst;
                        }
                        if (buyRFirst != 0) {
                            changesBuyR = buyR - buyRFirst;
                        }
                        if (sellRFirst != 0) {
                            changesSellR = sellR - sellRFirst;
                        }
                    }
                }
            } while (c.moveToNext());
        }
        else {
            c.close();
        }

        if (changes) {
            db.execSQL(
                    "UPDATE " + TABLE_NAME_BLACKM + " SET " +
                            "buyD = '" + buyD +
                            "', sellD = '" + sellD +
                            "', buyE = '" + buyE +
                            "', sellE = '" + sellE +
                            "', buyR = '" + buyR +
                            "', sellR = '" + sellR +
                            "', changesBuyD = '" + changesBuyD +
                            "', changesSellD = '" + changesSellD +
                            "', changesBuyE = '" + changesBuyE +
                            "', changesSellE = '" + changesSellE +
                            "', changesBuyR = '" + changesBuyR +
                            "', changesSellR = '" + changesSellR +
                            "', dateServer = '" + dateServer +
                            "', buyDFirstInDay = '" + buyD +
                            "', sellDFirstInDay = '" + sellD +
                            "', buyEFirstInDay = '" + buyE +
                            "', sellEFirstInDay = '" + sellE +
                            "', buyRFirstInDay = '" + buyR +
                            "', sellRFirstInDay = '" + sellR +
                            "' WHERE id = 1");
        }
        else {
            db.execSQL("UPDATE " + TABLE_NAME_BLACKM + " SET " +
                    "buyD = '" + buyD +
                    "', sellD = '" + sellD +
                    "', buyE = '" + buyE +
                    "', sellE = '" + sellE +
                    "', buyR = '" + buyR +
                    "', sellR = '" + sellR +
                    "', changesBuyD = '" + changesBuyD +
                    "', changesSellD = '" + changesSellD +
                    "', changesBuyE = '" + changesBuyE +
                    "', changesSellE = '" + changesSellE +
                    "', changesBuyR = '" + changesBuyR +
                    "', changesSellR = '" + changesSellR +
                    "', dateServer = '" + dateServer +
                    "' WHERE id = 1");
        }
    }

    public void setBankInfoUkraine(SQLiteDatabase db, String name, double buyD, double sellD, double buyE, double sellE, double buyR, double sellR, String dateServer) {
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
            int buyDIndex = c.getColumnIndex("buyUSD");
            int sellDIndex = c.getColumnIndex("sellUSD");
            int buyEIndex = c.getColumnIndex("buyEUR");
            int sellEIndex = c.getColumnIndex("sellEUR");
            int buyRIndex = c.getColumnIndex("buyRUB");
            int sellRIndex = c.getColumnIndex("sellRUB");
            int dateServerIndex = c.getColumnIndex("dateServer");
            int buyDFirstIndex = c.getColumnIndex("buyDFirstInDay");
            int sellDFirstIndex = c.getColumnIndex("sellDFirstInDay");
            int buyEFirstIndex = c.getColumnIndex("buyEFirstInDay");
            int sellEFirstIndex = c.getColumnIndex("sellEFirstInDay");
            int buyRFirstIndex = c.getColumnIndex("buyRFirstInDay");
            int sellRFirstIndex = c.getColumnIndex("sellRFirstInDay");

            //if days the same than count changes fo first data of the day
            do {
                if (!c.getString(dateServerIndex).isEmpty()) {
                    if (name.toLowerCase().equals(c.getString(nameIndex).toLowerCase())) {
                        if (!dateServer.substring(0, 10).equals(c.getString(dateServerIndex).substring(0, 10))) {
                            changesBuyD = buyD - c.getDouble(buyDIndex);
                            changesSellD = sellD - c.getDouble(sellDIndex);
                            changesBuyE = buyE - c.getDouble(buyEIndex);
                            changesSellE = sellE - c.getDouble(sellEIndex);
                            changesBuyR = buyR - c.getDouble(buyRIndex);
                            changesSellR = sellR - c.getDouble(sellRIndex);
                            changes = true;
                        } else {
                            double buyDFirst = c.getDouble(buyDFirstIndex);
                            double sellDFirst = c.getDouble(sellDFirstIndex);
                            double buyEFirst = c.getDouble(buyEFirstIndex);
                            double sellEFirst = c.getDouble(sellEFirstIndex);
                            double buyRFirst = c.getDouble(buyRFirstIndex);
                            double sellRFirst = c.getDouble(sellRFirstIndex);

                            if (buyDFirst != 0) {
                                changesBuyD = buyD - buyDFirst;
                            }
                            if (sellDFirst != 0) {
                                changesSellD = sellD - sellDFirst;
                            }
                            if (buyEFirst != 0) {
                                changesBuyE = buyE - buyEFirst;
                            }
                            if (sellEFirst != 0) {
                                changesSellE = sellE - sellEFirst;
                            }
                            if (buyRFirst != 0) {
                                changesBuyR = buyR - buyRFirst;
                            }
                            if (sellRFirst != 0) {
                                changesSellR = sellR - sellRFirst;
                            }
                        }
                    }
                    c.moveToLast();
                }
            } while (c.moveToNext());
        }
        else {
            c.close();
        }

        String dollarRequest = "";
        String euroRequest = "";
        String rubRequest = "";
        if (buyD != 0 && sellD != 0) {
            dollarRequest = "buyUSD = '" + buyD + "', sellUSD = '" + sellD + "', ";
        }
        if (buyE != 0 && sellE != 0) {
            euroRequest = "buyEUR = '" + buyE + "', sellEUR = '" + sellE + "', ";
        }
        if (buyR != 0 && sellR != 0) {
            rubRequest = "buyRUB = '" + buyR + "', sellRUB = '" + sellR + "', ";
        }

        if (changes) {
            db.execSQL("UPDATE " + TABLE_NAME_BANKS + " SET " + dollarRequest + euroRequest + rubRequest +
                    "changesBuyD = '" + changesBuyD +
                    "', changesSellD = '" + changesSellD +
                    "', changesBuyE = '" + changesBuyE +
                    "', changesSellE = '" + changesSellE +
                    "', changesBuyR = '" + changesBuyR +
                    "', changesSellR = '" + changesSellR +
                    "', buyDFirstInDay = '" + buyD +
                    "', sellDFirstInDay = '" + sellD +
                    "', buyEFirstInDay = '" + buyE +
                    "', sellEFirstInDay = '" + sellE +
                    "', buyRFirstInDay = '" + buyR +
                    "', sellRFirstInDay = '" + sellR +
                    "', dateServer = '" + dateServer +
                    "' WHERE name = '" + name + "'");
        }
        else {
            db.execSQL("UPDATE " + TABLE_NAME_BANKS + " SET " + dollarRequest + euroRequest + rubRequest +
                    "changesBuyD = '" + changesBuyD +
                    "', changesSellD = '" + changesSellD +
                    "', changesBuyE = '" + changesBuyE +
                    "', changesSellE = '" + changesSellE +
                    "', changesBuyR = '" + changesBuyR +
                    "', changesSellR = '" + changesSellR +
                    "', dateServer = '" + dateServer +
                    "' WHERE name = '" + name + "'");
        }

    }



    //---------------------------------------------------------------Uk------------------------------------------------------------------------

    public void setBankInfoUk(SQLiteDatabase db, String name, double buyUSD, double sellUSD, double buyEUR, double sellEUR, double buyRUB, double sellRUB, double buyCAD, double sellCAD, double buyTYR, double sellTYR, double buyPLN, double sellPLN, double buyILS, double sellILS, double buyCNY, double sellCNY, double buyCZK, double sellCZK, double buySEK, double sellSEK, double buyCHF, double sellCHF, double buyJPY, double sellJPY, String dateServer) {
        double changesBuyUSD = 0;
        double changesSellUSD = 0;
        double changesBuyEUR = 0;
        double changesSellEUR = 0;
        double changesBuyRUB = 0;
        double changesSellRUB = 0;
        double changesBuyCAD = 0;
        double changesSellCAD = 0;
        double changesBuyTYR = 0;
        double changesSellTYR = 0;
        double changesBuyPLN = 0;
        double changesSellPLN = 0;
        double changesBuyILS = 0;
        double changesSellILS = 0;
        double changesBuyCNY = 0;
        double changesSellCNY = 0;
        double changesBuyCZK = 0;
        double changesSellCZK = 0;
        double changesBuySEK = 0;
        double changesSellSEK = 0;
        double changesBuyCHF = 0;
        double changesSellCHF = 0;
        double changesBuyJPY = 0;
        double changesSellJPY = 0;


        Cursor c = db.query(Utils.getBanksTableName(), null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int buyUSDIndex = c.getColumnIndex("buyUSD");
            int sellUSDIndex = c.getColumnIndex("sellUSD");
            int buyEURIndex = c.getColumnIndex("buyEUR");
            int sellEURIndex = c.getColumnIndex("sellEUR");
            int buyRUBIndex = c.getColumnIndex("buyRUB");
            int sellRUBIndex = c.getColumnIndex("sellRUB");
            int buyCADIndex = c.getColumnIndex("buyCAD");
            int sellCADIndex = c.getColumnIndex("sellCAD");
            int buyTYRIndex = c.getColumnIndex("buyTYR");
            int sellTYRIndex = c.getColumnIndex("sellTYR");
            int buyPLNIndex = c.getColumnIndex("buyPLN");
            int sellPLNIndex = c.getColumnIndex("sellPLN");
            int buyILSIndex = c.getColumnIndex("buyILS");
            int sellILSIndex = c.getColumnIndex("sellILS");
            int buyCNYIndex = c.getColumnIndex("buyCNY");
            int sellCNYIndex = c.getColumnIndex("sellCNY");
            int buyCZKIndex = c.getColumnIndex("buyCZK");
            int sellCZKIndex = c.getColumnIndex("sellCZK");
            int buySEKIndex = c.getColumnIndex("buySEK");
            int sellSEKIndex = c.getColumnIndex("sellSEK");
            int buyCHFIndex = c.getColumnIndex("buyCHF");
            int sellCHFIndex = c.getColumnIndex("sellCHF");
            int buyJPYIndex = c.getColumnIndex("buyJPY");
            int sellJPYIndex = c.getColumnIndex("sellJPY");

            int nameIndex = c.getColumnIndex("name");
            int dateServerIndex = c.getColumnIndex("dateServer");

            do {
                if (name.toLowerCase().equals(c.getString(nameIndex).toLowerCase())) {
                    if ((!c.getString(dateServerIndex).isEmpty()) && (!dateServer.substring(0, 10).equals(c.getString(dateServerIndex).substring(0, 10)))) {
                        changesBuyUSD = buyUSD - c.getDouble(buyUSDIndex);
                        changesSellUSD = sellUSD - c.getDouble(sellUSDIndex);
                        changesBuyEUR = buyEUR - c.getDouble(buyEURIndex);
                        changesSellEUR = sellEUR - c.getDouble(sellEURIndex);
                        changesBuyRUB = buyRUB - c.getDouble(buyRUBIndex);
                        changesSellRUB = sellRUB - c.getDouble(sellRUBIndex);
                        changesBuyCAD = buyCAD - c.getDouble(buyCADIndex);
                        changesSellCAD = sellCAD - c.getDouble(sellCADIndex);
                        changesBuyTYR = buyTYR - c.getDouble(buyTYRIndex);
                        changesSellTYR = sellTYR - c.getDouble(sellTYRIndex);
                        changesBuyPLN = buyPLN - c.getDouble(buyPLNIndex);
                        changesSellPLN = sellPLN - c.getDouble(sellPLNIndex);
                        changesBuyILS = buyILS - c.getDouble(buyILSIndex);
                        changesSellILS = sellILS - c.getDouble(sellILSIndex);
                        changesBuyCNY = buyCNY - c.getDouble(buyCNYIndex);
                        changesSellCNY = sellCNY - c.getDouble(sellCNYIndex);
                        changesBuyCZK = buyCZK - c.getDouble(buyCZKIndex);
                        changesSellCZK = sellCZK - c.getDouble(sellCZKIndex);
                        changesBuySEK = buySEK - c.getDouble(buySEKIndex);
                        changesSellSEK = sellSEK - c.getDouble(sellSEKIndex);
                        changesBuyCHF = buyCHF - c.getDouble(buyCHFIndex);
                        changesSellCHF = sellCHF - c.getDouble(sellCHFIndex);
                        changesBuyJPY = buyJPY - c.getDouble(buyJPYIndex);
                        changesSellJPY = sellJPY - c.getDouble(sellJPYIndex);

                    }
                }
                c.moveToLast();
            } while (c.moveToNext());
        }
        else {
            c.close();
        }



        String buyUsdRequest = (buyUSD != 0) ? "buyUSD = '" + buyUSD + "', " : "";
        String sellUsdRequest = (sellUSD != 0) ? "sellUSD = '" + sellUSD + "', " : "";
        String buyEurRequest = (buyEUR != 0) ? "buyEUR = '" + buyEUR + "', " : "";
        String sellEurRequest = (sellEUR != 0) ? "sellEUR = '" + sellEUR + "', " : "";
        String buyRubRequest = (buyRUB != 0) ? "buyRUB = '" + buyRUB + "', " : "";
        String sellRubRequest = (sellRUB != 0) ? "sellRUB = '" + sellRUB + "', " : "";
        String buyCadRequest = (buyCAD != 0) ? "buyCAD = '" + buyCAD + "', " : "";
        String sellCadRequest = (sellCAD != 0) ? "sellCAD = '" + sellCAD + "', " : "";
        String buyTyrRequest = (buyTYR != 0) ? "buyTYR = '" + buyTYR + "', " : "";
        String sellTyrRequest = (sellTYR != 0) ? "sellTYR = '" + sellTYR + "', " : "";
        String buyPlnRequest = (buyPLN != 0) ? "buyPLN = '" + buyPLN + "', " : "";
        String sellPlnRequest = (sellPLN != 0) ? "sellPLN = '" + sellPLN + "', " : "";
        String buyIlsRequest = (buyILS != 0) ? "buyILS = '" + buyILS + "', " : "";
        String sellIlsRequest = (sellILS != 0) ? "sellILS = '" + sellILS + "', " : "";
        String buyCnyRequest = (buyCNY != 0) ? "buyCNY = '" + buyCNY + "', " : "";
        String sellCnyRequest = (sellCNY != 0) ? "sellCNY = '" + sellCNY + "', " : "";
        String buyCzkRequest = (buyCZK != 0) ? "buyCZK = '" + buyCZK + "', " : "";
        String sellCzkRequest = (sellCZK != 0) ? "sellCZK = '" + sellCZK + "', " : "";
        String buySekRequest = (buySEK != 0) ? "buySEK = '" + buySEK + "', " : "";
        String sellSekRequest = (sellSEK != 0) ? "sellSEK = '" + sellSEK + "', " : "";
        String buyChfRequest = (buyCHF != 0) ? "buyCHF = '" + buyCHF + "', " : "";
        String sellChfRequest = (sellCHF != 0) ? "sellCHF = '" + sellCHF + "', " : "";
        String buyJpyRequest = (buyJPY != 0) ? "buyJPY = '" + buyJPY + "', " : "";
        String sellJpyRequest = (sellJPY != 0) ? "sellJPY = '" + sellJPY + "', " : "";

        db.execSQL("UPDATE " + Utils.getBanksTableName() + " SET " + buyUsdRequest + sellUsdRequest
                + buyEurRequest + sellEurRequest
                + buyRubRequest + sellRubRequest
                + buyCadRequest + sellCadRequest
                + buyTyrRequest + sellTyrRequest
                + buyPlnRequest + sellPlnRequest
                + buyIlsRequest + sellIlsRequest
                + buyCnyRequest + sellCnyRequest
                + buyCzkRequest + sellCzkRequest
                + buySekRequest + sellSekRequest
                + buyChfRequest + sellChfRequest
                + buyJpyRequest + sellJpyRequest
                + " changesBuyUSD = '" + changesBuyUSD +
                "', changesSellUSD = '" + changesSellUSD +
                "', changesBuyEUR = '" + changesBuyEUR +
                "', changesSellEUR = '" + changesSellEUR +
                "', changesBuyRUB = '" + changesBuyRUB +
                "', changesSellRUB = '" + changesSellRUB +
                "', changesBuyCAD = '" + changesBuyCAD +
                "', changesSellCAD = '" + changesSellCAD +
                "', changesBuyTYR = '" + changesBuyTYR +
                "', changesSellTYR = '" + changesSellTYR +
                "', changesBuyPLN = '" + changesBuyPLN +
                "', changesSellPLN = '" + changesSellPLN +
                "', changesBuyILS = '" + changesBuyILS +
                "', changesSellILS = '" + changesSellILS +
                "', changesBuyCNY = '" + changesBuyCNY +
                "', changesSellCNY = '" + changesSellCNY +
                "', changesBuyCZK = '" + changesBuyCZK +
                "', changesSellCZK = '" + changesSellCZK +
                "', changesBuySEK = '" + changesBuySEK +
                "', changesSellSEK = '" + changesSellSEK +
                "', changesBuyCHF = '" + changesBuyCHF +
                "', changesSellCHF = '" + changesSellCHF +
                "', changesBuyJPY = '" + changesBuyJPY +
                "', changesSellJPY = '" + changesSellJPY +
                "', dateServer = '" + dateServer +
                "' WHERE name = '" + name + "'");
    }




    //---------------------------------------------------------------Poland------------------------------------------------------------------------

    public void setBankInfoPoland(SQLiteDatabase db, String name, double buyUSD, double sellUSD, double buyEUR, double sellEUR, double buyGBP, double sellGBP, double buyCHF, double sellCHF, String dateServer) {
        double changesBuyUSD = 0;
        double changesSellUSD = 0;
        double changesBuyEUR = 0;
        double changesSellEUR = 0;
        double changesBuyGBP = 0;
        double changesSellGBP = 0;
        double changesBuyCHF = 0;
        double changesSellCHF = 0;


        Cursor c = db.query(Utils.getBanksTableName(), null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int buyUSDIndex = c.getColumnIndex("buyUSD");
            int sellUSDIndex = c.getColumnIndex("sellUSD");
            int buyEURIndex = c.getColumnIndex("buyEUR");
            int sellEURIndex = c.getColumnIndex("sellEUR");
            int buyGBPIndex = c.getColumnIndex("buyGBP");
            int sellGBPIndex = c.getColumnIndex("sellGBP");
            int buyCHFIndex = c.getColumnIndex("buyCHF");
            int sellCHFIndex = c.getColumnIndex("sellCHF");

            int nameIndex = c.getColumnIndex("name");
            int dateServerIndex = c.getColumnIndex("dateServer");

            do {
                if (name.toLowerCase().equals(c.getString(nameIndex).toLowerCase())) {
                    if ((!c.getString(dateServerIndex).isEmpty()) && (!dateServer.substring(0, 10).equals(c.getString(dateServerIndex).substring(0, 10)))) {
                        changesBuyUSD = buyUSD - c.getDouble(buyUSDIndex);
                        changesSellUSD = sellUSD - c.getDouble(sellUSDIndex);
                        changesBuyEUR = buyEUR - c.getDouble(buyEURIndex);
                        changesSellEUR = sellEUR - c.getDouble(sellEURIndex);
                        changesBuyGBP = buyGBP - c.getDouble(buyGBPIndex);
                        changesSellGBP = sellGBP - c.getDouble(sellGBPIndex);
                        changesBuyCHF = buyCHF - c.getDouble(buyCHFIndex);
                        changesSellCHF = sellCHF - c.getDouble(sellCHFIndex);
                    }
                }
                c.moveToLast();
            } while (c.moveToNext());
        }
        else {
            c.close();
        }


        String buyUsdRequest = (buyUSD != 0) ? "buyUSD = '" + buyUSD + "', " : "";
        String sellUsdRequest = (sellUSD != 0) ? "sellUSD = '" + sellUSD + "', " : "";
        String buyEurRequest = (buyEUR != 0) ? "buyEUR = '" + buyEUR + "', " : "";
        String sellEurRequest = (sellEUR != 0) ? "sellEUR = '" + sellEUR + "', " : "";
        String buyGBPRequest = (buyGBP != 0) ? "buyGBP = '" + buyGBP + "', " : "";
        String sellGBPRequest = (sellGBP != 0) ? "sellGBP = '" + sellGBP + "', " : "";
        String buyChfRequest = (buyCHF != 0) ? "buyCHF = '" + buyCHF + "', " : "";
        String sellChfRequest = (sellCHF != 0) ? "sellCHF = '" + sellCHF + "', " : "";

        db.execSQL("UPDATE " + Utils.getBanksTableName() + " SET " + buyUsdRequest + sellUsdRequest
                + buyEurRequest + sellEurRequest
                + buyGBPRequest + sellGBPRequest
                + buyChfRequest + sellChfRequest
                + " changesBuyUSD = '" + changesBuyUSD +
                "', changesSellUSD = '" + changesSellUSD +
                "', changesBuyEUR = '" + changesBuyEUR +
                "', changesSellEUR = '" + changesSellEUR +
                "', changesBuyGBP = '" + changesBuyGBP +
                "', changesSellGBP = '" + changesSellGBP +
                "', changesBuyCHF = '" + changesBuyCHF +
                "', changesSellCHF = '" + changesSellCHF +
                "', dateServer = '" + dateServer +
                "' WHERE name = '" + name + "'");
    }




    //---------------------------------------------------------------Russia------------------------------------------------------------------------

    public void setBankInfoRussia(SQLiteDatabase db, String name, double buyUSD, double sellUSD, double buyEUR, double sellEUR, String dateServer) {
        double changesBuyUSD = 0;
        double changesSellUSD = 0;
        double changesBuyEUR = 0;
        double changesSellEUR = 0;


        Cursor c = db.query(Utils.getBanksTableName(), null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int buyUSDIndex = c.getColumnIndex("buyUSD");
            int sellUSDIndex = c.getColumnIndex("sellUSD");
            int buyEURIndex = c.getColumnIndex("buyEUR");
            int sellEURIndex = c.getColumnIndex("sellEUR");

            int nameIndex = c.getColumnIndex("name");
            int dateServerIndex = c.getColumnIndex("dateServer");

            do {
                if (name.toLowerCase().equals(c.getString(nameIndex).toLowerCase())) {
                    if ((!c.getString(dateServerIndex).isEmpty()) && (!dateServer.substring(0, 10).equals(c.getString(dateServerIndex).substring(0, 10)))) {
                        changesBuyUSD = buyUSD - c.getDouble(buyUSDIndex);
                        changesSellUSD = sellUSD - c.getDouble(sellUSDIndex);
                        changesBuyEUR = buyEUR - c.getDouble(buyEURIndex);
                        changesSellEUR = sellEUR - c.getDouble(sellEURIndex);
                    }
                }
                c.moveToLast();
            } while (c.moveToNext());
        }
        else {
            c.close();
        }


        String buyUsdRequest = (buyUSD != 0) ? "buyUSD = '" + buyUSD + "', " : "";
        String sellUsdRequest = (sellUSD != 0) ? "sellUSD = '" + sellUSD + "', " : "";
        String buyEurRequest = (buyEUR != 0) ? "buyEUR = '" + buyEUR + "', " : "";
        String sellEurRequest = (sellEUR != 0) ? "sellEUR = '" + sellEUR + "', " : "";

        db.execSQL("UPDATE " + Utils.getBanksTableName() + " SET " + buyUsdRequest + sellUsdRequest
                + buyEurRequest + sellEurRequest
                + " changesBuyUSD = '" + changesBuyUSD +
                "', changesSellUSD = '" + changesSellUSD +
                "', changesBuyEUR = '" + changesBuyEUR +
                "', changesSellEUR = '" + changesSellEUR +
                "', dateServer = '" + dateServer +
                "' WHERE name = '" + name + "'");
    }



    //--------------------------------------------------------For at least few countries------------------------------------------------------------
    public void setCurrenciesInfo(SQLiteDatabase db, String name, String country, String description, double rate, double rateSell, String date) {
        String tM;
        switch (Utils.country_code) {
            case Utils.USA_CODE:
                tM = Utils.TABLE_NAME_CURRENCIES_Usa;
                break;
            case Utils.EUROPE_CODE:
                tM = Utils.TABLE_NAME_CURRENCIES_Europe;
                break;
            case Utils.UK_CODE:
                tM = Utils.TABLE_NAME_CURRENCIES_Uk;
                break;
            case Utils.POLAND_CODE:
                tM = Utils.TABLE_NAME_CURRENCIES_Poland;
                break;
            case Utils.TURKEY_CODE:
                tM = Utils.TABLE_NAME_CURRENCIES_Turkey;
                break;
            case Utils.RUSSIA_CODE:
                tM = Utils.TABLE_NAME_CURRENCIES_Russia;
                break;
            case Utils.UKRAINE_CODE:
                tM = Utils.TABLE_NAME_CURRENCIES_Ukraine;
                break;
            default:
                tM = Utils.TABLE_NAME_CURRENCIES_Uk;
                break;
        }
        db.execSQL("UPDATE " + tM + " SET " +
                "rate  = '" + rate +
                "', rateSell = '" + rateSell +
                "', date = '" + date +
                "' WHERE name = '" + name + "'");
    }

    //update different cells for one stroke
    public void setCbankInfo(SQLiteDatabase db, String name, double rate, String date) {
        //name of cell for update in db
        String updateRate = "";
        String updateChanges = "";

        //if name of currency is in switch case
        boolean updates = true;

        switch (name) {
            case "USD":
                updateRate = "rateUSD";
                updateChanges = "changesUSD";
                break;
            case "GBP":
                updateRate = "rateGBP";
                updateChanges = "changesGBP";
                break;
            case "EUR":
                updateRate = "rateEUR";
                updateChanges = "changesEUR";
                break;
            case "RUB":
                updateRate = "rateRUB";
                updateChanges = "changesRUB";
                break;
            case "CAD":
                updateRate = "rateCAD";
                updateChanges = "changesCAD";
                break;
            case "TRY":
                updateRate = "rateTYR";
                updateChanges = "changesTYR";
                break;
            case "PLN":
                updateRate = "ratePLN";
                updateChanges = "changesPLN";
                break;
            case "ILS":
                updateRate = "rateILS";
                updateChanges = "changesILS";
                break;
            case "CNY":
                updateRate = "rateCNY";
                updateChanges = "changesCNY";
                break;
            case "CZK":
                updateRate = "rateCZK";
                updateChanges = "changesCZK";
                break;
            case "SEK":
                updateRate = "rateSEK";
                updateChanges = "changesSEK";
                break;
            case "CHF":
                updateRate = "rateCHF";
                updateChanges = "changesCHF";
                break;
            case "JPY":
                updateRate = "rateJPY";
                updateChanges = "changesJPY";
                break;
            default:
                updates = false;
                break;
        }

        if (updates) {
            String[] columns = {updateRate};
            double changes = 0;
            Cursor c = db.query(Utils.getCBankTableName(), columns, null, null, null, null, null);
            if (c != null) {
                int indexRate = c.getColumnIndex(updateRate);
                if ((indexRate != 0) && c.getDouble(indexRate) != 0) {
                    changes = rate - c.getDouble(indexRate);
                }

            }
            db.execSQL("UPDATE " + Utils.getCBankTableName() + " SET " + updateRate + " = '" + rate + "', " + updateChanges + " = '" + changes + "', dateServer = '" + date + "' WHERE id = 1");
        }

    }
}
