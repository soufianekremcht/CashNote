package com.soufianekre.cashnotes.ui.splash;

import com.soufianekre.cashnotes.ui.base.BaseContract;

public interface SplashScreenContract {
    interface View extends BaseContract.MvpView {
        void openMainActivity();
    }

    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void goToMainActivity();
    }
}
