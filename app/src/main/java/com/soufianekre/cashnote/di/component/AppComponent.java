package com.soufianekre.cashnote.di.component;


import android.app.Application;
import android.content.Context;

import com.soufianekre.cashnote.MyApp;
import com.soufianekre.cashnote.data.DataManager;
import com.soufianekre.cashnote.di.module.AppModule;
import com.soufianekre.cashnote.di.scope.ApplicationContext;

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
