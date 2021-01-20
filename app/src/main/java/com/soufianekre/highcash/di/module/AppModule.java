package com.soufianekre.highcash.di.module;


import android.app.Application;
import android.content.Context;

import com.soufianekre.highcash.MyApp;
import com.soufianekre.highcash.data.AppDataManager;
import com.soufianekre.highcash.data.DataManager;
import com.soufianekre.highcash.di.scope.ApplicationContext;
import com.soufianekre.highcash.di.scope.PreferenceInfo;
import com.soufianekre.highcash.helper.AppConst;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final MyApp mApplication;

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
