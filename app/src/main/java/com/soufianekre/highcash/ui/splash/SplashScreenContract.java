package com.soufianekre.highcash.ui.splash;

import com.soufianekre.highcash.ui.app_base.BaseContract;

public interface SplashScreenContract {
    interface View extends BaseContract.MvpView {
        void openMainActivity();
    }

    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void goToMainActivity();
    }
}
