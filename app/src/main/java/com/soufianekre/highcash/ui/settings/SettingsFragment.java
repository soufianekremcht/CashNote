package com.soufianekre.highcash.ui.settings;

import android.content.Intent;
import android.os.Bundle;
//import javax.inject.Inject;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.ListPreference;
import androidx.preference.Preference;

import com.soufianekre.highcash.R;
import com.soufianekre.highcash.data.app_preference.PrefConst;
import com.soufianekre.highcash.helper.PermissionHelper;
import com.soufianekre.highcash.helper.currency.CashCurrency;
import com.soufianekre.highcash.helper.currency.CurrencyHelper;
import com.soufianekre.highcash.ui.app_base.BasePreferenceFragment;
import com.soufianekre.highcash.ui.settings.export.ExportDataDialogFragment;
import com.soufianekre.highcash.ui.settings.rate_us.RateUsActivity;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class SettingsFragment extends BasePreferenceFragment implements SettingsContract.View{


    public static final String EXPORT_DATA_DIALOG = "export_data_dialog";
    private static final String DEFAULT_CURRENCY = "default_currency";


    @Inject
    SettingsPresenter<SettingsContract.View> mPresenter;


    private String defaultCurrency;

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_layout,null);
        if (getActivityComponent() != null){
            getActivityComponent().inject(this);
            mPresenter.onAttach(this);
        }
        if (savedInstanceState != null)
            defaultCurrency = savedInstanceState.getString(DEFAULT_CURRENCY,"");
        else
            defaultCurrency = mPresenter.getDefaultCurrencyCode();


        setupCurrencyPicker();
        setupPreferences();


    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DEFAULT_CURRENCY,defaultCurrency);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.e("On Resume");

    }

    @Override
    public void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        switch (preference.getKey()){
            case PrefConst.PREF_DEFAULT_CURRENCY:
                mPresenter.setDefaultCurrencyCode(newValue.toString());
                String currencyCode = CurrencyHelper.getCommodity(newValue.toString()).getMnemonic();
                String full_name = CurrencyHelper.getCommodity(newValue.toString()).getFullName();
                preference.setSummary(full_name);
                mPresenter.setDefaultCurrencyCode(currencyCode);
                return true;


        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()){
            case PrefConst.PREF_EXPORT_DATA:
                // open Export Dialog
                PermissionHelper.requestStoragePermission(getActivity());
                if (PermissionHelper.isStoragePermissionGranted(getContext())){

                    FragmentTransaction fragmentTransaction = getActivity().
                            getSupportFragmentManager().beginTransaction();
                    Fragment fragment = getActivity().getSupportFragmentManager()
                            .findFragmentByTag(EXPORT_DATA_DIALOG);

                    if (fragment != null){
                        fragmentTransaction.remove(fragment);
                    }
                    ExportDataDialogFragment dataDialogFragment = new ExportDataDialogFragment();
                    dataDialogFragment.show(fragmentTransaction,EXPORT_DATA_DIALOG);
                }else{
                    showMessage("You Need Storage Permissions To Use This Feature");
                }

                return true;
            case PrefConst.PREF_RATE_US:
                startActivity(new Intent(getActivity(), RateUsActivity.class));
                return true;
            default:
                return false;
        }
    }
    private void setupCurrencyPicker(){
        List<CashCurrency> currenciesList = CurrencyHelper.fetchAllCurrency();
        CharSequence[] mCurrencyEntries = new CharSequence[currenciesList.size()];
        CharSequence[] mCurrencyEntryValues = new CharSequence[currenciesList.size()];
        Timber.e("Currency Picker working here");
        for (int i = 0 ;i <currenciesList.size();i++){
            String code = currenciesList.get(i).getCurrencyCode();
            String name = currenciesList.get(i).getFullName();
            mCurrencyEntries[i] = code + " - " + name;
            mCurrencyEntryValues[i] = code;
        }
        Timber.e("Currency Should Be Displayed");


        ListPreference pref = findPreference(PrefConst.PREF_DEFAULT_CURRENCY);
        String currencyName = CurrencyHelper.getCommodity(defaultCurrency).getFullName();
        Timber.e("Currerncy Entries Size : " + mCurrencyEntries.length);


        if (pref != null){
            pref.setEntries(mCurrencyEntries);
            pref.setEntryValues(mCurrencyEntryValues);
            pref.setOnPreferenceChangeListener(this);

        }else{
            Timber.e("some thing wrong with this ");
        }

    }
    private void setupPreferences(){
        Preference exportPref = findPreference(PrefConst.PREF_EXPORT_DATA);
        exportPref.setOnPreferenceClickListener(this);
    }


}