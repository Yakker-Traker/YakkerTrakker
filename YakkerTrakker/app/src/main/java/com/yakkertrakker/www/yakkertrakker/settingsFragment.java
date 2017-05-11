package com.yakkertrakker.www.yakkertrakker;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.yakkertrakker.www.yakkertrakker.Util.Utils;
import com.yakkertrakker.www.yakkertrakker.data.JSONWeatherParser;
import com.yakkertrakker.www.yakkertrakker.data.WeatherHttpClient;
import com.yakkertrakker.www.yakkertrakker.model.Weather;




/**
 * A simple {@link Fragment} subclass.
 */
public class settingsFragment extends Fragment {

    private Spinner unitsSpinner;
    private Spinner mapSpinner;

    public settingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        unitsSpinner = (Spinner) view.findViewById(R.id.units_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(settingsFragment.this.getContext(), R.array.distance_choice_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitsSpinner.setAdapter(adapter);

        mapSpinner = (Spinner) view.findViewById(R.id.map_spinner);
        ArrayAdapter<CharSequence> mapAdapter = ArrayAdapter.createFromResource(settingsFragment.this.getContext(), R.array.map_choice_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapSpinner.setAdapter(mapAdapter);

        return view;
    }
}








