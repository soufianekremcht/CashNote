package com.soufianekre.highcash.data.db.model;

import androidx.annotation.DrawableRes;

import java.io.Serializable;

public class TransactionCategory implements Serializable {

    private String Name;
    @DrawableRes
    private int categoryImage;

    public TransactionCategory(String name, int categoryImage) {
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
