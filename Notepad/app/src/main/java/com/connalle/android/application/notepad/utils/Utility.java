package com.connalle.android.application.notepad.utils;

import android.widget.EditText;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utility {
    private final String TAG = Constants.APP_TAG + ":" + getClass().getSimpleName();

    public static String GetDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                Constants.DATE_FORMAT, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static Date GetDate(int year, int month, int day, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, min);

        return calendar.getTime();
    }

    public static String GetMethodName() {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        return ste[3].getMethodName();
    }

    public static boolean IsEmpty(EditText editText) {
        if (editText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public static String GenerateFileName(String string, String id) {
        String fileName = null;
        fileName = string.replaceAll("\\s+", "");
        if (fileName.length() > 20)
            fileName = fileName.substring(0, Constants.FILENAME_LENGTH_MAX);

        //To ensure that the filename is always unique
        fileName += "_" + id;
        return fileName;
    }

    public static String GenerateHash(String input) {
        String hash = null;
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {

        }

        messageDigest.update(input.getBytes(), 0, input.length());
        hash = new BigInteger(1, messageDigest.digest()).toString();

        return hash;
    }
}
