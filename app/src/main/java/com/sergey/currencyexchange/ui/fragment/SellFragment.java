package com.sergey.currencyexchange.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.ApplicationInfo;
import com.sergey.currencyexchange.model.Utils;
import com.sergey.currencyexchange.ui.BankListAdapterConv;

import java.math.BigDecimal;



public class SellFragment extends Fragment {

    private static final int GRN = 0;
    private static final int USD = 1;
    private static final int EUR = 2;
    private static final int RUB = 3;
    private TextView nbuCurrencyView;
    private TextView nbuSumView;
    private TextView mBankCurrencyView;
    private TextView mBankSumView;
    private TextView blackMarketCurrencyView;
    private TextView blackMarketSumView;
    private RecyclerView recyclerView;
    private BankListAdapterConv adapter;
    private ApplicationInfo applicationInfo;

    private int fromCurrency = USD;
    private int toCurrency = GRN;

    private double[] nbuCurrency;
    private double[] mBankSell;
    private double[] blackMarketSell;


    private static final int SELLFRAGMENT = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewFragmentSell = inflater.inflate(R.layout.fragment, null);
        nbuCurrencyView = (TextView)viewFragmentSell.findViewById(R.id.nbu_currency);
        nbuSumView = (TextView)viewFragmentSell.findViewById(R.id.nbu_sum);
        mBankCurrencyView = (TextView)viewFragmentSell.findViewById(R.id.mBank_currency);
        mBankSumView = (TextView)viewFragmentSell.findViewById(R.id.mBank_sum);
        blackMarketCurrencyView = (TextView)viewFragmentSell.findViewById(R.id.blackMarket_currency);
        blackMarketSumView = (TextView)viewFragmentSell.findViewById(R.id.blackMarket_sum);
        recyclerView = (RecyclerView)viewFragmentSell.findViewById(R.id.recyclerViewBanks);

        applicationInfo = ApplicationInfo.getInstance();
        nbuCurrency = applicationInfo.getNbu().getRate();
        mBankSell = applicationInfo.getMBank().getSell();
        blackMarketSell = applicationInfo.getBlackMarket().getSell();

        nbuCurrencyView.setText(String.format("%.4f", Utils.roundResut(nbuCurrency[0])) + "  |  " + String.format("%.4f", Utils.roundResut(nbuCurrency[1])) + "  |  " + String.format("%.4f", Utils.roundResut(nbuCurrency[2])));
        nbuSumView.setText("0");
        mBankCurrencyView.setText(String.format("%.3f", Utils.roundResut(mBankSell[0])) + "  |  " + String.format("%.3f", Utils.roundResut(mBankSell[1])) + "  |  " + String.format("%.3f", Utils.roundResut(mBankSell[2])));
        mBankSumView.setText("0");
        blackMarketCurrencyView.setText(String.format("%.2f", Utils.roundResut(blackMarketSell[0])) + "  |  " + String.format("%.2f", Utils.roundResut(blackMarketSell[1])) + "  |  " + String.format("%.2f", Utils.roundResut(blackMarketSell[2])));
        blackMarketSumView.setText("0");

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        adapter = new BankListAdapterConv(SELLFRAGMENT, getContext(), fromCurrency, toCurrency);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);

        return viewFragmentSell;
    }


    public void setSumInfo(double count, int fromCurrency, int toCurrency) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        nbuSumView.setText(String.format("%.2f", resultSum(count, fromCurrency, toCurrency, nbuCurrency)));
        mBankSumView.setText(String.format("%.2f",resultSum(count, fromCurrency, toCurrency, mBankSell)));
        blackMarketSumView.setText(String.format("%.2f",resultSum(count, fromCurrency, toCurrency, blackMarketSell)));
        refreshAdapter(count);
    }

    public BigDecimal resultSum(double count, int fromCurrency, int toCurrency, double[] currencies) {
        double rate = countRate(fromCurrency, toCurrency, currencies);
        BigDecimal result = BigDecimal.valueOf(count * rate);
        return result;
    }

    public void refreshAdapter(double count) {
        adapter.refreshInfo(count, fromCurrency, toCurrency);
        adapter.notifyDataSetChanged();
    }

    public double countRate(int fromCurrency, int toCurrency, double[] currencies) {
        double finalRate = 0;
        switch (fromCurrency) {
            case GRN:
                switch (toCurrency) {
                    case GRN:
                        finalRate = 1;
                        break;
                    case USD:
                        finalRate = 1 / currencies[0];
                        break;
                    case EUR:
                        finalRate = 1 / currencies[1];
                        break;
                    case RUB:
                        finalRate = 1 / currencies[2];
                        break;
                }
                break;
            case USD:
                switch (toCurrency) {
                    case GRN:
                        finalRate = currencies[0];
                        break;
                    case USD:
                        finalRate = 1;
                        break;
                    case EUR:
                        finalRate = currencies[0] / currencies[1];
                        break;
                    case RUB:
                        finalRate = currencies[0] / currencies[2];
                        break;
                }
                break;
            case EUR:
                switch (toCurrency) {
                    case GRN:
                        finalRate = currencies[1];
                        break;
                    case USD:
                        finalRate = currencies[1] / currencies[0];
                        break;
                    case EUR:
                        finalRate = 1;
                        break;
                    case RUB:
                        finalRate = currencies[1] / currencies[2];
                        break;
                }
                break;
            case RUB:
                switch (toCurrency) {
                    case GRN:
                        finalRate = currencies[2];
                        break;
                    case USD:
                        finalRate = currencies[2] / currencies[0];
                        break;
                    case EUR:
                        finalRate = currencies[2] / currencies[1];
                        break;
                    case RUB:
                        finalRate = 1;
                        break;
                }
                break;
        }
        return finalRate;
    }
}
