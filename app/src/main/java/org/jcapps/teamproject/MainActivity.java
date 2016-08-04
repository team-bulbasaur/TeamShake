package org.jcapps.teamproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.yelp.clientlib.entities.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    public String mlatitude, mlongitude;
    public Map dbMap = new HashMap<String, String>();
    public Map paramsMap = new HashMap<String, String>();
    public ArrayList<Restaurant> restaurants = new ArrayList<>();
    public RestaurantListAdapter restaurantListAdapter;
    public ListView restaurantListView;
    public Intent filterIntent;
    public UserDBHelper db;
    public AdapterView.OnItemClickListener restaurantClickListener;
    public Intent detailIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detailIntent = new Intent(MainActivity.this, DetailActivity.class);

        yelp();
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

        // get db, grab filters, tack on latlong, query yelp
        db = UserDBHelper.getInstance(this);
        dbMap = db.getFilter();
        Integer sort = Integer.parseInt(dbMap.get("sort").toString());
        Integer radius_filter = Integer.parseInt(dbMap.get("radius_filter").toString());
        String category_filter = dbMap.get("category_filter").toString();

        Toast.makeText(this, sort + " " + radius_filter + " " + category_filter, Toast.LENGTH_SHORT).show();

        paramsMap.put("latitude", mlatitude);
        paramsMap.put("longitude", mlongitude);

        // if no category_filter specified, don't pass it to yelp, otherwise do
        if (!category_filter.equals(""))
            paramsMap.put("category_filter", category_filter.toString());

        // if radius filter = 11 (unlimited), don't pass it, otherwise do
        if (radius_filter != 11) {
            // convert miles to meters
            radius_filter *= 1600;
            paramsMap.put("radius_filter", radius_filter.toString());
        }

        paramsMap.put("sort", sort.toString());

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
            return Yelper.search(params);
        }

        protected void onPostExecute(SearchResponse searchResponse) {
            restaurants = Yelper.constructRestaurants(searchResponse);
            RestaurantListAdapter restaurantListAdapter = new RestaurantListAdapter(mContext, restaurants);
            ListView restaurantListView = (ListView) findViewById(R.id.restaurant_card_list_view);
            restaurantListView.setAdapter(restaurantListAdapter);

            restaurantClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Restaurant restaurant = restaurants.get(position);
                    detailIntent.putExtra("RESTAURANT", restaurant);
                    startActivity(detailIntent);
                }
            };
            restaurantListView.setOnItemClickListener(restaurantClickListener);

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

    @Override
    protected void onStop() {
        GPS.sharedInstance(MainActivity.this).stopUsingGPS();
        super.onStop();
    }
}