package com.soufianekre.cashnote.ui.accounts;

import com.soufianekre.cashnote.data.DataManager;
import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.data.db.model.CashTransaction;
import com.soufianekre.cashnote.helper.rx.SchedulerProvider;
import com.soufianekre.cashnote.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AccountsPresenter<V extends AccountsContract.View> extends BasePresenter<V>
        implements AccountsContract.Presenter<V> {

    @Inject
    public AccountsPresenter(DataManager dataManager,
                             SchedulerProvider schedulerProvider,
                             CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void getAccounts() {
        getCompositeDisposable().add(getDataManager().getRoomDb().cashAccountDao().getAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accounts -> {
                    getMvpView().notifyAccountAdapter(accounts);

                }, Timber::e));
    }


    public void getAccountTransactions(int accountId){
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
                    //getMvpView().notifyAdapter(cashTransactionList, income, expense);
                }, throwable -> Timber.e("%s", throwable.getMessage())));

    }

    @Override
    public void onDeleteAccount(List<CashAccount> accountList, int position) {
        CashAccount accountToDelete = accountList.get(position);
        getCompositeDisposable().add(getDataManager().getRoomDb().cashAccountDao()
                .deleteAccount(accountToDelete)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    getMvpView().onAccountDeleted(position);
                }, Timber::e));
    }
}
