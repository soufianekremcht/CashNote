package com.example.highcash.di.module;


import android.app.Application;
import android.content.Context;

import com.example.highcash.MyApp;
import com.example.highcash.data.AppDataManager;
import com.example.highcash.data.DataManager;
import com.example.highcash.di.scope.ApplicationContext;
import com.example.highcash.di.scope.PreferenceInfo;
import com.example.highcash.helper.AppConst;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private MyApp mApplication;

    public AppModule(MyApp application) {
        this.mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConst.PREF_NAME;
    }


    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager dataManager){
        return dataManager;
    }


}
