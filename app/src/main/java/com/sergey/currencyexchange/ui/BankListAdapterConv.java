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
import com.sergey.currencyexchange.model.ApplicationInfo;
import com.sergey.currencyexchange.model.Bank;
import com.sergey.currencyexchange.model.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class BankListAdapterConv extends RecyclerView.Adapter<BankListAdapterConv.ViewHolder> {
    private static final int GRN = 0;
    private static final int USD = 1;
    private static final int EUR = 2;
    private static final int RUB = 3;
    private int typefragment; //0 - buy; 1 - sell;
    private ArrayList<Bank> bankList;
    private double count;
    private Context context;
    private ApplicationInfo applicationInfo;
    private int fromCurrency;
    private int toCurrency;
    private static final String TAG = "BankListAdapter";

    public BankListAdapterConv(int typefragment, Context context, int fromCurrency, int toCurrency) {
        applicationInfo = ApplicationInfo.getInstance();
        this.bankList = applicationInfo.getBankList();
        this.typefragment = typefragment;
        this.context = context;
        this.count = 0;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_banks_convert, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BankListAdapterConv.ViewHolder viewHolder, int i) {
        Bank bank = bankList.get(i);
        double[] buyCurrency = bank.getBuy();
        double[] sellCurrency = bank.getSell();

        try {
            viewHolder.logoBank.setImageResource(getIconBank(bank));
        }
        catch (Exception e) {}

        if (bank.getName().equals("Укрсоцбанк UniCredit Bank TM")) {
            viewHolder.nameBank.setText(bank.getName().substring(0, 10));
        }
        else {
            viewHolder.nameBank.setText(bank.getName());
        }
        viewHolder.line_recycle.setVisibility(View.VISIBLE);
        if (typefragment == 0)
        {
            viewHolder.bankCurrency.setText(String.format("%.2f", Utils.roundResut(buyCurrency[0])) + "  |  " + String.format("%.2f", Utils.roundResut(buyCurrency[1])) + "  |  " + String.format("%.2f", Utils.roundResut(buyCurrency[2])));
            viewHolder.bankSum.setText(String.format("%.2f", resultSum(count, fromCurrency, toCurrency, bank.getBuy())));
        }
        else
        {
            viewHolder.bankCurrency.setText(String.format("%.2f", Utils.roundResut(sellCurrency[0])) + "  |  " + String.format("%.2f", Utils.roundResut(sellCurrency[1])) + "  |  " + String.format("%.2f", Utils.roundResut(sellCurrency[2])));
            viewHolder.bankSum.setText(String.format("%.2f", resultSum(count, fromCurrency, toCurrency, bank.getSell())));
        }
    }

    public BigDecimal resultSum(double count, int fromCurrency, int toCurrency, double[] currencies) {
        double rate = countRate(fromCurrency, toCurrency, currencies);
        BigDecimal result = BigDecimal.valueOf(count * rate);
        return result;
    }

    public double countRate(int fromCurrency, int toCurrency, double[] currencies) {
        double finalRate = 0;
        switch (fromCurrency) {
            case GRN:
                switch (toCurrency) {
                    case GRN:
                        finalRate = 1;
                        break;
                    case USD:
                        finalRate = 1 / currencies[0];
                        break;
                    case EUR:
                        finalRate = 1 / currencies[1];
                        break;
                    case RUB:
                        finalRate = 1 / currencies[2];
                        break;
                }
                break;
            case USD:
                switch (toCurrency) {
                    case GRN:
                        finalRate = currencies[0];
                        break;
                    case USD:
                        finalRate = 1;
                        break;
                    case EUR:
                        finalRate = currencies[0] / currencies[1];
                        break;
                    case RUB:
                        finalRate = currencies[0] / currencies[2];
                        break;
                }
                break;
            case EUR:
                switch (toCurrency) {
                    case GRN:
                        finalRate = currencies[1];
                        break;
                    case USD:
                        finalRate = currencies[1] / currencies[0];
                        break;
                    case EUR:
                        finalRate = 1;
                        break;
                    case RUB:
                        finalRate = currencies[1] / currencies[2];
                        break;
                }
                break;
            case RUB:
                switch (toCurrency) {
                    case GRN:
                        finalRate = currencies[2];
                        break;
                    case USD:
                        finalRate = currencies[2] / currencies[0];
                        break;
                    case EUR:
                        finalRate = currencies[2] / currencies[1];
                        break;
                    case RUB:
                        finalRate = 1;
                        break;
                }
                break;
        }
        return finalRate;
    }

    public int getIconBank(Bank bank) {
        String iconName = bank.getIcon();
        int id = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());

        return id;
    }

    @Override
    public int getItemCount() {
        return bankList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 * 2;
    }

    public void refreshInfo(double count, int fromCurrency, int toCurrency) {
        this.count = count;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
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
