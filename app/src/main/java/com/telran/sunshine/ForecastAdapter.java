package com.telran.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor}
 * to a {@link android.widget.ListView}.
 */

public class ForecastAdapter extends CursorAdapter {

    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;

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

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_FUTURE_DAY + 1;
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
        int resourceId = -1;
        int viewType = getItemViewType(cursor.getPosition());

        if(viewType == VIEW_TYPE_TODAY)
            resourceId = R.layout.list_item_forecast_today;
        else if(viewType == VIEW_TYPE_FUTURE_DAY)
            resourceId = R.layout.list_item_forecast;

        View view = LayoutInflater.from(context).inflate(resourceId, parent, false);
        if(view!=null)
            view.setTag(new ViewHolder(view));
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
        ViewHolder vh = (ViewHolder)view.getTag();
        if(vh != null)
            vh.fillHolder(mContext, cursor);
    }

    private static class ViewHolder{
        private final TextView mDate;
        private final TextView mTempMax;
        private final TextView mTempMin;
        private final TextView mDescr;
        private final ImageView mIcon;

        public ViewHolder(View view){
            mDate = (TextView) view.findViewById(R.id.list_item_date_txtvw);
            mTempMax = (TextView) view.findViewById(R.id.list_item_high_txtvw);
            mTempMin = (TextView) view.findViewById(R.id.list_item_low_txtvw);
            mDescr = (TextView) view.findViewById(R.id.list_item_forecast_txtvw);
            mIcon = (ImageView) view.findViewById(R.id.list_item_icon);
        }

        public void fillHolder(final Context context, Cursor cursor){
            // Read weather icon ID from cursor
            int weatherId = cursor.getInt(ForecastFragment.WEATHER_COL_ID);
            mIcon.setImageResource(R.mipmap.ic_launcher);

            String frendlyDate = Utility.getFriendlyDayString(context,
                    cursor.getLong(ForecastFragment.WEATHER_COL_DATE));
            mDate.setText(frendlyDate);

            mDescr.setText(cursor.getString(ForecastFragment.WEATHER_COL_DESC));

            boolean isMetric = Utility.isMetric(context);

            // Read high temperature from cursor
            double high = cursor.getDouble(ForecastFragment.WEATHER_COL_TEMP_MAX);
            mTempMax.setText(Utility.formatTemperature(high, isMetric));

            // Read high temperature from cursor
            double low = cursor.getDouble(ForecastFragment.WEATHER_COL_TEMP_MIN);
            mTempMin.setText(Utility.formatTemperature(low, isMetric));
        }
    }
}
