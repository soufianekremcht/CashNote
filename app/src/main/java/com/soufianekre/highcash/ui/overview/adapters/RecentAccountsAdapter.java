package com.soufianekre.highcash.ui.overview.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soufianekre.highcash.MyApp;
import com.soufianekre.highcash.R;
import com.soufianekre.highcash.data.app_preference.PrefConst;
import com.soufianekre.highcash.data.db.model.CashAccount;
import com.soufianekre.highcash.data.db.model.CashTransaction;
import com.soufianekre.highcash.ui.app_base.BaseViewHolder;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentAccountsAdapter extends RecyclerView.Adapter<RecentAccountsAdapter.AccountReducedViewHolder> {

    private final Context mContext;
    private final List<CashAccount> recentAccountList;

    public RecentAccountsAdapter(Context mContext, List<CashAccount> accountList) {
        this.mContext = mContext;
        this.recentAccountList = accountList;
    }

    @NonNull
    @Override
    public AccountReducedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_account_recent,parent,false);
        return new AccountReducedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountReducedViewHolder holder, int position) {
        CashAccount cashAccount = recentAccountList.get(position);
        holder.accountName.setText(cashAccount.getName());
        holder.accountColorImg.setColorFilter(R.color.accent_amber);
        String balance = getAccountTotalBalance(position) + " " +
                MyApp.AppPref().getString(PrefConst.PREF_DEFAULT_CURRENCY,"$");

        holder.accountBalance.setText(balance);

        holder.accountTransactionsCounter.setText(String.format(Locale.US,
                "%d transactions" ,cashAccount.getTransactionsList().size()));
        //holder.category_indicator.setBackgroundColor(cashAccount.getColor());
    }

    @Override
    public int getItemCount() {
        return recentAccountList.size();
    }

    private int getAccountTotalBalance(int position){
        int totalBalance = 0;
        CashAccount account = recentAccountList.get(position);
        for (CashTransaction transaction : account.getTransactionsList()){
            totalBalance += transaction.getBalance();
        }
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
        public void onBind(int currentPosition) {
            super.onBind(currentPosition);
            CashAccount cashAccount = recentAccountList.get(currentPosition);
            accountName.setText(cashAccount.getName());
            accountColorImg.setColorFilter(cashAccount.getColor());


            String balance = getAccountTotalBalance(currentPosition) + " " +
                    MyApp.AppPref().getString(PrefConst.PREF_DEFAULT_CURRENCY,"$");

            accountBalance.setText(balance);

            accountTransactionsCounter.setText(String.format(Locale.US,
                    "%d transactions" ,cashAccount.getTransactionsList().size()));
            //holder.category_indicator.setBackgroundColor(cashAccount.getColor());
        }
    }
}
