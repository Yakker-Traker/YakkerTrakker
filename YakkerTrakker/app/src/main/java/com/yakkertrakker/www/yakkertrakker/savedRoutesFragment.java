package com.yakkertrakker.www.yakkertrakker;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import SQLite.Coordinates;
import SQLite.Routes;
import SQLite.Yak_Trak_SQLite;


/**
 * A simple {@link Fragment} subclass.
 */
public class savedRoutesFragment extends ListFragment implements OnItemClickListener {
    public savedRoutesFragment() {
        // Required empty public constructor
    }


    ArrayList<String> selected = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Yak_Trak_SQLite db = new Yak_Trak_SQLite(getActivity());
        // For Testing Purposes.
        //  Routes a = new Routes("Route1", "04/23/1232", "It was a hot day");
        //  Routes b = new Routes("Route2", "04/21/1998", "It was a cold day");

        //db.addRouteIntoDataBase(a);


        Routes c = new Routes("Delete Selected Routes", "", "");
        boolean found = db.findRouteInDataBase("Delete Selected Routes");
        if (found == false)
            db.addRouteIntoDataBase(c);
        else {
            db.deleteRouteFromDataBase(c.getRoute_name());
            db.addRouteIntoDataBase(c);

        }
        List<Routes> rList = db.getAllRoutesFromDataBase();
        int size = rList.size();
        String[] routeNames = new String[size];
        for (int i = 0; i < rList.size(); i++) {
            Routes temp = rList.get(i);
            routeNames[i] = temp.getRoute_name() + "\t " + temp.getDate_created() + "\n\t" + temp.getComments();
            selected.add("0");
        }
        final View view = inflater.inflate(R.layout.fragment_saved_rotues, container, false);

        if (size == 0) {
            String[] noRoutes = {"You currently do not have any saved routes."};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.rowlayout, R.id.RouteNametb, noRoutes);
            setListAdapter(adapter);
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.rowlayout, R.id.RouteNametb, routeNames);
            setListAdapter(adapter);
        }


        return view;
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {

        //---------------------------------------

        final AlertDialog dialog = new AlertDialog.Builder(savedRoutesFragment.this.getContext()).create();

        dialog.setTitle("Enter a Route Name");
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Set as Current", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList(parent, view, position, id);
                dialog.dismiss();
            }
        });

        //---------------------------------------
/*

        Yak_Trak_SQLite db = new Yak_Trak_SQLite(getActivity());
        List<Routes> rList = db.getAllRoutesFromDataBase();
        int size = rList.size();

        if (position != size - 1 && selected.get(position) == "0") {
            TextView r = (TextView) view.findViewById(R.id.RouteNametb);
            r.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            selected.set(position,"1");

        }
        else {
            TextView r = (TextView) view.findViewById(R.id.RouteNametb);
            r.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            selected.set(position,"0");
        }

        Routes c = new Routes("Delete Selected Routes", "", "");
        boolean found =db.findRouteInDataBase("Delete Selected Routes");
        if (found==false)
            db.addRouteIntoDataBase(c);
        else {
            db.deleteRouteFromDataBase(c.getRoute_name());
            db.addRouteIntoDataBase(c);

        }
        rList = db.getAllRoutesFromDataBase();
        size = rList.size();
        String[] routeNames = new String[size];
        for (int i = 0; i < rList.size(); i++) {
            Routes temp = rList.get(i);
            routeNames[i] = temp.getRoute_name() + "\t " + temp.getDate_created() + "\n\t" + temp.getComments();
        }


        if (position == size - 1){
            for (int i = 0; i < selected.size()-1; i++){
                    String a = selected.get(i);
                    String b = "1";
                    if (a.equals(b)){
                        db.deleteRouteFromDataBase(rList.get(i).getRoute_name());
                    }

            }
            if (size-1 != 0)
                Toast.makeText(getActivity(), "Routes were sucessfully deleted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "No routes to delete", Toast.LENGTH_SHORT).show();


            rList = db.getAllRoutesFromDataBase();
            size = rList.size();
            routeNames = new String[size];
            for (int i = 0; i < rList.size(); i++) {
                Routes temp = rList.get(i);
                routeNames[i] = temp.getRoute_name() + ":\t " + temp.getDate_created() + "\n\t" + temp.getComments();
            }



            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.rowlayout, R.id.RouteNametb, routeNames);
            setListAdapter(adapter);
        }

*/

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemClickListener(this);


    }

    private void refreshList(AdapterView<?> parent, View view, final int position, long id) {


        Yak_Trak_SQLite db = new Yak_Trak_SQLite(getActivity());
        List<Routes> rList = db.getAllRoutesFromDataBase();
        int size = rList.size();

        if (position != size - 1 && selected.get(position) == "0") {
            TextView r = (TextView) view.findViewById(R.id.RouteNametb);
            r.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            selected.set(position, "1");

        } else {
            TextView r = (TextView) view.findViewById(R.id.RouteNametb);
            r.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            selected.set(position, "0");
        }

        Routes c = new Routes("Delete Selected Routes", "", "");
        boolean found = db.findRouteInDataBase("Delete Selected Routes");
        if (found == false)
            db.addRouteIntoDataBase(c);
        else {
            db.deleteRouteFromDataBase(c.getRoute_name());
            db.addRouteIntoDataBase(c);

        }
        rList = db.getAllRoutesFromDataBase();
        size = rList.size();
        String[] routeNames = new String[size];
        for (int i = 0; i < rList.size(); i++) {
            Routes temp = rList.get(i);
            routeNames[i] = temp.getRoute_name() + "\t " + temp.getDate_created() + "\n\t" + temp.getComments();
        }


        if (position == size - 1) {
            for (int i = 0; i < selected.size() - 1; i++) {
                String a = selected.get(i);
                String b = "1";
                if (a.equals(b)) {
                    db.deleteRouteFromDataBase(rList.get(i).getRoute_name());
                }

            }
            if (size - 1 != 0)
                Toast.makeText(getActivity(), "Routes were sucessfully deleted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "No routes to delete", Toast.LENGTH_SHORT).show();


            rList = db.getAllRoutesFromDataBase();
            size = rList.size();
            routeNames = new String[size];
            for (int i = 0; i < rList.size(); i++) {
                Routes temp = rList.get(i);
                routeNames[i] = temp.getRoute_name() + ":\t " + temp.getDate_created() + "\n\t" + temp.getComments();
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.rowlayout, R.id.RouteNametb, routeNames);
            setListAdapter(adapter);
        }

    }
}






