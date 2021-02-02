package com.soufianekre.cashnote.ui.main;

import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.ui.base.BaseContract;
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
        void showOverViewFragment();
        void setBalance(int balanceValue);

    }
}
