package com.sergey.currencyexchange.ui;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.Bank;
import com.sergey.currencyexchange.model.NumberUtils;

import java.util.List;

/**
 * Created by Sergey on 16.05.2016.
 */
public class BankListAdapterConv extends RecyclerView.Adapter<BankListAdapterConv.ViewHolder> {
    private int typefragment; //0 - buy; 1 - sell;
    private List<Bank> bankList;
    private double count;
    private Context context;
    private static final String TAG = "BankListAdapter";

    public BankListAdapterConv(List<Bank> bankList, int typefragment, Context context)
    {
        this.bankList = bankList;
        this.typefragment = typefragment;
        this.context = context;
        this.count = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_banks_convert, viewGroup, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(BankListAdapterConv.ViewHolder viewHolder, int i) {
        Bank bank = bankList.get(i);

        try {
            viewHolder.logoBank.setImageDrawable(getIconBank(bank));
        }
        catch (Exception e) {}

        viewHolder.nameBank.setText(bank.getName());
        viewHolder.line_recycle.setVisibility(View.VISIBLE);
        if (typefragment == 0)
        {
            viewHolder.bankCurrency.setText(String.format("%.2f", NumberUtils.roundResut(bank.getBuy())));
            viewHolder.bankSum.setText(String.format("%.2f", resultSum(count, bank.getBuy())));
        }
        else
        {
            viewHolder.bankCurrency.setText(String.format("%.2f", NumberUtils.roundResut(bank.getSell())));
            viewHolder.bankSum.setText(String.format("%.2f", resultSum(count, bank.getSell())));
        }

        if (i == bankList.size() - 1)
        {
            viewHolder.line_recycle.setVisibility(View.GONE);
        }
    }

    public double resultSum(double count, double currency)
    {
        return NumberUtils.roundResut(count * currency);
    }

    public Drawable getIconBank(Bank bank)
    {
        String iconName = bank.getIcon();
        int id = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        Drawable iconBank = context.getResources().getDrawable(id);

        return iconBank;
    }

    @Override
    public int getItemCount() {
        return bankList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 * 2;
    }

    public void refreshCount(double count)
    {
        this.count = count;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView logoBank;
        private TextView nameBank;
        private TextView bankCurrency;
        private TextView bankSum;
        private View line_recycle;


        public ViewHolder(View itemView) {
            super(itemView);
            logoBank = (ImageView)itemView.findViewById(R.id.logo_bank);
            nameBank = (TextView)itemView.findViewById(R.id.name_bank);
            bankSum = (TextView)itemView.findViewById(R.id.bank_sum);
            bankCurrency = (TextView)itemView.findViewById(R.id.bank_currency);
            line_recycle = (View)itemView.findViewById(R.id.line_recycle_conv);
        }
    }
}
