package com.example.highcash.di.component;


import android.app.Application;
import android.content.Context;

import com.example.highcash.MyApp;
import com.example.highcash.data.DataManager;
import com.example.highcash.di.module.ActivityModule;
import com.example.highcash.di.scope.ApplicationContext;
import com.example.highcash.di.module.AppModule;
import com.example.highcash.ui.main.di.MainComponent;
import com.example.highcash.ui.main.di.MainModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, MainModule.class})
public interface AppComponent {

    void inject(MyApp myApp);

    //MainComponent.Factory mainComponent();

    @ApplicationContext
    Context context();

    Application application();

    DataManager getDataManager();
}
