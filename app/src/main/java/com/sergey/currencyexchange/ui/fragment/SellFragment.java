package com.sergey.currencyexchange.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sergey.currencyexchange.R;

/**
 * Created by Sergey on 11.05.2016.
 */
public class SellFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewFragmentSell = inflater.inflate(R.layout.fragment, null);
        return viewFragmentSell;
    }
}
