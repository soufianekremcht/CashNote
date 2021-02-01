package com.soufianekre.cashnote.ui.overview;

import com.soufianekre.cashnote.data.DataManager;
import com.soufianekre.cashnote.data.db.model.BalanceHistory;
import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.data.db.model.CashTransaction;
import com.soufianekre.cashnote.ui.app_base.BasePresenter;
import com.soufianekre.cashnote.helper.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Collections;
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
    public void getOverView(){
        getMvpView().showMessage("OverView show");
        getCompositeDisposable().add(getDataManager().getRoomDb().accountDao().getAccounts()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(cashAccounts -> {
                    // get All Recent Transactions
                    List<CashTransaction> allTransactions = new ArrayList<>();
                    List<CashTransaction> recentTransaction = new ArrayList<>();
                    for (CashAccount cashAccount : cashAccounts) {
                        allTransactions.addAll(cashAccount.getTransactionsList());
                    }
                    Collections.sort(allTransactions, (o1, o2) -> {
                        long c = o2.getLastUpdatedDate() - o1.getLastUpdatedDate();
                        if (c > 0) return 1;
                        else return -1;

                    });
                    if (allTransactions.size() < 5) {
                        recentTransaction.addAll(allTransactions);
                    } else {
                        for (int i = 0;i<4;i++){
                            recentTransaction.add(allTransactions.get(i));
                        }
                    }

                    getMvpView().updateRecentAccounts(cashAccounts);
                    getMvpView().updateRecentTransactions(recentTransaction);

                    getMvpView().setExpenseIncomeLineChart(allTransactions);
                    getMvpView().setSummaryPieChart(cashAccounts);

                }, throwable ->
                        handleError("Get OverView Presenter",throwable)
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
                        Timber.e("DB History size :" + balanceHistories.size());
                        List<BalanceHistory> results = new ArrayList<>();
                        int size = balanceHistories.size();

                        if (maxDays > size && size > 0) maxDays = size;
                        if (maxDays < 1) maxDays = 1;

                        Timber.e("start index :" + maxDays);
                        for (int i = 1; i <= maxDays; i++) {
                            Timber.e("looping index :" + (size - i));
                            BalanceHistory history = balanceHistories.get(size - i);
                            results.add(history);
                        }
                        Timber.e("Requested History Result size :" + results.size());
                        getMvpView().setBalanceChart(results, daysBackward);
                    }, throwable ->
                            Timber.e(throwable.getLocalizedMessage())));

    }


}
