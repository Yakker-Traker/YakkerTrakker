package com.yakkertrakker.www.yakkertrakker;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.yakkertrakker.www.yakkertrakker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import SQLite.Yak_Trak_SQLite;


/**
 * A simple {@link Fragment} subclass.
 */
public class tidesFragment extends Fragment {
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyMMdd");
    String beginDate = sdf.format(new java.util.Date());
    String pickStation = "9414290";
    String station = "9414863";
    private String TAG = tidesFragment.class.getSimpleName();
    private android.app.ProgressDialog pDialog;
    private ListView lv;

    private String url = "https://tidesandcurrents.noaa.gov/api/datagetter?begin_date=" +
            beginDate+
            "&range=168&station="
            +station+
            "&product=predictions&datum=MLLW&units=english&time_zone=lst&application=ports_screen&format=json";

    java.util.ArrayList<java.util.HashMap<String, String>> tideList;
    java.util.ArrayList<java.util.HashMap<String, String>> highLowTide;

    public tidesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tides, container, false);

        tideList = new java.util.ArrayList<>();
        highLowTide = new java.util.ArrayList<>();

        lv = (ListView) view.findViewById(R.id.list);

        new GetContacts().execute();

        return view;
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat(inputPattern);
        java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat(outputPattern);

        java.util.Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


private class GetContacts extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(tidesFragment.this.getContext());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url);

        Log.e(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray tide = jsonObj.optJSONArray("predictions");

                for (int i = 0; i < tide.length(); i+=5) {
                    JSONObject c = tide.getJSONObject(i);

                    String tides = c.getString("t");
                    String height = c.getString("v");

                    tides = parseDateToddMMyyyy(tides);

                    HashMap<String, String> water = new HashMap<>();

                    // adding each child node to HashMap key => value
                    water.put("t", tides);
                    water.put("v", height);

                    tideList.add(water);
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });*/

            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });*/

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //super.onPostExecute(result);

        for(int j = 1; j<tideList.size()-1; j++){

            String tester = tideList.get(j-1).get("v");
            String test = tideList.get(j).get("v");
            String testest = tideList.get(j+1).get("v");

            //String Dater = tideList.get(j).get("t");

            Double tallnessFirst = Double.parseDouble(tester);
            Double tallnessMiddle = Double.parseDouble(test);
            Double tallnessLast = Double.parseDouble(testest);

            if (tallnessFirst<=tallnessMiddle && tallnessMiddle>=tallnessLast){

                //hightide
                highLowTide.add(tideList.get(j));
                j+=2;

            }else if(tallnessFirst>=tallnessMiddle && tallnessMiddle<=tallnessLast){

                //lowtide
                highLowTide.add(tideList.get(j));
                j+=2;

            }
        }

        super.onPostExecute(result);

        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();

        ListAdapter adapter = new SimpleAdapter(tidesFragment.this.getContext(), highLowTide,
                R.layout.list_item, new String[]{ "t","v"},
                new int[]{R.id.time, R.id.height});

        lv.setAdapter(adapter);

        lv.setBackgroundColor(Color.LTGRAY);
    }

}

}






