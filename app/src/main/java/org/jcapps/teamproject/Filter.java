package org.jcapps.teamproject;

/**
 * Created by JC on 8/3/16.
 */
public class Filter {
    public int sort;
    public int radius_filter;
    public String category_filter;


    public Filter(int sort, int radius_filter, String category_filter) {
        this.sort = sort;
        this.radius_filter = radius_filter;
        this.category_filter = category_filter;
    }

    public int getSort() {
        return sort;
    }

    public int getRadius_filter() {
        return radius_filter;
    }

    public String getCategory_filter() {
        return category_filter;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "sort=" + sort +
                ", radius_filter=" + radius_filter +
                ", category_filter='" + category_filter + '\'' +
                '}';
    }
}
