package com.example.highcash.ui.account_edit;

import android.util.Log;

import com.example.highcash.data.DataManager;
import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.helper.rx.SchedulerProvider;
import com.example.highcash.ui.a_base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AccountEditorPresenter<V extends AccountEditorContract.View> extends BasePresenter<V>
    implements AccountEditorContract.Presenter<V> {

    @Inject
    public AccountEditorPresenter(DataManager dataManager,
                                  SchedulerProvider schedulerProvider,
                                  CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void saveNewAccount(CashAccount newAccount) {
        getCompositeDisposable().add(getDataManager().getRoomDb().accountDao()
                .addAccount(newAccount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> getMvpView().showMessage("Account has been added"),
                        throwable -> Log.e("update account", "Unable to To Add Account",
                                throwable)));
    }

    @Override
    public void saveEditedAccount(CashAccount accountToEdit) {
        getCompositeDisposable().add(getDataManager().getRoomDb().accountDao()
                .updateAccount(accountToEdit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> getMvpView().showMessage("Account has been Updated"),
                        throwable -> Log.e("update account", "Unable to To Update Account",
                                throwable)));
    }

    @Override
    public void getAccountToEdit(int accountToEditId) {
        getCompositeDisposable().add(getDataManager().getRoomDb().accountDao()
                .getAccount(accountToEditId)
                .subscribeOn(Schedulers.io())
                .subscribe(cashAccount -> {
                    getMvpView().setEditedAccountInfo(cashAccount);
                },Throwable::printStackTrace));
    }
}
