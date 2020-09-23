package com.example.highcash.data.db.converters;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.example.highcash.data.db.model.TransactionCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class AccountCategoryDBConverter {

    @TypeConverter
    public static TransactionCategory stringToAccountCategory(String string){
        if (!TextUtils.isEmpty(string)){
            Type listType = new TypeToken<TransactionCategory>() {}.getType();
            return new Gson().fromJson(string,listType);
        }else{
            return null;
        }
    }

    @TypeConverter
    public static String fromAccountCategory(TransactionCategory transactionCategory){
        Gson gson = new Gson();
        return gson.toJson(transactionCategory);
    }
}

