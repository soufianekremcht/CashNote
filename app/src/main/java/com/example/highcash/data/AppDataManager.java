package com.example.highcash.data;

import android.content.Context;

import com.example.highcash.data.db.CashRoomDb;
import com.example.highcash.di.scope.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppDataManager implements DataManager {
    private final Context mContext;


    @Inject
    AppDataManager(@ApplicationContext Context mContext) {
        this.mContext = mContext;
    }



    public CashRoomDb getRoomDb(){
        return CashRoomDb.getInstance(mContext);
    }
}
