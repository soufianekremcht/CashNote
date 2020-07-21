package com.example.highcash.ui.overview;

import com.example.highcash.data.db.model.BalanceHistory;
import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.data.db.model.CashTransaction;
import com.example.highcash.ui.base.BaseContract;

import java.util.List;

public interface OverViewContract {
    public static interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void getOverView();
        void getBalanceHistory(int currentDay, int daysBackward);

    }

    public static interface View extends BaseContract.MvpView {

        void setExpenseChart(List<CashTransaction> accounts);
        void setBalanceChart(List<BalanceHistory> balance_history, int days);
        void setSummaryChart(List<CashAccount> accounts);
        void updateRecentTransactions(List<CashTransaction> transactions);
        void updateRecentAccounts(List<CashAccount> accounts);

    }
}
