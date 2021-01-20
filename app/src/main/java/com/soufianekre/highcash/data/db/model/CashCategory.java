package com.soufianekre.highcash.data.db.model;

import androidx.annotation.DrawableRes;

import java.io.Serializable;

public class CashCategory implements Serializable {

    private String name;
    @DrawableRes
    private int image;
    private int color;

    public CashCategory(String name, int image, int color) {
        this.name = name;
        this.image = image;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
