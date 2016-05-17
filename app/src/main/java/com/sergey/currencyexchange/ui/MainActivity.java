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
import com.sergey.currencyexchange.model.Bank;
import com.sergey.currencyexchange.model.BlackMarket;
import com.sergey.currencyexchange.model.MBank;
import com.sergey.currencyexchange.model.Nbu;
import com.sergey.currencyexchange.ui.fragment.SelectCurrencyFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private final static int ACTID = 0;

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
    private SelectCurrencyFragment selectCurrencyFragment;

    private Nbu nbu;
    private MBank mBank;
    private BlackMarket blackMarket;
    private ArrayList<Bank> bankList;

    private GraphView graph;
    private RecyclerView recyclerViewBanks;
    private BankListAdapter adapter;
    private static int currencyId = 0;//0 - usd; 1 - eur; 2 - rub;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialization toolbar imagebuttons
        mainToolBarImage = (ImageButton)findViewById(R.id.icon_main_toolbar);
        converterToolBarImage = (ImageButton)findViewById(R.id.icon_converter_toolbar);
        iconCurrencyToolbar = (ImageButton)findViewById(R.id.icon_currency_toolbar);
        //initialization mainactivity views
        //NBU
        nbuRate = (TextView)findViewById(R.id.nbu_rate);
        nbuChangesText = (TextView)findViewById(R.id.nbu_changes_text);
        nbuDate = (TextView)findViewById(R.id.nbu_date);
        nbuChangesImgUp = (ImageView)findViewById(R.id.nbu_changes_img_up);
        nbuChangesImgDown = (ImageView)findViewById(R.id.nbu_changes_img_down);
        //MB
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
        //BlackMarket
        blackMBuyRate = (TextView)findViewById(R.id.blackM_buy_rate);
        blackMSellRate = (TextView)findViewById(R.id.blackM_sell_rate);
        blackMDate = (TextView)findViewById(R.id.blackM_date);
        blackMBuyChangesText = (TextView)findViewById(R.id.blackM_buy_changes_text);
        blackMSellChangesText = (TextView)findViewById(R.id.blackM_sell_changes_text);
        blackMBuyChangesImgUp = (ImageView)findViewById(R.id.blackM_buy_changes_img_up);
        blackMBuyChangesImgDown = (ImageView)findViewById(R.id.blackM_buy_changes_img_down);
        blackMSellChangesImgUp = (ImageView)findViewById(R.id.blackM_sell_changes_img_up);
        blackMSellChangesImgDown = (ImageView)findViewById(R.id.blackM_sell_changes_img_down);

        nbuTest();
        mbTest();
        graphTest();
        blackMTest();
        banksTest();

        //Toolbar actions
        Bundle args = new Bundle();
        args.putInt("fromActivity", ACTID);
        selectCurrencyFragment = new SelectCurrencyFragment();
        selectCurrencyFragment.setArguments(args);

        mainToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));
        mainToolBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getSupportFragmentManager().beginTransaction().remove(selectCurrencyFragment).commit();
            }
        });
        converterToolBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getSupportFragmentManager().beginTransaction().remove(selectCurrencyFragment).commit();
                startConvertActivity();
            }
        });

        currencyId = (int)getIntent().getIntExtra("currencyId", 0);
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

    }

    public void nbuTest() {
        /*NBU TEST*/
        nbu = new Nbu("14.03.16 15:06", 24.019960);
        nbu.setNewInformation("11.05.16 16:08", 26.58741);
        nbuRate.setText(String.valueOf(String.format("%.4f",nbu.getRate())));
        nbuDate.setText(nbu.getDate());
        setChangesInfo(nbu);
        /*END NBU TEST*/
    }

    public void mbTest() {
        /*MB TEST*/
        ArrayList<String> mBankDateArray = new ArrayList<>();
        ArrayList<Double> mBankBuyArray = new ArrayList<>();
        ArrayList<Double> mBankSellArray = new ArrayList<>();
        mBankDateArray.addAll(Arrays.asList("12.05.16 10:08", "12.05.16 11:08", "12.05.16 12:08", "12.05.16 13:08", "12.05.16 14:08"));
        mBankBuyArray.addAll(Arrays.asList(25.3100, 25.2400, 25.2500, 25.3200, 25.3400));
        mBankSellArray.addAll(Arrays.asList(25.3700, 25.3100, 25.3200, 25.3600, 25.3500));
        mBank = new MBank(mBankDateArray, mBankBuyArray, mBankSellArray);
        //mBank.setNewInformation(mBankDates, mBankBuy, mBankSell);

        mbBuyRate.setText(String.format("%.4f",mBank.getBuy()));
        mbSellRate.setText(String.format("%.4f",mBank.getSell()));
        mbDate.setText(mBank.getDate());
        setChangesInfo(mBank);
        /*END MB TEST*/
    }

    public void graphTest() {
        /*GRAPH TEST*/
        ArrayList<Double> buyInfoForGraph = mBank.getBuyArray();
        ArrayList<Double> sellInfoForGraph = mBank.getSellArray();

        graph.getGridLabelRenderer().setNumVerticalLabels(5);
        LineGraphSeries<DataPoint> buyGraph = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, buyInfoForGraph.get(buyInfoForGraph.size() - 5)),
                new DataPoint(1, buyInfoForGraph.get(buyInfoForGraph.size() - 4)),
                new DataPoint(2, buyInfoForGraph.get(buyInfoForGraph.size() - 3)),
                new DataPoint(3, buyInfoForGraph.get(buyInfoForGraph.size() - 2)),
                new DataPoint(4, buyInfoForGraph.get(buyInfoForGraph.size() - 1))
        });
        graph.addSeries(buyGraph);
        PointsGraphSeries<DataPoint> buyPoint = new PointsGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, buyInfoForGraph.get(buyInfoForGraph.size() - 5)),
                new DataPoint(1, buyInfoForGraph.get(buyInfoForGraph.size() - 4)),
                new DataPoint(2, buyInfoForGraph.get(buyInfoForGraph.size() - 3)),
                new DataPoint(3, buyInfoForGraph.get(buyInfoForGraph.size() - 2)),
                new DataPoint(4, buyInfoForGraph.get(buyInfoForGraph.size() - 1))
        });
        buyPoint.setSize(10);
        buyPoint.setColor(Color.argb(255, 80, 222, 105));
        graph.addSeries(buyPoint);

        LineGraphSeries<DataPoint> sellGraph = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, sellInfoForGraph.get(sellInfoForGraph.size() - 5)),
                new DataPoint(1, sellInfoForGraph.get(sellInfoForGraph.size() - 4)),
                new DataPoint(2, sellInfoForGraph.get(sellInfoForGraph.size() - 3)),
                new DataPoint(3, sellInfoForGraph.get(sellInfoForGraph.size() - 2)),
                new DataPoint(4, sellInfoForGraph.get(sellInfoForGraph.size() - 1))
        });
        graph.addSeries(sellGraph);
        PointsGraphSeries<DataPoint> sellPoint = new PointsGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, sellInfoForGraph.get(sellInfoForGraph.size() - 5)),
                new DataPoint(1, sellInfoForGraph.get(sellInfoForGraph.size() - 4)),
                new DataPoint(2, sellInfoForGraph.get(sellInfoForGraph.size() - 3)),
                new DataPoint(3, sellInfoForGraph.get(sellInfoForGraph.size() - 2)),
                new DataPoint(4, sellInfoForGraph.get(sellInfoForGraph.size() - 1))
        });
        sellPoint.setSize(10);
        sellPoint.setColor(Color.argb(255, 248, 50, 36));
        graph.addSeries(sellPoint);
        /*END_GRAPH*/
    }

    public void blackMTest() {
        /*BlackMarket TEST*/
        blackMarket = new BlackMarket("14.03.16 15:06", 24.019960, 27.9572 );
        blackMarket.setNewInformation("11.05.16 16:08", 27.58741, 28.8574);
        blackMBuyRate.setText(String.format("%.4f",blackMarket.getBuy()));
        blackMSellRate.setText(String.format("%.4f",blackMarket.getSell()));
        blackMDate.setText(blackMarket.getDate());
        setChangesInfo(blackMarket);
        /*END BlackMarket TEST*/
    }

    public void banksTest() {
        /*BANKS_TEST*/
        Bank PrivatB = new Bank("privat", getString(R.string.privat));
        Bank OshadB = new Bank("oshad", getString(R.string.oshad));
        Bank SberB = new Bank("privat", getString(R.string.sber_bank));
        Bank RaiphB = new Bank("raif", getString(R.string.raif));
        Bank UkrsotsB = new Bank("ukrsots", getString(R.string.ukrsots));
        Bank AlphaB = new Bank("alpha_bank", getString(R.string.alpha_bank));
        Bank UkrSibB = new Bank("ukrsib", getString(R.string.ukrsib));
        Bank PumbB = new Bank("pumb",  getString(R.string.pumb));
        Bank VtbB = new Bank("vtb", getString(R.string.vtb));
        Bank OtpB = new Bank("otp", getString(R.string.otp));
        Bank CrediAgriB = new Bank("crediagr", getString(R.string.crediagr));

        bankList = new ArrayList<Bank>();
        bankList.addAll(Arrays.asList(PrivatB, OshadB, SberB, RaiphB, UkrsotsB, AlphaB, UkrSibB, PumbB, VtbB, OtpB, CrediAgriB));

        recyclerViewBanks = (RecyclerView)findViewById(R.id.recycler_view_banks_convert);
        recyclerViewBanks.setNestedScrollingEnabled(false);
        recyclerViewBanks.setFocusable(false);
        adapter = new BankListAdapter(bankList, this);
        recyclerViewBanks.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerViewBanks.setLayoutManager(layoutManager);
        recyclerViewBanks.setItemAnimator(itemAnimator);

        for (int i = 0; i < 11; i++)
        {
            bankList.get(i).setNewInformation("06.05.2016 21:47", 25.6150, 25.8300);
        }
        refreshAdapter();
        /*END_BANKS_TEST*/
    }

    public void startConvertActivity() {

        Intent intent = new Intent(MainActivity.this, Converter.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);

                /*transfer parcebale objects to Converter activity*/
        intent.putExtra("currencyId", getCurrencyId());
        intent.putExtra(Nbu.class.getCanonicalName(), nbu);
        intent.putExtra(MBank.class.getCanonicalName(), mBank);
        intent.putExtra(BlackMarket.class.getCanonicalName(), blackMarket);
        intent.putParcelableArrayListExtra(bankList.getClass().getCanonicalName(), bankList);
        startActivity(intent);
    }

    public void refreshAdapter() {
        adapter.addItemstoList(bankList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void setChangesInfo(MBank mBank) {
        if (mBank.getChangesBuy() != 0)
        {
            if (mBank.getChangesBuy() < 0)
            {
                mbBuyChangesText.setText(String.format("%.4f", mBank.getChangesBuy()));
                mbBuyChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                mbBuyChangesText.setText("+" + String.format("%.4f", mBank.getChangesBuy()));
                mbBuyChangesImgUp.setVisibility(View.VISIBLE);
            }
        }

        if (mBank.getChangesSell() != 0)
        {
            if (mBank.getChangesSell() < 0)
            {
                mbSellChangesText.setText(String.format("%.4f", mBank.getChangesSell()));
                mbSellChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                mbSellChangesText.setText("+" + String.format("%.4f", mBank.getChangesSell()));
                mbSellChangesImgUp.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setChangesInfo(BlackMarket blackMarket) {
        if (blackMarket.getChangesBuy() != 0)
        {
            if (blackMarket.getChangesBuy() < 0)
            {
                blackMBuyChangesText.setText(String.format("%.4f", blackMarket.getChangesBuy()));
                blackMBuyChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                blackMBuyChangesText.setText("+" + String.format("%.4f", blackMarket.getChangesBuy()));
                blackMBuyChangesImgUp.setVisibility(View.VISIBLE);
            }
        }

        if (blackMarket.getChangesSell() != 0)
        {
            if (blackMarket.getChangesSell() < 0)
            {
                blackMSellChangesText.setText(String.format("%.4f", blackMarket.getChangesSell()));
                blackMSellChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                blackMSellChangesText.setText("+" + String.format("%.4f", blackMarket.getChangesSell()));
                blackMSellChangesImgUp.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setChangesInfo(Nbu nbu) {
        if (nbu.getChanges() != 0)
        {
            if (nbu.getChanges() < 0)
            {
                nbuChangesText.setText(String.format("%.4f", nbu.getChanges()));
                nbuChangesImgDown.setVisibility(View.VISIBLE);
            }
            else
            {
                nbuChangesText.setText("+" + String.format("%.4f", nbu.getChanges()));
                nbuChangesImgUp.setVisibility(View.VISIBLE);
            }
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
