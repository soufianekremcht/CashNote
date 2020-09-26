package com.example.highcash.ui.overview;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.highcash.MyApp;
import com.example.highcash.R;
import com.example.highcash.data.app_preference.PrefConst;
import com.example.highcash.data.db.model.BalanceHistory;
import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.data.db.model.CashTransaction;
import com.example.highcash.ui.a_base.BaseFragment;
import com.example.highcash.ui.overview.adapters.RecentAccountsAdapter;
import com.example.highcash.ui.overview.adapters.RecentTransactionsAdapter;
import com.example.highcash.ui.views.CustomItemDecoration;
import com.example.highcash.helper.chart.DayAxisFormatter;
import com.example.highcash.helper.chart.BalanceValueFormatter;
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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.highcash.helper.AppUtils.CURRENT_YEAR;
import static com.example.highcash.helper.AppUtils.checkCurrentYearDays;


public class OverViewFragment extends BaseFragment implements OverViewContract.View {


    public static void main(String[]args){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019,2,11);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        System.out.println("day of year " +day);
        System.out.println(" year " +year);
        System.out.println("What " + checkCurrentYearDays(calendar));
    }

    @BindView(R.id.recent_transaction_list)
    RecyclerView recentTransactionListView;
    @BindView(R.id.accounts_list)
    RecyclerView reducedAccountListView;
    @BindView(R.id.expense_chart)
    LineChart expenseIncomeChart;
    @BindView(R.id.monthly_summary_chart)
    PieChart summaryChart;
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

    public static OverViewFragment newInstance(){
        OverViewFragment fragment = new OverViewFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        // months start from 0
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_YEAR);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview,container,false);
        if (getActivityComponent() != null){
            getActivityComponent().inject(this);
            ButterKnife.bind(this,v);
            presenter.onAttach(this);
        }
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reducedAccountListView.setHasFixedSize(true);
        reducedAccountListView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        reducedAccountListView.addItemDecoration(new CustomItemDecoration(getContext()
                , CustomItemDecoration.VERTICAL_LIST,8));
        reducedAccountListView.setAdapter(recentAccountsAdapter);



        recentTransactionListView.setHasFixedSize(true);
        recentTransactionListView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        recentTransactionListView.addItemDecoration(new CustomItemDecoration(getActivity(),
                CustomItemDecoration.VERTICAL_LIST,8));
        recentTransactionListView.setAdapter(recentTransactionsAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getOverView();
        presenter.getBalanceHistory(currentDay,6);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        /*
        expenseIncomeChart.onFinishTemporaryDetach();
        summaryChart.onFinishTemporaryDetach();
        balanceChart.onFinishTemporaryDetach();*/
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setExpenseIncomeChart(List<CashTransaction> transactions){
        // Customize The Chart
        ValueFormatter xAxisFormatter = new DayAxisFormatter(expenseIncomeChart);

        XAxis xAxis = expenseIncomeChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        //xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);
        Description desc = new Description();
        /*** maybe this cause memory Leaks ***/
        /*desc.setText(String.format("Transactions per day in %s",
                MyApp.AppPref().getString(PrefConst.PREF_DEFAULT_CURRENCY,"USD")));

         */
        expenseIncomeChart.setDescription(desc);

        YAxis leftAxis = expenseIncomeChart.getAxisLeft();
        leftAxis.setLabelCount(6, false);
        ValueFormatter customValueFormatter = new BalanceValueFormatter();
        leftAxis.setValueFormatter(customValueFormatter);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);


        YAxis rightAxis = expenseIncomeChart.getAxisRight();
        rightAxis.setEnabled(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        ArrayList<Entry> ExpenseValues = new ArrayList<>();
        ArrayList<Entry> IncomeValues = new ArrayList<>();

        for (int i = 0; i <7; i++) {

            float incomeValue = 0f;
            float expenseValue = 0f;
            for (int j= 0; j<transactions.size(); j++) {
                CashTransaction transaction = transactions.get(j);
                calendar.setTimeInMillis(transaction.getLastUpdatedDate());
                // get time of Creation in days if it created this year
                int transactionLastUpdated  = checkCurrentYearDays(calendar);

                if (transactionLastUpdated == currentDay+i -6){
                    if (transaction.isExpense())
                        expenseValue -= (float) transaction.getBalance();
                    else
                        incomeValue += (float) transaction.getBalance();
                }
            }
            ExpenseValues.add(new Entry(currentDay +i -6,expenseValue));
            IncomeValues.add(new Entry(currentDay+i-6 ,incomeValue));
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
        expenseIncomeChart.animateX(2000);
        LineData data = new LineData(dataSets);
        expenseIncomeChart.setData(data);
        expenseIncomeChart.invalidate();
    }


    @Override
    public void setBalanceChart(List<BalanceHistory> balances,int days){
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

        // this replaces setStartAtZero(true)

  /*      YAxis rightAxis = balanceChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        //rightAxis.setTypeface(tfLight);
        rightAxis.setLabelCount(5, false);
        rightAxis.setValueFormatter(customValueFormatter);
        rightAxis.setSpaceTop(15f);
        */


        Description desc = new Description();
        desc.setText(String.format("Balance per day in %s",
                MyApp.AppPref().getString(PrefConst.PREF_DEFAULT_CURRENCY,"$")));

        balanceChart.setDescription(desc);


        List<BarEntry> barEntryValues = new ArrayList<>();
        if (days>balances.size()) days = balances.size();
        for (int i = 0 ;i<days;i++){
            float value = balances.get(i).getValue();
            //values.add(new BarEntry(currentDay-days+i,value));
            barEntryValues.add(new BarEntry(currentDay-i,value));
        }


        BarDataSet bardataSet = new BarDataSet(barEntryValues, "Balance");
        bardataSet.setColor(Color.GREEN);
        BarData barData = new BarData(bardataSet);
        balanceChart.animateX(2000);
        balanceChart.setData(barData);
    }

    @Override
    public void setSummaryChart(List<CashAccount> accounts){
        float totalExpense = 1f;
        float totalIncome = 1f;
        Description desc = new Description();
        desc.setText("Summary of this month's transaction");

        summaryChart.setDescription(desc);
        summaryChart.setCenterTextColor(Color.BLACK);
        summaryChart.setEntryLabelColor(Color.BLACK);
        summaryChart.setDrawEntryLabels(false);
        ArrayList<PieEntry> values = new ArrayList<>();
        List<CashTransaction> transactionsOfThisMonth = new ArrayList<>();

        // check the transactions of this month
        for (CashAccount cashAccount :accounts) {
            for (CashTransaction cashTransaction : cashAccount.getTransactionsList()) {
                calendar.setTimeInMillis(cashTransaction.getLastUpdatedDate());
                int year = calendar.get(Calendar.YEAR);
                if (year == CURRENT_YEAR){
                    int month = calendar.get(Calendar.MONTH);
                    if (month == currentMonth){
                        transactionsOfThisMonth.add(cashTransaction);
                        if (cashTransaction.isExpense())
                            totalExpense -= cashTransaction.getBalance();
                        else
                            totalIncome += cashTransaction.getBalance();
                    }
                }

            }
        }
        // draw a chart for the summary of total income and expenses
        values.add(new PieEntry(totalExpense,"Expense"));
        values.add(new PieEntry(totalIncome,"Income"));

        String summaryLabel = String.format(Locale.US," Summary for %d-%d",
                currentMonth+1,CURRENT_YEAR);

        PieDataSet set1 = new PieDataSet(values, summaryLabel);
        set1.setColors(ColorTemplate.createColors(new int[]{Color.RED,Color.GREEN}));
        set1.setValueTextColor(Color.BLACK);
        PieData pieData = new PieData(set1);

        summaryChart.animateXY(1000,1000);
        summaryChart.setData(pieData);

    }

    @Override
    public void updateRecentTransactions(List<CashTransaction> transactions) {
        recentTransactionsAdapter.addItems(transactions);
    }

    @Override
    public void updateRecentAccounts(List<CashAccount> accounts) {
        recentAccountsAdapter.addItems(accounts);
    }







}
