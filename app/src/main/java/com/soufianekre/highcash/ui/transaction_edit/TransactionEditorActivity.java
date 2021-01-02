package com.soufianekre.highcash.ui.transaction_edit;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.soufianekre.highcash.R;
import com.soufianekre.highcash.data.db.model.CashAccount;
import com.soufianekre.highcash.data.db.model.CashTransaction;
import com.soufianekre.highcash.data.db.model.TransactionCategory;
import com.soufianekre.highcash.helper.CategoryUtils;
import com.soufianekre.highcash.ui.a_base.BaseActivity;
import com.soufianekre.highcash.helper.AppUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maltaisn.calcdialog.CalcDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.soufianekre.highcash.ui.main.MainActivity.ACCOUNT_PARENT;
import static com.soufianekre.highcash.ui.main.MainActivity.RESULT_T;
import static com.soufianekre.highcash.helper.AppConst.TRANSACTION_ACCOUNT_PARENT;
import static com.soufianekre.highcash.helper.AppConst.TRANSACTION_IS_EDITING;
import static com.soufianekre.highcash.helper.AppConst.TRANSACTION_TO_EDIT_POS;
import static com.soufianekre.highcash.helper.AppUtils.MAIN_DATE_FORMAT;

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
    TextView transactionBalanceField;
    @BindView(R.id.expense_or_income_switch)
    SwitchCompat expenseOrIncomeSwitch;
    @BindView(R.id.transaction_date_field)
    TextView transactionDateField;
    @BindView(R.id.transaction_notes_field)
    EditText transactionNotesField;
    @BindView(R.id.save_transaction_fab)
    FloatingActionButton saveTransactionFab;
    @BindView(R.id.category_recycler_view)
    RecyclerView categoryRecyclerView;
    @BindView(R.id.category_selected_img)
    ImageView categorySelectedImg;

    private TransactionCategoryAdapter categoryAdapter;


    private CashAccount accountParent;

    private CashTransaction transactionToEdit;
    private int transactionToEditPos;
    private boolean isEditing;


    private String transactionDescription = "";
    private double transactionBalance = 0;
    private Date transactionDate = new Date();
    private boolean transactionIsExpense = false;
    private String transactionNotes = "";
    private TransactionCategory currentTransactionCategory;

    private long TomorrowInMillis = new Date().getTime() - 24*60*60*1000;

    private CalcDialog calcDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_edit);
        ButterKnife.bind(this);
        calcDialog = new CalcDialog();

        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(this);
        setupUi();
        checkIntent();
        setIncomeToExpenseSwitch();
        setTransactionDateListener();
        transactionBalanceField.setOnClickListener( v->{
            calcDialog.getSettings().setInitialValue(new BigDecimal(transactionBalance));
            calcDialog.show(getSupportFragmentManager(), "calc_dialog");
        });
        saveTransactionFab.setOnClickListener(v -> {
            saveTransaction();
        });

    }
    private void setupUi() {
        toolbar.setTitle(R.string.empty_string);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        List<TransactionCategory> transactionCategories = CategoryUtils.getAllCategories();
        categoryAdapter = new TransactionCategoryAdapter(this, transactionCategories, this);
        categoryRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        categoryRecyclerView.setLayoutManager(gridLayoutManager);
        categoryRecyclerView.setAdapter(categoryAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void setOldTransactionInfo(CashTransaction transactionToEdit) {
        if (transactionToEdit != null) {
            transactionDescription = transactionToEdit.getName();
            transactionBalance = transactionToEdit.getBalance();

            if (transactionToEdit.getLastUpdatedDate() != 0) transactionDate = new Date(transactionToEdit.getLastUpdatedDate());
            transactionIsExpense = transactionToEdit.isExpense();
            currentTransactionCategory = transactionToEdit.getCategory();
            transactionDescriptionField.setText(transactionDescription);
            transactionBalanceField.setText(String.valueOf(transactionToEdit.getBalance()));
        }
        //update widget
        if (currentTransactionCategory != null)
            categoryAdapter.setSelectedCategoryPosition(currentTransactionCategory);
        setTransactionDateField();
        expenseOrIncomeSwitch.setChecked(!transactionIsExpense);
        transactionBalanceField.setTextColor(transactionIsExpense ? Color.RED : Color.GREEN);
    }

    // date picker listener
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        setTransactionDate(year, monthOfYear, dayOfMonth);
    }




    private void checkIntent() {
        if (getIntent().getExtras() != null) {
            accountParent = getIntent().getExtras().getParcelable(TRANSACTION_ACCOUNT_PARENT);
            isEditing = getIntent().getBooleanExtra(TRANSACTION_IS_EDITING, false);
            if (isEditing && accountParent != null) {
                transactionToEditPos = getIntent().getExtras().getInt(TRANSACTION_TO_EDIT_POS, -1);
                transactionToEdit = accountParent.getTransactionsList().get(transactionToEditPos);
                transactionNotesField.setText(AppUtils.formatDate(new Date(transactionToEdit.getLastUpdatedDate()), MAIN_DATE_FORMAT));
                //change title To Edit transaction
                title.setText(R.string.edit_transaction);
            }
            setOldTransactionInfo(transactionToEdit);
        }
    }

    private void setTransactionDateListener() {
        transactionDateField.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dbp = DatePickerDialog
                    .newInstance(TransactionEditorActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH));
            dbp.setOkColor(Color.WHITE);
            dbp.setCancelColor(Color.WHITE);
            dbp.show(getSupportFragmentManager(), "DatePickerDialog");
        });
    }

    private void setIncomeToExpenseSwitch() {
        expenseOrIncomeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            transactionIsExpense = !isChecked;
            if (!transactionIsExpense) {
                expenseOrIncomeSwitch.setText(R.string.income);
                transactionBalanceField.setTextColor(Color.GREEN);

            } else {
                expenseOrIncomeSwitch.setText(R.string.expense);
                transactionBalanceField.setTextColor(Color.RED);
            }
        });
    }


    private void setTransactionDateField() {
        transactionDateField.setText(AppUtils.formatDate(transactionDate, MAIN_DATE_FORMAT));
    }


    private void setTransactionDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        int hour, minute;

        if (calendar.before(Calendar.getInstance())) return;
        calendar.set(year, monthOfYear, dayOfMonth);

        if (transactionDate != null) calendar.setTime(transactionDate);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        calendar.set(year, monthOfYear, dayOfMonth, hour, minute);
        transactionDate = calendar.getTime();

        setTransactionDateField();
    }


    private boolean CheckSubmittedData() {


        if (transactionDescriptionField.getText().toString().isEmpty() ||
                transactionBalanceField.getText().toString().isEmpty()) {
            transactionBalanceField.setError("You need to put some Data  !!");
            return false;

        } else if (transactionDate != null && transactionDate.before(new Date(TomorrowInMillis))) {
            Toast.makeText(TransactionEditorActivity.this,
                    getResources().getString(R.string.transaction_date_field_error),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (currentTransactionCategory == null)
            showMessage("You need to set A category !!");
        transactionDescription = transactionDescriptionField.getText().toString();
        transactionNotes = transactionNotesField.getText().toString();
        return true;

    }

    private void saveTransaction() {
        if (CheckSubmittedData()) {
            CashTransaction newTransaction = new CashTransaction();
            newTransaction.setName(transactionDescription);
            if (transactionDate != null)
                newTransaction.setLastUpdatedDate(transactionDate.getTime());
            if (transactionIsExpense && transactionBalance > 0 || !transactionIsExpense && transactionBalance < 0)
                transactionBalance *= -1;
            newTransaction.setBalance((int) transactionBalance);
            newTransaction.setAccountSourceId(accountParent.getAccountId());
            newTransaction.setLastUpdatedDate(new Date().getTime());
            newTransaction.setExpense(transactionIsExpense);
            newTransaction.setAccountParentName(accountParent.getName());
            newTransaction.setNotes(transactionNotes);
            newTransaction.setCategory(currentTransactionCategory);

            //
            if (!isEditing) accountParent.getTransactionsList().add(newTransaction);
            else accountParent.getTransactionsList().set(transactionToEditPos, newTransaction);

            presenter.saveTransaction(accountParent);
            Intent transactionActivityIntent = new Intent();
            // send the data back to The main activity
            transactionActivityIntent.putExtra(ACCOUNT_PARENT, accountParent);
            setResult(RESULT_T, transactionActivityIntent);
            finish();
        }
    }


    @Override
    public void animateCategory(TransactionCategory selectedCategory) {
        /*This variable indicates the value by which the fab should rotate and the direction */
        // TODO:Test Different Animations

        currentTransactionCategory = selectedCategory;
        // Rise Animations
        categorySelectedImg.animate()
                //.rotationBy(rotation)        // rest 180 covered by "shrink" animation
                .setDuration(100)
                .scaleX(1.3f)           //Scaling to 130%
                .scaleY(1.3f)
                //.withLayer()
                .withEndAction(() -> {
                    //Changing the icon by the end of animation
                    Glide.with(this)
                            .asDrawable()
                            .load(selectedCategory.getCategoryImage())
                            .into(categorySelectedImg);

                    categorySelectedImg.animate()
                            //.rotationBy(rotation)   //Complete the rest of the rotation
                            .setDuration(100)
                            .scaleX(1)              //Scaling back to what it was
                            .scaleY(1)
                            // hardware layout for optimizing animation (has some drawback)
                            .start();

                })
                .start();
        // Shrink Animation

    }

    @Override
    public void onValueEntered(int requestCode, @Nullable BigDecimal value) {
        if (value != null){
            transactionBalance = Double.parseDouble(value.toString());
            transactionBalanceField.setText(String.format("%.2f",transactionBalance));
        }


    }
}
