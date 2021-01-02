package com.soufianekre.highcash.di.module;


import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.soufianekre.highcash.di.scope.ActivityContext;
import com.soufianekre.highcash.di.scope.ActivityScope;
import com.soufianekre.highcash.helper.rx.AppSchedulerProvider;
import com.soufianekre.highcash.helper.rx.SchedulerProvider;
import com.soufianekre.highcash.ui.account_edit.AccountEditorContract;
import com.soufianekre.highcash.ui.account_edit.AccountEditorPresenter;
import com.soufianekre.highcash.ui.accounts.AccountsAdapter;
import com.soufianekre.highcash.ui.accounts.AccountsContract;
import com.soufianekre.highcash.ui.accounts.AccountsPresenter;
import com.soufianekre.highcash.ui.main.MainContract;
import com.soufianekre.highcash.ui.main.MainPresenter;
import com.soufianekre.highcash.ui.overview.OverViewContract;
import com.soufianekre.highcash.ui.overview.OverViewPresenter;
import com.soufianekre.highcash.ui.overview.adapters.RecentTransactionsAdapter;
import com.soufianekre.highcash.ui.overview.adapters.RecentAccountsAdapter;
import com.soufianekre.highcash.ui.settings.SettingsContract;
import com.soufianekre.highcash.ui.settings.SettingsPresenter;
import com.soufianekre.highcash.ui.settings.export.ExportDataContract;
import com.soufianekre.highcash.ui.settings.export.ExportDataPresenter;
import com.soufianekre.highcash.ui.splash.SplashScreenContract;
import com.soufianekre.highcash.ui.splash.SplashScreenPresenter;
import com.soufianekre.highcash.ui.transaction_edit.TransactionEditorContract;
import com.soufianekre.highcash.ui.transaction_edit.TransactionEditorPresenter;
import com.soufianekre.highcash.ui.transaction_filter.TransactionFilterContract;
import com.soufianekre.highcash.ui.transaction_filter.TransactionFilterPresenter;
import com.soufianekre.highcash.ui.transactions.TransactionsContract;
import com.soufianekre.highcash.ui.transactions.TransactionsAdapter;
import com.soufianekre.highcash.ui.transactions.TransactionsPresenter;
import com.soufianekre.highcash.ui.transactions.search.SearchContract;
import com.soufianekre.highcash.ui.transactions.search.SearchPresenter;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;



@Module
public class ActivityModule {
    private AppCompatActivity mActivity;

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
