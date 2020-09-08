package com.example.highcash.ui.account_editor;


import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.highcash.R;
import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.ui.account_editor.adapter.AccountCategory;
import com.example.highcash.ui.account_editor.adapter.AccountCategoryAdapter;
import com.example.highcash.ui.account_editor.adapter.ColorAdapter;
import com.example.highcash.ui.base.BaseActivity;
import com.example.highcash.ui.views.colors.ColorPreference;
import com.example.highcash.helper.AccountCategoryUtils;
import com.example.highcash.helper.AppUtils;
import com.example.highcash.helper.KeyboardUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.example.highcash.helper.AppConst.ACCOUNT_TO_EDIT_ID;
import static com.example.highcash.ui.main.MainActivity.RESULT_A;

public class AccountEditorActivity extends BaseActivity implements AccountEditorContract.View {


    @BindView(R.id.add_account_toolbar)
    Toolbar toolbar;

    @BindView(R.id.add_account_title_text)
    TextView accountLayoutTitle;

    @BindView(R.id.account_name_field)
    EditText accountNameField;

    @BindView(R.id.account_description_field)
    EditText accountDescriptionField;

    @BindView(R.id.account_save_fab)
    FloatingActionButton accountSaveFab;

//    @BindView(R.id.account_color_picker)
    ImageView accountColorPicker;

    @BindView(R.id.account_category_recycler_view)
    RecyclerView accountCategoryRecyclerView;

    @BindView(R.id.category_selected_img)
    ImageView categorySelectedImg;

    @Inject
    AccountEditorContract.Presenter<AccountEditorContract.View> presenter;

    private MaterialDialog colorDialog;
    private CashAccount accountToEdit;
    private int accountToEditId;
    private String accountName;
    private String accountDescription;
    private int accountColor;
    private int accountType;
    private AccountCategory currentCategory;

    private AccountCategoryAdapter accountCategoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_editor);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        presenter.onAttach(this);
        checkIntents();
        setupUi();
        KeyboardUtils.showSoftInput(this, accountDescriptionField);
        KeyboardUtils.showSoftInput(this, accountNameField);

        setListeners();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    private void checkIntents(){
        if (getIntent().getExtras() != null){
            accountToEditId= getIntent().getExtras().getInt(ACCOUNT_TO_EDIT_ID);
            if(accountToEditId != -1) {
                accountLayoutTitle.setText(R.string.edit_account);
                presenter.getAccountToEdit(accountToEditId);
            }

        }else{
            showMessage("Account To Edit is " + accountToEdit);
        }
    }
    @Override
    public void setEditedAccountInfo(CashAccount account){
        if (account != null){
            accountToEdit = account;

            accountName = account.getName();
            accountDescription = account.getDescription();
            accountType = 0;
            accountColor = account.getColor();
            currentCategory = account.getCategory();
            //accountCategoryAdapter.setSelected(currentCategory);
        }else{
            accountType = 0;
        }

        int currentColor = R.color.accent_blue;
        accountColor = AppUtils.getColor(this, currentColor);
        accountNameField.setText(accountName);
        accountDescriptionField.setText(accountDescription);
    }



    private void setupUi() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Account");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        if  (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        accountCategoryRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        accountCategoryRecyclerView.setLayoutManager(gridLayoutManager);
        accountCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());

       accountCategoryAdapter = new AccountCategoryAdapter( this,
                AccountCategoryUtils.getAllCategories(),this);
        accountCategoryRecyclerView.setAdapter(accountCategoryAdapter);


    }

    private void setListeners(){
        //accountColorPicker.setOnClickListener(v -> colorChangeDialog());
        accountSaveFab.setOnClickListener(v -> {
                onAccountSaved();
        });
    }




    private boolean getData(){
        accountName = accountNameField.getText().toString();
        if (accountName.equals("")){
            accountNameField.setError("You need to set A name for the account !!");
            return false;
        }
        return true;
    }

    public void onAccountSaved(){
        if (getData()){
            if (currentCategory == null)
                showMessage("Error : You Should Add A Category: ");
            else{
                if (accountToEdit != null){
                    accountToEdit.setName(accountName);
                    accountToEdit.setDescription(accountDescription);
                    accountToEdit.setColor(accountColor);
                    accountToEdit.setType(accountType);
                    accountToEdit.setCategory(currentCategory);
                    presenter.saveEditedAccount(accountToEdit);
                }else {
                    accountToEdit = new CashAccount();
                    accountToEdit.setTransactionsList(new ArrayList<>());
                    accountToEdit.setName(accountName);
                    accountToEdit.setDescription(accountDescription);
                    accountToEdit.setColor(accountColor);
                    accountToEdit.setType(accountType);
                    accountToEdit.setCategory(currentCategory);
                    presenter.saveNewAccount(accountToEdit);
                }
                Intent i = new Intent();
                setResult(RESULT_A,i);
                finish();
            }

        }

    }

    @Override
    public void animateCategory(AccountCategory selectedCategory) {
        /*This variable indicates the value by which the fab should rotate and the direction */
        // TODO:Test Different Animations

        currentCategory = selectedCategory;
        int rotation = 180;
        // Rise Animations
        categorySelectedImg.animate()
                //.rotationBy(rotation)        // rest 180 covered by "shrink" animation
                .setDuration(100)
                .scaleX(1.3f)           //Scaling to 130%
                .scaleY(1.3f)
                //.withLayer()
                .withEndAction(() -> {
                    //Changing the icon by the end of animation
                    categorySelectedImg.setImageResource(selectedCategory.getCategoryImage());
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
}