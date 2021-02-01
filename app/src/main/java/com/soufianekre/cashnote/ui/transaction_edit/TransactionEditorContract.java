package com.soufianekre.cashnote.ui.transaction_edit;

import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.data.db.model.CashTransaction;
import com.soufianekre.cashnote.ui.app_base.BaseContract;
import com.maltaisn.calcdialog.CalcDialog;

public interface TransactionEditorContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void saveTransaction(CashAccount accountParent);

    }

    interface View extends BaseContract.MvpView,
            TransactionCategoryAdapter.CategoryAdapterListener ,
            CalcDialog.CalcDialogCallback{
        void setOldTransactionInfo(CashTransaction transaction);
    }
}
