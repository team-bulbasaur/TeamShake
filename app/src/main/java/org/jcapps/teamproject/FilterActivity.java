package org.jcapps.teamproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class FilterActivity extends AppCompatActivity {

    private SeekBar radiusBar = null;
    private Double searchRadius;
    private TextView tvRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        // TODO: handle the filter selections - call databasehelper methods to update preferences table
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
                Toast.makeText(FilterActivity.this, "Search Radius Progress: " + progressChanged, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
