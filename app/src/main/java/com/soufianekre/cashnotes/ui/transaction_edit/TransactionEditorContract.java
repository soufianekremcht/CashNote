package com.soufianekre.cashnotes.ui.transaction_edit;

import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.ui.base.BaseContract;
import com.maltaisn.calcdialog.CalcDialog;

public interface TransactionEditorContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void insertNewTransaction(CashTransaction cashTransaction);
        void updateTransaction(CashTransaction cashTransaction);
        void getAccountList();
        void getAccount(int account_id);

    }

    interface View extends BaseContract.MvpView,
            TransactionCategoryAdapter.CategoryAdapterListener ,
            CalcDialog.CalcDialogCallback{
        void setOldTransactionInfo(CashTransaction transaction);

        void saveAndFinish();
    }
}
