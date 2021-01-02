package com.soufianekre.highcash.ui.transaction_filter;

import com.soufianekre.highcash.data.db.model.CashTransaction;
import com.soufianekre.highcash.ui.a_base.BaseContract;
import com.soufianekre.highcash.ui.transactions.TransactionsAdapter;

import java.util.List;

public interface TransactionFilterContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
         void getTransactions(String month, int year, boolean filtered);
    }

    interface View extends BaseContract.MvpView, TransactionsAdapter.TransactionsAdapterListener {
        void showTransactions(List<CashTransaction> transactions);
    }
}
