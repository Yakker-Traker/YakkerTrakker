package com.yakkertrakker.www.yakkertrakker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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



    //Array of Strings;
    String [] testArray = {"Cat", "Dog", "Mouse","Austin"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Yak_Trak_SQLite db = new Yak_Trak_SQLite(getActivity());
        // For Testing Purposes.
        Routes a  = new Routes ("Route1","04/23/1232","It was a hot day");
        Routes b = new Routes ("Route2", "04/21/1998","It was a cold day");
        db.addRouteIntoDataBase(a);
        db.addRouteIntoDataBase(b);
      //  db.deleteRouteFromDataBase(a.getRoute_name());
      //  db.deleteRouteFromDataBase(b.getRoute_name());



        List<Routes> rList =  db.getAllRoutesFromDataBase();

        int size = rList.size();
        String [] routeNames = new String [size];

        for (int i = 0; i <rList.size(); i++){
            Routes temp = rList.get(i);
            routeNames [i] = temp.getRoute_name();
        }








        View view = inflater.inflate(R.layout.fragment_saved_rotues, container, false);

        if (size == 0) {
            String [] noRoutes = {"You currently do not have any saved routes."};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.rowlayout, R.id.textView, noRoutes);
            setListAdapter(adapter);
        }
        else{
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.rowlayout, R.id.textView, routeNames);
            setListAdapter(adapter);

        }






        return view;
    }



}








