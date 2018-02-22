package com.locker.tinylocker.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.locker.tinylocker.R;
import com.locker.tinylocker.activity.TheDetailsActivity;
import com.locker.tinylocker.model.SharedPref;
import com.locker.tinylocker.model.Utility;

/**
 * Created by Prasanna on 12/17/2016.
 */
public class DialogAlternativePassCode extends Dialog {
    private EditText passCode;
    private String SimplePattern;
    private Activity activity;

    public DialogAlternativePassCode(Activity context, String SimplePattern) {
        super(context);
        this.activity = context;
        this.SimplePattern = SimplePattern;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reset_passcode_dialog);

        TextView submit = (TextView) findViewById(R.id.submit);
        passCode = (EditText) findViewById(R.id.reset_id);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passCode.getText().toString().trim().length() == 6) {

                    savePattern(SimplePattern, passCode.getText().toString().trim());

                    dismiss();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(activity,TheDetailsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            activity.startActivity(intent);
                            activity.finish();
                        }
                    }, 200);

                    Utility.getInstance(activity).showSnackBar(getWindow().findViewById(android.R.id.content),"Pattern saved successfully!");
//                    Toast.makeText(activity, "Pattern saved successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Utility.getInstance(activity).showSnackBar(getWindow().findViewById(android.R.id.content),"Please enter a valid 6-digit number!");

//                    Toast.makeText(activity, "Please enter a valid 6-digit number!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void savePattern(String simplePattern, String passcode) {
        SharedPref prefs = new SharedPref(getContext());
        prefs.setPrefs(simplePattern, true, passcode);

        Log.e("whole", "Pattern - " + prefs.getPattern() + " Passcode - " + prefs.getResetPasscode() + " isRegistered - " + prefs.isRegistered());
    }
}