package com.soufianekre.highcash.ui.settings.export;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.soufianekre.highcash.MyApp;
import com.soufianekre.highcash.R;
import com.soufianekre.highcash.data.app_preference.PrefConst;
import com.soufianekre.highcash.data.db.model.CashTransaction;
import com.soufianekre.highcash.di.component.ActivityComponent;
import com.soufianekre.highcash.helper.AppUtils;
import com.soufianekre.highcash.ui.app_base.BaseDialogFragment;
import com.soufianekre.highcash.ui.settings.SettingsActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.opencsv.CSVWriter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.soufianekre.highcash.helper.AppUtils.MAIN_DATE_FORMAT;

public class ExportDataDialogFragment extends BaseDialogFragment implements ExportDataContract.View {

    @BindView(R.id.export_data_start_date)
    TextView exportDataStartDateText;
    @BindView(R.id.export_data_end_date)
    TextView exportDataEndDateText;
    @BindView(R.id.export_data_type_spinner)
    Spinner exportDataTypeSpinner;

    @Inject
    ExportDataContract.Presenter<ExportDataContract.View> mPresenter;

    boolean isStartPicker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

        View dialog_view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_export_data,null);
        setupUI(dialog_view);
        builder.setView(dialog_view);
        builder.setTitle("Export Form :");
        // connect The presenter With View
        mPresenter.onAttach(this);
        builder.setPositiveButton("OK", (dialog, which) -> {
            mPresenter.getTransactionData();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dismiss();
        });

        return builder.create();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }


    private void setupUI(View dialog_view) {
        ButterKnife.bind(this,dialog_view);
        ActivityComponent activityComponent = getBaseActivity().getActivityComponent();
        if (activityComponent != null){
            activityComponent.inject(this);
            mPresenter.onAttach(this);
        }

        exportDataStartDateText.setOnClickListener(v->{
            isStartPicker = true;
            setDatePicker();
        });

        exportDataEndDateText.setOnClickListener(v->{
            isStartPicker = false;
            setDatePicker();
        });


    }
    @Override
    public void exportToCSV(List<CashTransaction> transactions)  {
        // Go Through All The Transactions in The DB And Export Them As CSV
        showMessage("Csv Exporting ");
        String baseDirPath = android.os.Environment.getExternalStorageDirectory()+ "/HighCashData";
        File baseDir = new File(baseDirPath);
        boolean dirIsCreated = baseDir.mkdir();
        if (dirIsCreated){
            String fileName = String.format("transactions_%s.csv",AppUtils.formatDate(new Date(),MAIN_DATE_FORMAT));
            String filePath = baseDir + File.separator + fileName;
            File csvFile = new File(filePath);
            CSVWriter writer;
            FileWriter fileWriter;
            // File exist
            try {
                if(csvFile.exists() && !csvFile.isDirectory()) {
                    fileWriter = new FileWriter(filePath, true);
                    writer = new CSVWriter(fileWriter);
                }
                else writer = new CSVWriter(new FileWriter(filePath));
                // write The data
                String[] columnNames = new String[]{"Name","Balance","Date","Last Updated"};
                writer.writeNext(columnNames);


                for (CashTransaction transaction : transactions){
                    String[] column = new String[4];
                    column[0] = transaction.getName();
                    column[1] = String.format("%d %s", transaction.getBalance(),
                            MyApp.AppPref().getString(PrefConst.PREF_DEFAULT_CURRENCY, "$"));
                    column[2] = AppUtils.formatDate(new Date(transaction.getLastUpdatedDate()),AppUtils.MAIN_DATE_FORMAT);
                    writer.writeNext(column);
                }
                writer.close();
                shareFile(csvFile);

            } catch (IOException e) {
                Log.e("Export CSV",e.getLocalizedMessage());
            }

        }

    }

    @Override
    public void exportToExcel(List<CashTransaction> transactions) {

    }


    private void shareFile(File file){
        Log.e("Share File","Sharing");
        try {
            Uri fileUri =  FileProvider.getUriForFile(
                    getSettingsActivity(),
                    "com.example.highcash.fileprovider",
                    file);
            if (fileUri != null) {
                // Put the Uri and MIME type in the result Intent
                Log.e("Share File","Sharing What ");
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_csv_file)));
            }
        }catch (IllegalArgumentException e) {
            Log.e("File Selector",
                    "The selected file can't be shared: " + file.toString());
        }

    }

    private SettingsActivity getSettingsActivity() {
        return (SettingsActivity) getActivity();
    }

    private void setDatePicker(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dbp = DatePickerDialog
                .newInstance(this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
        dbp.setOkColor(Color.WHITE);
        dbp.setCancelColor(Color.WHITE);
        dbp.show(getChildFragmentManager(),"ExportEndDatePickerDialog");
    }

    private void setDateField(Date date){
        if (isStartPicker)
            exportDataStartDateText.setText(AppUtils.formatDate(date,MAIN_DATE_FORMAT));
        else
            exportDataEndDateText.setText(AppUtils.formatDate(date,MAIN_DATE_FORMAT));
    }


    private void setExportDate(int year, int monthOfYear, int dayOfMonth){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,monthOfYear,dayOfMonth);
        Date exportDate = new Date(calendar.getTimeInMillis());
        setDateField(exportDate);
    }



    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        setExportDate(year,monthOfYear,dayOfMonth);

    }
}


