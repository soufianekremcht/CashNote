package com.soufianekre.cashnotes.ui.account_edit;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.soufianekre.cashnotes.R;
import com.soufianekre.cashnotes.data.db.model.CashAccount;
import com.soufianekre.cashnotes.helper.KeyboardUtils;
import com.soufianekre.cashnotes.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.soufianekre.cashnotes.helper.AppConst.ACCOUNT_TO_EDIT_ID;
import static com.soufianekre.cashnotes.ui.main.MainActivity.RESULT_A;

@SuppressLint("NonConstantResourceId")
public class AccountEditorActivity extends BaseActivity implements AccountEditorContract.View {


    @BindView(R.id.account_edit_layout)
    CoordinatorLayout accountEditLayout;
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


    private CashAccount accountToEdit;

    private String accountName="";
    private String accountDescription="";
    private int accountColor = -1;
    private int accountType = -1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);
        if (getActivityComponent() != null) {
            getActivityComponent().inject(this);
            setUnBinder(ButterKnife.bind(this));
            presenter.onAttach(this);
        }
        checkIntents();
        setupUi();
        KeyboardUtils.showSoftInput(this, accountNameField);
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

    @Override
    public void setCurrentAccountInfo(CashAccount account) {
        if (account != null) {
            accountToEdit = account;
            accountName = account.getName();
            accountDescription = account.getDescription();
            accountColor = account.getColor();
            changeStatusBarColor(accountColor);
            accountEditLayout.setBackgroundTintList(ColorStateList.valueOf(accountColor));
        }

        accountNameField.setText(accountName);
        accountDescriptionField.setText(accountDescription);
    }

    @Override
    public void saveAndExit() {
        Intent i = new Intent();
        setResult(RESULT_A, i);
        finish();
    }

    // ColorChooserDialog Listeners
    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
        accountColor = selectedColor;
        accountEditLayout.setBackgroundTintList(ColorStateList.valueOf(selectedColor));

    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {
        accountColorPicker.setColorFilter(accountColor);
        changeStatusBarColor(accountColor);

        accountEditLayout.setBackgroundTintList(ColorStateList.valueOf(accountColor));
    }


    private void checkIntents() {
        if (getIntent().getExtras() != null) {
            int accountToEditId = getIntent().getExtras().getInt(ACCOUNT_TO_EDIT_ID);
            if (accountToEditId != -1) {
                accountLayoutTitle.setText(R.string.edit_account);
                presenter.getAccountToEdit(accountToEditId);
            }
        } else {
            Timber.e("Account To Edit is %s", accountToEdit);
        }
    }


    private void setupUi() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Account");
        // change toolbar background color

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        accountSaveFab.setOnClickListener(v -> {
            onAccountSaved();

        });
        accountColorPicker.setOnClickListener(v -> showColorDialog());
    }


    private boolean getData() {
        accountName = accountNameField.getText().toString();
        accountDescription = accountDescriptionField.getText().toString();
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
                accountToEdit.setName(accountName);
                accountToEdit.setDescription(accountDescription);
                if (accountColor == -1) {
                    accountToEdit.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
                } else {
                    accountToEdit.setColor(accountColor);
                }

                accountToEdit.setType(accountType);
                presenter.saveNewAccount(accountToEdit);
            }
        }

    }

    public void showColorDialog() {
        // Pass a context, along with the title of the dialog
        boolean accentMode = false;
        int primaryPreselect = ContextCompat.getColor(this, R.color.accent_amber);
        ColorChooserDialog colorChooserDialog =
                new ColorChooserDialog.Builder(this, R.string.choose_color)
                        .titleSub(R.string.colors)  // title of dialog when viewing shades of a color
                        .accentMode(false)  // when true, will display accent palette instead of primary palette
                        .doneButton(R.string.confirm)  // changes label of the done button
                        .cancelButton(R.string.cancel)  // changes label of the cancel button
                        .backButton(R.string.go_back)// changes label of the back button
                        //.preselect()  // optionally preselects a color
                        .dynamicButtonColor(true)  // defaults to true, false will disable changing action buttons' color to currently selected color
                        .show(this); // an AppCompatActivity which implements ColorCallback

    }

    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
        ActionBar bar = getSupportActionBar();
        if (bar != null)
            bar.setBackgroundDrawable(new ColorDrawable(color));

    }


}
