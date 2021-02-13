package com.soufianekre.cashnotes.helper;

import android.content.Context;

import com.soufianekre.cashnotes.R;
import com.soufianekre.cashnotes.data.db.model.CashCategory;

import java.util.ArrayList;
import java.util.List;

public class CategoryUtils {


    private static final List<CashCategory> categories = new ArrayList<>();

    // Expenses
    private static CashCategory car;
    private static CashCategory bank;
    private static CashCategory shopping;
    private static CashCategory house;
    private static CashCategory sport;
    private static CashCategory health;
    private static CashCategory travel;
    private static CashCategory food;
    private static CashCategory entertainments;


    // Income
    private static CashCategory gift;

    private static CashCategory income;
    private static CashCategory investments;
    private static CashCategory social;


    private static CashCategory awards;
    /*
    private static CashCategory gift;
    private static CashCategory gift;

     */



    public static void init(Context c, boolean isExpenses) {
        if (isExpenses) {

            // Expenses
            car = new CashCategory("Car", R.drawable.ctg_car, AppUtils.getColor(c, R.color.accent_amber));
            bank = new CashCategory("Saving", R.drawable.ctg_dividends, AppUtils.getColor(c, R.color.accent_light_green));
            shopping = new CashCategory("Shopping", R.drawable.ctg_coupons, AppUtils.getColor(c, R.color.accent_blue));
            house = new CashCategory("House", R.drawable.ctg_home, AppUtils.getColor(c, R.color.accent_deep_orange));
            sport = new CashCategory("Sport", R.drawable.ctg_sports, AppUtils.getColor(c, R.color.accent_indigo));
            health = new CashCategory("Health", R.drawable.ctg_health, AppUtils.getColor(c, R.color.accent_green));
            food = new CashCategory("Food", R.drawable.ctg_food, AppUtils.getColor(c, R.color.accent_light_green));
            travel = new CashCategory("Travel", R.drawable.ctg_airplane, AppUtils.getColor(c, R.color.accent_light_blue));
            social = new CashCategory("Social", R.drawable.ctg_social, AppUtils.getColor(c, R.color.accent_pink));
            entertainments = new CashCategory("Entertainments", R.drawable.ctg_sport, AppUtils.getColor(c, R.color.accent_orange));


        } else {
            // Income
            gift = new CashCategory("Gift", R.drawable.ctg_gift, AppUtils.getColor(c, R.color.accent_amber));
            investments = new CashCategory("Investments", R.drawable.ctg_income, AppUtils.getColor(c, R.color.accent_indigo));
            income = new CashCategory("awards", R.drawable.ctg_coupons, AppUtils.getColor(c, R.color.accent_teal));
            awards = new CashCategory("income", R.drawable.ctg_dividends, AppUtils.getColor(c, R.color.accent_deep_purple));

        }


    }

    public static List<CashCategory> getExpensesCategories(Context c) {
        init(c, true);
        categories.clear();
        categories.add(car);
        categories.add(bank);
        categories.add(shopping);
        categories.add(house);
        categories.add(sport);
        categories.add(health);
        categories.add(entertainments);
        categories.add(food);

        return categories;

    }

    public static List<CashCategory> getIncomeCategories(Context c) {
        init(c, false);
        categories.clear();
        categories.add(gift);
        categories.add(income);
        categories.add(investments);
        categories.add(awards);

        return categories;

    }
}
