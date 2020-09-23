package com.example.highcash.ui.account_edit;

import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.ui.base.BaseContract;

public interface AccountEditorContract {
    public static interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void saveNewAccount(CashAccount account);
        void saveEditedAccount(CashAccount accountToEdit);
        void getAccountToEdit(int accountId);

    }

    public static interface View extends BaseContract.MvpView{
        public void setEditedAccountInfo(CashAccount cashAccount);

    }
}
