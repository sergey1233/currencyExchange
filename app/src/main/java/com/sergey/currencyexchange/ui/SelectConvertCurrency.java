package com.sergey.currencyexchange.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import com.sergey.currencyexchange.R;
import com.sergey.currencyexchange.model.SelectSpinnerAdapter;


public class SelectConvertCurrency extends AppCompatActivity {

    private static final String TAG = "SelectConvertCurrency";
    private static final int MAINACTIVITYTYPE = 0;
    private static final int CONVERTERTYPE = 1;
    private static final int SELECTCURRENCYTYPE = 2;
    private static final int SELECTCONVERTCURRENCYTYPE = 3;
    private static final int TYPEACTIVITY = SELECTCONVERTCURRENCYTYPE;
    private static final int GRN = 0;
    private static final int USD = 1;
    private static final int EUR = 2;
    private static final int RUB = 3;
    private int fromCurrency;
    private int toCurrency;
    private Button buttonSelectSpinner;
    private Spinner selectFromSpinner;
    private Spinner selectToSpinner;
    private String[] currencies = {"GRN", "USD", "EUR", "RUB"};
    private int[] flags = {R.drawable.flag_ukraine_dark, R.drawable.flag_usa_dark, R.drawable.flag_europe_dark, R.drawable.flag_russia_dark};
    private int[] icons = {R.drawable.icon_grn_dark, R.drawable.icon_dollar_dark, R.drawable.icon_euro_dark, R.drawable.icon_rb_dark};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_convert_currency);

        selectFromSpinner = (Spinner)findViewById(R.id.select_from_spinner);
        selectToSpinner = (Spinner)findViewById(R.id.select_to_spinner);
        final SelectSpinnerAdapter selectSpinnerAdapter = new SelectSpinnerAdapter(SelectConvertCurrency.this, currencies, flags, icons);
        selectFromSpinner.setAdapter(selectSpinnerAdapter);
        selectToSpinner.setAdapter(selectSpinnerAdapter);


        setSpinnerSelection();

        selectFromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectSpinnerAdapter.notifyDataSetChanged();
                if (currencies[i].equals(currencies[0])) {
                    fromCurrency = GRN;
                }
                else if (currencies[i].equals(currencies[1])) {
                    fromCurrency = USD;
                }
                else if (currencies[i].equals(currencies[2])) {
                    fromCurrency = EUR;
                }
                else if (currencies[i].equals(currencies[3])) {
                    fromCurrency = RUB;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });
        selectToSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectSpinnerAdapter.notifyDataSetChanged();
                if (currencies[i].equals(currencies[0])) {
                    toCurrency = GRN;
                }
                else if (currencies[i].equals(currencies[1])) {
                    toCurrency = USD;
                }
                else if (currencies[i].equals(currencies[2])) {
                    toCurrency = EUR;
                }
                else if (currencies[i].equals(currencies[3])) {
                    toCurrency = RUB;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });

        buttonSelectSpinner = (Button)findViewById(R.id.spinner_ok);
        buttonSelectSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectConvertCurrency.this, Converter.class);
                intent.putExtra("fromActivity", TYPEACTIVITY);
                intent.putExtra("fromCurrency", fromCurrency);
                intent.putExtra("toCurrency", toCurrency);
                startActivity(intent);
            }
        });
    }

    public void setSpinnerSelection() {
        fromCurrency = getIntent().getIntExtra("fromCurrency", 0);
        toCurrency = getIntent().getIntExtra("toCurrency", 0);
        selectFromSpinner.setSelection(fromCurrency);
        selectToSpinner.setSelection(toCurrency);
    }
}

