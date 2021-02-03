package com.soufianekre.cashnote.ui.account_edit;

import com.soufianekre.cashnote.data.DataManager;
import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.helper.rx.SchedulerProvider;
import com.soufianekre.cashnote.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

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
        getCompositeDisposable().add(getDataManager().getRoomDb().cashAccountDao()
                .addAccount(newAccount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            getMvpView().saveAndExit();
                            getMvpView().showMessage("Account has been added");
                        },
                        throwable -> Timber.e(throwable, "Unable to To Add Account")));
    }

    @Override
    public void saveEditedAccount(CashAccount accountToEdit) {
        getCompositeDisposable().add(getDataManager().getRoomDb().cashAccountDao()
                .updateAccount(accountToEdit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            getMvpView().saveAndExit();
                            getMvpView().showMessage("Account has been Updated");
                        },
                        throwable -> Timber.e(throwable, "Unable to To Update Account")));
    }

    @Override
    public void getAccountToEdit(int accountToEditId) {
        getCompositeDisposable().add(getDataManager().getRoomDb().cashAccountDao()
                .getAccount(accountToEditId)
                .subscribeOn(Schedulers.io())
                .subscribe(cashAccount -> {
                    getMvpView().setCurrentAccountInfo(cashAccount);
                }, Throwable::printStackTrace));
    }
}
