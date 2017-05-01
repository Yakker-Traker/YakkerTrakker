package com.yakkertrakker.www.yakkertrakker.model;

/**
 * Created by ryanpetit787 on 4/11/17.
 */

public class Weather {
    public Location location;
    public String iconData;
    public CurrentCondition currentCondition = new CurrentCondition();
    public Temperature temperature = new Temperature();
    public Wind wind = new Wind();
    public Snow snow = new Snow();
    public Clouds clouds = new Clouds();

}
