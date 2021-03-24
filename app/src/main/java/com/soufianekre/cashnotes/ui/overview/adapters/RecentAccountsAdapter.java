package com.soufianekre.cashnotes.ui.overview.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soufianekre.cashnotes.MyApp;
import com.soufianekre.cashnotes.R;
import com.soufianekre.cashnotes.data.app_preference.PrefsConst;
import com.soufianekre.cashnotes.data.db.model.CashAccount;
import com.soufianekre.cashnotes.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentAccountsAdapter extends RecyclerView.Adapter<RecentAccountsAdapter.AccountReducedViewHolder> {

    private final List<CashAccount> recentAccountList;

    public RecentAccountsAdapter(Context mContext, List<CashAccount> accountList) {
        this.recentAccountList = accountList;
    }

    @NonNull
    @Override
    public AccountReducedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_account,
                parent,false);
        return new AccountReducedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountReducedViewHolder holder, int position) {
        holder.onBind(position);

    }

    @Override
    public int getItemCount() {
        return recentAccountList.size();
    }

    public void addItems(List<CashAccount> accounts){
        recentAccountList.clear();
        recentAccountList.addAll(accounts);
        notifyDataSetChanged();
    }

    @SuppressLint("NonConstantResourceId")
    public class AccountReducedViewHolder extends BaseViewHolder {
        @BindView(R.id.recent_account_color_img)
        ImageView accountColorImg;
        @BindView(R.id.recent_account_name_text)
        TextView accountName;

        @BindView(R.id.recent_account_transaction_count)
        TextView accountTransactionsCounter;


        AccountReducedViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            CashAccount cashAccount = recentAccountList.get(position);
            accountName.setText(cashAccount.getName());
            accountColorImg.setBackgroundTintList(ColorStateList.valueOf(cashAccount.getColor()));
        }
    }
}
