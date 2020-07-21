package com.example.highcash.ui.transactions;


import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.highcash.MyApp;
import com.example.highcash.R;
import com.example.highcash.data.app_preference.PrefConst;
import com.example.highcash.data.db.model.CashTransaction;
import com.example.highcash.ui.account_editor.adapter.AccountCategory;
import com.example.highcash.helper.AppUtils;
import com.example.highcash.ui.base.BaseViewHolder;
import com.example.highcash.ui.views.FlipAnimator;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {
    private Context mContext;
    private List<CashTransaction> transactionsList;
    private TransactionsAdapterListener listener;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private SparseBooleanArray animatedItems = new SparseBooleanArray();
    private int currentSelectedIndex = -1;
    private boolean reverseAllAnimations = false;
    public boolean selectionMode =false;

    public TransactionsAdapter(Context mContext, List<CashTransaction> transactionsList) {
        this.mContext = mContext;
        this.transactionsList = transactionsList;

    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_transaction,parent,false);
        return new TransactionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.onBind(position);
    }
    @Override
    public int getItemCount() {
        return transactionsList.size();
    }


    public void addItems(List<CashTransaction> transactions){
        transactionsList = new ArrayList<>();
        transactionsList.addAll(transactions);
        notifyDataSetChanged();
    }
    void deleteItem(int position){
        transactionsList.remove(position);
        notifyItemRemoved(position);
    }
    public void setAdapterListener(TransactionsAdapterListener listener){
        this.listener = listener;
    }

    // Selections

    void clearSelections(){
        animatedItems.clear();
        selectedItems.clear();
    }


    void toggleSelection(int position){
        currentSelectedIndex = position;
        if (!selectedItems.get(position)){
            selectedItems.put(position,true);
            animatedItems.put(position,true);
        }else{
            selectedItems.delete(position);
            animatedItems.delete(position);
        }
        notifyItemChanged(position);
    }

    int getSelectedItemsCount(){
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }


    // Animate Selection
    private void applyIconAnimation(TransactionViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.transactionCategoryImg.setVisibility(View.GONE);
            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.transactionCategoryImg, true);
                resetCurrentIndex();
            }

        } else {
            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.transactionCategoryImg);
            holder.transactionCategoryImg.setVisibility(View.VISIBLE);
            holder.transactionCategoryImg.setAlpha(1f);

            if ((reverseAllAnimations && animatedItems.get(position, false)) ||
                    currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.transactionCategoryImg, false);
                resetCurrentIndex();
            }
        }
    }

    private void resetCurrentIndex(){
        currentSelectedIndex = -1;
    }
    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animatedItems.clear();
    }


    class TransactionViewHolder extends BaseViewHolder {
        @BindView(R.id.transactions_card_view)
        CardView transactionCardView;
        @BindView(R.id.transaction_category_img)
        CircularImageView transactionCategoryImg;
        @BindView(R.id.icon_back)
        RelativeLayout iconBack;
        @BindView(R.id.card_transaction_title_text)
        TextView transactionTitle;
        @BindView(R.id.transaction_money_text)
        TextView transactionMoneyTextView;
        @BindView(R.id.transaction_last_updated_date_txt)
        TextView transactionLastUpdatedDate;
        @BindView(R.id.card_transaction_source_text)
        TextView transactionSourceText;
        TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onBind(int currentPosition) {
            super.onBind(currentPosition);
            CashTransaction transaction = transactionsList.get(currentPosition);
            transactionLastUpdatedDate.setText(String.format("%s %s",
                    mContext.getString(R.string.last_updated),
                    AppUtils.formatDate(new Date(transaction.getDate()), AppUtils.MAIN_DATE_FORMAT)));


            String balance = transaction.getBalance() + MyApp.AppPref().getString(
                    PrefConst.PREF_DEFAULT_CURRENCY,"$");;
            transactionMoneyTextView.setText(balance);
            transactionTitle.setText(String.format("%s",transaction.getTitle()));
            transactionCardView.setOnClickListener(v -> listener.onTransactionClick(currentPosition));
            transactionCardView.setOnLongClickListener(v -> listener.onTransactionLongClick(currentPosition));

            if (transactionMoneyTextView.getText().toString().substring(0,1).equals("-"))
                transactionMoneyTextView.setTextColor(AppUtils.getColor(mContext,R.color.piechart_red));
            else
                transactionMoneyTextView.setTextColor(AppUtils.getColor(mContext,R.color.accent_green));

            if (listener.getAccountCategory() != null)
                transactionCategoryImg.setImageDrawable(mContext.getDrawable(listener.getAccountCategory().getCategoryImage()));

            if (listener.getAccountSource() != null)
                transactionSourceText.setText(String.format("Source : %s", listener.getAccountSource()));
            applyIconAnimation(this,currentPosition);
        }


    }

    public interface TransactionsAdapterListener{
        void onTransactionClick(int position);
        boolean onTransactionLongClick(int position);
        String getAccountSource();
        AccountCategory getAccountCategory();
    }
}
