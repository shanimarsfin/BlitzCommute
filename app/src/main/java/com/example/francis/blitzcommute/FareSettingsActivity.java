package com.example.francis.blitzcommute;

import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import static com.example.francis.blitzcommute.BlitzConstants.*;

public class FareSettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar = null;
    private SharedPreferences sp = null;
    private SharedPreferences.Editor editor = null;
    private Switch triSwitch = null;
    private Switch taxiSwitch = null;
    private Switch jeepSwitch = null;
    private EditText triPPK = null;
    private EditText triPPT = null;
    private EditText taxiPPK = null;
    private EditText taxiPPT = null;
    private EditText jeepPPK = null;
    private EditText jeepPPT = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_settings);

        mToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        triSwitch = (Switch) findViewById(R.id.tricycleSwitch);
        taxiSwitch = (Switch) findViewById(R.id.taxiSwitch);
        jeepSwitch = (Switch) findViewById(R.id.jeepSwitch);
        triPPK = (EditText) findViewById(R.id.triPPK);
        taxiPPK = (EditText) findViewById(R.id.taxiPPK);
        jeepPPK = (EditText) findViewById(R.id.jeepPPK);
        triPPT = (EditText) findViewById(R.id.triPPT);
        taxiPPT = (EditText) findViewById(R.id.taxiPPT);
        jeepPPT = (EditText) findViewById(R.id.jeepPPT);

        sp = getSharedPreferences(SHARED_PREFERENCES_FILENAME, MODE_PRIVATE);
        editor = getSharedPreferences(SHARED_PREFERENCES_FILENAME, MODE_PRIVATE).edit();


        // Apply states if it exists in SharedPreferences
        if (sp.getBoolean(SP_TRICYCLE_SWITCH_STATE_NAME, true)) {
            triSwitch.setChecked(sp.getBoolean(SP_TRICYCLE_SWITCH_STATE_NAME, true));
        }
        if (sp.getBoolean(SP_TAXI_SWITCH_STATE_NAME, true)) {
            taxiSwitch.setChecked(sp.getBoolean(SP_TAXI_SWITCH_STATE_NAME, true));
        }
        if (sp.getBoolean(SP_JEEP_SWITCH_STATE_NAME, true)) {
            jeepSwitch.setChecked(sp.getBoolean(SP_JEEP_SWITCH_STATE_NAME, true));
        }
        if (!sp.getString(SP_TRICYCLE_PPK_NAME, "0.0").equals("0.0")) {
            triPPK.setText(sp.getString(SP_TRICYCLE_PPK_NAME, "0.0"));
        }
        if (!sp.getString(SP_TAXI_PPK_NAME, "0.0").equals("0.0")) {
            taxiPPK.setText(sp.getString(SP_TAXI_PPK_NAME, "0.0"));
        }
        if (!sp.getString(SP_JEEP_PPK_NAME, "0.0").equals("0.0")) {
            jeepPPK.setText(sp.getString(SP_JEEP_PPK_NAME, "0.0"));
        }
        if (!sp.getString(SP_TRICYCLE_PPT_NAME, "0.0").equals("0.0")) {
            triPPT.setText(sp.getString(SP_TRICYCLE_PPT_NAME, "0.0"));
        }
        if (!sp.getString(SP_TAXI_PPT_NAME, "0.0").equals("0.0")) {
            taxiPPT.setText(sp.getString(SP_TAXI_PPT_NAME, "0.0"));
        }
        if (!sp.getString(SP_JEEP_PPT_NAME, "0.0").equals("0.0")) {
            jeepPPT.setText(sp.getString(SP_JEEP_PPT_NAME, "0.0"));
        }

        // Handles Changes on all Switches and EditTexts
        triSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(SP_TRICYCLE_SWITCH_STATE_NAME, true);
                } else {
                    editor.putBoolean(SP_TRICYCLE_SWITCH_STATE_NAME, false);
                }
                editor.commit();
            }
        });

        taxiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(SP_TAXI_SWITCH_STATE_NAME, true);
                } else {
                    editor.putBoolean(SP_TAXI_SWITCH_STATE_NAME, false);
                }
                editor.commit();
            }
        });

        jeepSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(SP_JEEP_SWITCH_STATE_NAME, true);
                } else {
                    editor.putBoolean(SP_JEEP_SWITCH_STATE_NAME, false);
                }
                editor.commit();
            }
        });

        triPPK.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editor.putString(SP_TRICYCLE_PPK_NAME, s.toString()).commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        taxiPPK.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editor.putString(SP_TAXI_PPK_NAME, s.toString()).commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        jeepPPK.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editor.putString(SP_JEEP_PPK_NAME, s.toString()).commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        triPPT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editor.putString(SP_TRICYCLE_PPT_NAME, s.toString()).commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        taxiPPT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editor.putString(SP_TAXI_PPT_NAME, s.toString()).commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        jeepPPT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editor.putString(SP_JEEP_PPT_NAME, s.toString()).commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // if an intent from FareCalculation is sent here
        // because of empty initial settings
        Bundle data = getIntent().getExtras();
        boolean noInitialSettings = false;
        if (data != null) {
            noInitialSettings = data.getBoolean("fromFareCalculation");
        }
        if (noInitialSettings) {
            Toast.makeText(this, "Please configure the prices for each vehicles.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        sp = getSharedPreferences(SHARED_PREFERENCES_FILENAME, MODE_PRIVATE);
        editor = getSharedPreferences(SHARED_PREFERENCES_FILENAME, MODE_PRIVATE).edit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
