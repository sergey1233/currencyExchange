package com.sergey.currencyexchange.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.sergey.currencyexchange.ui.fragment.BuyFragment;
import com.sergey.currencyexchange.ui.fragment.SellFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 05.05.2016.
 */
public class Converter extends AppCompatActivity {

    private ImageButton mainToolBarImage;
    private ImageButton converterToolBarImage;
    private ImageView flagFrom;
    private ImageView flagTo;
    private ImageView currencyFrom;
    private ImageView currencyTo;
    private EditText countFromEdit;
    private EditText currencyExchangeEdit;
    private TextView countToResult;
    private Button buttonCounted;
    private double countFrom = 0;
    private double currencyExchange = 0;
    private int countEditInputs = 0;
    private InputMethodManager imm;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converter);

        mainToolBarImage = (ImageButton)findViewById(R.id.icon_main_toolbar);
        converterToolBarImage = (ImageButton)findViewById(R.id.icon_converter_toolbar);
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
                    countFromEdit.setHint("");
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
                    currencyExchangeEdit.setHint("");
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

        converterToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
        mainToolBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Converter.this, MainActivity.class);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setDollarToGrn()
    {
        flagFrom.setImageResource(R.drawable.flag_usa_dark);
        flagTo.setImageResource(R.drawable.flag_ukraine_dark);
        currencyFrom.setImageResource(R.drawable.icon_dollar_dark);
        currencyTo.setImageResource(R.drawable.icon_grn_dark);
    }

    public Double getResult(double countFrom, double currencyExchange)
    {
        return countFrom * currencyExchange;
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
    public void countStandartKeyButtonDone(EditText editText)
    {
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
    public void resultOutput()
    {
        if (countFromEdit.getText() != null && currencyExchangeEdit.getText() != null)
        {
            if (isDigit(countFromEdit.getText().toString()) == true && isDigit(currencyExchangeEdit.getText().toString()) == true)
            {
                countFrom = Double.parseDouble(countFromEdit.getText().toString());
                currencyExchange = Double.parseDouble(currencyExchangeEdit.getText().toString());
                countToResult.setText(getResult(countFrom, currencyExchange).toString());

                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
            else
            {
                Toast.makeText(Converter.this, "Некорректно ведены данные", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BuyFragment(), getString(R.string.buy));
        adapter.addFragment(new SellFragment(), getString(R.string.sell));
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
