package com.soufianekre.cashnotes.ui.account_edit;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.soufianekre.cashnotes.data.db.model.CashAccount;
import com.soufianekre.cashnotes.ui.base.BaseContract;

public interface AccountEditorContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void saveNewAccount(CashAccount account);
        void saveEditedAccount(CashAccount accountToEdit);
        void getAccountToEdit(int accountId);

    }

    interface View extends BaseContract.MvpView, ColorChooserDialog.ColorCallback{
        void setCurrentAccountInfo(CashAccount cashAccount);

        void saveAndExit();
    }
}
