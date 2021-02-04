package com.soufianekre.cashnote.ui.settings.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import com.soufianekre.cashnote.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.settings_toolbar)
    Toolbar settingsToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setupUi();
        loadFragment(new AboutFragment());


    }

    private void setupUi(){
        settingsToolbar.setTitle(R.string.about);
        setSupportActionBar(settingsToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        settingsToolbar.setNavigationOnClickListener(v -> onBackPressed());

    }

    void loadFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings_container,fragment)
                .commit();
    }

    public Toolbar getSettingsToolbar(){
        return settingsToolbar;
    }


}
