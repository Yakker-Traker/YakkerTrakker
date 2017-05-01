package com.yakkertrakker.www.yakkertrakker.data;

import com.yakkertrakker.www.yakkertrakker.Util.Utils;
import com.yakkertrakker.www.yakkertrakker.model.Location;
import com.yakkertrakker.www.yakkertrakker.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by ryanpetit787 on 4/11/17.
 */

public class JSONWeatherParser {

    public static Weather getWeather(String data){

        Weather weather = new Weather();
        // Create JSON Object from data
        try {
            JSONObject jsonObject = new JSONObject(data);
            Location location = new Location();
            JSONObject coordinateObj = Utils.getObject("coord", jsonObject);
            location.setLat(Utils.getFloat("lat", coordinateObj));
            location.setLon(Utils.getFloat("lon", coordinateObj));

            // Get Sys object

            JSONObject sysObject = Utils.getObject("sys", jsonObject);
            location.setCountry(Utils.getString("country", sysObject));
            location.setCity(Utils.getString("name", jsonObject));
            weather.location = location;


            // Get weather info

            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            JSONObject jsonWeather = jsonArray.getJSONObject(0);
            weather.currentCondition.setWeatherId(Utils.getInt("id", jsonWeather));
            weather.currentCondition.setDescription(Utils.getString("description", jsonWeather));
            weather.currentCondition.setCondition(Utils.getString("main", jsonWeather));
            weather.currentCondition.setIcon(Utils.getString("icon", jsonWeather));

            JSONObject mainObject = Utils.getObject("main", jsonObject);
            weather.currentCondition.setHumidity(Utils.getInt("humidity", mainObject));
            weather.currentCondition.setPressure(Utils.getInt("pressure", mainObject));
            weather.currentCondition.setMinTemp(Utils.getFloat("temp_min", mainObject));
            weather.currentCondition.setMaxTemp(Utils.getFloat("temp_max", mainObject));
            weather.currentCondition.setTemperature(Utils.getDouble("temp", mainObject));

            JSONObject windObject = Utils.getObject("wind", jsonObject);
            weather.wind.setSpeed(Utils.getFloat("speed", windObject));
            weather.wind.setDeg(Utils.getFloat("deg", windObject));

            JSONObject cloudObject = Utils.getObject("clouds", jsonObject);
            weather.clouds.setPrecipitation(Utils.getInt("all", cloudObject));

            return weather;

        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
    }
}
