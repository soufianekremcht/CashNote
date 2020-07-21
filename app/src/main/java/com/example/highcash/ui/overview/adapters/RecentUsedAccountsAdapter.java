package com.example.highcash.ui.overview.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.highcash.MyApp;
import com.example.highcash.R;
import com.example.highcash.data.app_preference.PrefConst;
import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.data.db.model.CashTransaction;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentUsedAccountsAdapter extends RecyclerView.Adapter<RecentUsedAccountsAdapter.AccountReducedViewHolder> {

    private Context mContext;
    private List<CashAccount> recentAccountList;

    public RecentUsedAccountsAdapter(Context mContext, List<CashAccount> accountList) {
        this.mContext = mContext;
        this.recentAccountList = accountList;
    }

    @NonNull
    @Override
    public AccountReducedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_account_reduced,parent,false);
        return new AccountReducedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountReducedViewHolder holder, int position) {
        CashAccount cashAccount = recentAccountList.get(position);
        holder.accountName.setText(cashAccount.getName());
        holder.account_category_img.setImageResource(cashAccount.getCategory().getCategoryImage());
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

    class AccountReducedViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.account_category_img_reduced)
        ImageView account_category_img;
        @BindView(R.id.account_name_text)
        TextView accountName;
        @BindView(R.id.account_balance_text)
        TextView accountBalance;
        @BindView(R.id.account_transaction_count)
        TextView accountTransactionsCounter;
        /*@BindView(R.id.category_indicator)
        View category_indicator;*/
        AccountReducedViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
