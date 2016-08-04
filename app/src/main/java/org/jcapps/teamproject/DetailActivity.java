package org.jcapps.teamproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    TextView mName, mCategories, mSnippetText, mAddress, mPhone, mReviews, mDistance;
    ImageView image, snippetImg, ratingImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mName = (TextView) findViewById(R.id.tv_det_name);
        mCategories = (TextView) findViewById(R.id.tv_det_categories);
       // mSnippetText = (TextView) findViewById(R.id.tv_det_snippetText);
        mAddress = (TextView) findViewById(R.id.tv_det_address);
        mPhone = (TextView) findViewById(R.id.tv_det_phone);
        mReviews = (TextView) findViewById(R.id.tv_det_reviews);
        mDistance = (TextView) findViewById(R.id.tv_det_distance);

        Restaurant restaurant = (Restaurant) getIntent().getSerializableExtra("RESTAURANT");

        mName.setText(restaurant.name);
        mCategories.setText(restaurant.categories);
//        //mSnippetText.setText(restaurant.snippetText);
        mAddress.setText(restaurant.address);
        mPhone.setText(restaurant.phone);
        mReviews.setText(String.format(Locale.ENGLISH, "%d reviews", restaurant.reviewCount));
        mDistance.setText(String.format(Locale.ENGLISH, "%.0f m", restaurant.distance));



    }
}
