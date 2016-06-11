package com.sergey.currencyexchange.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.ApplicationInfo;
import com.sergey.currencyexchange.model.Bank;
import com.sergey.currencyexchange.model.BlackMarket;
import com.sergey.currencyexchange.model.MBank;
import com.sergey.currencyexchange.model.Nbu;
import com.sergey.currencyexchange.model.Utils;
import com.sergey.currencyexchange.ui.BankListAdapterConv;

import java.math.BigDecimal;
import java.util.ArrayList;


public class BuyFragment extends Fragment {


    private TextView nbuCurrencyView;
    private TextView nbuSumView;
    private TextView mBankCurrencyView;
    private TextView mBankSumView;
    private TextView blackMarketCurrencyView;
    private TextView blackMarketSumView;
    private RecyclerView recyclerView;
    private BankListAdapterConv adapter;

    private Bundle bundle;
    private double nbuCurrency;
    private double mBankBuy;
    private double blackMarketBuy;
    private ArrayList<Bank> bankListBuy;


    private static final int BUYFRAGMENT = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewFragmentBuy = inflater.inflate(R.layout.fragment, null);

        nbuCurrencyView = (TextView)viewFragmentBuy.findViewById(R.id.nbu_currency);
        nbuSumView = (TextView)viewFragmentBuy.findViewById(R.id.nbu_sum);
        mBankCurrencyView = (TextView)viewFragmentBuy.findViewById(R.id.mBank_currency);
        mBankSumView = (TextView)viewFragmentBuy.findViewById(R.id.mBank_sum);
        blackMarketCurrencyView = (TextView)viewFragmentBuy.findViewById(R.id.blackMarket_currency);
        blackMarketSumView = (TextView)viewFragmentBuy.findViewById(R.id.blackMarket_sum);
        recyclerView = (RecyclerView)viewFragmentBuy.findViewById(R.id.recyclerViewBanks);

        bundle = getArguments();
        nbuCurrency = bundle.getDouble(Nbu.class.getCanonicalName());
        mBankBuy = bundle.getDouble(MBank.class.getCanonicalName());
        blackMarketBuy = bundle.getDouble(BlackMarket.class.getCanonicalName());
        bankListBuy = bundle.getParcelableArrayList(ArrayList.class.getCanonicalName());

        nbuCurrencyView.setText(String.format("%.4f", Utils.roundResut(nbuCurrency)));
        nbuSumView.setText("0");
        mBankCurrencyView.setText(String.format("%.3f", Utils.roundResut(mBankBuy)));
        mBankSumView.setText("0");
        blackMarketCurrencyView.setText(String.format("%.2f", Utils.roundResut(blackMarketBuy)));
        blackMarketSumView.setText("0");

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        adapter = new BankListAdapterConv(bankListBuy, BUYFRAGMENT, ApplicationInfo.getInstance().getCurrencyId(), getContext());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);

        return viewFragmentBuy;
    }

    public void setSumInfo(double count)
    {
        nbuSumView.setText(String.format("%.2f", resultSumNbu(count)));
        mBankSumView.setText(String.format("%.2f",resultSumMBank(count)));
        blackMarketSumView.setText(String.format("%.2f",resultSumBlackM(count)));
        refreshAdapter(count);
    }

    public BigDecimal resultSumNbu(double count)
    {
        BigDecimal x = BigDecimal.valueOf(count * nbuCurrency);
        return x;
    }

    public BigDecimal resultSumMBank(double count)
    {
        BigDecimal x = BigDecimal.valueOf(count * mBankBuy);
        return x;
    }

    public BigDecimal resultSumBlackM(double count)
    {
        BigDecimal x = BigDecimal.valueOf(count * blackMarketBuy);
        return x;
    }

    public void refreshAdapter(double count)
    {
        adapter.refreshCount(count);
        adapter.notifyDataSetChanged();
    }
}
