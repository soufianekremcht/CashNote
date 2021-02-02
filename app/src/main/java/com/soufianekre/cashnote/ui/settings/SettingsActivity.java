package com.soufianekre.cashnote.ui.settings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.soufianekre.cashnote.R;
import com.soufianekre.cashnote.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.settings_toolbar)
    Toolbar settingsToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        setupUi();
        if (savedInstanceState == null)
            show_settings_fragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void setupUi() {
        setSupportActionBar(settingsToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        settingsToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }


    void show_settings_fragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_fragment_container,SettingsFragment.newInstance())
                .commit();
    }
}
