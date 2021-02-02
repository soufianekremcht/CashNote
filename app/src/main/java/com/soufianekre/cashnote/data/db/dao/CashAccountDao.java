package com.soufianekre.cashnote.data.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.soufianekre.cashnote.data.db.model.CashAccount;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;


@Dao
public interface CashAccountDao {

    @Insert
    Completable addAccount(CashAccount account);

    @Update
    Completable updateAccount(CashAccount account);

    @Delete
    Completable deleteAccount(CashAccount account);

    @Query("Select * from cash_account Where id =:id LIMIT 1")
    Flowable<CashAccount> getAccount(int id);

    @Query("Select * from cash_account")
    Flowable<List<CashAccount>> getAccounts();

    @Query("Delete from cash_account")
    void deleteTables();



}

