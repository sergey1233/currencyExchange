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
import android.widget.TextView;
import android.widget.Toast;

import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.Bank;
import com.sergey.currencyexchange.model.BlackMarket;
import com.sergey.currencyexchange.model.MBank;
import com.sergey.currencyexchange.model.Nbu;
import com.sergey.currencyexchange.model.NumberUtils;
import com.sergey.currencyexchange.ui.fragment.BuyFragment;
import com.sergey.currencyexchange.ui.fragment.SelectCurrencyFragment;
import com.sergey.currencyexchange.ui.fragment.SellFragment;
import com.sergey.currencyexchange.ui.widgets.WrapContentHeightViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 05.05.2016.
 */
public class Converter extends AppCompatActivity {

    private static final String TAG = "Converter:";
    private final static int ACTID = 1;

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
    private Button buttonCounted;
    private double countFrom = 0;  //initialization
    private double currencyExchange = 0; //initialization
    private int countEditInputs = 0; //initialization
    private InputMethodManager imm;
    private TabLayout tabLayout;
    private WrapContentHeightViewPager viewPager;
    private BuyFragment buyFragment;
    private SellFragment sellFragment;
    private SelectCurrencyFragment selectCurrencyFragment;

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

        //initialization toolbar imagebuttons
        mainToolBarImage = (ImageButton)findViewById(R.id.icon_main_toolbar);
        converterToolBarImage = (ImageButton)findViewById(R.id.icon_converter_toolbar);
        iconCurrencyToolbar = (ImageButton)findViewById(R.id.icon_currency_toolbar);

        //initialization converter views
        flagFrom = (ImageView)findViewById(R.id.flag_from);
        flagTo = (ImageView)findViewById(R.id.flag_to);
        currencyFrom = (ImageView)findViewById(R.id.currency_from);
        currencyTo = (ImageView)findViewById(R.id.currency_to);
        countFromEdit = (EditText)findViewById(R.id.count_from_edittext);
        currencyExchangeEdit = (EditText)findViewById(R.id.currency_exchange_edittext);
        countToResult = (TextView)findViewById(R.id.count_to_result);
        buttonCounted = (Button)findViewById(R.id.button_counted);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //set default icons
        setDollarToGrn();
        flagTo.setImageResource(R.drawable.flag_ukraine_dark);
        currencyTo.setImageResource(R.drawable.icon_grn_dark);


        //get and transfer buy and sell currency from MainActivity to Fragments
        transferCurrencyRates();

        countFromEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (countEditInputs != 0)
                {
                    countFromEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    countStandartKeyButtonDone(countFromEdit);
                }
                if (hasFocus)
                {
                    countFromEdit.setText("");
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    countEditInputs++;
                }
            }
        });

        currencyExchangeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    currencyExchangeEdit.setText("");
                    countStandartKeyButtonDone(currencyExchangeEdit);
                }
            }
        });

        buttonCounted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultOutput();
            }
        });



        //Toolbar actions
        converterToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
        mainToolBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Converter.this, MainActivity.class);
                intent.putExtra("currencyId", getCurrencyId());
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        Bundle args = new Bundle();
        args.putInt("fromActivity", ACTID);
        selectCurrencyFragment = new SelectCurrencyFragment();
        selectCurrencyFragment.setArguments(args);

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


        iconCurrencyToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = selectCurrencyFragment.getClass().getSimpleName();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selectCurrencyFragment, tag)
                        .addToBackStack(tag)
                        .commit();
            }
        });


        buyFragment = new BuyFragment();
        buyFragment.setArguments(bundleBuy);
        sellFragment = new SellFragment();
        sellFragment.setArguments(bundleSell);
        viewPager = (WrapContentHeightViewPager)findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void transferCurrencyRates() {
        currencyId = (int)getIntent().getIntExtra("currencyId", 0);
        nbu = (Nbu)getIntent().getParcelableExtra(Nbu.class.getCanonicalName());
        mBank = (MBank)getIntent().getParcelableExtra(MBank.class.getCanonicalName());
        blackMarket = (BlackMarket)getIntent().getParcelableExtra(BlackMarket.class.getCanonicalName());
        bankList = (ArrayList)getIntent().getParcelableArrayListExtra(ArrayList.class.getCanonicalName());

        nbuRate = nbu.getRate();
        mBankBuy = mBank.getBuy();
        mBankSell = mBank.getSell();
        blackMarketBuy = blackMarket.getBuy();
        blackMarketSell = blackMarket.getSell();


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
    }

    public void setDollarToGrn() {
        flagFrom.setImageResource(R.drawable.flag_usa_dark);
        currencyFrom.setImageResource(R.drawable.icon_dollar_dark);
    }

    public void setEuroToGrn() {
        flagFrom.setImageResource(R.drawable.flag_usa_dark);
        currencyFrom.setImageResource(R.drawable.icon_dollar_dark);
    }


    public Double getResult(double countFrom, double currencyExchange) {
        return NumberUtils.roundResut(countFrom * currencyExchange);
    }

    public boolean isDigit(String string) {
        try {
            Double.parseDouble(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //processing, pressing the keypad button
    public void countStandartKeyButtonDone(EditText editText) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                {
                    resultOutput();
                }
                return false;
            }
        });
    }

    //result output to textview, and hide keyboard
    public void resultOutput() {
        if (countFromEdit.getText() != null)
        {
            if (isDigit(countFromEdit.getText().toString())) {
                countFrom = Double.parseDouble(countFromEdit.getText().toString());
                buyFragment.setSumInfo(countFrom);
                sellFragment.setSumInfo(countFrom);
            }
            else
            {
                Toast.makeText(Converter.this, getText(R.string.exception_count), Toast.LENGTH_SHORT).show();
            }

            if (currencyExchangeEdit.getText() != null)
            {
                if (isDigit(currencyExchangeEdit.getText().toString()) == true)
                {
                    currencyExchange = Double.parseDouble(currencyExchangeEdit.getText().toString());
                    countToResult.setText(String.format("%.2f", getResult(countFrom, currencyExchange)));
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                else
                {
                    Toast.makeText(Converter.this, getText(R.string.exception_currency), Toast.LENGTH_SHORT).show();
                }
            }
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

        public Fragment getFragmentBuy()
        {
            return mFragmentList.get(0);
        }

        public Fragment getFragmentSell()
        {
            return mFragmentList.get(1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static void setCurrencyId(int id)
    {
        currencyId = id;
    }

    public static int getCurrencyId()
    {
        return currencyId;
    }
}
