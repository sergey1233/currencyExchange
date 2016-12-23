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
import com.sergey.currencyexchange.model.ApplicationInfo;
import com.sergey.currencyexchange.model.Bank;
import com.sergey.currencyexchange.model.Country;
import com.sergey.currencyexchange.model.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class BankListAdapterConv extends RecyclerView.Adapter<BankListAdapterConv.ViewHolder> {
    private ArrayList<Bank> bankList;
    private double count;
    private Context context;
    private Country country;
    private int fromCurrency;
    private int toCurrency;
    private static final String TAG = "BankListAdapter";

    public BankListAdapterConv(Context context, int fromCurrency, int toCurrency) {
        country = ApplicationInfo.getInstance().getCountry();
        this.context = context;
        this.count = 0;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        chekBanklist(country.getBankList());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (Utils.country_code == Utils.UK_CODE) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_banks_convert_uk, viewGroup, false);
        }
        else {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_banks_convert, viewGroup, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BankListAdapterConv.ViewHolder viewHolder, int i) {
        Bank bank = bankList.get(i);

        double[] rateCurrency = bank.getRateConv(fromCurrency, toCurrency);

        try {
            viewHolder.logoBank.setImageResource(getIconBank(bank));
        }
        catch (Exception e) {}

        if (Utils.country_code != Utils.UK_CODE) {
            if (bank.getName().equals("Укрсоцбанк UniCredit Bank TM")) {
                viewHolder.nameBank.setText(bank.getName().substring(0, 10));
            } else {
                viewHolder.nameBank.setText(bank.getName());
            }
        }
        else {
           viewHolder.nameBank.setText("");
        }

        viewHolder.line_recycle.setVisibility(View.VISIBLE);

        viewHolder.bankCurrency.setText(String.valueOf(Utils.roundResut(rateCurrency[0]) + "  |  " + String.valueOf(Utils.roundResut(rateCurrency[1]))));

        viewHolder.bankSum.setText(resultSum(count, rateCurrency));

    }

    public String resultSum(double count, double[] currencies) {
        BigDecimal countBig = new BigDecimal(String.valueOf(count));
        BigDecimal rateFrom;
        BigDecimal rateTo;

        //Because in UK buy and sell have different meaning than in Ukraine and rates are not from pound(like grn), but from another currency
        if (Utils.country_code == Utils.UK_CODE) {
            rateFrom = new BigDecimal(1).divide(new BigDecimal(String.valueOf(currencies[0])), 5, RoundingMode.HALF_UP);
            rateTo = new BigDecimal(1).divide(new BigDecimal(String.valueOf(currencies[1])), 5, RoundingMode.HALF_UP);
        }
        else {
            rateFrom = new BigDecimal(String.valueOf(currencies[0]));
            rateTo = (new BigDecimal(String.valueOf(currencies[1])));
        }

        BigDecimal result;
        try {
            result = (countBig.multiply(rateFrom).divide(rateTo, 5, RoundingMode.HALF_UP));
        }
        catch (ArithmeticException e){
            result = new BigDecimal(0);
        }

        return String.valueOf(result.setScale(2, RoundingMode.HALF_UP));
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

    public void chekBanklist(ArrayList<Bank> bankArray) {
        if (bankArray != null && !bankArray.isEmpty()) {
            this.bankList = new ArrayList<>(bankArray);
            for (Bank bank : bankArray) {
                double[] currencies = bank.getRateConv(fromCurrency, toCurrency);
                if (currencies[0] == 0 || currencies[1] == 0) {
                    this.bankList.remove(bank);
                }
            }
        }
        else {
            this.bankList = bankArray;
        }
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
