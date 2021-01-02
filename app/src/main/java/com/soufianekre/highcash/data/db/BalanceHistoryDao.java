package com.soufianekre.highcash.data.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.soufianekre.highcash.data.db.model.BalanceHistory;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
@Dao
public interface BalanceHistoryDao {


    //TODO : NEED TO FIX THIS && ADD CLEAN DB AFTER 30 DAYS
    @Query("Select * from balance_history")
    Flowable<List<BalanceHistory>> getBalanceHistoryList();


    @Query("Select * from balance_history Where days =:days And year=:year  LIMIT 1")
    Flowable<BalanceHistory> getBalanceHistory(int days, int year);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addBalance(BalanceHistory balance);

    @Update
    Completable updateBalance(BalanceHistory balance);

    @Delete
    Completable deleteBalance(BalanceHistory balance);


}
