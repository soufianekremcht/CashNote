package com.soufianekre.highcash.data.db.converters;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.soufianekre.highcash.data.db.model.CashCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class AccountCategoryDBConverter {

    @TypeConverter
    public static CashCategory stringToAccountCategory(String string){
        if (!TextUtils.isEmpty(string)){
            Type listType = new TypeToken<CashCategory>() {}.getType();
            return new Gson().fromJson(string,listType);
        }else{
            return null;
        }
    }

    @TypeConverter
    public static String fromAccountCategory(CashCategory transactionCategory){
        Gson gson = new Gson();
        return gson.toJson(transactionCategory);
    }
}

