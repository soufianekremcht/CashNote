package com.example.highcash.ui.transactions.search;


import com.example.highcash.data.db.model.CashTransaction;
import com.example.highcash.ui.base.BaseContract;

import java.util.List;

public interface SearchContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {
        void onQuerySubmitted(String query);

    }

    interface View extends BaseContract.MvpView {
        void notifyAdapter(List<CashTransaction> transactions);
    }
}
