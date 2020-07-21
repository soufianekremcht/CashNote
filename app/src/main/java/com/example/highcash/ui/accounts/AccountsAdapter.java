package com.example.highcash.ui.accounts;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.highcash.MyApp;
import com.example.highcash.R;
import com.example.highcash.data.app_preference.PrefConst;
import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.data.db.model.CashTransaction;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder> {

    private Context mContext;
    private List<CashAccount> accountList;
    private AccountsAdapterListener listener;

    public AccountsAdapter(Context mContext,List<CashAccount> accountList,AccountsAdapterListener listener) {
        this.mContext = mContext;
        this.accountList = accountList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AccountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_accounts, parent, false);
        return new AccountsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsViewHolder holder, int position) {
        CashAccount currentAccount = accountList.get(position);
        holder.accountTitle.setText(currentAccount.getName());
        holder.accountTotalTransactions.setText(
                String.format(Locale.US,"%d transactions",currentAccount.getTransactionsList().size()));
        String balance = getAccountTotalBalance(position) +  MyApp.AppPref().getString(PrefConst.PREF_DEFAULT_CURRENCY,"$");

        holder.accountBalance.setText(balance);
        holder.accountCategoryImg.setBorderColor(currentAccount.getColor());

        holder.accountCategoryImg.setImageDrawable(
                mContext.getDrawable(currentAccount.getCategory().getCategoryImage()));

        holder.accountCategoryImg.setCircleColor(Color.WHITE);
        setListeners(holder,position);
    }
    @Override
    public int getItemCount() {
        return accountList.size();
    }

    private void setListeners(AccountsViewHolder holder,int position){

        holder.accountCardView.setOnClickListener(v ->{
            listener.onAccountClick(position);
        });
        holder.accountOptionsBtn.setOnClickListener(v -> {
            listener.onAccountPopUpMenuShow(v,position);
        });

    }
    private int getAccountTotalBalance(int position){
        int totalBalance = 0;
        CashAccount account = accountList.get(position);
        for (CashTransaction transaction : account.getTransactionsList()){
            totalBalance += transaction.getBalance();
        }
        return totalBalance;
    }


    public void addItems(List<CashAccount> accounts){
        accountList.clear();
        accountList.addAll(accounts);
        notifyDataSetChanged();
    }
    public void deleteItem(int position){
        accountList.remove(position);
        notifyItemRemoved(position);
    }
    public List<CashAccount> getAccountList(){
        return accountList;
    }


    public void setAdapterListener(AccountsAdapterListener listener){
        this.listener = listener;
    }




    class AccountsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.account_card_view)
        CardView accountCardView;
        @BindView(R.id.card_account_title)
        TextView accountTitle;
        @BindView(R.id.card_account_total_transactions)
        TextView accountTotalTransactions;
        @BindView(R.id.card_account_balance)
        TextView accountBalance;
        @BindView(R.id.card_account_category_img)
        CircularImageView accountCategoryImg;
        @BindView(R.id.card_account_options_btn)
        ImageView accountOptionsBtn;

        AccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface AccountsAdapterListener{
         void onAccountPopUpMenuShow(View view,int position);
         void onAccountClick(int position);

    }
}
