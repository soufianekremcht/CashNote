package com.soufianekre.highcash.helper;

import android.app.Activity;

import androidx.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.soufianekre.highcash.R;

public class DialogsUtil {
    private static final String TAG = "GeneralDialogCreation";

    public static MaterialDialog showBasicDialog(Activity activity, @StringRes int content,
                                                 @StringRes int title,
                                                 @StringRes int positiveText,
                                                 @StringRes int negativeText) {
        int accentColor = AppUtils.getColor(activity, R.color.mdtp_accent_color);
        MaterialDialog.Builder a = new MaterialDialog.Builder(activity)
                .content(content)
                .widgetColor(accentColor)
                .theme(Theme.LIGHT)
                .title(title)
                .positiveText(positiveText)
                .positiveColor(accentColor)
                .negativeText(negativeText)
                .negativeColor(accentColor);
        return a.build();
    }

    public static MaterialDialog showBasicDialogWithListener(Activity activity, @StringRes int content,
                                                 @StringRes int title,
                                                 @StringRes int positiveText,
                                                 @StringRes int negativeText,
                                                  MaterialDialog.SingleButtonCallback callback) {
        int accentColor = AppUtils.getColor(activity,R.color.mdtp_accent_color);
        MaterialDialog.Builder a = new MaterialDialog.Builder(activity)
                .content(content)
                .widgetColor(accentColor)
                .theme(Theme.LIGHT)
                .title(title)
                .positiveText(positiveText)
                .positiveColor(accentColor)
                .onPositive(callback)
                .negativeText(negativeText)
                .negativeColor(accentColor);
        return a.build();
    }
}
