package com.example.highcash.data.app_preference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;

public interface PreferencesHelper {

    <T> void set(@NonNull String key, @Nullable T value);
    String getString(@NonNull String key,String defaults);

    int getInt(@NonNull String key);
    long getLong(@NonNull String key);
    boolean getBoolean(@NonNull String key);
    float getFloat(@NonNull String key);
    boolean isExist(@NonNull String key);
    void clearKey(@NonNull String key);
    Map<String, ?> getAll();


}
