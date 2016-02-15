package com.example.francis.blitzcommute;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.francis.blitzcommute.BlitzConstants.*;

public class ProfileActivity extends AppCompatActivity {

    private final static int PROFILE_NUMBER = 1;
    private static final int CAMERA_REQUEST = 1888;
    private String requestImageURI = "";
    private Context context = null;
    private Toolbar mToolbar;
    private EditText fnameText = null;
    private EditText lnameText = null;
    private EditText emailText = null;
    private EditText phoneText = null;
    private ImageView profileImage = null;
    private ImageLoader imageLoader = null;
    private ImageView jsonTestImage = null;
    private ImageButton cameraButton = null;
    private Button saveButton = null;
    private Cursor data = null;
    private byte[] profilePic = null;
    private int id;
    private String datasourceURL = "http://192.168.1.8:8080/LoginServlet/profile";
    private String message = "";
    private String jsonScript = "";
    private String fname;
    private String lname;
    private String email;
    private String phoneNumber;
    private DBProfile dbProfile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context = this;
        mToolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbProfile = new DBProfile(this);
        fnameText = (EditText) findViewById(R.id.firstNameText);
        lnameText = (EditText) findViewById(R.id.lastNameText);
        emailText = (EditText) findViewById(R.id.emailText);
        phoneText = (EditText) findViewById((R.id.phoneNumber));
        profileImage = (ImageView) findViewById(R.id.profileImage);
        jsonTestImage = (ImageView) findViewById(R.id.jsonTestImage);
        cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        saveButton = (Button) findViewById(R.id.saveProfileButton);

        data = getProfileData(PROFILE_NUMBER);
        if (data.getCount() != 0) {
            data.moveToFirst();
            id = data.getInt(data.getColumnIndex(PROFILE_COLUMN_ID));
            fname = data.getString(data.getColumnIndex(PROFILE_COLUMN_FNAME));
            lname = data.getString(data.getColumnIndex(PROFILE_COLUMN_LNAME));
            email = data.getString(data.getColumnIndex(PROFILE_COLUMN_EMAIL));
            phoneNumber = data.getString(data.getColumnIndex(PROFILE_COLUMN_PHONE));
            fnameText.setText(fname);
            lnameText.setText(lname);
            emailText.setText(email);
            phoneText.setText(phoneNumber);

            profilePic = data.getBlob(data.getColumnIndex(PROFILE_COLUMN_PICTURE));
            profileImage.setImageBitmap(Helper.byteArrayToBitmap(profilePic));
        } else {
            // Initialize profilePic to the image in ImageView
            BitmapDrawable bitmapDrawable = (BitmapDrawable) profileImage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            profilePic = Helper.convertToByteArray(bitmap);
        }

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

    }

    private Cursor getProfileData(int profileIndex) {
        return dbProfile.getData(profileIndex);
    }

    private void saveProfile() {

        fname = fnameText.getText().toString();
        lname = lnameText.getText().toString();
        phoneNumber = phoneText.getText().toString();
        email = emailText.getText().toString();
        boolean cond = false;
        if (checkFields()) {
            Toast.makeText(this, "Fields must not be empty.", Toast.LENGTH_SHORT).show();
        } else {
            if (data.getCount() == 0) {
                cond = dbProfile.insertProfile(fname, lname, phoneNumber, email, profilePic);
            } else {
                cond = dbProfile.updateProfile(PROFILE_NUMBER, fname, lname, phoneNumber, email, profilePic);
            }
        }

        if (cond) {
            message = "Profile saved locally.";
        } else {
            message = "Failed to save profile locally.";
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private boolean checkFields() {
        return fnameText.getText().toString().equals("") || lnameText.getText().toString().equals("")
                || emailText.getText().toString().equals("") || phoneText.getText().toString().equals("");
    }

    private void makeJsonRequest(String url, int method,
                                 Map<String, String> params) {
        message = "";
        CustomRequest customRequest = new CustomRequest(method, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        jsonScript = response.toString();
                        requestImageURI = response.getString("profilepicURL") + email;
                        Toast.makeText(context, "Sync completed.", Toast.LENGTH_SHORT).show();

                    } else {
                        message = response.getString("message");
                    }
                } catch (JSONException ex) {
                    Log.e("JSON Response Exception", ex.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                Toast.makeText(context, "Failed to connect to remote database.", Toast.LENGTH_SHORT).show();
            }
        });

        customRequest.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the jsonObjectRequest to the requestQueue
        RequestSingleton.getInstance(this.getApplicationContext()).addToRequestQueue(customRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            profileImage.setImageBitmap(photo);
            profilePic = Helper.convertToByteArray(photo);
        }
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

        if (id == R.id.action_sync_account) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("Synchronize Profile")
                    .setMessage("This will save your profile. Proceed?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            if (checkFields()) {
                                Toast.makeText(context, "Fields must not be empty.", Toast.LENGTH_SHORT).show();
                            } else {
                                saveProfile();
                                Map<String, String> profileMap = new HashMap<>();
                                profileMap.put(PROFILE_COLUMN_FNAME, fnameText.getText().toString());
                                profileMap.put(PROFILE_COLUMN_LNAME, lnameText.getText().toString());
                                profileMap.put(PROFILE_COLUMN_EMAIL, emailText.getText().toString());
                                profileMap.put(PROFILE_COLUMN_PHONE, phoneText.getText().toString());
                                profileMap.put(PROFILE_COLUMN_PICTURE, Helper.byteArrayToString(profilePic));

                                makeJsonRequest(datasourceURL, Request.Method.POST, profileMap);

                                if (!message.equals("")) {
                                    Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .show();
        } else if (id == R.id.action_show_json_script) {
            if (!jsonScript.equals("")) {
                for (int i = 0; i < 3; i++) {
                    Toast.makeText(ProfileActivity.this, jsonScript, Toast.LENGTH_LONG).show();
                }
            }
        } else if (id == R.id.action_request_image) {

            // Using the url sent with json request from the url
            // the image to be displayed on the networkimageview
            if (!requestImageURI.equals("")) {
                // Retrieves an image specified by the URL, displays it in the UI.
                ImageRequest request = new ImageRequest(requestImageURI,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                jsonTestImage.setImageBitmap(bitmap);
                            }
                        }, 0, 0, null, null,
                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                Log.e("ImageRequestError", error.toString());
                            }
                        });
                // Access the RequestQueue through your singleton class.
                RequestSingleton.getInstance(this).addToRequestQueue(request);
            } else {
                Toast.makeText(ProfileActivity.this, "Image URL not available.", Toast.LENGTH_SHORT).show();
            }

        } else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(1).setVisible(true);
        menu.getItem(2).setVisible(true);
        menu.getItem(3).setVisible(true);

        return true;
    }
}
