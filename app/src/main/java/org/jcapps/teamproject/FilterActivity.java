package org.jcapps.teamproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

public class FilterActivity extends AppCompatActivity {

    private SeekBar searchRadius = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        // TODO: handle the filter selections - call databasehelper methods to update preferences table

        searchRadius = (SeekBar) findViewById(R.id.sb_radius);
        searchRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(FilterActivity.this, "Search Radius Progress: " + progressChanged, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
