package com.soufianekre.cashnotes.ui.settings;


import androidx.annotation.NonNull;

import com.soufianekre.cashnotes.MyApp;
import com.soufianekre.cashnotes.data.DataManager;
import com.soufianekre.cashnotes.data.app_preference.PrefsConst;
import com.soufianekre.cashnotes.ui.base.BasePresenter;
import com.soufianekre.cashnotes.helper.currency.CashCurrency;
import com.soufianekre.cashnotes.helper.currency.CurrencyHelper;
import com.soufianekre.cashnotes.helper.currency.Money;
import com.soufianekre.cashnotes.helper.rx.SchedulerProvider;

import java.util.Currency;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;


public class SettingsPresenter<V extends SettingsContract.View> extends BasePresenter<V> implements SettingsContract.Presenter<V> {



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
            Timber.e("%s", e.getMessage());
        } finally {
            currencyCode = MyApp.AppPref().getString(
                    PrefsConst.PREF_DEFAULT_CURRENCY,currencyCode);
        }
        return currencyCode;
    }


    public void setDefaultCurrencyCode(@NonNull String currencyCode){
        MyApp.AppPref().set(PrefsConst.PREF_DEFAULT_CURRENCY,currencyCode);
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