package com.example.highcash.ui.accounts;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.highcash.R;
import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.helper.DialogsUtil;
import com.mikhaellopez.circularimageview.CircularImageView;

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
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_card_account, parent, false);
        return new AccountsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsViewHolder holder, int position) {
        CashAccount currentAccount = accountList.get(position);
        holder.accountTitle.setText(currentAccount.getName());
        holder.accountTotalTransactions.setText(
                String.format(Locale.US,"%d transactions",currentAccount.getTransactionsList().size()));

        holder.accountColorImg.setCircleColor(R.color.primary_amber);
        holder.accountColorImg.setColorFilter(R.color.primary_green);

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
            // pop up
            PopupMenu popupMenu = new PopupMenu(mContext,v);
            popupMenu.inflate(R.menu.popup_menu_edit);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.popup_menu_edit:
                        listener.onAccountEdit(position);
                        break;
                    case R.id.popup_menu_delete:
                        //show delete dialog;
                        DialogsUtil.showBasicDialogWithListener((Activity) mContext,
                                R.string.delete_message,
                                R.string.dialog_delete_account_title,
                                R.string.confirm,
                                R.string.cancel,
                                (dialog, which) -> deleteItem(position)).show();
                        break;
                }
                return true;
            });
            popupMenu.show();
        });
    }
    public void addItems(List<CashAccount> accounts){
        accountList.clear();
        accountList.addAll(accounts);
        notifyDataSetChanged();
    }
    public void deleteItem(int position){
        accountList.remove(position);
        listener.onAccountDelete(position);
        notifyItemRemoved(position);
    }

    public List<CashAccount> getAccountList(){
        return accountList;
    }

    public void setAdapterListener(AccountsAdapterListener listener){
        this.listener = listener;
    }




    static class AccountsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.account_card_view)
        CardView accountCardView;
        @BindView(R.id.card_account_title)
        TextView accountTitle;
        @BindView(R.id.card_account_total_transactions)
        TextView accountTotalTransactions;
        @BindView(R.id.card_account_color_img)
        CircularImageView accountColorImg;
        @BindView(R.id.card_account_options_btn)
        ImageView accountOptionsBtn;

        AccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface AccountsAdapterListener{
        void onAccountDelete(int position);
        void onAccountEdit(int position);
        void onAccountClick(int position);

    }
}
