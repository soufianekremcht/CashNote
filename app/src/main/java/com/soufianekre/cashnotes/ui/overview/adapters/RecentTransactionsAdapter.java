package com.soufianekre.cashnotes.ui.overview.adapters;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.soufianekre.cashnotes.MyApp;
import com.soufianekre.cashnotes.R;
import com.soufianekre.cashnotes.data.app_preference.PrefsConst;
import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.ui.base.BaseViewHolder;
import com.soufianekre.cashnotes.helper.AppUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.soufianekre.cashnotes.helper.AppUtils.MAIN_DATE_FORMAT;

public class RecentTransactionsAdapter extends RecyclerView.Adapter<RecentTransactionsAdapter.RecentTransactionsViewHolder> {
    private final Context context;
    private final List<CashTransaction> recentTransactions;


    public RecentTransactionsAdapter(Context context,List<CashTransaction> recentTransactions) {
        this.context = context;
        this.recentTransactions = recentTransactions;
    }

    @NonNull
    @Override
    public RecentTransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_recent_transaction,parent,false);
        return new RecentTransactionsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentTransactionsViewHolder holder, int position) {
       holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return recentTransactions.size();
    }

    public void addItems(List<CashTransaction> transactions){
        recentTransactions.clear();
        recentTransactions.addAll(transactions);
        notifyDataSetChanged();
    }


    public class RecentTransactionsViewHolder extends BaseViewHolder {
        @BindView(R.id.recent_transaction_description_text)
        TextView recentTransactionTitle;
        @BindView(R.id.transaction_creation_date_text)
        TextView recentTransactionCreationDate;
        @BindView(R.id.recent_transaction_balance_text)
        TextView recentTransactionBalance;
        @BindView(R.id.recent_transaction_category_img)
        ImageView recentTransactionCategoryImg;

        RecentTransactionsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onBind(int currentPosition) {
            super.onBind(currentPosition);

            CashTransaction transaction = recentTransactions.get(currentPosition);
            recentTransactionTitle.setText(String.format("%s",transaction.getName()));
            String balance = transaction.getBalance() + " " + MyApp.AppPref().getString(PrefsConst.PREF_DEFAULT_CURRENCY,"$");

            recentTransactionBalance.setText(balance);
            if (transaction.getCategory() != null) {
                Glide.with(context)
                        .asDrawable()
                        .load(transaction.getCategory().getImage())
                        .into(recentTransactionCategoryImg);
                recentTransactionCategoryImg.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                recentTransactionCategoryImg.setBackgroundTintList(ColorStateList.valueOf(transaction.getCategory().getColor()));

            }
            recentTransactionCreationDate.setText(AppUtils
                    .formatDate(new Date(transaction.getLastUpdatedDate()),MAIN_DATE_FORMAT));
        }
    }



}
