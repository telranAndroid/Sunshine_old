package com.telran.sunshine;

import android.content.Context;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created on 18-Jul-15.
 */
public class Utility {
    public static String getPreferredLocation(Context context){
        return SettingsActivity.getLocation(context);
    }

    public static boolean isMetric(Context context) {
        return SettingsActivity.getUnitsType(context).equalsIgnoreCase(context.getString(R.string.pref_units_metric));
    }

    static String formatTemperature(double temperature, boolean isMetric) {
        double temp = temperature;

        if(!isMetric)
            temp = temp * 9/5 + 32;

        return String.format(Locale.getDefault(), "%.1f", temp);
    }

    static String formatDate(long dateInMillisec){
        Date date = new Date(dateInMillisec);
        return DateFormat.getDateInstance().format(date);
    }
}
