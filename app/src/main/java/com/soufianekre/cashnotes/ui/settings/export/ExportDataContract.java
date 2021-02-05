package com.soufianekre.cashnotes.ui.settings.export;

import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.ui.base.BaseContract;
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
