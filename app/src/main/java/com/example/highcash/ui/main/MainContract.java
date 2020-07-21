package com.example.highcash.ui.main;

import com.example.highcash.ui.base.BaseContract;

public interface MainContract {
    public static interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void onDrawerOptionOverViewClick();
        void onDrawerOptionAccountsClick();
        void onViewInitialized();
        void setBalanceForCurrentDay();
        void saveBalanceToDb(int days, int year, int balanceValue);


    }

    public static interface View extends BaseContract.MvpView {
        void showAccountFragment();
        void showTransactionsFragment();
        void showOverViewFragment();
        void setBalance(int balanceValue);

    }
}
