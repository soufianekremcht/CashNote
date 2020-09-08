package com.example.highcash.ui.transactions.search;


import com.example.highcash.data.db.model.CashTransaction;
import com.example.highcash.ui.base.BaseContract;
import com.example.highcash.ui.transactions.TransactionsAdapter;
import com.example.highcash.ui.transactions.show_transaction.TransactionBottomSheetDialog;

import java.util.List;

public interface SearchContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void onQuerySubmitted(String query);

    }

    interface View extends BaseContract.MvpView, TransactionsAdapter.TransactionsAdapterListener,
            TransactionBottomSheetDialog.ShowTransactionDialogListener {
        void notifyAdapter(List<CashTransaction> transactions);
    }
}
