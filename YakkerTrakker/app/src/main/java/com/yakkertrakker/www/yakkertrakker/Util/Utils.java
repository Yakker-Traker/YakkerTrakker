package com.yakkertrakker.www.yakkertrakker.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ryanpetit787 on 4/10/17.
 */

public class Utils {

    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    public static final String API_KEY = "&appid=675a4a4cf30ff02238408cebe17ae629";
    public static final String ICON_URL = "http://api.openweathermap.org/img/w/";

    public static JSONObject getObject(String tagName, JSONObject jsonObject) throws JSONException{
        JSONObject jObj = jsonObject.getJSONObject(tagName);
        return jObj;
    }

    public static String getString(String tagName, JSONObject jsonObject) throws JSONException{
        return jsonObject.getString(tagName);
    }

    public static float getFloat(String tagName, JSONObject jsonObject) throws  JSONException{
        return (float) jsonObject.getDouble(tagName);
    }

    public static double getDouble(String tageName, JSONObject jsonObject) throws JSONException{
        return (float) jsonObject.getDouble(tageName);
    }

    public static int getInt(String tagName, JSONObject jsonObject) throws JSONException{
        return jsonObject.getInt(tagName);
    }

 }
