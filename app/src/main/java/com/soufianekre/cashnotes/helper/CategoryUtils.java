package com.soufianekre.cashnotes.helper;

import android.content.Context;

import com.soufianekre.cashnotes.R;
import com.soufianekre.cashnotes.data.db.model.CashCategory;

import java.util.ArrayList;
import java.util.List;

public class CategoryUtils {



    private static final List<CashCategory> categories = new ArrayList<>();


    private static CashCategory car;
    private static CashCategory bank;
    private static CashCategory shopping;
    private static CashCategory house;
    private static CashCategory sport;
    private static CashCategory health;

    public static void init(Context c){
        //car = new CashCategory("Car", R.drawable.ctg_car,AppUtils.getColor(c,R.color.accent_deep_purple));
        bank = new CashCategory("Saving",R.drawable.ctg_dividends,AppUtils.getColor(c,R.color.accent_light_green));
        shopping = new CashCategory("Shopping",R.drawable.ctg_coupons,AppUtils.getColor(c,R.color.accent_blue));
        house = new CashCategory("House",R.drawable.ctg_home,AppUtils.getColor(c,R.color.accent_deep_orange));
        sport = new CashCategory("Sport",R.drawable.ctg_sport,AppUtils.getColor(c,R.color.accent_indigo));
        health = new CashCategory("Health",R.drawable.ctg_health,AppUtils.getColor(c,R.color.accent_green));
    }
    public static List<CashCategory> getAllCategories(Context c){
        init(c);
        categories.clear();
        //categories.add(car);
        categories.add(bank);
        categories.add(shopping);
        categories.add(house);
        categories.add(sport);
        categories.add(health);
        return categories;

    }
}
