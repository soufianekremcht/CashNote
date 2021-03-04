package com.soufianekre.cashnotes.helper;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



public class AppUtils {
    public static final String MAIN_DATE_FORMAT = "dd/MM/yyyy";
    public static final String TIME_24H_FORMAT = "HH:mm";
    public static final int CURRENT_YEAR = 2021;
    private static final Calendar calendar = Calendar.getInstance();

    public static int getColor(Context c,int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return c.getColor(color);
        } else {
            return ContextCompat.getColor(c,color);
        }
    }
    public static String getString(Context context, @StringRes int res){
        return context.getString(res);
    }



    public static String formatDate(Date date, String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return simpleDateFormat.format(date);
    }
    public static void showToast(Context mContext,String text){
        Toast.makeText(mContext,text,Toast.LENGTH_SHORT).show();
    }

    public static int checkCurrentYearDays(Calendar calendar){
        Calendar calendarX = Calendar.getInstance();
        // days of the month start from 1 and months from 0
        calendarX.set(CURRENT_YEAR,0,1);
        if (calendar.get(Calendar.YEAR) == CURRENT_YEAR)
            return calendar.get(Calendar.DAY_OF_YEAR);
        else
            return -1;
    }


    public static int getCurrentDaysCount(){
        return calendar.get(Calendar.DAY_OF_YEAR);
    }
    public static int getCurrentYear(){
        return calendar.get(Calendar.YEAR);
    }
}
