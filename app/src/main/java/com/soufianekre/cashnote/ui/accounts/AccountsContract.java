package com.soufianekre.cashnote.ui.accounts;

import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.ui.base.BaseContract;

import java.util.List;

public interface AccountsContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void getAccounts();
        void onDeleteAccount(List<CashAccount> accounts, int position);
    }

    interface View extends BaseContract.MvpView, AccountsAdapter.AccountsAdapterListener {
        void notifyAccountAdapter(List<CashAccount> accounts);

        void onAccountDeleted(int position);
    }
}
