package com.soufianekre.cashnotes.ui.overview;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.soufianekre.cashnotes.MyApp;
import com.soufianekre.cashnotes.R;
import com.soufianekre.cashnotes.data.app_preference.PrefsConst;
import com.soufianekre.cashnotes.data.db.model.BalanceHistory;
import com.soufianekre.cashnotes.data.db.model.CashAccount;
import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.helper.chart.BalanceValueFormatter;
import com.soufianekre.cashnotes.helper.chart.DayAxisFormatter;
import com.soufianekre.cashnotes.ui.base.BaseFragment;
import com.soufianekre.cashnotes.ui.main.MainActivity;
import com.soufianekre.cashnotes.ui.overview.adapters.RecentAccountsAdapter;
import com.soufianekre.cashnotes.ui.overview.adapters.RecentTransactionsAdapter;
import com.soufianekre.cashnotes.ui.views.CustomItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.soufianekre.cashnotes.helper.AppUtils.CURRENT_YEAR;
import static com.soufianekre.cashnotes.helper.AppUtils.checkCurrentYearDays;

@SuppressLint("NonConstantResourceId")

public class OverViewFragment extends BaseFragment implements OverViewContract.View {


    @BindView(R.id.recent_transaction_list)
    RecyclerView recentTransactionListView;
    @BindView(R.id.accounts_list)
    RecyclerView reducedAccountListView;
    @BindView(R.id.expense_chart)
    LineChart expenseIncomeLineChart;
    @BindView(R.id.monthly_summary_chart)
    PieChart summaryPieChart;
    @BindView(R.id.balance_history_chart)
    BarChart balanceChart;


    @Inject
    OverViewContract.Presenter<OverViewContract.View> presenter;
    @Inject
    RecentTransactionsAdapter recentTransactionsAdapter;
    @Inject
    RecentAccountsAdapter recentAccountsAdapter;


    private Calendar calendar;
    private int currentMonth;
    private int currentDay;

    private MainActivity mainActivity;

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, 2, 11);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        System.out.println("day of year " + day);
        System.out.println(" year " + year);
        System.out.println("What " + checkCurrentYearDays(calendar));
    }

    public static OverViewFragment newInstance() {
        OverViewFragment fragment = new OverViewFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        mainActivity = (MainActivity) getActivity();

        // months start from 0

        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_YEAR);
        mainActivity.getFab().hide();
        mainActivity.getMainToolbar().setTitle("Overview");

        setRetainInstance(true);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        if (getActivityComponent() != null) {
            getActivityComponent().inject(this);
            setUnBinder(ButterKnife.bind(this, v));
            presenter.onAttach(this);
        }
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reducedAccountListView.setHasFixedSize(true);
        reducedAccountListView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        reducedAccountListView.addItemDecoration(new CustomItemDecoration(view.getContext()
                , CustomItemDecoration.VERTICAL_LIST, 8));
        reducedAccountListView.setAdapter(recentAccountsAdapter);


        recentTransactionListView.setHasFixedSize(true);
        recentTransactionListView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recentTransactionListView.addItemDecoration(new CustomItemDecoration(view.getContext(),
                CustomItemDecoration.VERTICAL_LIST, 8));
        recentTransactionListView.setAdapter(recentTransactionsAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getOverView();
        presenter.getBalanceHistory(currentDay, 6);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        summaryPieChart.onFinishTemporaryDetach();
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setupSummaryPieChart(List<CashTransaction> transactions) {
        float totalExpense = 1f;
        float totalIncome = 1f;
        Description desc = new Description();
        desc.setText("Summary of this month");
        int textColor = ContextCompat.getColor(getActivity(),R.color.colorOnSurface);

        summaryPieChart.setDescription(desc);
        summaryPieChart.setUsePercentValues(true);
        summaryPieChart.setExtraOffsets(5, 10, 5, 5);
        summaryPieChart.setCenterTextColor(Color.BLACK);
        summaryPieChart.setEntryLabelColor(Color.BLACK);
        summaryPieChart.setDrawEntryLabels(false);
        // not showing the center text
        summaryPieChart.setDrawCenterText(false);

        ArrayList<PieEntry> values = new ArrayList<>();
        List<CashTransaction> transactionsOfThisMonth = new ArrayList<>();

        // check the transactions of this month

        for (CashTransaction cashTransaction : transactions) {
            // check cash flow of the current month
            calendar.setTimeInMillis(cashTransaction.getLastUpdatedDate());
            int year = calendar.get(Calendar.YEAR);
            if (year == CURRENT_YEAR) {
                int month = calendar.get(Calendar.MONTH);
                if (month == currentMonth) {
                    transactionsOfThisMonth.add(cashTransaction);
                    if (cashTransaction.isExpense())
                        totalExpense -= cashTransaction.getBalance();
                    else
                        totalIncome += cashTransaction.getBalance();
                }
            }

        }

        // draw a chart for the summary of total income and expenses

        values.add(new PieEntry(totalExpense, "Expense"));
        values.add(new PieEntry(totalIncome, "Income"));

        String summaryLabel = String.format(" Summary for %d-%d", currentMonth + 1, CURRENT_YEAR);
        PieDataSet set1 = new PieDataSet(values, summaryLabel);

        set1.setColors(ColorTemplate.createColors(new int[]{Color.RED, Color.GREEN}));
        set1.setValueTextColor(textColor);


        PieData pieData = new PieData(set1);
        pieData.setValueFormatter(new PercentFormatter());

        summaryPieChart.animateXY(1000, 1000);
        summaryPieChart.setData(pieData);
        summaryPieChart.invalidate();


    }

    @Override
    public void setupExpenseIncomeLineChart(List<CashTransaction> transactions) {
        // Customize The Chart
        ValueFormatter xAxisFormatter = new DayAxisFormatter(expenseIncomeLineChart);

        XAxis xAxis = expenseIncomeLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        //xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);


        /* maybe this cause memory Leaks ***/
        /* Desc.setText(String.format("Transactions per day in %s",
                MyApp.AppPref().getString(PrefConst.PREF_DEFAULT_CURRENCY,"USD")));

         */

        YAxis leftAxis = expenseIncomeLineChart.getAxisLeft();
        leftAxis.setLabelCount(6, false);
        ValueFormatter customValueFormatter = new BalanceValueFormatter();
        leftAxis.setValueFormatter(customValueFormatter);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        YAxis rightAxis = expenseIncomeLineChart.getAxisRight();
        rightAxis.setEnabled(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        ArrayList<Entry> ExpenseValues = new ArrayList<>();
        ArrayList<Entry> IncomeValues = new ArrayList<>();

        for (int i = 0; i < 7; i++) {

            float incomeValue = 0f;
            float expenseValue = 0f;
            for (int j = 0; j < transactions.size(); j++) {
                CashTransaction transaction = transactions.get(j);
                calendar.setTimeInMillis(transaction.getLastUpdatedDate());
                // get time of Creation in days if it created this year
                int transactionLastUpdated = checkCurrentYearDays(calendar);

                if (transactionLastUpdated == currentDay + i - 6) {
                    if (transaction.isExpense())
                        expenseValue -= (float) transaction.getBalance();
                    else
                        incomeValue += (float) transaction.getBalance();
                }
            }
            ExpenseValues.add(new Entry(currentDay + i - 6, expenseValue));
            IncomeValues.add(new Entry(currentDay + i - 6, incomeValue));
        }

        LineDataSet expenseDataSet = new LineDataSet(ExpenseValues, " Expense");
        expenseDataSet.setColor(Color.RED);
        dataSets.add(expenseDataSet);

        LineDataSet incomeDataSet = new LineDataSet(IncomeValues, " Income");
        incomeDataSet.setColor(Color.GREEN);
        dataSets.add(incomeDataSet);
        /*
        dataSets.setDrawCircleHole(true);
         */
        expenseIncomeLineChart.animateX(2000);
        LineData data = new LineData(dataSets);
        expenseIncomeLineChart.setData(data);
        expenseIncomeLineChart.invalidate();
    }


    @Override
    public void setBalanceChart(List<BalanceHistory> balances, int days) {
        balanceChart.setMaxVisibleValueCount(7);
        ValueFormatter xAxisFormatter = new DayAxisFormatter(balanceChart);

        XAxis xAxis = balanceChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);
        // Currency suffix

        YAxis rightAxis = balanceChart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = balanceChart.getAxisLeft();
        //leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(6, false);
        ValueFormatter customValueFormatter = new BalanceValueFormatter();
        leftAxis.setValueFormatter(customValueFormatter);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setStartAtZero(true);
        // this replaces setStartAtZero(true)


        Description desc = new Description();
        desc.setText(String.format("Balance per day in %s",
                MyApp.AppPref().getString(PrefsConst.PREF_DEFAULT_CURRENCY, "$")));

        balanceChart.setDescription(desc);


        List<BarEntry> barEntryValues = new ArrayList<>();
        if (days > balances.size()) days = balances.size();
        for (int i = 0; i < days; i++) {
            float value = balances.get(i).getValue();
            //values.add(new BarEntry(currentDay-days+i,value));
            barEntryValues.add(new BarEntry(currentDay - i, value));
        }


        BarDataSet bardataSet = new BarDataSet(barEntryValues, "Balance");
        bardataSet.setColor(Color.GREEN);
        BarData barData = new BarData(bardataSet);
        balanceChart.animateX(2000);
        balanceChart.setData(barData);
        balanceChart.invalidate();
    }


    @Override
    public void updateRecentTransactions(List<CashTransaction> allTransactions) {
        List<CashTransaction> recentTransactions = new ArrayList<>();

        // sort transaction from latest one
        Collections.sort(allTransactions, (o1, o2) -> {
            long c = o2.getLastUpdatedDate() - o1.getLastUpdatedDate();
            if (c > 0) return 1;
            else return -1;

        });

        setupExpenseIncomeLineChart(allTransactions);
        setupSummaryPieChart(allTransactions);

        if (allTransactions.size() < 5) {
            recentTransactions.addAll(allTransactions);
        } else {
            for (int i = 0; i < 4; i++) {
                recentTransactions.add(allTransactions.get(i));
            }
        }
        recentTransactionsAdapter.addItems(recentTransactions);
    }

    @Override
    public void updateRecentAccounts(List<CashAccount> accounts) {

        List<CashAccount> recentAccounts = new ArrayList<>();

        // sort transaction from latest one
        Collections.sort(accounts, (o1, o2) -> {
            long c = o2.getCreatedAt() - o1.getCreatedAt();
            if (c > 0) return 1;
            else return -1;

        });
        if (accounts.size() < 5) {
            recentAccounts.addAll(accounts);
        } else {
            for (int i = 0; i < 4; i++) {
                recentAccounts.add(accounts.get(i));
            }
        }

        recentAccountsAdapter.addItems(recentAccounts);
    }


}
