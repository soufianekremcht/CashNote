package com.soufianekre.cashnotes.ui.accounts;

import com.soufianekre.cashnotes.data.db.model.CashAccount;
import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.ui.base.BaseContract;

import java.util.List;

public interface AccountsContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void getAccounts();
        void onDeleteAccount(List<CashAccount> accounts, int position);
    }

    interface View extends BaseContract.MvpView, AccountsAdapter.AccountsAdapterListener {
        void notifyAccountAdapter(List<CashAccount> accounts,List<CashTransaction> transactions);

        void onAccountDeleted(int position);
    }
}
