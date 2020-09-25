package com.example.highcash.di.component;


import com.example.highcash.di.scope.ActivityScope;
import com.example.highcash.di.module.ActivityModule;
import com.example.highcash.ui.accounts.AccountsFragment;
import com.example.highcash.ui.account_edit.AccountEditorActivity;
import com.example.highcash.ui.transactions.TransactionsActivity;
import com.example.highcash.ui.transactions.search.SearchActivity;
import com.example.highcash.ui.settings.export.ExportDataDialogFragment;
import com.example.highcash.ui.transaction_edit.TransactionEditorActivity;
import com.example.highcash.ui.main.MainActivity;
import com.example.highcash.ui.overview.OverViewFragment;
import com.example.highcash.ui.settings.SettingsActivity;
import com.example.highcash.ui.settings.SettingsFragment;
import com.example.highcash.ui.splash.SplashScreenActivity;
import com.example.highcash.ui.transaction_filter.TransactionFilterActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class,modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(AccountsFragment accountsFragment);

    void inject(TransactionsActivity transactionActivity);

    void inject(OverViewFragment overviewFragment);

    void inject(SplashScreenActivity splashscreenActivity);

    void inject(AccountEditorActivity accountEditorActivity);

    void inject(TransactionEditorActivity transactionEditorActivity);

    void inject(TransactionFilterActivity transactionFilterActivity);


    void inject(SettingsActivity settingsActivity);
    void inject(SettingsFragment settingsFragment);
    void inject(ExportDataDialogFragment exportDataDialogFragment);

    void inject(SearchActivity searchActivity);

}
