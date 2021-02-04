package com.soufianekre.cashnote.ui.settings.about;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.soufianekre.cashnote.R;
import com.soufianekre.cashnote.data.app_preference.PrefsConst;


public class AboutFragment extends PreferenceFragmentCompat implements
        Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {


    public AboutFragment() {
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_about_layout);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        switch (key){
            default:
                return false;
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        switch (key){

            case PrefsConst.PREF_ABOUT_PRIVACY_POLICY:
                openBrowser("https://clippy.flycricket.io/privacy.html");
                return true;
            case PrefsConst.PREF_ABOUT_OPEN_SOURCE_LICENSES:
                return true;
            case PrefsConst.PREF_ABOUT_APP_DEV:
                goToGooglePlayStore();

            default:
                return false;
        }
    }


    private void openBrowser(String url){
        Intent open2 = new Intent(Intent.ACTION_VIEW);
        open2.setData(Uri.parse(url));
        startActivity(open2);
    }

    private void goToReportIssuesPage() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SoufianeKreX")));
    }

    private void goToGooglePlayStore() {
        final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }

    }

}
