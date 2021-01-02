package com.soufianekre.highcash.ui.accounts;

import com.soufianekre.highcash.data.db.model.CashAccount;
import com.soufianekre.highcash.ui.a_base.BaseContract;

import java.util.List;

public interface AccountsContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void getAccounts();
        void onDeleteOptionClick(List<CashAccount> accounts, int position);
    }

    interface View extends BaseContract.MvpView, AccountsAdapter.AccountsAdapterListener {
        void setupAccountsAdapter(List<CashAccount> accounts);

    }
}
