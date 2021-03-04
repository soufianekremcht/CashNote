package com.soufianekre.cashnotes.ui.base;



import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.soufianekre.cashnotes.MyApp;
import com.soufianekre.cashnotes.di.component.ActivityComponent;
import com.soufianekre.cashnotes.di.component.DaggerActivityComponent;
import com.soufianekre.cashnotes.di.module.ActivityModule;
import com.google.android.material.snackbar.Snackbar;

import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

public abstract class BaseActivity extends AppCompatActivity implements BaseContract.MvpView {

    private Unbinder mUnBinder;
    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .appComponent(((MyApp) getApplication()).getComponent())
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        hideKeyboard();
        super.onStop();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void showError(String message) {
        Toasty.error(this,message,Toasty.LENGTH_SHORT).show();

    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toasty.info(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toasty.info(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onDestroy() {
        hideKeyboard();
        if (mUnBinder != null) mUnBinder.unbind();
        super.onDestroy();
    }

    public ActivityComponent getActivityComponent(){
        return mActivityComponent;
    }


    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        snackbar.show();
    }

}