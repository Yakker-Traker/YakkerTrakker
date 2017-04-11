package SQLite;

/**
 * Created by dell on 4/6/2017.
 */

public class Routes {
    private String route_name;
    private String date_created;
    private String comments  = " ";

    private int id;

    public Routes (){}

    public Routes (String route_name, String date_created, String comments){
        this.route_name = route_name;
        this.date_created = date_created;
        if (comments.length() != 0) {
            this.comments = comments;
        }
    }

    public String getRoute_name (){
        return route_name;
    }

    public String getDate_created (){
        return date_created;
    }

    public String getComments (){return comments; }

    public int  get_Id (){return id; }

    public void setRoute_name(String route_name){
        this.route_name = route_name;
    }

    public void setComments (String comments){
        this.comments = comments;
    }

    public void setId (int x){
        this.id = x;
    }
}

