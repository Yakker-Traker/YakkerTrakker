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


/**
 * A simple {@link Fragment} subclass.
 */
public class weatherFragment extends Fragment {

    private TextView cityName;
    private TextView temp;
    private ImageView iconView;
    private TextView description;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;

    Weather weather = new Weather();


    public weatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(fragment_weather, container, false);


        cityName = (TextView) view.findViewById(R.id.cityText);
        iconView = (ImageView) view.findViewById(R.id.thumbnailIcon);
        temp = (TextView) view.findViewById(R.id.tempText);
        description = (TextView) view.findViewById(R.id.cloudText);
        humidity = (TextView) view.findViewById(R.id.humidityText);
        pressure = (TextView) view.findViewById(R.id.pressureText);
        wind = (TextView) view.findViewById(R.id.windText);


        //ChangeCity changeCity = new ChangeCity(YakerTrakerActivity.this);
        //renderWeatherData(changeCity.getCity());
         renderWeatherData("Rohnert Park, US");



        return view;
    }

    public void renderWeatherData(String city) {
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + "&units=metric"});
    }

    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImg(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iconView.setImageBitmap(bitmap);
        }

        private Bitmap downloadImg(String code) {
            try {
                URL url = new URL(Utils.ICON_URL + code + ".png");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    private class WeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {

            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

            weather = JSONWeatherParser.getWeather(data);

            Log.v("Data: ", weather.currentCondition.getDescription());

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);


            DecimalFormat decimalFormat = new DecimalFormat("#.#");

            String temperatureFormat = decimalFormat.format(weather.currentCondition.getTemperature());

            cityName.setText(weather.location.getCity() + "," + weather.location.getCountry());
            temp.setText("" + temperatureFormat + "°F");
            humidity.setText("Humidity: " + weather.currentCondition.getHumidity() + "%");
            pressure.setText("Pressure: " + weather.currentCondition.getPressure() + " hPa");
            wind.setText("Wind: " + weather.wind.getSpeed() + " mps");
            description.setText("Condition " + weather.currentCondition.getCondition() + " (" +
                    weather.currentCondition.getDescription() + ")");

            weather.iconData = weather.currentCondition.getIcon();

            DownloadImageAsyncTask downloadImgTask = new DownloadImageAsyncTask();
            downloadImgTask.execute(weather.iconData);

        }
    }
}






