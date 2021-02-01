package com.soufianekre.cashnote.helper.currency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CurrencyHelper {


    public static CashCurrency getCommodity(String currencyCode){
        return new CashCurrency(getCurrencyFullName(currencyCode),currencyCode,100);
    }

    public static List<CashCurrency> fetchAllCurrency(){
        Map<String,String> currencies = CurrencyConstant.getCurrencyNames();
        List<CashCurrency> commodities = new ArrayList<>();
        for (String key : currencies.keySet()) {
            CashCurrency cashCurrency = new CashCurrency(currencies.get(key),key,100);
            commodities.add(cashCurrency);
        }
        return commodities;

    }

    public static String getCurrencyFullName(String currencyCode){
        return CurrencyConstant.currencyNames.get(currencyCode);
    }
}
