package com.example.highcash.ui.transaction_filter;

import android.os.Bundle;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.highcash.R;
import com.example.highcash.data.db.model.CashTransaction;
import com.example.highcash.ui.account_editor.adapter.AccountCategory;
import com.example.highcash.ui.base.BaseActivity;
import com.example.highcash.ui.transaction_filter.filter.TransactionFilterDialog;
import com.example.highcash.ui.transactions.TransactionsAdapter;

import java.util.ArrayList;
import java.util.List;

public class TransactionFilterActivity extends BaseActivity implements TransactionFilterContract.View {


    public static final String SELECTED_MONTH ="selected_month";

    @BindView(R.id.transaction_filter_toolbar)
    Toolbar transactionFilterToolbar;

    @BindView(R.id.month_filter_chooser_btn)
    TextView monthFilterChooserBtn;

    @BindView(R.id.transaction_filter_title_text)
    TextView transactionFilterTitleText;

    @BindView(R.id.transactions_filter_recycler_view)
    RecyclerView transactionsFilteredRecyclerView;

    @BindView(R.id.filter_empty_view)
    RelativeLayout filterEmptyView;

    private TransactionsAdapter transactionsAdapter;
    public int selected_month_position = -1;
    public int selected_year = 2020;
    private boolean isFilterShown = false;


    @Inject
    TransactionFilterPresenter<TransactionFilterContract.View> mPresenter;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_filter);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(TransactionFilterActivity.this);
        setupUi();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private void setupUi(){
        transactionFilterToolbar.setNavigationOnClickListener(v->{
            onBackPressed();
        });
        transactionFilterToolbar.setTitle("");
        setSupportActionBar(transactionFilterToolbar);

        transactionsAdapter = new TransactionsAdapter(this,new ArrayList<>());
        transactionsFilteredRecyclerView.setHasFixedSize(true);
        transactionsFilteredRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionsFilteredRecyclerView.setAdapter(transactionsAdapter);
        transactionsAdapter.setAdapterListener(this);
        monthFilterChooserBtn.setOnClickListener(v->{
            showFilter();
        });
        transactionFilterTitleText.setText(R.string.all);
        mPresenter.getTransactions("",-1,false);


    }

    private void showFilter() {
        isFilterShown = true;
        TransactionFilterDialog filterDialog = new TransactionFilterDialog();
        filterDialog.show(getSupportFragmentManager(),"transaction_filter_dialog");
    }

    @Override
    protected void onDestroy() {
        selected_month_position = -1;
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void filterTransactionsByMonth(String month, int year){
        mPresenter.getTransactions(month,year,true);
        transactionFilterTitleText.setText(String.format("%s %s",month,year));
    }

    @Override
    public void showTransactions(List<CashTransaction> transactions) {
        transactionsAdapter.addItems(transactions);
        checkEmptyView();
    }

    private void checkEmptyView() {
        if (transactionsAdapter.getItemCount()>0){
            transactionsFilteredRecyclerView.setVisibility(View.VISIBLE);
            filterEmptyView.setVisibility(View.GONE);
        }else{
            transactionsFilteredRecyclerView.setVisibility(View.GONE);
            filterEmptyView.setVisibility(View.VISIBLE);
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
    public String getAccountSource() {
        return null;
    }

    @Override
    public AccountCategory getAccountCategory() {
        return null;
    }

}