package com.sergey.currencyexchange.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ImageButton mainToolBarImage;
    private ImageButton converterToolBarImage;
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
    private GraphView graph;


    private RecyclerView recyclerViewBanks;
    private BankListAdapter adapter;
    private ArrayList<Bank> bankList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolBarImage = (ImageButton)findViewById(R.id.icon_main_toolbar);
        converterToolBarImage = (ImageButton)findViewById(R.id.icon_converter_toolbar);
        mainToolBarImage.setColorFilter(Color.argb(255, 255, 255, 255));

        converterToolBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Converter.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });


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



        /*NBU TEST*/
        nbu = new Nbu("14.03.16 15:06", 24.019960);
        nbu.setNewInformation("11.05.16 16:08", 26.58741);
        nbuRate.setText(String.valueOf(String.format("%.4f",nbu.getCurrency())));
        nbuDate.setText(nbu.getDate());
        setChangesInfo(nbu);
        /*END NBU TEST*/

        /*MB TEST*/
        ArrayList<String> mBankDates = new ArrayList<>();
        ArrayList<Double> mBankBuy = new ArrayList<>();
        ArrayList<Double> mBankSell = new ArrayList<>();
        mBankDates.addAll(Arrays.asList("12.05.16 10:08", "12.05.16 11:08", "12.05.16 12:08", "12.05.16 13:08", "12.05.16 14:08"));
        mBankBuy.addAll(Arrays.asList(25.3100, 25.2400, 25.2500, 25.3200, 25.3400));
        mBankSell.addAll(Arrays.asList(25.3700, 25.3100, 25.3200, 25.3600, 25.3500));
        mBank = new MBank(mBankDates, mBankBuy, mBankSell);
        //mBank.setNewInformation(mBankDates, mBankBuy, mBankSell);

        mbBuyRate.setText(String.format("%.4f",mBank.getBuy()));
        mbSellRate.setText(String.format("%.4f",mBank.getSell()));
        mbDate.setText(mBank.getDate());
        setChangesInfo(mBank);
        /*END MB TEST*/


        /*GRAPH TEST*/
        ArrayList<Double> buyInfoForGraph = mBank.getBuyArray();
        ArrayList<Double> sellInfoForGraph = mBank.getSellArray();
        ArrayList<String> dateInfoForgGraph = mBank.getDateArray();
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


        /*BlackMarket TEST*/
        BlackMarket blackMarket = new BlackMarket("14.03.16 15:06", 24.019960, 27.9572 );
        blackMarket.setNewInformation("11.05.16 16:08", 26.58741, 28.8574);
        blackMBuyRate.setText(String.format("%.4f",blackMarket.getBuy()));
        blackMSellRate.setText(String.format("%.4f",blackMarket.getSell()));
        blackMDate.setText(blackMarket.getDate());
        setChangesInfo(blackMarket);
        /*END BlackMarket TEST*/



        /*BANKS_TEST*/
        Bank PrivatB = new Bank("ПриватБанк");
        Bank OshadB = new Bank("ОщадБанк");
        Bank SberB = new Bank("Сбербанк");
        Bank RaiphB = new Bank("Райффайзен Банк Аваль");
        Bank UkrsotsB = new Bank("Укрсоцбанк");
        Bank AlphaB = new Bank("Альфа-Банк");
        Bank UkrSibB = new Bank("УкрСиббанк");
        Bank PumbB = new Bank("ПУМБ");
        Bank VtbB = new Bank("ВТБ Банк");
        Bank OtpB = new Bank("ОТП Банк");
        Bank KrediAgriB = new Bank("Креди Агриколь Банк");

        bankList = new ArrayList<Bank>();
        bankList.addAll(Arrays.asList(PrivatB, OshadB, SberB, RaiphB, UkrsotsB, AlphaB, UkrSibB, PumbB, VtbB, OtpB, KrediAgriB));

        recyclerViewBanks = (RecyclerView)findViewById(R.id.recyclerViewBanks);
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
            bankList.get(i).setInformationOfBank("06.05.2016 9:46", 25.6000, 25.6400);
        }
        refreshAdapter();

        for (int i = 0; i < 11; i++)
        {
            bankList.get(i).setInformationOfBank("06.05.2016 9:47", 25.6150, 25.6100);
        }
        refreshAdapter();
        /*END_BANKS_TEST*/

    }

    public void refreshAdapter()
    {
        adapter.addItemstoList(bankList);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void setChangesInfo(MBank mBank)
    {
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

    public void setChangesInfo(BlackMarket blackMarket)
    {
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

    public void setChangesInfo(Nbu nbu)
    {
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

}
