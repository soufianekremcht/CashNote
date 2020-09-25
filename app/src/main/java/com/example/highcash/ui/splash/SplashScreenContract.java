package com.example.highcash.ui.splash;

import com.example.highcash.ui.base.BaseContract;

public interface SplashScreenContract {
    interface View extends BaseContract.MvpView {
        void openMainActivity();
    }

    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void goToMainActivity();
    }
}
