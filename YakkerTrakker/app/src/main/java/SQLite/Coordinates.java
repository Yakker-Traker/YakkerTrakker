package SQLite;

/**
 * Created by dell on 4/6/2017.
 */

public class Coordinates {
    private int id;
    private float latitude;
    private float longitude;
    private String route_name;

    public Coordinates (){}

    public Coordinates (float latitude, float longitude, String route_name){
        this.latitude = latitude;
        this.longitude = longitude;
        this.route_name = route_name;
    }

    public float getLatitude(){
        return latitude;
    }

    public float getLongitude(){
        return longitude;
    }

    public String getRoute_name(){
        return route_name;
    }

    public void setLatitude (float latitude){
        this.latitude = latitude;
    }

    public void setLongitude(float longitude){
        this.longitude = longitude;
    }
}
