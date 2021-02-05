package com.soufianekre.cashnotes.di.component;


import android.app.Application;
import android.content.Context;

import com.soufianekre.cashnotes.MyApp;
import com.soufianekre.cashnotes.data.DataManager;
import com.soufianekre.cashnotes.di.module.AppModule;
import com.soufianekre.cashnotes.di.scope.ApplicationContext;

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
