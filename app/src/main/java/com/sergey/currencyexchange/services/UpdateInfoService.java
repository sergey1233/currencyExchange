package com.sergey.currencyexchange.services;


import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sergey.currencyexchange.model.ApplicationInfo;
import com.sergey.currencyexchange.model.Bank;
import com.sergey.currencyexchange.model.BlackMarket;
import com.sergey.currencyexchange.model.DBHelper;
import com.sergey.currencyexchange.model.DBInterface;
import com.sergey.currencyexchange.model.MBank;
import com.sergey.currencyexchange.model.Nbu;
import com.sergey.currencyexchange.model.Utils;
import com.sergey.currencyexchange.ui.MainActivity;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;


public class UpdateInfoService extends Service {

    private static final String URL = "http://currencyexchange.zzz.com.ua/";
    private final String NBU = "nbu";
    private final String MB = "mb";
    private final String BLACKM = "blackM";
    private final String BANKS = "banks";

    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(URL)
            .build();
    private DBInterface intf = retrofit.create(DBInterface.class);
    private DBHelper dbHelper;
    private static final int DBVERSION = 12;
    private SQLiteDatabase db;

    private ApplicationInfo app;
    private Nbu nbu;
    private MBank mBank;
    private BlackMarket blackMarket;
    private ArrayList<Bank> bankList = new ArrayList<>();

    private static CountDownLatch latch;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        dbHelper = new DBHelper(this, DBVERSION);
        db = dbHelper.getWritableDatabase();

        app = ApplicationInfo.getInstance();
        nbu = app.getNbu();
        mBank = app.getMBank();
        blackMarket = app.getBlackMarket();
        bankList = app.getBankList();
    }

    public void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new UpdateRun()).start();

        new Thread(new UpdateCall(NBU)).start();
        new Thread(new UpdateCall(MB)).start();
        new Thread(new UpdateCall(BLACKM)).start();
        new Thread(new UpdateCall(BANKS)).start();

        return super.onStartCommand(intent, flags, startId);
    }


    public void callNbu() {

        Call<Object> callNbu = intf.request(Nbu.getUrlType());
        callNbu.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Map<String, String> map = (Map)response.body();
                double rateD = Double.parseDouble(map.get("rateD"));
                double rateE = Double.parseDouble(map.get("rateE"));
                double rateR = Double.parseDouble(map.get("rateR"));
                String dateServer = map.get("dateServer");
                dbHelper.setNbuInfo(db, rateD, rateE, rateR, dateServer.substring(0, 16));
                getData(nbu);
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                getData(nbu);
            }
        });
    }

    public void callMBank() {
        Call<Object> callMBank = intf.request(MBank.getUrlType());
        callMBank.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                ArrayList<Map<String, String>> mapArray = (ArrayList<Map<String, String>>)response.body();
                for (Map<String, String> map : mapArray) {
                    int idTime = Integer.parseInt(map.get("idTime"));
                    double buyD = Double.parseDouble(map.get("buyD"));
                    double sellD = Double.parseDouble(map.get("sellD"));
                    double buyE = Double.parseDouble(map.get("buyE"));
                    double sellE = Double.parseDouble(map.get("sellE"));
                    double buyR = Double.parseDouble(map.get("buyR"));
                    double sellR = Double.parseDouble(map.get("sellR"));
                    String dateServer = map.get("dateServer");
                    dbHelper.setMBInfo(db, idTime, buyD, sellD, buyE, sellE, buyR, sellR, dateServer.substring(0, 16));
                }
                getData(mBank);
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                getData(mBank);
            }
        });
    }

    public void callBlackM() {
        Call<Object> callBlackM = intf.request(BlackMarket.getUrlType());
        callBlackM.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Map<String, String> map = (Map)response.body();
                double buyD = Double.parseDouble(map.get("buyD"));
                double sellD = Double.parseDouble(map.get("sellD"));
                double buyE = Double.parseDouble(map.get("buyE"));
                double sellE = Double.parseDouble(map.get("sellE"));
                double buyR = Double.parseDouble(map.get("buyR"));
                double sellR = Double.parseDouble(map.get("sellR"));
                String dateServer = map.get("dateServer");
                dbHelper.setBlackMInfo(db, buyD, sellD, buyE, sellE, buyR, sellR, dateServer.substring(0, 16));
                getData(blackMarket);
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                getData(blackMarket);
            }
        });
    }

    public void callBanks() {
        Call<Object> callBank = intf.request(Bank.getUrlType());
        callBank.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                ArrayList<Map<String, String>> mapArray = (ArrayList<Map<String, String>>)response.body();
                for (Map<String, String> map : mapArray) {
                    String name = map.get("name");
                    double buyD = Double.parseDouble(map.get("buyD"));
                    double sellD = Double.parseDouble(map.get("sellD"));
                    double buyE = Double.parseDouble(map.get("buyE"));
                    double sellE = Double.parseDouble(map.get("sellE"));
                    double buyR = Double.parseDouble(map.get("buyR"));
                    double sellR = Double.parseDouble(map.get("sellR"));
                    String dateServer = map.get("dateServer");
                    dbHelper.setBankInfo(db, name, buyD, sellD, buyE, sellE, buyR, sellR, dateServer.substring(0, 16));
                }
                getData(bankList);
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                getData(bankList);
            }
        });
    }

    public void getData(Nbu nbu) {
        Cursor c = db.query(NBU, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int rateDIndex = c.getColumnIndex("rateD");
            int rateEIndex = c.getColumnIndex("rateE");
            int rateRIndex = c.getColumnIndex("rateR");
            int changesDIndex = c.getColumnIndex("changesD");
            int changesEIndex = c.getColumnIndex("changesE");
            int changesRIndex = c.getColumnIndex("changesR");
            int dateServerIndex = c.getColumnIndex("dateServer");

            do {
                nbu.setNewInformation(c.getString(dateServerIndex), c.getDouble(rateDIndex), c.getDouble(rateEIndex), c.getDouble(rateRIndex), c.getDouble(changesDIndex), c.getDouble(changesEIndex), c.getDouble(changesRIndex));
            } while (c.moveToNext());
        }
        else {
            c.close();
        }
        latch.countDown();
    }

    public void getData(MBank mBank) {
        ArrayList<String> dateArray = new ArrayList<>();
        ArrayList<Double> buyDArray = new ArrayList<>();
        ArrayList<Double> sellDArray = new ArrayList<>();
        ArrayList<Double> buyEArray = new ArrayList<>();
        ArrayList<Double> sellEArray = new ArrayList<>();
        ArrayList<Double> buyRArray = new ArrayList<>();
        ArrayList<Double> sellRArray = new ArrayList<>();

        Cursor c = db.query(MB, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int buyDIndex = c.getColumnIndex("buyD");
            int sellDIndex = c.getColumnIndex("sellD");
            int buyEIndex = c.getColumnIndex("buyE");
            int sellEIndex = c.getColumnIndex("sellE");
            int buyRIndex = c.getColumnIndex("buyR");
            int sellRIndex = c.getColumnIndex("sellR");
            int dateServerIndex = c.getColumnIndex("dateServer");

            do {
                if (c.getDouble(buyDIndex) != 0 && c.getDouble(sellDIndex) != 0) {
                    buyDArray.add(c.getDouble(buyDIndex));
                    sellDArray.add(c.getDouble(sellDIndex));
                    dateArray.add(c.getString(dateServerIndex));
                }
                if (c.getDouble(buyEIndex) != 0 && c.getDouble(sellEIndex) != 0) {
                    buyEArray.add(c.getDouble(buyEIndex));
                    sellEArray.add(c.getDouble(sellEIndex));
                }
                if (c.getDouble(buyRIndex) != 0 && c.getDouble(sellRIndex) != 0) {
                    buyRArray.add(c.getDouble(buyRIndex));
                    sellRArray.add(c.getDouble(sellRIndex));
                }
            } while (c.moveToNext());
        }
        else {
            c.close();
        }

        mBank.setNewInformation(Utils.toPrimitiveStringArray(dateArray), Utils.toPrimitiveDoubleArray(buyDArray), Utils.toPrimitiveDoubleArray(sellDArray), Utils.toPrimitiveDoubleArray(buyEArray), Utils.toPrimitiveDoubleArray(sellEArray), Utils.toPrimitiveDoubleArray(buyRArray), Utils.toPrimitiveDoubleArray(sellRArray));
        latch.countDown();
    }

    public void getData(BlackMarket blackMarket) {
        Cursor c = db.query(BLACKM, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int buyDIndex = c.getColumnIndex("buyD");
            int sellDIndex = c.getColumnIndex("sellD");
            int buyEIndex = c.getColumnIndex("buyE");
            int sellEIndex = c.getColumnIndex("sellE");
            int buyRIndex = c.getColumnIndex("buyR");
            int sellRIndex = c.getColumnIndex("sellR");
            int changesBuyDIndex = c.getColumnIndex("changesBuyD");
            int changesSellDIndex = c.getColumnIndex("changesSellD");
            int changesBuyEIndex = c.getColumnIndex("changesBuyE");
            int changesSellEIndex = c.getColumnIndex("changesSellE");
            int changesBuyRIndex = c.getColumnIndex("changesBuyR");
            int changesSellRIndex = c.getColumnIndex("changesSellR");
            int dateServerIndex = c.getColumnIndex("dateServer");

            do {
                blackMarket.setNewInformation(c.getString(dateServerIndex), c.getDouble(buyDIndex), c.getDouble(sellDIndex), c.getDouble(buyEIndex), c.getDouble(sellEIndex), c.getDouble(buyRIndex), c.getDouble(sellRIndex), c.getDouble(changesBuyDIndex), c.getDouble(changesSellDIndex), c.getDouble(changesBuyEIndex), c.getDouble(changesSellEIndex), c.getDouble(changesBuyRIndex), c.getDouble(changesSellRIndex));
            } while (c.moveToNext());
        }
        else {
            c.close();
        }
        latch.countDown();
    }

    public void getData(ArrayList<Bank> bankList) {
        Cursor c = db.query(BANKS, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int nameIndex = c.getColumnIndex("name");
            int buyDIndex = c.getColumnIndex("buyD");
            int sellDIndex = c.getColumnIndex("sellD");
            int buyEIndex = c.getColumnIndex("buyE");
            int sellEIndex = c.getColumnIndex("sellE");
            int buyRIndex = c.getColumnIndex("buyR");
            int sellRIndex = c.getColumnIndex("sellR");
            int changesBuyDIndex = c.getColumnIndex("changesBuyD");
            int changesSellDIndex = c.getColumnIndex("changesSellD");
            int changesBuyEIndex = c.getColumnIndex("changesBuyE");
            int changesSellEIndex = c.getColumnIndex("changesSellE");
            int changesBuyRIndex = c.getColumnIndex("changesBuyR");
            int changesSellRIndex = c.getColumnIndex("changesSellR");
            int dateServerIndex = c.getColumnIndex("dateServer");

            do {
                for (int i = 0; i < bankList.size(); i++) {
                    if (bankList.get(i).getName().equals(c.getString(nameIndex))) {
                        bankList.get(i).setNewInformation(c.getString(dateServerIndex), c.getDouble(buyDIndex), c.getDouble(sellDIndex), c.getDouble(buyEIndex), c.getDouble(sellEIndex), c.getDouble(buyRIndex), c.getDouble(sellRIndex), c.getDouble(changesBuyDIndex), c.getDouble(changesSellDIndex), c.getDouble(changesBuyEIndex), c.getDouble(changesSellEIndex), c.getDouble(changesBuyRIndex), c.getDouble(changesSellRIndex));
                        break;
                    }
                }
            } while (c.moveToNext());
        }
        else {
            c.close();
        }
        latch.countDown();
    }

    class UpdateRun implements Runnable  {
        @Override
        public void run() {
            latch = new CountDownLatch(4);

            try {
                latch.await();
            }
            catch (InterruptedException ie) {}

            stop();
        }

        void stop() {
            Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
            sendBroadcast(intent);
            stopSelf();
        }
    }

    class UpdateCall implements Runnable {
        private String name;

        public UpdateCall (String name) {

            this.name = name;

        }

        @Override
        public void run() {
            if (name.equals(NBU)) {
                callNbu();
            }
            else if (name.equals(MB)) {
                callMBank();
            }
            else if (name.equals(BLACKM)) {
                callBlackM();
            }
            else if (name.equals((BANKS))) {
                callBanks();
            }
        }
    }
}
