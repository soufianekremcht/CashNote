package com.soufianekre.highcash.ui.account_edit;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.soufianekre.highcash.data.db.model.CashAccount;
import com.soufianekre.highcash.ui.a_base.BaseContract;

public interface AccountEditorContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void saveNewAccount(CashAccount account);
        void saveEditedAccount(CashAccount accountToEdit);
        void getAccountToEdit(int accountId);

    }

    interface View extends BaseContract.MvpView, ColorChooserDialog.ColorCallback{
        void setEditedAccountInfo(CashAccount cashAccount);

    }
}
