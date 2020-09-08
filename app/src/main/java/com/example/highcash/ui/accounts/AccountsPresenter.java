package com.example.highcash.ui.accounts;

import android.util.Log;

import com.example.highcash.data.DataManager;
import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.ui.base.BasePresenter;
import com.example.highcash.helper.rx.SchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AccountsPresenter<V extends AccountsContract.View> extends BasePresenter<V>
    implements AccountsContract.Presenter<V> {

    @Inject
    public AccountsPresenter(DataManager dataManager,
                             SchedulerProvider schedulerProvider,
                             CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void getAccounts(){
        getCompositeDisposable().add(getDataManager().getRoomDb().accountDao().getAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accounts -> {

                    getMvpView().setupAccountsAdapter(accounts);


                },throwable -> {
                    Log.e("Get Accounts", throwable.getMessage(), throwable);
                }));
    }
    @Override
    public void onDeleteOptionClick(List<CashAccount> accountList, int position){
        CashAccount accountToDelete = accountList.get(position);
        getCompositeDisposable().add(getDataManager().getRoomDb().accountDao()
                .deleteAccount(accountToDelete)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() ->{
                            //getMvpView().showMessage("Delete account Successes");
                        },
                        throwable ->
                                Log.e("DeleteAccount", throwable.getMessage(), throwable)));
    }
}
