package com.locker.tinylocker.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.locker.tinylocker.R;
import com.locker.tinylocker.activity.PatternActivity;
import com.locker.tinylocker.model.SharedPref;
import com.locker.tinylocker.model.Utility;

/**
 * Created by Prasanna on 12/18/2016.
 */
public class ResetPatternDialog extends Dialog {
    private Activity activity;
    private SharedPref prefs;
    private String passCodeString;
    private EditText passCode;

    public ResetPatternDialog(Activity context) {
        super(context);
        this.activity = context;
        prefs = new SharedPref(activity);
        passCodeString = prefs.getResetPasscode();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reset_passcode_dialog);

        TextView submit = (TextView) findViewById(R.id.submit);
        passCode = (EditText) findViewById(R.id.reset_id);
        TextView content = (TextView) findViewById(R.id.content);

        content.setText(R.string.delete_content);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passCode.getText().toString().trim().length() == 6) {

                    validatePassCode(passCode.getText().toString().trim());

                } else {
                    Utility.getInstance(activity).showSnackBar(getWindow().findViewById(android.R.id.content), "Please enter a valid 6-digit passcode!");
                }
            }
        });
    }

    public void validatePassCode(String getText) {
        if (passCodeString.equals(getText)) {
            prefs.setPrefs(prefs.getPattern(), false, passCodeString);
            dismiss();
            activity.finish();
            Intent intent = new Intent(activity, PatternActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
        } else {
            Utility.getInstance(activity).showSnackBar(getWindow().findViewById(android.R.id.content), "Please enter the registered passcode to proceed!");
        }
    }
}