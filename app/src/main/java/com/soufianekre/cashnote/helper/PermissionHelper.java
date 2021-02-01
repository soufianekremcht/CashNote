package com.soufianekre.cashnote.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.soufianekre.cashnote.ui.app_base.BaseActivity;

import java.util.List;

import timber.log.Timber;

public class PermissionHelper {

    public static void requestStoragePermission(Activity activity) {
        Dexter.withActivity(activity).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.isAnyPermissionPermanentlyDenied()){
//                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
//                    intent.setData(uri);
//                    activity.startActivityForResult(intent, 101);
                }else{

                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).withErrorListener(error -> {
            Timber.e(error.name());
        }).check();
    }

    public static boolean isStoragePermissionGranted(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }



}
