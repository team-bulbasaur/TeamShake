package org.jcapps.teamproject;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FilterActivity extends AppCompatActivity {

    private SeekBar radiusBar = null;
    private Double searchRadius;
    private TextView tvRadius;
    private Button btnClear;
    private Button btnSet;

    private View selectedSort;
    private Integer sort;
    private Integer radius_filter;
    private String category_filter;
    private ArrayList<String> categoriesChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        categoriesChecked = new ArrayList<>();

        btnClear = (Button) findViewById(R.id.btn_clear);
        btnSet = (Button) findViewById(R.id.btn_set);

        selectedSort = findViewById(R.id.rb_sortdist);

        // set button background & text colors to R.color values
        btnClear.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        btnClear.setTextColor(getResources().getColor(R.color.colorButtonText));
        btnSet.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        btnSet.setTextColor(getResources().getColor(R.color.colorButtonText));

        // TODO: handle the filter selections - call databasehelper methods to update preferences table
        // db = Database access
        // db.getFilters();

        // generate category checkboxes from strings array
        makeCategories();

        tvRadius = (TextView) findViewById(R.id.tv_radius_value);
        radiusBar = (SeekBar) findViewById(R.id.sb_radius);
        radiusBar.setMinimumHeight(200);
        radiusBar.setMax(10);
        radiusBar.setProgress(10);
        radiusBar.incrementProgressBy(1);
        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress + 1;

                tvRadius.setText(String.format(Locale.ENGLISH, "%10d mi.", progressChanged));

                if (progressChanged == 11) {
                    tvRadius.setText("unlimited");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                radius_filter = progressChanged;
                // Toast.makeText(FilterActivity.this, "Search Radius Progress: " + progressChanged, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // category checkbox list builder
    public void makeCategories() {

        String[] checkboxes = this.getResources().getStringArray(R.array.yelp_categories);

        for (int i = 0; i < checkboxes.length; i++) {


            String value = getResources().getStringArray(R.array.yelp_categories)[i];

            // TODO : create Hashmap of <yelp_String, display_String> here to populate checkboxes  or manually clear checkboxes

            //Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
        }

    }
    // click handling
    public void onSortClicked(View view) {
        selectedSort = view;
        filterIt();
    }
    public void onCategoryClicked(View view) {
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()) {
            String text = checkBox.getText().toString();
            Toast.makeText(this, text + " checked", Toast.LENGTH_SHORT).show();

        } else {
            String text = checkBox.getText().toString();
            Toast.makeText(this, text + " unchecked", Toast.LENGTH_SHORT).show();
        }
    }
    public void filterIt() {
        // get rb id - set sort value
        switch(selectedSort.getId()) {
            case R.id.rb_sortbest:
                sort = 0;
                break;
            case R.id.rb_sortdist:
                sort = 1;
                break;
            case R.id.rb_sortrate:
                sort = 2;
                break;
        }
        // radius_filter value set in onStopTrackingTouch

        // category_filter values set in onCategoryClicked

        // call dbhelper method to update preferences
    }
}
