package com.example.weight;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.weight.money.MoneyView;

public class MoneyActivity extends AppCompatActivity implements TextWatcher {

    private MoneyView moneyView;
    private EditText text, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        moneyView = (MoneyView) findViewById(R.id.view_money);
        moneyView.setTotalCircleColor(Color.RED);
        moneyView.setMoneyCircleColor(Color.GRAY);

        text = (EditText) findViewById(R.id.expenses);
        text.addTextChangedListener(this);
        total = (EditText) findViewById(R.id.total);
        total.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(text.getText().toString().trim()) && !TextUtils.isEmpty(total.getText().toString().trim())) {
            moneyView.initData(Double.parseDouble(text.getText().toString().trim()), Double.parseDouble(total.getText().toString().trim()));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
