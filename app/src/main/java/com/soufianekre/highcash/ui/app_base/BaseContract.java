package com.soufianekre.highcash.ui.app_base;

import io.reactivex.Flowable;

public interface BaseContract {
    interface MvpPresenter<V extends MvpView>{

        void onAttach(V mvpView);
        void onDetach();
        <T> Flowable<T> manageObservable(Flowable observable);
        void handleError(String tag, Throwable throwable);


    }

    interface MvpView {


        void showError(String message);
        void showMessage(String message);

        void hideKeyboard();
    }
}
