package com.soufianekre.cashnote.ui.settings.export;

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

public class ExportDataPresenter<V extends ExportDataContract.View> extends BasePresenter<V>
        implements ExportDataContract.Presenter<V> {

    @Inject
    public ExportDataPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void getTransactionData() {
        getCompositeDisposable().add(getDataManager()
                .getRoomDb().accountDao().getAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cashAccounts -> {
                    // get All The Transactions Within The Time Frame
                    List<CashTransaction> transactions =new ArrayList<>();
                    for (CashAccount account : cashAccounts){
                        transactions.addAll(account.getTransactionsList());
                    }
                    getMvpView().exportToCSV(transactions);

                }, throwable -> Log.e("Export Presenter", throwable.getMessage())));
    }
}
