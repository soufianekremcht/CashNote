package com.soufianekre.cashnote.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.soufianekre.cashnote.data.db.converters.CashCategoryConverter;
import com.soufianekre.cashnote.data.db.dao.BalanceHistoryDao;
import com.soufianekre.cashnote.data.db.dao.CashAccountDao;
import com.soufianekre.cashnote.data.db.dao.CashTransactionDao;
import com.soufianekre.cashnote.data.db.model.BalanceHistory;
import com.soufianekre.cashnote.data.db.model.CashAccount;
import com.soufianekre.cashnote.data.db.model.CashTransaction;


@Database(entities = {CashAccount.class,CashTransaction.class, BalanceHistory.class},exportSchema = false,version =11)
@TypeConverters({CashCategoryConverter.class})

public abstract class CashRoomDb extends RoomDatabase {
    private static final String DB_NAME = "cash_note.db";
    private static CashRoomDb instance ;

    public static synchronized CashRoomDb getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context, CashRoomDb.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract CashAccountDao cashAccountDao();
    public abstract BalanceHistoryDao balanceHistoryDao();
    public abstract CashTransactionDao cashTransactionDao();

    private static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.

        }
    };

}
