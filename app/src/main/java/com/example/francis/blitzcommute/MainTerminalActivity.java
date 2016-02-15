package com.example.francis.blitzcommute;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.example.francis.blitzcommute.BlitzConstants.*;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainTerminalActivity extends AppCompatActivity {

    private static final int FARE_REQUEST_CODE = 1337;
    private List<String> vehicles = new ArrayList<String>() {{
        add("jeep");
        add("taxi");
        add("tricycle");
        add("uber");
    }};
    private ActionBarDrawerToggle drawerToggle = null;
    private DrawerLayout drawerLayout = null;
    private NavigationView navigationView = null;
    private Toolbar mToolbar = null;
    private String offMessage = " is switched off.";
    private FloatingActionButton triButton = null;
    private FloatingActionButton taxiButton = null;
    private FloatingActionButton jeepButton = null;
    private FloatingActionButton uberButton = null;
    private TextView profileName = null;
    private CircleImageView profileImage = null;
    private TextView phoneNumberText = null;
    private TextView totalFare = null;
    private SharedPreferences sp = null;
    private SharedPreferences.Editor editor = null;
    private DBProfile dbProfile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_terminal);

        dbProfile = new DBProfile(this);
        SQLiteDatabase db = dbProfile.getReadableDatabase();
        //dbProfile.deleteTable(PROFILE_TABLE_NAME);
        dbProfile.onCreate(db);
        dbProfile.displayTableContents(PROFILE_TABLE_NAME);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);


        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.isChecked()) {
                    item.setChecked(false);

                } else {
                    item.setChecked(true);
                }
                drawerLayout.closeDrawers();

                Intent i;
                switch (item.getItemId()) {
                    case R.id.edit_profile_item:
                        i = new Intent(getBaseContext(), ProfileActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.fare_settings_item:
                        i = new Intent(getBaseContext(), FareSettingsActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.reset_fare_item:
                        editor.putString(SP_TOTAL_FARE, "0.0").commit();
                        totalFare.setText("00.00");
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Since the header is in another xml, we need to
        // retrieve it first before we can find its views
        View headerLayout = navigationView.getHeaderView(0);
        profileName = (TextView) headerLayout.findViewById(R.id.profileNameText);
        profileImage = (CircleImageView) headerLayout.findViewById(R.id.circleView);
        phoneNumberText = (TextView) headerLayout.findViewById(R.id.phoneNumber);

        // Setup Shared Preferences
        sp = getSharedPreferences(SHARED_PREFERENCES_FILENAME, MODE_PRIVATE);
        editor = getSharedPreferences(SHARED_PREFERENCES_FILENAME, MODE_PRIVATE).edit();

        totalFare = (TextView) findViewById(R.id.fareText);
        if (!sp.getString(SP_TOTAL_FARE, "0.0").equals("0.0")) {
            totalFare.setText(sp.getString(SP_TOTAL_FARE, "0.0"));
        }

        triButton = (FloatingActionButton) findViewById(R.id.tricycleButton);
        uberButton = (FloatingActionButton) findViewById(R.id.uberButton);

        triButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if (sp.getBoolean(SP_TRICYCLE_SWITCH_STATE_NAME, true)) {
                    editor.putBoolean(SP_TRICYCLE_SWITCH_STATE_NAME, true);
                    editor.commit();
                    i = new Intent(v.getContext(), FareCalculationActivity.class);
                    i.putExtra("vehicle", "tricycle");
                    startActivityForResult(i, FARE_REQUEST_CODE);
                } else if (sp.getBoolean(SP_TRICYCLE_SWITCH_STATE_NAME, true)) {
                    i = new Intent(v.getContext(), FareCalculationActivity.class);
                    i.putExtra("vehicle", "tricycle");
                    startActivityForResult(i, FARE_REQUEST_CODE);
                } else {
                    Toast.makeText(v.getContext(), "Tricycle" + offMessage, Toast.LENGTH_LONG).show();
                }
            }
        });

        taxiButton = (FloatingActionButton) findViewById(R.id.taxiButton);
        taxiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if (sp.getBoolean(SP_TAXI_SWITCH_STATE_NAME, true)) {
                    editor.putBoolean(SP_TAXI_SWITCH_STATE_NAME, true);
                    editor.commit();
                    i = new Intent(v.getContext(), FareCalculationActivity.class);
                    i.putExtra("vehicle", "taxi");
                    startActivityForResult(i, FARE_REQUEST_CODE);
                } else if (sp.getBoolean(SP_TAXI_SWITCH_STATE_NAME, true)) {
                    i = new Intent(v.getContext(), FareCalculationActivity.class);
                    i.putExtra("vehicle", "taxi");
                    startActivityForResult(i, FARE_REQUEST_CODE);
                } else {
                    Toast.makeText(v.getContext(), "Taxi" + offMessage, Toast.LENGTH_LONG).show();
                }
            }
        });

        jeepButton = (FloatingActionButton) findViewById(R.id.jeepButton);
        jeepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if (sp.getBoolean(SP_JEEP_SWITCH_STATE_NAME, true)) {
                    editor.putBoolean(SP_JEEP_SWITCH_STATE_NAME, true);
                    editor.commit();
                    i = new Intent(v.getContext(), FareCalculationActivity.class);
                    i.putExtra("vehicle", "jeep");
                    startActivityForResult(i, FARE_REQUEST_CODE);
                } else if (sp.getBoolean(SP_JEEP_SWITCH_STATE_NAME, true)) {
                    i = new Intent(v.getContext(), FareCalculationActivity.class);
                    i.putExtra("vehicle", "jeep");
                    startActivityForResult(i, FARE_REQUEST_CODE);
                } else {
                    Toast.makeText(v.getContext(), "Jeep" + offMessage, Toast.LENGTH_LONG).show();
                }
            }
        });

        uberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "To be implemented soon.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!sp.getString(SP_TOTAL_FARE, "empty").equals("empty")) {
            totalFare.setText(sp.getString(SP_TOTAL_FARE, "empty"));
        }

        Cursor data = dbProfile.getData(PROFILE_NUMBER);
        if (data.getCount() != 0) {
            data.moveToFirst();
            String fname = data.getString(data.getColumnIndex(PROFILE_COLUMN_FNAME));
            String lname = data.getString(data.getColumnIndex(PROFILE_COLUMN_LNAME));
            String phoneNumber = data.getString((data.getColumnIndex(PROFILE_COLUMN_PHONE)));
            byte[] picture = data.getBlob(data.getColumnIndex(PROFILE_COLUMN_PICTURE));

            if (fname.equals("") || lname.equals("") || phoneNumber.equals("") || picture.length == 0) {
                Toast.makeText(MainTerminalActivity.this, "Profile entry is incomplete", Toast.LENGTH_SHORT).show();
            } else {
                profileName.setText(fname + " " + lname);
                phoneNumberText.setText(phoneNumber);
                profileImage.setImageBitmap(Helper.byteArrayToBitmap(picture));
            }
        } else {
            Toast.makeText(MainTerminalActivity.this, "No Profile to display.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FARE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                totalFare.setText(sp.getString(SP_TOTAL_FARE, "0.0"));
            } else if (resultCode == RESULT_CANCELED) {
                if (data.hasExtra("vehicle")) {
                    String cancelMessage = "Fare Calculation Cancelled.\n" + data.getStringExtra("vehicle").toUpperCase() + " was turned off.";
                    Toast.makeText(MainTerminalActivity.this, cancelMessage, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Do nothing.
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
