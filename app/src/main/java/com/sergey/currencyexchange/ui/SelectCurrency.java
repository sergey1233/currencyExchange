package com.sergey.currencyexchange.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.ApplicationInfo;


public class SelectCurrency extends AppCompatActivity {

    private static final String TAG = "SelectCurrency:";
    private final static int TYPEACTIVITY = 2;
    private MaterialRippleLayout rippleLayoutUsd;
    private MaterialRippleLayout rippleLayoutEur;
    private MaterialRippleLayout rippleLayoutRub;

    private static final int DOLLAR = 0;
    private static final int EURO = 1;
    private static final int RB = 2;

    private Intent intent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_currency);
        rippleLayoutUsd = (MaterialRippleLayout)findViewById(R.id.ripple_view_usd);
        rippleLayoutEur = (MaterialRippleLayout)findViewById(R.id.ripple_view_eur);
        rippleLayoutRub = (MaterialRippleLayout)findViewById(R.id.ripple_view_rub);

        int typeAct = getIntent().getIntExtra("fromActivity", 0);
        switch(typeAct) {
            case 0:
                intent = new Intent(SelectCurrency.this, MainActivity.class);
                break;
            case 1:
                intent = new Intent(SelectCurrency.this, Converter.class);
                break;
        }
        intent.putExtra("fromActivity", TYPEACTIVITY);

        rippleLayoutUsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationInfo.getInstance().setCurrencyId(DOLLAR);
                startActivity(intent);
            }
        });

        rippleLayoutEur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationInfo.getInstance().setCurrencyId(EURO);
                startActivity(intent);
            }
        });

        rippleLayoutRub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationInfo.getInstance().setCurrencyId(RB);
                startActivity(intent);
            }
        });
    }
}
