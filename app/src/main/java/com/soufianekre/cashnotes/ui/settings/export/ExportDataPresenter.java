package com.soufianekre.cashnotes.ui.settings.export;

import com.soufianekre.cashnotes.data.DataManager;
import com.soufianekre.cashnotes.helper.rx.SchedulerProvider;
import com.soufianekre.cashnotes.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ExportDataPresenter<V extends ExportDataContract.View> extends BasePresenter<V>
        implements ExportDataContract.Presenter<V> {

    @Inject
    public ExportDataPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void getTransactionData() {
        getCompositeDisposable().add(getDataManager()
                .getRoomDb().cashTransactionDao().getAllTransactions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    // get All The Transactions Within The Time Frame
                    getMvpView().exportToCSV(results);

                }, Timber::e));
    }
}
