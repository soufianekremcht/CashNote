package com.example.highcash.data.db.converters;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.example.highcash.data.db.model.CashTransaction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class CashTransactionDBConverter {
    @TypeConverter
    public static List<CashTransaction> stringToTransactions(String string){
        if (!TextUtils.isEmpty(string)){
            Type listType = new TypeToken<List<CashTransaction>>() {}.getType();
            return new Gson().fromJson(string,listType);
        }else{
            return null;
        }
    }

    @TypeConverter
    public static String fromTransactions(List<CashTransaction> subAccounts){
        Gson gson = new Gson();
        return gson.toJson(subAccounts);
    }

}
