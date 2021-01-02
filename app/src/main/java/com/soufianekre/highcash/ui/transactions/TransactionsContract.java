package com.soufianekre.highcash.ui.transactions;

import com.soufianekre.highcash.data.db.model.CashAccount;
import com.soufianekre.highcash.data.db.model.CashTransaction;
import com.soufianekre.highcash.ui.a_base.BaseContract;
import com.soufianekre.highcash.ui.transactions.show_transaction.ShowTransactionFragment;

import java.util.List;

public interface TransactionsContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void onDeleteOptionClick(CashAccount accountParent, List<CashTransaction> transactions, int position);
        void getTransactions(int accountId);


    }

    interface View extends BaseContract.MvpView, TransactionsAdapter.TransactionsAdapterListener,
            ShowTransactionFragment.ShowTransactionDialogListener {
        void notifyAdapter(List<CashTransaction> transactionList, double income, double expense);
        void onTransactionEdit(int position);
        void onTransactionDelete(int position);
        void setAccountParent(CashAccount account);

    }
}
