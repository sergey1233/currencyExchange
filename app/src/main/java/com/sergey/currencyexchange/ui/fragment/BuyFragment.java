package com.sergey.currencyexchange.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sergey.currencyexchange.R;

/**
 * Created by Sergey on 11.05.2016.
 */
public class BuyFragment extends Fragment {

    private TextView nbuCurrency;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewFragmentBuy = inflater.inflate(R.layout.fragment, null);
        nbuCurrency = (TextView)viewFragmentBuy.findViewById(R.id.nbu_currency);
        nbuCurrency.setText("");

        return viewFragmentBuy;
    }
}
