package com.sergey.currencyexchange.ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rey.material.widget.ProgressView;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.ApplicationInfo;
import com.sergey.currencyexchange.model.Bank;
import com.sergey.currencyexchange.model.BlackMarket;
import com.sergey.currencyexchange.model.CentralBank;
import com.sergey.currencyexchange.model.Country;
import com.sergey.currencyexchange.model.Currency;
import com.sergey.currencyexchange.model.DBHelper;
import com.sergey.currencyexchange.model.MBank;
import com.sergey.currencyexchange.model.Utils;
import com.sergey.currencyexchange.services.UpdateInfoService;
import com.sergey.currencyexchange.ui.fragment.SelectCountryFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private AdView mAdView;

    private final static String TAG = "MainActivity = ";
    private static boolean EXIT_FROM_ALL_ACTIVITIES = false;
    private final static int TYPEACTIVITY = Utils.MAINACTIVITYTYPE;
    private SharedPreferences preferences;
    private static final String SAVED_COUNTRY = "SAVED_COUNTRY";


    //UKRAINE VARIABLES
    private final String NBU = Utils.NBU_DB_NAME;
    private final String MB = Utils.MB_DB_NAME;
    private final String BLACKM = Utils.BLACKM_DB_NAME;
    private final String BANKS = Utils.BANKS_DB_NAME;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private static final int DBVERSION = Utils.DBVERSION;

    private ImageButton mainToolBarImage;
    private ImageButton nbuCurrencyToolbarImage;
    private ImageButton converterToolBarImage;
    private ImageButton iconCurrencyToolbar;
    private ImageButton iconMenu;

    private TextView nbuText;
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
    private TextView currencyExchangePoint;

    private ApplicationInfo app;
    private Country country;
    private CentralBank nbu;
    private MBank mBank;
    private BlackMarket blackMarket;
    private ArrayList<Bank> bankList;

    private RecyclerView recyclerViewBanks;
    private BankListAdapter adapter;


    private int typeAct;
    private PullRefreshLayout refreshLayout;
    private BroadcastReceiver broadcastReceiver;
    private Intent serviceIntent;
    private IntentFilter intFilt;
    public final static String BROADCAST_ACTION = "com.sergey.currencyexchange.servicebackbroadcast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //what activity was privious
        typeAct = getIntent().getIntExtra("fromActivity", 0);

        //the main unique data of the app
        app = ApplicationInfo.getInstance();

        if (typeAct == 0 || app.getCountries().size() < 7) {//first open
            app.removeAllCountries();
            app.setCountries(new Country("currency_flag_usa", getString(R.string.usa), 1));
            app.setCountries(new Country("currency_flag_europe", getString(R.string.europe), 2));
            app.setCountries(new Country("currency_flag_great_britain", getString(R.string.greatbritain), 3));
            app.setCountries(new Country("currency_flag_poland", getString(R.string.poland), 4));
            app.setCountries(new Country("currency_flag_turkey", getString(R.string.turkey), 5));
            app.setCountries(new Country("currency_flag_russia", getString(R.string.russia), 6));
            app.setCountries(new Country("currency_flag_ukraine", getString(R.string.ukraine), 7));
        }


        //get from which country is user
        if (Utils.country_code == 0) {
            getCountryCodeFirstOpen();
        }

        //setContentView and start view depending of country_code
        startCountryView();
    }

    public void getCountryCodeFirstOpen() {
        preferences = getPreferences(MODE_PRIVATE);

        Utils.country_code = preferences.getInt(SAVED_COUNTRY, 0);
        if (Utils.country_code == 0) {
            String countryISOCode = "";
            TelephonyManager teleMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            if (teleMgr != null) {
                countryISOCode = teleMgr.getSimCountryIso();
                String[] euroZone = {"at", "be", "de", "gr", "ie", "es", "it", "cy", "lv", "lt", "lu", "mt", "nl", "pt", "sk", "si", "fi", "fr", "ee", "om", "ch"};
                String[] russiaZone = {"ru", "kz", "kk", "am", "az", "by", "tm", "uz", "ge"};

                for (String code : euroZone) {
                    if ((countryISOCode.toLowerCase()).equals(code.toLowerCase())) {
                        countryISOCode = "eu";
                        break;
                    }
                }

                for (String code : russiaZone) {
                    if ((countryISOCode.toLowerCase()).equals(code.toLowerCase())) {
                        countryISOCode = "ru";
                        break;
                    }
                }

                switch (countryISOCode.toLowerCase()) {
                    case "us":
                        Utils.country_code = Utils.USA_CODE;
                        break;
                    case "eu":
                        Utils.country_code = Utils.EUROPE_CODE;
                        break;
                    case "uk":
                        Utils.country_code = Utils.UK_CODE;
                        break;
                    case "pl":
                        Utils.country_code = Utils.POLAND_CODE;
                        break;
                    case "ru":
                        Utils.country_code = Utils.RUSSIA_CODE;
                        break;
                    case "tr":
                        Utils.country_code = Utils.TURKEY_CODE;
                        break;
                    case "ua":
                        Utils.country_code = Utils.UKRAINE_CODE;
                        break;
                    default:
                        Utils.country_code = Utils.UK_CODE;
                        break;
                }
            }
            else {
                Utils.country_code = Utils.UK_CODE;
            }
        }
    }

    public void startCountryView() {
        switch (Utils.country_code) {
            case Utils.USA_CODE:
                setUsaCountry();
                break;
            case Utils.UK_CODE:
                setUKCountry();
                break;
            case Utils.EUROPE_CODE:
                setEuropeCountry();
                break;
            case Utils.POLAND_CODE:
                setPolandCountry();
                break;
            case Utils.TURKEY_CODE:
                setTurkeyCountry();
                break;
            case Utils.RUSSIA_CODE:
                setRussiaCountry();
                break;
            case Utils.UKRAINE_CODE:
                setUkraineCountry();
                break;
            default:
                setUKCountry();
                break;
        }
    }

    //---------------------------------------------------------------USA---------------------------------------------------------------
    //USA VARIABLES
    private TextView currencyTopHint;
    private EditText cBCountText;
    private EditText findEditText;
    private ImageView cBFindIcon;
    private RecyclerView recyclerViewRates;
    private CBankRatesAdapter cBRateAdapter;
    private ProgressView progressLinear;
    private ArrayList<Currency> currencyListUsa;
    private ImageButton toolbar_country_flag;

    public void setUsaCountry() {
        setContentView(R.layout.official_rates);
        //get main data from ApplicationInfo from db, create new currencyList if exists
        getAppDataUsa();
        initializeViewVariablesUSA();
        initializeAdmob();

        uploadData();
    }

    public void getAppDataUsa() {
        country = app.getCountry();
        currencyListUsa = country.getCBank().getCurrencyList();
        //if fisrt opened set banklist by start objects Bank
        if (currencyListUsa.size() == 0) {
            setCurrencyListUsa();
        }
    }

    public void initializeViewVariablesUSA() {
        currencyTopHint = (TextView)findViewById(R.id.official_rates_hint);
        currencyTopHint.setText(getString(R.string.show_rate_current_currency_us));
        iconMenu = (ImageButton)findViewById(R.id.icon_menu);
        iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopupMenu(view);
            }
        });
        toolbar_country_flag = (ImageButton)findViewById(R.id.toolbar_country_flag);
        toolbar_country_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSelectCountryFragment();
            }
        });
        setToolbarCountryFlag();
        progressLinear = (ProgressView)findViewById(R.id.progressBar);

        cBCountText = (EditText)findViewById(R.id.cb_rates_count);
        findEditText = (EditText)findViewById(R.id.cb_rates_find);
        cBFindIcon = (ImageView)findViewById(R.id.cb_find_icon);
        cBFindIcon.setImageResource(R.drawable.icon_find);

        recyclerViewRates = (RecyclerView)findViewById(R.id.recycler_view_cb_rates);
        cBRateAdapter = new CBankRatesAdapter(currencyListUsa, MainActivity.this);
        recyclerViewRates.setAdapter(cBRateAdapter);
        recyclerViewRates.setNestedScrollingEnabled(false);
        recyclerViewRates.setFocusable(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerViewRates.setLayoutManager(layoutManager);
        recyclerViewRates.setItemAnimator(itemAnimator);
        serviceIntent = new Intent(this, UpdateInfoService.class);


        cBCountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (Utils.isDigit(editable.toString())) {
                    double count = Double.parseDouble(editable.toString());
                    cBRateAdapter.addCount(count, true);
                }
                else {
                    cBRateAdapter.addCount(1, false);

                    Toast.makeText(MainActivity.this, getText(R.string.exception_count), Toast.LENGTH_SHORT).show();
                }
                cBRateAdapter.notifyDataSetChanged();
            }
        });

        findEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                ArrayList<Currency> currencyNewFound = new ArrayList<Currency>();
                if (editable.toString().isEmpty()) {
                    cBRateAdapter.addItemstoList(currencyListUsa);
                    cBRateAdapter.notifyDataSetChanged();
                }
                else {
                    for (Currency currency : currencyListUsa) {
                        if ((currency.getCountry().toLowerCase().contains(editable.toString().toLowerCase())) || currency.getName().toLowerCase().contains(editable.toString().toLowerCase())) {
                            currencyNewFound.add(currency);
                        }
                        else if (currency.getDescription().toLowerCase().contains(editable.toString().toLowerCase())) {
                            currencyNewFound.add(currency);
                        }
                    }
                    cBRateAdapter.addItemstoList(currencyNewFound);
                    cBRateAdapter.notifyDataSetChanged();
                }
            }
        });

        setViewsUsa();
    }

    public void setCurrencyListUsa() {
        dbHelper = new DBHelper(this, DBVERSION);
        db = dbHelper.getWritableDatabase();
        Currency[] currencies = {new Currency("currency_flag_hongong", "HKD", getString(R.string.hong_kong_currency), getString(R.string.hong_kong)),
                new Currency("currency_flag_hungary","HUF", getString(R.string.hungary_currency), getString(R.string.hungary)),
                new Currency("currency_flag_india", "INR", getString(R.string.india_currency), getString(R.string.india)),
                new Currency("currency_flag_israel", "ILS", getString(R.string.israel_currency), getString(R.string.israel)),
                new Currency("currency_flag_japan", "JPY", getString(R.string.japan_currency), getString(R.string.japan)),
                new Currency("currency_flag_korea", "KRW", getString(R.string.south_korea_currency), getString(R.string.south_korea)),
                new Currency("currency_flag_mexico", "MXN", getString(R.string.mexico_currency), getString(R.string.mexico)),
                new Currency("currency_flag_new_zealand", "NZD", getString(R.string.new_zealand_currency), getString(R.string.new_zealand)),
                new Currency("currency_flag_norway", "NOK", getString(R.string.norway_currency), getString(R.string.norway)),
                new Currency("currency_flag_romania", "RON", getString(R.string.romania_currency), getString(R.string.romania)),
                new Currency("currency_flag_singapore", "SGD", getString(R.string.singapore_currency), getString(R.string.singapore)),
                new Currency("currency_flag_russia", "RUB", getString(R.string.russia_currency), getString(R.string.russia)),
                new Currency("currency_flag_sweden", "SEK", getString(R.string.sweden_currency), getString(R.string.sweden)),
                new Currency("currency_flag_switzerland", "CHF", getString(R.string.switzerland_currency), getString(R.string.switzerland)),
                new Currency("currency_flag_turkey", "TRY", getString(R.string.turkey_currency), getString(R.string.turkey)),
                new Currency("currency_flag_great_britain", "GBP", getString(R.string.greatbritain_currency), getString(R.string.greatbritain)),
                new Currency("currency_flag_europe", "EUR", getString(R.string.europe_currency), getString(R.string.europe)),
                new Currency("currency_flag_poland", "PLN", getString(R.string.poland_currency), getString(R.string.poland)),
                new Currency("currency_flag_brazil", "BRL", getString(R.string.brazil_currency), getString(R.string.brazil)),
                new Currency("currency_flag_australia", "AUD", getString(R.string.australia_currency), getString(R.string.australia)),
                new Currency("currency_flag_bulgaria", "BGN", getString(R.string.bulgary_currency), getString(R.string.bulgary)),
                new Currency("currency_flag_canada", "CAD", getString(R.string.canada_currency), getString(R.string.canada)),
                new Currency("currency_flag_china", "CNY", getString(R.string.china_currency), getString(R.string.china)),
                new Currency("currency_flag_croatia", "HRK", getString(R.string.croatia_currency), getString(R.string.croatia)),
                new Currency("currency_flag_czech", "CZK", getString(R.string.czech_currency), getString(R.string.czech)),
                new Currency("currency_flag_denmark", "DKK", getString(R.string.denmark_currency), getString(R.string.denmark)),
                new Currency("currency_flag_malaysia", "MYR", getString(R.string.malaysia_currency), getString(R.string.malaysia)),
                new Currency("currency_flag_south_africa", "ZAR", getString(R.string.south_africa_currency), getString(R.string.south_africa)),
                new Currency("currency_flag_thailand", "THB", getString(R.string.thailand_currency), getString(R.string.thailand)),
                new Currency("currency_flag_philippines", "PHP", getString(R.string.philippines_currency), getString(R.string.philippines)),
                new Currency("currency_flag_indonesia", "IDR", getString(R.string.indonesia_currency), getString(R.string.indonesia))};
        for (Currency currency : currencies) {
            currencyListUsa.add(currency);
        }

        getDataOfficialRatesUsa();
    }

    public void getDataOfficialRatesUsa() {
        Cursor c = db.query(Utils.getOfficialCurrencyTableName(), null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int nameIndex = c.getColumnIndex("name");
            int countryIndex = c.getColumnIndex("country");
            int descriptionIndex = c.getColumnIndex("description");
            int rateIndex = c.getColumnIndex("rate");
            int dateIndex = c.getColumnIndex("date");

            do {
                for (int i = 0; i < currencyListUsa.size(); i++) {
                    if (currencyListUsa.get(i).getName().equals(c.getString(nameIndex))) {
                        currencyListUsa.get(i).setNewInformation(c.getDouble(rateIndex), c.getString(dateIndex));
                        break;
                    }
                }
            } while (c.moveToNext());
        }
        else {
            c.close();
        }
        dbHelper.close();
    }

    public void uploadData() {
        progressLinear.start();
        startService(serviceIntent);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressLinear.stop();
                    }
                }, 2000);
                setViewsUsa();
            }
        };
        intFilt = new IntentFilter(BROADCAST_ACTION);

    }

    public void setViewsUsa() {
        cBRateAdapter.addItemstoList(country.getCBank().getCurrencyList());
        cBRateAdapter.notifyDataSetChanged();
    }


    //---------------------------------------------------------------Europe---------------------------------------------------------------
    //Europe VARIABLES
    private ArrayList<Currency> currencyListEurope;

    public void setEuropeCountry() {
        setContentView(R.layout.official_rates);
        //get main data from ApplicationInfo from db, create new currencyList if exists
        getAppDataEurope();
        initializeViewVariablesEurope();

        uploadDataEurope();
        initializeAdmob();
    }

    public void getAppDataEurope() {
        country = app.getCountry();
        currencyListEurope = country.getCBank().getCurrencyList();
        //if fisrt opened set banklist by start objects Bank
        if (currencyListEurope.size() == 0) {
            setCurrencyListEurope();
        }
    }

    public void initializeViewVariablesEurope() {
        currencyTopHint = (TextView)findViewById(R.id.official_rates_hint);
        currencyTopHint.setText(getString(R.string.show_rate_current_currency_eu));
        iconMenu = (ImageButton)findViewById(R.id.icon_menu);
        iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopupMenu(view);
            }
        });
        toolbar_country_flag = (ImageButton)findViewById(R.id.toolbar_country_flag);
        toolbar_country_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSelectCountryFragment();
            }
        });
        setToolbarCountryFlag();
        progressLinear = (ProgressView)findViewById(R.id.progressBar);

        cBCountText = (EditText)findViewById(R.id.cb_rates_count);
        findEditText = (EditText)findViewById(R.id.cb_rates_find);
        cBFindIcon = (ImageView)findViewById(R.id.cb_find_icon);
        cBFindIcon.setImageResource(R.drawable.icon_find);

        recyclerViewRates = (RecyclerView)findViewById(R.id.recycler_view_cb_rates);
        cBRateAdapter = new CBankRatesAdapter(currencyListEurope, MainActivity.this);
        recyclerViewRates.setAdapter(cBRateAdapter);
        recyclerViewRates.setNestedScrollingEnabled(false);
        recyclerViewRates.setFocusable(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerViewRates.setLayoutManager(layoutManager);
        recyclerViewRates.setItemAnimator(itemAnimator);
        serviceIntent = new Intent(this, UpdateInfoService.class);


        cBCountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (Utils.isDigit(editable.toString())) {
                    double count = Double.parseDouble(editable.toString());
                    cBRateAdapter.addCount(count, true);
                }
                else {
                    cBRateAdapter.addCount(1, false);

                    Toast.makeText(MainActivity.this, getText(R.string.exception_count), Toast.LENGTH_SHORT).show();
                }
                cBRateAdapter.notifyDataSetChanged();
            }
        });

        findEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                ArrayList<Currency> currencyNewFound = new ArrayList<Currency>();
                if (editable.toString().isEmpty()) {
                    cBRateAdapter.addItemstoList(currencyListEurope);
                    cBRateAdapter.notifyDataSetChanged();
                }
                else {
                    for (Currency currency : currencyListEurope) {
                        if ((currency.getCountry().toLowerCase().contains(editable.toString().toLowerCase())) || currency.getName().toLowerCase().contains(editable.toString().toLowerCase())) {
                            currencyNewFound.add(currency);
                        }
                        else if (currency.getDescription().toLowerCase().contains(editable.toString().toLowerCase())) {
                            currencyNewFound.add(currency);
                        }
                    }
                    cBRateAdapter.addItemstoList(currencyNewFound);
                    cBRateAdapter.notifyDataSetChanged();
                }
            }
        });

        setViewsEurope();
    }

    public void setCurrencyListEurope() {
        dbHelper = new DBHelper(this, DBVERSION);
        db = dbHelper.getWritableDatabase();
        Currency[] currencies = {new Currency("currency_flag_hongong", "HKD", getString(R.string.hong_kong_currency), getString(R.string.hong_kong)),
                new Currency("currency_flag_hungary","HUF", getString(R.string.hungary_currency), getString(R.string.hungary)),
                new Currency("currency_flag_india", "INR", getString(R.string.india_currency), getString(R.string.india)),
                new Currency("currency_flag_israel", "ILS", getString(R.string.israel_currency), getString(R.string.israel)),
                new Currency("currency_flag_japan", "JPY", getString(R.string.japan_currency), getString(R.string.japan)),
                new Currency("currency_flag_korea", "KRW", getString(R.string.south_korea_currency), getString(R.string.south_korea)),
                new Currency("currency_flag_mexico", "MXN", getString(R.string.mexico_currency), getString(R.string.mexico)),
                new Currency("currency_flag_new_zealand", "NZD", getString(R.string.new_zealand_currency), getString(R.string.new_zealand)),
                new Currency("currency_flag_norway", "NOK", getString(R.string.norway_currency), getString(R.string.norway)),
                new Currency("currency_flag_romania", "RON", getString(R.string.romania_currency), getString(R.string.romania)),
                new Currency("currency_flag_singapore", "SGD", getString(R.string.singapore_currency), getString(R.string.singapore)),
                new Currency("currency_flag_russia", "RUB", getString(R.string.russia_currency), getString(R.string.russia)),
                new Currency("currency_flag_sweden", "SEK", getString(R.string.sweden_currency), getString(R.string.sweden)),
                new Currency("currency_flag_switzerland", "CHF", getString(R.string.switzerland_currency), getString(R.string.switzerland)),
                new Currency("currency_flag_turkey", "TRY", getString(R.string.turkey_currency), getString(R.string.turkey)),
                new Currency("currency_flag_great_britain", "GBP", getString(R.string.greatbritain_currency), getString(R.string.greatbritain)),
                new Currency("currency_flag_usa", "USD", getString(R.string.usa_currency), getString(R.string.usa)),
                new Currency("currency_flag_poland", "PLN", getString(R.string.poland_currency), getString(R.string.poland)),
                new Currency("currency_flag_brazil", "BRL", getString(R.string.brazil_currency), getString(R.string.brazil)),
                new Currency("currency_flag_australia", "AUD", getString(R.string.australia_currency), getString(R.string.australia)),
                new Currency("currency_flag_bulgaria", "BGN", getString(R.string.bulgary_currency), getString(R.string.bulgary)),
                new Currency("currency_flag_canada", "CAD", getString(R.string.canada_currency), getString(R.string.canada)),
                new Currency("currency_flag_china", "CNY", getString(R.string.china_currency), getString(R.string.china)),
                new Currency("currency_flag_croatia", "HRK", getString(R.string.croatia_currency), getString(R.string.croatia)),
                new Currency("currency_flag_czech", "CZK", getString(R.string.czech_currency), getString(R.string.czech)),
                new Currency("currency_flag_denmark", "DKK", getString(R.string.denmark_currency), getString(R.string.denmark)),
                new Currency("currency_flag_malaysia", "MYR", getString(R.string.malaysia_currency), getString(R.string.malaysia)),
                new Currency("currency_flag_south_africa", "ZAR", getString(R.string.south_africa_currency), getString(R.string.south_africa)),
                new Currency("currency_flag_thailand", "THB", getString(R.string.thailand_currency), getString(R.string.thailand)),
                new Currency("currency_flag_philippines", "PHP", getString(R.string.philippines_currency), getString(R.string.philippines)),
                new Currency("currency_flag_indonesia", "IDR", getString(R.string.indonesia_currency), getString(R.string.indonesia))};
        for (Currency currency : currencies) {
            currencyListEurope.add(currency);
        }

        getDataOfficialRatesEurope();
    }

    public void getDataOfficialRatesEurope() {
        Cursor c = db.query(Utils.getOfficialCurrencyTableName(), null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int nameIndex = c.getColumnIndex("name");
            int countryIndex = c.getColumnIndex("country");
            int descriptionIndex = c.getColumnIndex("description");
            int rateIndex = c.getColumnIndex("rate");
            int dateIndex = c.getColumnIndex("date");

            do {
                for (int i = 0; i < currencyListEurope.size(); i++) {
                    if (currencyListEurope.get(i).getName().equals(c.getString(nameIndex))) {
                        currencyListEurope.get(i).setNewInformation(c.getDouble(rateIndex), c.getString(dateIndex));
                        break;
                    }
                }
            } while (c.moveToNext());
        }
        else {
            c.close();
        }
        dbHelper.close();
    }

    public void uploadDataEurope() {
        progressLinear.start();
        startService(serviceIntent);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressLinear.stop();
                    }
                }, 2000);
                setViewsEurope();
            }
        };
        intFilt = new IntentFilter(BROADCAST_ACTION);

    }

    public void setViewsEurope() {
        cBRateAdapter.addItemstoList(country.getCBank().getCurrencyList());
        cBRateAdapter.notifyDataSetChanged();
    }

    //---------------------------------------------------------------Turkey---------------------------------------------------------------

    private ArrayList<Currency> currencyListTurkey;

    public void setTurkeyCountry() {
        setContentView(R.layout.official_rates);
        //get main data from ApplicationInfo from db, create new currencyList if exists
        getAppDataTurkey();
        initializeViewVariablesTurkey();

        uploadDataTurkey();
        initializeAdmob();
    }

    public void getAppDataTurkey() {
        country = app.getCountry();
        currencyListTurkey = country.getCBank().getCurrencyList();
        //if fisrt opened set banklist by start objects Bank
        if (currencyListTurkey.size() == 0) {
            setCurrencyListTurkey();
        }
    }

    public void initializeViewVariablesTurkey() {
        currencyTopHint = (TextView)findViewById(R.id.official_rates_hint);
        currencyTopHint.setText(getString(R.string.show_rate_current_currency_tr));
        iconMenu = (ImageButton)findViewById(R.id.icon_menu);
        iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopupMenu(view);
            }
        });
        toolbar_country_flag = (ImageButton)findViewById(R.id.toolbar_country_flag);
        toolbar_country_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSelectCountryFragment();
            }
        });
        setToolbarCountryFlag();
        progressLinear = (ProgressView)findViewById(R.id.progressBar);

        cBCountText = (EditText)findViewById(R.id.cb_rates_count);
        findEditText = (EditText)findViewById(R.id.cb_rates_find);
        cBFindIcon = (ImageView)findViewById(R.id.cb_find_icon);
        cBFindIcon.setImageResource(R.drawable.icon_find);

        recyclerViewRates = (RecyclerView)findViewById(R.id.recycler_view_cb_rates);
        cBRateAdapter = new CBankRatesAdapter(currencyListTurkey, MainActivity.this);
        recyclerViewRates.setAdapter(cBRateAdapter);
        recyclerViewRates.setNestedScrollingEnabled(false);
        recyclerViewRates.setFocusable(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerViewRates.setLayoutManager(layoutManager);
        recyclerViewRates.setItemAnimator(itemAnimator);
        serviceIntent = new Intent(this, UpdateInfoService.class);


        cBCountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (Utils.isDigit(editable.toString())) {
                    double count = Double.parseDouble(editable.toString());
                    cBRateAdapter.addCount(count, true);
                }
                else {
                    cBRateAdapter.addCount(1, false);

                    Toast.makeText(MainActivity.this, getText(R.string.exception_count), Toast.LENGTH_SHORT).show();
                }
                cBRateAdapter.notifyDataSetChanged();
            }
        });

        findEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                ArrayList<Currency> currencyNewFound = new ArrayList<Currency>();
                if (editable.toString().isEmpty()) {
                    cBRateAdapter.addItemstoList(currencyListTurkey);
                    cBRateAdapter.notifyDataSetChanged();
                }
                else {
                    for (Currency currency : currencyListTurkey) {
                        if ((currency.getCountry().toLowerCase().contains(editable.toString().toLowerCase())) || currency.getName().toLowerCase().contains(editable.toString().toLowerCase())) {
                            currencyNewFound.add(currency);
                        }
                        else if (currency.getDescription().toLowerCase().contains(editable.toString().toLowerCase())) {
                            currencyNewFound.add(currency);
                        }
                    }
                    cBRateAdapter.addItemstoList(currencyNewFound);
                    cBRateAdapter.notifyDataSetChanged();
                }
            }
        });

        setViewsTurkey();
    }

    public void setCurrencyListTurkey() {
        dbHelper = new DBHelper(this, DBVERSION);
        db = dbHelper.getWritableDatabase();
        Currency[] currencies = {new Currency("currency_flag_europe", "EUR", getString(R.string.europe_currency), getString(R.string.europe)),
                new Currency("currency_flag_usa", "USD", getString(R.string.usa_currency), getString(R.string.usa)),
                new Currency("currency_flag_australia", "AUD", getString(R.string.australia_currency), getString(R.string.australia)),
                new Currency("currency_flag_denmark", "DKK", getString(R.string.denmark_currency), getString(R.string.denmark)),
                new Currency("currency_flag_great_britain", "GBP", getString(R.string.greatbritain_currency), getString(R.string.greatbritain)),
                new Currency("currency_flag_switzerland", "CHF", getString(R.string.switzerland_currency), getString(R.string.switzerland)),
                new Currency("currency_flag_sweden", "SEK", getString(R.string.sweden_currency), getString(R.string.sweden)),
                new Currency("currency_flag_canada", "CAD", getString(R.string.canada_currency), getString(R.string.canada)),
                new Currency("currency_flag_kuwait", "KWD", getString(R.string.kuwait_currency), getString(R.string.kuwait)),
                new Currency("currency_flag_norway", "NOK", getString(R.string.norway_currency), getString(R.string.norway)),
                new Currency("currency_flag_saudi_arabia", "SAR", getString(R.string.saudi_arabia_currency), getString(R.string.saudi_arabia)),
                new Currency("currency_flag_japan", "JPY", getString(R.string.japan_currency), getString(R.string.japan)),
                new Currency("currency_flag_bulgaria", "BGN", getString(R.string.bulgary_currency), getString(R.string.bulgary)),
                new Currency("currency_flag_romania", "RON", getString(R.string.romania_currency), getString(R.string.romania)),
                new Currency("currency_flag_russia", "RUB", getString(R.string.russia_currency), getString(R.string.russia)),
                new Currency("currency_flag_china", "CNY", getString(R.string.china_currency), getString(R.string.china)),
                new Currency("currency_flag_pakistan", "PKR", getString(R.string.pakistan_currency), getString(R.string.pakistan))};
        for (Currency currency : currencies) {
            currencyListTurkey.add(currency);
        }

        getDataOfficialRatesTurkey();
    }

    public void getDataOfficialRatesTurkey() {
        Cursor c = db.query(Utils.getOfficialCurrencyTableName(), null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int nameIndex = c.getColumnIndex("name");
            int rateIndex = c.getColumnIndex("rate");
            int dateIndex = c.getColumnIndex("date");

            do {
                for (int i = 0; i < currencyListTurkey.size(); i++) {
                    if (currencyListTurkey.get(i).getName().equals(c.getString(nameIndex))) {
                        String dateFromDB = c.getString(dateIndex);
                        if (dateFromDB.length() > 10) {
                            dateFromDB = dateFromDB.substring(0, 10);
                        }
                        currencyListTurkey.get(i).setNewInformation(c.getDouble(rateIndex), dateFromDB);
                        break;
                    }
                }
            } while (c.moveToNext());
        }
        else {
            c.close();
        }
        dbHelper.close();
    }

    public void uploadDataTurkey() {
        progressLinear.start();
        startService(serviceIntent);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressLinear.stop();
                    }
                }, 2000);
                setViewsTurkey();
            }
        };
        intFilt = new IntentFilter(BROADCAST_ACTION);

    }

    public void setViewsTurkey() {
        cBRateAdapter.addItemstoList(country.getCBank().getCurrencyList());
        cBRateAdapter.notifyDataSetChanged();
    }



    //---------------------------------------------------------------Ukraine---------------------------------------------------------------
    private Space bottomSpace;

    public void setUkraineCountry() {
        setContentView(R.layout.activity_main);

        //if we come from another country if the currency not available to Ukraine set it USD
        ArrayList<Integer> arrayCurrencies = new ArrayList<>();
        arrayCurrencies.addAll(Arrays.asList(Utils.usd, Utils.eur, Utils.rb));
        if (!arrayCurrencies.contains(Utils.currencyId)) {
            Utils.currencyId = Utils.usd;
        }

        initializeViewVariablesUkraine(); // findViewById all view elements in this Activity

        //get main data from ApplicationInfo
        getAppDataUkraine();

        setToolbarCurrencyIcon();
        setStartViewUkraine(typeAct);
        setToolbarClickListener();
        initializeAdmob(); // initialize advertising banner

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 1500);
                setViewsUkraine();
            }
        };
        intFilt = new IntentFilter(BROADCAST_ACTION);

        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startService(serviceIntent);
            }
        });
    }

    public void getAppDataUkraine() {
        country = app.getCountry();
        nbu = country.getCBank();
        mBank = country.getMBank();
        blackMarket = country.getBlackMarket();
        bankList = country.getBankList();
        //if fisrt opened set banklist by start objects Bank
        if (bankList.size() == 0) {
            setBankListUkraine();
        }
    }

    public void initializeViewVariablesUkraine() {
        mainToolBarImage = (ImageButton)findViewById(R.id.icon_main_toolbar);
        mainToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));

        nbuCurrencyToolbarImage = (ImageButton)findViewById(R.id.icon_nbu_currency_toolbar);
        converterToolBarImage = (ImageButton)findViewById(R.id.icon_converter_toolbar);
        iconCurrencyToolbar = (ImageButton)findViewById(R.id.icon_currency_toolbar);
        iconMenu = (ImageButton)findViewById(R.id.icon_menu);
        iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopupMenu(view);
            }
        });

        nbuText = (TextView)findViewById(R.id.nbu_text);
        nbuText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CBankRates.class);
                startActivity(intent);
            }
        });
        nbuRate = (TextView)findViewById(R.id.nbu_rate);
        nbuRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CBankRates.class);
                startActivity(intent);
            }
        });
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
        adapter = new BankListAdapter(bankList, Utils.currencyId, MainActivity.this);
        recyclerViewBanks.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerViewBanks.setLayoutManager(layoutManager);
        recyclerViewBanks.setItemAnimator(itemAnimator);
        serviceIntent = new Intent(this, UpdateInfoService.class);
        refreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        bottomSpace = (Space)findViewById(R.id.bottom_space);
    }

    public void setStartViewUkraine(int typeAct) {
        if (typeAct != Utils.CONVERTERTYPE && typeAct != Utils.SELECTCURRENCYTYPE && typeAct != Utils.CBANKRATETYPE) {
            dbHelper = new DBHelper(this, DBVERSION);
            db = dbHelper.getWritableDatabase();
            setFirstViewsUkraine();

            refreshLayout.setRefreshing(true);
            startService(serviceIntent);
        }
        else {
            setViewsUkraine();
        }
    }

    public void setFirstViewsUkraine() {
        getDataUkraine(nbu);
        getDataUkraine(mBank);
        getDataUkraine(blackMarket);
        getDataUkraine(bankList);
        setViewsUkraine();
        dbHelper.close();
    }

    public void setViewsUkraine() {
        setNbuViewUkraine(nbu);
        setMBankViewUkraine(mBank);
        setBLackMarketViewUkraine(blackMarket);
        setBanklistView(bankList);
    }

    public void setNbuViewUkraine(CentralBank nbu) {
        nbuRate.setText(String.valueOf(String.format("%.4f",nbu.getRate(Utils.currencyId))));
        nbuDate.setText(nbu.getDate());
        setChangesInfoUkraine(nbu);
    }

    public void setMBankViewUkraine(MBank mBank) {
        mbBuyRate.setText(String.format("%.4f",mBank.getBuy(Utils.currencyId)));
        mbSellRate.setText(String.format("%.4f",mBank.getSell(Utils.currencyId)));
        mbDate.setText(mBank.getDate());
        setChangesInfoUkraine(mBank);
    }

    public void setBLackMarketViewUkraine(BlackMarket blackMarket) {
        blackMBuyRate.setText(String.format("%.4f",blackMarket.getBuy(Utils.currencyId)));
        blackMSellRate.setText(String.format("%.4f",blackMarket.getSell(Utils.currencyId)));
        blackMDate.setText(blackMarket.getDate());
        setChangesInfoUkraine(blackMarket);
    }

    public void setChangesInfoUkraine(CentralBank nbu) {
        if (nbu.getChanges(Utils.currencyId) != 0)
        {
            if (nbu.getChanges(Utils.currencyId) < 0)
            {
                nbuChangesText.setText(String.format("%.4f", nbu.getChanges(Utils.currencyId)));
                nbuChangesImgUp.setVisibility(View.INVISIBLE);
                nbuChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                nbuChangesText.setText("+" + String.format("%.4f", nbu.getChanges(Utils.currencyId)));
                nbuChangesImgUp.setVisibility(View.VISIBLE);
                nbuChangesImgDown.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setChangesInfoUkraine(MBank mBank) {
        if (mBank.getChangesBuy(Utils.currencyId) != 0)
        {
            if (mBank.getChangesBuy(Utils.currencyId) < 0)
            {
                mbBuyChangesText.setText(String.format("%.4f", mBank.getChangesBuy(Utils.currencyId)));
                mbBuyChangesImgUp.setVisibility(View.INVISIBLE);
                mbBuyChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                mbBuyChangesText.setText("+" + String.format("%.4f", mBank.getChangesBuy(Utils.currencyId)));
                mbBuyChangesImgUp.setVisibility(View.VISIBLE);
                mbBuyChangesImgDown.setVisibility(View.INVISIBLE);
            }
        }

        if (mBank.getChangesSell(Utils.currencyId) != 0)
        {
            if (mBank.getChangesSell(Utils.currencyId) < 0)
            {
                mbSellChangesText.setText(String.format("%.4f", mBank.getChangesSell(Utils.currencyId)));
                mbSellChangesImgUp.setVisibility(View.INVISIBLE);
                mbSellChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                mbSellChangesText.setText("+" + String.format("%.4f", mBank.getChangesSell(Utils.currencyId)));
                mbSellChangesImgUp.setVisibility(View.VISIBLE);
                mbSellChangesImgDown.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setChangesInfoUkraine(BlackMarket blackMarket) {
        if (blackMarket.getChangesBuy(Utils.currencyId) != 0)
        {
            if (blackMarket.getChangesBuy(Utils.currencyId) < 0)
            {
                blackMBuyChangesText.setText(String.format("%.4f", blackMarket.getChangesBuy(Utils.currencyId)));
                blackMBuyChangesImgUp.setVisibility(View.INVISIBLE);
                blackMBuyChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                blackMBuyChangesText.setText("+" + String.format("%.4f", blackMarket.getChangesBuy(Utils.currencyId)));
                blackMBuyChangesImgUp.setVisibility(View.VISIBLE);
                blackMBuyChangesImgDown.setVisibility(View.INVISIBLE);
            }
        }

        if (blackMarket.getChangesSell(Utils.currencyId) != 0)
        {
            if (blackMarket.getChangesSell(Utils.currencyId) < 0)
            {
                blackMSellChangesText.setText(String.format("%.4f", blackMarket.getChangesSell(Utils.currencyId)));
                blackMSellChangesImgUp.setVisibility(View.INVISIBLE);
                blackMSellChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                blackMSellChangesText.setText("+" + String.format("%.4f", blackMarket.getChangesSell(Utils.currencyId)));
                blackMSellChangesImgUp.setVisibility(View.VISIBLE);
                blackMSellChangesImgDown.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void getDataUkraine(CentralBank nbu) {
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
                nbu.setNewInformationUkraine(c.getString(dateServerIndex), c.getDouble(rateDIndex), c.getDouble(rateEIndex), c.getDouble(rateRIndex), c.getDouble(changesDIndex), c.getDouble(changesEIndex), c.getDouble(changesRIndex));
            } while (c.moveToNext());
        }
        else {
            c.close();
        }
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
    }

    public void setBankListUkraine() {
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
        Bank UkrGaz = new Bank("bank_icon_ugb", getString(R.string.ugb));
        Bank TaskoB = new Bank("bank_icon_taskobank", getString(R.string.taskobank));
        Bank KreditD = new Bank("bank_icon_kreditdnepr", getString(R.string.kreditdnepr));
        bankList.addAll(Arrays.asList(PrivatB, OshadB, SberB, RaiphB, UkrsotsB, AlphaB, UkrSibB, PumbB, VtbB, OtpB, CrediAgriB, UkrGaz, TaskoB, KreditD));
    }



    //---------------------------------------------------------------Uk---------------------------------------------------------------
    private TextView official_text;
    private TextView official_rate;
    private TextView officialChangesText;
    private TextView officialDate;
    private ImageView officialChangesImgUp;
    private ImageView officialChangesImgDown;

    private CentralBank centralBank;

    public void setUKCountry() {
        setContentView(R.layout.activity_main_second);

        //if we come from another country if the currency not available for current country set it USD
        ArrayList<Integer> arrayCurrencies = new ArrayList<>();
        arrayCurrencies.addAll(Arrays.asList(Utils.usd, Utils.eur, Utils.rb, Utils.cad, Utils.tyr, Utils.pln, Utils.ils, Utils.cny, Utils.czk, Utils.sek, Utils.chf, Utils.jpy));
        if (!arrayCurrencies.contains(Utils.currencyId)) {
            Utils.currencyId = Utils.usd;
        }

        initializeViewVariablesUk(); // findViewById all view elements in this Activity

        //get main data from ApplicationInfo
        getAppDataUk();
        setToolbarCurrencyIcon();
        setStartViewUk(typeAct);
        setToolbarClickListener();
        initializeAdmob(); // initialize advertising banner

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 1500);
                setViewsUk();
            }
        };
        intFilt = new IntentFilter(BROADCAST_ACTION);

        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startService(serviceIntent);
            }
        });
    }

    public void initializeViewVariablesUk() {
        mainToolBarImage = (ImageButton)findViewById(R.id.icon_main_toolbar);
        mainToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
        nbuCurrencyToolbarImage = (ImageButton)findViewById(R.id.icon_nbu_currency_toolbar);
        converterToolBarImage = (ImageButton)findViewById(R.id.icon_converter_toolbar);
        iconCurrencyToolbar = (ImageButton)findViewById(R.id.icon_currency_toolbar);
        iconMenu = (ImageButton)findViewById(R.id.icon_menu);
        iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopupMenu(view);
            }
        });

        official_text = (TextView)findViewById(R.id.official_text);
        official_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CBankRates.class);
                startActivity(intent);
            }
        });
        official_rate = (TextView)findViewById(R.id.official_rate);
        official_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CBankRates.class);
                startActivity(intent);
            }
        });
        officialChangesText = (TextView)findViewById(R.id.official_changes_text);
        officialDate = (TextView)findViewById(R.id.official_date);
        officialChangesImgUp = (ImageView)findViewById(R.id.official_changes_img_up);
        officialChangesImgDown = (ImageView)findViewById(R.id.official_changes_img_down);
        recyclerViewBanks = (RecyclerView)findViewById(R.id.recycler_view_banks_convert);
        recyclerViewBanks.setNestedScrollingEnabled(false);
        recyclerViewBanks.setFocusable(false);
        adapter = new BankListAdapter(bankList, Utils.currencyId, MainActivity.this);
        recyclerViewBanks.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerViewBanks.setLayoutManager(layoutManager);
        recyclerViewBanks.setItemAnimator(itemAnimator);
        serviceIntent = new Intent(this, UpdateInfoService.class);
        refreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
    }

    public void getAppDataUk() {
        country = app.getCountry();
        centralBank = country.getCBank();
        bankList = country.getBankList();
        if (bankList.size() == 0) {
            setBankListUk();
        }
    }

    public void setBankListUk() {
        Bank DEBENHAMS = new Bank("bank_icon_debenhams", getString(R.string.debenhams));
        Bank Travelex = new Bank("bank_icon_travelex", getString(R.string.travelex));
        Bank ICE = new Bank("bank_icon_ice", getString(R.string.ice));
        Bank NatWest = new Bank("bank_icon_natwest", getString(R.string.natwest));
        Bank RBS = new Bank("bank_icon_rbs", getString(R.string.rbs));
//        Bank VirginAtlantic = new Bank("bank_icon_virgin_atlantic",  getString(R.string.virgin_atlantic));
        Bank MSBANK = new Bank("bank_icon_msbank", getString(R.string.msbank));
        Bank Eurochange = new Bank("bank_icon_eurochange", getString(R.string.eurochange));
        Bank ACEFX = new Bank("bank_icon_ace_fx", getString(R.string.ace_fx));
        Bank GRIFFIN = new Bank("bank_icon_griffin", getString(R.string.griffin));
        Bank Sterling = new Bank("bank_icon_sterling", getString(R.string.sterling));
//        Bank MoneyCorp = new Bank("bank_icon_moneycorp", getString(R.string.moneycorp));
        bankList.addAll(Arrays.asList(DEBENHAMS, Travelex, ICE, NatWest, RBS, MSBANK, Eurochange, ACEFX, GRIFFIN, Sterling));
    }

    public void setStartViewUk(int typeAct) {
        if (typeAct != Utils.CONVERTERTYPE && typeAct != Utils.SELECTCURRENCYTYPE && typeAct != Utils.CBANKRATETYPE) {
            dbHelper = new DBHelper(this, DBVERSION);
            db = dbHelper.getWritableDatabase();
            setFirstViewsUk();

            refreshLayout.setRefreshing(true);
            startService(serviceIntent);
        }
        else {
            setViewsUk();
        }
    }

    public void setFirstViewsUk() {
        getDataUk(centralBank);
        getDataUk(bankList);
        setViewsUk();
        dbHelper.close();
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
    }

    public void setViewsUk() {
        setCBView(centralBank);
        setBanklistView(bankList);
    }


    //---------------------------------------------------------------Poland---------------------------------------------------------------

    public void setPolandCountry() {
        setContentView(R.layout.activity_main_second);

        ArrayList<Integer> arrayCurrencies = new ArrayList<>();
        arrayCurrencies.addAll(Arrays.asList(Utils.usd, Utils.eur, Utils.gbp, Utils.chf));
        if (!arrayCurrencies.contains(Utils.currencyId)) {
            Utils.currencyId = Utils.usd;
        }

        initializeViewVariablesPoland(); // findViewById all view elements in this Activity

        //get main data from ApplicationInfo
        getAppDataPoland();
        setToolbarCurrencyIcon();
        setStartViewPoland(typeAct);
        setToolbarClickListener();
        initializeAdmob(); // initialize advertising banner

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 1500);
                setViewsUk();
            }
        };
        intFilt = new IntentFilter(BROADCAST_ACTION);

        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startService(serviceIntent);
            }
        });
    }

    public void initializeViewVariablesPoland() {
        mainToolBarImage = (ImageButton)findViewById(R.id.icon_main_toolbar);
        mainToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
        nbuCurrencyToolbarImage = (ImageButton)findViewById(R.id.icon_nbu_currency_toolbar);
        converterToolBarImage = (ImageButton)findViewById(R.id.icon_converter_toolbar);
        iconCurrencyToolbar = (ImageButton)findViewById(R.id.icon_currency_toolbar);
        iconMenu = (ImageButton)findViewById(R.id.icon_menu);
        iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopupMenu(view);
            }
        });

        official_text = (TextView)findViewById(R.id.official_text);
        official_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CBankRates.class);
                startActivity(intent);
            }
        });
        official_rate = (TextView)findViewById(R.id.official_rate);
        official_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CBankRates.class);
                startActivity(intent);
            }
        });
        officialChangesText = (TextView)findViewById(R.id.official_changes_text);
        officialDate = (TextView)findViewById(R.id.official_date);
        officialChangesImgUp = (ImageView)findViewById(R.id.official_changes_img_up);
        officialChangesImgDown = (ImageView)findViewById(R.id.official_changes_img_down);
        currencyExchangePoint = (TextView)findViewById(R.id.currency_exchange_point);
        currencyExchangePoint.setText(R.string.banksPoland);
        recyclerViewBanks = (RecyclerView)findViewById(R.id.recycler_view_banks_convert);
        recyclerViewBanks.setNestedScrollingEnabled(false);
        recyclerViewBanks.setFocusable(false);
        adapter = new BankListAdapter(bankList, Utils.currencyId, MainActivity.this);
        recyclerViewBanks.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerViewBanks.setLayoutManager(layoutManager);
        recyclerViewBanks.setItemAnimator(itemAnimator);
        serviceIntent = new Intent(this, UpdateInfoService.class);
        refreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
    }

    public void getAppDataPoland() {
        country = app.getCountry();
        centralBank = country.getCBank();
        bankList = country.getBankList();
        if (bankList.size() == 0) {
            setBankListPoland();
        }
    }

    public void setBankListPoland() {
        Bank Alior = new Bank("alior_logo", getString(R.string.alior));
        Bank BPS = new Bank("bps_logo", getString(R.string.bps));
        Bank WBK = new Bank("zachodni_logo", getString(R.string.wbk));
        Bank BOS = new Bank("bos_logo", getString(R.string.bos));
        Bank Credit = new Bank("credit_agricole_logo", getString(R.string.credit));
        Bank Getin = new Bank("getin_logo", getString(R.string.getin));
        Bank ING = new Bank("ing_logo", getString(R.string.ing));
        Bank Deutche = new Bank("deutche_logo", getString(R.string.deutche));
        Bank Paribas = new Bank("bnp_paribas_logo", getString(R.string.paribas));
        Bank Raiffeisen = new Bank("raiffeisen_logo", getString(R.string.raiffeisen));
        Bank Mbank = new Bank("mbank_logo", getString(R.string.mbank));
        Bank PKO = new Bank("pko_logo", getString(R.string.pko));
        bankList.addAll(Arrays.asList(Alior, BPS, WBK, BOS, Credit, Getin, ING, Deutche, Paribas, Raiffeisen, Mbank, PKO));
    }

    public void setStartViewPoland(int typeAct) {
        if (typeAct != Utils.CONVERTERTYPE && typeAct != Utils.SELECTCURRENCYTYPE && typeAct != Utils.CBANKRATETYPE) {
            dbHelper = new DBHelper(this, DBVERSION);
            db = dbHelper.getWritableDatabase();
            setFirstViewsPoland();

            refreshLayout.setRefreshing(true);
            startService(serviceIntent);
        }
        else {
            setViewsPoland();
        }
    }

    public void setFirstViewsPoland() {
        getDataPoland(centralBank);
        getDataPoland(bankList);
        setViewsPoland();
        dbHelper.close();
    }

    public void setViewsPoland() {
        setCBView(centralBank);
        setBanklistView(bankList);
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
    }



    //---------------------------------------------------------------Russia---------------------------------------------------------------

    public void setRussiaCountry() {
        setContentView(R.layout.activity_main_second);

        ArrayList<Integer> arrayCurrencies = new ArrayList<>();
        arrayCurrencies.addAll(Arrays.asList(Utils.usd, Utils.eur));
        if (!arrayCurrencies.contains(Utils.currencyId)) {
            Utils.currencyId = Utils.usd;
        }

        initializeViewVariablesRussia(); // findViewById all view elements in this Activity

        //get main data from ApplicationInfo
        getAppDataRussia();
        setToolbarCurrencyIcon();
        setStartViewRussia(typeAct);
        setToolbarClickListener();
        initializeAdmob(); // initialize advertising banner

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 1500);
                setViewsUk();
            }
        };
        intFilt = new IntentFilter(BROADCAST_ACTION);

        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startService(serviceIntent);
            }
        });
    }

    public void initializeViewVariablesRussia() {
        mainToolBarImage = (ImageButton)findViewById(R.id.icon_main_toolbar);
        mainToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
        nbuCurrencyToolbarImage = (ImageButton)findViewById(R.id.icon_nbu_currency_toolbar);
        converterToolBarImage = (ImageButton)findViewById(R.id.icon_converter_toolbar);
        iconCurrencyToolbar = (ImageButton)findViewById(R.id.icon_currency_toolbar);
        iconMenu = (ImageButton)findViewById(R.id.icon_menu);
        iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopupMenu(view);
            }
        });

        official_text = (TextView)findViewById(R.id.official_text);
        official_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CBankRates.class);
                startActivity(intent);
            }
        });
        official_rate = (TextView)findViewById(R.id.official_rate);
        official_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CBankRates.class);
                startActivity(intent);
            }
        });
        officialChangesText = (TextView)findViewById(R.id.official_changes_text);
        officialDate = (TextView)findViewById(R.id.official_date);
        officialChangesImgUp = (ImageView)findViewById(R.id.official_changes_img_up);
        officialChangesImgDown = (ImageView)findViewById(R.id.official_changes_img_down);
        currencyExchangePoint = (TextView)findViewById(R.id.currency_exchange_point);
        currencyExchangePoint.setText(R.string.banksRussia);
        recyclerViewBanks = (RecyclerView)findViewById(R.id.recycler_view_banks_convert);
        recyclerViewBanks.setNestedScrollingEnabled(false);
        recyclerViewBanks.setFocusable(false);
        adapter = new BankListAdapter(bankList, Utils.currencyId, MainActivity.this);
        recyclerViewBanks.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerViewBanks.setLayoutManager(layoutManager);
        recyclerViewBanks.setItemAnimator(itemAnimator);
        serviceIntent = new Intent(this, UpdateInfoService.class);
        refreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
    }

    public void getAppDataRussia() {
        country = app.getCountry();
        centralBank = country.getCBank();
        bankList = country.getBankList();
        if (bankList.size() == 0) {
            setBankListRussia();
        }
    }

    public void setBankListRussia() {
        Bank SberBankRussia = new Bank("bank_icon_sber_russia", getString(R.string.sber_russia));
        Bank GasPromBankRussia = new Bank("bank_icon_gazprom_russia", getString(R.string.gasprom_russia));
        Bank RosSelHozBankRussia = new Bank("bank_icon_rosselhozbank_russia", getString(R.string.rosselhoz_russia));
//        Bank AlphaBankRussia = new Bank("bank_icon_alpha_russia", getString(R.string.alpha_russia));
        Bank MoscowCreditBankRussia = new Bank("bank_icon_moscow_credit_russia", getString(R.string.moscow_credit_russia));
        Bank PromSvyazBankRussia = new Bank("bank_icon_promsvyazbank_russia", getString(R.string.promsvyaz_russia));
        Bank UnicreditBankRussia = new Bank("bank_icon_unicredit_russia", getString(R.string.unicredit_russia));
        Bank BinBankRussia = new Bank("bank_icon_binbank_russia", getString(R.string.binbank_russia));
        Bank RaiffaisenBankRussia = new Bank("bank_icon_raiffaisen_russia", getString(R.string.raiffaisen_russia));
        Bank RosBankRussia = new Bank("bank_icon_rosbank_russia", getString(R.string.rosbank_russia));
        Bank MoscowBankRussia = new Bank("bank_icon_bank_of_moscow_russia", getString(R.string.moscow_bank_russia));
        Bank StPetersburgBankRussia = new Bank("bank_icon_st_petersburg_russia", getString(R.string.st_petersburg_bank_russia));
        Bank SovKomBankRussia = new Bank("bank_icon_sovkombank_russia", getString(R.string.sovkombank_russia));
        Bank RussianStandartBankRussia = new Bank("bank_icon_russian_standart_russia", getString(R.string.russian_standart_russia));
        Bank MoscowOblBankRussia = new Bank("bank_icon_mosoblbank_russia", getString(R.string.moscow_obl_bank_russia));
        Bank CityBankRussia = new Bank("bank_icon_city_russia", getString(R.string.city_bank_russia));
        Bank AbsoluteBankRussia = new Bank("bank_icon_absolutebank_russia", getString(R.string.absolute_bank_russia));
        Bank TinkoffBankRussia = new Bank("bank_icon_tinkof_russia", getString(R.string.tinkoff_russia));

        bankList.addAll(Arrays.asList(SberBankRussia, GasPromBankRussia, RosSelHozBankRussia, MoscowCreditBankRussia, PromSvyazBankRussia, UnicreditBankRussia, BinBankRussia, RaiffaisenBankRussia, RosBankRussia, MoscowBankRussia, StPetersburgBankRussia, SovKomBankRussia, RussianStandartBankRussia, MoscowOblBankRussia, CityBankRussia, AbsoluteBankRussia, TinkoffBankRussia));
    }

    public void setStartViewRussia(int typeAct) {
        if (typeAct != Utils.CONVERTERTYPE && typeAct != Utils.SELECTCURRENCYTYPE && typeAct != Utils.CBANKRATETYPE) {
            dbHelper = new DBHelper(this, DBVERSION);
            db = dbHelper.getWritableDatabase();
            setFirstViewsRussia();

            refreshLayout.setRefreshing(true);
            startService(serviceIntent);
        }
        else {
            setViewsRussia();
        }
    }

    public void setFirstViewsRussia() {
        getDataRussia(centralBank);
        getDataRussia(bankList);
        setViewsRussia();
        dbHelper.close();
    }

    public void setViewsRussia() {
        setCBView(centralBank);
        setBanklistView(bankList);
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
    }

    //----------------------------------------------global maethods at least for couple countries

    public void setCBView(CentralBank centralBank) {
        official_rate.setText(String.valueOf(String.format("%.4f",centralBank.getRate(Utils.currencyId))));
        officialDate.setText(centralBank.getDate());
        setChangesInfo(centralBank);
    }

    public void setChangesInfo(CentralBank cBank) {
        if (cBank.getChanges(Utils.currencyId) != 0)
        {
            if (cBank.getChanges(Utils.currencyId) < 0)
            {
                official_rate.setText(String.format("%.4f", cBank.getChanges(Utils.currencyId)));
                officialChangesImgUp.setVisibility(View.INVISIBLE);
                officialChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                officialChangesText.setText("+" + String.format("%.4f", cBank.getChanges(Utils.currencyId)));
                officialChangesImgUp.setVisibility(View.VISIBLE);
                officialChangesImgDown.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setBanklistView(ArrayList<Bank> bankList) {
        refreshAdapter(bankList);
    }

    public void refreshAdapter(ArrayList<Bank> bankList) {
        adapter.addItemstoList(bankList);
        adapter.addCurrencyId(Utils.currencyId);
        adapter.notifyDataSetChanged();
    }

    public void setToolbarClickListener() {
        nbuCurrencyToolbarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CBankRates.class);
                startActivity(intent);
            }
        });

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
    }

    public void setToolbarCurrencyIcon() {
        switch (Utils.currencyId)
        {
            case Utils.usd:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_dollar_dark);
                break;
            case Utils.eur:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_euro_dark);
                break;
            case Utils.rb:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_rb_dark);
                break;
            case Utils.gbp:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_gbp_dark);
                break;
            case Utils.chf:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_chf_dark);
                break;
            case Utils.tyr:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_tyr_dark);
                break;
            case Utils.cad:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_cad_dark);
                break;
            case Utils.pln:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_pln_dark);
                break;
            case Utils.ils:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_ils_dark);
                break;
            case Utils.cny:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_cny_dark);
                break;
            case Utils.czk:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_czk_dark);
                break;
            case Utils.sek:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_sek_dark);
                break;
            case Utils.jpy:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_jpy_dark);
                break;
            default:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_dollar_dark);
                break;

        }
    }

    public void setToolbarCountryFlag() {
        try {
            toolbar_country_flag.setBackgroundResource(getIconBank(country));
        }
        catch (Exception e) {
            Log.d("toolbarFlag=", " " + e.getMessage());
        }
    }

    public int getIconBank(Country country) {
        String iconName = country.getFlag();
        int id = getApplicationContext().getResources().getIdentifier(iconName, "drawable", getApplicationContext().getPackageName());

        return id;
    }

    public void initializeAdmob() {
        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                if (bottomSpace != null) {
                    bottomSpace.setVisibility(View.GONE);
                }
            }
        });
    }

    public void showpopupMenu(View view) {
        PopupMenu popup = new PopupMenu(MainActivity.this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_change_country:
                        startSelectCountryFragment();
                        return true;
                    case R.id.action_proversion:
                        goToDownloadProVersion();
                        return true;
                    case R.id.action_estimate:
                        estimateApp();
                        return true;
                    case R.id.action_exit:
                        moveTaskToBack(true);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();
    }

    public void startSelectCountryFragment() {
        Fragment selectCountryFragment = new SelectCountryFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
            .replace(R.id.main_container, selectCountryFragment)
            .addToBackStack("selectCountry")
            .commit();

        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void goToDownloadProVersion() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.sergey.exrate")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.sergey.exrate")));
        }
    }

    public void estimateApp() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
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
        else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (EXIT_FROM_ALL_ACTIVITIES) {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            registerReceiver(broadcastReceiver, intFilt);
        }
        catch (IllegalArgumentException ilae){
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(broadcastReceiver);
        }
        catch (IllegalArgumentException ilae){
        }

        preferences = getPreferences(MODE_PRIVATE);
        Editor ed = preferences.edit();
        ed.putInt(SAVED_COUNTRY, Utils.country_code);
        ed.commit();
    }

}
