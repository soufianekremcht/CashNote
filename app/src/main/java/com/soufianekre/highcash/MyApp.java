package com.soufianekre.highcash;

import android.app.Application;

import com.soufianekre.highcash.data.DataManager;
import com.soufianekre.highcash.data.app_preference.AppPreferencesHelper;
import com.soufianekre.highcash.data.app_preference.PreferencesHelper;
import com.soufianekre.highcash.di.component.AppComponent;
import com.soufianekre.highcash.di.component.DaggerAppComponent;
import com.soufianekre.highcash.di.module.AppModule;
import com.soufianekre.highcash.helper.AppConst;
import com.github.mikephil.charting.utils.Utils;

import javax.inject.Inject;


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
