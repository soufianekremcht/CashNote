package com.soufianekre.highcash.ui.category_add;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.soufianekre.highcash.R;
import com.soufianekre.highcash.ui.app_base.BaseActivity;

import butterknife.ButterKnife;

class AddCashCategoryActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        ButterKnife.bind(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




}