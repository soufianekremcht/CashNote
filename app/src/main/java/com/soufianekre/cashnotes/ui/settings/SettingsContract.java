package com.soufianekre.cashnotes.ui.settings;

import androidx.preference.Preference;

import com.soufianekre.cashnotes.ui.base.BaseContract;

public interface SettingsContract {
    interface Presenter<V extends View> extends BaseContract.MvpPresenter<V> {

    }

    interface View extends BaseContract.MvpView, Preference.OnPreferenceClickListener,Preference.OnPreferenceChangeListener {
    }
}
