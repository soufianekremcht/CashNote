package com.soufianekre.highcash.ui.transaction_edit;


import android.util.Log;

import com.soufianekre.highcash.data.DataManager;
import com.soufianekre.highcash.data.db.model.CashAccount;
import com.soufianekre.highcash.ui.app_base.BasePresenter;
import com.soufianekre.highcash.helper.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

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
                        .subscribe(() -> getMvpView().showMessage("Transaction has been added"),
                                throwable -> Timber.e(throwable, "Unable to To Update Account")));
    }

}
