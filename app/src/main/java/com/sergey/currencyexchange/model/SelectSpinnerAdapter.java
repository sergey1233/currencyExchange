package com.sergey.currencyexchange.model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sergey.currencyexchange.R;
import java.util.ArrayList;


public class SelectSpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<Currency> currencyArray;
    LayoutInflater inflter;

    public SelectSpinnerAdapter(Context applicationContext, ArrayList<Currency> currencyArray) {
        this.context = applicationContext;
        this.currencyArray = currencyArray;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return currencyArray.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_item, null);
        ImageView flag = (ImageView)view.findViewById(R.id.item_spinner_flag);
        ImageView icon = (ImageView) view.findViewById(R.id.item_spinner_icon);
        TextView name = (TextView) view.findViewById(R.id.item_spinner_name);

        if (i == currencyArray.size()) {
            flag.setImageResource(context.getResources().getIdentifier(currencyArray.get(i-1).getCountryFlag(), "drawable", context.getPackageName()));
            icon.setImageResource(context.getResources().getIdentifier(currencyArray.get(i-1).getIconCurrency(), "drawable", context.getPackageName()));
            name.setText(currencyArray.get(i-1).getName());
        }
        else {
            flag.setImageResource(context.getResources().getIdentifier(currencyArray.get(i).getCountryFlag(), "drawable", context.getPackageName()));
            icon.setImageResource(context.getResources().getIdentifier(currencyArray.get(i).getIconCurrency(), "drawable", context.getPackageName()));
            name.setText(currencyArray.get(i).getName());
        }
        return view;
    }
}
