package com.soufianekre.cashnote.helper.currency;

import java.util.HashMap;
import java.util.Map;

public class CurrencyConstant {


    static Map<String,String> currencyNames = new HashMap<>();

    public static Map<String, String> getCurrencyNames() {
        setCurrencyNames();
        return currencyNames;
    }

    private static void setCurrencyNames() {
        currencyNames.put("AUD", "Australian Dollar $");
        currencyNames.put("CAD", "Canadian Dollar");
        currencyNames.put("EUR", "Euro €");
        currencyNames.put("GBP", "Pound Sterling £");
        currencyNames.put("JPY", "Yen ¥");
        currencyNames.put("USD", "US - Dollars $ ");
        currencyNames.put("MAD", "Morrocan Dirham DH");
        currencyNames.put("NUL", "No Currency");
//        currencyNames.put("","");}
    }
}
