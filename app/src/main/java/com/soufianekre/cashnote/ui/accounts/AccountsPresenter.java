package com.soufianekre.cashnote.ui.accounts;

import com.soufianekre.cashnote.data.DataManager;
import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.ui.app_base.BasePresenter;
import com.soufianekre.cashnote.helper.rx.SchedulerProvider;

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
    public void getAccounts(){
        getCompositeDisposable().add(getDataManager().getRoomDb().accountDao().getAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accounts -> {
                    getMvpView().setupAccountsAdapter(accounts);

                }, Timber::e));
    }
    @Override
    public void onDeleteOptionClick(List<CashAccount> accountList, int position){
        CashAccount accountToDelete = accountList.get(position);
        getCompositeDisposable().add(getDataManager().getRoomDb().accountDao()
                .deleteAccount(accountToDelete)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            //getMvpView().showMessage("Delete account Successes");
                        },
                        Timber::e));
    }
}
