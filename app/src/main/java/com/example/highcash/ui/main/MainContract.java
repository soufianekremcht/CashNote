package com.example.highcash.ui.main;

import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.ui.a_base.BaseContract;
import com.google.android.material.navigation.NavigationView;

public interface MainContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void onDrawerOptionOverViewClick();
        void onDrawerOptionAccountsClick();
        void setBalanceForCurrentDay();
        void saveBalanceToDb(int days, int year, int balanceValue);


    }

    interface View extends BaseContract.MvpView , NavigationView.OnNavigationItemSelectedListener {
        void showAccountFragment();
        void showTransactionsActivity(CashAccount account);
        void showOverViewFragment();
        void setBalance(int balanceValue);

    }
}
