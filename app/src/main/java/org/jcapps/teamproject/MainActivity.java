package org.jcapps.teamproject;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    public String mlatitude, mlongitude;
    public Map paramsMap = new HashMap<String, String>();
    public ArrayList<Restaurant> restaurants = new ArrayList<>();

//>>>>>>>>>>>>>>>>>>>>
    public String mdistance;
    Double dist = 0.0;
//<<<<<<<<<<<<<<<<<<<<

    @BindView(R.id.latitude)TextView tvLatitude;
    @BindView(R.id.longitude)TextView tvLongitude;
    @BindView(R.id.info)TextView tvInfo;
    @BindView(R.id.btn_yelp)Button btnYelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_yelp) void yelp() {

        boolean permissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (permissionGranted) {
            GPS gps_instance = GPS.sharedInstance(MainActivity.this);
            Location location = gps_instance.getLastKnownLocation();
            mlatitude = String.valueOf(location.getLatitude());
            mlongitude = String.valueOf(location.getLongitude());

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);

        }

        paramsMap.put("latitude", mlatitude);
        paramsMap.put("longitude", mlongitude);

        // TODO: get search parameters from ui / db
        //paramsMap.put("term", "food");
        //paramsMap.put("limit", "20");         // limit = 1-20, but can start new query from 21-40, etc.
        paramsMap.put("sort", "1");             // 0 = Best Match; 1 = Distance; 2 = Highest Rated
        paramsMap.put("radius_filter", "800");  // meters
        paramsMap.put("category_filter", "restaurants");

        YelpTask yelpTask = new YelpTask();
        yelpTask.execute(paramsMap);
    }

    private class YelpTask extends AsyncTask<Map, Void, SearchResponse> {

        @Override
        protected SearchResponse doInBackground(Map... parameters) {
            // first element of parameters is our map
            Map<String, String> params = parameters[0];
            return Yelper.search(paramsMap);
        }

        protected void onPostExecute(SearchResponse searchResponse) {
            restaurants = Yelper.constructRestaurants(searchResponse);

            tvLatitude.setText(mlatitude);
            tvLongitude.setText(mlongitude);

            StringBuilder sb = new StringBuilder();
            for (int inc = 0; inc < restaurants.size(); inc++) {
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                dist = distance(Double.parseDouble(mlatitude),
                                Double.parseDouble(mlongitude),
                                restaurants.get(inc).getLatitude(),
                                restaurants.get(inc).getLongitude());
                if (dist > 1) {
                    mdistance = "distance: " + String.format("%.2f", dist) + " miles";
                } else {
                    dist = dist * 5280;
                    mdistance = "distance: " + String.format("%.2f", dist) + " feet";
                }
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                sb.append(restaurants.get(inc).getName());
                sb.append("\n");
                sb.append(restaurants.get(inc).getLatitude());
                sb.append(" , ");
                sb.append(restaurants.get(inc).getLongitude());
                sb.append("\n");
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                sb.append(mdistance);
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                sb.append("\n");

            }
            tvInfo.setText(sb.toString());
    }
}
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GPS gps_instance = GPS.sharedInstance(MainActivity.this);
//                    Location location = gps_instance.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Location location = gps_instance.getLastKnownLocation();
                    mlatitude = String.valueOf(location.getLatitude());
                    mlongitude = String.valueOf(location.getLongitude());
                }
            }
        }
    }
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // Distance is converted into miles.
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    //*	This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    //*	This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    @Override
    protected void onStop() {
        GPS.sharedInstance(MainActivity.this).stopUsingGPS();
        super.onStop();
    }
}