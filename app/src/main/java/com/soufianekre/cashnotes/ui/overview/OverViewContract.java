package com.soufianekre.cashnotes.ui.overview;

import com.soufianekre.cashnotes.data.db.model.BalanceHistory;
import com.soufianekre.cashnotes.data.db.model.CashAccount;
import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.ui.base.BaseContract;

import java.util.List;

public interface OverViewContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void getOverView();
        void getBalanceHistory(int currentDay, int daysBackward);

    }

    interface View extends BaseContract.MvpView {

        void setupExpenseIncomeLineChart(List<CashTransaction> transactions);
        void setBalanceChart(List<BalanceHistory> balance_history, int days);
        void setupSummaryPieChart(List<CashTransaction> transactions);
        void updateRecentAccounts(List<CashAccount> accounts);
        void updateRecentTransactions(List<CashTransaction> allTransactions);

    }
}
