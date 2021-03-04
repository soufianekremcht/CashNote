package com.soufianekre.cashnotes.ui.transaction_filter;

import com.soufianekre.cashnotes.data.db.model.CashAccount;
import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.ui.base.BaseContract;
import com.soufianekre.cashnotes.ui.transactions.TransactionsAdapter;

import java.util.List;

public interface TransactionFilterContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
         void getTransactions(String month, int year, boolean filtered);
         void getAllAccounts();
    }

    interface View extends BaseContract.MvpView, TransactionsAdapter.TransactionsAdapterListener {
        void notifyAdapter(List<CashTransaction> transactions);
        void updateAccountList(List<CashAccount> accounts);
    }
}
