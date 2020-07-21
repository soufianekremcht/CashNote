package com.example.highcash.ui.account_editor.adapter;

import androidx.annotation.DrawableRes;

import java.io.Serializable;

public class AccountCategory implements Serializable {

    private String Name;
    @DrawableRes
    private int categoryImage;

    public AccountCategory(String name, int categoryImage) {
        Name = name;
        this.categoryImage = categoryImage;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(int categoryImage) {
        this.categoryImage = categoryImage;
    }
}
