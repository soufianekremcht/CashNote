package com.soufianekre.cashnote.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.soufianekre.cashnote.data.db.converters.AccountCategoryDBConverter;
import com.soufianekre.cashnote.data.db.converters.CashTransactionDBConverter;
import com.soufianekre.cashnote.data.db.model.BalanceHistory;
import com.soufianekre.cashnote.data.db.model.CashAccount;


@Database(entities = {CashAccount.class, BalanceHistory.class},exportSchema = false,version =10)
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

    private static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.

        }
    };

}
