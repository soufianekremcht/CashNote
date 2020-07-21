package com.example.highcash.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.highcash.MyApp;
import com.example.highcash.R;
import com.example.highcash.data.view_model.SharedViewModel;
import com.example.highcash.ui.accounts.AccountsFragment;
import com.example.highcash.ui.base.BaseActivity;
import com.example.highcash.ui.main.di.MainComponent;
import com.example.highcash.ui.overview.OverViewFragment;
import com.example.highcash.ui.settings.SettingsActivity;
import com.example.highcash.ui.transaction_filter.TransactionFilterActivity;
import com.example.highcash.ui.transactions.TransactionsFragment;
import com.example.highcash.helper.AppUtils;
import com.example.highcash.ui.transactions.search.SearchActivity;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.highcash.helper.AppConst.ACCOUNT_PARENT_ID;

public class MainActivity extends BaseActivity implements MainContract.View {
    public static final String FRAGMENT_TRANSACTION_TAG ="fragment_transaction";
    public static final String FRAGMENT_ACCOUNT_TAG ="fragment_account";
    private static final String FRAGMENT_OVERVIEW_TAG = "overViewFragment";


    public static final int REFRESH_TRANSACTION_LIST_CODE = 55;
    public static final int REFRESH_ACCOUNT_LIST_CODE = 44;
    public static final int RESULT_T = 533;
    public static final int RESULT_A = 511;

    public int CurrentDaysCount = AppUtils.getCurrentDaysCount();
    public int CurrentYear = AppUtils.getCurrentYear();

    MainComponent mainComponent;


    @BindView(R.id.main_drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_nav_view)
    NavigationView mainNavView;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;

    private ActionBarDrawerToggle drawerToggle;
    public SharedViewModel model;

    @Inject
    MainContract.Presenter<MainContract.View> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));
        mainComponent = ((MyApp) getApplicationContext()).getComponent().mainComponent().create();
        mainComponent.inject(this);

        getActivityComponent().inject(this);

        presenter.onAttach(this);
        model = new ViewModelProvider(this).get(SharedViewModel.class);
        setupUi();

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setBalanceForCurrentDay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                return true;
            case R.id.main_menu_search:
                Intent search  = new Intent(this, SearchActivity.class);
                startActivity(search);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_T) {
            model.setAccountId(data.getIntExtra(ACCOUNT_PARENT_ID, -1));
            showTransactionsFragment();
        } else if (resultCode == RESULT_A){
            showAccountFragment();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() instanceof TransactionsFragment)
            showAccountFragment();
        else
            super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    private void setupUi(){
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,mainToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerToggle.setDrawerSlideAnimationEnabled(true);
        drawerToggle.syncState();


        // change header color
        View header = mainNavView.getHeaderView(0);
        ImageView header_back_img = header.findViewById(R.id.nav_header_background_img);
        header_back_img.setBackgroundColor(AppUtils.getColor(this,R.color.colorPrimary));


        mainNavView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.drawer_menu_overview:
                    presenter.onDrawerOptionOverViewClick();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    mainNavView.setCheckedItem(R.id.drawer_menu_overview);
                    return true;
                case R.id.drawer_menu_accounts:
                    presenter.onDrawerOptionAccountsClick();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    mainNavView.setCheckedItem(R.id.drawer_menu_accounts);
                    return true;
                case R.id.drawer_menu_filter:
                    startActivity(new Intent(this, TransactionFilterActivity.class));
                    mainNavView.setCheckedItem(R.id.drawer_menu_filter);
                    return true;
                case R.id.drawer_menu_settings:
                    startActivity(new Intent(this,SettingsActivity.class));
                    return true;

            }
            return false;
        });

        showAccountFragment();
    }


    @Override
    public void showAccountFragment() {
        loadFragmentWithoutAnimations(AccountsFragment.newInstance(),FRAGMENT_ACCOUNT_TAG);
        setToolbarTitle(R.string.accounts);
    }

    @Override
    public void showTransactionsFragment() {
        loadFragment(TransactionsFragment.newInstance(),FRAGMENT_TRANSACTION_TAG);
    }

    @Override
    public void showOverViewFragment() {
        loadFragment(OverViewFragment.newInstance(),FRAGMENT_OVERVIEW_TAG);
    }

    @Override
    public void setBalance(int balanceValue) {
        presenter.saveBalanceToDb(CurrentDaysCount,CurrentYear,balanceValue);
    }

    public void loadFragment(Fragment fragment, String tag){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.fragment_container,fragment,tag)
                .commit();
    }

    public void loadFragmentWithoutAnimations(Fragment fragment,String tag){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,fragment,tag)
                .commit();

    }
    private Fragment getCurrentFragment(){
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }



    public void setToolbarTitle(String title){
        mainToolbar.setTitle(title);
    }
    public void setToolbarTitle(@StringRes int stringResId){
        mainToolbar.setTitle(stringResId);
    }


}
