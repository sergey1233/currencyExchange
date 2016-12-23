package com.sergey.currencyexchange.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.Currency;
import com.sergey.currencyexchange.model.SelectSpinnerAdapter;
import com.sergey.currencyexchange.model.Utils;

import java.util.ArrayList;
import java.util.Arrays;


public class SelectConvertCurrency extends AppCompatActivity {

    private AdView mAdView;
    private static final String TAG = "SelectConvertCurrency";

    private static final int TYPEACTIVITY = Utils.SELECTCONVERTCURRENCYTYPE;
    private int fromCurrency;
    private int toCurrency;
    private Button buttonSelectSpinner;
    private Spinner selectFromSpinner;
    private Spinner selectToSpinner;
    private ArrayList<Currency> currencyArray = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_convert_currency);

        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        setCurrencyArray();

        selectFromSpinner = (Spinner)findViewById(R.id.select_from_spinner);
        selectToSpinner = (Spinner)findViewById(R.id.select_to_spinner);
        final SelectSpinnerAdapter selectSpinnerAdapter = new SelectSpinnerAdapter(SelectConvertCurrency.this, currencyArray);
        selectFromSpinner.setAdapter(selectSpinnerAdapter);
        selectToSpinner.setAdapter(selectSpinnerAdapter);

        setSpinnerSelection();

        selectFromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectSpinnerAdapter.notifyDataSetChanged();
                if (i == currencyArray.size()) {
                    fromCurrency = currencyArray.get(i-1).getCurrencyId();
                }
                else {
                    fromCurrency = currencyArray.get(i).getCurrencyId();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });
        selectToSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectSpinnerAdapter.notifyDataSetChanged();
                if (i == currencyArray.size()) {
                    toCurrency = currencyArray.get(i-1).getCurrencyId();
                }
                else {
                    toCurrency = currencyArray.get(i).getCurrencyId();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });

        buttonSelectSpinner = (Button)findViewById(R.id.spinner_ok);
        buttonSelectSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectConvertCurrency.this, Converter.class);
                intent.putExtra("fromActivity", TYPEACTIVITY);
                intent.putExtra("fromCurrency", fromCurrency);
                intent.putExtra("toCurrency", toCurrency);
                startActivity(intent);
            }
        });
    }

    public void setCurrencyArray() {
        switch (Utils.country_code) {
            case Utils.UK_CODE:
                setSelectCurrencySpinnerConvListUk();
                break;
            case Utils.POLAND_CODE:
                setSelectCurrencySpinnerConvListPoland();
                break;
            case Utils.RUSSIA_CODE:
                setSelectCurrencySpinnerConvListRussia();
                break;
            case Utils.UKRAINE_CODE:
                setSelectCurrencySpinnerConvListUkraine();
                break;
            default:
                setSelectCurrencySpinnerConvListUk();
                break;
        }
    }

    public void setSelectCurrencySpinnerConvListUk() {
        currencyArray.addAll(Arrays.asList(new Currency("currency_flag_usa", "icon_dollar_dark", "USD", Utils.usd, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_europe", "icon_euro_dark", "EUR", Utils.eur, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_russia", "icon_rb_dark", "RUB", Utils.rb, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_great_britain", "icon_gbp_dark", "GBP", Utils.gbp, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_switzerland", "icon_chf_dark", "CHF", Utils.chf, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_turkey", "icon_tyr_dark", "TYR", Utils.tyr, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_canada", "icon_cad_dark", "CAD", Utils.cad, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_poland", "icon_pln_dark", "PLN", Utils.pln, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_israel", "icon_ils_dark", "ILS", Utils.ils, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_china", "icon_cny_dark", "CNY", Utils.cny, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_czech", "icon_czk_dark", "CZK", Utils.czk, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_sweden", "icon_sek_dark", "SEK", Utils.sek, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_japan", "icon_jpy_dark", "JPY", Utils.jpy, Utils.SELECTCONVERTCURRENCYTYPE)));
    }

    public void setSelectCurrencySpinnerConvListPoland() {
        currencyArray.addAll(Arrays.asList(new Currency("currency_flag_usa", "icon_dollar_dark", "USD", Utils.usd, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_europe", "icon_euro_dark", "EUR", Utils.eur, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_great_britain", "icon_gbp_dark", "GBP", Utils.gbp, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_switzerland", "icon_chf_dark", "CHF", Utils.chf, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_poland", "icon_pln_dark", "PLN", Utils.pln, Utils.SELECTCONVERTCURRENCYTYPE)));
    }

    public void setSelectCurrencySpinnerConvListRussia() {
        currencyArray.addAll(Arrays.asList(new Currency("currency_flag_usa", "icon_dollar_dark", "USD", Utils.usd, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_europe", "icon_euro_dark", "EUR", Utils.eur, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_russia", "icon_rb_dark", "RUB", Utils.rb, Utils.SELECTCONVERTCURRENCYTYPE)));
    }

    public void setSelectCurrencySpinnerConvListUkraine() {
        currencyArray.addAll(Arrays.asList(new Currency("currency_flag_usa", "icon_dollar_dark", "USD", Utils.usd, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_europe", "icon_euro_dark", "EUR", Utils.eur, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_ukraine", "icon_grn_dark", "UAH", Utils.uah, Utils.SELECTCONVERTCURRENCYTYPE),
                new Currency("currency_flag_russia", "icon_rb_dark", "RUB", Utils.rb, Utils.SELECTCONVERTCURRENCYTYPE)));
    }

    public void setSpinnerSelection() {
        fromCurrency = getIntent().getIntExtra("fromCurrency", 0);
        toCurrency = getIntent().getIntExtra("toCurrency", 0);

        selectFromSpinner.setSelection(getFromArrayCurrencyIndex(fromCurrency));
        selectToSpinner.setSelection(getFromArrayCurrencyIndex(toCurrency));
    }

    public int getFromArrayCurrencyIndex(int currencyId) {
        int index = 0;
        int i = 0;
        for (Currency currency : currencyArray) {
            if (currency.getCurrencyId() == currencyId) {
                index = i;
                break;
            }
            i++;
        }

        return index;
    }
}

