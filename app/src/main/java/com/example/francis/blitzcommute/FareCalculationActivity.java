package com.example.francis.blitzcommute;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static com.example.francis.blitzcommute.BlitzConstants.*;

public class FareCalculationActivity extends AppCompatActivity {

    private Toolbar mToolbar = null;
    private SharedPreferences sp = null;
    private SharedPreferences.Editor editor = null;
    private EditText time = null;
    private EditText kilometers = null;
    private Button minFare = null;
    private Button fare = null;
    private ImageView selectedVehicle = null;
    private double ppk = 0.0d;
    private double ppt = 0.0d;
    private double totalFare = 0.0d;
    private String vehicle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_calculation);

        mToolbar = (Toolbar) findViewById(R.id.calculation_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        time = (EditText) findViewById(R.id.timeInputText);
        kilometers = (EditText) findViewById(R.id.kmInputText);
        minFare = (Button) findViewById(R.id.minFareButton);
        fare = (Button) findViewById(R.id.calculateFareButton);
        selectedVehicle = (ImageView) findViewById(R.id.selectedVehicleImage);

        // Determine the vehicle selected
        // Important: This block must precede getSharePreferencesData()---
        Bundle data = getIntent().getExtras();
        try {
            vehicle = data.get("vehicle").toString();

            //Retrieve vehicle image using the vehicle name
            int resId = this.getResources().getIdentifier(vehicle, "drawable", this.getPackageName());
            selectedVehicle.setImageResource(resId);
        } catch (NullPointerException ex) {
            Toast.makeText(this, "Selected vehicle information does not exist.", Toast.LENGTH_LONG).show();
        }
        //---

        try {
            getSharedPreferencesData(SHARED_PREFERENCES_FILENAME);

        } catch (NullPointerException ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }


        // Button Listeners
        minFare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ppk == 0 || ppt == 0) {
                    Intent i = new Intent(v.getContext(), FareSettingsActivity.class);
                    i.putExtra("fromFareCalculation", true);
                    startActivity(i);


                } else {
                    totalFare = totalFare + calculateFare(1.0, 1.0, ppk, ppt);
                    editor.putString(SP_TOTAL_FARE, String.valueOf(totalFare)).commit();
                    Intent i = new Intent();
                    setResult(RESULT_OK, i);
                    finish();
                }

            }
        });

        fare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(time.getText().toString().equals("")
                        || kilometers.getText().toString().equals(""))) {


                    if (ppk == 0 || ppt == 0) {
                        Intent i = new Intent(v.getContext(), FareSettingsActivity.class);
                        i.putExtra("fromFareCalculation", true);
                        startActivity(i);
                    } else {
                        double t = Double.parseDouble(time.getText().toString());
                        double k = Double.parseDouble(kilometers.getText().toString());
                        totalFare = totalFare + calculateFare(t, k, ppk, ppt);
                        editor.putString(SP_TOTAL_FARE, String.valueOf(totalFare)).commit();
                        Intent i = new Intent();
                        setResult(RESULT_OK, i);
                        finish();
                    }
                } else {
                    Toast.makeText(v.getContext(), "Time and distance must not be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        this.finish();
    }

    private double calculateFare(Double time, Double distance, Double ppk, Double ppt) {
        double totalFare = (time * ppt) + (distance * ppk);
        return totalFare;
    }


    @Override
    protected void onResume() {
        super.onResume();

        //Reread Shared Preferences data
        try {
            getSharedPreferencesData(SHARED_PREFERENCES_FILENAME);

        } catch (NullPointerException ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
        // Check if the vehicle selected was turned off during settings prompt
        if (!sp.getBoolean(vehicle + "SwitchState", true)) {
            Intent i = new Intent();
            i.putExtra("vehicle", vehicle);
            setResult(RESULT_CANCELED, i);
            finish();
        }
    }


    private boolean getSharedPreferencesData(String prefFileName) throws NullPointerException {
        sp = getSharedPreferences(prefFileName, MODE_PRIVATE);
        editor = getSharedPreferences(prefFileName, MODE_PRIVATE).edit();

        if (!vehicle.equals("")) {
            //Check if PPK/PPT inputs are safe to parse
            if (!sp.getString(vehicle + "PPK", "0.0").equals("")) {
                ppk = Double.parseDouble(sp.getString(vehicle + "PPK", "0.0"));
            }
            if (!sp.getString(vehicle + "PPT", "0.0").equals("")) {
                ppt = Double.parseDouble(sp.getString(vehicle + "PPT", "0.0"));
            }

            totalFare = Double.parseDouble(sp.getString(SP_TOTAL_FARE, "0.0"));
        } else {
            return false;
        }

        return !(ppk == 0.0 || ppt == 0.0 || totalFare == 0.0);
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
