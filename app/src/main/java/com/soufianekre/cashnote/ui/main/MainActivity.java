package com.soufianekre.cashnote.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.soufianekre.cashnote.R;

import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.helper.AppUtils;
import com.soufianekre.cashnote.ui.account_edit.AccountEditorActivity;
import com.soufianekre.cashnote.ui.accounts.AccountsFragment;
import com.soufianekre.cashnote.ui.app_base.BaseActivity;
import com.soufianekre.cashnote.ui.overview.OverViewFragment;
import com.soufianekre.cashnote.ui.settings.SettingsActivity;
import com.soufianekre.cashnote.ui.transaction_filter.TransactionFilterActivity;

import com.soufianekre.cashnote.ui.transactions.TransactionsActivity;
import com.soufianekre.cashnote.ui.transactions.search.SearchActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends BaseActivity implements MainContract.View {
    public static final String FRAGMENT_ACCOUNT_TAG = "fragment_account";
    private static final String FRAGMENT_OVERVIEW_TAG = "overviewFragment";


    public static final int REFRESH_TRANSACTION_LIST_CODE = 55;
    public static final int REFRESH_ACCOUNT_LIST_CODE = 44;
    public static final int RESULT_T = 533;
    public static final int RESULT_A = 511;
    public static final String ACCOUNT_PARENT = "account_parent";

    public int CurrentDaysCount = AppUtils.getCurrentDaysCount();
    public int CurrentYear = AppUtils.getCurrentYear();


    @BindView(R.id.main_drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_nav_view)
    NavigationView mainNavView;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.add_account_fab)
    FloatingActionButton addAccountTransactionFab;

    private ActionBarDrawerToggle drawerToggle;

    @Inject
    MainContract.Presenter<MainContract.View> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);

         */
        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);

        setupUi();
        if (savedInstanceState == null)
            showAccountFragment();

    }


    private void checkThemeSwitch(){
        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're in day time
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're at night!
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                // We don't know what mode we're in, assume notnight
        }
    }


    private void setupUi() {
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //mainToolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerToggle.setDrawerSlideAnimationEnabled(true);
        drawerToggle.syncState();
        mainNavView.setNavigationItemSelectedListener(this);



        addAccountTransactionFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AccountEditorActivity.class);
            startActivityForResult(intent, REFRESH_ACCOUNT_LIST_CODE);
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.main_menu_search:
                Intent search = new Intent(this, SearchActivity.class);
                startActivity(search);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_A) {
            showAccountFragment();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (getCurrentFragment() instanceof OverViewFragment)
            addAccountTransactionFab.hide();
        else
            addAccountTransactionFab.show();

        presenter.setBalanceForCurrentDay();
    }


    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }


    @Override
    public void showAccountFragment() {
        loadFragmentWithoutAnimations(AccountsFragment.newInstance(), FRAGMENT_ACCOUNT_TAG);
        setToolbarTitle(R.string.accounts);
    }

    @Override
    public void showTransactionsActivity(CashAccount account) {
        Intent transactionActivityIntent = new Intent(this, TransactionsActivity.class);
        transactionActivityIntent.putExtra(ACCOUNT_PARENT, account);
        startActivity(transactionActivityIntent);
    }

    @Override
    public void showOverViewFragment() {
        loadFragment(OverViewFragment.newInstance(), FRAGMENT_OVERVIEW_TAG);
    }

    @Override
    public void setBalance(int balanceValue) {
        presenter.saveBalanceToDb(CurrentDaysCount, CurrentYear, balanceValue);
    }

    public void loadFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_left)
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    public void loadFragmentWithoutAnimations(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .commit();

    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    public void setToolbarTitle(@StringRes int stringResId) {
        mainToolbar.setTitle(stringResId);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.drawer_menu_overview:
                presenter.onDrawerOptionOverViewClick();
                mainNavView.setCheckedItem(R.id.drawer_menu_overview);
                break;
            case R.id.drawer_menu_accounts:
                presenter.onDrawerOptionAccountsClick();
                mainNavView.setCheckedItem(R.id.drawer_menu_accounts);
                break;
            case R.id.drawer_menu_filter:
                startActivity(new Intent(this, TransactionFilterActivity.class));
                break;
            case R.id.drawer_menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        new Handler().postDelayed(() -> {
            drawerLayout.closeDrawer(GravityCompat.START);
        }, 250);
        return true;
    }


}
