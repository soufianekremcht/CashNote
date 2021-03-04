package com.soufianekre.cashnotes;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.soufianekre.cashnotes.data.DataManager;
import com.soufianekre.cashnotes.data.app_preference.AppPreferencesHelper;
import com.soufianekre.cashnotes.data.app_preference.PreferencesHelper;
import com.soufianekre.cashnotes.di.component.AppComponent;
import com.soufianekre.cashnotes.di.component.DaggerAppComponent;
import com.soufianekre.cashnotes.di.module.AppModule;
import com.soufianekre.cashnotes.helper.AppConst;
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

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);

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
