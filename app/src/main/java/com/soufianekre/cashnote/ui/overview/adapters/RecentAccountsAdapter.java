package com.soufianekre.cashnote.ui.overview.adapters;


import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soufianekre.cashnote.MyApp;
import com.soufianekre.cashnote.R;
import com.soufianekre.cashnote.data.app_preference.PrefsConst;
import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.ui.base.BaseViewHolder;

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

    private int getAccountTotalBalance(int position){
        int totalBalance = 0;
        CashAccount account = recentAccountList.get(position);
        /*
        for (CashTransaction transaction : account.getTransactionsList()){
            totalBalance += transaction.getBalance();
        }

         */
        return totalBalance;
    }
    public void addItems(List<CashAccount> accounts){
        recentAccountList.clear();
        recentAccountList.addAll(accounts);
        notifyDataSetChanged();
    }

    public class AccountReducedViewHolder extends BaseViewHolder {
        @BindView(R.id.recent_account_color_img)
        ImageView accountColorImg;
        @BindView(R.id.recent_account_name_text)
        TextView accountName;
        @BindView(R.id.recent_account_balance_text)
        TextView accountBalance;
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
            accountColorImg.setColorFilter(R.color.accent_amber);
            String balance = getAccountTotalBalance(position) + " " +
                    MyApp.AppPref().getString(PrefsConst.PREF_DEFAULT_CURRENCY,"$");

            accountBalance.setText(balance);
            accountColorImg.setBackgroundTintList(ColorStateList.valueOf(cashAccount.getColor()));
        }
    }
}
