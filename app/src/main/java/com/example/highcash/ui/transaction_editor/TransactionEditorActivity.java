package com.example.highcash.ui.transaction_editor;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.highcash.R;
import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.data.db.model.CashTransaction;
import com.example.highcash.ui.base.BaseActivity;
import com.example.highcash.helper.AppUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.example.highcash.ui.main.MainActivity.RESULT_T;
import static com.example.highcash.helper.AppConst.ACCOUNT_PARENT_ID;
import static com.example.highcash.helper.AppConst.TRANSACTION_ACCOUNT;
import static com.example.highcash.helper.AppConst.TRANSACTION_IS_EDITING;
import static com.example.highcash.helper.AppConst.TRANSACTION_TO_EDIT_POS;
import static com.example.highcash.helper.AppUtils.MAIN_DATE_FORMAT;

public class TransactionEditorActivity extends BaseActivity
        implements DatePickerDialog.OnDateSetListener
                , TransactionEditorContract.View {



    @Inject
    TransactionEditorContract.Presenter<TransactionEditorContract.View> presenter;

    @BindView(R.id.add_transaction_toolbar)
    Toolbar toolbar;

    @BindView(R.id.add_transaction_title_text)
    TextView title;

    @BindView(R.id.transaction_description_field)
    EditText transactionDescriptionField;

    @BindView(R.id.transaction_amount_field)
    EditText transactionBalanceField;

    @BindView(R.id.expense_or_income_switch)
    Switch expenseOrIncomeSwitch;

    @BindView(R.id.transaction_date_field)
    TextView transactionDateField;

    @BindView(R.id.transaction_notes_field)
    EditText transactionNotesField;

    @BindView(R.id.save_transaction_fab)
    FloatingActionButton saveTransactionFab;


    private CashAccount accountParent;

    private CashTransaction transactionToEdit;
    private int transactionToEditPos;
    private boolean isEditing;



    private String transactionDescription ="";
    private String transactionNotes ="";
    private double transactionBalance = 0;
    private Date transactionDate = new Date();
    private boolean transactionIsExpense = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_editor);
        ButterKnife.bind(this);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(this);
        setupUi();
        checkIntent();
        setIncomeToExpenseSwitch();
        setTransactionDateListener();

        saveTransactionFab.setOnClickListener(v -> {
            saveTransaction();
        });

    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }


    @Override
    public void setOldTransactionInfo(CashTransaction transactionToEdit){
        if (transactionToEdit != null){
            transactionDescription = transactionToEdit.getTitle();
            transactionBalance = transactionToEdit.getBalance();
            if (transactionToEdit.getLastUpdatedDate() != 0)
                transactionDate = new Date(transactionToEdit.getLastUpdatedDate());
            transactionIsExpense = transactionToEdit.isExpense();
            transactionDescriptionField.setText(transactionDescription);
            transactionBalanceField.setText(String.valueOf(transactionToEdit.getBalance()));
        }
        //update widget
        setTransactionDateField();
        expenseOrIncomeSwitch.setChecked(!transactionIsExpense);
        transactionBalanceField.setTextColor(transactionIsExpense ? Color.RED:Color.GREEN);
    }

    // date picker listener
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        setTransactionDate(year,monthOfYear,dayOfMonth);
    }



    private void setupUi() {
        toolbar.setTitle(R.string.empty_string);
        //remove the shadows
        //getActionBar().setElevation(0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

    }

    private void checkIntent(){
        accountParent = getIntent().getExtras().getParcelable(TRANSACTION_ACCOUNT);
        isEditing = getIntent().getBooleanExtra(TRANSACTION_IS_EDITING,false);
        if (isEditing && accountParent != null){
            transactionToEditPos = getIntent().getExtras().getInt(TRANSACTION_TO_EDIT_POS,-1);
            transactionToEdit = accountParent.getTransactionsList().get(transactionToEditPos);
            transactionNotesField.setText(AppUtils.formatDate(new Date(transactionToEdit.getDate()),MAIN_DATE_FORMAT));
            //change title To Edit transaction
            title.setText("Edit Transaction");
        }
        setOldTransactionInfo(transactionToEdit);


    }

    private void setTransactionDateListener(){

        transactionDateField.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dbp = DatePickerDialog
                    .newInstance(TransactionEditorActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH));
            dbp.setOkColor(Color.WHITE);
            dbp.setCancelColor(Color.WHITE);
            dbp.show(getSupportFragmentManager(),"DatePickerDialog");
        });
    }
    private void setIncomeToExpenseSwitch(){
        expenseOrIncomeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            transactionIsExpense = !isChecked;
            if (!transactionIsExpense){
                expenseOrIncomeSwitch.setText(R.string.income);
                transactionBalanceField.setTextColor(Color.GREEN);

            }else{
                expenseOrIncomeSwitch.setText(R.string.expense);
                transactionBalanceField.setTextColor(Color.RED);
            }
        });
    }


    private void setTransactionDateField(){
        transactionDateField.setText(AppUtils.formatDate(transactionDate,MAIN_DATE_FORMAT));
    }


    private void setTransactionDate(int year, int monthOfYear, int dayOfMonth){
        Calendar calendar = Calendar.getInstance();
        int hour,minute;

        if (calendar.before(Calendar.getInstance())) return;
        calendar.set(year,monthOfYear,dayOfMonth);

        if (transactionDate != null) calendar.setTime(transactionDate);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        calendar.set(year, monthOfYear, dayOfMonth, hour, minute);
        transactionDate = calendar.getTime();

        setTransactionDateField();
    }


    private boolean CheckSubmittedData(){
        transactionBalance = Integer.parseInt(transactionBalanceField.getText().toString());
        transactionDescription = transactionDescriptionField.getText().toString();

        if (transactionDescription.equals("")){
            transactionBalanceField.setError("You need to put A description !!");
            return false;
        }else if (transactionDate != null && transactionDate.before(new Date())) {
            Toast.makeText(TransactionEditorActivity.this,
                    getResources().getString(R.string.transaction_date_field_error),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        transactionNotes = transactionNotesField.getText().toString();
        return true;

    }
    private void saveTransaction(){
        if (CheckSubmittedData()){
            CashTransaction newTransaction = new CashTransaction();
            newTransaction.setTitle(transactionDescription);
            if (transactionDate != null) newTransaction.setLastUpdatedDate(transactionDate.getTime());
            if (transactionIsExpense && transactionBalance >0 || !transactionIsExpense && transactionBalance <0)
                transactionBalance *= -1;
            newTransaction.setBalance((int) transactionBalance);
            newTransaction.setAccountSourceId(accountParent.getAccountId());
            newTransaction.setDate(new Date().getTime());
            newTransaction.setLastUpdatedDate(new Date().getTime());
            newTransaction.setExpense(transactionIsExpense);
            if (!isEditing){
                accountParent.getTransactionsList().add(newTransaction);
            }else{
                accountParent.getTransactionsList().set(transactionToEditPos,newTransaction);
            }
            presenter.saveTransaction(accountParent);


            Intent mainActivityIntent = new Intent();
            // send the data back to The main activity
            mainActivityIntent.putExtra(ACCOUNT_PARENT_ID, accountParent.getAccountId());
            setResult(RESULT_T,mainActivityIntent);
            finish();
        }

    }

}
