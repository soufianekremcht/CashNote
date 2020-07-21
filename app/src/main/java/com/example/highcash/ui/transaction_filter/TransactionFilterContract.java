package com.example.highcash.ui.transaction_filter;

import com.example.highcash.data.db.model.CashTransaction;
import com.example.highcash.ui.base.BaseContract;
import com.example.highcash.ui.transactions.TransactionsAdapter;

import java.util.List;

public interface TransactionFilterContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
         void getTransactions(String month, int year, boolean filtered);
    }

    interface View extends BaseContract.MvpView, TransactionsAdapter.TransactionsAdapterListener {
        void showTransactions(List<CashTransaction> transactions);
    }
}
