package com.soufianekre.cashnote.ui.transaction_filter;


import com.soufianekre.cashnote.data.DataManager;
import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.data.db.model.CashTransaction;
import com.soufianekre.cashnote.helper.rx.SchedulerProvider;
import com.soufianekre.cashnote.ui.app_base.BasePresenter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class TransactionFilterPresenter<V extends TransactionFilterContract.View> extends BasePresenter<V> implements TransactionFilterContract.Presenter<V> {

    private static final String TAG = "TransactionFilterPresenter";



    @Inject
    public TransactionFilterPresenter(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void getTransactions(String month, int year, boolean filtered) {
        getCompositeDisposable().add(getDataManager().getRoomDb()
                .accountDao()
                .getAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accounts -> {

                    List<CashTransaction> results = new ArrayList<>();
                    for(CashAccount cashAccount : accounts){
                        for (CashTransaction transaction : cashAccount.getTransactionsList()){
                            if (filtered){
                                if (isChosen(transaction,month,year))
                                    results.add(transaction);
                            }else{
                                results.add(transaction);
                            }

                        }
                    }
                    getMvpView().notifyAdapter(results);

                },throwable -> getMvpView().showMessage(throwable.getLocalizedMessage()))

        );

    }

    private boolean isChosen(CashTransaction transaction,String chosenMonth,int chosenYear){
        Date date = new Date(transaction.getLastUpdatedDate());
        DateTime dateTime = new DateTime(date);
        String month = dateTime.toString("MMM");
        int year = dateTime.year().get();
        int day = dateTime.dayOfYear().get();

        return chosenYear == year && month.equals(chosenMonth);

    }

}