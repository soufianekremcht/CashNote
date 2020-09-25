package com.example.highcash.ui.transactions.search;


import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.highcash.R;
import com.example.highcash.data.db.model.CashTransaction;
import com.example.highcash.helper.KeyboardUtils;
import com.example.highcash.ui.base.BaseActivity;
import com.example.highcash.ui.transactions.TransactionsAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity implements SearchContract.View {

    @BindView(R.id.search_result_recycler_view)
    RecyclerView searchResultRecyclerView;
    @BindView(R.id.search_empty_data_view)
    RelativeLayout searchEmptyView;
    @BindView(R.id.search_toolbar)
    Toolbar searchToolbar;
    @BindView(R.id.search_field)
    SearchView searchView;

    @Inject
    TransactionsAdapter transactionsAdapter;

    @Inject
    SearchPresenter<SearchContract.View> mPresenter;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        mPresenter.onAttach(this);
        setupUi();

    }


    private void setupUi() {
        setSupportActionBar(searchToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.onQuerySubmitted(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPresenter.onQuerySubmitted(newText);
                return true;
            }
        });
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                KeyboardUtils.showSoftInput(SearchActivity.this, v);


        });
        searchView.requestFocus();
        transactionsAdapter.setAdapterListener(this);

        searchResultRecyclerView.setHasFixedSize(true);
        searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultRecyclerView.setAdapter(transactionsAdapter);


    }


    @Override
    public void onDestroy() {
        KeyboardUtils.hideSoftInput(this);
        mPresenter.onDetach();
        super.onDestroy();
    }


    @Override
    public void notifyAdapter(List<CashTransaction> transactions) {
        transactionsAdapter.addItems(transactions);
        checkEmptyData();
    }

    private void checkEmptyData(){
        if (transactionsAdapter.getItemCount()>0){
            searchResultRecyclerView.setVisibility(View.VISIBLE);
            searchEmptyView.setVisibility(View.GONE);
        }else{
            searchResultRecyclerView.setVisibility(View.GONE);
            searchEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTransactionClick(int position) {

    }

    @Override
    public boolean onTransactionLongClick(int position) {
        return false;
    }

    @Override
    public void onTransactionDelete(int position) {

    }
    @Override
    public void onTransactionEditClicked(View v, int position) {

    }
}
