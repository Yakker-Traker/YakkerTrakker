package com.yakkertrakker.www.yakkertrakker;

import java.util.List;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
//import android.support.v4.widget.SearchViewCompatIcs;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import SQLite.Coordinates;
import SQLite.Routes;
import SQLite.Yak_Trak_SQLite;

public class YakkerTrakkerActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yakker_trakker);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    // Starting to implement the main button.
        final Button mainButton;
        mainButton = (Button)findViewById(R.id.main_Button);
        mainButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                if (mainButton.getText() == "Comming Soon !"){
                    mainButton.setText("=");
                }
                else {
                    mainButton.setText("Comming Soon !");
                }
            }

        });



    // Code to test the data base test ignore or delete, not going to be included in the final app.
        Button dbButton;
        dbButton = (Button)findViewById(R.id.DataBaseButtom);
        dbButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendtoDataBase(v);
            }
        });
    }

    public void sendtoDataBase (View view){
       Intent db_intent = new Intent(this, Data_Base_Test.class);
       startActivity(db_intent);

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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng one = new LatLng(-30, 145);
        mMap.addMarker(new MarkerOptions().position(one).title("One"));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
