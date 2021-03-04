package com.soufianekre.cashnotes.ui.transactions;

import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.ui.base.BaseContract;
import com.soufianekre.cashnotes.ui.transactions.show_transaction.ShowTransactionFragment;

import java.util.List;

public interface TransactionsContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void deleteTransaction(CashTransaction transaction,int position);
        void getTransactions(int accountId);


    }

    interface View extends BaseContract.MvpView, TransactionsAdapter.TransactionsAdapterListener,
            ShowTransactionFragment.ShowTransactionDialogListener {
        void notifyAdapter(List<CashTransaction> transactionList, int income, int expense);
    }
}
