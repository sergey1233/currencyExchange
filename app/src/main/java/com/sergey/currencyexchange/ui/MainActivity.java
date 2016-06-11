package com.sergey.currencyexchange.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.AlertDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.ApplicationInfo;
import com.sergey.currencyexchange.model.Bank;
import com.sergey.currencyexchange.model.BlackMarket;
import com.sergey.currencyexchange.model.DBHelper;
import com.sergey.currencyexchange.model.DBInterface;
import com.sergey.currencyexchange.model.MBank;
import com.sergey.currencyexchange.model.Nbu;
import com.sergey.currencyexchange.model.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private static boolean EXIT_FROM_ALL_ACTIVITIES = false;
    private final static int TYPEACTIVITY = 0;
    private final String TABLE_NAME_NBU = "nbu";
    private final String TABLE_NAME_MB = "mb";
    private final String TABLE_NAME_BLACKM = "blackM";
    private final String TABLE_NAME_BANKS = "banks";

    private ImageButton mainToolBarImage;
    private ImageButton converterToolBarImage;
    private ImageButton iconCurrencyToolbar;
    private ImageButton loadToolBarImage;
    private TextView nbuRate;
    private TextView nbuChangesText;
    private TextView nbuDate;
    private ImageView nbuChangesImgUp;
    private ImageView nbuChangesImgDown;
    private TextView mbBuyRate;
    private TextView mbSellRate;
    private TextView mbDate;
    private TextView mbBuyChangesText;
    private TextView mbSellChangesText;
    private ImageView mbBuyChangesImgUp;
    private ImageView mbBuyChangesImgDown;
    private ImageView mbSellChangesImgUp;
    private ImageView mbSellChangesImgDown;
    private TextView blackMBuyRate;
    private TextView blackMSellRate;
    private TextView blackMDate;
    private TextView blackMBuyChangesText;
    private TextView blackMSellChangesText;
    private ImageView blackMBuyChangesImgUp;
    private ImageView blackMBuyChangesImgDown;
    private ImageView blackMSellChangesImgUp;
    private ImageView blackMSellChangesImgDown;

    private ApplicationInfo app;
    private Nbu nbu;
    private MBank mBank;
    private BlackMarket blackMarket;
    private ArrayList<Bank> bankList = new ArrayList<>();

    private RecyclerView recyclerViewBanks;
    private BankListAdapter adapter;
    private static int currencyId = 0;//0 - usd; 1 - eur; 2 - rub;

    private static final String URL = "http://currencyexchange.zzz.com.ua/";
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(URL)
            .build();
    private DBInterface intf = retrofit.create(DBInterface.class);
    private DBHelper dbHelper;
    private static final int DBVERSION = 1;
    private SQLiteDatabase db;
    private int typeAct;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolBarImage = (ImageButton)findViewById(R.id.icon_main_toolbar);
        converterToolBarImage = (ImageButton)findViewById(R.id.icon_converter_toolbar);
        iconCurrencyToolbar = (ImageButton)findViewById(R.id.icon_currency_toolbar);
        loadToolBarImage = (ImageButton)findViewById(R.id.icon_load_toolbar);
        nbuRate = (TextView)findViewById(R.id.nbu_rate);
        nbuChangesText = (TextView)findViewById(R.id.nbu_changes_text);
        nbuDate = (TextView)findViewById(R.id.nbu_date);
        nbuChangesImgUp = (ImageView)findViewById(R.id.nbu_changes_img_up);
        nbuChangesImgDown = (ImageView)findViewById(R.id.nbu_changes_img_down);
        mbBuyRate = (TextView)findViewById(R.id.mb_buy_rate);
        mbSellRate = (TextView)findViewById(R.id.mb_sell_rate);
        mbDate = (TextView)findViewById(R.id.mb_date);
        mbBuyChangesText = (TextView)findViewById(R.id.mb_buy_changes_text);
        mbSellChangesText = (TextView)findViewById(R.id.mb_sell_changes_text);
        mbBuyChangesImgUp = (ImageView)findViewById(R.id.mb_buy_changes_img_up);
        mbBuyChangesImgDown = (ImageView)findViewById(R.id.mb_buy_changes_img_down);
        mbSellChangesImgUp = (ImageView)findViewById(R.id.mb_sell_changes_img_up);
        mbSellChangesImgDown = (ImageView)findViewById(R.id.mb_sell_changes_img_down);
        blackMBuyRate = (TextView)findViewById(R.id.blackM_buy_rate);
        blackMSellRate = (TextView)findViewById(R.id.blackM_sell_rate);
        blackMDate = (TextView)findViewById(R.id.blackM_date);
        blackMBuyChangesText = (TextView)findViewById(R.id.blackM_buy_changes_text);
        blackMSellChangesText = (TextView)findViewById(R.id.blackM_sell_changes_text);
        blackMBuyChangesImgUp = (ImageView)findViewById(R.id.blackM_buy_changes_img_up);
        blackMBuyChangesImgDown = (ImageView)findViewById(R.id.blackM_buy_changes_img_down);
        blackMSellChangesImgUp = (ImageView)findViewById(R.id.blackM_sell_changes_img_up);
        blackMSellChangesImgDown = (ImageView)findViewById(R.id.blackM_sell_changes_img_down);
        recyclerViewBanks = (RecyclerView)findViewById(R.id.recycler_view_banks_convert);
        recyclerViewBanks.setNestedScrollingEnabled(false);
        recyclerViewBanks.setFocusable(false);
        adapter = new BankListAdapter(bankList, currencyId, this);
        recyclerViewBanks.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerViewBanks.setLayoutManager(layoutManager);
        recyclerViewBanks.setItemAnimator(itemAnimator);
        dialog = new SpotsDialog(this, R.style.Custom);

        mainToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
        typeAct = getIntent().getIntExtra("fromActivity", 0);
        dbHelper = new DBHelper(MainActivity.this, DBVERSION);

        app = ApplicationInfo.getInstance();
        currencyId = app.getCurrencyId();
        nbu = app.getNbu();
        mBank = app.getMBank();
        blackMarket = app.getBlackMarket();
        bankList = app.getBankList();

        setToolbarIcon();
        setViews();

        if (bankList.size() == 0) {
            Bank PrivatB = new Bank("bank_icon_privat", getString(R.string.privat));
            Bank OshadB = new Bank("bank_icon_oshad", getString(R.string.oshad));
            Bank SberB = new Bank("bank_icon_sber", getString(R.string.sber_bank));
            Bank RaiphB = new Bank("bank_icon_raif", getString(R.string.raif));
            Bank UkrsotsB = new Bank("bank_icon_ukrsots", getString(R.string.ukrsots));
            Bank AlphaB = new Bank("bank_icon_alpha_bank", getString(R.string.alpha_bank));
            Bank UkrSibB = new Bank("bank_icon_ukrsib", getString(R.string.ukrsib));
            Bank PumbB = new Bank("bank_icon_pumb",  getString(R.string.pumb));
            Bank VtbB = new Bank("bank_icon_vtb", getString(R.string.vtb));
            Bank OtpB = new Bank("bank_icon_otp", getString(R.string.otp));
            Bank CrediAgriB = new Bank("bank_icon_crediagr", getString(R.string.crediagr));
            bankList.addAll(Arrays.asList(PrivatB, OshadB, SberB, RaiphB, UkrsotsB, AlphaB, UkrSibB, PumbB, VtbB, OtpB, CrediAgriB));
        }

        if (typeAct != 1 && typeAct != 2) {
            db = dbHelper.getWritableDatabase();
            requestInfo();
        }

        converterToolBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Converter.class);
                intent.putExtra("fromActivity", TYPEACTIVITY);
                startActivity(intent);
            }
        });
        iconCurrencyToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectCurrency.class);
                intent.putExtra("fromActivity", TYPEACTIVITY);
                startActivity(intent);
            }
        });
        loadToolBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = dbHelper.getWritableDatabase();
                requestInfo();
            }
        });
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            EXIT_FROM_ALL_ACTIVITIES = true;
            finish(); // finish activity
        } else {
            Toast.makeText(this, R.string.back, Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (EXIT_FROM_ALL_ACTIVITIES) {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        dbHelper.close();
    }

    public void setViews() {
        setNbuView(nbu);
        setMBankView(mBank);
        setBLackMarketView(blackMarket);
        setBanklistView(bankList);
    }

    public void requestInfo() {
        dialog.show();
        callNbu();
        callMBank();
        callBlackM();
        callBanks();
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
        Cursor c = db.query(TABLE_NAME_NBU, null, null, null, null, null, null);
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
        setNbuView(nbu);
    }

    public void getData(MBank mBank) {
        ArrayList<String> dateArray = new ArrayList<>();
        ArrayList<Double> buyDArray = new ArrayList<>();
        ArrayList<Double> sellDArray = new ArrayList<>();
        ArrayList<Double> buyEArray = new ArrayList<>();
        ArrayList<Double> sellEArray = new ArrayList<>();
        ArrayList<Double> buyRArray = new ArrayList<>();
        ArrayList<Double> sellRArray = new ArrayList<>();

        Cursor c = db.query(TABLE_NAME_MB, null, null, null, null, null, null);
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
        setMBankView(mBank);
    }

    public void getData(BlackMarket blackMarket) {
        Cursor c = db.query(TABLE_NAME_BLACKM, null, null, null, null, null, null);
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
        setBLackMarketView(blackMarket);
    }

    public void getData(ArrayList<Bank> bankList) {
        Cursor c = db.query(TABLE_NAME_BANKS, null, null, null, null, null, null);
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
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 2500);
        setBanklistView(bankList);
    }

    public void setNbuView(Nbu nbu) {
        nbuRate.setText(String.valueOf(String.format("%.4f",nbu.getRate(currencyId))));
        nbuDate.setText(nbu.getDate());
        setChangesInfo(nbu);
    }

    public void setMBankView(MBank mBank) {
        mbBuyRate.setText(String.format("%.4f",mBank.getBuy(currencyId)));
        mbSellRate.setText(String.format("%.4f",mBank.getSell(currencyId)));
        mbDate.setText(mBank.getDate());
        setChangesInfo(mBank);
    }

    public void setBLackMarketView(BlackMarket blackMarket) {
        blackMBuyRate.setText(String.format("%.4f",blackMarket.getBuy(currencyId)));
        blackMSellRate.setText(String.format("%.4f",blackMarket.getSell(currencyId)));
        blackMDate.setText(blackMarket.getDate());
        setChangesInfo(blackMarket);
    }

    public void setBanklistView(ArrayList<Bank> bankList) {
        refreshAdapter(bankList);
    }

    public void setToolbarIcon() {
        switch (currencyId)
        {
            case 0:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_dollar_dark);
                break;
            case 1:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_euro_dark);
                break;
            case 2:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_rb_dark);
                break;
        }
    }


    public void refreshAdapter(ArrayList<Bank> bankList) {
        adapter.addItemstoList(bankList);
        adapter.addCurrencyId(currencyId);
        adapter.notifyDataSetChanged();
    }

    public void setChangesInfo(Nbu nbu) {
        if (nbu.getChanges(currencyId) != 0)
        {
            if (nbu.getChanges(currencyId) < 0)
            {
                nbuChangesText.setText(String.format("%.4f", nbu.getChanges(currencyId)));
                nbuChangesImgUp.setVisibility(View.INVISIBLE);
                nbuChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                nbuChangesText.setText("+" + String.format("%.4f", nbu.getChanges(currencyId)));
                nbuChangesImgUp.setVisibility(View.VISIBLE);
                nbuChangesImgDown.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setChangesInfo(MBank mBank) {
        if (mBank.getChangesBuy(currencyId) != 0)
        {
            if (mBank.getChangesBuy(currencyId) < 0)
            {
                mbBuyChangesText.setText(String.format("%.4f", mBank.getChangesBuy(currencyId)));
                mbBuyChangesImgUp.setVisibility(View.INVISIBLE);
                mbBuyChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                mbBuyChangesText.setText("+" + String.format("%.4f", mBank.getChangesBuy(currencyId)));
                mbBuyChangesImgUp.setVisibility(View.VISIBLE);
                mbBuyChangesImgDown.setVisibility(View.INVISIBLE);
            }
        }

        if (mBank.getChangesSell(currencyId) != 0)
        {
            if (mBank.getChangesSell(currencyId) < 0)
            {
                mbSellChangesText.setText(String.format("%.4f", mBank.getChangesSell(currencyId)));
                mbSellChangesImgUp.setVisibility(View.INVISIBLE);
                mbSellChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                mbSellChangesText.setText("+" + String.format("%.4f", mBank.getChangesSell(currencyId)));
                mbSellChangesImgUp.setVisibility(View.VISIBLE);
                mbSellChangesImgDown.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setChangesInfo(BlackMarket blackMarket) {
        if (blackMarket.getChangesBuy(currencyId) != 0)
        {
            if (blackMarket.getChangesBuy(currencyId) < 0)
            {
                blackMBuyChangesText.setText(String.format("%.4f", blackMarket.getChangesBuy(currencyId)));
                blackMBuyChangesImgUp.setVisibility(View.INVISIBLE);
                blackMBuyChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                blackMBuyChangesText.setText("+" + String.format("%.4f", blackMarket.getChangesBuy(currencyId)));
                blackMBuyChangesImgUp.setVisibility(View.VISIBLE);
                blackMBuyChangesImgDown.setVisibility(View.INVISIBLE);
            }
        }

        if (blackMarket.getChangesSell(currencyId) != 0)
        {
            if (blackMarket.getChangesSell(currencyId) < 0)
            {
                blackMSellChangesText.setText(String.format("%.4f", blackMarket.getChangesSell(currencyId)));
                blackMSellChangesImgUp.setVisibility(View.INVISIBLE);
                blackMSellChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                blackMSellChangesText.setText("+" + String.format("%.4f", blackMarket.getChangesSell(currencyId)));
                blackMSellChangesImgUp.setVisibility(View.VISIBLE);
                blackMSellChangesImgDown.setVisibility(View.INVISIBLE);
            }
        }
    }
}
