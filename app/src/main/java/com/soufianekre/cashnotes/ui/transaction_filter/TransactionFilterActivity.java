package com.soufianekre.cashnotes.ui.transaction_filter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.soufianekre.cashnotes.MyApp;
import com.soufianekre.cashnotes.R;
import com.soufianekre.cashnotes.data.app_preference.PrefsConst;
import com.soufianekre.cashnotes.data.db.model.CashAccount;
import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.ui.base.BaseActivity;
import com.soufianekre.cashnotes.ui.transaction_filter.filter.TransactionFilterDialog;
import com.soufianekre.cashnotes.ui.transactions.TransactionsAdapter;
import com.soufianekre.cashnotes.ui.transactions.show_transaction.ShowTransactionFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")

public class TransactionFilterActivity extends BaseActivity implements TransactionFilterContract.View {

    //CONSTANTS

    public static final String FILTER_SHOW_TRANSACTION_TAG ="filter_show_transaction" ;

    public static final String SELECTED_MONTH = "selected_month";
    public int selected_month_position = -1;
    public int selected_year = 2020;


    @BindView(R.id.transaction_filter_toolbar)
    Toolbar transactionFilterToolbar;
    @BindView(R.id.month_filter_chooser_btn)
    TextView monthFilterChooserBtn;
    @BindView(R.id.transactions_filter_recycler_view)
    RecyclerView transactionsFilteredRecyclerView;
    @BindView(R.id.filter_empty_view)
    LinearLayout filterEmptyView;
    @BindView(R.id.month_summary_chart)
    PieChart monthPieChart;
    @Inject
    TransactionFilterPresenter<TransactionFilterContract.View> mPresenter;
    private TransactionsAdapter transactionsAdapter;

    private boolean isFilterShown = false;
    private List<CashAccount> allAccounts = new ArrayList<>();

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
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.main_menu_search).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApp.AppPref().getBoolean(PrefsConst.ACTIVATE_DARK_MODE)) {
            monthPieChart.setEntryLabelColor(Color.WHITE);
            monthPieChart.getDescription().setTextColor(Color.WHITE);
            monthPieChart.getLegend().setTextColor(Color.WHITE);
        }else{
            monthPieChart.setEntryLabelColor(Color.BLACK);
            monthPieChart.getDescription().setTextColor(Color.BLACK);
            monthPieChart.getLegend().setTextColor(Color.BLACK);
        }
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

    public void filterTransactionsByMonth(String month, int year) {
        mPresenter.getTransactions(month, year, true);
        transactionFilterToolbar.setTitle(String.format(" %s %s", month, year));
    }


    @Override
    public void updateAccountList(List<CashAccount> accounts) {
        allAccounts = accounts;
    }


    @Override
    public void notifyAdapter(List<CashTransaction> transactions) {
        setSummaryChart(transactions);
        transactionsAdapter.addItems(transactions);
        checkEmptyView();
    }

    public void setSummaryChart(List<CashTransaction> transactions) {
        float totalExpense = 1f;
        float totalIncome = 1f;
        Description desc = new Description();
        desc.setText("Summary of The Month");

        monthPieChart.setUsePercentValues(true);

        monthPieChart.getDescription().setEnabled(false);
        //monthSummaryPieChart.setCenterTextTypeface(tfLight);
        //monthSummaryPieChart.setCenterText(generateCenterSpannableText());

        monthPieChart.setDrawHoleEnabled(true);
        monthPieChart.setHoleColor(Color.TRANSPARENT);

        monthPieChart.setTransparentCircleColor(Color.WHITE);
        monthPieChart.setTransparentCircleAlpha(110);

        monthPieChart.setHoleRadius(58f);
        monthPieChart.setTransparentCircleRadius(61f);

        monthPieChart.setDrawCenterText(true);

        monthPieChart.setRotationEnabled(false);
        monthPieChart.setHighlightPerTapEnabled(true);

        monthPieChart.setMaxAngle(180f); // HALF CHART
        monthPieChart.setRotationAngle(180f);
        monthPieChart.setCenterTextOffset(0, -20);


        monthPieChart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = monthPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        monthPieChart.setEntryLabelColor(Color.WHITE);
        monthPieChart.setEntryLabelTextSize(12f);


        // Getting Data


        ArrayList<PieEntry> values = new ArrayList<>();
        List<CashTransaction> transactionsOfThisMonth = new ArrayList<>();
        // check the transactions of this month
        for (CashTransaction cashTransaction : transactions) {
            if (cashTransaction.isExpense())
                totalExpense -= cashTransaction.getBalance();
            else
                totalIncome += cashTransaction.getBalance();
        }
        // set Summary using Percent

        // draw a chart for the summary of total income and expenses
        values.add(new PieEntry(totalExpense, "Expense"));
        values.add(new PieEntry(totalIncome, "Income"));




        PieDataSet set1 = new PieDataSet(values, "Summary");
        set1.setColors(ColorTemplate.MATERIAL_COLORS);
        //set1.setColors(ColorTemplate.createColors(new int[]{Color.RED, Color.GREEN}));
        set1.setSliceSpace(3f);
        set1.setSelectionShift(5f);
        PieData pieData = new PieData(set1);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextColor(Color.WHITE);
        if (!MyApp.AppPref().getBoolean(PrefsConst.ACTIVATE_DARK_MODE)){
            pieData.setValueTextColor(Color.BLACK);
        }
        // testing
        monthPieChart.setData(pieData);
        monthPieChart.invalidate();

    }


    @Override
    public void onTransactionClick(CashTransaction cashTransaction, int position) {
        ShowTransactionFragment dialog = ShowTransactionFragment.newInstance(cashTransaction, position,false);
        dialog.setDialogListener(null);
        dialog.show(getSupportFragmentManager(), FILTER_SHOW_TRANSACTION_TAG);

    }

    @Override
    public boolean onTransactionLongClick(int position) {
        return false;
    }

    @Override
    public void onTransactionDeleteClicked(CashTransaction cashTransaction, int position) {

    }

    private void setupUi() {
        transactionFilterToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        setSupportActionBar(transactionFilterToolbar);

        transactionsAdapter = new TransactionsAdapter(this, new ArrayList<>());
        transactionsFilteredRecyclerView.setHasFixedSize(true);
        transactionsFilteredRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionsFilteredRecyclerView.setAdapter(transactionsAdapter);
        transactionsAdapter.setAdapterListener(this);
        monthFilterChooserBtn.setOnClickListener(v -> {
            showFilterDialog();
        });
        transactionFilterToolbar.setTitle("Filter By Month :");
        mPresenter.getTransactions("", -1, false);


    }

    private void showFilterDialog() {
        isFilterShown = true;
        TransactionFilterDialog filterDialog = new TransactionFilterDialog();
        filterDialog.show(getSupportFragmentManager(), "transaction_filter_dialog");
    }


    private void checkEmptyView() {
        if (transactionsAdapter.getItemCount() > 0) {
            transactionsFilteredRecyclerView.setVisibility(View.VISIBLE);
            filterEmptyView.setVisibility(View.GONE);
        } else {
            transactionsFilteredRecyclerView.setVisibility(View.GONE);
            filterEmptyView.setVisibility(View.VISIBLE);
        }
    }

}