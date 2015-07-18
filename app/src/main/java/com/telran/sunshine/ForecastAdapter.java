package com.telran.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor}
 * to a {@link android.widget.ListView}.
 */

public class ForecastAdapter extends CursorAdapter {

    /**
     * Recommended constructor.
     *
     * @param c The cursor from which to get the data.
     * @param context The context
     * @param flags Flags used to determine the behavior of the adapter; may
     * be any combination of {@link #FLAG_AUTO_REQUERY} and
     * {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */
    public ForecastAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
    }


    /**
     * Makes a new view to hold the data pointed to by cursor.
     * Remember that these views are reused as needed.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_forecast, parent, false);
        return view;
    }

    /**
     * This is where we fill-in the views with the contents of the cursor.
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtVw = (TextView) view;

        txtVw.setText(convertCursorRowToUXFormat(cursor));
    }


    /**
     *  Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        Boolean isMetric = Utility.isMetric(mContext);

        String highLowStr = String.format(Locale.getDefault(), "%s/%s", Utility.formatTemperature(high, isMetric), Utility.formatTemperature(low, isMetric));
        return highLowStr;
    }

    /**
     * This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
     * string.
     */
    private String convertCursorRowToUXFormat(Cursor cursorRow) {
        //get row indexes for our cursor

        String highLow = formatHighLows(cursorRow.getDouble(ForecastFragment.WEATHER_COL_TEMP_MAX),
                cursorRow.getDouble(ForecastFragment.WEATHER_COL_TEMP_MIN));

        return String.format(Locale.getDefault(), "%s - %s - %s",
                Utility.formatDate(cursorRow.getLong(ForecastFragment.WEATHER_COL_DATE)),
                cursorRow.getString(ForecastFragment.WEATHER_COL_DESC),
                highLow);
    }
}
