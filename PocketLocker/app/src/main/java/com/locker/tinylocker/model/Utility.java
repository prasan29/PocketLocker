package com.locker.tinylocker.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.locker.tinylocker.R;

/**
 * Created by Prasanna on 12/26/2016.
 */
public class Utility {
    private Activity activity;

    public Utility(Activity activity) {
        this.activity = activity;
    }

    public static Utility getInstance(Activity activity) {
        return new Utility(activity);
    }

    public void showSnackBar(View view, String message) {
        Snackbar.make(view.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    public ProgressDialog onProgressDialog() {
        ProgressDialog mProgressDialog = new ProgressDialog(activity, R.style.MyTheme);
        mProgressDialog.setCancelable(false);
        return mProgressDialog;
    }
}