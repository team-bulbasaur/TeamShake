package org.jcapps.teamproject;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;

public class Yelper {

    public static SearchResponse search(Map params) {

        SearchResponse searchResponse = null;

        // get the lat and long out of & remove them from params
        Double latitude = Double.parseDouble(params.get("latitude").toString());
        Double longitude = Double.parseDouble(params.get("longitude").toString());
        params.remove("latitude");
        params.remove("longitude");

        // build our coordinatOptions object
        CoordinateOptions coordinates = CoordinateOptions.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();

        // yelpAPI oauth w/ keys from gradle.properties file, which is .gitignored
        YelpAPIFactory yelpAPIFactory = new YelpAPIFactory(
                BuildConfig.CONSUMER_KEY,
                BuildConfig.CONSUMER_SECRET,
                BuildConfig.TOKEN,
                BuildConfig.TOKEN_SECRET);
        YelpAPI yelpAPI = yelpAPIFactory.createAPI();

        // make the call
        Call<SearchResponse> call = yelpAPI.search(coordinates, params);
        try {
            searchResponse = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResponse;
    }

    public static ArrayList<Restaurant> constructRestaurants(SearchResponse searchResponse) {

        ArrayList<Restaurant> restaurants = new ArrayList<>();

        for (int i = 0; i < searchResponse.businesses().size(); i++) {
            // get what we need from the yelp response object
            String id = searchResponse.businesses().get(i).id().toString();
            String name = searchResponse.businesses().get(i).name().toString();
            String image = searchResponse.businesses().get(i).imageUrl().toString();
            String website = searchResponse.businesses().get(i).mobileUrl().toString();
            Double latitude = searchResponse.businesses().get(i).location().coordinate().latitude();
            Double longitude = searchResponse.businesses().get(i).location().coordinate().longitude();
            String snippetText = searchResponse.businesses().get(i).snippetText().toString();
            Integer reviewCount = searchResponse.businesses().get(i).reviewCount();
            String snippetImg = searchResponse.businesses().get(i).snippetImageUrl();
            String ratingImg = searchResponse.businesses().get(i).ratingImgUrlLarge().toString();
            Double distance = searchResponse.businesses().get(i).distance();
            String phone = searchResponse.businesses().get(i).displayPhone().toString();

            // unpack and format categories and displayAddress lists
            StringBuilder sb = new StringBuilder();
            Integer categoriesListSize = searchResponse.businesses().get(i).categories().size();
            for (int j = 0; j < categoriesListSize; j++) {
                sb.append(searchResponse.businesses().get(i).categories().get(j).name());
                if (j + 1 < categoriesListSize)
                    sb.append(" \u00B7 "); // put a middot between category names
            }
            String categories = sb.toString();
            sb.setLength(0);
            Integer addressListSize = searchResponse.businesses().get(i).location().displayAddress().size();
            for (int k = 0; k < addressListSize; k++) {
                sb.append(searchResponse.businesses().get(i).location().displayAddress().get(k).toString());
                if (k + 1 < addressListSize)
                    sb.append("\n");  // if there's another element in addressList, put it on a new line
            }
            String address = sb.toString();

            // TODO: call method to query database for favorited status, rating and add to Restaurant constructor

            // construct new Restaurant object
            Restaurant restaurant = new Restaurant(id, name, image, categories, address, phone,
                    website, ratingImg, reviewCount, snippetImg, snippetText, latitude, longitude, distance);

            // add to arraylist
            restaurants.add(restaurant);
        }
        return restaurants;
    }
}
