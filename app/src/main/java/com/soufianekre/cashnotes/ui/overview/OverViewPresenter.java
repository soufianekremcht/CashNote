package com.soufianekre.cashnotes.ui.overview;

import com.soufianekre.cashnotes.data.DataManager;
import com.soufianekre.cashnotes.data.db.model.BalanceHistory;
import com.soufianekre.cashnotes.helper.rx.SchedulerProvider;
import com.soufianekre.cashnotes.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;


public class OverViewPresenter<V extends OverViewContract.View> extends BasePresenter<V> implements
        OverViewContract.Presenter<V> {

    @Inject
    public OverViewPresenter(DataManager dataManager,
                             SchedulerProvider schedulerProvider,
                             CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void getOverView() {
        getAccounts();
        getTransactions();


    }


    private void getAccounts() {
        getCompositeDisposable().add(getDataManager().getRoomDb().cashAccountDao().getAccounts()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(cashAccounts -> {
                            // get All Recent Transactions
                            getMvpView().updateRecentAccounts(cashAccounts);
                        }, throwable ->
                                handleError("Get OverView Presenter", throwable)
                ));

    }

    private void getTransactions() {
        getCompositeDisposable().add(getDataManager().getRoomDb()
                .cashTransactionDao().getAllTransactions()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(transactions -> {
                            getMvpView().updateRecentTransactions(transactions);

                        }, throwable -> {
                            handleError("Get OverView Presenter", throwable);
                        }
                ));
    }


    @Override
    public void getBalanceHistory(int currentDay, int daysBackward) {
        // TODO : NEED MORE WORK ON THIS FEATURE

        getCompositeDisposable().add(
                getDataManager().getRoomDb().balanceHistoryDao().getBalanceHistoryList()
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(balanceHistories -> {
                            // get Balance History Form past on certain time frame
                            int maxDays = daysBackward;
                            Timber.e("DB History size :%s", balanceHistories.size());
                            List<BalanceHistory> results = new ArrayList<>();
                            int size = balanceHistories.size();

                            if (maxDays > size && size > 0) maxDays = size;
                            if (maxDays < 1) maxDays = 1;

                            Timber.e("start index :%s", maxDays);
                            for (int i = 1; i <= maxDays; i++) {
                                Timber.e("looping index :%s", (size - i));
                                BalanceHistory history = balanceHistories.get(size - i);
                                results.add(history);
                            }
                            Timber.e("Requested History Result size :%s", results.size());
                            getMvpView().setBalanceChart(results, daysBackward);
                        }, throwable ->
                                Timber.e(throwable.getLocalizedMessage())));

    }


}
