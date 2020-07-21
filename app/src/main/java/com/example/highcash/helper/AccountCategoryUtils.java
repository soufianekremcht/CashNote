package com.example.highcash.helper;

import com.example.highcash.R;
import com.example.highcash.ui.account_editor.adapter.AccountCategory;

import java.util.ArrayList;
import java.util.List;

public class AccountCategoryUtils {

    private static List<AccountCategory> categories = new ArrayList<>();


    private static AccountCategory car = new AccountCategory("Car", R.drawable.category_car);
    private static AccountCategory bank = new AccountCategory("Saving",R.drawable.category_bank);
    private static AccountCategory shopping = new AccountCategory("Shopping",R.drawable.category_shopping);
    private static AccountCategory house = new AccountCategory("House",R.drawable.category_home);
//    private AccountCategory food = new AccountCategory("Food",R.drawable.category_bank);
//    private AccountCategory  = new AccountCategory("Shelter",R.drawable.category_bank);

    public static List<AccountCategory> getAllCategories(){
        categories.clear();
        categories.add(car);
        categories.add(bank);
        categories.add(shopping);
        categories.add(house);


        return categories;

    }
}
