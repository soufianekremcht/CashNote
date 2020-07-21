package com.example.highcash.ui.transactions;

import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.data.db.model.CashTransaction;
import com.example.highcash.ui.base.BaseContract;
import com.example.highcash.ui.transactions.show_transaction.ShowTransactionDialogFragment;

import java.util.List;

public interface TransactionContract {
    public static interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void onDeleteOptionClick(CashAccount accountParent, List<CashTransaction> transactions, int position);
        void getTransactions(int accountId);


    }

    public static interface View extends BaseContract.MvpView, TransactionsAdapter.TransactionsAdapterListener,
            ShowTransactionDialogFragment.ShowTransactionDialogListener {
        void showTransactionEditorActivity();
        void setTransactions(List<CashTransaction> transactionList);
        void onTransactionEdit(int position);
        void onTransactionDelete(int position);
        void setAccountParent(CashAccount account);

    }
}
