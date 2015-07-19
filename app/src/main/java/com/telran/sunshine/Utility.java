package com.telran.sunshine;

import android.content.Context;
import android.text.format.Time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    static String formatTemperature(Context context, double temperature, boolean isMetric) {
        double temp = temperature;

        if(!isMetric)
            temp = temp * 9/5 + 32;

        return context.getString(R.string.format_temperature, temp, isMetric ? "C" : "F"); //String.format(Locale.getDefault(), "%.1f", temp);
    }

    // Format used for storing dates in the database.  ALso used for converting those strings
    // back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "yyyyMMdd";

    /**
     * Helper method to convert the database representation of the date into something to display
     * to users.  As classy and polished a user experience as "20140102" is, we can do better.
     *
     * @param context Context to use for resource localization
     * @param dateInMillis The date in milliseconds
     * @return a user-friendly representation of the date.
     */
    public static String getFriendlyDayString(Context context, long dateInMillis) {
        // The day string for forecast uses the following logic:
        // For today: "Today, June 8"
        // For tomorrow:  "Tomorrow"
        // For the next 5 days: "Wednesday" (just the day name)
        // For all days after that: "Mon Jun 8"

        Time time = new Time();
        time.setToNow();
        long currentTime = System.currentTimeMillis();
        int julianDay = Time.getJulianDay(dateInMillis, time.gmtoff);
        int currentJulianDay = Time.getJulianDay(currentTime, time.gmtoff);

        // If the date we're building the String for is today's date, the format
        // is "Today, June 24"
        if (julianDay == currentJulianDay) {
            String today = context.getString(R.string.today);
            int formatId = R.string.format_full_friendly_date;
            return String.format(context.getString(
                    formatId,
                    today,
                    getFormattedMonthDay(context, dateInMillis)));
        } else if ( julianDay < currentJulianDay + 7 ) {
            // If the input date is less than a week in the future, just return the day name.
            return getDayName(context, dateInMillis);
        } else {
            // Otherwise, use the form "Mon Jun 3"
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(dateInMillis);
        }
    }

    /**
     * Given a day, returns just the name to use for that day.
     * E.g "today", "tomorrow", "wednesday".
     *
     * @param context Context to use for resource localization
     * @param dateInMillis The date in milliseconds
     * @return
     */
    public static String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if ( julianDay == currentJulianDay +1 ) {
            return context.getString(R.string.tomorrow);
        } else {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }

    /**
     * Converts db date format to the format "Month day", e.g "June 24".
     * @param context Context to use for resource localization
     * @param dateInMillis The db formatted date string, expected to be of the form specified
     *                in Utility.DATE_FORMAT
     * @return The day in the form of a string formatted "December 6"
     */
    public static String getFormattedMonthDay(Context context, long dateInMillis ) {
        Time time = new Time();
        time.setToNow();
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(Utility.DATE_FORMAT);
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("MMMM dd");
        String monthDayString = monthDayFormat.format(dateInMillis);
        return monthDayString;
    }


    static String formatDate(long dateInMillisec){
        Date date = new Date(dateInMillisec);
        return DateFormat.getDateInstance().format(date);
    }

    public static String getFormattedWind(Context context, float windSpeed, float degrees) {
        int windFormat;
        if (Utility.isMetric(context)) {
            windFormat = R.string.format_wind_kmh;
        } else {
            windFormat = R.string.format_wind_mph;
            windSpeed = .621371192237334f * windSpeed;
        }

        // From wind direction in degrees, determine compass direction as a string (e.g NW)
        // You know what's fun, writing really long if/else statements with tons of possible
        // conditions.  Seriously, try it!
        String direction = "Unknown";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "N";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "NE";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "E";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "SE";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "S";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "SW";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "W";
        } else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "NW";
        }
        return String.format(context.getString(windFormat), windSpeed, direction);
    }

    /**
     * Helper method to provide the icon resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     * @param weatherCondId from OpenWeatherMap API response
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
    public static int getIconResourceForWeatherCondition(int weatherCondId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherCondId >= 200 && weatherCondId <= 232) {
            return R.drawable.ic_storm;
        } else if (weatherCondId >= 300 && weatherCondId <= 321) {
            return R.drawable.ic_light_rain;
        } else if (weatherCondId >= 500 && weatherCondId <= 504) {
            return R.drawable.ic_rain;
        } else if (weatherCondId == 511) {
            return R.drawable.ic_snow;
        } else if (weatherCondId >= 520 && weatherCondId <= 531) {
            return R.drawable.ic_rain;
        } else if (weatherCondId >= 600 && weatherCondId <= 622) {
            return R.drawable.ic_snow;
        } else if (weatherCondId >= 701 && weatherCondId <= 761) {
            return R.drawable.ic_fog;
        } else if (weatherCondId == 761 || weatherCondId == 781) {
            return R.drawable.ic_storm;
        } else if (weatherCondId == 800) {
            return R.drawable.ic_clear;
        } else if (weatherCondId == 801) {
            return R.drawable.ic_light_clouds;
        } else if (weatherCondId >= 802 && weatherCondId <= 804) {
            return R.drawable.ic_cloudy;
        }
        return -1;
    }

    /**
     * Helper method to provide the art resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     * @param weatherCondId from OpenWeatherMap API response
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
    public static int getArtResourceForWeatherCondition(int weatherCondId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherCondId >= 200 && weatherCondId <= 232) {
            return R.drawable.art_storm;
        } else if (weatherCondId >= 300 && weatherCondId <= 321) {
            return R.drawable.art_light_rain;
        } else if (weatherCondId >= 500 && weatherCondId <= 504) {
            return R.drawable.art_rain;
        } else if (weatherCondId == 511) {
            return R.drawable.art_snow;
        } else if (weatherCondId >= 520 && weatherCondId <= 531) {
            return R.drawable.art_rain;
        } else if (weatherCondId >= 600 && weatherCondId <= 622) {
            return R.drawable.art_snow;
        } else if (weatherCondId >= 701 && weatherCondId <= 761) {
            return R.drawable.art_fog;
        } else if (weatherCondId == 761 || weatherCondId == 781) {
            return R.drawable.art_storm;
        } else if (weatherCondId == 800) {
            return R.drawable.art_clear;
        } else if (weatherCondId == 801) {
            return R.drawable.art_light_clouds;
        } else if (weatherCondId >= 802 && weatherCondId <= 804) {
            return R.drawable.art_clouds;
        }
        return -1;
    }
}
