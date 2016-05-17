package com.sergey.currencyexchange.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.BlackMarket;
import com.sergey.currencyexchange.model.MBank;
import com.sergey.currencyexchange.model.Nbu;
import com.sergey.currencyexchange.ui.Converter;
import com.sergey.currencyexchange.ui.MainActivity;

/**
 * Created by Sergey on 17.05.2016.
 */
public class SelectCurrencyFragment extends Fragment {

    private static final String TAG = "SelectCurrencyFragment:";
    private ImageButton mainToolBarImage;
    private ImageButton converterToolBarImage;
    private ImageButton iconCurrencyToolbar;
    private FragmentActivity activity;
    private int ACTID; // 0 - MainActivity; 1 - Converter;
    private ImageView flagFrom;
    private ImageView currencyFrom;

    private MaterialRippleLayout rippleLayoutUsd;
    private MaterialRippleLayout rippleLayoutEur;
    private MaterialRippleLayout rippleLayoutRub;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewFragmentSelectCurrency = inflater.inflate(R.layout.select_currency_fragment, null);

        activity = getActivity();
        ACTID = getArguments().getInt("fromActivity", 0);

        if (ACTID == 1) {
            flagFrom = (ImageView)activity.findViewById(R.id.flag_from);
            currencyFrom = (ImageView)activity.findViewById(R.id.currency_from);
        }

        mainToolBarImage = (ImageButton)activity.findViewById(R.id.icon_main_toolbar);
        converterToolBarImage = (ImageButton)activity.findViewById(R.id.icon_converter_toolbar);
        iconCurrencyToolbar = (ImageButton)activity.findViewById(R.id.icon_currency_toolbar);

        mainToolBarImage.setColorFilter(null);
        converterToolBarImage.setColorFilter(null);
        iconCurrencyToolbar.setColorFilter(Color.argb(255, 255, 255, 255));

        rippleLayoutUsd = (MaterialRippleLayout)viewFragmentSelectCurrency.findViewById(R.id.ripple_view_usd);
        rippleLayoutEur = (MaterialRippleLayout)viewFragmentSelectCurrency.findViewById(R.id.ripple_view_eur);
        rippleLayoutRub = (MaterialRippleLayout)viewFragmentSelectCurrency.findViewById(R.id.ripple_view_rub);

        rippleLayoutUsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconCurrencyToolbar.setImageResource(R.drawable.icon_dollar_dark);
                if (ACTID == 0)
                {
                    MainActivity.setCurrencyId(0);
                    iconCurrencyToolbar.setColorFilter(null);
                    mainToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
                }
                else if (ACTID == 1)
                {
                    Converter.setCurrencyId(0);
                    iconCurrencyToolbar.setColorFilter(null);
                    converterToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
                    flagFrom.setImageResource(R.drawable.flag_usa_dark);
                    currencyFrom.setImageResource(R.drawable.icon_dollar_dark);
                }
                activity.getSupportFragmentManager().beginTransaction().remove(SelectCurrencyFragment.this).commit();
            }
        });

        rippleLayoutEur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconCurrencyToolbar.setImageResource(R.drawable.icon_euro_dark);
                if (ACTID == 0)
                {
                    MainActivity.setCurrencyId(1);
                    iconCurrencyToolbar.setColorFilter(null);
                    mainToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
                }
                else if (ACTID == 1)
                {
                    Converter.setCurrencyId(1);
                    iconCurrencyToolbar.setColorFilter(null);
                    converterToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
                    flagFrom.setImageResource(R.drawable.flag_europe_dark);
                    currencyFrom.setImageResource(R.drawable.icon_euro_dark);
                }
                activity.getSupportFragmentManager().beginTransaction().remove(SelectCurrencyFragment.this).commit();
            }
        });

        rippleLayoutRub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconCurrencyToolbar.setImageResource(R.drawable.icon_rb_dark);
                if (ACTID == 0)
                {
                    MainActivity.setCurrencyId(2);
                    iconCurrencyToolbar.setColorFilter(null);
                    mainToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
                }
                else if (ACTID == 1)
                {
                    Converter.setCurrencyId(2);
                    iconCurrencyToolbar.setColorFilter(null);
                    converterToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
                    flagFrom.setImageResource(R.drawable.flag_russia_dark);
                    currencyFrom.setImageResource(R.drawable.icon_rb_dark);
                }
                activity.getSupportFragmentManager().beginTransaction().remove(SelectCurrencyFragment.this).commit();
            }
        });

        return viewFragmentSelectCurrency;
    }
}
