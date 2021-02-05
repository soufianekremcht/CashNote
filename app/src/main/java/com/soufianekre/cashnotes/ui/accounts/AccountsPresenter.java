package com.soufianekre.cashnotes.ui.accounts;

import com.soufianekre.cashnotes.data.DataManager;
import com.soufianekre.cashnotes.data.db.model.CashAccount;
import com.soufianekre.cashnotes.helper.rx.SchedulerProvider;
import com.soufianekre.cashnotes.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AccountsPresenter<V extends AccountsContract.View> extends BasePresenter<V>
        implements AccountsContract.Presenter<V> {

    @Inject
    public AccountsPresenter(DataManager dataManager,
                             SchedulerProvider schedulerProvider,
                             CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void getAccounts() {
        getCompositeDisposable().add(getDataManager().getRoomDb().cashAccountDao().getAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accounts -> {
                    getMvpView().notifyAccountAdapter(accounts);

                }, Timber::e));
    }

    @Override
    public void getTransactions(){
        getCompositeDisposable().add(getDataManager()
                .getRoomDb().cashTransactionDao().getAllTransactions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {

                    getMvpView().setInfo(results);
                }, throwable -> Timber.e("%s", throwable.getMessage())));

    }

    @Override
    public void onDeleteAccount(List<CashAccount> accountList, int position) {
        CashAccount accountToDelete = accountList.get(position);
        getCompositeDisposable().add(getDataManager().getRoomDb().cashAccountDao()
                .deleteAccount(accountToDelete)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    getMvpView().onAccountDeleted(position);
                }, Timber::e));
    }
}
