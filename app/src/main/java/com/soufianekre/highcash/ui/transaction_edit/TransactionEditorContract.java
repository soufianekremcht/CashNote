package com.soufianekre.highcash.ui.transaction_edit;

import com.soufianekre.highcash.data.db.model.CashAccount;
import com.soufianekre.highcash.data.db.model.CashTransaction;
import com.soufianekre.highcash.ui.app_base.BaseContract;
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
