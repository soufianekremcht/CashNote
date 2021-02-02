package com.soufianekre.cashnote.ui.transactions.show_transaction;

import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.ui.base.BaseContract;

public interface ShowTransactionContract {

    interface Presenter<V extends ShowTransactionContract.View> extends BaseContract.MvpPresenter<V> {
        void getTransactionAccount(int accountId);
    }

    interface View extends BaseContract.MvpView {
        void setTransactionAccount(CashAccount cashAccount);
    }

}
