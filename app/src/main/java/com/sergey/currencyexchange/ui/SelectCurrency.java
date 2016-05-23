package com.sergey.currencyexchange.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.ApplicationInfo;
import com.sergey.currencyexchange.model.Bank;
import com.sergey.currencyexchange.model.BlackMarket;
import com.sergey.currencyexchange.model.MBank;
import com.sergey.currencyexchange.model.Nbu;
import com.sergey.currencyexchange.ui.Converter;
import com.sergey.currencyexchange.ui.MainActivity;

import java.util.ArrayList;

/**
 * Created by Sergey on 17.05.2016.
 */
public class SelectCurrency extends AppCompatActivity {

    private static final String TAG = "SelectCurrencyt:";
    private MaterialRippleLayout rippleLayoutUsd;
    private MaterialRippleLayout rippleLayoutEur;
    private MaterialRippleLayout rippleLayoutRub;

    private static final int DOLLAR = 0;
    private static final int EURO = 1;
    private static final int RB = 2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_currency);
        rippleLayoutUsd = (MaterialRippleLayout)findViewById(R.id.ripple_view_usd);
        rippleLayoutEur = (MaterialRippleLayout)findViewById(R.id.ripple_view_eur);
        rippleLayoutRub = (MaterialRippleLayout)findViewById(R.id.ripple_view_rub);

        rippleLayoutUsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationInfo.getInstance().setCurrencyId(DOLLAR);
                onBackPressed();
            }
        });

        rippleLayoutEur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationInfo.getInstance().setCurrencyId(EURO);
                onBackPressed();
            }
        });

        rippleLayoutRub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationInfo.getInstance().setCurrencyId(RB);
                onBackPressed();
            }
        });


    }
}
