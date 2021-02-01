package com.soufianekre.cashnote.ui.app_base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;

import com.soufianekre.cashnote.di.component.ActivityComponent;

import butterknife.Unbinder;

abstract public class BasePreferenceFragment extends PreferenceFragmentCompat implements BaseContract.MvpView {


    private BaseActivity mActivity;
    private Unbinder mUnBinder;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
        }
    }

    @Override
    public void onDetach() {
        if (mActivity != null) mActivity = null;
        super.onDetach();
    }



    @Override
    public void showError(String message) {
        if (mActivity != null) {
            mActivity.showError(message);
        }
    }

    @Override
    public void showMessage(String message) {
        if (mActivity != null) {
            mActivity.showMessage(message);
        }
    }


    @Override
    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    @Override
    public void onDestroy() {
        hideKeyboard();
        if (mUnBinder != null)
            mUnBinder.unbind();
        super.onDestroy();
    }

    public ActivityComponent getActivityComponent() {
        if (mActivity != null) {
            return mActivity.getActivityComponent();
        }
        return null;
    }
    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }
}
