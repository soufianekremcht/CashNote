package com.soufianekre.cashnote.di.module;


import android.app.Application;
import android.content.Context;

import com.soufianekre.cashnote.MyApp;
import com.soufianekre.cashnote.data.AppDataManager;
import com.soufianekre.cashnote.data.DataManager;
import com.soufianekre.cashnote.di.scope.ApplicationContext;
import com.soufianekre.cashnote.di.scope.PreferenceInfo;
import com.soufianekre.cashnote.helper.AppConst;

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
