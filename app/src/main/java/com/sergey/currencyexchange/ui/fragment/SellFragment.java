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
import com.sergey.currencyexchange.model.Bank;
import com.sergey.currencyexchange.model.BlackMarket;
import com.sergey.currencyexchange.model.MBank;
import com.sergey.currencyexchange.model.Nbu;
import com.sergey.currencyexchange.model.Utils;
import com.sergey.currencyexchange.ui.BankListAdapterConv;

import java.math.BigDecimal;
import java.util.ArrayList;


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

        nbuCurrencyView.setText(String.format("%.4f", Utils.roundResut(nbuCurrency)));
        nbuSumView.setText("0");
        mBankCurrencyView.setText(String.format("%.3f", Utils.roundResut(mBankSell)));
        mBankSumView.setText("0");
        blackMarketCurrencyView.setText(String.format("%.2f", Utils.roundResut(blackMarketSell)));
        blackMarketSumView.setText("0");

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        adapter = new BankListAdapterConv(bankListSell, SELLFRAGMENT, ApplicationInfo.getInstance().getCurrencyId(), getContext());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);

        return viewFragmentSell;
    }


    public void setSumInfo(double count)
    {
        nbuSumView.setText(String.format("%.2f",resultSumNbu(count)));
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
        BigDecimal x = BigDecimal.valueOf(count * mBankSell);
        return x;
    }

    public BigDecimal resultSumBlackM(double count)
    {
        BigDecimal x = BigDecimal.valueOf(count * blackMarketSell);
        return x;
    }

    public void refreshAdapter(double count)
    {
        adapter.refreshCount(count);
        adapter.notifyDataSetChanged();
    }
}
