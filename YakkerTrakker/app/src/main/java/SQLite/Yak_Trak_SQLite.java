package SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dell on 4/6/2017.
 */

public class Yak_Trak_SQLite extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 57;

    private static final String DATABASE_NAME = "Yakker_Trakker_DB";

    public Yak_Trak_SQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase ytDatabase){
        // CREATES THE ROUTES_TABLE
        String CREATE_ROUTES_TABLE = "CREATE TABLE routes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "route_name TEXT UNIQUE, " +
                "date_created TEXT," +
                "comments TEXT );";
        ytDatabase.execSQL(CREATE_ROUTES_TABLE);

  // CREATES THE COORDINATES_TABLE
        String CREATE_COORDINATES_TABLE = "CREATE TABLE coordinates (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "latitude REAL," +
                "longitude REAL," +
                "route_name TEXT," +
                "UNIQUE (latitude, longitude, route_name));";
        ytDatabase.execSQL(CREATE_COORDINATES_TABLE);

        // CREATES THE TIDE_TABLES_TABLE
        String CREATE_TIDES_TABLE = "CREATE TABLE tides ("+
                "station_name TEXT UNIQUE," +
                "station_id TEXT UNIQUE,"+
                "latitude TEXT,"+
                "longitude TEXT);";
        ytDatabase.execSQL(CREATE_TIDES_TABLE);
       // insertTideData();
    }

    public void insertTideData () {
        SQLiteDatabase ytDatabase = this.getWritableDatabase();
        ytDatabase.delete("tides","station_name = ?",new String [] {"*"});

        String TideData = "Richmond, CA - Station ID: 9414863\n" +
                "Latitude\n" +
                "37° 55.4' N\n" +
                "Longitude\n" +
                "122° 24.6' W\n" +
                "\n" +
                "San Francisco, CA - Station ID: 9414290\n" +
                "Latitude\n" +
                "37° 48.4' N\n" +
                "Longitude\n" +
                "122° 27.9' W\n" +
                "\n" +
                "Point Reyes, CA - Station ID: 9415020\n" +
                "Latitude\n" +
                "37° 59.8' N\n" +
                "Longitude\n" +
                "122° 58.6' W\n" +
                "\n" +
                "Monterey, CA - Station ID: 9413450\n" +
                "Latitude\n" +
                "36° 36.3' N\n" +
                "Longitude\n" +
                "121° 53.3' W\n" +
                "\n" +
                "Port San Luis, CA - Station ID: 9412110\n" +
                "Latitude\n" +
                "35° 10.1' N\n" +
                "Longitude\n" +
                "120° 45.2' W\n" +
                "\n" +
                "Santa Monica, CA - Station ID: 9410840\n" +
                "Latitude\n" +
                "34° 0.5' N\n" +
                "Longitude\n" +
                "118° 30' W\n" +
                "\n" +
                "La Jolla, CA - Station ID: 9410230\n" +
                "Latitude\n" +
                "32° 52' N\n" +
                "Longitude\n" +
                "117° 15.4' W\n" +
                "\n" +
                "Arena Cove, CA - Station ID: 9416841\n" +
                "Latitude\n" +
                "38° 54.9' N\n" +
                "Longitude\n" +
                "123° 42.7' W\n" +
                "\n" +
                "North Spit, CA - Station ID: 9418767\n" +
                "Latitude\n" +
                "40° 46' N\n" +
                "Longitude\n" +
                "124° 13' W\n";
        TideData = TideData.replaceAll("[\n]","").replace("\r","");
        int itr = 0;
        for (int i = 0; i < 9; i++){
            ContentValues values = new ContentValues();

            String data = "";
            while (TideData.charAt(itr) != '-') {
                data += TideData.charAt(itr);
                itr++;
            }
            values.put("station_name", data);
            data = "";
            while (TideData.charAt(itr) != '9') {
                itr++;
            }
            while (TideData.charAt(itr) != 'L') {
                data += TideData.charAt(itr);
                itr++;
            }
            values.put("station_id", data);
            data = "";
            while (TideData.charAt(itr) != 'e') {
                itr++;
            }
            itr++;
            while (TideData.charAt(itr) != 'L') {
                data += TideData.charAt(itr);
                itr++;
            }
            values.put("latitude", data);
            data = "";
            while (TideData.charAt(itr) != 'e') {
                itr++;
            }
            itr++;
            while (TideData.charAt(itr) != 'N' && TideData.charAt(itr) != 'S'&& TideData.charAt(itr) != 'E' &&TideData.charAt(itr) != 'W') {
                data += TideData.charAt(itr);
                itr++;
            }
            data += TideData.charAt(itr);
            values.put("longitude", data);
            long inserted = ytDatabase.insert("tides", null, values);
            itr++;
        }

    }






    @Override
    public void onUpgrade (SQLiteDatabase ytDatabase, int oldVersion, int newVersion){
        ytDatabase.execSQL(("DROP TABLE IF EXISTS test"));
        ytDatabase.execSQL("DROP TABLE IF EXISTS routes");
        ytDatabase.execSQL("DROP TABLE IF EXISTS coordinates");
        ytDatabase.execSQL("DROP TABLE IF EXISTS tides");

        this.onCreate(ytDatabase);
    }

    private  static final String ROUTES_TABLE = "routes";

    private static final String KEY_ROUTE_NAME = "route_name";
    private static final String KEY_DATE_CREATED = "date_created";
    private static final String KEY_COMMENTS = "comments";

    private static final String COORDINATES_TABLE = "coordinates";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    public void addRouteIntoDataBase(Routes route) {
        SQLiteDatabase Yak_Trak_Database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ROUTE_NAME, route.getRoute_name());
        values.put(KEY_DATE_CREATED, route.getDate_created());
        values.put(KEY_COMMENTS, route.getComments());
        long inserted = Yak_Trak_Database.insert(ROUTES_TABLE, null, values);
        if (inserted != -1) {
            route.setId((int)inserted);
        }
        Yak_Trak_Database.close();
    }

    public void updateRoute (Routes route, int id){

        SQLiteDatabase Yak_Trak_Database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("route_name", route.getRoute_name());
        values.put("comments", route.getComments());

        Yak_Trak_Database.update(ROUTES_TABLE,
                values,
                "id = ?",
                new String [] {String.valueOf(id)});
        Yak_Trak_Database.close();
    }

    public boolean findRouteInDataBase (String tideName){

        String COMMAND = "SELECT * FROM "+ ROUTES_TABLE + " WHERE route_name == "+ "'" + tideName+ "'";
        SQLiteDatabase Yak_Trak_Database = this.getWritableDatabase();

        Cursor cursor = Yak_Trak_Database.rawQuery(COMMAND, null);
        if (cursor.moveToFirst()){

            cursor.close();
            Yak_Trak_Database.close();
            return true;
        }
        cursor.close();
        Yak_Trak_Database.close();
        return false;
    }

    public String getTideFromDataBase (String tideName){

        String COMMAND = "SELECT * FROM "+ "tides" + " WHERE station_name == "+ "'" + tideName+ "'";
        SQLiteDatabase Yak_Trak_Database = this.getWritableDatabase();

        Cursor cursor = Yak_Trak_Database.rawQuery(COMMAND, null);
        String data ="";
        if (cursor.moveToFirst()){
            data += cursor.getString(1);
            data += cursor.getString (2);
            data += cursor.getString (3);
            Yak_Trak_Database.close();
            return data;
        }
        cursor.close();
        Yak_Trak_Database.close();
        return data;
    }

    // Delete an entire Route including corresponding routes from the table.
    public void deleteRouteFromDataBase (String routeName){
        SQLiteDatabase Yak_Trak_Database = this.getWritableDatabase();
        // Deletes all the coordinates for the given route.
        Yak_Trak_Database.delete(COORDINATES_TABLE,"route_name == ?",new String [] {routeName});
        Yak_Trak_Database.delete(ROUTES_TABLE,"route_name = ?",new String [] {routeName});
        Yak_Trak_Database.close();
    }

    public List<Routes> getAllRoutesFromDataBase (){
        SQLiteDatabase Yak_Trak_Database = this.getWritableDatabase();
        List RouteList = new LinkedList <Routes>();

        String COMMAND = "SELECT * FROM " + ROUTES_TABLE + ';';
        Cursor cursor = Yak_Trak_Database.rawQuery(COMMAND,null);

        if (cursor.moveToFirst()){
            do{
                Routes route = new Routes ();
                route.setId(cursor.getInt(0));
                route.setRoute_name(cursor.getString(1));
                route.setDate_created(cursor.getString(2));
                route.setComments(cursor.getString(3));
                RouteList.add(route);
            }while (cursor.moveToNext());

        }
        Yak_Trak_Database.close();
        return RouteList;
    }

    // Adds a coordinate to the coordinates table.
    public void addCoordinateIntoDataBase(Coordinates coordinates) {
        SQLiteDatabase Yak_Trak_Database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, coordinates.getLatitude());
        values.put(KEY_LONGITUDE, coordinates.getLongitude());
        values.put(KEY_ROUTE_NAME, coordinates.getRoute_name());
        long inserted = Yak_Trak_Database.insert(COORDINATES_TABLE, null, values);
        if (inserted != -1) {
            coordinates.setId((int)inserted);
        }
            Yak_Trak_Database.close();
    }

    // Deletes the coordinate from the coordinates table.
    public void deleteCoordinateFromDataBase (Coordinates coordinates){
        SQLiteDatabase Yak_Trak_Database = this.getWritableDatabase();
        Yak_Trak_Database.delete(COORDINATES_TABLE,"id = ?", new String [] {String.valueOf(coordinates.get_id())});
        Yak_Trak_Database.close();
    }

    // Returns the coordinates in the route.
    public List<Coordinates> getCoordinatesInRoute(Routes route) {
        List<Coordinates> coordinates_list = new LinkedList<>();

        SQLiteDatabase Yak_Trak_Database = this.getWritableDatabase();

        String COMMAND = "SELECT * FROM "  + COORDINATES_TABLE + " WHERE route_name == " +  '"' +route.getRoute_name() +'"';
        Cursor cursor = Yak_Trak_Database.rawQuery(COMMAND, null);

        if (cursor.moveToFirst()) {
            do {
                Coordinates point = new Coordinates();
                point.setId(cursor.getInt(0));
                point.setLatitude(cursor.getInt(1));
                point.setLongitude(cursor.getInt(2));
                point.setRoute_name(cursor.getString(3));
                coordinates_list.add(point);
            } while (cursor.moveToNext());
        }
        return coordinates_list;
    }
}