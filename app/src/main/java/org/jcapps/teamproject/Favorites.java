package org.jcapps.teamproject;

/**
 * Created by JC on 8/1/16.
 */
public class Favorites {
    public String id;
    public String name;
    public String userrating;
    public Favorites(String id, String name, String userrating) {
        this.id = id;
        this.name = name;
        this.userrating = userrating;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getUserrating() {
        return userrating;
    }

    @Override
    public String toString() {
        return "Favorites{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", userrating='" + userrating + '\'' +
                '}';
    }
}
