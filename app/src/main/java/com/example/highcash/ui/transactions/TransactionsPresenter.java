package com.example.highcash.ui.transactions;

import android.util.Log;

import com.example.highcash.data.DataManager;
import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.data.db.model.CashTransaction;
import com.example.highcash.ui.base.BasePresenter;
import com.example.highcash.helper.rx.SchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TransactionsPresenter<V extends TransactionContract.View> extends BasePresenter<V>
        implements TransactionContract.Presenter<V> {

    private List<CashTransaction> transactionsList;
    @Inject
    public TransactionsPresenter(DataManager dataManager,
                                 SchedulerProvider schedulerProvider,
                                 CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }
    @Override
    public void getTransactions(int accountId){
        getCompositeDisposable().add(getDataManager()
                .getRoomDb().accountDao().getAccount(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cashAccount -> {
                    transactionsList = cashAccount.getTransactionsList();
                    getMvpView().setTransactions(transactionsList);
                    getMvpView().setAccountParent(cashAccount);
                }, throwable -> Log.e("Transaction Presenter",""+throwable.getMessage())));

    }

    @Override
    public void onDeleteOptionClick(CashAccount accountParent,List<CashTransaction> transactions, int position) {
        getCompositeDisposable().add(getDataManager()
                .getRoomDb()
                .accountDao()
                .updateAccount(accountParent)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe());

    }
}
