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
import com.sergey.currencyexchange.model.Bank;
import com.sergey.currencyexchange.model.BlackMarket;
import com.sergey.currencyexchange.model.MBank;
import com.sergey.currencyexchange.model.Nbu;
import com.sergey.currencyexchange.model.NumberUtils;
import com.sergey.currencyexchange.ui.BankListAdapterConv;

import java.util.ArrayList;

/**
 * Created by Sergey on 11.05.2016.
 */
public class SellFragment extends Fragment {

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
    private double mBankSell;
    private double blackMarketSell;
    private ArrayList<Bank> bankListSell;

    private double nbuSum;
    private double mBankSum;
    private double blackMarketSum;

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

        bundle = getArguments();
        nbuCurrency = bundle.getDouble(Nbu.class.getCanonicalName());
        mBankSell = bundle.getDouble(MBank.class.getCanonicalName());
        blackMarketSell = bundle.getDouble(BlackMarket.class.getCanonicalName());
        bankListSell = bundle.getParcelableArrayList(ArrayList.class.getCanonicalName());

        nbuCurrencyView.setText(String.format("%.2f", NumberUtils.roundResut(nbuCurrency)));
        nbuSumView.setText("0");
        mBankCurrencyView.setText(String.format("%.2f", NumberUtils.roundResut(mBankSell)));
        mBankSumView.setText("0");
        blackMarketCurrencyView.setText(String.format("%.2f", NumberUtils.roundResut(blackMarketSell)));
        blackMarketSumView.setText("0");

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        adapter = new BankListAdapterConv(bankListSell, SELLFRAGMENT, getContext());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);

        return viewFragmentSell;
    }


    public void setSumInfo(double count)
    {
        nbuSumView.setText(String.format("%.2f", resultSumNbu(count)));
        mBankSumView.setText(String.format("%.2f", resultSumMBank(count)));
        blackMarketSumView.setText(String.format("%.2f", resultSumBlackM(count)));
        refreshAdapter(count);
    }

    public double resultSumNbu(double count)
    {
        nbuSum = count * nbuCurrency;
        return NumberUtils.roundResut(nbuSum);
    }

    public double resultSumMBank(double count)
    {
        mBankSum = count * mBankSell;
        return NumberUtils.roundResut(mBankSum);
    }

    public double resultSumBlackM(double count)
    {
        blackMarketSum = count * blackMarketSell;
        return NumberUtils.roundResut(blackMarketSum);
    }

    public void refreshAdapter(double count)
    {
        adapter.refreshCount(count);
        adapter.notifyDataSetChanged();
    }
}
