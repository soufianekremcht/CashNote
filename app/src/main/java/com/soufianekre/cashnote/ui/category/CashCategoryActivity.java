package com.soufianekre.cashnote.ui.category;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.soufianekre.cashnote.R;
import com.soufianekre.cashnote.ui.base.BaseActivity;

import butterknife.ButterKnife;

class CashCategoryActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
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
