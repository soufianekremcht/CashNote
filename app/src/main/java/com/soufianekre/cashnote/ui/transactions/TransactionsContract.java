package com.soufianekre.cashnote.ui.transactions;

import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.data.db.model.CashTransaction;
import com.soufianekre.cashnote.ui.base.BaseContract;
import com.soufianekre.cashnote.ui.transactions.show_transaction.ShowTransactionFragment;

import java.util.List;

public interface TransactionsContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void deleteTransaction(CashTransaction transaction);
        void getTransactions(int accountId);


    }

    interface View extends BaseContract.MvpView, TransactionsAdapter.TransactionsAdapterListener,
            ShowTransactionFragment.ShowTransactionDialogListener {
        void notifyAdapter(List<CashTransaction> transactionList, int income, int expense);
    }
}
