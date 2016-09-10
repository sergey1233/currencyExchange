package com.sergey.currencyexchange.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.Utils;
import com.sergey.currencyexchange.ui.fragment.BuyFragment;
import com.sergey.currencyexchange.ui.fragment.SellFragment;
import com.sergey.currencyexchange.ui.widgets.WrapContentHeightViewPager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class Converter extends AppCompatActivity {

    private static final String TAG = "Converter:";
    private static final int MAINACTIVITYTYPE = 0;
    private static final int CONVERTERTYPE = 1;
    private static final int SELECTCURRENCYTYPE = 2;
    private static final int SELECTCONVERTCURRENCYTYPE = 3;
    private static final int TYPEACTIVITY = CONVERTERTYPE;
    private static final int GRN = 0;
    private static final int USD = 1;
    private static final int EUR = 2;
    private static final int RUB = 3;
    private int fromCurrency;
    private int toCurrency;

    private ImageButton mainToolBarImage;
    private ImageButton converterToolBarImage;
    private ImageButton iconCurrencyToolbar;
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
    private TabLayout tabLayout;
    private WrapContentHeightViewPager viewPager;
    private BuyFragment buyFragment;
    private SellFragment sellFragment;
    private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converter);
        mainToolBarImage = (ImageButton)findViewById(R.id.icon_main_toolbar);
        converterToolBarImage = (ImageButton)findViewById(R.id.icon_converter_toolbar);
        iconCurrencyToolbar = (ImageButton)findViewById(R.id.icon_currency_toolbar);
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
        if (typeAct == MAINACTIVITYTYPE) {
            fromCurrency = USD;
            toCurrency = GRN;
        }
        else if (typeAct == SELECTCONVERTCURRENCYTYPE) {
            fromCurrency = getIntent().getIntExtra("fromCurrency", 0);
            toCurrency = getIntent().getIntExtra("toCurrency", 0);
        }

        setImages();

        buyFragment = new BuyFragment();
        sellFragment = new SellFragment();
        viewPager = (WrapContentHeightViewPager)findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

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

    public void setImages() {
        set_own_currency.setImageResource(R.drawable.button_own_currency);
        converterToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
        iconCurrencyToolbar.setImageResource(R.drawable.icon_dollar_dark);
        setFromCurrencyElements();
        setToCurrencyElements();

    }

    public void setFromCurrencyElements() {
        switch (fromCurrency) {
            case GRN:
                flagFrom.setImageResource(R.drawable.flag_ukraine_dark);
                currencyFromIcon.setImageResource(R.drawable.icon_grn_dark);
                break;
            case USD:
                flagFrom.setImageResource(R.drawable.flag_usa_dark);
                currencyFromIcon.setImageResource(R.drawable.icon_dollar_dark);
                break;
            case EUR:
                flagFrom.setImageResource(R.drawable.flag_europe_dark);
                currencyFromIcon.setImageResource(R.drawable.icon_euro_dark);
                break;
            case RUB:
                flagFrom.setImageResource(R.drawable.flag_russia_dark);
                currencyFromIcon.setImageResource(R.drawable.icon_rb_dark);
                break;

        }
    }

    public void setToCurrencyElements() {
        switch (toCurrency) {
            case GRN:
                flagTo.setImageResource(R.drawable.flag_ukraine_dark);
                currencyToIcon.setImageResource(R.drawable.icon_grn_dark);
                break;
            case USD:
                flagTo.setImageResource(R.drawable.flag_usa_dark);
                currencyToIcon.setImageResource(R.drawable.icon_dollar_dark);
                break;
            case EUR:
                flagTo.setImageResource(R.drawable.flag_europe_dark);
                currencyToIcon.setImageResource(R.drawable.icon_euro_dark);
                break;
            case RUB:
                flagTo.setImageResource(R.drawable.flag_russia_dark);
                currencyToIcon.setImageResource(R.drawable.icon_rb_dark);
                break;

        }
    }

    public BigDecimal getResult(double countFrom, double currencyExchange) {
        BigDecimal result = new BigDecimal(countFrom * currencyExchange);
        return result;
    }

    public void resultOutput(Double count) {
        if (countFrom != count) {
            countFrom = count;
            buyFragment.setSumInfo(countFrom, fromCurrency, toCurrency);
            sellFragment.setSumInfo(countFrom, fromCurrency, toCurrency);
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

    public void resultOutputCurrency(Double currency) {
        currencyExchange = currency;
        if (getResult(countFrom, currencyExchange).doubleValue() < 999999999) {
            countToResult.setText(String.format("%.2f", getResult(countFrom, currencyExchange)));
        }
        else {
            Toast.makeText(Converter.this, getText(R.string.too_big), Toast.LENGTH_SHORT).show();
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(buyFragment, getString(R.string.buy));
        adapter.addFragment(sellFragment, getString(R.string.sell));
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
