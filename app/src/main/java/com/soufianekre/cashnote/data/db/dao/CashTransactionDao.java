package com.soufianekre.cashnote.data.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.soufianekre.cashnote.data.db.model.CashTransaction;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface CashTransactionDao {

    @Insert
    Completable insert(CashTransaction transaction);

    @Update
    Completable update(CashTransaction transaction);

    @Delete
    Completable delete(CashTransaction transaction);

    @Query("SELECT * FROM cash_transaction ")
    Flowable<List<CashTransaction>> getAllTransactions();

    @Query("SELECT * FROM cash_transaction WHERE account_id=:accountId")
    Flowable<List<CashTransaction>> getTransactionsFromAccount(int accountId);
}
