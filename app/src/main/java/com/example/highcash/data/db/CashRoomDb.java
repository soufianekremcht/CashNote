package com.example.highcash.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.highcash.data.db.converters.AccountCategoryDBConverter;
import com.example.highcash.data.db.converters.CashTransactionDBConverter;
import com.example.highcash.data.db.model.BalanceHistory;
import com.example.highcash.data.db.model.CashAccount;


@Database(entities = {CashAccount.class, BalanceHistory.class},exportSchema = false,version =1)
@TypeConverters({CashTransactionDBConverter.class, AccountCategoryDBConverter.class})

public abstract class CashRoomDb extends RoomDatabase {
    private static final String DB_NAME = "high_cash.db";
    private static CashRoomDb instance ;

    public static synchronized CashRoomDb getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context, CashRoomDb.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract CashAccountDao accountDao();
    public abstract BalanceHistoryDao balanceHistoryDao();

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.

        }
    };

}
