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
import com.sergey.currencyexchange.model.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class CBankRatesAdapter extends RecyclerView.Adapter<CBankRatesAdapter.ViewHolder> {
    private ArrayList<Currency> currencyList;
    private Context context;
    private double count = 1;
    private boolean changes = false;
    private static final String TAG = "NbuRatesAdapter";

    public CBankRatesAdapter(ArrayList<Currency> currencyList, Context context) {
        this.currencyList = currencyList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_nbu_currencies, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Currency currency = currencyList.get(i);

        try {
            viewHolder.flagCountry.setImageResource(getIconCountry(currency));
        }
        catch (Exception e) {
            Log.d("adapter", e.getMessage());
        }

        viewHolder.nameCurrency.setText(currency.getName());
        viewHolder.nameCountry.setText(currency.getCountry());
        viewHolder.descriptionCurrency.setText(currency.getDescription());
        if (changes) {
            viewHolder.rateCurrency.setText(getResult(currency, count));
        }
        else {
            viewHolder.rateCurrency.setText(String.valueOf(currency.getRate()));
        }
        viewHolder.dateRate.setText(currency.getDate());
        if (i == currencyList.size())
        {
            viewHolder.line_recycle.setVisibility(View.GONE);
        }
    }

    public String getResult(Currency currency, double count) {
        BigDecimal countBig = new BigDecimal(String.valueOf(count));
        BigDecimal rateBig = new BigDecimal(String.valueOf(currency.getRate()));
        BigDecimal result = rateBig.multiply(countBig);

        return String.valueOf(result.setScale(2, RoundingMode.HALF_UP));
    }

    public int getIconCountry(Currency currency) {
        String iconName = currency.getCountryFlag();
        int id = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());

        return id;
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 * 2;
    }

    public void addItemstoList(ArrayList<Currency> currencyList) {
        this.currencyList = currencyList;
    }

    public void addCount(double count, boolean changes) {
        this.count = count;
        this.changes = changes;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView flagCountry;
        private TextView nameCurrency;
        private TextView nameCountry;
        private TextView descriptionCurrency;
        private TextView rateCurrency;
        private TextView dateRate;
        private View line_recycle;


        public ViewHolder(View itemView) {
            super(itemView);
            flagCountry = (ImageView)itemView.findViewById(R.id.nbu_rates_country_flag);
            nameCurrency = (TextView)itemView.findViewById(R.id.nbu_rates_currency_name);
            nameCountry = (TextView)itemView.findViewById(R.id.nbu_rates_country_name);
            descriptionCurrency = (TextView)itemView.findViewById(R.id.nbu_rates_currency_description);
            rateCurrency = (TextView)itemView.findViewById(R.id.nbu_rates_rate);
            dateRate = (TextView)itemView.findViewById(R.id.nbu_rates_date);
            line_recycle = (View)itemView.findViewById(R.id.line_recycle);
        }
    }
}
