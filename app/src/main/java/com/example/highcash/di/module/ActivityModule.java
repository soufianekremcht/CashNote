package com.example.highcash.di.module;


import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.highcash.di.scope.ActivityContext;
import com.example.highcash.di.scope.ActivityScope;
import com.example.highcash.helper.rx.AppSchedulerProvider;
import com.example.highcash.helper.rx.SchedulerProvider;
import com.example.highcash.ui.account_edit.AccountEditorContract;
import com.example.highcash.ui.account_edit.AccountEditorPresenter;
import com.example.highcash.ui.accounts.AccountsAdapter;
import com.example.highcash.ui.accounts.AccountsContract;
import com.example.highcash.ui.accounts.AccountsPresenter;
import com.example.highcash.ui.main.MainContract;
import com.example.highcash.ui.main.MainPresenter;
import com.example.highcash.ui.overview.OverViewContract;
import com.example.highcash.ui.overview.OverViewPresenter;
import com.example.highcash.ui.overview.adapters.RecentTransactionsAdapter;
import com.example.highcash.ui.overview.adapters.RecentAccountsAdapter;
import com.example.highcash.ui.settings.SettingsContract;
import com.example.highcash.ui.settings.SettingsPresenter;
import com.example.highcash.ui.settings.export.ExportDataContract;
import com.example.highcash.ui.settings.export.ExportDataPresenter;
import com.example.highcash.ui.splash.SplashScreenContract;
import com.example.highcash.ui.splash.SplashScreenPresenter;
import com.example.highcash.ui.transaction_edit.TransactionEditorContract;
import com.example.highcash.ui.transaction_edit.TransactionEditorPresenter;
import com.example.highcash.ui.transaction_filter.TransactionFilterContract;
import com.example.highcash.ui.transaction_filter.TransactionFilterPresenter;
import com.example.highcash.ui.transactions.TransactionsContract;
import com.example.highcash.ui.transactions.TransactionsAdapter;
import com.example.highcash.ui.transactions.TransactionsPresenter;
import com.example.highcash.ui.transactions.search.SearchContract;
import com.example.highcash.ui.transactions.search.SearchPresenter;

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
        return new AccountsAdapter(mActivity,new ArrayList<>(),null);
    }

    // Transactions
    @Provides
    TransactionsContract.Presenter<TransactionsContract.View> provideTransactionsPresenter(
            TransactionsPresenter<TransactionsContract.View> presenter){
        return presenter;
    }
    @Provides
    TransactionsAdapter provideTransactionsAdapter(){
        return new TransactionsAdapter(mActivity,new ArrayList<>());
    }

    // Over View
    @Provides
    OverViewContract.Presenter<OverViewContract.View> provideOverViewPresenter(
            OverViewPresenter<OverViewContract.View> presenter){
        return presenter;
    }
    @Provides
    RecentTransactionsAdapter provideRecentTransactionsAdapter(){
        return new RecentTransactionsAdapter(mActivity,new ArrayList<>());
    }
    @Provides
    RecentAccountsAdapter provideRecentUsedAccountsAdapter(){
        return new RecentAccountsAdapter(mActivity, new ArrayList<>());
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
        return new LinearLayoutManager(mActivity, RecyclerView.VERTICAL,false);
    }


}
