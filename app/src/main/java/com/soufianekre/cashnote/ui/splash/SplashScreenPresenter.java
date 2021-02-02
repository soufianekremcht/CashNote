package com.soufianekre.cashnote.ui.splash;

import com.soufianekre.cashnote.data.DataManager;
import com.soufianekre.cashnote.ui.base.BasePresenter;
import com.soufianekre.cashnote.helper.rx.SchedulerProvider;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class SplashScreenPresenter<V extends SplashScreenContract.View> extends BasePresenter<V>
    implements SplashScreenContract.Presenter<V> {

    @Inject
    SplashScreenPresenter(DataManager dataManager,
                          SchedulerProvider schedulerProvider,
                          CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void goToMainActivity() {
        Observer observer = new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
                getCompositeDisposable().add(d);
            }

            @Override
            public void onNext(Object o) {
                getMvpView().openMainActivity();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        Observable.timer(1, TimeUnit.SECONDS).subscribe(observer);
    }
}
