package com.locker.tinylocker.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.locker.tinylocker.R;

/**
 * Created by Prasanna on 12/17/2016.
 */
public class SharedPref {
    public static final String PREF_NAME = "myPref";
    SharedPreferences prefs;
    private Context context;
    private SharedPreferences.Editor editor;

    public SharedPref(Context context) {
        this.context = context;
        prefs = this.context.getApplicationContext().getSharedPreferences(PREF_NAME, 0);
        editor = prefs.edit();
    }

    public void setPrefs(String pattern, boolean registered, String reset) {
        editor.putBoolean(context.getString(R.string.editor_is_registered_string), registered);
        editor.putString(context.getString(R.string.editor_pattern_string), pattern);
        editor.putString(context.getString(R.string.editor_reset_string), reset);
        editor.apply();
    }

    public boolean isRegistered() {
        return prefs.getBoolean(context.getString(R.string.editor_is_registered_string), false);
    }

    public String getPattern() {
        return prefs.getString(context.getString(R.string.editor_pattern_string), null);
    }

    public String getResetPasscode() {
        return prefs.getString(context.getString(R.string.editor_reset_string), null);
    }

    public void resetPrefs() {
        editor.clear();
        editor.apply();
    }
}