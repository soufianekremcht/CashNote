package com.soufianekre.cashnote.ui.settings.export;

import com.soufianekre.cashnote.data.db.model.CashTransaction;
import com.soufianekre.cashnote.ui.app_base.BaseContract;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.List;

public interface ExportDataContract {
    interface View extends BaseContract.MvpView, DatePickerDialog.OnDateSetListener {
        void exportToCSV(List<CashTransaction> transactions);
        void exportToExcel(List<CashTransaction> transactions);
    }
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V>{

        void getTransactionData();

    }
}
