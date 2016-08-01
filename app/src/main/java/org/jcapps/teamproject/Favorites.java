package org.jcapps.teamproject;

/**
 * Created by JC on 8/1/16.
 */
public class Favorites {
    public String id;
    public String name;
    public String restaurant_id;
    public String ratingImg;
    public String userrating;
    public String usercomment;
    public Favorites(String id, String name, String restaurant_id,
                     String ratingImg, String userrating, String usercomment) {
        this.id = id;
        this.name = name;
        this.restaurant_id = restaurant_id;
        this.ratingImg = ratingImg;
        this.userrating = userrating;
        this.usercomment = usercomment;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getRestaurant_id() {
        return restaurant_id;
    }
    public String getRatingImg() {
        return ratingImg;
    }
    public String getUserrating() {
        return userrating;
    }
    public String getUsercomment() {
        return usercomment;
    }
}
