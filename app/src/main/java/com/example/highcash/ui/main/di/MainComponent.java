package com.example.highcash.ui.main.di;

import com.example.highcash.di.scope.ActivityScope;
import com.example.highcash.ui.main.MainActivity;

import dagger.Subcomponent;


@Subcomponent
public interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        MainComponent create();
    }
    void inject(MainActivity mainActivity);

}
