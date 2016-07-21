package com.sergey.currencyexchange.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.ApplicationInfo;
import com.sergey.currencyexchange.model.Bank;
import com.sergey.currencyexchange.model.BlackMarket;
import com.sergey.currencyexchange.model.MBank;
import com.sergey.currencyexchange.model.Nbu;
import com.sergey.currencyexchange.model.Utils;
import com.sergey.currencyexchange.ui.fragment.BuyFragment;
import com.sergey.currencyexchange.ui.fragment.SellFragment;
import com.sergey.currencyexchange.ui.widgets.WrapContentHeightViewPager;
import java.util.ArrayList;
import java.util.List;


public class Converter extends AppCompatActivity implements TextView.OnEditorActionListener {

    private static final String TAG = "Converter:";
    private static final int TYPEACTIVITY = 1;

    private ImageButton mainToolBarImage;
    private ImageButton converterToolBarImage;
    private ImageButton iconCurrencyToolbar;
    private ImageView flagFrom;
    private ImageView flagTo;
    private ImageView currencyFrom;
    private ImageView currencyTo;
    private EditText countFromEdit;
    private EditText currencyExchangeEdit;
    private TextView countToResult;
    private Button set_own_currency;
    private LinearLayout own_currecny_linear;

    private Button buttonCounted;
    private double countFrom = 0;
    private double currencyExchange = 0;
    private TabLayout tabLayout;
    private WrapContentHeightViewPager viewPager;
    private BuyFragment buyFragment;
    private SellFragment sellFragment;
    private InputMethodManager imm;

    private ApplicationInfo app;
    private Nbu nbu;
    private MBank mBank;
    private BlackMarket blackMarket;
    private ArrayList<Bank> bankList;
    private Bundle bundleBuy;
    private Bundle bundleSell;
    private double nbuRate;
    private double mBankBuy;
    private double mBankSell;
    private double blackMarketBuy;
    private double blackMarketSell;
    private ViewPagerAdapter adapter;
    private static int currencyId = 0;//0 - usd; 1 - eur; 2 - rub;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converter);
        mainToolBarImage = (ImageButton)findViewById(R.id.icon_main_toolbar);
        converterToolBarImage = (ImageButton)findViewById(R.id.icon_converter_toolbar);
        iconCurrencyToolbar = (ImageButton)findViewById(R.id.icon_currency_toolbar);
        flagFrom = (ImageView)findViewById(R.id.flag_from);
        flagTo = (ImageView)findViewById(R.id.flag_to);
        currencyFrom = (ImageView)findViewById(R.id.currency_from);
        currencyTo = (ImageView)findViewById(R.id.currency_to);
        countFromEdit = (EditText)findViewById(R.id.count_from_edittext);
        currencyExchangeEdit = (EditText)findViewById(R.id.currency_exchange_edittext);
        countToResult = (TextView)findViewById(R.id.count_to_result);
        set_own_currency = (Button)findViewById(R.id.set_own_currency);
        own_currecny_linear = (LinearLayout)findViewById(R.id.own_currency_linear);
        buttonCounted = (Button)findViewById(R.id.button_counted);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        countFromEdit.setOnEditorActionListener(this);
        currencyExchangeEdit.setOnEditorActionListener(this);
        flagTo.setImageResource(R.drawable.flag_ukraine_dark);
        currencyTo.setImageResource(R.drawable.icon_grn_dark);

        app = ApplicationInfo.getInstance();
        currencyId = app.getCurrencyId();
        nbu = app.getNbu();
        mBank = app.getMBank();
        blackMarket = app.getBlackMarket();
        bankList = app.getBankList();

        nbuRate = nbu.getRate(currencyId);
        mBankBuy = mBank.getBuy(currencyId);
        mBankSell = mBank.getSell(currencyId);
        blackMarketBuy = blackMarket.getBuy(currencyId);
        blackMarketSell = blackMarket.getSell(currencyId);

        bundleBuy = new Bundle();
        bundleBuy.putDouble(Nbu.class.getCanonicalName(), nbuRate);
        bundleBuy.putDouble(MBank.class.getCanonicalName(), mBankBuy);
        bundleBuy.putDouble(BlackMarket.class.getCanonicalName(), blackMarketBuy);
        bundleBuy.putParcelableArrayList(ArrayList.class.getCanonicalName(), bankList);

        bundleSell = new Bundle();
        bundleSell.putDouble(Nbu.class.getCanonicalName(), nbuRate);
        bundleSell.putDouble(MBank.class.getCanonicalName(), mBankSell);
        bundleSell.putDouble(BlackMarket.class.getCanonicalName(), blackMarketSell);
        bundleSell.putParcelableArrayList(ArrayList.class.getCanonicalName(), bankList);

        buyFragment = new BuyFragment();
        buyFragment.setArguments(bundleBuy);
        sellFragment = new SellFragment();
        sellFragment.setArguments(bundleSell);
        viewPager = (WrapContentHeightViewPager)findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        switch (currencyId)
        {
            case 0: //usd
                iconCurrencyToolbar.setImageResource(R.drawable.icon_dollar_dark);
                flagFrom.setImageResource(R.drawable.flag_usa_dark);
                currencyFrom.setImageResource(R.drawable.icon_dollar_dark);
                break;
            case 1: //euro
                iconCurrencyToolbar.setImageResource(R.drawable.icon_euro_dark);
                flagFrom.setImageResource(R.drawable.flag_europe_dark);
                currencyFrom.setImageResource(R.drawable.icon_euro_dark);
                break;
            case 2://rub
                iconCurrencyToolbar.setImageResource(R.drawable.icon_rb_dark);
                flagFrom.setImageResource(R.drawable.flag_russia_dark);
                currencyFrom.setImageResource(R.drawable.icon_rb_dark);
                break;
        }

        countFromEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    countFromEdit.setText("");
                }
            }
        });
        currencyExchangeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    currencyExchangeEdit.setText("");
                }
            }
        });

        buttonCounted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultOutput();
                imm.hideSoftInputFromWindow(buttonCounted.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        set_own_currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (own_currecny_linear.getVisibility() == View.GONE) {
                    own_currecny_linear.setVisibility(View.VISIBLE);
                    set_own_currency.setText(R.string.hide_own_currency);
                    currencyExchangeEdit.setText(String.valueOf(nbuRate));
                }
                else {
                    own_currecny_linear.setVisibility(View.GONE);
                    set_own_currency.setText(R.string.set_own_currency);
                }
            }
        });

        converterToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
        mainToolBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Converter.this, MainActivity.class);
                intent.putExtra("fromActivity", TYPEACTIVITY);
                startActivity(intent);
            }
        });

        iconCurrencyToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Converter.this, SelectCurrency.class);
                intent.putExtra("fromActivity", TYPEACTIVITY);
                startActivity(intent);
            }
        });
    }

    public double getResult(double countFrom, double currencyExchange) {
        double result = countFrom * currencyExchange;
        return result;
    }

    //result output to textview, and hide keyboard
    public void resultOutput() {
        if (countFromEdit.getText() != null)
        {
            if (Utils.isDigit(countFromEdit.getText().toString())) {
                if (getResult(countFrom, currencyExchange) < 9999999) {
                    countFrom = Double.parseDouble(countFromEdit.getText().toString());
                    buyFragment.setSumInfo(countFrom);
                    sellFragment.setSumInfo(countFrom);
                }
                else {
                    Toast.makeText(Converter.this, getText(R.string.too_big), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(Converter.this, getText(R.string.exception_count), Toast.LENGTH_SHORT).show();
            }

            if (own_currecny_linear.getVisibility() == View.VISIBLE) {
                if (currencyExchangeEdit.getText() != null) {
                    if (Utils.isDigit(currencyExchangeEdit.getText().toString()) == true)
                    {
                        currencyExchange = Double.parseDouble(currencyExchangeEdit.getText().toString());
                        if (getResult(countFrom, currencyExchange) < 9999999) {
                            countToResult.setText(String.format("%.2f", getResult(countFrom, currencyExchange)));
                        }
                        else {
                            Toast.makeText(Converter.this, getText(R.string.too_big), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(Converter.this, getText(R.string.exception_currency), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            resultOutput();
        }
        return false;
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
