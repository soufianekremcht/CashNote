package com.soufianekre.cashnote.ui.transactions.search;


import com.soufianekre.cashnote.data.DataManager;
import com.soufianekre.cashnote.data.db.model.CashTransaction;
import com.soufianekre.cashnote.helper.rx.SchedulerProvider;
import com.soufianekre.cashnote.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SearchPresenter<V extends SearchContract.View> extends BasePresenter<V>
        implements SearchContract.Presenter<V> {


    @Inject
    public SearchPresenter(DataManager dataManager,
                           SchedulerProvider schedulerProvider,
                           CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void onQuerySubmitted(String query) {
        getCompositeDisposable().add(getDataManager()
                .getRoomDb().cashTransactionDao().getAllTransactions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transactions -> {
                    List<CashTransaction> result = new ArrayList<>();
                    for (CashTransaction transaction : transactions) {
                        if (transaction.getName().contains(query))
                            result.add(transaction);
                    }
                    getMvpView().notifyAdapter(result);
                }, throwable -> Timber.e("%s", throwable.getMessage())));
    }
}
