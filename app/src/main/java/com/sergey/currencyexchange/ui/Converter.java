package com.sergey.currencyexchange.ui;

import android.content.Context;
import android.content.Intent;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.ApplicationInfo;
import com.sergey.currencyexchange.model.Country;
import com.sergey.currencyexchange.model.Utils;
import com.sergey.currencyexchange.ui.fragment.SelectCountryFragment;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class Converter extends AppCompatActivity {

    InterstitialAd mInterstitialAd;
    private static final String TAG = "Converter:";
    private static final int TYPEACTIVITY = Utils.CONVERTERTYPE;

    private ImageButton mainToolBarImage;
    private ImageButton nbuCurrencyToolbarImage;
    private ImageButton converterToolBarImage;
    private ImageButton iconCurrencyToolbar;
    private ImageButton iconMenu;
    private ImageButton flagFrom;
    private ImageButton flagTo;
    private ImageView currencyFromIcon;
    private ImageView currencyToIcon;
    private EditText countFromEdit;
    private EditText currencyExchangeEdit;
    private TextView countToResult;
    private ImageButton set_own_currency;
    private LinearLayout own_currecny_linear;
    private ImageButton arrow;

    private double countFrom = 0;
    private double currencyExchange = 0;

    private TextView officialCurrencyView;
    private TextView officialSumView;
    private TextView mBankCurrencyView;
    private TextView mBankSumView;
    private TextView blackMarketCurrencyView;
    private TextView blackMarketSumView;
    private RecyclerView recyclerView;
    private BankListAdapterConv adapter;
    private Country country;


    private double[] officialRates;
    private double[] mBankRates;
    private double[] blackMarketRates;

    private int fromCurrency = Utils.usd;
    private int toCurrency = Utils.uah;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converter);

        initializeAdvertisment();
        requestNewInterstitial();

        initializeVariables();
        setClickEvent();
    }

    public void initializeAdvertisment() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.banner_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                Intent intent = new Intent(Converter.this, CBankRates.class);
                startActivity(intent);
            }
        });
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public void initializeVariables() {
        mainToolBarImage = (ImageButton)findViewById(R.id.icon_main_toolbar);
        nbuCurrencyToolbarImage = (ImageButton)findViewById(R.id.icon_nbu_currency_toolbar);
        converterToolBarImage = (ImageButton)findViewById(R.id.icon_converter_toolbar);
        iconCurrencyToolbar = (ImageButton)findViewById(R.id.icon_currency_toolbar);
        iconMenu = (ImageButton)findViewById(R.id.icon_menu);
        flagFrom = (ImageButton)findViewById(R.id.flag_from);
        flagTo = (ImageButton)findViewById(R.id.flag_to);
        arrow = (ImageButton)findViewById(R.id.arrow);
        currencyFromIcon = (ImageView)findViewById(R.id.currency_from);
        currencyToIcon = (ImageView)findViewById(R.id.currency_to);
        countFromEdit = (EditText)findViewById(R.id.count_from_edittext);
        currencyExchangeEdit = (EditText)findViewById(R.id.currency_exchange_edittext);
        countToResult = (TextView)findViewById(R.id.count_to_result);
        set_own_currency = (ImageButton)findViewById(R.id.set_own_currency);
        own_currecny_linear = (LinearLayout)findViewById(R.id.own_currency_linear);

        int typeAct = getIntent().getIntExtra("fromActivity", 0);
        setStartCurrencies(typeAct);

        setImages();
        getData();


        //set difference view on the botton of convert activity view
        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        if (Utils.country_code == Utils.UKRAINE_CODE) {
            stub.setLayoutResource(R.layout.converter_bottom_ukraine);
        }
        else {
            stub.setLayoutResource(R.layout.converter_bottom);
        }
        stub.inflate();
        //After stub.inflate because this textview is in R.layout.converter_bottom
        if (Utils.country_code == Utils.RUSSIA_CODE) {
            TextView titleBlockBanks = (TextView)findViewById(R.id.title_block_banks);
            titleBlockBanks.setText(R.string.banksRussia);
        }
        else if (Utils.country_code == Utils.POLAND_CODE) {
            TextView titleBlockBanks = (TextView)findViewById(R.id.title_block_banks);
            titleBlockBanks.setText(R.string.banksPoland);
        }

        officialCurrencyView = (TextView)findViewById(R.id.nbu_currency);
        officialSumView = (TextView)findViewById(R.id.nbu_sum);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewBanks);

        officialCurrencyView.setText(String.format("%.4f", Utils.roundResut(officialRates[0])) + "  |  " + String.format("%.4f", Utils.roundResut(officialRates[1])));
        officialSumView.setText("0");
        if (Utils.country_code == Utils.UKRAINE_CODE) {
            mBankCurrencyView = (TextView)findViewById(R.id.mBank_currency);
            mBankSumView = (TextView)findViewById(R.id.mBank_sum);
            blackMarketCurrencyView = (TextView)findViewById(R.id.blackMarket_currency);
            blackMarketSumView = (TextView)findViewById(R.id.blackMarket_sum);

            mBankCurrencyView.setText(String.format("%.4f", Utils.roundResut(mBankRates[0])) + "  |  " + String.format("%.4f", Utils.roundResut(mBankRates[1])));
            mBankSumView.setText("0");
            blackMarketCurrencyView.setText(String.format("%.4f", Utils.roundResut(blackMarketRates[0])) + "  |  " + String.format("%.4f", Utils.roundResut(blackMarketRates[1])));
            blackMarketSumView.setText("0");
        }

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        adapter = new BankListAdapterConv(this, fromCurrency, toCurrency);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);

    }

    public void setStartCurrencies(int typeAct) {
        if (typeAct == Utils.MAINACTIVITYTYPE || typeAct == Utils.CBANKRATETYPE) {
            switch (Utils.country_code) {
                case Utils.UK_CODE:
                    fromCurrency = Utils.gbp;
                    toCurrency = Utils.usd;
                    break;
                case Utils.POLAND_CODE:
                    fromCurrency = Utils.usd;
                    toCurrency = Utils.pln;
                    break;
                case Utils.RUSSIA_CODE:
                    fromCurrency = Utils.usd;
                    toCurrency = Utils.rb;
                    break;
                case Utils.UKRAINE_CODE:
                    fromCurrency = Utils.usd;
                    toCurrency = Utils.uah;
                    break;
                default:
                    fromCurrency = Utils.usd;
                    toCurrency = Utils.eur;
                    break;
            }
        }
        else if (typeAct == Utils.SELECTCONVERTCURRENCYTYPE) {
            fromCurrency = getIntent().getIntExtra("fromCurrency", 0);
            toCurrency = getIntent().getIntExtra("toCurrency", 0);

        }
    }

    public void getData() {
        country = ApplicationInfo.getInstance().getCountry();
        officialRates = country.getCBank().getRatesConv(fromCurrency, toCurrency);
        if (Utils.country_code == Utils.UKRAINE_CODE) {
            mBankRates = country.getMBank().getRatesConv(fromCurrency, toCurrency);
            blackMarketRates = country.getBlackMarket().getRatesConv(fromCurrency, toCurrency);
        }
    }

    public void setClickEvent() {
        countFromEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (Utils.isDigit(editable.toString())) {
                    double count = Double.parseDouble(editable.toString());
                    resultOutput(count);
                }
                else {
                    Toast.makeText(Converter.this, getText(R.string.exception_count), Toast.LENGTH_SHORT).show();
                }
            }
        });

        currencyExchangeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (Utils.isDigit(editable.toString())) {
                    double currency = Double.parseDouble(editable.toString());
                    resultOutputCurrency(currency);
                }
                else {
                    Toast.makeText(Converter.this, getText(R.string.exception_count), Toast.LENGTH_SHORT).show();
                }
            }
        });

        set_own_currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (own_currecny_linear.getVisibility() == View.GONE) {
                    own_currecny_linear.setVisibility(View.VISIBLE);
                }
                else {
                    currencyExchangeEdit.setText("0");
                    own_currecny_linear.setVisibility(View.GONE);
                }
            }
        });

        mainToolBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Converter.this, MainActivity.class);
                intent.putExtra("fromActivity", TYPEACTIVITY);
                startActivity(intent);
            }
        });

        nbuCurrencyToolbarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Intent intent = new Intent(Converter.this, CBankRates.class);
                    startActivity(intent);
                }
            }
        });

        iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopupMenu(view);
            }
        });

        final Intent selectConvertCurrencyIntent = new Intent(Converter.this, SelectConvertCurrency.class);
        selectConvertCurrencyIntent.putExtra("fromCurrency", fromCurrency);
        selectConvertCurrencyIntent.putExtra("toCurrency", toCurrency);
        iconCurrencyToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(selectConvertCurrencyIntent);
            }
        });
        flagFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(selectConvertCurrencyIntent);
            }
        });
        flagTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(selectConvertCurrencyIntent);
            }
        });
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(selectConvertCurrencyIntent);
            }
        });
    }

    public void showpopupMenu(View view) {
        PopupMenu popup = new PopupMenu(Converter.this, view);
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

    public void setImages() {
        set_own_currency.setImageResource(R.drawable.button_own_currency);
        converterToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
        setFromCurrencyElements();
        setToCurrencyElements();

    }

    public void setFromCurrencyElements() {
        switch (fromCurrency) {
            case Utils.uah:
                flagFrom.setBackgroundResource(R.drawable.currency_flag_ukraine);
                currencyFromIcon.setImageResource(R.drawable.icon_grn_dark);
                iconCurrencyToolbar.setImageResource(R.drawable.icon_grn_dark);
                break;
            case Utils.gbp:
                flagFrom.setBackgroundResource(R.drawable.currency_flag_great_britain);
                currencyFromIcon.setImageResource(R.drawable.icon_gbp_dark);
                iconCurrencyToolbar.setImageResource(R.drawable.icon_gbp_dark);
                break;
            case Utils.usd:
                flagFrom.setBackgroundResource(R.drawable.currency_flag_usa);
                currencyFromIcon.setImageResource(R.drawable.icon_dollar_dark);
                iconCurrencyToolbar.setImageResource(R.drawable.icon_dollar_dark);
                break;
            case Utils.eur:
                flagFrom.setBackgroundResource(R.drawable.currency_flag_europe);
                currencyFromIcon.setImageResource(R.drawable.icon_euro_dark);
                iconCurrencyToolbar.setImageResource(R.drawable.icon_euro_dark);
                break;
            case Utils.rb:
                flagFrom.setBackgroundResource(R.drawable.currency_flag_russia);
                currencyFromIcon.setImageResource(R.drawable.icon_rb_dark);
                iconCurrencyToolbar.setImageResource(R.drawable.icon_rb_dark);
                break;
            case Utils.cad:
                flagFrom.setBackgroundResource(R.drawable.currency_flag_canada);
                currencyFromIcon.setImageResource(R.drawable.icon_cad_dark);
                iconCurrencyToolbar.setImageResource(R.drawable.icon_cad_dark);
                break;
            case Utils.tyr:
                flagFrom.setBackgroundResource(R.drawable.currency_flag_turkey);
                currencyFromIcon.setImageResource(R.drawable.icon_tyr_dark);
                iconCurrencyToolbar.setImageResource(R.drawable.icon_tyr_dark);
                break;
            case Utils.ils:
                flagFrom.setBackgroundResource(R.drawable.currency_flag_israel);
                currencyFromIcon.setImageResource(R.drawable.icon_ils_dark);
                iconCurrencyToolbar.setImageResource(R.drawable.icon_ils_dark);
                break;
            case Utils.pln:
                flagFrom.setBackgroundResource(R.drawable.currency_flag_poland);
                currencyFromIcon.setImageResource(R.drawable.icon_pln_dark);
                iconCurrencyToolbar.setImageResource(R.drawable.icon_pln_dark);
                break;
            case Utils.cny:
                flagFrom.setBackgroundResource(R.drawable.currency_flag_china);
                currencyFromIcon.setImageResource(R.drawable.icon_cny_dark);
                iconCurrencyToolbar.setImageResource(R.drawable.icon_cny_dark);
                break;
            case Utils.czk:
                flagFrom.setBackgroundResource(R.drawable.currency_flag_czech);
                currencyFromIcon.setImageResource(R.drawable.icon_czk_dark);
                iconCurrencyToolbar.setImageResource(R.drawable.icon_czk_dark);
                break;
            case Utils.sek:
                flagFrom.setBackgroundResource(R.drawable.currency_flag_sweden);
                currencyFromIcon.setImageResource(R.drawable.icon_sek_dark);
                iconCurrencyToolbar.setImageResource(R.drawable.icon_sek_dark);
                break;
            case Utils.chf:
                flagFrom.setBackgroundResource(R.drawable.currency_flag_switzerland);
                currencyFromIcon.setImageResource(R.drawable.icon_chf_dark);
                iconCurrencyToolbar.setImageResource(R.drawable.icon_chf_dark);
                break;
            case Utils.jpy:
                flagFrom.setBackgroundResource(R.drawable.currency_flag_japan);
                currencyFromIcon.setImageResource(R.drawable.icon_jpy_dark);
                iconCurrencyToolbar.setImageResource(R.drawable.icon_jpy_dark);
                break;
        }
    }

    public void setToCurrencyElements() {
        switch (toCurrency) {
            case Utils.uah:
                flagTo.setBackgroundResource(R.drawable.currency_flag_ukraine);
                currencyToIcon.setImageResource(R.drawable.icon_grn_dark);
                break;
            case Utils.gbp:
                flagTo.setBackgroundResource(R.drawable.currency_flag_great_britain);
                currencyToIcon.setImageResource(R.drawable.icon_gbp_dark);
                break;
            case Utils.usd:
                flagTo.setBackgroundResource(R.drawable.currency_flag_usa);
                currencyToIcon.setImageResource(R.drawable.icon_dollar_dark);
                break;
            case Utils.eur:
                flagTo.setBackgroundResource(R.drawable.currency_flag_europe);
                currencyToIcon.setImageResource(R.drawable.icon_euro_dark);
                break;
            case Utils.rb:
                flagTo.setBackgroundResource(R.drawable.currency_flag_russia);
                currencyToIcon.setImageResource(R.drawable.icon_rb_dark);
                break;
            case Utils.cad:
                flagTo.setBackgroundResource(R.drawable.currency_flag_canada);
                currencyToIcon.setImageResource(R.drawable.icon_cad_dark);
                break;
            case Utils.tyr:
                flagTo.setBackgroundResource(R.drawable.currency_flag_turkey);
                currencyToIcon.setImageResource(R.drawable.icon_tyr_dark);
                break;
            case Utils.ils:
                flagTo.setBackgroundResource(R.drawable.currency_flag_israel);
                currencyToIcon.setImageResource(R.drawable.icon_ils_dark);
                break;
            case Utils.pln:
                flagTo.setBackgroundResource(R.drawable.currency_flag_poland);
                currencyToIcon.setImageResource(R.drawable.icon_pln_dark);
                break;
            case Utils.cny:
                flagTo.setBackgroundResource(R.drawable.currency_flag_china);
                currencyToIcon.setImageResource(R.drawable.icon_cny_dark);
                break;
            case Utils.czk:
                flagTo.setBackgroundResource(R.drawable.currency_flag_czech);
                currencyToIcon.setImageResource(R.drawable.icon_czk_dark);
                break;
            case Utils.sek:
                flagTo.setBackgroundResource(R.drawable.currency_flag_sweden);
                currencyToIcon.setImageResource(R.drawable.icon_sek_dark);
                break;
            case Utils.chf:
                flagTo.setBackgroundResource(R.drawable.currency_flag_switzerland);
                currencyToIcon.setImageResource(R.drawable.icon_chf_dark);
                break;
            case Utils.jpy:
                flagTo.setBackgroundResource(R.drawable.currency_flag_japan);
                currencyToIcon.setImageResource(R.drawable.icon_jpy_dark);
                break;
        }
    }

    public BigDecimal getResult(double countFrom, double currencyExchange) {
        BigDecimal counFromBig = new BigDecimal(countFrom);
        BigDecimal currencyExchangeBig = new BigDecimal(currencyExchange);
        BigDecimal result = counFromBig.multiply(currencyExchangeBig);
        return result;
    }

    public void resultOutput(Double count) {
        if (countFrom != count) {
            countFrom = count;
            setSumInfo(countFrom, officialRates, mBankRates, blackMarketRates);
        }
        if (own_currecny_linear.getVisibility() == View.VISIBLE) {
            if (getResult(countFrom, currencyExchange).doubleValue() < 999999999) {
                countToResult.setText(String.format("%.2f", getResult(countFrom, currencyExchange)));
            }
            else {
                Toast.makeText(Converter.this, getText(R.string.too_big), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setSumInfo(double count, double[] officialRates, double[] mBankRates, double[] blackMarketRates) {
        officialSumView.setText(resultSum(count, officialRates));
        if (Utils.country_code == Utils.UKRAINE_CODE) {
            mBankSumView.setText(resultSum(count, mBankRates));
            blackMarketSumView.setText(resultSum(count, blackMarketRates));
        }
        refreshAdapter(count);
    }

    public String resultSum(double count, double[] currencies) {
        BigDecimal countBig = new BigDecimal(String.valueOf(count));
        BigDecimal rateFrom;
        BigDecimal rateTo;
        //Because in UK buy and sell have different meaning than in Ukraine and rates are not from pound(like grn), but from another currency
        if (Utils.country_code == Utils.UK_CODE) {
            rateFrom = new BigDecimal(1).divide(new BigDecimal(String.valueOf(currencies[0])), 5, RoundingMode.HALF_UP);
            rateTo = new BigDecimal(1).divide(new BigDecimal(String.valueOf(currencies[1])), 5, RoundingMode.HALF_UP);
        }
        else {
            rateFrom = new BigDecimal(String.valueOf(currencies[0]));
            rateTo = (new BigDecimal(String.valueOf(currencies[1])));
        }

        BigDecimal result;
        try {
            result = (countBig.multiply(rateFrom).divide(rateTo, 5, RoundingMode.HALF_UP));
        }
        catch (ArithmeticException e){
            result = new BigDecimal(0);
        }

        return String.valueOf(result.setScale(2, RoundingMode.HALF_UP));
    }

    public void refreshAdapter(double count) {
        adapter.refreshInfo(count, fromCurrency, toCurrency);
        adapter.notifyDataSetChanged();
    }

    public void resultOutputCurrency(Double currency) {
        currencyExchange = currency;
        if (getResult(countFrom, currencyExchange).doubleValue() < 999999999) {
            countToResult.setText(String.format("%.2f", getResult(countFrom, currencyExchange)));
        }
        else {
            Toast.makeText(Converter.this, getText(R.string.too_big), Toast.LENGTH_SHORT).show();
        }
    }
}
