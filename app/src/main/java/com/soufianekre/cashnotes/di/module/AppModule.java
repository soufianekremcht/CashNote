package com.soufianekre.cashnotes.di.module;


import android.app.Application;
import android.content.Context;

import com.soufianekre.cashnotes.MyApp;
import com.soufianekre.cashnotes.data.AppDataManager;
import com.soufianekre.cashnotes.data.DataManager;
import com.soufianekre.cashnotes.di.scope.ApplicationContext;
import com.soufianekre.cashnotes.di.scope.PreferenceInfo;
import com.soufianekre.cashnotes.helper.AppConst;

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
