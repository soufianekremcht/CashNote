package com.soufianekre.highcash.ui.transactions.search;


import com.soufianekre.highcash.data.db.model.CashTransaction;
import com.soufianekre.highcash.ui.a_base.BaseContract;
import com.soufianekre.highcash.ui.transactions.TransactionsAdapter;
import com.soufianekre.highcash.ui.transactions.show_transaction.ShowTransactionFragment;

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
