package com.soufianekre.cashnotes.ui.transactions.search;


import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.ui.base.BaseContract;
import com.soufianekre.cashnotes.ui.transactions.TransactionsAdapter;
import com.soufianekre.cashnotes.ui.transactions.show_transaction.ShowTransactionFragment;

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
