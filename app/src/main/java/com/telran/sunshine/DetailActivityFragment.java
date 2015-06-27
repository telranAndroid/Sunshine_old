package com.telran.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent parentIntent = getActivity().getIntent();
        if(parentIntent != null && parentIntent.hasExtra(Intent.EXTRA_TEXT)) {
            String forecast = parentIntent.getStringExtra(Intent.EXTRA_TEXT);
            TextView tv = (TextView) rootView.findViewById(R.id.txtvw_helloworld);
            tv.setText(forecast);
        }

        return rootView;
    }
}
