package com.example.highcash.ui.settings;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.highcash.MyApp;
import com.example.highcash.data.DataManager;
import com.example.highcash.data.app_preference.PrefConst;
import com.example.highcash.ui.base.BasePresenter;
import com.example.highcash.helper.currency.CashCurrency;
import com.example.highcash.helper.currency.CurrencyHelper;
import com.example.highcash.helper.currency.Money;
import com.example.highcash.helper.rx.SchedulerProvider;

import java.util.Currency;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class SettingsPresenter<V extends SettingsContract.View> extends BasePresenter<V> implements SettingsContract.Presenter<V> {

    private static final String TAG = "SettingsPresenter";

    @Inject
    public SettingsPresenter(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    public String getDefaultCurrencyCode(){
        Locale locale = getDefaultLocale();

        String currencyCode = "USD"; //start with USD as the default
        try {
            //there are some strange locales out there
            currencyCode = Currency.getInstance(locale).getCurrencyCode();
        } catch (Throwable e) {
            Log.e("HighCash", "" + e.getMessage());
        } finally {
            currencyCode = MyApp.AppPref().getString(
                    PrefConst.PREF_DEFAULT_CURRENCY,currencyCode);
        }
        return currencyCode;
    }


    public void setDefaultCurrencyCode(@NonNull String currencyCode){
        MyApp.AppPref().set(PrefConst.PREF_DEFAULT_CURRENCY,currencyCode);
        Money.DEFAULT_CURRENCY_CODE = currencyCode;
        CashCurrency.defaultCashCurrency = CurrencyHelper.getCommodity(currencyCode);
    }

    public static Locale getDefaultLocale() {
        Locale locale = Locale.getDefault();
        //sometimes the locale en_UK is returned which causes a crash with Currency
        if (locale.getCountry().equals("UK")) {
            locale = new Locale(locale.getLanguage(), "GB");
        }

        //for unsupported locale es_LG
        if (locale.getCountry().equals("LG")){
            locale = new Locale(locale.getLanguage(), "ES");
        }

        if (locale.getCountry().equals("en")){
            locale = Locale.US;
        }
        return locale;
    }

}