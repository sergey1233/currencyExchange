package com.sergey.currencyexchange.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.ApplicationInfo;
import com.sergey.currencyexchange.model.Country;
import com.sergey.currencyexchange.model.Utils;
import com.sergey.currencyexchange.services.RecyclerItemClickListener;
import com.sergey.currencyexchange.ui.CountryListAdapter;
import com.sergey.currencyexchange.ui.MainActivity;
import java.util.ArrayList;


public class SelectCountryFragment extends Fragment {

    private AdView mAdView;
    private RecyclerView recyclerViewCountries;
    private CountryListAdapter adapter;
    private ArrayList<Country> countries;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_country, null);

        initializeAdmob(view);
        initializeData();
        initializeViewVariables(view);

        return view;
    }

    public void initializeAdmob(View view) {
        mAdView = (AdView)view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void initializeData() {
        countries = ApplicationInfo.getInstance().getCountries();
    }

    public void initializeViewVariables(View view) {
        recyclerViewCountries = (RecyclerView)view.findViewById(R.id.recycler_view_countries);
        recyclerViewCountries.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewCountries, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Utils.country_code = countries.get(position).getCode();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("fromActivity", Utils.POPUPTYPE);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) { }
        }));
        adapter = new CountryListAdapter(countries, getContext());
        recyclerViewCountries.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerViewCountries.setLayoutManager(layoutManager);
        recyclerViewCountries.setItemAnimator(itemAnimator);
    }
}
