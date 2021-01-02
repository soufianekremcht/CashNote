package com.soufianekre.highcash.helper;

import com.soufianekre.highcash.R;
import com.soufianekre.highcash.data.db.model.TransactionCategory;

import java.util.ArrayList;
import java.util.List;

public class CategoryUtils {

    private static List<TransactionCategory> categories = new ArrayList<>();


    private static TransactionCategory car = new TransactionCategory("Car", R.drawable.category_car);
    private static TransactionCategory bank = new TransactionCategory("Saving",R.drawable.category_bank);
    private static TransactionCategory shopping = new TransactionCategory("Shopping",R.drawable.category_shopping);
    private static TransactionCategory house = new TransactionCategory("House",R.drawable.category_home);
//    private AccountCategory food = new AccountCategory("Food",R.drawable.category_bank);
//    private AccountCategory  = new AccountCategory("Shelter",R.drawable.category_bank);

    public static List<TransactionCategory> getAllCategories(){
        categories.clear();
        categories.add(car);
        categories.add(bank);
        categories.add(shopping);
        categories.add(house);


        return categories;

    }
}
