package com.soufianekre.highcash.di.component;


import com.soufianekre.highcash.di.scope.ActivityScope;
import com.soufianekre.highcash.di.module.ActivityModule;
import com.soufianekre.highcash.ui.accounts.AccountsFragment;
import com.soufianekre.highcash.ui.account_edit.AccountEditorActivity;
import com.soufianekre.highcash.ui.transactions.TransactionsActivity;
import com.soufianekre.highcash.ui.transactions.search.SearchActivity;
import com.soufianekre.highcash.ui.settings.export.ExportDataDialogFragment;
import com.soufianekre.highcash.ui.transaction_edit.TransactionEditorActivity;
import com.soufianekre.highcash.ui.main.MainActivity;
import com.soufianekre.highcash.ui.overview.OverViewFragment;
import com.soufianekre.highcash.ui.settings.SettingsActivity;
import com.soufianekre.highcash.ui.settings.SettingsFragment;
import com.soufianekre.highcash.ui.splash.SplashScreenActivity;
import com.soufianekre.highcash.ui.transaction_filter.TransactionFilterActivity;

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
