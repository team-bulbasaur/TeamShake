package org.jcapps.teamproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantListAdapter extends ArrayAdapter<Restaurant> {

    static class ViewHolder {
        ImageView ivImage;
        TextView tvName;
        ImageView ivRating;
        TextView tvReviews;
        TextView tvCategories;
        TextView tvDistance;
    }

    public RestaurantListAdapter(Context context, ArrayList<Restaurant> restaurants) {
        super(context, 0, restaurants);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Restaurant restaurant = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.restaurant_card, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.ivRating = (ImageView) convertView.findViewById(R.id.iv_rating);
            viewHolder.tvReviews = (TextView) convertView.findViewById(R.id.tv_reviews);
            viewHolder.tvCategories = (TextView) convertView.findViewById(R.id.tv_categories);
            viewHolder.tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(getContext()).load(restaurant.image).placeholder(R.drawable.ghost).resize(300,300).into(viewHolder.ivImage);
        viewHolder.tvName.setText(restaurant.name);
        Picasso.with(getContext()).load(restaurant.ratingImg).resize(250,50).into(viewHolder.ivRating);
        viewHolder.tvReviews.setText(String.format("%d reviews", restaurant.reviewCount));
        viewHolder.tvCategories.setText(restaurant.categories);
        viewHolder.tvDistance.setText(String.format("%.0f m", restaurant.distance));

        return convertView;
    }
}
