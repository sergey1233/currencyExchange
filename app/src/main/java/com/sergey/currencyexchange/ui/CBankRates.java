package com.sergey.currencyexchange.ui;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;
import android.widget.Toast;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.ApplicationInfo;
import com.sergey.currencyexchange.model.Currency;
import com.sergey.currencyexchange.model.DBHelper;
import com.sergey.currencyexchange.model.Utils;
import com.sergey.currencyexchange.ui.fragment.SelectCountryFragment;
import java.util.ArrayList;



public class CBankRates extends AppCompatActivity {

    private static final int TYPEACTIVITY = Utils.CBANKRATETYPE;

    private ImageButton mainToolBarImage;
    private ImageButton nbuCurrencyToolbarImage;
    private ImageButton converterToolBarImage;
    private ImageButton iconCurrencyToolbar;
    private TextView textTitle;
    private RecyclerView recyclerViewRates;
    private CBankRatesAdapter adapter;
    private EditText nbuCountText;
    private EditText findEditText;
    private ImageView nbuFindIcon;
    private ImageButton iconMenu;
    private TextView currencyBottomHint;

    private ArrayList<Currency> currencyList;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private static final int DBVERSION = Utils.DBVERSION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nbu_rates);

        mainToolBarImage = (ImageButton)findViewById(R.id.icon_main_toolbar);
        nbuCurrencyToolbarImage = (ImageButton)findViewById(R.id.icon_nbu_currency_toolbar);
        converterToolBarImage = (ImageButton)findViewById(R.id.icon_converter_toolbar);
        iconCurrencyToolbar = (ImageButton)findViewById(R.id.icon_currency_toolbar);
        recyclerViewRates = (RecyclerView)findViewById(R.id.recycler_view_nbu_rates);
        iconMenu = (ImageButton)findViewById(R.id.icon_menu);
        currencyBottomHint = (TextView)findViewById(R.id.official_rates_hint);
        setCurrencyBottomHintText();

        textTitle = (TextView)findViewById(R.id.text_title);
        if (Utils.country_code == Utils.UKRAINE_CODE) {
            textTitle.setText(R.string.rate_nbu);
        }
        nbuCountText = (EditText)findViewById(R.id.nbu_rates_count);
        findEditText = (EditText)findViewById(R.id.nbu_rates_find);
        nbuFindIcon = (ImageView)findViewById(R.id.nbu_find_icon);
        nbuFindIcon.setImageResource(R.drawable.icon_find);

        currencyList = ApplicationInfo.getInstance().getCountry().getCBank().getCurrencyList();
        adapter = new CBankRatesAdapter(currencyList, this);
        recyclerViewRates.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerViewRates.setLayoutManager(layoutManager);
        recyclerViewRates.setItemAnimator(itemAnimator);

        nbuCurrencyToolbarImage.setColorFilter(Color.argb(255, 255, 255, 255));
        iconCurrencyToolbar.setVisibility(View.INVISIBLE);

        nbuCountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (Utils.isDigit(editable.toString())) {
                    double count = Double.parseDouble(editable.toString());
                    adapter.addCount(count, true);
                }
                else {
                    adapter.addCount(1, false);

                    Toast.makeText(CBankRates.this, getText(R.string.exception_count), Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
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
                    adapter.addItemstoList(currencyList);
                    adapter.notifyDataSetChanged();
                }
                else {
                    for (Currency currency : currencyList) {
                        if ((currency.getCountry().toLowerCase().contains(editable.toString().toLowerCase())) || currency.getName().toLowerCase().contains(editable.toString().toLowerCase())) {
                            currencyNewFound.add(currency);
                        }
                        else if (currency.getDescription().toLowerCase().contains(editable.toString().toLowerCase())) {
                            currencyNewFound.add(currency);
                        }
                    }
                    adapter.addItemstoList(currencyNewFound);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        //if first opened set currencyList(for CBankRates) by start objects Currency
        if (currencyList.size() == 0) {
            switch (Utils.country_code) {
                case Utils.UK_CODE:
                    setCurrencyListUk();
                    break;
                case Utils.POLAND_CODE:
                    setCurrencyListPoland();
                    break;
                case Utils.RUSSIA_CODE:
                    setCurrencyListRussia();
                    break;
                case Utils.UKRAINE_CODE:
                    setCurrencyListUkraine();
                    break;
                default:
                    setCurrencyListUk();
                    break;
            }
        }

        mainToolBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CBankRates.this, MainActivity.class);
                intent.putExtra("fromActivity", TYPEACTIVITY);
                startActivity(intent);
            }
        });

        converterToolBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CBankRates.this, Converter.class);
                intent.putExtra("fromActivity", TYPEACTIVITY);
                startActivity(intent);
            }
        });

        iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopupMenu(view);
            }
        });
    }

    public void showpopupMenu(View view) {
        PopupMenu popup = new PopupMenu(CBankRates.this, view);
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

    public void setCurrencyBottomHintText() {
        switch (Utils.country_code) {
            case Utils.UK_CODE:
                currencyBottomHint.setText(getString(R.string.show_rate_current_currency_uk));
                break;
            case Utils.POLAND_CODE:
                currencyBottomHint.setText(getString(R.string.show_rate_current_currency_pl));
                break;
            case Utils.RUSSIA_CODE:
                currencyBottomHint.setText(getString(R.string.show_rate_current_currency_ru));
                break;
            case Utils.UKRAINE_CODE:
                currencyBottomHint.setText(getString(R.string.show_rate_current_currency_ua));
                break;
            default:
                currencyBottomHint.setText(getString(R.string.show_rate_current_currency_uk));
                break;
        }
    }

    public void setCurrencyListUk() {
        dbHelper = new DBHelper(this, DBVERSION);
        db = dbHelper.getWritableDatabase();
        Currency[] currencies = {new Currency("currency_flag_europe", "EUR", getString(R.string.europe_currency), getString(R.string.europe)),
                new Currency("currency_flag_usa", "USD", getString(R.string.usa_currency), getString(R.string.usa)),
                new Currency("currency_flag_poland", "PLN", getString(R.string.poland_currency), getString(R.string.poland)),
                new Currency("currency_flag_russia", "RUB", getString(R.string.russia_currency), getString(R.string.russia)),
                new Currency("currency_flag_sweden", "SEK", getString(R.string.sweden_currency), getString(R.string.sweden)),
                new Currency("currency_flag_switzerland", "CHF", getString(R.string.switzerland_currency), getString(R.string.switzerland)),
                new Currency("currency_flag_turkey", "TRY", getString(R.string.turkey_currency), getString(R.string.turkey)),
                new Currency("currency_flag_hongong", "HKD", getString(R.string.hong_kong_currency), getString(R.string.hong_kong)),
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
            currencyList.add(currency);
        }

        getData(currencyList);
    }

    public void setCurrencyListPoland() {
        dbHelper = new DBHelper(this, DBVERSION);
        db = dbHelper.getWritableDatabase();
        Currency[] currencies = {new Currency("currency_flag_hongong", "HKD", getString(R.string.hong_kong_currency), getString(R.string.hong_kong)),
                new Currency("currency_flag_usa", "USD", getString(R.string.usa_currency), getString(R.string.usa)),
                new Currency("currency_flag_europe", "EUR", getString(R.string.europe_currency), getString(R.string.europe)),
                new Currency("currency_flag_switzerland", "CHF", getString(R.string.switzerland_currency), getString(R.string.switzerland)),
                new Currency("currency_flag_great_britain", "GBP", getString(R.string.greatbritain_currency), getString(R.string.greatbritain)),
                new Currency("currency_flag_russia", "RUB", getString(R.string.russia_currency), getString(R.string.russia)),
                new Currency("currency_flag_czech", "CZK", getString(R.string.czech_currency), getString(R.string.czech)),
                new Currency("currency_flag_island", "ISK", getString(R.string.island_currency), getString(R.string.island)),
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
                new Currency("currency_flag_sweden", "SEK", getString(R.string.sweden_currency), getString(R.string.sweden)),
                new Currency("currency_flag_turkey", "TRY", getString(R.string.turkey_currency), getString(R.string.turkey)),
                new Currency("currency_flag_ukraine", "UAH", getString(R.string.ukraine_currency), getString(R.string.ukraine)),
                new Currency("currency_flag_brazil", "BRL", getString(R.string.brazil_currency), getString(R.string.brazil)),
                new Currency("currency_flag_australia", "AUD", getString(R.string.australia_currency), getString(R.string.australia)),
                new Currency("currency_flag_bulgaria", "BGN", getString(R.string.bulgary_currency), getString(R.string.bulgary)),
                new Currency("currency_flag_canada", "CAD", getString(R.string.canada_currency), getString(R.string.canada)),
                new Currency("currency_flag_china", "CNY", getString(R.string.china_currency), getString(R.string.china)),
                new Currency("currency_flag_croatia", "HRK", getString(R.string.croatia_currency), getString(R.string.croatia)),
                new Currency("currency_flag_denmark", "DKK", getString(R.string.denmark_currency), getString(R.string.denmark)),
//                new Currency("currency_flag_indonesia", "IDR", getString(R.string.indonesia_currency), getString(R.string.indonesia)),
                new Currency("currency_flag_malaysia", "MYR", getString(R.string.malaysia_currency), getString(R.string.malaysia)),
                new Currency("currency_flag_philippines", "PHP", getString(R.string.philippines_currency), getString(R.string.philippines)),
                new Currency("currency_flag_thailand", "THB", getString(R.string.thailand_currency), getString(R.string.thailand)),
                new Currency("currency_flag_south_africa", "ZAR", getString(R.string.south_africa_currency), getString(R.string.south_africa))};
        for (Currency currency : currencies) {
            currencyList.add(currency);
        }

        getData(currencyList);
    }

    public void setCurrencyListRussia() {
        dbHelper = new DBHelper(this, DBVERSION);
        db = dbHelper.getWritableDatabase();
        Currency[] currencies = {new Currency("currency_flag_europe", "EUR", getString(R.string.europe_currency), getString(R.string.europe)),
                new Currency("currency_flag_usa", "USD", getString(R.string.usa_currency), getString(R.string.usa)),
                new Currency("currency_flag_kazakhstan", "KZT", getString(R.string.kazakhstan_currency), getString(R.string.kazakhstan)),
                new Currency("currency_flag_azerbaijan", "AZN", getString(R.string.azerbaijan_currency), getString(R.string.azerbaijan)),
                new Currency("currency_flag_hungary","HUF", getString(R.string.hungary_currency), getString(R.string.hungary)),
                new Currency("currency_flag_belarus", "BYN", getString(R.string.belarus_currency), getString(R.string.belarus)),
                new Currency("currency_flag_armenia", "AMD", getString(R.string.armenia_currency), getString(R.string.armenia)),
                new Currency("currency_flag_turkey", "TRY", getString(R.string.turkey_currency), getString(R.string.turkey)),
                new Currency("currency_flag_ukraine", "UAH", getString(R.string.ukraine_currency), getString(R.string.ukraine)),
                new Currency("currency_flag_poland", "PLN", getString(R.string.poland_currency), getString(R.string.poland)),
                new Currency("currency_flag_great_britain", "GBP", getString(R.string.greatbritain_currency), getString(R.string.greatbritain)),
                new Currency("currency_flag_japan", "JPY", getString(R.string.japan_currency), getString(R.string.japan)),
                new Currency("currency_flag_india", "INR", getString(R.string.india_currency), getString(R.string.india)),
                new Currency("currency_flag_korea", "KRW", getString(R.string.south_korea_currency), getString(R.string.south_korea)),
                new Currency("currency_flag_norway", "NOK", getString(R.string.norway_currency), getString(R.string.norway)),
                new Currency("currency_flag_romania", "RON", getString(R.string.romania_currency), getString(R.string.romania)),
                new Currency("currency_flag_singapore", "SGD", getString(R.string.singapore_currency), getString(R.string.singapore)),
                new Currency("currency_flag_sweden", "SEK", getString(R.string.sweden_currency), getString(R.string.sweden)),
                new Currency("currency_flag_switzerland", "CHF", getString(R.string.switzerland_currency), getString(R.string.switzerland)),
                new Currency("currency_flag_brazil", "BRL", getString(R.string.brazil_currency), getString(R.string.brazil)),
                new Currency("currency_flag_australia", "AUD", getString(R.string.australia_currency), getString(R.string.australia)),
                new Currency("currency_flag_bulgaria", "BGN", getString(R.string.bulgary_currency), getString(R.string.bulgary)),
                new Currency("currency_flag_canada", "CAD", getString(R.string.canada_currency), getString(R.string.canada)),
                new Currency("currency_flag_china", "CNY", getString(R.string.china_currency), getString(R.string.china)),
                new Currency("currency_flag_czech", "CZK", getString(R.string.czech_currency), getString(R.string.czech)),
                new Currency("currency_flag_denmark", "DKK", getString(R.string.denmark_currency), getString(R.string.denmark)),
                new Currency("currency_flag_kyrgystan", "KGS", getString(R.string.kyrgyzstan_currency), getString(R.string.kyrgyzstan)),
                new Currency("currency_flag_moldova", "MDL", getString(R.string.moldova_currency), getString(R.string.moldova)),
                new Currency("currency_flag_tajikistan", "TJS", getString(R.string.tajikistan_currency), getString(R.string.tajikistan)),
                new Currency("currency_flag_turkmenistan", "TMT", getString(R.string.turkmenistan_currency), getString(R.string.turkmenistan)),
                new Currency("currency_flag_uzbekistan", "UZS", getString(R.string.uzbekistan_currency), getString(R.string.uzbekistan)),
                new Currency("currency_flag_south_africa", "ZAR", getString(R.string.south_africa_currency), getString(R.string.south_africa))};
        for (Currency currency : currencies) {
            currencyList.add(currency);
        }

        getData(currencyList);
    }

    public void setCurrencyListUkraine() {
        dbHelper = new DBHelper(this, DBVERSION);
        db = dbHelper.getWritableDatabase();
        Currency[] currencies = {new Currency("currency_flag_great_britain", "GBP", getString(R.string.greatbritain_currency), getString(R.string.greatbritain)),
                new Currency("currency_flag_usa", "USD", getString(R.string.usa_currency), getString(R.string.usa)),
                new Currency("currency_flag_europe", "EUR", getString(R.string.europe_currency), getString(R.string.europe)),
                new Currency("currency_flag_poland", "PLN", getString(R.string.poland_currency), getString(R.string.poland)),
                new Currency("currency_flag_russia", "RUB", getString(R.string.russia_currency), getString(R.string.russia)),
                new Currency("currency_flag_belarus", "BYN", getString(R.string.belarus_currency), getString(R.string.belarus)),
                new Currency("currency_flag_sweden", "SEK", getString(R.string.sweden_currency), getString(R.string.sweden)),
                new Currency("currency_flag_switzerland", "CHF", getString(R.string.switzerland_currency), getString(R.string.switzerland)),
                new Currency("currency_flag_syria", "SYP", getString(R.string.syria_currency), getString(R.string.syria)),
                new Currency("currency_flag_turkey", "TRY", getString(R.string.turkey_currency), getString(R.string.turkey)),
                new Currency("currency_flag_hongong", "HKD", getString(R.string.hong_kong_currency), getString(R.string.hong_kong)),
                new Currency("currency_flag_hungary","HUF", getString(R.string.hungary_currency), getString(R.string.hungary)),
                new Currency("currency_flag_island", "ISK", getString(R.string.island_currency), getString(R.string.island)),
                new Currency("currency_flag_india", "INR", getString(R.string.india_currency), getString(R.string.india)),
                new Currency("currency_flag_irak", "IQD", getString(R.string.irak_currency), getString(R.string.irak)),
                new Currency("currency_flag_israel", "ILS", getString(R.string.israel_currency), getString(R.string.israel)),
                new Currency("currency_flag_georgia", "GEL", getString(R.string.georgia_currency), getString(R.string.georgia)),
                new Currency("currency_flag_japan", "JPY", getString(R.string.japan_currency), getString(R.string.japan)),
                new Currency("currency_flag_kazakhstan", "KZT", getString(R.string.kazakhstan_currency), getString(R.string.kazakhstan)),
                new Currency("currency_flag_korea", "KRW", getString(R.string.south_korea_currency), getString(R.string.south_korea)),
                new Currency("currency_flag_kuwait", "KWD", getString(R.string.kuwait_currency), getString(R.string.kuwait)),
                new Currency("currency_flag_kyrgystan", "KGS", getString(R.string.kyrgyzstan_currency), getString(R.string.kyrgyzstan)),
                new Currency("currency_flag_lebanon", "LBP", getString(R.string.lebanon_currency), getString(R.string.lebanon)),
                new Currency("currency_flag_libya", "LYD", getString(R.string.lebanon_currency), getString(R.string.libya)),
                new Currency("currency_flag_mexico", "MXN", getString(R.string.mexico_currency), getString(R.string.mexico)),
                new Currency("currency_flag_mongolia", "MNT", getString(R.string.mongolia_currency), getString(R.string.mongolia)),
                new Currency("currency_flag_moldova", "MDL", getString(R.string.moldova_currency), getString(R.string.moldova)),
                new Currency("currency_flag_new_zealand", "NZD", getString(R.string.new_zealand_currency), getString(R.string.new_zealand)),
                new Currency("currency_flag_norway", "NOK", getString(R.string.norway_currency), getString(R.string.norway)),
                new Currency("currency_flag_pakistan", "PKR", getString(R.string.pakistan_currency), getString(R.string.pakistan)),
                new Currency("currency_flag_peru", "PEN", getString(R.string.peru_currency), getString(R.string.peru)),
                new Currency("currency_flag_romania", "RON", getString(R.string.romania_currency), getString(R.string.romania)),
                new Currency("currency_flag_saudi_arabia", "SAR", getString(R.string.saudi_arabia_currency), getString(R.string.saudi_arabia)),
                new Currency("currency_flag_singapore", "SGD", getString(R.string.singapore_currency), getString(R.string.singapore)),
                new Currency("currency_flag_turkmenistan", "TMT", getString(R.string.turkmenistan_currency), getString(R.string.turkmenistan)),
                new Currency("currency_flag_egypt", "EGP", getString(R.string.egypt_currency), getString(R.string.egypt)),
                new Currency("currency_flag_uzbekistan", "UZS", getString(R.string.uzbekistan_currency), getString(R.string.uzbekistan)),
                new Currency("currency_flag_taiwan", "TWD", getString(R.string.taiwan_currency), getString(R.string.taiwan)),
                new Currency("currency_flag_western_africa", "XOF", getString(R.string.western_africa_currency), getString(R.string.western_africa)),
                new Currency("currency_flag_brazil", "BRL", getString(R.string.brazil_currency), getString(R.string.brazil)),
                new Currency("currency_flag_tajikistan", "TJS", getString(R.string.tajikistan_currency), getString(R.string.tajikistan)),
                new Currency("currency_flag_azerbaijan", "AZN", getString(R.string.azerbaijan_currency), getString(R.string.azerbaijan)),
                new Currency("currency_flag_australia", "AUD", getString(R.string.australia_currency), getString(R.string.australia)),
                new Currency("currency_flag_armenia", "AMD", getString(R.string.armenia_currency), getString(R.string.armenia)),
                new Currency("currency_flag_bulgaria", "BGN", getString(R.string.bulgary_currency), getString(R.string.bulgary)),
                new Currency("currency_flag_canada", "CAD", getString(R.string.canada_currency), getString(R.string.canada)),
                new Currency("currency_flag_chile", "CLP", getString(R.string.chile_currency), getString(R.string.chile)),
                new Currency("currency_flag_china", "CNY", getString(R.string.china_currency), getString(R.string.china)),
                new Currency("currency_flag_croatia", "HRK", getString(R.string.croatia_currency), getString(R.string.croatia)),
                new Currency("currency_flag_czech", "CZK", getString(R.string.czech_currency), getString(R.string.czech)),
                new Currency("currency_flag_denmark", "DKK", getString(R.string.denmark_currency), getString(R.string.denmark)),
                new Currency("currency_flag_gold", "XAU", getString(R.string.gold_currency), getString(R.string.gold)),
                new Currency("currency_flag_silver", "XAG", getString(R.string.silver_currency), getString(R.string.silver)),
                new Currency("currency_flag_platinum", "XPT", getString(R.string.platinum), getString(R.string.platinum)),
                new Currency("currency_flag_paladium", "XPD", getString(R.string.paladium), getString(R.string.paladium))};
        for (Currency currency : currencies) {
            currencyList.add(currency);
        }

        getData(currencyList);

    }

    public void getData(ArrayList<Currency> currencyList) {
        Cursor c = db.query(Utils.getOfficialCurrencyTableName(), null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int nameIndex = c.getColumnIndex("name");
            int rateIndex = c.getColumnIndex("rate");
            int dateIndex = c.getColumnIndex("date");

            do {
                for (int i = 0; i < currencyList.size(); i++) {
                    if (currencyList.get(i).getName().equals(c.getString(nameIndex))) {
                        currencyList.get(i).setNewInformation(c.getDouble(rateIndex), c.getString(dateIndex));
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
}
