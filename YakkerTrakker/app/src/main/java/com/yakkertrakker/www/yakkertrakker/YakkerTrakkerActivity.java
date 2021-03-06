package com.yakkertrakker.www.yakkertrakker;


import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.support.v4.widget.SearchViewCompatIcs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import android.support.v7.widget.Toolbar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

import SQLite.Routes;
import SQLite.Coordinates;
import SQLite.Yak_Trak_SQLite;


//import static com.yakkertrakker.www.yakkertrakker.R.id.details_window;


public class YakkerTrakkerActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, com.google.android.gms.common.api.Result, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker currLocationMarker;
    private LocationRequest mLocationRequest;
    private ToggleButton startStop;
    private Boolean routeStarted;
    private ArrayList<Location> myRoute = new ArrayList<Location>();
    private Bundle myBundle;
    private float myDistance;
    private Calendar myCalendar;
    private int startHour;
    private int startMin;
    private int startSec;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private ToggleButton openDrawer;
    Button clear;
    private android.app.ProgressDialog pDialog;
    private boolean debug;
    private String routeNameTransfer;

    Yak_Trak_SQLite localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yakker_trakker);
        routeStarted = false;
        localDB = new Yak_Trak_SQLite(this);
        localDB.insertTideData();
      //  String a = localDB.getTideFromDataBase("Richmond, CA "); // For testing
        final String title = "Trakker";

        debug = true;
        if(debug == true){
            testDatabase();
        }

        Calendar myCal = Calendar.getInstance();
        String routeDate = new StringBuilder().append(String.valueOf(myCal.get(Calendar.MONTH))).append("-").append(String.valueOf(myCal.get(Calendar.DATE))).append("-").append(String.valueOf(myCal.get(Calendar.YEAR))).toString();

        TextView timeText = (TextView) findViewById(R.id.time_window);
        TextView speedText = (TextView) findViewById(R.id.speed_window);
        TextView distanceText = (TextView) findViewById(R.id.details_window);
        distanceText.setText(getResources().getString(R.string.key_distanceDefault));
        speedText.setText(getResources().getString(R.string.key_timeDefault));
        timeText.setText(getResources().getString(R.string.key_speedDefault));

        mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                R.string.key_drawerOpen,
                R.string.key_drawerClose){


            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                getActionBar().setTitle(title);
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View view){
                super.onDrawerOpened(view);
                getActionBar().setTitle(title);
                invalidateOptionsMenu();
            }

        };

        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        openDrawer = (ToggleButton) findViewById(R.id.drawer_button);
        openDrawer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked == true){
                mDrawer.openDrawer(GravityCompat.START);
            }
            else{
                mDrawer.openDrawer(GravityCompat.START);
            }
        }
    });


    }

    void testDatabase(){
        //39, -123
        int tempLat = 39;
        int tempLong = -123;
        Routes currentRoute = new Routes();
        Coordinates currentCoord;

        currentRoute.setRoute_name(getResources().getString(R.string.key_currentRoute));
        localDB.addRouteIntoDataBase(currentRoute);
        for(int i = 0; i < 10; i++){
            currentCoord  = new Coordinates();
            currentCoord.setRoute_name(getResources().getString(R.string.key_currentRoute));
            currentCoord.setLatitude(tempLat+i);
            currentCoord.setLongitude(tempLong+i);
            localDB.addCoordinateIntoDataBase(currentCoord);
        }
        return;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        boolean drawerOpen = mDrawer.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.start_stop).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public void checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //Asking user
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            else{

            }
        }
        return;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);

            }

        }
        else{
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


        startStop = (ToggleButton)findViewById(R.id.start_stop);
        startStop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    //If button is pressed, place marker at last known location so long as it is not null
                    if(mLastLocation != null){
                        myRoute.add(mLastLocation);
                        makeMarker(mLastLocation);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                        if(myBundle == null) {
                            myCalendar = Calendar.getInstance();
                            startSec = myCalendar.get(Calendar.SECOND);
                            startMin = myCalendar.get(Calendar.MINUTE);
                            startHour = myCalendar.get(Calendar.HOUR_OF_DAY);
                        }
                    }
                    routeStarted = true;
                }
                else{
                    if(mLastLocation != null){
                        myRoute.add(mLastLocation);
                    }
                    //routeFinished();
                    routeStarted = false;
                }
            }
        });

        if(myBundle == null) {
            myCalendar = Calendar.getInstance();
            startSec = myCalendar.get(Calendar.SECOND);
            startMin = myCalendar.get(Calendar.MINUTE);
            startHour = myCalendar.get(Calendar.HOUR_OF_DAY);
        }
        else{
            if(myBundle.containsKey(getResources().getString(R.string.key_hour))){
                startHour = myBundle.getInt(getResources().getString(R.string.key_hour));
            }
            if(myBundle.containsKey(getResources().getString(R.string.key_min))){
                startMin = myBundle.getInt(getResources().getString(R.string.key_min));
            }
            if(myBundle.containsKey(getResources().getString(R.string.key_sec))){
                startSec = myBundle.getInt(getResources().getString(R.string.key_sec));
            }
            if(myBundle.containsKey(getResources().getString(R.string.key_routeStarted))){
                routeStarted = myBundle.getBoolean(getResources().getString(R.string.key_routeStarted));
            }
            if(myBundle.containsKey(getResources().getString(R.string.key_currentRoute))){
                routeNameTransfer = myBundle.getString(getResources().getString(R.string.key_currentRoute));
                myBundle.remove(getResources().getString(R.string.key_currentRoute));
            }
            if(myBundle.containsKey(getResources().getString(R.string.key_route))){
                myRoute = myBundle.getParcelableArrayList(getResources().getString(R.string.key_route));
                if(localDB.findRouteInDataBase(routeNameTransfer) == true){
                    Routes tempRoute = new Routes();
                    tempRoute.setRoute_name(routeNameTransfer);
                    List<Coordinates> tempCoordList = localDB.getCoordinatesInRoute(tempRoute);
                    for(int i = 0; i < tempCoordList.size(); i++){
                        Coordinates tempCoord = tempCoordList.get(i);
                        Location tempLoc = new Location(new StringBuilder().append(tempCoord.getLatitude()).append(", ").append(tempCoord.getLongitude()).toString());
                        tempLoc.setLongitude(tempCoord.getLongitude());
                        tempLoc.setLatitude(tempCoord.getLatitude());
                        myRoute.add(tempLoc);
                    }
                }
                if(myRoute.isEmpty() == false){
                    for(int i = 0; i < myRoute.size(); i++){
                        Location temp = myRoute.get(i);
                        makeMarker(temp);
                    }
                }
            }
        }


        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng one = new LatLng(-30, 145);
        mMap.addMarker(new MarkerOptions().position(one).title("One"));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */

    }

    protected synchronized void buildGoogleApiClient() {
        if(mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
            mGoogleApiClient.connect();

        }
    }

    @Override
    public void onConnected(@Nullable Bundle savedInstanceState) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Ask for permissions here
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (routeStarted == true) {
            myRoute.add(location);
            makeMarker(mLastLocation);
            setDistance();
            setTime();
            setSpeed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(getResources().getString(R.string.key_route), myRoute);
        savedInstanceState.putBoolean(getResources().getString(R.string.key_routeStarted), routeStarted);
        savedInstanceState.putInt(getResources().getString(R.string.key_hour), startHour);
        savedInstanceState.putInt(getResources().getString(R.string.key_min), startMin);
        savedInstanceState.putInt(getResources().getString(R.string.key_sec), startSec);

    }
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        myBundle = savedInstanceState;
    }

    @Override
    protected void onPause(){
        super.onPause();

    }

    public void makeMarker(Location current){

        double newLat = current.getLatitude();
        double newLong = current.getLongitude();
        String newLatStr = Double.toString(newLat);
        String newLongStr = Double.toString(newLong);
        String titleStr = new StringBuilder().append(newLatStr).append(", ").append(newLongStr).toString();
        LatLng latLng = new LatLng(current.getLatitude(), current.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(titleStr);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        if(mMap != null) {
            currLocationMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Override
    public Status getStatus() {
        return null;
    }


    public void setDistance(){
        myDistance = 0;
        Location temp1, temp2;
        TextView distanceText = (TextView) findViewById(R.id.details_window);
        if(myRoute.size() > 1) {
            for (int i = 1; i < myRoute.size(); i++) {
                temp1 = myRoute.get(i - 1);
                temp2 = myRoute.get(i);
                myDistance = myDistance + temp1.distanceTo(temp2);
            }
        }
        else{
            myDistance = 0;
        }
        int tempDistance = (int) myDistance;
        String distanceString;
        if(myDistance/1000 != 0){
            distanceString = Integer.toString(tempDistance/1000);
            distanceString = new StringBuilder().append(getResources().getString(R.string.key_distanceHeader)).append(" ").append(distanceString).append(getResources().getString(R.string.key_kilometers)).toString();
        }
        else {
            distanceString = Integer.toString(tempDistance);
            distanceString = new StringBuilder().append(getResources().getString(R.string.key_distanceHeader)).append(" ").append(distanceString).append(getResources().getString(R.string.key_meters)).toString();

        }
        distanceText.setText(distanceString);
        return;
    }

    public void setTime(){
        myCalendar = Calendar.getInstance();
        int curSec = myCalendar.get(Calendar.SECOND);
        int curMin = myCalendar.get(Calendar.MINUTE);
        int curHour = myCalendar.get(Calendar.HOUR_OF_DAY);
        TextView timeText = (TextView)findViewById(R.id.time_window);

        curSec = curSec - startSec;
        curMin = curMin - startMin;
        curHour = curHour - startHour;

        String timeString = new StringBuilder().append("Error").toString();

        if(curHour > 0 && curMin > 0 && curSec > 0){
            String hourString = Integer.toString(curHour);
            String minString = Integer.toString(curMin);
            String secString = Integer.toString(curSec);

            timeString = new StringBuilder().append(getResources().getString(R.string.key_timeHeader)).append(" ").append(hourString).append(getResources().getString(R.string.key_hour)).append(" ").append(minString).append(getResources().getString(R.string.key_min)).append(" ").append(secString).append(getResources().getString(R.string.key_sec)).toString();

        }

        if(curHour > 0 && curMin > 0 && curSec <= 0){
            String hourString = Integer.toString(curHour);
            String minString = Integer.toString(curMin);
            timeString = new StringBuilder().append(getResources().getString(R.string.key_timeHeader)).append(" ").append(hourString).append(getResources().getString(R.string.key_hour)).append(" ").append(minString).append(getResources().getString(R.string.key_min)).toString();
        }

        if(curHour > 0 && curMin <= 0 && curSec <= 0){
            String hourString = Integer.toString(curHour);
            timeString = new StringBuilder().append(getResources().getString(R.string.key_timeHeader)).append(" ").append(hourString).append(getResources().getString(R.string.key_hour)).toString();
        }

        if(curHour <= 0 && curMin > 0 && curSec > 0){
            String minString = Integer.toString(curMin);
            String secString = Integer.toString(curSec);
            timeString = new StringBuilder().append(getResources().getString(R.string.key_timeHeader)).append(" ").append(minString).append(getResources().getString(R.string.key_min)).append(" ").append(secString).append(getResources().getString(R.string.key_sec)).toString();

        }

        if(curHour <= 0 && curMin > 0 && curSec <= 0){
            String minString = Integer.toString(curMin);
            timeString = new StringBuilder().append(getResources().getString(R.string.key_timeHeader)).append(" ").append(minString).append(getResources().getString(R.string.key_min)).toString();
        }

        if(curHour <= 0 && curMin <= 0 && curSec > 0){
            String secString = Integer.toString(curSec);
            timeString = new StringBuilder().append(getResources().getString(R.string.key_timeHeader)).append(" ").append(secString).append(getResources().getString(R.string.key_sec)).toString();
        }

        timeText.setText(timeString);
        return;

    }

    public void setSpeed(){
        int curSec = myCalendar.get(Calendar.SECOND);
        int curMin = myCalendar.get(Calendar.MINUTE);
        int curHour = myCalendar.get(Calendar.HOUR_OF_DAY);
        TextView speedText = (TextView)findViewById(R.id.speed_window);

        curSec = curSec - startSec;
        curMin = curMin - startMin;
        curHour = curHour - startHour;

        if(curHour <= 0){
            curHour = 0;
        }
        if(curMin <= 0){
            curHour = 0;
        }
        curMin = curMin*60;
        curHour = curHour*3600;
        int time = curSec + curMin + curHour;

        double localDist = 0;
        Location temp1, temp2;
        if(myRoute.size() > 1) {
            for (int i = 1; i < myRoute.size(); i++) {
                temp1 = myRoute.get(i - 1);
                temp2 = myRoute.get(i);
                localDist = localDist + temp1.distanceTo(temp2);
            }
        }
        int speed = (int) (localDist/time);
        String speedString = Integer.toString(speed);
        String speedSet = new StringBuilder().append(getResources().getString(R.string.key_speedHeader)).append(" ").append(speedString).append(" m/s").toString();
        speedText.setText(speedSet);
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_yakker_trakker_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.save_route){
            routeDialog();

        } else if (id == R.id.weather){
            weatherFragment fragment = new weatherFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.tides){
            tidesFragment fragment = new tidesFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.settings){
            /*
            settingsFragment fragment = new settingsFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
            */

        } else if (id == R.id.routes_access){
            savedRoutesFragment fragment = new savedRoutesFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.open_map){
            final android.support.v4.app.Fragment fragment = new Fragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.refresh_map){
            refreshMap();
        }
        openDrawer.setChecked(true);
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void refreshMap(){
        if(mMap != null && myRoute.size() > 0){
            /*
            mMap.clear();
            for(int i = 0; i < myRoute.size(); i++){
                makeMarker(myRoute.get(i));
            }
            */
            LatLng temp1 = new LatLng(myRoute.get(0).getLatitude(), myRoute.get(0).getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(temp1));

        }
        return;
    }

    private void routeFinished(String name, String comments){
        String routeName = name;
        String routeComment = comments;
        Calendar myCal = Calendar.getInstance();
        String routeDate = new StringBuilder().append(String.valueOf(myCal.get(Calendar.MONTH))).append("/").append(String.valueOf(myCal.get(Calendar.DATE))).append("/").append(String.valueOf(myCal.get(Calendar.YEAR))).toString();

        if(routeComment == ""){
            routeComment = getResources().getString(R.string.key_defaultComment);
        }
        Coordinates curCoord;
        Routes curRoute = new Routes(routeName, routeDate, routeComment);
        localDB.addRouteIntoDataBase(curRoute);
        for(int i = 0; i < myRoute.size(); i++){
            Location temp = myRoute.get(i);
            curCoord = new Coordinates(temp.getLatitude(), temp.getLongitude(), routeName);
            localDB.addCoordinateIntoDataBase(curCoord);
        }

        TextView timeText = (TextView) findViewById(R.id.time_window);
        TextView speedText = (TextView) findViewById(R.id.speed_window);
        TextView distanceText = (TextView) findViewById(R.id.details_window);
        distanceText.setText(getResources().getString(R.string.key_distanceDefault));
        speedText.setText(getResources().getString(R.string.key_timeDefault));
        timeText.setText(getResources().getString(R.string.key_speedDefault));

        startStop.setChecked(false);
        routeStarted = false;
        mMap.clear();
        myRoute.clear();
        return;
    }

    private void routeDialog(){
        final EditText input = new EditText(YakkerTrakkerActivity.this);
        LinearLayout.LayoutParams oneInput = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(oneInput);
        final AlertDialog dialog = new AlertDialog.Builder(YakkerTrakkerActivity.this).create();
        dialog.setView(input);
        dialog.setTitle("Enter a Route Name");
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save Route", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Add Comments", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(input.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Name Required", Toast.LENGTH_SHORT).show();

                }else {
                    routeFinished(input.getText().toString(), "");
                    dialog.dismiss();
                }
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(input.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Name Required", Toast.LENGTH_SHORT).show();

                }else {
                    dialog.dismiss();
                    commentDialog(input.getText().toString());
                }
            }
        });

    }

    private void commentDialog(String name){
        final String localName = name;
        final String localComment = "";
        final EditText input = new EditText(YakkerTrakkerActivity.this);
        LinearLayout.LayoutParams oneInput = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(oneInput);
        AlertDialog dialog = new AlertDialog.Builder(YakkerTrakkerActivity.this).create();
        dialog.setView(input);
        dialog.setTitle("Enter some comments");
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save Route", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(TextUtils.isEmpty(input.getText().toString())){
                    routeFinished(localName, localComment);
                }
                else{
                    routeFinished(localName, input.getText().toString());
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    //End Main


}
//This is my branch