package com.soufianekre.cashnote.ui.main;

import com.soufianekre.cashnote.data.DataManager;
import com.soufianekre.cashnote.data.db.model.BalanceHistory;
import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.data.db.model.CashTransaction;
import com.soufianekre.cashnote.ui.app_base.BasePresenter;
import com.soufianekre.cashnote.helper.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class MainPresenter<V extends MainContract.View> extends BasePresenter<V>
    implements MainContract.Presenter<V> {
    private static final String TAG = "MainPresenter";

    @Inject
    public MainPresenter(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void onDrawerOptionOverViewClick() {
        getMvpView().showOverViewFragment();
    }


    @Override
    public void onDrawerOptionAccountsClick() {
        getMvpView().showAccountFragment();
    }


    @Override
    public void setBalanceForCurrentDay() {
        getCompositeDisposable().add(
                getDataManager().getRoomDb().accountDao().getAccounts()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(accounts -> {
                    int balanceValue = 0;
                    for (CashAccount account :accounts){
                        for (CashTransaction transaction : account.getTransactionsList()){
                            balanceValue+= transaction.getBalance();
                        }
                    }
                    getMvpView().setBalance(balanceValue);
                })
        );
    }

    @Override
    public void saveBalanceToDb(int days,int year,int balanceValue) {
        BalanceHistory balanceHistory = new BalanceHistory();
        balanceHistory.setDays(days);
        balanceHistory.setYear(year);
        balanceHistory.setValue(balanceValue);
        getCompositeDisposable().add(
                getDataManager().getRoomDb().balanceHistoryDao().addBalance(balanceHistory)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(() -> Timber.i("Balance for today is saved"))
        );
    }

}
