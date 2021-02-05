package com.soufianekre.cashnotes.di.component;


import com.soufianekre.cashnotes.di.scope.ActivityScope;
import com.soufianekre.cashnotes.di.module.ActivityModule;
import com.soufianekre.cashnotes.ui.accounts.AccountsFragment;
import com.soufianekre.cashnotes.ui.account_edit.AccountEditorActivity;
import com.soufianekre.cashnotes.ui.transactions.TransactionsActivity;
import com.soufianekre.cashnotes.ui.transactions.search.SearchActivity;
import com.soufianekre.cashnotes.ui.settings.export.ExportDataDialogFragment;
import com.soufianekre.cashnotes.ui.transaction_edit.TransactionEditorActivity;
import com.soufianekre.cashnotes.ui.main.MainActivity;
import com.soufianekre.cashnotes.ui.overview.OverViewFragment;
import com.soufianekre.cashnotes.ui.settings.SettingsActivity;
import com.soufianekre.cashnotes.ui.settings.SettingsFragment;
import com.soufianekre.cashnotes.ui.splash.SplashScreenActivity;
import com.soufianekre.cashnotes.ui.transaction_filter.TransactionFilterActivity;
import com.soufianekre.cashnotes.ui.transactions.show_transaction.ShowTransactionFragment;

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
