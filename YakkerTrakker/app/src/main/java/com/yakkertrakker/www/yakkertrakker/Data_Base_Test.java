package com.yakkertrakker.www.yakkertrakker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import SQLite.Coordinates;
import SQLite.Routes;
import SQLite.Yak_Trak_SQLite;

public class Data_Base_Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data__base__test);
        DataBaseTest();
    }

    public void DataBaseTest(){
        final Yak_Trak_SQLite db = new Yak_Trak_SQLite(this);
        db.getWritableDatabase();
        Button routeButton = (Button)findViewById(R.id.insertRouteButton);
        Button generateRandom = (Button)findViewById(R.id.generateRandomCooridantes_Button);
        Button deleteButton = (Button)findViewById(R.id.deleteRouteButton);
        final EditText route_name = (EditText)findViewById(R.id.route_name_edit);
        final EditText route_date = (EditText)findViewById(R.id.route_date_edit);
        final EditText route_comments = (EditText)findViewById(R.id.route_comment_edit);
        final EditText route_random = (EditText)findViewById(R.id.route_name_random);
        final EditText route_delete = (EditText)findViewById(R.id.route_name_delete);
        final TextView data_message = (TextView)findViewById(R.id.data_message_view);
        data_message.setText("");

        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = route_name.getText().toString();
                String b = route_date.getText().toString();
                String c = route_comments.getText().toString();
                if ((a.length() > 0 ) && (b.length() >0)) {
                    Routes route = new Routes(a, b, c);
                    db.addRouteIntoDataBase(route);
                }
                data_message.setText("The Route was inserted");

            }
        });

        generateRandom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                String a = route_random.getText().toString();
                if (a.length () > 0 && db.findRouteInDataBase(a)){
                    for (int x = 0; x < 10; x++) {
                        Coordinates c = new Coordinates(1.1f * x, 1.2f * x, a);
                        db.addCoordinateIntoDataBase(c);
                    }
                    data_message.setText("Coordinates were generated for the given route.");
                }

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                String a = route_delete.getText().toString();
                db.deleteRouteFromDataBase(a);

                data_message.setText("The route and its coordinates were deleted.");

            }
        });


    }

    public void dbTest(){
        Yak_Trak_SQLite db = new Yak_Trak_SQLite(this);
        db.getWritableDatabase();
        Routes r1 = new Routes ("test1_route","01/12/12", "comments");
        db.addRouteIntoDataBase(r1);
        for (int x = 0; x < 10; x++) {
            Coordinates c = new Coordinates(123.234f * x, 213.421f * x, "test1_route");
            db.addCoordinateIntoDataBase(c);
        }
        Routes r2 = new Routes ("test2_route","01/12/12", "comments");
        db.addRouteIntoDataBase(r2);
        for (int x = 0; x < 10; x++) {
            Coordinates c = new Coordinates(123.234f * x, 213.421f * x, "test2_route");
            db.addCoordinateIntoDataBase(c);
        }

        List<Coordinates> list = db.getCoordinatesInRoute(r1);
        db.deleteRouteFromDataBase(r1.getRoute_name());
        db.deleteRouteFromDataBase(r2.getRoute_name());
    }

}
