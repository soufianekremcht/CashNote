package com.soufianekre.cashnotes.ui.transaction_edit;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maltaisn.calcdialog.CalcDialog;
import com.soufianekre.cashnotes.R;
import com.soufianekre.cashnotes.data.db.model.CashAccount;
import com.soufianekre.cashnotes.data.db.model.CashCategory;
import com.soufianekre.cashnotes.data.db.model.CashTransaction;
import com.soufianekre.cashnotes.helper.AppUtils;
import com.soufianekre.cashnotes.helper.CategoryUtils;
import com.soufianekre.cashnotes.ui.base.BaseActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;
import timber.log.Timber;

import static com.soufianekre.cashnotes.helper.AppConst.TRANSACTION_ACCOUNT;
import static com.soufianekre.cashnotes.helper.AppConst.TRANSACTION_TO_EDIT;
import static com.soufianekre.cashnotes.helper.AppUtils.MAIN_DATE_FORMAT;
import static com.soufianekre.cashnotes.ui.main.MainActivity.RESULT_T;

@SuppressLint("NonConstantResourceId")
public class TransactionEditorActivity extends BaseActivity
        implements DatePickerDialog.OnDateSetListener
        , TransactionEditorContract.View {

    private final long TomorrowInMillis = new Date().getTime() - 24 * 60 * 60 * 1000;
    @Inject
    TransactionEditorContract.Presenter<TransactionEditorContract.View> presenter;


    @BindView(R.id.add_transaction_toolbar)
    Toolbar toolbar;

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

    // intent info
    private CashTransaction transactionToEdit;
    private CashAccount selected_account;

    //
    private TransactionCategoryAdapter categoryAdapter;


    private String transactionDescription = "";
    private double transactionBalance = 0;
    private Date transactionDate = new Date();
    private boolean transactionIsExpense = false;
    private String transactionNotes = "";
    private CashCategory currentTransactionCategory;
    // Calculator as Dialog
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
        transactionBalanceField.setOnClickListener(v -> {
            calcDialog.getSettings().setInitialValue(new BigDecimal(transactionBalance));
            calcDialog.show(getSupportFragmentManager(), "calc_dialog");
        });
        saveTransactionFab.setOnClickListener(v -> {
            saveTransaction();
        });

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
        if (calcDialog != null && calcDialog.isVisible())
            calcDialog.dismiss();
        presenter.onDetach();
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

            if (transactionToEdit.getLastUpdatedDate() != 0)
                transactionDate = new Date(transactionToEdit.getLastUpdatedDate());
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

    @Override
    public void saveAndFinish() {
        Intent transactionActivityIntent = new Intent();
        transactionActivityIntent.putExtra(TRANSACTION_ACCOUNT, selected_account);
        setResult(RESULT_T, transactionActivityIntent);

        finish();
    }

    // date picker listener
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        setTransactionDate(year, monthOfYear, dayOfMonth);
    }


    @Override
    public void onValueEntered(int requestCode, @Nullable BigDecimal value) {
        if (value != null) {
            transactionBalance = Double.parseDouble(value.toString());
            transactionBalanceField.setText(String.format("%.2f", transactionBalance));
        }
    }

    @Override
    public void animateCategory(CashCategory selectedCategory) {

        /*This variable indicates the value by which the fab should rotate and the direction */
        // TODO:Test Different Animations

        currentTransactionCategory = selectedCategory;
        // Rise Animations
        categorySelectedImg.animate()
                .setDuration(100)
                .scaleX(1.3f)
                .scaleY(1.3f)
                //.withLayer()
                .withEndAction(() -> {
                    //Changing the icon by the end of animation
                    Glide.with(this)
                            .asDrawable()
                            .load(selectedCategory.getImage())
                            .into(categorySelectedImg);
                    categorySelectedImg.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                    categorySelectedImg.setBackgroundTintList(ColorStateList.valueOf(selectedCategory.getColor()));

                    categorySelectedImg.animate()
                            //.rotationBy(rotation)   //Complete the rest of the rotation
                            .setDuration(250)
                            .scaleX(1)
                            .scaleY(1)
                            // hardware layout for optimizing animation (has some drawback)
                            .start();

                })
                .start();
    }


    private void setupUi() {
        setSupportActionBar(toolbar);
        // change toolbar background color
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        AppBarLayout mAppBarLayout = findViewById(R.id.add_transaction_app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //backupDescriptionCard.setAlpha((verticalOffset+252)/252f);

                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                Timber.d("Backup Collapsing VerticalOffset : %d", verticalOffset);
                Timber.d("Backup Scroll Range %d", scrollRange);
                if (scrollRange + verticalOffset == 10) {
                    isShow = true;
                    //showOption(R.id.action_info);
                    // hide Description

                } else if (isShow) {
                    isShow = false;
                    // show Description
                }
            }
        });

        setupCategoryList(false);



    }

    private void setupCategoryList(boolean isExpense) {

        List<CashCategory> categories;
        if (isExpense){
            categories = CategoryUtils.getExpensesCategories(this);
        }else{
            categories = CategoryUtils.getIncomeCategories(this);
        }

        categoryAdapter = new TransactionCategoryAdapter(this, categories, this);
        categoryRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        categoryRecyclerView.setLayoutManager(gridLayoutManager);
        categoryRecyclerView.setAdapter(categoryAdapter);
    }


    private void checkIntent() {
        if (getIntent().getExtras() != null) {
            transactionToEdit = getIntent().getParcelableExtra(TRANSACTION_TO_EDIT);
            selected_account = getIntent().getParcelableExtra(TRANSACTION_ACCOUNT);
            if (transactionToEdit != null) {
                transactionNotesField.setText(AppUtils.formatDate(new Date(transactionToEdit.getLastUpdatedDate()), MAIN_DATE_FORMAT));
                toolbar.setTitle(R.string.edit_transaction);
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
            dbp.setThemeDark(true);
            dbp.show(getSupportFragmentManager(), "DatePickerDialog");
        });
    }

    private void setIncomeToExpenseSwitch() {
        expenseOrIncomeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            transactionIsExpense = !isChecked;
            if (!transactionIsExpense) {
                expenseOrIncomeSwitch.setText(R.string.income);
                transactionBalanceField.setTextColor(Color.GREEN);
                setupCategoryList(false);


            } else {
                expenseOrIncomeSwitch.setText(R.string.expense);
                transactionBalanceField.setTextColor(Color.RED);
                setupCategoryList(true);
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
            showError("You need to put something first !!");
            return false;

        } else if (transactionDate != null && transactionDate.before(new Date(TomorrowInMillis))) {
            showError(getResources().getString(R.string.transaction_date_field_error));
            return false;
        } else if (currentTransactionCategory == null) {
            showError("You need to set A category !!");
            return false;
        }
        transactionDescription = transactionDescriptionField.getText().toString();
        transactionNotes = transactionNotesField.getText().toString();
        return true;

    }

    private void saveTransaction() {
        if (CheckSubmittedData()) {
            CashTransaction newTransaction = new CashTransaction();
            newTransaction.setName(transactionDescription);
            if (transactionDate != null) {
                newTransaction.setLastUpdatedDate(transactionDate.getTime());
            }
            if (transactionIsExpense && transactionBalance > 0 ||
                    !transactionIsExpense && transactionBalance < 0) {
                transactionBalance *= -1;
            }
            newTransaction.setBalance((int) transactionBalance);
            newTransaction.setLastUpdatedDate(new Date().getTime());
            newTransaction.setExpense(transactionIsExpense);
            newTransaction.setNotes(transactionNotes);
            newTransaction.setCategory(currentTransactionCategory);

            if (transactionToEdit != null) {
                newTransaction.setId(transactionToEdit.getId());
                newTransaction.setAccountId(transactionToEdit.getAccountId());
                presenter.updateTransaction(newTransaction);
            } else {
                newTransaction.setAccountId(selected_account.getId());
                presenter.insertNewTransaction(newTransaction);
            }

        }
    }


}
