package com.soufianekre.highcash.ui.a_base;

import androidx.annotation.StringRes;

import io.reactivex.Flowable;

public interface BaseContract {
    interface MvpPresenter<V extends MvpView>{

        void onAttach(V mvpView);
        void onDetach();
        <T> Flowable<T> manageObservable(Flowable observable);
        void handleError(String tag, Throwable throwable);


    }

    interface MvpView {

        void onError(@StringRes int resId);

        void onError(String message);

        void showMessage(String message);

        void showMessage(@StringRes int resId);

        void hideKeyboard();
    }
}
