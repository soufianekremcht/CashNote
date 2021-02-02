package com.soufianekre.cashnote.di.component;


import com.soufianekre.cashnote.di.scope.ActivityScope;
import com.soufianekre.cashnote.di.module.ActivityModule;
import com.soufianekre.cashnote.ui.accounts.AccountsFragment;
import com.soufianekre.cashnote.ui.account_edit.AccountEditorActivity;
import com.soufianekre.cashnote.ui.transactions.TransactionsActivity;
import com.soufianekre.cashnote.ui.transactions.search.SearchActivity;
import com.soufianekre.cashnote.ui.settings.export.ExportDataDialogFragment;
import com.soufianekre.cashnote.ui.transaction_edit.TransactionEditorActivity;
import com.soufianekre.cashnote.ui.main.MainActivity;
import com.soufianekre.cashnote.ui.overview.OverViewFragment;
import com.soufianekre.cashnote.ui.settings.SettingsActivity;
import com.soufianekre.cashnote.ui.settings.SettingsFragment;
import com.soufianekre.cashnote.ui.splash.SplashScreenActivity;
import com.soufianekre.cashnote.ui.transaction_filter.TransactionFilterActivity;
import com.soufianekre.cashnote.ui.transactions.show_transaction.ShowTransactionFragment;

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

    void inject(ShowTransactionFragment showTransactionFragment);

}
