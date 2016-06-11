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
import com.sergey.currencyexchange.model.Utils;
import java.util.ArrayList;


public class BankListAdapter extends RecyclerView.Adapter<BankListAdapter.ViewHolder> {

    private ArrayList<Bank> bankList;
    private Context context;
    private int currencyId = 0;
    private static final String TAG = "BankListAdapter";

    public BankListAdapter(ArrayList<Bank> bankList, int currencyId, Context context)
    {
        this.bankList = bankList;
        this.currencyId = currencyId;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_banks, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Bank bank = bankList.get(i);

        try {
            viewHolder.logoBank.setImageDrawable(getIconBank(bank));
        }
        catch (Exception e) {}

        if (bank.getName().equals("Укрсоцбанк UniCredit Bank TM")) {
            viewHolder.nameBank.setText(bank.getName().substring(0, 10));
        }
        else {
            viewHolder.nameBank.setText(bank.getName());
        }
        viewHolder.bankDate.setText(bank.getDate());
        viewHolder.bankBuyRate.setText(String.format("%.2f", Utils.roundResut(bank.getBuy(currencyId))));
        viewHolder.bankSellRate.setText(String.format("%.2f", Utils.roundResut(bank.getSell(currencyId))));
        setChangesInfo(viewHolder, bank);
        if (i == bankList.size())
        {
            viewHolder.line_recycle.setVisibility(View.GONE);
        }
    }

    public Drawable getIconBank(Bank bank) {
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

    public void addItemstoList(ArrayList<Bank> bankList)
    {
        this.bankList = bankList;
    }

    public void addCurrencyId(int currencyId)
    {
        this.currencyId = currencyId;
    }

    public void setChangesInfo(ViewHolder viewHolder, Bank bank) {
        if (bank.getChangesBuy(currencyId) != 0)
        {
            if (bank.getChangesBuy(currencyId) < 0)
            {
                viewHolder.bank_buy_changes_img_up.setVisibility(View.INVISIBLE);
                viewHolder.bank_buy_changes_img_down.setVisibility(View.VISIBLE);
            }
            else
            {
                viewHolder.bank_buy_changes_img_up.setVisibility(View.VISIBLE);
                viewHolder.bank_buy_changes_img_down.setVisibility(View.INVISIBLE);
            }
        }

        if (bank.getChangesSell(currencyId) != 0)
        {
            if (bank.getChangesSell(currencyId) < 0)
            {
                viewHolder.bank_sell_changes_img_up.setVisibility(View.INVISIBLE);
                viewHolder.bank_sell_changes_img_down.setVisibility(View.VISIBLE);
            }
            else
            {
                viewHolder.bank_sell_changes_img_up.setVisibility(View.VISIBLE);
                viewHolder.bank_sell_changes_img_down.setVisibility(View.INVISIBLE);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView logoBank;
        private TextView nameBank;
        private TextView bankDate;
        private TextView bankBuyRate;
        private TextView bankSellRate;
        private ImageView bank_buy_changes_img_up;
        private ImageView bank_buy_changes_img_down;
        private ImageView bank_sell_changes_img_up;
        private ImageView bank_sell_changes_img_down;
        private View line_recycle;


        public ViewHolder(View itemView) {
            super(itemView);
            logoBank = (ImageView)itemView.findViewById(R.id.logo_bank);
            nameBank = (TextView)itemView.findViewById(R.id.name_bank);
            bankDate = (TextView)itemView.findViewById(R.id.bank_date);
            bankBuyRate = (TextView)itemView.findViewById(R.id.bank_buy_rate);
            bankSellRate = (TextView)itemView.findViewById(R.id.bank_sell_rate);
            bank_buy_changes_img_up = (ImageView)itemView.findViewById(R.id.bank_buy_changes_img_up);
            bank_buy_changes_img_down = (ImageView)itemView.findViewById(R.id.bank_buy_changes_img_down);
            bank_sell_changes_img_up = (ImageView)itemView.findViewById(R.id.bank_sell_changes_img_up);
            bank_sell_changes_img_down = (ImageView)itemView.findViewById(R.id.bank_sell_changes_img_down);
            line_recycle = (View)itemView.findViewById(R.id.line_recycle);
        }
    }
}
