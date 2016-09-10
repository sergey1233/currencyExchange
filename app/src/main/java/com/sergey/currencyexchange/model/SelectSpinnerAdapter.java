package com.sergey.currencyexchange.model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sergey.currencyexchange.R;


public class SelectSpinnerAdapter extends BaseAdapter {
    Context context;
    String[] currencies;
    int[] flags;
    int[] icons;

    LayoutInflater inflter;

    public SelectSpinnerAdapter(Context applicationContext, String[] currencies, int[] flags, int[] icons) {
        this.context = applicationContext;
        this.currencies = currencies;
        this.flags = flags;
        this.icons = icons;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return flags.length;
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
        flag.setImageResource(flags[i]);
        icon.setImageResource(icons[i]);
        name.setText(currencies[i]);
        return view;
    }
}
