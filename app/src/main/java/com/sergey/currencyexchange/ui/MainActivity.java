package com.sergey.currencyexchange.ui;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.ApplicationInfo;
import com.sergey.currencyexchange.model.Bank;
import com.sergey.currencyexchange.model.BlackMarket;
import com.sergey.currencyexchange.model.MBank;
import com.sergey.currencyexchange.model.Nbu;


import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
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

    private Nbu nbu;
    private MBank mBank;
    private BlackMarket blackMarket;
    private ArrayList<Bank> bankList = new ArrayList<>();

    private GraphView graph;
    private RecyclerView recyclerViewBanks;
    private BankListAdapter adapter;
    private static int currencyId = 0;//0 - usd; 1 - eur; 2 - rub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");


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
        graph = (GraphView) findViewById(R.id.graph);
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


        mainToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
        nbuTest();
        mbTest();
        blackMTest();
        banksTest();

        ApplicationInfo.getInstance().setNewApplicationInfo(currencyId, nbu, mBank, blackMarket, bankList);

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        graph.removeAllSeries();

        currencyId = ApplicationInfo.getInstance().getCurrencyId();
        nbu = ApplicationInfo.getInstance().getNbu();
        mBank = ApplicationInfo.getInstance().getMBank();
        blackMarket = ApplicationInfo.getInstance().getBlackMarket();
        bankList = ApplicationInfo.getInstance().getBankList();

        setToolbarIcon();
        setViews();
    }

    public void nbuTest() {
        nbu = new Nbu("14.03.16 15:06", 24.019960, 28.4450, 0.3870);
        nbu.setNewInformation("11.05.16 16:08", 24.58741, 28.2210, 0.3678);
    }

    public void mbTest() {
        String[] mBankDateArray = {"12.05.16 10:08", "12.05.16 11:08", "12.05.16 12:08", "12.05.16 13:08", "12.05.16 14:08"};
        double[] mBankBuyArrayDollar = {25.3100, 25.2400, 25.2500, 25.3200, 25.3400};
        double[] mBankSellArrayDollar ={25.3700, 25.3100, 25.3200, 25.3600, 25.3500};
        double[] mBankBuyArrayEuro = {27.3100, 27.2400, 27.2500, 27.3200, 27.3400};
        double[] mBankSellArrayEuro = {27.3700, 27.3100, 27.3200, 27.3600, 27.3500};
        double[] mBankBuyArrayRb = {0.3100, 0.2400, 0.2500, 0.3200, 0.3400};
        double[] mBankSellArrayRb = {0.3700, 0.3100, 0.3200, 0.3600, 0.3500};


        mBank = new MBank(mBankDateArray, mBankBuyArrayDollar, mBankSellArrayDollar,mBankBuyArrayEuro, mBankSellArrayEuro, mBankBuyArrayRb, mBankSellArrayRb);
    }

    public void graphTest() {
        double[] buyInfoForGraph = mBank.getBuyArray(currencyId);
        double[] sellInfoForGraph = mBank.getSellArray(currencyId);


        graph.getGridLabelRenderer().setNumVerticalLabels(5);
        LineGraphSeries<DataPoint> buyGraph = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, buyInfoForGraph[buyInfoForGraph.length - 5]),
                new DataPoint(1, buyInfoForGraph[buyInfoForGraph.length - 4]),
                new DataPoint(2, buyInfoForGraph[buyInfoForGraph.length - 3]),
                new DataPoint(3, buyInfoForGraph[buyInfoForGraph.length - 2]),
                new DataPoint(4, buyInfoForGraph[buyInfoForGraph.length - 1])
        });
        graph.addSeries(buyGraph);
        PointsGraphSeries<DataPoint> buyPoint = new PointsGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, buyInfoForGraph[buyInfoForGraph.length - 5]),
                new DataPoint(1, buyInfoForGraph[buyInfoForGraph.length - 4]),
                new DataPoint(2, buyInfoForGraph[buyInfoForGraph.length - 3]),
                new DataPoint(3, buyInfoForGraph[buyInfoForGraph.length - 2]),
                new DataPoint(4, buyInfoForGraph[buyInfoForGraph.length - 1])
        });
        buyPoint.setSize(10);
        buyPoint.setColor(Color.argb(255, 80, 222, 105));
        graph.addSeries(buyPoint);

        LineGraphSeries<DataPoint> sellGraph = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, sellInfoForGraph[sellInfoForGraph.length - 5]),
                new DataPoint(1, sellInfoForGraph[sellInfoForGraph.length - 4]),
                new DataPoint(2, sellInfoForGraph[sellInfoForGraph.length - 3]),
                new DataPoint(3, sellInfoForGraph[sellInfoForGraph.length - 2]),
                new DataPoint(4, sellInfoForGraph[sellInfoForGraph.length - 1])
        });
        graph.addSeries(sellGraph);
        PointsGraphSeries<DataPoint> sellPoint = new PointsGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, sellInfoForGraph[sellInfoForGraph.length - 5]),
                new DataPoint(1, sellInfoForGraph[sellInfoForGraph.length - 4]),
                new DataPoint(2, sellInfoForGraph[sellInfoForGraph.length - 3]),
                new DataPoint(3, sellInfoForGraph[sellInfoForGraph.length - 2]),
                new DataPoint(4, sellInfoForGraph[sellInfoForGraph.length - 1])
        });
        sellPoint.setSize(10);
        sellPoint.setColor(Color.argb(255, 248, 50, 36));
        graph.addSeries(sellPoint);
    }

    public void blackMTest() {
        blackMarket = new BlackMarket("14.03.16 15:06", 24.019960, 24.9572, 27.019960, 27.9572, 0.019960, 0.9572);
        blackMarket.setNewInformation("14.03.16 15:06", 24.419960, 24.5572, 27.219960, 27.9672, 0.319960, 0.4572);
    }

    public void banksTest() {
        Bank PrivatB = new Bank("bank_icon_privat", getString(R.string.privat));
        Bank OshadB = new Bank("bank_icon_oshad", getString(R.string.oshad));
        Bank SberB = new Bank("bank_icon_privat", getString(R.string.sber_bank));
        Bank RaiphB = new Bank("bank_icon_raif", getString(R.string.raif));
        Bank UkrsotsB = new Bank("bank_icon_ukrsots", getString(R.string.ukrsots));
        Bank AlphaB = new Bank("bank_icon_alpha_bank", getString(R.string.alpha_bank));
        Bank UkrSibB = new Bank("bank_icon_ukrsib", getString(R.string.ukrsib));
        Bank PumbB = new Bank("bank_icon_pumb",  getString(R.string.pumb));
        Bank VtbB = new Bank("bank_icon_vtb", getString(R.string.vtb));
        Bank OtpB = new Bank("bank_icon_otp", getString(R.string.otp));
        Bank CrediAgriB = new Bank("bank_icon_crediagr", getString(R.string.crediagr));

        bankList = new ArrayList<Bank>();
        bankList.addAll(Arrays.asList(PrivatB, OshadB, SberB, RaiphB, UkrsotsB, AlphaB, UkrSibB, PumbB, VtbB, OtpB, CrediAgriB));

        for (int i = 0; i < 11; i++)
        {
            bankList.get(i).setNewInformation("06.05.2016 21:47", 25.6150, 25.8300, 27.6150, 27.8300, 0.6150, 0.8300);
        }
    }

    public void setViews() {
        nbuRate.setText(String.valueOf(String.format("%.4f",nbu.getRate(currencyId))));
        nbuDate.setText(nbu.getDate());
        setChangesInfo(nbu);

        mbBuyRate.setText(String.format("%.4f",mBank.getBuy(currencyId)));
        mbSellRate.setText(String.format("%.4f",mBank.getSell(currencyId)));
        mbDate.setText(mBank.getDate());
        setChangesInfo(mBank);

        graphTest();

        blackMBuyRate.setText(String.format("%.4f",blackMarket.getBuy(currencyId)));
        blackMSellRate.setText(String.format("%.4f",blackMarket.getSell(currencyId)));
        blackMDate.setText(blackMarket.getDate());
        setChangesInfo(blackMarket);

        refreshAdapter();
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

    public void refreshAdapter() {
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
