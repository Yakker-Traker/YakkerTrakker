package SQLite;

/**
 * Created by dell on 4/6/2017.
 */

public class Routes {
    private String route_name;
    private String date_created;
    private String comments  = " ";

    public Routes (){}

    public Routes (String route_name, String date_created, String comments){
        this.route_name = route_name;
        this.date_created = date_created;
        if (comments.length() != 0) {
            this.comments = comments;
        }
    }

    String getRoute_name (){
        return route_name;
    }

    String getDate_created (){
        return date_created;
    }

    String getComments (){
        return comments;
    }
}

