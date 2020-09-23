package com.example.highcash.ui.transactions.search;


import com.example.highcash.data.db.model.CashTransaction;
import com.example.highcash.ui.base.BaseContract;
import com.example.highcash.ui.transactions.TransactionsAdapter;
import com.example.highcash.ui.transactions.show_transaction.ShowTransactionFragment;

import java.util.List;

public interface SearchContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void onQuerySubmitted(String query);

    }

    interface View extends BaseContract.MvpView, TransactionsAdapter.TransactionsAdapterListener,
            ShowTransactionFragment.ShowTransactionDialogListener {
        void notifyAdapter(List<CashTransaction> transactions);
    }
}
