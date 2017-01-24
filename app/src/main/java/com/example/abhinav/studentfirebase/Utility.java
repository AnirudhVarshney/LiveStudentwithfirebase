package com.example.abhinav.studentfirebase;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ABHINAV on 27-05-2016.
 */
public class Utility {
    public static final String PREF_FILE_NAME = "userdetails";
    public static final String PREF_FILE_NAME_Profile = "userprofile";
    public static final String PREF_FILE_NAME_Firebase = "firebase";
    public static final String KEY_USER_LEARNED_DRAWER = "user";
    public void saveTopreferences(Context context,String userid) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

       editor.putString("id", userid);
        editor.apply();
    }

    public String readFrompreferences(Context context, String preferancename, String defaultvalue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferancename, defaultvalue);
    }
    public void saveTopreferencesfirebase(Context context, String studentid) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME_Firebase, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", studentid);
        editor.apply();
    }
    public int readFrompreferencesinteger(Context context, String preferancename, int defaultvalue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(preferancename, defaultvalue);
    }
    public String readFrompreferencesfirebase(Context context, String preferancename, String defaultvalue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME_Firebase, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferancename, defaultvalue);
    }
    public void saveTopreferencesProfile(Context context, String fathername,String mothername,String guardianname,String contact) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME_Profile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fname", fathername);
        editor.putString("mname", mothername);
        editor.putString("gname", guardianname);
        editor.putString("contact",contact);

        editor.apply();
    }

    public String readFrompreferencesProfile(Context context, String preferancename, String defaultvalue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME_Profile, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferancename, defaultvalue);
    }
    public int readFrompreferencesintegerProfile(Context context, String preferancename, int defaultvalue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME_Profile, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(preferancename, defaultvalue);
    }
}
