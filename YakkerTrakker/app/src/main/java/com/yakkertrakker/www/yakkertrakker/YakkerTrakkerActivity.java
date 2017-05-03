package com.yakkertrakker.www.yakkertrakker;


import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    Button clear;

    Yak_Trak_SQLite localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yakker_trakker);
        routeStarted = false;
        localDB = new Yak_Trak_SQLite(this);
        final String title = "Trakker";


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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);







        // Button added to clear Fragments can be deleted later after figureing
        // out how to control the back button.
        clear = (Button) findViewById(R.id.clearButton);
        clear.setVisibility(View.GONE);
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clear.setVisibility(View.GONE);


                final android.support.v4.app.Fragment fragment = new Fragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
            }
        });









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
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
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
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                    }
                    routeStarted = true;
                }
                else{
                    if(mLastLocation != null){
                        myRoute.add(mLastLocation);
                        mMap.clear();
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));
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
        }

        if(myBundle != null){
            if(myBundle.containsKey(getResources().getString(R.string.key_route))){
                myRoute = myBundle.getParcelableArrayList(getResources().getString(R.string.key_route));
                if(myRoute.isEmpty() == false){
                    for(int i = 0; i < myRoute.size(); i++){
                        Location temp = myRoute.get(i);
                        makeMarker(temp);
                    }

                }
            }
            if(myBundle.containsKey(getResources().getString(R.string.key_routeStarted))){
                routeStarted = myBundle.getBoolean(getResources().getString(R.string.key_routeStarted));
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
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        if(mMap != null) {
            currLocationMarker = mMap.addMarker(markerOptions);
        }
    }

    @Override
    public Status getStatus() {
        return null;
    }

    public void routeFinished(){
        //Details set to defaults
        String routeName = getResources().getString(R.string.key_defaultRoute);
        String routeDate = getResources().getString(R.string.key_defaultDate);
        String routeComment = getResources().getString(R.string.key_defaultComment);

        //Pop dialogue to get Route Name and comments here
        Dialog nameDialog = new Dialog(this);
        nameDialog.show();

        Coordinates curCoord;
        Routes curRoute = new Routes(routeName, routeDate, routeComment);
        localDB.addRouteIntoDataBase(curRoute);
        for(int i = 0; i < myRoute.size(); i++){
            Location temp = myRoute.get(i);
            curCoord = new Coordinates(temp.getLatitude(), temp.getLongitude(), routeName);
            localDB.addCoordinateIntoDataBase(curCoord);
        }
        myRoute.clear();
        return;
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
        int tempDistance = (int) myDistance;
        String distanceString = Integer.toString(tempDistance);
        distanceString = new StringBuilder().append(distanceString).append(getResources().getString(R.string.key_meters)).toString();
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

            timeString = new StringBuilder().append(hourString).append(getResources().getString(R.string.key_hour)).append(" ").append(minString).append(getResources().getString(R.string.key_min)).append(" ").append(secString).append(getResources().getString(R.string.key_sec)).toString();

        }

        if(curHour > 0 && curMin > 0 && curSec <= 0){
            String hourString = Integer.toString(curHour);
            String minString = Integer.toString(curMin);
            timeString = new StringBuilder().append(hourString).append(getResources().getString(R.string.key_hour)).append(" ").append(minString).append(getResources().getString(R.string.key_min)).toString();
        }

        if(curHour > 0 && curMin <= 0 && curSec <= 0){
            String hourString = Integer.toString(curHour);
            timeString = new StringBuilder().append(hourString).append(getResources().getString(R.string.key_hour)).toString();
        }

        if(curHour <= 0 && curMin > 0 && curSec > 0){
            String minString = Integer.toString(curMin);
            String secString = Integer.toString(curSec);
            timeString = new StringBuilder().append(minString).append(getResources().getString(R.string.key_min)).append(" ").append(secString).append(getResources().getString(R.string.key_sec)).toString();

        }

        if(curHour <= 0 && curMin > 0 && curSec <= 0){
            String minString = Integer.toString(curMin);
            timeString = new StringBuilder().append(minString).append(getResources().getString(R.string.key_min)).toString();
        }

        if(curHour <= 0 && curMin <= 0 && curSec > 0){
            String secString = Integer.toString(curSec);
            timeString = new StringBuilder().append(secString).append(getResources().getString(R.string.key_sec)).toString();
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
        String speedSet = new StringBuilder().append(speedString).append(" m/s").toString();
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
            Toast.makeText(getApplicationContext(), "Save Route", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.weather){
            weatherFragment fragment = new weatherFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();

            clear.setVisibility(View.VISIBLE);



        } else if (id == R.id.tides){
            Toast.makeText(getApplicationContext(), "Tides", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.settings){
            Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
            settingsFragment sfragment = new settingsFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, sfragment);
            fragmentTransaction.commit();
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //End Main


}
//This is my branch.