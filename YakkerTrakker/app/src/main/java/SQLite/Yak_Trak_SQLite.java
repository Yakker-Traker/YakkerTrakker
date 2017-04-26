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
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Yakker_Trakker_DB";

    public Yak_Trak_SQLite (Context context){
        super (context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase ytDatabase ){
        // CREATES THE ROUTES_TABLE
        String CREATE_ROUTES_TABLE = "CREATE TABlE routes ("+
                "id INTERGER PRIMARY KEY AUTOINCREMENT," +
                "route_name VARCHAR (256)PRIMARY KEY, " +
                "date_created VARCHAR (256)," +
                "comments VARCHAR (256) );";
        ytDatabase.execSQL(CREATE_ROUTES_TABLE);


        // CREATES THE COORDINATES_TABLE
        String CREATE_COORDINATES_TABLE = "CREATE TABLE coordinates (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "latitude REAL," +
                "longitude REAL),"+
                "route_name VARCHAR (256));";
        ytDatabase.execSQL(CREATE_COORDINATES_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private  static final String ROUTES_TABLE = "routes";
    private static final String KEY_ROUTE_NAME = "route_name";
    private  static final String KEY_DATE_CREATED = "date_created";
    private static final String KEY_COMMENTS = "comments";

    private static final String COORDINATES_TABLE = "coordinates";
    private static final String KEY_LATITUDE ="latitude";
    private static final String KEY_LONGITUDE = "longitude";


    public void addRoute (Routes route){
        SQLiteDatabase Yak_Trak_Database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ROUTE_NAME, route.getRoute_name());
        values.put(KEY_DATE_CREATED,route.getDate_created());
        values.put(KEY_COMMENTS,route.getComments());

        Yak_Trak_Database.insert(ROUTES_TABLE, null, values);

        Yak_Trak_Database.close();
    }

    // Returns the coordinates in the route.
    public List<Coordinates> getCoordinatesInRoute (Routes route){
        List<Coordinates> coordinates_list = new LinkedList<>();

        SQLiteDatabase Yak_Trak_Database = this.getReadableDatabase();

        String COMMAND = "SELECT * FROM"  + COORDINATES_TABLE +" WHERE" + route.getRoute_name() + " = route_name ;";

        Cursor cursor = Yak_Trak_Database.rawQuery(COMMAND,null);

        if (cursor.moveToFirst()){
            do {
                    Coordinates point = new Coordinates();
                    point.setLatitude(cursor.getInt(0));
                    point.setLongitude(cursor.getInt(1));
                    coordinates_list.add(point);
            }while(cursor.moveToNext());
        }
        return coordinates_list;
    }


    public void addCoordinate (Coordinates coordinates){
        SQLiteDatabase Yak_Trak_Database = this.getWritableDatabase();

        ContentValues values = new ContentValues ();
        values.put(KEY_LATITUDE, coordinates.getLatitude());
        values.put(KEY_LONGITUDE, coordinates.getLongitude());
        values.put(KEY_ROUTE_NAME, coordinates.getRoute_name());

        Yak_Trak_Database.insert( COORDINATES_TABLE, null, values);

        Yak_Trak_Database.close();
    }

}
