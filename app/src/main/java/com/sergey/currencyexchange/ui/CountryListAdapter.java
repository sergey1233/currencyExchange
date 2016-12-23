package com.sergey.currencyexchange.ui;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.Country;
import java.util.ArrayList;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {

    private ArrayList<Country> countries;
    private Context context;
    private static final String TAG = "CountryListAdapter";

    public CountryListAdapter(ArrayList<Country> countries, Context context) {
        this.countries = countries;
        this.context = context;
    }

    @Override
    public CountryListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_countries, viewGroup, false);

        return new CountryListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CountryListAdapter.ViewHolder viewHolder, int i) {
        Country country = countries.get(i);
        try {
            viewHolder.countryFlag.setImageResource(getIconBank(country));
        }
        catch (Exception e) {
            Log.d("adapter", e.getMessage());
        }
        viewHolder.countryName.setText(country.getName());
        if (i == (countries.size() - 1)) {
            viewHolder.seperateLine.setVisibility(View.GONE);
        }
    }

    public int getIconBank(Country country) {
        String iconName = country.getFlag();
        int id = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());

        return id;
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 * 2;
    }

    public void addItemstoList(ArrayList<Country> countries)
    {
        this.countries = countries;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView countryFlag;
        private TextView countryName;
        private View seperateLine;

        public ViewHolder(View itemView) {
            super(itemView);
            countryFlag = (ImageView)itemView.findViewById(R.id.select_country_flag);
            countryName = (TextView)itemView.findViewById(R.id.select_country_name);
            seperateLine = (View)itemView.findViewById(R.id.seperate_line);
        }
    }
}
