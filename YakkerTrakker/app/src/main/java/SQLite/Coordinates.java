package SQLite;

/**
 * Created by dell on 4/6/2017.
 */

public class Coordinates {
    private double latitude;
    private double longitude;
    private String route_name;

    // Used for the database
    private int id;

    public Coordinates (){}

    public Coordinates (double latitude, double longitude, String route_name){
        this.latitude = latitude;
        this.longitude = longitude;
        this.route_name = route_name;
        this.id = 0;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public String getRoute_name(){
        return route_name;
    }

    public int get_id(){ return id;}

    public void setLatitude (float latitude){
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setRoute_name(String route_name){
        this.route_name = route_name;
    }

    public void setId (int x){
        this.id = x;
    }
}
