package com.soufianekre.cashnotes.ui.main;

import com.soufianekre.cashnotes.data.DataManager;
import com.soufianekre.cashnotes.data.db.model.BalanceHistory;
import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.helper.rx.SchedulerProvider;
import com.soufianekre.cashnotes.ui.base.BasePresenter;

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
                getDataManager().getRoomDb().cashTransactionDao()
                        .getAllTransactions()
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(results -> {
                            int balanceValue = 0;
                            for (CashTransaction transaction : results) {
                                balanceValue += transaction.getBalance();

                            }
                            getMvpView().setBalance(balanceValue);
                        })
        );
    }

    @Override
    public void saveBalanceToDb(int days, int year, int balanceValue) {
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
