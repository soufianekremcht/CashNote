package com.soufianekre.cashnotes.ui.transactions.show_transaction;

import com.soufianekre.cashnotes.data.DataManager;
import com.soufianekre.cashnotes.helper.rx.SchedulerProvider;
import com.soufianekre.cashnotes.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class ShowTransactionPresenter<V extends ShowTransactionContract.View> extends BasePresenter<V>
        implements ShowTransactionContract.Presenter<V> {


    @Inject
    public ShowTransactionPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void getTransactionAccount(int account_id) {

        getCompositeDisposable().add(
                getDataManager()
                        .getRoomDb().cashAccountDao()
                        .getAccount(account_id)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(result -> {
                            getMvpView().setTransactionAccount(result);
                        }, throwable -> Timber.e(throwable.getLocalizedMessage())));
    }
}
