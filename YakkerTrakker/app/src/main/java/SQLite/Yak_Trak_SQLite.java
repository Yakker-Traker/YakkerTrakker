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
    private static final int DATABASE_VERSION = 34;

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

    }

    @Override
    public void onUpgrade (SQLiteDatabase ytDatabase, int oldVersion, int newVersion){
        ytDatabase.execSQL(("DROP TABLE IF EXISTS test"));
        ytDatabase.execSQL("DROP TABLE IF EXISTS routes");
        ytDatabase.execSQL("DROP TABLE IF EXISTS coordinates");

        this.onCreate(ytDatabase);
    }


    private static final String ROUTES_TABLE = "routes";
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

    public boolean findRouteInDataBase (String routeName){

        String COMMAND = "SELECT * FROM "+ ROUTES_TABLE + " WHERE route_name == "+ "'" + routeName+ "'";
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

    // Delete an entire Route including corresponding routes from the table.
    public void deleteRouteFromDataBase (String routeName){
        SQLiteDatabase Yak_Trak_Database = this.getWritableDatabase();
        // Deletes all the coordinates for the given route.
        Yak_Trak_Database.delete(COORDINATES_TABLE,"route_name == ?",new String [] {routeName});
        Yak_Trak_Database.delete(ROUTES_TABLE,"route_name = ?",new String [] {routeName});
        Yak_Trak_Database.close();
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