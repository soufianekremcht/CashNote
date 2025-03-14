package com.soufianekre.cashnotes.ui.transactions;

import com.soufianekre.cashnotes.data.DataManager;
import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.ui.base.BasePresenter;
import com.soufianekre.cashnotes.helper.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TransactionsPresenter<V extends TransactionsContract.View> extends BasePresenter<V>
        implements TransactionsContract.Presenter<V> {

    @Inject
    public TransactionsPresenter(DataManager dataManager,
                                 SchedulerProvider schedulerProvider,
                                 CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }
    @Override
    public void getTransactions(int accountId){
        getCompositeDisposable().add(getDataManager()
                .getRoomDb().cashTransactionDao().getTransactionsFromAccount(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cashTransactionList -> {
                    int income = 0;
                    int expense = 0;

                    for (CashTransaction transaction : cashTransactionList) {
                        if (transaction.isExpense()) {
                            expense += transaction.getBalance();
                        } else {
                            income += transaction.getBalance();
                        }
                    }
                    getMvpView().notifyAdapter(cashTransactionList, income, expense);
                }, throwable -> Timber.e("%s", throwable.getMessage())));

    }

    @Override
    public void deleteTransaction(CashTransaction transaction,int position) {
        getCompositeDisposable().add(getDataManager()
                .getRoomDb()
                .cashTransactionDao()
                .delete(transaction)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(() -> {
                    //getMvpView().onTransactionDeleted(position);
                },Timber::e));

    }
}
