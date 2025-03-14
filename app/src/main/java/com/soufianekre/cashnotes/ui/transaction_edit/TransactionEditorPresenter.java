package com.soufianekre.cashnotes.ui.transaction_edit;


import com.soufianekre.cashnotes.data.DataManager;
import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.helper.rx.SchedulerProvider;
import com.soufianekre.cashnotes.ui.base.BasePresenter;

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
    public void insertNewTransaction(CashTransaction transaction) {

        getCompositeDisposable().add(
                getDataManager()
                        .getRoomDb().cashTransactionDao()
                        .insert(transaction)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(() -> {
                                    getMvpView().showMessage("Transaction has been added");
                                    getMvpView().saveAndFinish();
                                },
                                Timber::e));
    }


    @Override
    public void updateTransaction(CashTransaction transaction) {

        getCompositeDisposable().add(
                getDataManager()
                        .getRoomDb().cashTransactionDao()
                        .update(transaction)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(() -> {
                                    getMvpView().showMessage("Transaction has been added");
                                    getMvpView().saveAndFinish();
                                },
                                throwable -> Timber.e(throwable)));
    }

    @Override
    public void getAccountList() {

        getCompositeDisposable().add(
                getDataManager()
                        .getRoomDb().cashAccountDao()
                        .getAccounts()
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(cashAccounts -> {
                        }, Timber::e));
    }

    @Override
    public void getAccount(int account_id) {

        getCompositeDisposable().add(
                getDataManager()
                        .getRoomDb().cashAccountDao()
                        .getAccount(account_id)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(result -> {

                        }, throwable -> Timber.e(throwable.getLocalizedMessage())));
    }

}
