package com.example.francis.blitzcommute;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import static com.example.francis.blitzcommute.BlitzConstants.*;

/**
 * Created by Francis on 2/5/2016.
 */
public class DBProfile extends SQLiteOpenHelper {

    public DBProfile(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String query = "create table if not exists " + PROFILE_TABLE_NAME +
                "(" + PROFILE_COLUMN_ID + " integer primary key autoincrement," +
                PROFILE_COLUMN_FNAME + " text," +
                PROFILE_COLUMN_LNAME + " text," +
                PROFILE_COLUMN_PICTURE + " blob," +
                PROFILE_COLUMN_EMAIL + " text," +
                PROFILE_COLUMN_PHONE + " text," +
                PROFILE_COLUMN_CREATEDON + " datetime default current_timestamp," +
                PROFILE_COLUMN_UPDATEDON + " datetime," +
                PROFILE_COLUMN_STATUS + " text)";
        try {
            db.execSQL(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("drop table if exists " + PROFILE_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertProfile(String fname, String lname, String phone, String email, byte[] profilePic) {
        long validate = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROFILE_COLUMN_FNAME, fname);
        contentValues.put(PROFILE_COLUMN_LNAME, lname);
        contentValues.put(PROFILE_COLUMN_PICTURE, profilePic);
        contentValues.put(PROFILE_COLUMN_EMAIL, email);
        contentValues.put(PROFILE_COLUMN_PHONE, phone);
        contentValues.put(PROFILE_COLUMN_UPDATEDON, System.currentTimeMillis());
        contentValues.put(PROFILE_COLUMN_STATUS, "CREATED");
        validate = db.insert(PROFILE_TABLE_NAME, null, contentValues);
        return validate != -1;

    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + PROFILE_TABLE_NAME + " where " + PROFILE_COLUMN_ID + "=" + id + "";
        Cursor res = db.rawQuery(query, null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PROFILE_TABLE_NAME);
        return numRows;
    }

    public boolean updateProfile(Integer id, String fname, String lname, String phone, String email, byte[] profilePic) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROFILE_COLUMN_FNAME, fname);
        contentValues.put(PROFILE_COLUMN_LNAME, lname);
        contentValues.put(PROFILE_COLUMN_PICTURE, profilePic);
        contentValues.put(PROFILE_COLUMN_EMAIL, email);
        contentValues.put(PROFILE_COLUMN_PHONE, phone);
        contentValues.put(PROFILE_COLUMN_UPDATEDON, System.currentTimeMillis());
        contentValues.put(PROFILE_COLUMN_STATUS, "updated");
        db.update(PROFILE_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteProfile(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PROFILE_TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public void displayTableContents(String tablename) {
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + tablename, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {

            int id = res.getInt(res.getColumnIndex(PROFILE_COLUMN_ID));
            String fname = res.getString(res.getColumnIndex(PROFILE_COLUMN_FNAME));
            String lname = res.getString(res.getColumnIndex(PROFILE_COLUMN_LNAME));
            String email = res.getString(res.getColumnIndex(PROFILE_COLUMN_EMAIL));
            String phoneNumber = res.getString(res.getColumnIndex(PROFILE_COLUMN_PHONE));

            String toPrint = id + "\t" + fname + "\t" + lname + "\t" + email + "\t" + phoneNumber + "\t";
            Log.e(id + "", toPrint);
            res.moveToNext();
        }
    }

    public void deleteTable(String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("drop table if exists " + tablename);
        } catch (SQLException ex) {
            Log.e("SQLException", "Wrong SQL Command");
        }
    }

}
