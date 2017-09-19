package com.example.weight.bank;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.weight.R;

import java.util.List;

public class BankListAdapter extends RecyclerView.Adapter {
    Context context;
    List data;
    int[] colorId = {R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark};

    public BankListAdapter(Context context, List data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bank_list, parent, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = context.getResources().getDimensionPixelSize(R.dimen.bank_item_margin);
        view.setLayoutParams(params);
        return new BankHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BankHolder) holder).getView().setBackgroundResource(colorId[position % 3]);
        if (position == 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ((BankHolder) holder).getView().setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private class BankHolder extends RecyclerView.ViewHolder {
        private View view;

        public BankHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public View getView() {
            return view;
        }
    }
}
