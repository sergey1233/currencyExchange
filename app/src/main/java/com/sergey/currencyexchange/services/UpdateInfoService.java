package com.sergey.currencyexchange.services;


import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.AppResponse;
import com.sergey.currencyexchange.model.AppResponseInterface;
import com.sergey.currencyexchange.model.AppResponseInterfaceUkraine;
import com.sergey.currencyexchange.model.AppResponseUkraine;
import com.sergey.currencyexchange.model.ApplicationInfo;
import com.sergey.currencyexchange.model.Bank;
import com.sergey.currencyexchange.model.BlackMarket;
import com.sergey.currencyexchange.model.CentralBank;
import com.sergey.currencyexchange.model.Country;
import com.sergey.currencyexchange.model.Currency;
import com.sergey.currencyexchange.model.DBHelper;
import com.sergey.currencyexchange.model.MBank;
import com.sergey.currencyexchange.model.Utils;
import com.sergey.currencyexchange.ui.MainActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;


public class UpdateInfoService extends Service {

    private static final String TAG = "UpdateInfoService";
    private static final String URL = "http://exrate.com.ua//";
    private final String NBU = Utils.NBU_DB_NAME;
    private final String MB = Utils.MB_DB_NAME;
    private final String BLACKM = Utils.BLACKM_DB_NAME;
    private final String BANKS = Utils.BANKS_DB_NAME;
    private final String ALLDATA = "allData";

    private Gson gson = new GsonBuilder().create();
    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(13, TimeUnit.SECONDS)
            .connectTimeout(13, TimeUnit.SECONDS)
            .build();
    private Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(URL)
            .build();
    private DBHelper dbHelper;
    private static final int DBVERSION = Utils.DBVERSION;
    private SQLiteDatabase db;

    private ApplicationInfo app;
    private Country country;
    private CentralBank centralBank;
    private MBank mBank;
    private BlackMarket blackMarket;
    private ArrayList<Bank> bankList;

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
        country = app.getCountry();
        centralBank = country.getCBank();
        mBank = country.getMBank();
        blackMarket = country.getBlackMarket();
        bankList = country.getBankList();
    }

    public void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new UpdateRun()).start();
        new Thread(new UpdateCall(ALLDATA)).start();

        return super.onStartCommand(intent, flags, startId);
    }

    public void callDataUsa() {
        AppResponseInterface interfaceApp = retrofit.create(AppResponseInterface.class);
        Call<AppResponse> callAppData = interfaceApp.request(ApplicationInfo.getInstance().getUrlType());

        callAppData.enqueue(new Callback<AppResponse>() {
            @Override
            public void onResponse(Call<AppResponse> call, Response<AppResponse> response) {
                AppResponse appResponse = response.body();

                ArrayList<Map<String, String>> currencyListResponse = appResponse.getCurrencyListResponse();
                if ((currencyListResponse != null) && (!currencyListResponse.isEmpty())) {
                    callCurrencies(currencyListResponse);
                }
                else {
                    stopThisService();
                }
            }

            @Override
            public void onFailure(Call<AppResponse> call, Throwable t) {
                Toast.makeText(UpdateInfoService.this, R.string.inet_problem, Toast.LENGTH_SHORT).show();
                stopThisService();
            }
        });
    }

    public void callDataEurope() {
        AppResponseInterface interfaceApp = retrofit.create(AppResponseInterface.class);
        Call<AppResponse> callAppData = interfaceApp.request(ApplicationInfo.getInstance().getUrlType());

        callAppData.enqueue(new Callback<AppResponse>() {
            @Override
            public void onResponse(Call<AppResponse> call, Response<AppResponse> response) {
                AppResponse appResponse = response.body();

                ArrayList<Map<String, String>> currencyListResponse = appResponse.getCurrencyListResponse();
                if ((currencyListResponse != null) && (!currencyListResponse.isEmpty())) {
                    callCurrencies(currencyListResponse);
                }
                else {
                    stopThisService();
                }
            }

            @Override
            public void onFailure(Call<AppResponse> call, Throwable t) {
                Toast.makeText(UpdateInfoService.this, R.string.inet_problem, Toast.LENGTH_SHORT).show();
                stopThisService();
            }
        });
    }

    public void callDataTurkey() {
        AppResponseInterface interfaceApp = retrofit.create(AppResponseInterface.class);
        Call<AppResponse> callAppData = interfaceApp.request(ApplicationInfo.getInstance().getUrlType());

        callAppData.enqueue(new Callback<AppResponse>() {
            @Override
            public void onResponse(Call<AppResponse> call, Response<AppResponse> response) {
                AppResponse appResponse = response.body();

                ArrayList<Map<String, String>> currencyListResponse = appResponse.getCurrencyListResponse();
                if ((currencyListResponse != null) && (!currencyListResponse.isEmpty())) {
                    callCurrencies(currencyListResponse);
                }
                else {
                    stopThisService();
                }
            }

            @Override
            public void onFailure(Call<AppResponse> call, Throwable t) {
                Toast.makeText(UpdateInfoService.this, R.string.inet_problem, Toast.LENGTH_SHORT).show();
                stopThisService();
            }
        });
    }

    public void callDataUk() {
        AppResponseInterface interfaceApp = retrofit.create(AppResponseInterface.class);
        Call<AppResponse> callAppData = interfaceApp.request(ApplicationInfo.getInstance().getUrlType());

        callAppData.enqueue(new Callback<AppResponse>() {
            @Override
            public void onResponse(Call<AppResponse> call, Response<AppResponse> response) {
                AppResponse appResponse = response.body();

                ArrayList<Map<String, String>> bankListResponse = appResponse.getBankListResponse();
                if ((bankListResponse != null) && (!bankListResponse.isEmpty())) {
                    callBanksUk(bankListResponse);
                }
                else {
                    latch.countDown();
                }

                ArrayList<Map<String, String>> currencyListResponse = appResponse.getCurrencyListResponse();
                if ((currencyListResponse != null) && (!currencyListResponse.isEmpty())) {
                    callCurrencies(currencyListResponse);
                    callCBankUk(currencyListResponse);
                }
                else {
                    latch.countDown();
                    latch.countDown();
                    latch.countDown();
                    latch.countDown();
                }
            }

            @Override
            public void onFailure(Call<AppResponse> call, Throwable t) {
                Toast.makeText(UpdateInfoService.this, R.string.inet_problem, Toast.LENGTH_SHORT).show();
                stopThisService();
            }
        });
    }

    public void callDataPoland() {
        AppResponseInterface interfaceApp = retrofit.create(AppResponseInterface.class);
        Call<AppResponse> callAppData = interfaceApp.request(ApplicationInfo.getInstance().getUrlType());

        callAppData.enqueue(new Callback<AppResponse>() {
            @Override
            public void onResponse(Call<AppResponse> call, Response<AppResponse> response) {
                AppResponse appResponse = response.body();

                ArrayList<Map<String, String>> bankListResponse = appResponse.getBankListResponse();
                if ((bankListResponse != null) && (!bankListResponse.isEmpty())) {
                    callBanksPoland(bankListResponse);
                } else {
                    latch.countDown();
                }

                ArrayList<Map<String, String>> currencyListResponse = appResponse.getCurrencyListResponse();
                if ((currencyListResponse != null) && (!currencyListResponse.isEmpty())) {
                    callCurrencies(currencyListResponse);
                    callCBankPoland(currencyListResponse);
                } else {
                    latch.countDown();
                    latch.countDown();
                    latch.countDown();
                    latch.countDown();
                }
            }

            @Override
            public void onFailure(Call<AppResponse> call, Throwable t) {
                Toast.makeText(UpdateInfoService.this, R.string.inet_problem, Toast.LENGTH_SHORT).show();
                stopThisService();
            }
        });
    }

    public void callDataRussia() {
        AppResponseInterface interfaceApp = retrofit.create(AppResponseInterface.class);
        Call<AppResponse> callAppData = interfaceApp.request(ApplicationInfo.getInstance().getUrlType());

        callAppData.enqueue(new Callback<AppResponse>() {
            @Override
            public void onResponse(Call<AppResponse> call, Response<AppResponse> response) {
                AppResponse appResponse = response.body();

                ArrayList<Map<String, String>> bankListResponse = appResponse.getBankListResponse();
                if ((bankListResponse != null) && (!bankListResponse.isEmpty())) {
                    callBanksRussia(bankListResponse);
                } else {
                    latch.countDown();
                }

                ArrayList<Map<String, String>> currencyListResponse = appResponse.getCurrencyListResponse();
                if ((currencyListResponse != null) && (!currencyListResponse.isEmpty())) {
                    callCurrencies(currencyListResponse);
                    callCBankRussia(currencyListResponse);
                } else {
                    latch.countDown();
                    latch.countDown();
                    latch.countDown();
                    latch.countDown();
                }
            }

            @Override
            public void onFailure(Call<AppResponse> call, Throwable t) {
                Toast.makeText(UpdateInfoService.this, R.string.inet_problem, Toast.LENGTH_SHORT).show();
                stopThisService();
            }
        });
    }

    public void callDataUkraine() {
        AppResponseInterfaceUkraine interfaceApp = retrofit.create(AppResponseInterfaceUkraine.class);
        Call<AppResponseUkraine> callAppData = interfaceApp.request(ApplicationInfo.getInstance().getUrlType());
        callAppData.enqueue(new Callback<AppResponseUkraine>() {
            @Override
            public void onResponse(Call<AppResponseUkraine> call, Response<AppResponseUkraine> response) {
                AppResponseUkraine appResponse = response.body();

                Map<String, String> nbuResponse = appResponse.getNbuResponse();
                if ((nbuResponse != null) && (!nbuResponse.isEmpty())) {
                    callNbuUkraine(nbuResponse);
                }
                else {
                    latch.countDown();
                }

                Map<String, String> mBankResponse = appResponse.getMBankResponse();
                if ((mBankResponse != null) && (!mBankResponse.isEmpty())) {
                    callMBankUkraine(mBankResponse);
                }
                else {
                    latch.countDown();
                }

                Map<String, String> blackMResponse = appResponse.getBlackMResponse();
                if ((blackMResponse != null) && (!blackMResponse.isEmpty())) {
                    callBlackMUkraine(blackMResponse);
                }
                else {
                    latch.countDown();
                }

                ArrayList<Map<String, String>> bankListResponse = appResponse.getBankListResponse();
                if ((bankListResponse != null) && (!bankListResponse.isEmpty())) {
                    callBanksUkraine(bankListResponse);
                }
                else {
                    latch.countDown();
                }

                ArrayList<Map<String, String>> currencyListResponse = appResponse.getCurrencyListResponse();
                if ((currencyListResponse != null) && (!currencyListResponse.isEmpty())) {
                    callCurrencies(currencyListResponse);
                }
                else {
                    latch.countDown();
                }
            }

            @Override
            public void onFailure(Call<AppResponseUkraine> call, Throwable t) {
                Toast.makeText(UpdateInfoService.this, R.string.inet_problem, Toast.LENGTH_SHORT).show();
                for (int i = 0; i < 5; i++) {
                    latch.countDown();
                }
            }
        });

    }


    //------------------------------------------------------------UK----------------------------------------------------


    public void callBanksUk(ArrayList<Map<String, String>> mapArray) {
        for (Map<String, String> map : mapArray) {
            String name = map.get("name");
            double buyUSD = Double.parseDouble(map.get("buyUSD"));
            double sellUSD = Double.parseDouble(map.get("sellUSD"));
            double buyEUR = Double.parseDouble(map.get("buyEUR"));
            double sellEUR = Double.parseDouble(map.get("sellEUR"));
            double buyRUB = Double.parseDouble(map.get("buyRUB"));
            double sellRUB = Double.parseDouble(map.get("sellRUB"));
            double buyCAD = Double.parseDouble(map.get("buyCAD"));
            double sellCAD = Double.parseDouble(map.get("sellCAD"));
            double buyTYR = Double.parseDouble(map.get("buyTYR"));
            double sellTYR = Double.parseDouble(map.get("sellTYR"));
            double buyPLN = Double.parseDouble(map.get("buyPLN"));
            double sellPLN = Double.parseDouble(map.get("sellPLN"));
            double buyILS = Double.parseDouble(map.get("buyILS"));
            double sellILS = Double.parseDouble(map.get("sellILS"));
            double buyCNY = Double.parseDouble(map.get("buyCNY"));
            double sellCNY = Double.parseDouble(map.get("sellCNY"));
            double buyCZK = Double.parseDouble(map.get("buyCZK"));
            double sellCZK = Double.parseDouble(map.get("sellCZK"));
            double buySEK = Double.parseDouble(map.get("buySEK"));
            double sellSEK = Double.parseDouble(map.get("sellSEK"));
            double buyCHF = Double.parseDouble(map.get("buyCHF"));
            double sellCHF = Double.parseDouble(map.get("sellCHF"));
            double buyJPY = Double.parseDouble(map.get("buyJPY"));
            double sellJPY = Double.parseDouble(map.get("sellJPY"));
            String dateServer = map.get("dateServer");
            dbHelper.setBankInfoUk(db, name, buyUSD, sellUSD, buyEUR, sellEUR, buyRUB, sellRUB, buyCAD, sellCAD, buyTYR, sellTYR, buyPLN, sellPLN, buyILS, sellILS, buyCNY, sellCNY, buyCZK, sellCZK, buySEK, sellSEK, buyCHF, sellCHF, buyJPY, sellJPY, dateServer.substring(0, 16));
        }
        getDataUk(bankList);
    }

    public void callCBankUk(ArrayList<Map<String, String>> mapArray) {
        List<String> arrayNameOfCurrencies = new ArrayList<>(Arrays.asList("USD", "EUR", "RUB", "CAD", "TRY", "PLN", "ILS", "CNY", "CZK", "SEK", "CHF", "JPY"));

        for (Map <String, String> map : mapArray) {
            if (arrayNameOfCurrencies.contains(map.get("name"))) {
                String name = map.get("name");
                double rate = Double.parseDouble(map.get("rate"));
                String date = map.get("date");

                dbHelper.setCbankInfo(db, name, rate, date);
            }
        }
        getDataUk(centralBank);
    }

    public void getDataUk(ArrayList<Bank> bankList) {
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

            int changesBuyUSDIndex = c.getColumnIndex("changesBuyUSD");
            int changesSellUSDIndex = c.getColumnIndex("changesSellUSD");
            int changesBuyEURIndex = c.getColumnIndex("changesBuyEUR");
            int changesSellEURIndex = c.getColumnIndex("changesSellEUR");
            int changesBuyRUBIndex = c.getColumnIndex("changesBuyRUB");
            int changesSellRUBIndex = c.getColumnIndex("changesSellRUB");
            int changesBuyCADIndex = c.getColumnIndex("changesBuyCAD");
            int changesSellCADIndex = c.getColumnIndex("changesSellCAD");
            int changesBuyTYRIndex = c.getColumnIndex("changesBuyTYR");
            int changesSellTYRIndex = c.getColumnIndex("changesSellTYR");
            int changesBuyPLNIndex = c.getColumnIndex("changesBuyPLN");
            int changesSellPLNIndex = c.getColumnIndex("changesSellPLN");
            int changesBuyILSIndex = c.getColumnIndex("changesBuyILS");
            int changesSellILSIndex = c.getColumnIndex("changesSellILS");
            int changesBuyCNYIndex = c.getColumnIndex("changesBuyCNY");
            int changesSellCNYIndex = c.getColumnIndex("changesSellCNY");
            int changesBuyCZKIndex = c.getColumnIndex("changesBuyCZK");
            int changesSellCZKIndex = c.getColumnIndex("changesSellCZK");
            int changesBuySEKIndex = c.getColumnIndex("changesBuySEK");
            int changesSellSEKIndex = c.getColumnIndex("changesSellSEK");
            int changesBuyCHFIndex = c.getColumnIndex("changesBuyCHF");
            int changesSellCHFIndex = c.getColumnIndex("changesSellCHF");
            int changesBuyJPYIndex = c.getColumnIndex("changesBuyJPY");
            int changesSellJPYIndex = c.getColumnIndex("changesSellJPY");

            int dateServerIndex = c.getColumnIndex("dateServer");
            int nameIndex = c.getColumnIndex("name");

            do {
                for (int i = 0; i < bankList.size(); i++) {
                    if (bankList.get(i).getName().toLowerCase().equals(c.getString(nameIndex).toLowerCase())) {
                        Map <Integer, Double> buyRates = new HashMap<>();
                        buyRates.put(Utils.usd, c.getDouble(buyUSDIndex));
                        buyRates.put(Utils.eur, c.getDouble(buyEURIndex));
                        buyRates.put(Utils.rb, c.getDouble(buyRUBIndex));
                        buyRates.put(Utils.cad, c.getDouble(buyCADIndex));
                        buyRates.put(Utils.tyr, c.getDouble(buyTYRIndex));
                        buyRates.put(Utils.pln, c.getDouble(buyPLNIndex));
                        buyRates.put(Utils.ils, c.getDouble(buyILSIndex));
                        buyRates.put(Utils.cny, c.getDouble(buyCNYIndex));
                        buyRates.put(Utils.czk, c.getDouble(buyCZKIndex));
                        buyRates.put(Utils.sek, c.getDouble(buySEKIndex));
                        buyRates.put(Utils.chf, c.getDouble(buyCHFIndex));
                        buyRates.put(Utils.jpy, c.getDouble(buyJPYIndex));

                        Map <Integer, Double> sellRates = new HashMap<>();
                        sellRates.put(Utils.usd, c.getDouble(sellUSDIndex));
                        sellRates.put(Utils.eur, c.getDouble(sellEURIndex));
                        sellRates.put(Utils.rb, c.getDouble(sellRUBIndex));
                        sellRates.put(Utils.cad, c.getDouble(sellCADIndex));
                        sellRates.put(Utils.tyr, c.getDouble(sellTYRIndex));
                        sellRates.put(Utils.pln, c.getDouble(sellPLNIndex));
                        sellRates.put(Utils.ils, c.getDouble(sellILSIndex));
                        sellRates.put(Utils.cny, c.getDouble(sellCNYIndex));
                        sellRates.put(Utils.czk, c.getDouble(sellCZKIndex));
                        sellRates.put(Utils.sek, c.getDouble(sellSEKIndex));
                        sellRates.put(Utils.chf, c.getDouble(sellCHFIndex));
                        sellRates.put(Utils.jpy, c.getDouble(sellJPYIndex));

                        Map <Integer, Double> buyChanges = new HashMap<>();
                        buyChanges.put(Utils.usd, c.getDouble(changesBuyUSDIndex));
                        buyChanges.put(Utils.eur, c.getDouble(changesBuyEURIndex));
                        buyChanges.put(Utils.rb, c.getDouble(changesBuyRUBIndex));
                        buyChanges.put(Utils.cad, c.getDouble(changesBuyCADIndex));
                        buyChanges.put(Utils.tyr, c.getDouble(changesBuyTYRIndex));
                        buyChanges.put(Utils.pln, c.getDouble(changesBuyPLNIndex));
                        buyChanges.put(Utils.ils, c.getDouble(changesBuyILSIndex));
                        buyChanges.put(Utils.cny, c.getDouble(changesBuyCNYIndex));
                        buyChanges.put(Utils.czk, c.getDouble(changesBuyCZKIndex));
                        buyChanges.put(Utils.sek, c.getDouble(changesBuySEKIndex));
                        buyChanges.put(Utils.chf, c.getDouble(changesBuyCHFIndex));
                        buyChanges.put(Utils.jpy, c.getDouble(changesBuyJPYIndex));

                        Map <Integer, Double> sellChanges = new HashMap<>();
                        sellChanges.put(Utils.usd, c.getDouble(changesSellUSDIndex));
                        sellChanges.put(Utils.eur, c.getDouble(changesSellEURIndex));
                        sellChanges.put(Utils.rb, c.getDouble(changesSellRUBIndex));
                        sellChanges.put(Utils.cad, c.getDouble(changesSellCADIndex));
                        sellChanges.put(Utils.tyr, c.getDouble(changesSellTYRIndex));
                        sellChanges.put(Utils.pln, c.getDouble(changesSellPLNIndex));
                        sellChanges.put(Utils.ils, c.getDouble(changesSellILSIndex));
                        sellChanges.put(Utils.cny, c.getDouble(changesSellCNYIndex));
                        sellChanges.put(Utils.czk, c.getDouble(changesSellCZKIndex));
                        sellChanges.put(Utils.sek, c.getDouble(changesSellSEKIndex));
                        sellChanges.put(Utils.chf, c.getDouble(changesSellCHFIndex));
                        sellChanges.put(Utils.jpy, c.getDouble(changesSellJPYIndex));

                        bankList.get(i).setNewInformation(c.getString(dateServerIndex), buyRates, sellRates, buyChanges, sellChanges);
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

    public void getDataUk(CentralBank centralBank) {
        Cursor c = db.query(Utils.getCBankTableName(), null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int rateUSDIndex = c.getColumnIndex("rateUSD");
            int rateEURIndex = c.getColumnIndex("rateEUR");
            int rateRUBIndex = c.getColumnIndex("rateRUB");
            int rateCADIndex = c.getColumnIndex("rateCAD");
            int rateTYRIndex = c.getColumnIndex("rateTYR");
            int ratePLNIndex = c.getColumnIndex("ratePLN");
            int rateILSIndex = c.getColumnIndex("rateILS");
            int rateCNYIndex = c.getColumnIndex("rateCNY");
            int rateCZKIndex = c.getColumnIndex("rateCZK");
            int rateSEKIndex = c.getColumnIndex("rateSEK");
            int rateCHFIndex = c.getColumnIndex("rateCHF");
            int rateJPYIndex = c.getColumnIndex("rateJPY");

            int changesUSDIndex = c.getColumnIndex("changesUSD");
            int changesEURIndex = c.getColumnIndex("changesEUR");
            int changesRUBIndex = c.getColumnIndex("changesRUB");
            int changesCADIndex = c.getColumnIndex("changesCAD");
            int changesTYRIndex = c.getColumnIndex("changesTYR");
            int changesPLNIndex = c.getColumnIndex("changesPLN");
            int changesILSIndex = c.getColumnIndex("changesILS");
            int changesCNYIndex = c.getColumnIndex("changesCNY");
            int changesCZKIndex = c.getColumnIndex("changesCZK");
            int changesSEKIndex = c.getColumnIndex("changesSEK");
            int changesCHFIndex = c.getColumnIndex("changesCHF");
            int changesJPYIndex = c.getColumnIndex("changesJPY");

            int dateServerIndex = c.getColumnIndex("dateServer");

            do {
                Map<Integer, Double> cBankDataMainRates = new HashMap<Integer, Double>();
                cBankDataMainRates.put(Utils.usd, c.getDouble(rateUSDIndex));
                cBankDataMainRates.put(Utils.eur, c.getDouble(rateEURIndex));
                cBankDataMainRates.put(Utils.rb, c.getDouble(rateRUBIndex));
                cBankDataMainRates.put(Utils.cad, c.getDouble(rateCADIndex));
                cBankDataMainRates.put(Utils.tyr, c.getDouble(rateTYRIndex));
                cBankDataMainRates.put(Utils.pln, c.getDouble(ratePLNIndex));
                cBankDataMainRates.put(Utils.ils, c.getDouble(rateILSIndex));
                cBankDataMainRates.put(Utils.cny, c.getDouble(rateCNYIndex));
                cBankDataMainRates.put(Utils.czk, c.getDouble(rateCZKIndex));
                cBankDataMainRates.put(Utils.sek, c.getDouble(rateSEKIndex));
                cBankDataMainRates.put(Utils.chf, c.getDouble(rateCHFIndex));
                cBankDataMainRates.put(Utils.jpy, c.getDouble(rateJPYIndex));

                Map<Integer, Double> cBankDataMainChanges = new HashMap<Integer, Double>();
                cBankDataMainChanges.put(Utils.usd, c.getDouble(changesUSDIndex));
                cBankDataMainChanges.put(Utils.eur, c.getDouble(changesEURIndex));
                cBankDataMainChanges.put(Utils.rb, c.getDouble(changesRUBIndex));
                cBankDataMainChanges.put(Utils.cad, c.getDouble(changesCADIndex));
                cBankDataMainChanges.put(Utils.tyr, c.getDouble(changesTYRIndex));
                cBankDataMainChanges.put(Utils.pln, c.getDouble(changesPLNIndex));
                cBankDataMainChanges.put(Utils.ils, c.getDouble(changesILSIndex));
                cBankDataMainChanges.put(Utils.cny, c.getDouble(changesCNYIndex));
                cBankDataMainChanges.put(Utils.czk, c.getDouble(changesCZKIndex));
                cBankDataMainChanges.put(Utils.sek, c.getDouble(changesSEKIndex));
                cBankDataMainChanges.put(Utils.chf, c.getDouble(changesCHFIndex));
                cBankDataMainChanges.put(Utils.jpy, c.getDouble(changesJPYIndex));

                centralBank.setNewInformation(c.getString(dateServerIndex), cBankDataMainRates, cBankDataMainChanges);
            } while (c.moveToNext());
        }
        else {
            c.close();
        }
        latch.countDown();
        latch.countDown();
        latch.countDown();
    }


    //------------------------------------------------------------POLAND-----------------------------------------------------

    public void callBanksPoland(ArrayList<Map<String, String>> mapArray) {
        for (Map<String, String> map : mapArray) {
            String name = map.get("name");
            double buyUSD = Double.parseDouble(map.get("buyUSD"));
            double sellUSD = Double.parseDouble(map.get("sellUSD"));
            double buyEUR = Double.parseDouble(map.get("buyEUR"));
            double sellEUR = Double.parseDouble(map.get("sellEUR"));
            double buyGBP = Double.parseDouble(map.get("buyGBP"));
            double sellGBP = Double.parseDouble(map.get("sellGBP"));
            double buyCHF = Double.parseDouble(map.get("buyCHF"));
            double sellCHF = Double.parseDouble(map.get("sellCHF"));

            String dateServer = map.get("dateServer");
            dbHelper.setBankInfoPoland(db, name, buyUSD, sellUSD, buyEUR, sellEUR, buyGBP, sellGBP, buyCHF, sellCHF, dateServer.substring(0, 16));
        }
        getDataPoland(bankList);
    }

    public void callCBankPoland(ArrayList<Map<String, String>> mapArray) {
        List<String> arrayNameOfCurrencies = new ArrayList<>(Arrays.asList("USD", "EUR", "GBP", "CHF"));

        for (Map <String, String> map : mapArray) {
            if (arrayNameOfCurrencies.contains(map.get("name"))) {

                String name = map.get("name");
                String country = map.get("country");
                String description = map.get("description");
                double rate = Double.parseDouble(map.get("rate"));
                String date = map.get("date");

                dbHelper.setCbankInfo(db, name, rate, date);
            }
        }
        getDataPoland(centralBank);
    }

    public void getDataPoland(ArrayList<Bank> bankList) {
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

            int changesBuyUSDIndex = c.getColumnIndex("changesBuyUSD");
            int changesSellUSDIndex = c.getColumnIndex("changesSellUSD");
            int changesBuyEURIndex = c.getColumnIndex("changesBuyEUR");
            int changesSellEURIndex = c.getColumnIndex("changesSellEUR");
            int changesBuyGBPIndex = c.getColumnIndex("changesBuyGBP");
            int changesSellGBPIndex = c.getColumnIndex("changesSellGBP");
            int changesBuyCHFIndex = c.getColumnIndex("changesBuyCHF");
            int changesSellCHFIndex = c.getColumnIndex("changesSellCHF");

            int dateServerIndex = c.getColumnIndex("dateServer");
            int nameIndex = c.getColumnIndex("name");

            do {
                for (int i = 0; i < bankList.size(); i++) {
                    if (bankList.get(i).getName().toLowerCase().equals(c.getString(nameIndex).toLowerCase())) {
                        Map <Integer, Double> buyRates = new HashMap<>();
                        buyRates.put(Utils.usd, c.getDouble(buyUSDIndex));
                        buyRates.put(Utils.eur, c.getDouble(buyEURIndex));
                        buyRates.put(Utils.gbp, c.getDouble(buyGBPIndex));
                        buyRates.put(Utils.chf, c.getDouble(buyCHFIndex));

                        Map <Integer, Double> sellRates = new HashMap<>();
                        sellRates.put(Utils.usd, c.getDouble(sellUSDIndex));
                        sellRates.put(Utils.eur, c.getDouble(sellEURIndex));
                        sellRates.put(Utils.gbp, c.getDouble(sellGBPIndex));
                        sellRates.put(Utils.chf, c.getDouble(sellCHFIndex));

                        Map <Integer, Double> buyChanges = new HashMap<>();
                        buyChanges.put(Utils.usd, c.getDouble(changesBuyUSDIndex));
                        buyChanges.put(Utils.eur, c.getDouble(changesBuyEURIndex));
                        buyChanges.put(Utils.gbp, c.getDouble(changesBuyGBPIndex));
                        buyChanges.put(Utils.chf, c.getDouble(changesBuyCHFIndex));

                        Map <Integer, Double> sellChanges = new HashMap<>();
                        sellChanges.put(Utils.usd, c.getDouble(changesSellUSDIndex));
                        sellChanges.put(Utils.eur, c.getDouble(changesSellEURIndex));
                        sellChanges.put(Utils.gbp, c.getDouble(changesSellGBPIndex));
                        sellChanges.put(Utils.chf, c.getDouble(changesSellCHFIndex));

                        bankList.get(i).setNewInformation(c.getString(dateServerIndex), buyRates, sellRates, buyChanges, sellChanges);
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

    public void getDataPoland(CentralBank centralBank) {
        Cursor c = db.query(Utils.getCBankTableName(), null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int rateUSDIndex = c.getColumnIndex("rateUSD");
            int rateEURIndex = c.getColumnIndex("rateEUR");
            int rateGBPIndex = c.getColumnIndex("rateGBP");
            int rateCHFIndex = c.getColumnIndex("rateCHF");

            int changesUSDIndex = c.getColumnIndex("changesUSD");
            int changesEURIndex = c.getColumnIndex("changesEUR");
            int changesGBPIndex = c.getColumnIndex("changesGBP");
            int changesCHFIndex = c.getColumnIndex("changesCHF");

            int dateServerIndex = c.getColumnIndex("dateServer");

            do {
                Map<Integer, Double> cBankDataMainRates = new HashMap<Integer, Double>();
                cBankDataMainRates.put(Utils.usd, c.getDouble(rateUSDIndex));
                cBankDataMainRates.put(Utils.eur, c.getDouble(rateEURIndex));
                cBankDataMainRates.put(Utils.gbp, c.getDouble(rateGBPIndex));
                cBankDataMainRates.put(Utils.chf, c.getDouble(rateCHFIndex));

                Map<Integer, Double> cBankDataMainChanges = new HashMap<Integer, Double>();
                cBankDataMainChanges.put(Utils.usd, c.getDouble(changesUSDIndex));
                cBankDataMainChanges.put(Utils.eur, c.getDouble(changesEURIndex));
                cBankDataMainChanges.put(Utils.gbp, c.getDouble(changesGBPIndex));
                cBankDataMainChanges.put(Utils.chf, c.getDouble(changesCHFIndex));

                centralBank.setNewInformation(c.getString(dateServerIndex), cBankDataMainRates, cBankDataMainChanges);
            } while (c.moveToNext());
        }
        else {
            c.close();
        }
        latch.countDown();
        latch.countDown();
        latch.countDown();
    }

    //------------------------------------------------------------RUSSIA-----------------------------------------------------


    public void callBanksRussia(ArrayList<Map<String, String>> mapArray) {
        for (Map<String, String> map : mapArray) {
            String name = map.get("name");
            double buyUSD = Double.parseDouble(map.get("buyUSD"));
            double sellUSD = Double.parseDouble(map.get("sellUSD"));
            double buyEUR = Double.parseDouble(map.get("buyEUR"));
            double sellEUR = Double.parseDouble(map.get("sellEUR"));


            String dateServer = map.get("dateServer");
            dbHelper.setBankInfoRussia(db, name, buyUSD, sellUSD, buyEUR, sellEUR, dateServer.substring(0, 16));
        }
        getDataRussia(bankList);
    }

    public void callCBankRussia(ArrayList<Map<String, String>> mapArray) {
        List<String> arrayNameOfCurrencies = new ArrayList<>(Arrays.asList("USD", "EUR"));

        for (Map <String, String> map : mapArray) {
            if (arrayNameOfCurrencies.contains(map.get("name"))) {

                String name = map.get("name");
                double rate = Double.parseDouble(map.get("rate"));
                String date = map.get("date");

                dbHelper.setCbankInfo(db, name, rate, date);
            }
        }
        getDataRussia(centralBank);
    }

    public void getDataRussia(ArrayList<Bank> bankList) {
        Cursor c = db.query(Utils.getBanksTableName(), null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int buyUSDIndex = c.getColumnIndex("buyUSD");
            int sellUSDIndex = c.getColumnIndex("sellUSD");
            int buyEURIndex = c.getColumnIndex("buyEUR");
            int sellEURIndex = c.getColumnIndex("sellEUR");

            int changesBuyUSDIndex = c.getColumnIndex("changesBuyUSD");
            int changesSellUSDIndex = c.getColumnIndex("changesSellUSD");
            int changesBuyEURIndex = c.getColumnIndex("changesBuyEUR");
            int changesSellEURIndex = c.getColumnIndex("changesSellEUR");

            int dateServerIndex = c.getColumnIndex("dateServer");
            int nameIndex = c.getColumnIndex("name");

            do {
                for (int i = 0; i < bankList.size(); i++) {
                    if (bankList.get(i).getName().toLowerCase().equals(c.getString(nameIndex).toLowerCase())) {
                        Map <Integer, Double> buyRates = new HashMap<>();
                        buyRates.put(Utils.usd, c.getDouble(buyUSDIndex));
                        buyRates.put(Utils.eur, c.getDouble(buyEURIndex));

                        Map <Integer, Double> sellRates = new HashMap<>();
                        sellRates.put(Utils.usd, c.getDouble(sellUSDIndex));
                        sellRates.put(Utils.eur, c.getDouble(sellEURIndex));

                        Map <Integer, Double> buyChanges = new HashMap<>();
                        buyChanges.put(Utils.usd, c.getDouble(changesBuyUSDIndex));
                        buyChanges.put(Utils.eur, c.getDouble(changesBuyEURIndex));

                        Map <Integer, Double> sellChanges = new HashMap<>();
                        sellChanges.put(Utils.usd, c.getDouble(changesSellUSDIndex));
                        sellChanges.put(Utils.eur, c.getDouble(changesSellEURIndex));

                        bankList.get(i).setNewInformation(c.getString(dateServerIndex), buyRates, sellRates, buyChanges, sellChanges);
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

    public void getDataRussia(CentralBank centralBank) {
        Cursor c = db.query(Utils.getCBankTableName(), null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int rateUSDIndex = c.getColumnIndex("rateUSD");
            int rateEURIndex = c.getColumnIndex("rateEUR");

            int changesUSDIndex = c.getColumnIndex("changesUSD");
            int changesEURIndex = c.getColumnIndex("changesEUR");

            int dateServerIndex = c.getColumnIndex("dateServer");

            do {
                Map<Integer, Double> cBankDataMainRates = new HashMap<Integer, Double>();
                cBankDataMainRates.put(Utils.usd, c.getDouble(rateUSDIndex));
                cBankDataMainRates.put(Utils.eur, c.getDouble(rateEURIndex));

                Map<Integer, Double> cBankDataMainChanges = new HashMap<Integer, Double>();
                cBankDataMainChanges.put(Utils.usd, c.getDouble(changesUSDIndex));
                cBankDataMainChanges.put(Utils.eur, c.getDouble(changesEURIndex));

                centralBank.setNewInformation(c.getString(dateServerIndex), cBankDataMainRates, cBankDataMainChanges);
            } while (c.moveToNext());
        }
        else {
            c.close();
        }
        latch.countDown();
        latch.countDown();
        latch.countDown();
    }


    //------------------------------------------------------------UKRAINE----------------------------------------------------

    public void callNbuUkraine(Map<String, String> map) {
        double rateD = Double.parseDouble(map.get("rateD"));
        double rateE = Double.parseDouble(map.get("rateE"));
        double rateR = Double.parseDouble(map.get("rateR"));
        String dateServer = map.get("dateServer");
        dbHelper.setNbuInfo(db, rateD, rateE, rateR, dateServer.substring(0, 16));
        getDataUkraine(centralBank);
    }

    public void callMBankUkraine(Map<String, String> map) {
        int idTime = 10;
        if (!map.get("idTime").isEmpty()) {
            idTime = Integer.parseInt(map.get("idTime"));
        }
        double buyD = Double.parseDouble(map.get("buyD"));
        double sellD = Double.parseDouble(map.get("sellD"));
        double buyE = Double.parseDouble(map.get("buyE"));
        double sellE = Double.parseDouble(map.get("sellE"));
        double buyR = Double.parseDouble(map.get("buyR"));
        double sellR = Double.parseDouble(map.get("sellR"));
        String dateServer = map.get("dateServer");
        dbHelper.setMBInfo(db, idTime, buyD, sellD, buyE, sellE, buyR, sellR, dateServer.substring(0, 16));
        getDataUkraine(mBank);
    }

    public void callBlackMUkraine(Map<String, String> map) {
        double buyD = Double.parseDouble(map.get("buyD"));
        double sellD = Double.parseDouble(map.get("sellD"));
        double buyE = Double.parseDouble(map.get("buyE"));
        double sellE = Double.parseDouble(map.get("sellE"));
        double buyR = Double.parseDouble(map.get("buyR"));
        double sellR = Double.parseDouble(map.get("sellR"));
        String dateServer = map.get("dateServer");
        dbHelper.setBlackMInfo(db, buyD, sellD, buyE, sellE, buyR, sellR, dateServer.substring(0, 16));
        getDataUkraine(blackMarket);
    }

    public void callBanksUkraine(ArrayList<Map<String, String>> mapArray) {
        for (Map<String, String> map : mapArray) {
            String name = map.get("name");
            double buyD = Double.parseDouble(map.get("buyUSD"));
            double sellD = Double.parseDouble(map.get("sellUSD"));
            double buyE = Double.parseDouble(map.get("buyEUR"));
            double sellE = Double.parseDouble(map.get("sellEUR"));
            double buyR = Double.parseDouble(map.get("buyRUB"));
            double sellR = Double.parseDouble(map.get("sellRUB"));
            String dateServer = map.get("dateServer");
            dbHelper.setBankInfoUkraine(db, name, buyD, sellD, buyE, sellE, buyR, sellR, dateServer.substring(0, 16));
        }
        getDataUkraine(bankList);
    }

    public void getDataUkraine(ArrayList<Bank> bankList) {
        Cursor c = db.query(BANKS, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int nameIndex = c.getColumnIndex("name");
            int buyDIndex = c.getColumnIndex("buyUSD");
            int sellDIndex = c.getColumnIndex("sellUSD");
            int buyEIndex = c.getColumnIndex("buyEUR");
            int sellEIndex = c.getColumnIndex("sellEUR");
            int buyRIndex = c.getColumnIndex("buyRUB");
            int sellRIndex = c.getColumnIndex("sellRUB");
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
                        bankList.get(i).setNewInformationUkraine(c.getString(dateServerIndex), c.getDouble(buyDIndex), c.getDouble(sellDIndex), c.getDouble(buyEIndex), c.getDouble(sellEIndex), c.getDouble(buyRIndex), c.getDouble(sellRIndex), c.getDouble(changesBuyDIndex), c.getDouble(changesSellDIndex), c.getDouble(changesBuyEIndex), c.getDouble(changesSellEIndex), c.getDouble(changesBuyRIndex), c.getDouble(changesSellRIndex));
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

    public void getDataUkraine(CentralBank centralBank) {
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
                centralBank.setNewInformationUkraine(c.getString(dateServerIndex), c.getDouble(rateDIndex), c.getDouble(rateEIndex), c.getDouble(rateRIndex), c.getDouble(changesDIndex), c.getDouble(changesEIndex), c.getDouble(changesRIndex));
            } while (c.moveToNext());
        }
        else {
            c.close();
        }
        latch.countDown();
    }

    public void getDataUkraine(MBank mBank) {
        Cursor c = db.query(MB, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int idTime = c.getColumnIndex("idTime");
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
                mBank.setNewInformation(c.getString(dateServerIndex), c.getDouble(buyDIndex), c.getDouble(sellDIndex), c.getDouble(buyEIndex), c.getDouble(sellEIndex), c.getDouble(buyRIndex), c.getDouble(sellRIndex), c.getDouble(changesBuyDIndex), c.getDouble(changesSellDIndex), c.getDouble(changesBuyEIndex), c.getDouble(changesSellEIndex), c.getDouble(changesBuyRIndex), c.getDouble(changesSellRIndex));
            } while (c.moveToNext());
        }
        else {
            c.close();
        }
        latch.countDown();
    }

    public void getDataUkraine(BlackMarket blackMarket) {
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



    //------------------------------------------------------------For at least few countries-------------------------------------

    public void callCurrencies(ArrayList<Map<String, String>> mapArray) {
        for (Map<String, String> map : mapArray) {
            String name = map.get("name");
            String country = map.get("country");
            String description = map.get("description");
            double rate = Double.parseDouble(map.get("rate"));
            double rateSell = 0;
            if (map.containsKey("rateSell")) {
                rateSell = Double.parseDouble(map.get("rateSell"));
            }

            String date = map.get("date");
            dbHelper.setCurrenciesInfo(db, name, country, description, rate, rateSell, date);
        }
        getDataOfficialCurrencies(country.getCBank().getCurrencyList());
    }

    public void getDataOfficialCurrencies(ArrayList<Currency> currencyList) {
        Cursor c = db.query(Utils.getOfficialCurrencyTableName(), null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int nameIndex = c.getColumnIndex("name");
            int countryIndex = c.getColumnIndex("country");
            int descriptionIndex = c.getColumnIndex("description");
            int rateIndex = c.getColumnIndex("rate");
            int rateSellIndex = c.getColumnIndex("rateSell");
            int dateIndex = c.getColumnIndex("date");

            do {
                for (int i = 0; i < currencyList.size(); i++) {
                    if (currencyList.get(i).getName().equals(c.getString(nameIndex))) {
                        String dateFromDB = c.getString(dateIndex);
                        Log.d("UpdateService=", dateFromDB);
                        Log.d("UpdateService=", "length = " + dateFromDB.length());
                        Log.d("UpdateService=", dateFromDB.substring(0, 10));
                        if (dateFromDB.length() > 10) {
                            dateFromDB = dateFromDB.substring(0, 10);
                        }
                        currencyList.get(i).setNewInformation(c.getDouble(rateIndex), dateFromDB);
                        break;
                    }
                }
            } while (c.moveToNext());
        }
        else {
            c.close();
        }
        if ((Utils.country_code == Utils.USA_CODE) || (Utils.country_code == Utils.EUROPE_CODE) || (Utils.country_code == Utils.TURKEY_CODE)) {
            stopThisService();
        } else {
            latch.countDown();
        }
    }

    class UpdateRun implements Runnable  {
        @Override
        public void run() {
            latch = new CountDownLatch(5);
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
            switch (Utils.country_code) {
                case Utils.USA_CODE:
                    callDataUsa();
                    break;
                case Utils.EUROPE_CODE:
                    callDataEurope();
                    break;
                case Utils.UK_CODE:
                    callDataUk();
                    break;
                case Utils.POLAND_CODE:
                    callDataPoland();
                    break;
                case Utils.TURKEY_CODE:
                    callDataTurkey();
                    break;
                case Utils.RUSSIA_CODE:
                    callDataRussia();
                    break;
                case Utils.UKRAINE_CODE:
                    callDataUkraine();
                    break;
                default:
                    callDataUk();
                    break;
            }
        }
    }

    public void stopThisService() {
        for (int i = 0; i < 5; i++) {
            latch.countDown();
        }
    }
}
