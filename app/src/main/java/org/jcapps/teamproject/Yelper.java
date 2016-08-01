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
        String id = null;
        String name = null;
        String image = null;
        String website = null;
        Double latitude = null;
        Double longitude = null;
        String snippetText = null;
        Integer reviewCount = null;
        String snippetImg = null;
        String ratingImg = null;
        Double distance = null;
        String phone = null;

        for (int i = 0; i < searchResponse.businesses().size(); i++) {
            // get what we need from the yelp response object

            if (searchResponse.businesses().get(i).id() != null) {
                id = searchResponse.businesses().get(i).id().toString();
            } else {
                id = "not available";
            }

            name = searchResponse.businesses().get(i).name().toString();
            image = searchResponse.businesses().get(i).imageUrl().toString();
            website = searchResponse.businesses().get(i).mobileUrl().toString();
            latitude = searchResponse.businesses().get(i).location().coordinate().latitude();
            longitude = searchResponse.businesses().get(i).location().coordinate().longitude();
            snippetText = searchResponse.businesses().get(i).snippetText().toString();
            reviewCount = searchResponse.businesses().get(i).reviewCount();
            snippetImg = searchResponse.businesses().get(i).snippetImageUrl();
            ratingImg = searchResponse.businesses().get(i).ratingImgUrlLarge().toString();
            distance = searchResponse.businesses().get(i).distance();

            if (searchResponse.businesses().get(i).displayPhone() != null) {
                phone = searchResponse.businesses().get(i).displayPhone().toString();
            } else {
                phone = "not available";
            }
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

            // construct new Restaurant object
            Restaurant restaurant = new Restaurant(id, name, image, categories, address, phone,
                    website, ratingImg, reviewCount, snippetImg, snippetText, latitude, longitude, distance);

            // add to arraylist
            restaurants.add(restaurant);
        }
        return restaurants;
    }
}
