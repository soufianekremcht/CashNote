package com.example.highcash.di.component;


import android.app.Application;
import android.content.Context;

import com.example.highcash.MyApp;
import com.example.highcash.data.DataManager;
import com.example.highcash.di.module.AppModule;
import com.example.highcash.di.scope.ApplicationContext;

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
