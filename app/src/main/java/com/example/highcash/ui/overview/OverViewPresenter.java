package com.example.highcash.ui.overview;

import android.util.Log;

import com.example.highcash.data.DataManager;
import com.example.highcash.data.db.model.BalanceHistory;
import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.data.db.model.CashTransaction;
import com.example.highcash.ui.base.BasePresenter;
import com.example.highcash.helper.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


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

                    getMvpView().setExpenseChart(allTransactions);
                    getMvpView().setSummaryChart(cashAccounts);

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
                        Log.e("Get Balance history","DB History size :" +balanceHistories.size());
                        List<BalanceHistory> results = new ArrayList<>();
                        int size = balanceHistories.size();

                        if (maxDays > size && size>0) maxDays = size;
                        if (maxDays<1) maxDays = 1;

                        Log.e("Get Balance history","start index :" +maxDays);
                        for (int i = 1; i <= maxDays; i++) {
                            Log.e("Get Balance history","looping index :" +(size -i));
                            BalanceHistory history = balanceHistories.get(size - i);
                            results.add(history);
                        }
                        Log.e("Get Balance Success","Requested History Result size :" +results.size());
                        getMvpView().setBalanceChart(results, daysBackward);
                    }, throwable ->
                            Log.e("GetBalanceThrowable",throwable.getLocalizedMessage())));

    }


}
