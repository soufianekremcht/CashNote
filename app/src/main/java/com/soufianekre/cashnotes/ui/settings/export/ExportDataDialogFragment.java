package com.soufianekre.cashnotes.ui.settings.export;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.soufianekre.cashnotes.BuildConfig;
import com.soufianekre.cashnotes.MyApp;
import com.soufianekre.cashnotes.R;
import com.soufianekre.cashnotes.data.app_preference.PrefsConst;
import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.di.component.ActivityComponent;
import com.soufianekre.cashnotes.helper.AppUtils;
import com.soufianekre.cashnotes.ui.base.BaseDialogFragment;
import com.soufianekre.cashnotes.ui.settings.SettingsActivity;
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
import timber.log.Timber;

import static com.soufianekre.cashnotes.helper.AppUtils.MAIN_DATE_FORMAT;

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
        builder.setTitle(R.string.export_to);
        // connect The presenter With View
        mPresenter.onAttach(this);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            mPresenter.getTransactionData();
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
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
        String baseDirPath = android.os.Environment.getExternalStorageDirectory()+ "/CashNoteFiles";
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
                            MyApp.AppPref().getString(PrefsConst.PREF_DEFAULT_CURRENCY, "$"));
                    column[2] = AppUtils.formatDate(new Date(transaction.getLastUpdatedDate()),AppUtils.MAIN_DATE_FORMAT);
                    writer.writeNext(column);
                }
                writer.close();

                shareFile(csvFile);

            } catch (IOException e) {
                Timber.e(e.getLocalizedMessage());
            }

        }

    }

    @Override
    public void exportToExcel(List<CashTransaction> transactions) {

    }


    private void shareFile(File file){
        Timber.e("Sharing");
        try {
            Uri fileUri =  FileProvider.getUriForFile(
                    getSettingsActivity(),
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    file);
            if (fileUri != null) {
                // Put the Uri and MIME type in the result Intent
                Timber.e("Sharing File ");
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                sharingIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                ClipData clipData = ClipData.newUri(getActivity().getContentResolver(),
                        getString(R.string.app_name),
                        fileUri);

                sharingIntent.setClipData(clipData);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_file_to)));
            }
        }catch (IllegalArgumentException e) {
            Timber.e("The selected file can't be shared: %s", file.toString());
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
        dbp.show(getChildFragmentManager(),"ExportTimeFrameDialog");
    }

    private void setExportDate(int year, int monthOfYear, int dayOfMonth){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,monthOfYear,dayOfMonth);
        Date exportDate = new Date(calendar.getTimeInMillis());
        setDateField(exportDate);
    }


    private void setDateField(Date date){
        if (isStartPicker)
            exportDataStartDateText.setText(AppUtils.formatDate(date,MAIN_DATE_FORMAT));
        else
            exportDataEndDateText.setText(AppUtils.formatDate(date,MAIN_DATE_FORMAT));
    }




    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        setExportDate(year,monthOfYear,dayOfMonth);

    }
}


