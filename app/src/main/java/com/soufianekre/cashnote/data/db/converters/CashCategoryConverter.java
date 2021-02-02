package com.soufianekre.cashnote.data.db.converters;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.soufianekre.cashnote.data.db.model.CashCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class CashCategoryConverter {

    @TypeConverter
    public static CashCategory jsonToCashCategory(String json){
        if (!TextUtils.isEmpty(json)){
            Type listType = new TypeToken<CashCategory>() {}.getType();
            return new Gson().fromJson(json,listType);
        }else{
            return null;
        }
    }

    @TypeConverter
    public static String cashCategoryToJson(CashCategory transactionCategory){
        Gson gson = new Gson();
        return gson.toJson(transactionCategory);
    }
}

