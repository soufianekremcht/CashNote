package com.soufianekre.highcash.ui.transaction_filter;

import android.graphics.Color;
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

import com.soufianekre.highcash.R;
import com.soufianekre.highcash.data.db.model.CashTransaction;
import com.soufianekre.highcash.ui.app_base.BaseActivity;
import com.soufianekre.highcash.ui.transaction_filter.filter.TransactionFilterDialog;
import com.soufianekre.highcash.ui.transactions.TransactionsAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.soufianekre.highcash.helper.AppUtils.CURRENT_YEAR;

public class TransactionFilterActivity extends BaseActivity implements TransactionFilterContract.View {


    public static final String SELECTED_MONTH ="selected_month";

    @BindView(R.id.transaction_filter_toolbar)
    Toolbar transactionFilterToolbar;

    @BindView(R.id.month_filter_chooser_btn)
    TextView monthFilterChooserBtn;


    @BindView(R.id.transactions_filter_recycler_view)
    RecyclerView transactionsFilteredRecyclerView;

    @BindView(R.id.filter_empty_view)
    RelativeLayout filterEmptyView;

    @BindView(R.id.month_summary_chart)
    PieChart monthSummaryChart;
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
        setSupportActionBar(transactionFilterToolbar);

        transactionsAdapter = new TransactionsAdapter(this,new ArrayList<>());
        transactionsFilteredRecyclerView.setHasFixedSize(true);
        transactionsFilteredRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionsFilteredRecyclerView.setAdapter(transactionsAdapter);
        transactionsAdapter.setAdapterListener(this);
        monthFilterChooserBtn.setOnClickListener(v->{
            showFilterDialog();
        });
        transactionFilterToolbar.setTitle("Filter By Month :");
        mPresenter.getTransactions("",-1,false);


    }

    private void showFilterDialog() {
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
        transactionFilterToolbar.setTitle(String.format(" %s %s",month,year));
    }

    @Override
    public void showTransactions(List<CashTransaction> transactions) {
        setSummaryChart(transactions);
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
    public void onTransactionDelete(int position) {

    }


    public void setSummaryChart(List<CashTransaction> transactions){
        Calendar calendar = Calendar.getInstance();
        float totalExpense = 1f;
        float totalIncome = 1f;
        Description desc = new Description();
        desc.setText("Summary of The Month");
        monthSummaryChart.setDescription(desc);
        monthSummaryChart.setCenterTextColor(Color.BLACK);
        monthSummaryChart.setEntryLabelColor(Color.BLACK);
        monthSummaryChart.setDrawCenterText(false);
        monthSummaryChart.setDrawEntryLabels(false);
        ArrayList<PieEntry> values = new ArrayList<>();
        List<CashTransaction> transactionsOfThisMonth = new ArrayList<>();
        // check the transactions of this month
        for (CashTransaction cashTransaction : transactions) {
            if (cashTransaction.isExpense())
                totalExpense -= cashTransaction.getBalance();
            else
                totalIncome += cashTransaction.getBalance();
        }
        //Set Summary using Percent

        // draw a chart for the summary of total income and expenses
        values.add(new PieEntry(totalExpense,"Expense"));
        values.add(new PieEntry(totalIncome,"Income"));


        String summaryLabel = String.format(Locale.US," Summary for %d-%d",
                selected_month_position+1,CURRENT_YEAR);

        PieDataSet set1 = new PieDataSet(values, summaryLabel);
        set1.setColors(ColorTemplate.createColors(new int[]{Color.RED,Color.GREEN}));
        set1.setValueTextColor(Color.BLACK);
        PieData pieData = new PieData(set1);
        // testing
        monthSummaryChart.setUsePercentValues(true);
        monthSummaryChart.animateXY(1000,1000);
        monthSummaryChart.setData(pieData);
        monthSummaryChart.invalidate();

    }

}