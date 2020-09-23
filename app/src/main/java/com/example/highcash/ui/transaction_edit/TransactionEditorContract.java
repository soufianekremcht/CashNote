package com.example.highcash.ui.transaction_edit;

import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.data.db.model.CashTransaction;
import com.example.highcash.ui.base.BaseContract;

public interface TransactionEditorContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void saveTransaction(CashAccount accountParent);

    }

    interface View extends BaseContract.MvpView, TransactionCategoryAdapter.CategoryAdapterListener {
        void setOldTransactionInfo(CashTransaction transaction);
    }
}
