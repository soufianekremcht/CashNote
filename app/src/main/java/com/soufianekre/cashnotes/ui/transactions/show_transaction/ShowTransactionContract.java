package com.soufianekre.cashnotes.ui.transactions.show_transaction;

import com.soufianekre.cashnotes.data.db.model.CashAccount;
import com.soufianekre.cashnotes.ui.base.BaseContract;

public interface ShowTransactionContract {

    interface Presenter<V extends ShowTransactionContract.View> extends BaseContract.MvpPresenter<V> {
        void getTransactionAccount(int accountId);
    }

    interface View extends BaseContract.MvpView {
        void setTransactionAccount(CashAccount cashAccount);
    }

}
