package com.soufianekre.cashnote.ui.transactions;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.soufianekre.cashnote.MyApp;
import com.soufianekre.cashnote.R;
import com.soufianekre.cashnote.data.app_preference.PrefConst;
import com.soufianekre.cashnote.data.db.model.CashTransaction;
import com.soufianekre.cashnote.helper.AppUtils;
import com.soufianekre.cashnote.ui.base.BaseViewHolder;
import com.soufianekre.cashnote.ui.views.FlipAnimator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {
    private final Context mContext;
    private List<CashTransaction> transactionsList;
    private TransactionsAdapterListener listener;
    private final SparseBooleanArray selectedItems = new SparseBooleanArray();
    private final SparseBooleanArray animatedItems = new SparseBooleanArray();
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_transaction,parent,false);
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

    public void setAdapterListener(TransactionsAdapterListener listener){
        this.listener = listener;
    }


    public void addItems(List<CashTransaction> transactions){
        transactionsList = new ArrayList<>();
        transactionsList.addAll(transactions);
        notifyDataSetChanged();
    }
    void deleteItem(int position){
        listener.onTransactionDelete(transactionsList.get(position),position);
        transactionsList.remove(position);
        notifyItemRemoved(position);
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

    @SuppressLint("NonConstantResourceId")
    public class TransactionViewHolder extends BaseViewHolder {
        @BindView(R.id.transactions_card_view)
        CardView transactionCardView;
        @BindView(R.id.card_category_img)
        ImageView transactionCategoryImg;
        @BindView(R.id.icon_back)
        FrameLayout iconBack;
        @BindView(R.id.transaction_name_text)
        TextView transactionName;
        @BindView(R.id.transaction_money_text)
        TextView transactionMoneyTextView;
        @BindView(R.id.transaction_last_up_date_txt)
        TextView transactionLastUpdatedDate;


        TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onBind(int currentPosition) {
            super.onBind(currentPosition);
            CashTransaction transaction = transactionsList.get(currentPosition);
            transactionName.setText(String.format("%s", transaction.getName()));

            transactionLastUpdatedDate.setText(String.format("%s %s",
                    mContext.getString(R.string.last_updated),
                    AppUtils.formatDate(new Date(transaction.getLastUpdatedDate()), AppUtils.MAIN_DATE_FORMAT)));


            String balance = transaction.getBalance() + " " + MyApp.AppPref().getString(
                    PrefConst.PREF_DEFAULT_CURRENCY, "$");
            transactionMoneyTextView.setText(balance);
            if (transaction.getCategory() != null) {
                Glide.with(itemView)
                        .asDrawable()
                        .load(transaction.getCategory().getImage())
                        .into(transactionCategoryImg);
                transactionCategoryImg.setBackgroundTintList(ColorStateList.valueOf(transaction.getCategory().getColor()));
            }


            if (transactionMoneyTextView.getText().toString().startsWith("-"))
                transactionMoneyTextView.setTextColor(AppUtils.getColor(mContext, R.color.piechart_red));
            else
                transactionMoneyTextView.setTextColor(AppUtils.getColor(mContext, R.color.accent_green));

            // Listener
            transactionCardView.setOnClickListener(v -> listener.onTransactionClick(transaction,currentPosition));
            transactionCardView.setOnLongClickListener(v -> listener.onTransactionLongClick(currentPosition));

            applyIconAnimation(this, currentPosition);
        }
    }

    public interface TransactionsAdapterListener{
        void onTransactionClick(CashTransaction transaction,int position);
        boolean onTransactionLongClick(int position);
        void onTransactionDelete(CashTransaction transaction,int position);
    }
}
