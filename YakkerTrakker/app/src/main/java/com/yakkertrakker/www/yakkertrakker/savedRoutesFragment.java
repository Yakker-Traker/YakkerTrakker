package com.yakkertrakker.www.yakkertrakker;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import SQLite.Coordinates;
import SQLite.Routes;
import SQLite.Yak_Trak_SQLite;


/**
 * A simple {@link Fragment} subclass.
 */
public class savedRoutesFragment extends ListFragment {
    public savedRoutesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Yak_Trak_SQLite db = new Yak_Trak_SQLite(getActivity());
        // For Testing Purposes.
        Routes a  = new Routes ("Route1","04/23/1232","It was a hot day");
        Routes b = new Routes ("Route2", "04/21/1998","It was a cold day");
        db.addRouteIntoDataBase(a);
        db.addRouteIntoDataBase(b);




        List<Routes> rList =  db.getAllRoutesFromDataBase();

        int size = rList.size();
        String [] routeNames = new String [size];
        for (int i = 0; i <rList.size(); i++) {
            Routes temp = rList.get(i);
            routeNames[i] = temp.getRoute_name() + ":\t " + temp.getDate_created() + "\n\t"+ temp.getComments();
        }

        View view = inflater.inflate(R.layout.fragment_saved_rotues, container, false);

        if (size == 0) {
            String [] noRoutes = {"You currently do not have any saved routes."};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.rowlayout, R.id.RouteNametb, noRoutes);
            setListAdapter(adapter);
        }
        else{
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.rowlayout, R.id.RouteNametb, routeNames);
            setListAdapter(adapter);
        }

        return view;
    }


}








