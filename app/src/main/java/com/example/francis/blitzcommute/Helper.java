package com.example.francis.blitzcommute;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Francis on 2/2/2016.
 */
public final class Helper {
//    public static void hideSoftKeyboard(View view) {
//        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }

    private static boolean yesNoAnswer = false;

    public static final byte[] convertToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static final Bitmap byteArrayToBitmap(byte[] blob) {
        return BitmapFactory.decodeByteArray(blob, 0, blob.length);
    }

    public static final String byteArrayToString(byte[] blob) {
        return Base64.encodeToString(blob, Base64.DEFAULT);
    }

    public static final byte[] encodeStringToByteArray(String strCode) {
        return Base64.decode(strCode, Base64.DEFAULT);
    }

    public static boolean createYesNoDialog(Context context, String title, String message) {
        yesNoAnswer = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        yesNoAnswer = true;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();

        return yesNoAnswer;
    }
}
