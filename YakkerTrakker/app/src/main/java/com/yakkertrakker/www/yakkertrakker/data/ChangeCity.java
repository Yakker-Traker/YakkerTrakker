package com.yakkertrakker.www.yakkertrakker.data;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by ryanpetit787 on 4/25/17.
 */

public class ChangeCity {
    SharedPreferences preferences;

    public ChangeCity(Activity activity){
        preferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity(){
        return preferences.getString("city", "");
    }

    public void setCity(String city){
        preferences.edit().putString("city", city).commit();
    }

}
