package com.example.highcash.data.db.converters;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.example.highcash.ui.account_editor.adapter.AccountCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class AccountCategoryDBConverter {

    @TypeConverter
    public static AccountCategory stringToAccountCategory(String string){
        if (!TextUtils.isEmpty(string)){
            Type listType = new TypeToken<AccountCategory>() {}.getType();
            return new Gson().fromJson(string,listType);
        }else{
            return null;
        }
    }

    @TypeConverter
    public static String fromAccountCategory(AccountCategory accountCategory){
        Gson gson = new Gson();
        return gson.toJson(accountCategory);
    }
}

