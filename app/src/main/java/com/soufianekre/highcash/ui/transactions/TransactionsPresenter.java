package com.soufianekre.highcash.ui.transactions;

import android.util.Log;

import com.soufianekre.highcash.data.DataManager;
import com.soufianekre.highcash.data.db.model.CashAccount;
import com.soufianekre.highcash.data.db.model.CashTransaction;
import com.soufianekre.highcash.ui.app_base.BasePresenter;
import com.soufianekre.highcash.helper.rx.SchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TransactionsPresenter<V extends TransactionsContract.View> extends BasePresenter<V>
        implements TransactionsContract.Presenter<V> {

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
                    double income = 0;
                    double expense = 0;

                    transactionsList = cashAccount.getTransactionsList();
                    for (CashTransaction transaction: transactionsList){
                        if (transaction.isExpense()){
                            expense += transaction.getBalance();
                        }else{
                            income += transaction.getBalance();
                        }
                    }
                    getMvpView().notifyAdapter(transactionsList,income,expense);
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
