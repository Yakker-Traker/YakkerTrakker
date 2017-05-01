package com.yakkertrakker.www.yakkertrakker.model;

/**
 * Created by ryanpetit787 on 4/11/17.
 */

public class Temperature {
    private  double temp;
    private float minTemp;
    private float maxTemp;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }
}
