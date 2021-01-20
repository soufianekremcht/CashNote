package com.soufianekre.highcash.helper;

import android.graphics.Color;

import com.soufianekre.highcash.R;
import com.soufianekre.highcash.data.db.model.CashCategory;

import java.util.ArrayList;
import java.util.List;

public class CategoryUtils {

    private static final List<CashCategory> categories = new ArrayList<>();


    private static final CashCategory car = new CashCategory("Car", R.drawable.ctg_car, Color.RED);
    private static final CashCategory bank = new CashCategory("Saving",R.drawable.ctg_dividends,Color.BLUE);
    private static final CashCategory shopping = new CashCategory("Shopping",R.drawable.ctg_coupons,Color.MAGENTA);
    private static final CashCategory house = new CashCategory("House",R.drawable.ctg_home,Color.CYAN);


    public static List<CashCategory> getAllCategories(){
        categories.clear();
        categories.add(car);
        categories.add(bank);
        categories.add(shopping);
        categories.add(house);


        return categories;

    }
}
