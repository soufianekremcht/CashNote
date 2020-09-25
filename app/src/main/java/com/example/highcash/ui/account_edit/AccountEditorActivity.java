package com.example.highcash.ui.account_edit;


import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.example.highcash.R;
import com.example.highcash.data.db.model.CashAccount;
import com.example.highcash.ui.base.BaseActivity;
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

    @BindView(R.id.account_desc_field)
    EditText accountDescriptionField;

    @BindView(R.id.account_save_fab)
    FloatingActionButton accountSaveFab;

    @BindView(R.id.account_color_picker)
    ImageView accountColorPicker;


    @Inject
    AccountEditorContract.Presenter<AccountEditorContract.View> presenter;

    private ColorChooserDialog colorChooserDialog;
;

    private CashAccount accountToEdit;


    private String accountName;
    private String accountDescription;
    private int accountColor = R.color.colorPrimary;
    private int accountType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        presenter.onAttach(this);
        checkIntents();
        setupUi();
        KeyboardUtils.showSoftInput(this, accountNameField);
    }


    private void setupUi() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Account");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        accountSaveFab.setOnClickListener(v -> {
            onAccountSaved();

        });
        accountColorPicker.setOnClickListener( v-> showColorDialog());


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

    private void checkIntents() {
        if (getIntent().getExtras() != null) {
            int accountToEditId = getIntent().getExtras().getInt(ACCOUNT_TO_EDIT_ID);
            if (accountToEditId != -1) {
                accountLayoutTitle.setText(R.string.edit_account);
                presenter.getAccountToEdit(accountToEditId);
            }

        } else {
            showMessage("Account To Edit is " + accountToEdit);
        }
    }

    @Override
    public void setEditedAccountInfo(CashAccount account) {
        if (account != null) {
            accountToEdit = account;

            accountName = account.getName();
            accountDescription = account.getDescription();
            accountType = 0;
            accountColor = account.getColor();
        } else {
            accountType = 0;
        }

        accountNameField.setText(accountName);
        accountDescriptionField.setText(accountDescription);
    }


    private boolean getData() {
        accountName = accountNameField.getText().toString();
        if (accountName.equals("")) {
            accountNameField.setError("You need to set A name for the account !!");
            return false;
        }
        return true;
    }

    public void onAccountSaved() {
        if (getData()) {
            if (accountToEdit != null) {
                accountToEdit.setName(accountName);
                accountToEdit.setDescription(accountDescription);
                accountToEdit.setColor(accountColor);
                accountToEdit.setType(accountType);
                presenter.saveEditedAccount(accountToEdit);
            } else {
                accountToEdit = new CashAccount();
                accountToEdit.setTransactionsList(new ArrayList<>());
                accountToEdit.setName(accountName);
                accountToEdit.setDescription(accountDescription);
                accountToEdit.setColor(accountColor);
                accountToEdit.setType(accountType);
                presenter.saveNewAccount(accountToEdit);
            }
            Intent i = new Intent();
            setResult(RESULT_A, i);
            finish();
        }

    }

    public void showColorDialog(){
        // Pass a context, along with the title of the dialog
        boolean accentMode = false;

        colorChooserDialog =
                new ColorChooserDialog.Builder(this, R.string.choose_color)
                .titleSub(R.string.colors)  // title of dialog when viewing shades of a color
                .accentMode(false)  // when true, will display accent palette instead of primary palette
                .doneButton(R.string.confirm)  // changes label of the done button
                .cancelButton(R.string.cancel)  // changes label of the cancel button
                .backButton(R.string.go_back)  // changes label of the back button
                // .preselect(accentMode ? accentPreselect : primaryPreselect)  // optionally preselects a color
                .dynamicButtonColor(true)  // defaults to true, false will disable changing action buttons' color to currently selected color
                .show(this); // an AppCompatActivity which implements ColorCallback

    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
        accountColor = selectedColor;

    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {
        accountColorPicker.setColorFilter(accountColor);
    }
}
