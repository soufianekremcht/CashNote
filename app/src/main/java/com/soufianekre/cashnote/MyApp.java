package com.soufianekre.cashnote;

import android.app.Application;
import android.os.Build;

import com.soufianekre.cashnote.data.DataManager;
import com.soufianekre.cashnote.data.app_preference.AppPreferencesHelper;
import com.soufianekre.cashnote.data.app_preference.PreferencesHelper;
import com.soufianekre.cashnote.di.component.AppComponent;
import com.soufianekre.cashnote.di.component.DaggerAppComponent;
import com.soufianekre.cashnote.di.module.AppModule;
import com.soufianekre.cashnote.helper.AppConst;
import com.github.mikephil.charting.utils.Utils;

import javax.inject.Inject;

import timber.log.Timber;


public class MyApp extends Application {

    @Inject
    DataManager mDataManager;

    private AppComponent mApplicationComponent;

    private static PreferencesHelper appPrefHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this)).build();
        mApplicationComponent.inject(this);
        Utils.init(this);
        appPrefHelper = new AppPreferencesHelper(this, AppConst.PREF_NAME);
        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());

    }
    public AppComponent getComponent() {
        return mApplicationComponent;
    }


    public void setComponent (AppComponent applicationComponent){
        mApplicationComponent = applicationComponent;
    }


    public static PreferencesHelper AppPref(){
        return appPrefHelper;
    }
}
