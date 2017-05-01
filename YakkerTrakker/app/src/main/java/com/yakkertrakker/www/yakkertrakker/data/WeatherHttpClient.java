package com.yakkertrakker.www.yakkertrakker.data;

import com.yakkertrakker.www.yakkertrakker.Util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ryanpetit787 on 4/11/17.
 */

public class WeatherHttpClient {
    public String getWeatherData(String location){
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) (new URL(Utils.BASE_URL + location + Utils.API_KEY)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoInput(true);
            connection.connect();

            // Read the response
            StringBuffer stringBuffer = new StringBuffer();
            inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line  = null;

            while((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line + "\r\n");
            }

            inputStream.close();
            connection.disconnect();

            return stringBuffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
