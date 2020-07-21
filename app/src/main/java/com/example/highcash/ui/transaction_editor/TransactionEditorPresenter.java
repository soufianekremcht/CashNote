package com.example.highcash.ui.transaction_editor;


import android.util.Log;

import com.example.highcash.data.DataManager;
import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.ui.base.BasePresenter;
import com.example.highcash.helper.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class TransactionEditorPresenter<V extends TransactionEditorContract.View> extends BasePresenter<V>
        implements TransactionEditorContract.Presenter<V> {

    @Inject
    public TransactionEditorPresenter(DataManager dataManager,
                                   SchedulerProvider schedulerProvider,
                                   CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void saveTransaction(CashAccount accountParent) {

        getCompositeDisposable().add(
                getDataManager()
                        .getRoomDb().accountDao()
                        .updateAccount(accountParent)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(() ->getMvpView().showMessage("Transaction has been added"),
                                throwable -> Log.e("update account", "Unable to To Update Account",
                                        throwable)));
    }

}
