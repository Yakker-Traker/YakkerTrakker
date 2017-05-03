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
import android.widget.ImageView;
import android.widget.TextView;

import com.yakkertrakker.www.yakkertrakker.Util.Utils;
import com.yakkertrakker.www.yakkertrakker.data.JSONWeatherParser;
import com.yakkertrakker.www.yakkertrakker.data.WeatherHttpClient;
import com.yakkertrakker.www.yakkertrakker.model.Weather;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import static com.yakkertrakker.www.yakkertrakker.R.layout.fragment_weather;
import static com.yakkertrakker.www.yakkertrakker.R.layout.settings;


/**
 * A simple {@link Fragment} subclass.
 */
public class settingsFragment extends Fragment {




    public settingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(settings, container, false);


        //ChangeCity changeCity = new ChangeCity(YakerTrakerActivity.this);
        //renderWeatherData(changeCity.getCity());

    }

}






