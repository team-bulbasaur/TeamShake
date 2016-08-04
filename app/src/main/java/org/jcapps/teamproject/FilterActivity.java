package org.jcapps.teamproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FilterActivity extends AppCompatActivity {
    // buttons
    private Button btnClear;
    private Button btnSet;
    // distance slider
    private SeekBar radiusBar = null;
    private TextView tvRadius;
    // radio sort
    private RadioGroup sortGroup;
    private RadioButton sortDist, sortBest, sortRate;
    private View selectedSort;

    private Integer sort;
    private Integer radius_filter;
    private String category_filter;
    private ArrayList<String> categoriesChecked;
    private Map filterMap;

    UserDBHelper db;
    public static final String FILTER_PREFS = "filterPrefs";

    // ugliness (all sorts of last minute, poor form hacks in here)
    private CheckBox newamerican, tradamerican, bbq, breakfast_brunch, buffets, burgers, cajun, chicken_wings,
            chinese, delis, hotdogs, fishnchips, german, gluten_free, greek, halal, hotdog, indpak, irish, italian, japanese,
            korean, kosher, raw_food, mediterranean, mexican, mideastern, noodles, persian, pizza, polish, salad, sandwiches,
            scottish, seafood, soulfood, soup, southern, steak, sushi, tapasmallplates, thai, vegan, vegetarian, vietnamese,
            waffles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        categoriesChecked = new ArrayList<>();
        filterMap = new HashMap<String, String>();
        radius_filter = 11;

        btnClear = (Button) findViewById(R.id.btn_clear);
        btnSet = (Button) findViewById(R.id.btn_set);
        selectedSort = findViewById(R.id.rb_sortdist);

        // set button background & text colors to R.color values
        btnClear.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        btnClear.setTextColor(getResources().getColor(R.color.colorButtonText));
        btnSet.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        btnSet.setTextColor(getResources().getColor(R.color.colorButtonText));

        sortBest = (RadioButton) findViewById(R.id.rb_sortbest);
        sortDist = (RadioButton) findViewById(R.id.rb_sortdist);
        sortRate = (RadioButton) findViewById(R.id.rb_sortrate);

        // use sharedPreferences
        // getPreferences();

        // get db instance
        db = UserDBHelper.getInstance(this);

        // get stored filters from database
        filterMap = db.getFilter();
        sort = Integer.parseInt(filterMap.get("sort").toString());
        radius_filter = Integer.parseInt(filterMap.get("radius_filter").toString());
        category_filter = filterMap.get("category_filter").toString();

        tvRadius = (TextView) findViewById(R.id.tv_radius_value);
        radiusBar = (SeekBar) findViewById(R.id.sb_radius);

        setSortView();

        setCategoriesView();

        Toast.makeText(this, sort.toString() + " " + radius_filter.toString() + " " + category_filter, Toast.LENGTH_SHORT).show();
        // generate category checkboxes from strings array
        makeCategories();


        radiusBar.setMinimumHeight(200);
        radiusBar.setMax(10);
        radiusBar.setProgress(10);
        radiusBar.incrementProgressBy(1);

        setDistanceView();

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
            }
        });


    }

    // category checkbox list builder - fell outa the ugly tree....
    public void makeCategories() {
        newamerican = (CheckBox) findViewById(R.id.newamerican);
        tradamerican = (CheckBox) findViewById(R.id.tradamerican);
        bbq = (CheckBox) findViewById(R.id.bbq);
        breakfast_brunch = (CheckBox) findViewById(R.id.breakfast_brunch);
        buffets = (CheckBox) findViewById(R.id.buffets);
        burgers = (CheckBox) findViewById(R.id.burgers);
        cajun = (CheckBox) findViewById(R.id.cajun);
        chicken_wings = (CheckBox) findViewById(R.id.chicken_wings);
        chinese = (CheckBox) findViewById(R.id.chinese);
        delis = (CheckBox) findViewById(R.id.delis);
        hotdogs = (CheckBox) findViewById(R.id.hotdogs);
        fishnchips = (CheckBox) findViewById(R.id.fishnchips);
        german = (CheckBox) findViewById(R.id.german);
        gluten_free = (CheckBox) findViewById(R.id.gluten_free);
        greek = (CheckBox) findViewById(R.id.greek);
        halal = (CheckBox) findViewById(R.id.halal);
        hotdog = (CheckBox) findViewById(R.id.hotdog);
        indpak = (CheckBox) findViewById(R.id.indpak);
        irish = (CheckBox) findViewById(R.id.irish);
        italian = (CheckBox) findViewById(R.id.italian);
        japanese = (CheckBox) findViewById(R.id.japanese);
        korean = (CheckBox) findViewById(R.id.korean);
        kosher = (CheckBox) findViewById(R.id.kosher);
        raw_food = (CheckBox) findViewById(R.id.raw_food);
        mediterranean = (CheckBox) findViewById(R.id.mediterranean);
        mexican = (CheckBox) findViewById(R.id.mexican);
        mideastern = (CheckBox) findViewById(R.id.mideastern);
        noodles = (CheckBox) findViewById(R.id.noodles);
        persian = (CheckBox) findViewById(R.id.persian);
        pizza = (CheckBox) findViewById(R.id.pizza);
        polish = (CheckBox) findViewById(R.id.polish);
        salad = (CheckBox) findViewById(R.id.salad);
        sandwiches = (CheckBox) findViewById(R.id.sandwiches);
        scottish = (CheckBox) findViewById(R.id.scottish);
        seafood = (CheckBox) findViewById(R.id.seafood);
        soulfood = (CheckBox) findViewById(R.id.soulfood);
        soup = (CheckBox) findViewById(R.id.soup);
        southern = (CheckBox) findViewById(R.id.southern);
        steak = (CheckBox) findViewById(R.id.steak);
        sushi = (CheckBox) findViewById(R.id.sushi);
        tapasmallplates = (CheckBox) findViewById(R.id.tapasmallplates);
        thai = (CheckBox) findViewById(R.id.thai);
        vegan = (CheckBox) findViewById(R.id.vegan);
        vegetarian = (CheckBox) findViewById(R.id.vegetarian);
        vietnamese = (CheckBox) findViewById(R.id.vietnamese);
        waffles = (CheckBox) findViewById(R.id.waffles);
    }

    // click handling
    public void onSortClicked(View view) {
        selectedSort = view;
    }

    // ugly - hit every branch on the way down - shudda used a db table for the cats
    public void onCategoryClicked(View view) {
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()) {
            // we need to know the yelp category name, which is the id value of the checkbox...  shudda used a tag
            String id = checkBox.getResources().getResourceName(checkBox.getId());
            id = id.replaceFirst(".*/(\\w+)", "$1");  // HIYA!  Stand back!  I know Regular Expressions!
            categoriesChecked.add(id);
        } else {
            String id = checkBox.getResources().getResourceName(checkBox.getId());
            id = id.replaceFirst(".*/(\\w+)", "$1");  // HIYA!
            categoriesChecked.remove(id);
        }
    }

    public void resetIt(View view) {
        // if it's a checkbox, uncheck it
        LinearLayout layout = (LinearLayout) findViewById(R.id.category_container);
        for (int z = 0; z < layout.getChildCount(); z++) {
            View v = layout.getChildAt(z);
            if (v instanceof CheckBox)
                ((CheckBox) v).setChecked(false);
        }
        categoriesChecked.clear();
        // set distance filter to 11 ("These go to eleven.")
        radiusBar.setProgress(10);
        radius_filter = 11;
        // set sort to distance
        selectedSort = findViewById(R.id.rb_sortdist);
        sortDist.setChecked(true);

        filterMap.clear();
    }

    public void saveIt(View view) {
        // get rb id - set sort value
        switch (selectedSort.getId()) {
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
        // radius_filter value set in onStopTrackingTouch &
        // category_filter values set in onCategoryClicked
        if (categoriesChecked.size() != 0) {

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < categoriesChecked.size(); i++) {
                sb.append(categoriesChecked.get(i));
                if (i + 1 < categoriesChecked.size())
                    sb.append(",");
            }
            category_filter = sb.toString();
        } else category_filter = "";

        db.setFilter(radius_filter, sort, category_filter);
        //Toast.makeText(this, category_filter + " & " + sort + " & " + radius_filter, Toast.LENGTH_SHORT).show();
    }

    public void setSortView() {

        switch (sort) {
            case 0:
                selectedSort = findViewById(R.id.rb_sortbest);
                sortBest.setChecked(true);
                break;
            case 1:
                selectedSort = findViewById(R.id.rb_sortdist);
                sortDist.setChecked(true);
                break;
            case 2:
                selectedSort = findViewById(R.id.rb_sortrate);
                sortRate.setChecked(true);
                break;
        }
    }

    public void setDistanceView() {
        radiusBar.setProgress(radius_filter);

        if (radius_filter == 11) {
            tvRadius.setText("unlimited");
        } else {
            tvRadius.setText(String.format(Locale.ENGLISH, "%10d mi.", radius_filter));
        }
    }

    public void setCategoriesView() {
        if (category_filter == "") {
            LinearLayout layout = (LinearLayout) findViewById(R.id.category_container);
            for (int z = 0; z < layout.getChildCount(); z++) {
                View v = layout.getChildAt(z);
                if (v instanceof CheckBox)
                    ((CheckBox) v).setChecked(false);
            }
        } else {
            List<String> cats = Arrays.asList(category_filter.split("\\s*,\\s*"));
            for (String cat : cats) {
                //Toast.makeText(this, cat, Toast.LENGTH_SHORT).show();
                // we need to know the yelp category name, which is the id value of the checkbox...  shudda used a tag
                LinearLayout layout = (LinearLayout) findViewById(R.id.category_container);
                for (int z = 0; z < layout.getChildCount(); z++) {
                    View v = layout.getChildAt(z);

                    String id = v.getResources().getResourceName(v.getId());
                    id = id.replaceFirst(".*/(\\w+)", "$1");  // HIYA!  Stand back!  I know Regular Expressions!

                    if (id.equals(cat)) {
                        ((CheckBox) v).setChecked(true);
                        Toast.makeText(this, "ID: " + id + ", CAT: " + cat, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}