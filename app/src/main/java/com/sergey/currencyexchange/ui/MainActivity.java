package com.sergey.currencyexchange.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.ApplicationInfo;
import com.sergey.currencyexchange.model.Bank;
import com.sergey.currencyexchange.model.BlackMarket;
import com.sergey.currencyexchange.model.MBank;
import com.sergey.currencyexchange.model.Nbu;
import com.sergey.currencyexchange.services.UpdateInfoService;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private static boolean EXIT_FROM_ALL_ACTIVITIES = false;
    private final static int TYPEACTIVITY = 0;

    private ImageButton mainToolBarImage;
    private ImageButton converterToolBarImage;
    private ImageButton iconCurrencyToolbar;
    private TextView nbuRate;
    private TextView nbuChangesText;
    private TextView nbuDate;
    private ImageView nbuChangesImgUp;
    private ImageView nbuChangesImgDown;
    private TextView mbBuyRate;
    private TextView mbSellRate;
    private TextView mbDate;
    private TextView mbBuyChangesText;
    private TextView mbSellChangesText;
    private ImageView mbBuyChangesImgUp;
    private ImageView mbBuyChangesImgDown;
    private ImageView mbSellChangesImgUp;
    private ImageView mbSellChangesImgDown;
    private TextView blackMBuyRate;
    private TextView blackMSellRate;
    private TextView blackMDate;
    private TextView blackMBuyChangesText;
    private TextView blackMSellChangesText;
    private ImageView blackMBuyChangesImgUp;
    private ImageView blackMBuyChangesImgDown;
    private ImageView blackMSellChangesImgUp;
    private ImageView blackMSellChangesImgDown;

    private ApplicationInfo app;
    private Nbu nbu;
    private MBank mBank;
    private BlackMarket blackMarket;
    private ArrayList<Bank> bankList = new ArrayList<>();

    private RecyclerView recyclerViewBanks;
    private BankListAdapter adapter;
    private static int currencyId = 0;//0 - usd; 1 - eur; 2 - rub;

    private int typeAct;
    private PullRefreshLayout refreshLayout;
    private BroadcastReceiver broadcastReceiver;
    private Intent serviceIntent;
    private IntentFilter intFilt;
    public final static String BROADCAST_ACTION = "com.sergey.currencyexchange.servicebackbroadcast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainToolBarImage = (ImageButton)findViewById(R.id.icon_main_toolbar);
        converterToolBarImage = (ImageButton)findViewById(R.id.icon_converter_toolbar);
        iconCurrencyToolbar = (ImageButton)findViewById(R.id.icon_currency_toolbar);
        nbuRate = (TextView)findViewById(R.id.nbu_rate);
        nbuChangesText = (TextView)findViewById(R.id.nbu_changes_text);
        nbuDate = (TextView)findViewById(R.id.nbu_date);
        nbuChangesImgUp = (ImageView)findViewById(R.id.nbu_changes_img_up);
        nbuChangesImgDown = (ImageView)findViewById(R.id.nbu_changes_img_down);
        mbBuyRate = (TextView)findViewById(R.id.mb_buy_rate);
        mbSellRate = (TextView)findViewById(R.id.mb_sell_rate);
        mbDate = (TextView)findViewById(R.id.mb_date);
        mbBuyChangesText = (TextView)findViewById(R.id.mb_buy_changes_text);
        mbSellChangesText = (TextView)findViewById(R.id.mb_sell_changes_text);
        mbBuyChangesImgUp = (ImageView)findViewById(R.id.mb_buy_changes_img_up);
        mbBuyChangesImgDown = (ImageView)findViewById(R.id.mb_buy_changes_img_down);
        mbSellChangesImgUp = (ImageView)findViewById(R.id.mb_sell_changes_img_up);
        mbSellChangesImgDown = (ImageView)findViewById(R.id.mb_sell_changes_img_down);
        blackMBuyRate = (TextView)findViewById(R.id.blackM_buy_rate);
        blackMSellRate = (TextView)findViewById(R.id.blackM_sell_rate);
        blackMDate = (TextView)findViewById(R.id.blackM_date);
        blackMBuyChangesText = (TextView)findViewById(R.id.blackM_buy_changes_text);
        blackMSellChangesText = (TextView)findViewById(R.id.blackM_sell_changes_text);
        blackMBuyChangesImgUp = (ImageView)findViewById(R.id.blackM_buy_changes_img_up);
        blackMBuyChangesImgDown = (ImageView)findViewById(R.id.blackM_buy_changes_img_down);
        blackMSellChangesImgUp = (ImageView)findViewById(R.id.blackM_sell_changes_img_up);
        blackMSellChangesImgDown = (ImageView)findViewById(R.id.blackM_sell_changes_img_down);
        recyclerViewBanks = (RecyclerView)findViewById(R.id.recycler_view_banks_convert);
        recyclerViewBanks.setNestedScrollingEnabled(false);
        recyclerViewBanks.setFocusable(false);
        adapter = new BankListAdapter(bankList, currencyId, this);
        recyclerViewBanks.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerViewBanks.setLayoutManager(layoutManager);
        recyclerViewBanks.setItemAnimator(itemAnimator);
        serviceIntent = new Intent(this, UpdateInfoService.class);
        refreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mainToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
        typeAct = getIntent().getIntExtra("fromActivity", 0);


        app = ApplicationInfo.getInstance();
        currencyId = app.getCurrencyId();
        nbu = app.getNbu();
        mBank = app.getMBank();
        blackMarket = app.getBlackMarket();
        bankList = app.getBankList();

        setToolbarIcon();
        setViews();

        if (bankList.size() == 0) {
            Bank PrivatB = new Bank("bank_icon_privat", getString(R.string.privat));
            Bank OshadB = new Bank("bank_icon_oshad", getString(R.string.oshad));
            Bank SberB = new Bank("bank_icon_sber", getString(R.string.sber_bank));
            Bank RaiphB = new Bank("bank_icon_raif", getString(R.string.raif));
            Bank UkrsotsB = new Bank("bank_icon_ukrsots", getString(R.string.ukrsots));
            Bank AlphaB = new Bank("bank_icon_alpha_bank", getString(R.string.alpha_bank));
            Bank UkrSibB = new Bank("bank_icon_ukrsib", getString(R.string.ukrsib));
            Bank PumbB = new Bank("bank_icon_pumb",  getString(R.string.pumb));
            Bank VtbB = new Bank("bank_icon_vtb", getString(R.string.vtb));
            Bank OtpB = new Bank("bank_icon_otp", getString(R.string.otp));
            Bank CrediAgriB = new Bank("bank_icon_crediagr", getString(R.string.crediagr));
            Bank UkrGaz = new Bank("bank_icon_ugb", getString(R.string.ugb));
            Bank TaskoB = new Bank("bank_icon_taskobank", getString(R.string.taskobank));
            Bank KreditD = new Bank("bank_icon_kreditdnepr", getString(R.string.kreditdnepr));
            bankList.addAll(Arrays.asList(PrivatB, OshadB, SberB, RaiphB, UkrsotsB, AlphaB, UkrSibB, PumbB, VtbB, OtpB, CrediAgriB, UkrGaz, TaskoB, KreditD));
        }

        if (typeAct != 1 && typeAct != 2) {
            refreshLayout.setRefreshing(true);
            startService(serviceIntent);
        }

        converterToolBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Converter.class);
                intent.putExtra("fromActivity", TYPEACTIVITY);
                startActivity(intent);
            }
        });
        iconCurrencyToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectCurrency.class);
                intent.putExtra("fromActivity", TYPEACTIVITY);
                startActivity(intent);
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 1500);
                setViews();
            }
        };
        intFilt = new IntentFilter(BROADCAST_ACTION);



        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startService(serviceIntent);
            }
        });
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            EXIT_FROM_ALL_ACTIVITIES = true;
            finish(); // finish activity
        } else {
            Toast.makeText(this, R.string.back, Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (EXIT_FROM_ALL_ACTIVITIES) {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver, intFilt);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }


    public void setViews() {
        setNbuView(nbu);
        setMBankView(mBank);
        setBLackMarketView(blackMarket);
        setBanklistView(bankList);
    }

    public void setNbuView(Nbu nbu) {
        nbuRate.setText(String.valueOf(String.format("%.4f",nbu.getRate(currencyId))));
        nbuDate.setText(nbu.getDate());
        setChangesInfo(nbu);
    }

    public void setMBankView(MBank mBank) {
        mbBuyRate.setText(String.format("%.4f",mBank.getBuy(currencyId)));
        mbSellRate.setText(String.format("%.4f",mBank.getSell(currencyId)));
        mbDate.setText(mBank.getDate());
        setChangesInfo(mBank);
    }

    public void setBLackMarketView(BlackMarket blackMarket) {
        blackMBuyRate.setText(String.format("%.4f",blackMarket.getBuy(currencyId)));
        blackMSellRate.setText(String.format("%.4f",blackMarket.getSell(currencyId)));
        blackMDate.setText(blackMarket.getDate());
        setChangesInfo(blackMarket);
    }

    public void setBanklistView(ArrayList<Bank> bankList) {
        refreshAdapter(bankList);
    }

    public void setToolbarIcon() {
        switch (currencyId)
        {
            case 0:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_dollar_dark);
                break;
            case 1:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_euro_dark);
                break;
            case 2:
                iconCurrencyToolbar.setImageResource(R.drawable.icon_rb_dark);
                break;
        }
    }


    public void refreshAdapter(ArrayList<Bank> bankList) {
        adapter.addItemstoList(bankList);
        adapter.addCurrencyId(currencyId);
        adapter.notifyDataSetChanged();
    }

    public void setChangesInfo(Nbu nbu) {
        if (nbu.getChanges(currencyId) != 0)
        {
            if (nbu.getChanges(currencyId) < 0)
            {
                nbuChangesText.setText(String.format("%.4f", nbu.getChanges(currencyId)));
                nbuChangesImgUp.setVisibility(View.INVISIBLE);
                nbuChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                nbuChangesText.setText("+" + String.format("%.4f", nbu.getChanges(currencyId)));
                nbuChangesImgUp.setVisibility(View.VISIBLE);
                nbuChangesImgDown.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setChangesInfo(MBank mBank) {
        if (mBank.getChangesBuy(currencyId) != 0)
        {
            if (mBank.getChangesBuy(currencyId) < 0)
            {
                mbBuyChangesText.setText(String.format("%.4f", mBank.getChangesBuy(currencyId)));
                mbBuyChangesImgUp.setVisibility(View.INVISIBLE);
                mbBuyChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                mbBuyChangesText.setText("+" + String.format("%.4f", mBank.getChangesBuy(currencyId)));
                mbBuyChangesImgUp.setVisibility(View.VISIBLE);
                mbBuyChangesImgDown.setVisibility(View.INVISIBLE);
            }
        }

        if (mBank.getChangesSell(currencyId) != 0)
        {
            if (mBank.getChangesSell(currencyId) < 0)
            {
                mbSellChangesText.setText(String.format("%.4f", mBank.getChangesSell(currencyId)));
                mbSellChangesImgUp.setVisibility(View.INVISIBLE);
                mbSellChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                mbSellChangesText.setText("+" + String.format("%.4f", mBank.getChangesSell(currencyId)));
                mbSellChangesImgUp.setVisibility(View.VISIBLE);
                mbSellChangesImgDown.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setChangesInfo(BlackMarket blackMarket) {
        if (blackMarket.getChangesBuy(currencyId) != 0)
        {
            if (blackMarket.getChangesBuy(currencyId) < 0)
            {
                blackMBuyChangesText.setText(String.format("%.4f", blackMarket.getChangesBuy(currencyId)));
                blackMBuyChangesImgUp.setVisibility(View.INVISIBLE);
                blackMBuyChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                blackMBuyChangesText.setText("+" + String.format("%.4f", blackMarket.getChangesBuy(currencyId)));
                blackMBuyChangesImgUp.setVisibility(View.VISIBLE);
                blackMBuyChangesImgDown.setVisibility(View.INVISIBLE);
            }
        }

        if (blackMarket.getChangesSell(currencyId) != 0)
        {
            if (blackMarket.getChangesSell(currencyId) < 0)
            {
                blackMSellChangesText.setText(String.format("%.4f", blackMarket.getChangesSell(currencyId)));
                blackMSellChangesImgUp.setVisibility(View.INVISIBLE);
                blackMSellChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                blackMSellChangesText.setText("+" + String.format("%.4f", blackMarket.getChangesSell(currencyId)));
                blackMSellChangesImgUp.setVisibility(View.VISIBLE);
                blackMSellChangesImgDown.setVisibility(View.INVISIBLE);
            }
        }
    }
}
