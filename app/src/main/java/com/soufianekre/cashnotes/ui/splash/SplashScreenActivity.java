package com.soufianekre.cashnotes.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import com.soufianekre.cashnotes.R;
import com.soufianekre.cashnotes.ui.base.BaseActivity;
import com.soufianekre.cashnotes.ui.main.MainActivity;

import javax.inject.Inject;

public class SplashScreenActivity extends BaseActivity implements SplashScreenContract.View {

    @Inject
    SplashScreenContract.Presenter<SplashScreenContract.View> presenter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getActivityComponent().inject(this);
        presenter.onAttach(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        presenter.goToMainActivity();

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }


}
