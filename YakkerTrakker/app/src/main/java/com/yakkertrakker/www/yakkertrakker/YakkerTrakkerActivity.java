package com.yakkertrakker.www.yakkertrakker;


import java.util.List;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;

import SQLite.Routes;
import SQLite.Coordinates;
import SQLite.Yak_Trak_SQLite;


public class YakkerTrakkerActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, com.google.android.gms.common.api.Result {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker currLocationMarker;
    LocationRequest mLocationRequest;
    ToggleButton startStop;
    Boolean routeStarted;
    ArrayList<Location> route = new ArrayList<Location>();
    Routes curRoute = new Routes("TestRoute", "4-25-17", "");
    //Yak_Trak_SQLite localDB;

    Yak_Trak_SQLite localDB = new Yak_Trak_SQLite(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yakker_trakker);
        routeStarted = false;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
                        //Push location to database here
                        route.add(mLastLocation);
                        //Location LatLng pushed to ArrayList object
                        makeMarker(mLastLocation);
                        //markers.add(newMarker);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                    }
                    routeStarted = true;
                }
                else{
                    if(mLastLocation != null){
                        //Push location to database here
                        route.add(mLastLocation);
                        //Location LatLng pushed to ArrayList object
                        mMap.clear();
                        //markers.add(newMarker);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));
                    }
                    routeStarted = false;
                }
            }
        });

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
        if(routeStarted == true && mLastLocation != null) {

            route.add(mLastLocation);
            makeMarker(mLastLocation);

            //route.add(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            //currLocationMarker = mMap.addMarker(markerOptions);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(getResources().getString(R.string.routes_string), route);
        if(routeStarted == true) {
            savedInstanceState.putBoolean(getResources().getString(R.string.started_string), routeStarted);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onResume(){
        super.onResume();
        //Toast.makeText(this, "onResume Called", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPause(){
        super.onPause();
        //Toast.makeText(this, "Goodbye", Toast.LENGTH_SHORT).show();

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

    //End Main
}
