package com.sergey.currencyexchange.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.Currency;
import com.sergey.currencyexchange.model.Utils;
import java.util.ArrayList;


public class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Currency> selectCurrencyList;
    private Intent intent;

    public CurrencyListAdapter(ArrayList<Currency> selectCurrencyList, Context context) {
        this.selectCurrencyList = selectCurrencyList;
        this.context = context;
        intent = new Intent(context, MainActivity.class);
        intent.putExtra("fromActivity", Utils.SELECTCURRENCYTYPE);
    }

    @Override
    public CurrencyListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_select_currencies, viewGroup, false);

        return new CurrencyListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CurrencyListAdapter.ViewHolder viewHolder, int i) {
        final Currency currency = selectCurrencyList.get(i);

        viewHolder.rippleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.currencyId = currency.getCurrencyId();
                context.startActivity(intent);
            }
        });

        try {
            viewHolder.countryFlag.setImageResource(getImage(currency.getCountryFlag()));
        }
        catch (Exception e) {
            Log.d("adapter", e.getMessage());
        }

        try {
            viewHolder.currencyIcon.setImageResource(getImage(currency.getIconCurrency()));
        }
        catch (Exception e) {
            Log.d("adapter", e.getMessage());
        }

        viewHolder.currencyName.setText(currency.getName());

        if (i == (selectCurrencyList.size() - 1))
        {
            viewHolder.seperateLine.setVisibility(View.GONE);
        }
    }

    public int getImage(String iconName) {
        int id = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());

        return id;
    }

    public void addItemstoList(ArrayList<Currency> selectCurrencyList) {
        this.selectCurrencyList = selectCurrencyList;
    }

    @Override
    public int getItemCount() {
        return selectCurrencyList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 * 2;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialRippleLayout rippleLayout;
        private ImageView countryFlag;
        private ImageView currencyIcon;
        private TextView currencyName;
        private View seperateLine;

        public ViewHolder(View itemView) {
            super(itemView);
            rippleLayout = (MaterialRippleLayout)itemView.findViewById(R.id.ripple_view);
            countryFlag = (ImageView)itemView.findViewById(R.id.select_currency_country_flag);
            currencyIcon = (ImageView)itemView.findViewById(R.id.select_currency_currency_icon);
            currencyName = (TextView)itemView.findViewById(R.id.select_currency_country_name);
            seperateLine = (View)itemView.findViewById(R.id.seperate_line);
        }
    }
}
