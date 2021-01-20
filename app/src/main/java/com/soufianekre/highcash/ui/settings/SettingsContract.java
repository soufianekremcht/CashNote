package com.soufianekre.highcash.ui.settings;

import androidx.preference.Preference;

import com.soufianekre.highcash.ui.app_base.BaseContract;

public interface SettingsContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {

    }

    interface View extends BaseContract.MvpView, Preference.OnPreferenceClickListener,Preference.OnPreferenceChangeListener {
    }
}
