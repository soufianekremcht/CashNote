package com.soufianekre.cashnote.ui.splash;

import com.soufianekre.cashnote.ui.base.BaseContract;

public interface SplashScreenContract {
    interface View extends BaseContract.MvpView {
        void openMainActivity();
    }

    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void goToMainActivity();
    }
}
