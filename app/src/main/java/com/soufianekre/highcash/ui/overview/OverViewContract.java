package com.soufianekre.highcash.ui.overview;

import com.soufianekre.highcash.data.db.model.BalanceHistory;
import com.soufianekre.highcash.data.db.model.CashAccount;
import com.soufianekre.highcash.data.db.model.CashTransaction;
import com.soufianekre.highcash.ui.app_base.BaseContract;

import java.util.List;

public interface OverViewContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void getOverView();
        void getBalanceHistory(int currentDay, int daysBackward);

    }

    interface View extends BaseContract.MvpView {

        void setExpenseIncomeChart(List<CashTransaction> accounts);
        void setBalanceChart(List<BalanceHistory> balance_history, int days);
        void setSummaryChart(List<CashAccount> accounts);
        void updateRecentTransactions(List<CashTransaction> transactions);
        void updateRecentAccounts(List<CashAccount> accounts);

    }
}
