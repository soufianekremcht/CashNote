package com.soufianekre.cashnotes.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.soufianekre.cashnotes.MyApp;
import com.soufianekre.cashnotes.R;
import com.soufianekre.cashnotes.data.app_preference.PrefsConst;
import com.soufianekre.cashnotes.helper.AppUtils;
import com.soufianekre.cashnotes.ui.account_edit.AccountEditorActivity;
import com.soufianekre.cashnotes.ui.accounts.AccountsFragment;
import com.soufianekre.cashnotes.ui.base.BaseActivity;
import com.soufianekre.cashnotes.ui.google_sign_in.FirebaseSignInActivity;
import com.soufianekre.cashnotes.ui.overview.OverViewFragment;
import com.soufianekre.cashnotes.ui.settings.SettingsActivity;
import com.soufianekre.cashnotes.ui.transaction_filter.TransactionFilterActivity;
import com.soufianekre.cashnotes.ui.transactions.search.SearchActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.infinum.goldfinger.Goldfinger;
import co.infinum.goldfinger.rx.RxGoldfinger;
import io.reactivex.observers.DisposableObserver;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends BaseActivity implements MainContract.View {
    public static final String FRAGMENT_ACCOUNT_TAG = "fragment_account";
    public static final int REFRESH_TRANSACTION_LIST_CODE = 55;
    public static final int REFRESH_ACCOUNT_LIST_CODE = 44;
    public static final int RESULT_T = 533;
    public static final int RESULT_A = 511;
    public static final String SELECTED_ACCOUNT = "account_parent";
    private static final String FRAGMENT_OVERVIEW_TAG = "overviewFragment";
    public int CurrentDaysCount = AppUtils.getCurrentDaysCount();
    public int CurrentYear = AppUtils.getCurrentYear();


    @BindView(R.id.main_drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_nav_view)
    NavigationView mainNavView;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.add_account_fab)
    FloatingActionButton newAccountFab;
    @Inject
    MainContract.Presenter<MainContract.View> presenter;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        presenter.onAttach(this);

        // Finger Print authentication
        if (MyApp.AppPref().getBoolean(PrefsConst.PREF_SET_FINGER_PRINT)){
            checkFingerPrint();
        }else{
            setupUi();
        }



        if (savedInstanceState == null)
            showAccountFragment();

    }
    private void setupUi() {
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //mainToolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerToggle.setDrawerSlideAnimationEnabled(true);
        drawerToggle.syncState();
        mainNavView.setNavigationItemSelectedListener(this);
        SwitchCompat drawerToggleThemeSwitch =(SwitchCompat) mainNavView.getMenu().findItem(R.id.drawer_menu_toggle_night_mode)
                .getActionView();
        drawerToggleThemeSwitch.setOnClickListener(v->{
            switchTheme();
        });



        newAccountFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AccountEditorActivity.class);
            startActivityForResult(intent, REFRESH_ACCOUNT_LIST_CODE);
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
            if (newAccountFab != null)
                newAccountFab.hide();

        SwitchCompat drawerToggleThemeSwitch =(SwitchCompat) mainNavView.getMenu().findItem(R.id.drawer_menu_toggle_night_mode)
                .getActionView();
        drawerToggleThemeSwitch.setChecked(MyApp.AppPref().getBoolean(PrefsConst.ACTIVATE_DARK_MODE));
        // update balance database
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
        getMainToolbar().setTitle(R.string.accounts);
    }


    @Override
    public void showOverViewFragment() {
        loadFragment(OverViewFragment.newInstance(), FRAGMENT_OVERVIEW_TAG);
    }

    @Override
    public void setBalance(int balanceValue) {
        presenter.saveBalanceToDb(CurrentDaysCount, CurrentYear, balanceValue);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        new Handler().postDelayed(() -> {
            switch (item.getItemId()) {
                case R.id.drawer_menu_overview:
                    presenter.onDrawerOptionOverViewClick();
                    mainNavView.setCheckedItem(R.id.drawer_menu_overview);
                    break;
                case R.id.drawer_menu_accounts:
                    presenter.onDrawerOptionAccountsClick();
                    mainNavView.setCheckedItem(R.id.drawer_menu_accounts);
                    break;
                case R.id.drawer_menu_user:
                    Intent intent = new Intent(this, FirebaseSignInActivity.class);
                    startActivity(intent);
                    break;
                case R.id.drawer_menu_filter:
                    startActivity(new Intent(this, TransactionFilterActivity.class));

                    break;
                case R.id.drawer_menu_settings:
                    startActivity(new Intent(this, SettingsActivity.class));
                    break;

            }


        }, 250);
        return true;
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


    private void switchTheme() {
        if (MyApp.AppPref().getBoolean(PrefsConst.ACTIVATE_DARK_MODE)) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
            MyApp.AppPref().set(PrefsConst.ACTIVATE_DARK_MODE, false);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
            MyApp.AppPref().set(PrefsConst.ACTIVATE_DARK_MODE, true);
        }
    }


    private void checkFingerPrint() {
        RxGoldfinger goldfinger = new RxGoldfinger.Builder(this).build();

        Goldfinger.PromptParams params = new Goldfinger.PromptParams.Builder(this)
                .title("Checking For FingerPrint ..")
                .negativeButtonText(R.string.cancel)
                .description("Check your fingerprint to use the app..")
                .build();

        if (goldfinger.hasFingerprintHardware()) {
            if (goldfinger.canAuthenticate()) {
                goldfinger.authenticate(params).subscribe(new DisposableObserver<Goldfinger.Result>() {

                    @Override
                    public void onComplete() {
                        /* Fingerprint authentication is finished */

                    }

                    @Override
                    public void onError(Throwable e) {
                        /* Critical error happened */
                        showError("You are not verified ,Check Again! ");
                        finish();
                    }

                    @Override
                    public void onNext(Goldfinger.Result result) {
                        /* Result received */
                        showMessage("You have been verified Successfully! ");
                        setupUi();
                    }
                });
            }
        }else{
            showError("This Device does't support FingerPrint Hardware");
            setupUi();
        }
    }


    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    public FloatingActionButton getFab() {
        return newAccountFab;
    }

    public Toolbar getMainToolbar() {
        return mainToolbar;
    }

}
