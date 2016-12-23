package com.sergey.currencyexchange.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.Currency;
import com.sergey.currencyexchange.model.Utils;
import java.util.ArrayList;
import java.util.Arrays;


public class SelectCurrency extends AppCompatActivity {

    private AdView mAdView;
    private static final String TAG = "SelectCurrency:";
    private final static int TYPEACTIVITY = Utils.SELECTCURRENCYTYPE;
    private RecyclerView recyclerViewSelectCurrency;
    private CurrencyListAdapter adapter;
    private ArrayList<Currency> selectCurrencyList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_currency);


        initializeAdmob();
        setSelectCurrencyList();

        recyclerViewSelectCurrency = (RecyclerView)findViewById(R.id.recycle_view_select_currency);
        recyclerViewSelectCurrency.setNestedScrollingEnabled(false);
        recyclerViewSelectCurrency.setFocusable(false);
        adapter = new CurrencyListAdapter(selectCurrencyList, this);
        recyclerViewSelectCurrency.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerViewSelectCurrency.setLayoutManager(layoutManager);
        recyclerViewSelectCurrency.setItemAnimator(itemAnimator);
    }

    public void initializeAdmob() {
        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void setSelectCurrencyList() {
        switch (Utils.country_code) {
            case Utils.UK_CODE:
                setSelectCurrencyListUk();
                break;
            case Utils.POLAND_CODE:
                setSelectCurrencyListPoland();
                break;
            case Utils.RUSSIA_CODE:
                setSelectCurrencyListRussia();
                break;
            case Utils.UKRAINE_CODE:
                setSelectCurrencyListUkraine();
                break;
            default:
                setSelectCurrencyListUk();
                break;
        }
    }

    public void setSelectCurrencyListUk() {
        selectCurrencyList.addAll(Arrays.asList(new Currency("currency_flag_usa", "icon_dollar_dark", getString(R.string.usa), Utils.usd),
            new Currency("currency_flag_europe", "icon_euro_dark", getString(R.string.europe), Utils.eur),
            new Currency("currency_flag_russia", "icon_rb_dark", getString(R.string.russia), Utils.rb),
            new Currency("currency_flag_canada", "icon_cad_dark", getString(R.string.canada), Utils.cad),
            new Currency("currency_flag_turkey", "icon_tyr_dark", getString(R.string.turkey), Utils.tyr),
            new Currency("currency_flag_poland", "icon_pln_dark", getString(R.string.poland), Utils.pln),
            new Currency("currency_flag_israel", "icon_ils_dark", getString(R.string.israel), Utils.ils),
            new Currency("currency_flag_china", "icon_cny_dark", getString(R.string.china), Utils.cny),
            new Currency("currency_flag_czech", "icon_czk_dark", getString(R.string.czech), Utils.czk),
            new Currency("currency_flag_sweden", "icon_sek_dark", getString(R.string.sweden), Utils.sek),
            new Currency("currency_flag_switzerland", "icon_chf_dark", getString(R.string.switzerland), Utils.chf),
            new Currency("currency_flag_japan", "icon_jpy_dark", getString(R.string.japan), Utils.jpy)));
    }

    public void setSelectCurrencyListPoland() {
        selectCurrencyList.addAll(Arrays.asList(new Currency("currency_flag_usa", "icon_dollar_dark", getString(R.string.usa), Utils.usd),
                new Currency("currency_flag_europe", "icon_euro_dark", getString(R.string.europe), Utils.eur),
                new Currency("currency_flag_great_britain", "icon_gbp_dark", getString(R.string.greatbritain), Utils.gbp),
                new Currency("currency_flag_switzerland", "icon_chf_dark", getString(R.string.switzerland), Utils.chf)));
    }

    public void setSelectCurrencyListRussia() {
        selectCurrencyList.addAll(Arrays.asList(new Currency("currency_flag_usa", "icon_dollar_dark", getString(R.string.usa), Utils.usd),
                new Currency("currency_flag_europe", "icon_euro_dark", getString(R.string.europe), Utils.eur)));
    }

    public void setSelectCurrencyListUkraine() {
        selectCurrencyList.addAll(Arrays.asList(new Currency("currency_flag_usa", "icon_dollar_dark", getString(R.string.usa), Utils.usd),
            new Currency("currency_flag_europe", "icon_euro_dark", getString(R.string.europe), Utils.eur),
            new Currency("currency_flag_russia", "icon_rb_dark", getString(R.string.russia), Utils.rb)));
    }
}
