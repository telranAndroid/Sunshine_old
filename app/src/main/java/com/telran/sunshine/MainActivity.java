package com.telran.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        boolean res = true;
        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_map:
                openPreferredLocationOnMap();
                break;
            default:
                res = super.onOptionsItemSelected(item);
        }

        return res;
    }

    private void openPreferredLocationOnMap() {
        String pref_location = SettingsActivity.getLocation(this);

        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", encodeLocation(pref_location))
                .build();

        Intent intentMap = new Intent(Intent.ACTION_VIEW);
        intentMap.setData(geoLocation);

        if(intentMap.resolveActivity(getPackageManager())!=null){
            startActivity(intentMap);
        } else {
            Log.d(LOG_TAG, "Couldn't open " + pref_location + " on the map. There is no relevant Map application.");
        }
    }

    private String encodeLocation(String location) {
        if(location!=null) {
            location.replace(",", "%2C");
            location.replace(" ", "+");
        }
        return location;
    }

}
