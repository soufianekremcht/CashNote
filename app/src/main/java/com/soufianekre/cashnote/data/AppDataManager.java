package com.soufianekre.cashnote.data;

import android.content.Context;

import com.soufianekre.cashnote.data.db.CashRoomDb;
import com.soufianekre.cashnote.di.scope.ApplicationContext;

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
