package com.example.highcash.ui.transaction_filter.filter;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.highcash.R;
import com.example.highcash.data.db.model.CashTransaction;
import com.example.highcash.helper.AppUtils;
import com.example.highcash.ui.base.BaseDialogFragment;
import com.example.highcash.ui.settings.SettingsContract;
import com.example.highcash.ui.transaction_filter.TransactionFilterActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;




public class TransactionFilterDialog extends BottomSheetDialogFragment {



    @BindView(R.id.filter_months_grid_view)
    GridView filterGridView;

    @BindView(R.id.filter_arrow_left_img)
    ImageView filterArrowLeftBtn;

    @BindView(R.id.filter_arrow_right_img)
    ImageView filterArrowRightBtn;

    @BindView(R.id.filter_year_text)
    TextView filterYearText;


    int year = 2020;

    private TransactionFilterActivity transactionFilterActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionFilterActivity = (TransactionFilterActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_transaction_filter,container,false);
        ButterKnife.bind(this,v);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] months =new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        List<String> list = Arrays.asList(months);
        MonthAdapter monthAdapter = new MonthAdapter(getActivity(), list);
        monthAdapter.item_selected_position = getCurrentActivity().selected_month_position;
        filterGridView.setAdapter(monthAdapter);

        filterGridView.setOnItemClickListener((parent, view1, position, id) -> {
            String chosenMonth = (String) parent.getItemAtPosition(position);
            getCurrentActivity().selected_month_position = position;
            getCurrentActivity().selected_year = year;
            Log.e("Filter dialog" , chosenMonth);
            transactionFilterActivity.filterTransactionsByMonth(chosenMonth,year);
            dismiss();
        });

        filterArrowLeftBtn.setOnClickListener(v->{
            reduceYear();
        });
        filterArrowRightBtn.setOnClickListener(v->{
            increaseYear();
        });
    }


    private static void filterByMonth(){
        CashTransaction cashTransaction = new CashTransaction("moe",200,new Date().getTime(),0,false,"");

        Date date = new Date(cashTransaction.getLastUpdatedDate());
        DateTime dateTime = new DateTime(date);
        int month = dateTime.monthOfYear().get();
        int year = dateTime.year().get();
        int day = dateTime.dayOfYear().get();

        System.out.println("Day : "+day + "Month " + month + "Year " + year);
        System.out.println(dateTime.toString("MMM"));




    }


    private void increaseYear() {
        year++;
        filterYearText.setText(String.format("%d", year));

    }

    private void reduceYear() {
        year--;
        filterYearText.setText(String.format("%d", year));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private int getMonth(long date){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        return month + 1;
    }
    private int  getYear(long date){

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        int year = cal.get(Calendar.YEAR);
        return year + 1;
    }


    private TransactionFilterActivity getCurrentActivity(){
        return (TransactionFilterActivity) getActivity();
    }
}
