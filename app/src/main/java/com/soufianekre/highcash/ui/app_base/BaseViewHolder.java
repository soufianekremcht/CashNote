package com.soufianekre.highcash.ui.app_base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    private int mCurrentPosition;
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    public void onBind(int currentPosition){
        mCurrentPosition = currentPosition;
    }

    public int getmCurrentPosition() {
        return mCurrentPosition;
    }
}
