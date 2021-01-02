package com.soufianekre.highcash.di.component;


import android.app.Application;
import android.content.Context;

import com.soufianekre.highcash.MyApp;
import com.soufianekre.highcash.data.DataManager;
import com.soufianekre.highcash.di.module.AppModule;
import com.soufianekre.highcash.di.scope.ApplicationContext;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(MyApp myApp);

    //MainComponent.Factory mainComponent();

    @ApplicationContext
    Context context();

    Application application();

    DataManager getDataManager();
}
