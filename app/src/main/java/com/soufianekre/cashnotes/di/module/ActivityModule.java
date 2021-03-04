package com.soufianekre.cashnotes.di.module;


import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.soufianekre.cashnotes.di.scope.ActivityContext;
import com.soufianekre.cashnotes.di.scope.ActivityScope;
import com.soufianekre.cashnotes.helper.rx.AppSchedulerProvider;
import com.soufianekre.cashnotes.helper.rx.SchedulerProvider;
import com.soufianekre.cashnotes.ui.account_edit.AccountEditorContract;
import com.soufianekre.cashnotes.ui.account_edit.AccountEditorPresenter;
import com.soufianekre.cashnotes.ui.accounts.AccountsAdapter;
import com.soufianekre.cashnotes.ui.accounts.AccountsContract;
import com.soufianekre.cashnotes.ui.accounts.AccountsPresenter;
import com.soufianekre.cashnotes.ui.main.MainContract;
import com.soufianekre.cashnotes.ui.main.MainPresenter;
import com.soufianekre.cashnotes.ui.overview.OverViewContract;
import com.soufianekre.cashnotes.ui.overview.OverViewPresenter;
import com.soufianekre.cashnotes.ui.overview.adapters.RecentTransactionsAdapter;
import com.soufianekre.cashnotes.ui.overview.adapters.RecentAccountsAdapter;
import com.soufianekre.cashnotes.ui.settings.SettingsContract;
import com.soufianekre.cashnotes.ui.settings.SettingsPresenter;
import com.soufianekre.cashnotes.ui.settings.export.ExportDataContract;
import com.soufianekre.cashnotes.ui.settings.export.ExportDataPresenter;
import com.soufianekre.cashnotes.ui.splash.SplashScreenContract;
import com.soufianekre.cashnotes.ui.splash.SplashScreenPresenter;
import com.soufianekre.cashnotes.ui.transaction_edit.TransactionEditorContract;
import com.soufianekre.cashnotes.ui.transaction_edit.TransactionEditorPresenter;
import com.soufianekre.cashnotes.ui.transaction_filter.TransactionFilterContract;
import com.soufianekre.cashnotes.ui.transaction_filter.TransactionFilterPresenter;
import com.soufianekre.cashnotes.ui.transactions.TransactionsContract;
import com.soufianekre.cashnotes.ui.transactions.TransactionsAdapter;
import com.soufianekre.cashnotes.ui.transactions.TransactionsPresenter;
import com.soufianekre.cashnotes.ui.transactions.search.SearchContract;
import com.soufianekre.cashnotes.ui.transactions.search.SearchPresenter;
import com.soufianekre.cashnotes.ui.transactions.show_transaction.ShowTransactionContract;
import com.soufianekre.cashnotes.ui.transactions.show_transaction.ShowTransactionPresenter;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;



@Module
public class ActivityModule {
    private final AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    // Common
    @Provides
    @ActivityContext
    Context provideContext(){
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }


    // Splash Screen
    @Provides
    @ActivityScope
    SplashScreenContract.Presenter<SplashScreenContract.View> provideSplashScreenPresenter(
            SplashScreenPresenter<SplashScreenContract.View> presenter){
        return presenter;
    }

    // Main

    @Provides
    @ActivityScope
    MainContract.Presenter<MainContract.View> provideMainPresenter(
            MainPresenter<MainContract.View> presenter){
        return presenter;
    }

    // Account
    @Provides
    AccountsContract.Presenter<AccountsContract.View> provideAccountsPresenter(
            AccountsPresenter<AccountsContract.View> presenter){
        return presenter;
    }
    @Provides
    AccountsAdapter provideAccountsAdapter(){
        return new AccountsAdapter(provideContext(),new ArrayList<>(),null);
    }

    // Transactions
    @Provides
    TransactionsContract.Presenter<TransactionsContract.View> provideTransactionsPresenter(
            TransactionsPresenter<TransactionsContract.View> presenter){
        return presenter;
    }
    @Provides
    TransactionsAdapter provideTransactionsAdapter(){
        return new TransactionsAdapter(provideContext(),new ArrayList<>());
    }

    // Over View
    @Provides
    OverViewContract.Presenter<OverViewContract.View> provideOverViewPresenter(
            OverViewPresenter<OverViewContract.View> presenter){
        return presenter;
    }

    @Provides
    RecentTransactionsAdapter provideRecentTransactionsAdapter(){
        return new RecentTransactionsAdapter(provideContext(),new ArrayList<>());
    }
    @Provides
    RecentAccountsAdapter provideRecentUsedAccountsAdapter(){
        return new RecentAccountsAdapter(provideContext(), new ArrayList<>());
    }

    // Account Editor
    @Provides
    AccountEditorContract.Presenter<AccountEditorContract.View> provideAccountEditorPresenter(
            AccountEditorPresenter<AccountEditorContract.View> presenter){
        return presenter;
    }

    // Transaction Editor
    @Provides
    TransactionEditorContract.Presenter<TransactionEditorContract.View> provideTransactionEditorPresenter(
            TransactionEditorPresenter<TransactionEditorContract.View> presenter){
        return presenter;
    }

    // Transaction Filter
    @Provides
    TransactionFilterContract.Presenter<TransactionFilterContract.View> provideTransactionFilterPresenter(
            TransactionFilterPresenter<TransactionFilterContract.View> presenter){
        return presenter;

    }
    // Show Transaction

    @Provides
    ShowTransactionContract.Presenter<ShowTransactionContract.View> provideShowTransactionPresenter(
            ShowTransactionPresenter<ShowTransactionContract.View> presenter){
        return presenter;

    }

    // Settings

    @Provides
    SettingsContract.Presenter<SettingsContract.View> provideSettingsPresenter(
            SettingsPresenter<SettingsContract.View> presenter){
        return presenter;
    }

    @Provides
    ExportDataContract.Presenter<ExportDataContract.View> provideExportDataPresenter(
            ExportDataPresenter<ExportDataContract.View> presenter){
        return presenter;
    }

    //Search
    @Provides
    SearchContract.Presenter<SearchContract.View> provideSearchPresenter(
            SearchPresenter<SearchContract.View> presenterImp) {
        return presenterImp;
    }






    // Common

    @Provides
    LinearLayoutManager provideLinearLayoutManager(){
        return new LinearLayoutManager(provideActivity());
    }


}
