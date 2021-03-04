package com.soufianekre.cashnotes.data.app_preference;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.soufianekre.cashnotes.di.scope.ApplicationContext;
import com.soufianekre.cashnotes.di.scope.PreferenceInfo;
import com.soufianekre.cashnotes.helper.InputHelper;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppPreferencesHelper implements PreferencesHelper {


    private static SharedPreferences mPrefs = null;

    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context,
                                @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }
    @Override
    public  <T> void set(@NonNull String key, @Nullable T value) {
        if (InputHelper.isEmpty(key)) {
            throw new NullPointerException("Key must not be null! (key = " + key + "), (value = " + value + ")");
        }
        SharedPreferences.Editor edit = mPrefs.edit();
        if (InputHelper.isEmpty(value)) {
            clearKey(key);
            return;
        }
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else {
            edit.putString(key, value.toString());
        }
        edit.apply();//apply on UI
    }


    @Override
    @Nullable
    public String getString(@NonNull String key,String defaults) {
        return mPrefs.getString(key, defaults);
    }
    @Override
    public boolean getBoolean(@NonNull String key) {
        return mPrefs.getAll().get(key) instanceof Boolean && mPrefs.getBoolean(key, false);
    }
    @Override
    public int getInt(@NonNull String key) {
        return mPrefs.getAll().get(key) instanceof Integer ? mPrefs.getInt(key, 0) : -1;
    }
    @Override
    public long getLong(@NonNull String key) {
        return mPrefs.getLong(key, 0);
    }
    @Override
    public float getFloat(@NonNull String key) {
        return mPrefs.getFloat(key, 0);
    }
    @Override
    public void clearKey(@NonNull String key) {
        mPrefs.edit().remove(key).apply();
    }
    @Override
    public boolean isExist(@NonNull String key) {
        return mPrefs.contains(key);
    }

    public void clearPrefs() {
        mPrefs.edit().clear().apply();
    }
    @Override
    public Map<String, ?> getAll() {
        return mPrefs.getAll();
    }



}
