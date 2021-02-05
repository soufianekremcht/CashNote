package com.soufianekre.cashnotes.helper;

import android.content.Context;
import android.content.Intent;

import java.io.File;

public class FileUtils {

    public static void shareCSVFile(Context context, File file){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/csv");

    }
}
