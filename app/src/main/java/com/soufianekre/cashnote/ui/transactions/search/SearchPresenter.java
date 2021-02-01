package com.soufianekre.cashnote.ui.transactions.search;


import android.util.Log;

import com.soufianekre.cashnote.data.DataManager;
import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.data.db.model.CashTransaction;
import com.soufianekre.cashnote.helper.rx.SchedulerProvider;
import com.soufianekre.cashnote.ui.app_base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenter<V extends SearchContract.View> extends BasePresenter<V>
        implements SearchContract.Presenter<V> {

    private static final String TAG = "SearchPresenter";


    @Inject
    public SearchPresenter(DataManager dataManager,
                           SchedulerProvider schedulerProvider,
                           CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }



    @Override
    public void onQuerySubmitted(String query) {
        getCompositeDisposable().add(getDataManager()
                .getRoomDb().accountDao().getAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cashAccounts -> {
                    List<CashTransaction> result = new ArrayList<>();
                    for (CashAccount account : cashAccounts){
                        for (CashTransaction transaction : account.getTransactionsList()){
                            if (transaction.getName().contains(query))
                                result.add(transaction);
                        }
                    }
                    getMvpView().notifyAdapter(result);
                }, throwable -> Log.e("Search Presenter",""+throwable.getMessage())));
    }
}
