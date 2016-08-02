package org.jcapps.teamproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.yelp.clientlib.entities.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    public String mlatitude, mlongitude;
    public Map paramsMap = new HashMap<String, String>();
    public ArrayList<Restaurant> restaurants = new ArrayList<>();
    public RestaurantListAdapter restaurantListAdapter;
    public ListView restaurantListView;
    public Intent filterIntent;

//>>>>>>>>>>>>>>>>>>>>
    public String mdistance;
    Double dist = 0.0;
//<<<<<<<<<<<<<<<<<<<<

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_filter:
                filterIntent = new Intent(MainActivity.this, FilterActivity.class);
                startActivity(filterIntent);
                //Toast.makeText(getApplicationContext(), "Will go to the filter screen", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_food:
                yelp();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void yelp() {

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

        // TODO: get search parameters from db
        //paramsMap.put("term", "food");
        //paramsMap.put("limit", "20");         // limit = 1-20, but can start new query from 21-40, etc.
        paramsMap.put("sort", "1");             // 0 = Best Match; 1 = Distance; 2 = Highest Rated
        // paramsMap.put("radius_filter", "800");  // meters - sorting on distance, so don't need this
        paramsMap.put("category_filter", "restaurants");

        YelpTask yelpTask = new YelpTask(this);
        yelpTask.execute(paramsMap);
    }

    private class YelpTask extends AsyncTask<Map, Void, SearchResponse> {

        private Context mContext;
        public YelpTask (Context context) {
            mContext = context;
        }

        @Override
        protected SearchResponse doInBackground(Map... parameters) {
            // first element of parameters is our map
            Map<String, String> params = parameters[0];
            return Yelper.search(paramsMap);
        }

        protected void onPostExecute(SearchResponse searchResponse) {
            restaurants = Yelper.constructRestaurants(searchResponse);
            RestaurantListAdapter restaurantListAdapter = new RestaurantListAdapter(mContext, restaurants);
            ListView restaurantListView = (ListView) findViewById(R.id.restaurant_card_list_view);
            restaurantListView.setAdapter(restaurantListAdapter);


//            tvLatitude.setText(mlatitude);
//            tvLongitude.setText(mlongitude);
//
//            StringBuilder sb = new StringBuilder();
//            for (int inc = 0; inc < restaurants.size(); inc++) {
////>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//                dist = distance(Double.parseDouble(mlatitude),
//                                Double.parseDouble(mlongitude),
//                                restaurants.get(inc).getLatitude(),
//                                restaurants.get(inc).getLongitude());
//                if (dist > 1) {
//                    mdistance = "distance: " + String.format("%.2f", dist) + " miles";
//                } else {
//                    dist = dist * 5280;
//                    mdistance = "distance: " + String.format("%.2f", dist) + " feet";
//                }
////<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//                sb.append(restaurants.get(inc).getName());
//                sb.append("\n");
//                sb.append(restaurants.get(inc).getLatitude());
//                sb.append(" , ");
//                sb.append(restaurants.get(inc).getLongitude());
//                sb.append("\n");
////>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//                sb.append(mdistance);
////<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//                sb.append("\n");
//
//            }
//            tvInfo.setText(sb.toString());
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