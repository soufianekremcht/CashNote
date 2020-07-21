package com.example.highcash.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class FileUtils {

    public static void shareCSVFile(Context context, File file){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/csv");

    }
}
