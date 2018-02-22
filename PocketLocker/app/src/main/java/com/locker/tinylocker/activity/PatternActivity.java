package com.locker.tinylocker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amnix.materiallockview.MaterialLockView;
import com.locker.tinylocker.R;
import com.locker.tinylocker.database.DataBaseHelper;
import com.locker.tinylocker.dialog.DialogAlternativePassCode;
import com.locker.tinylocker.dialog.ResetPatternDialog;
import com.locker.tinylocker.model.SharedPref;

import java.util.List;

public class PatternActivity extends AppCompatActivity {

    private SharedPref sharedPref;
    private MaterialLockView pattern;
    private TextView headerText;
    private String savedPattern;
    private boolean isRegistered;
    private TextView welcome;
    private TextView resetPattern;
    private LinearLayout deleteLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern);

        DataBaseHelper.initInstance(this);

        sharedPref = new SharedPref(this);

        prefsLog();

        isRegistered = sharedPref.isRegistered();
        savedPattern = sharedPref.getPattern();

        init();

        listeners();

        headerTextChanger();
    }

    private void init() {
        pattern = (MaterialLockView) findViewById(R.id.pattern_id);
        welcome = (TextView) findViewById(R.id.welcome);
        headerText = (TextView) findViewById(R.id.header_id);
        resetPattern = (TextView) findViewById(R.id.delete);
        deleteLayout = (LinearLayout) findViewById(R.id.delete_layout);
    }

    private void prefsLog() {
        Log.e("whole", "Pattern - " + sharedPref.getPattern() + " PassCode - " + sharedPref.getResetPasscode() + " isRegistered - " + sharedPref.isRegistered());
    }

    private void headerTextChanger() {
        if (isRegistered) {
            headerText.setText(R.string.header_text);
            deleteLayout.setVisibility(View.VISIBLE);
            welcome.setText(R.string.login_text);
        } else {
            deleteLayout.setVisibility(View.GONE);
            headerText.setText(R.string.header_text_register);
        }
    }

    private void listeners() {

        pattern.setTactileFeedbackEnabled(false);

        resetPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPatternDialog resetPatternDialog =new ResetPatternDialog(PatternActivity.this);
                resetPatternDialog.setCancelable(true);
                resetPatternDialog.show();
            }
        });

        pattern.setOnPatternListener(new MaterialLockView.OnPatternListener() {
            @Override
            public void onPatternStart() {
                super.onPatternStart();
            }

            @Override
            public void onPatternCleared() {
                super.onPatternCleared();
            }

            @Override
            public void onPatternCellAdded(List<MaterialLockView.Cell> pattern, String SimplePattern) {
                super.onPatternCellAdded(pattern, SimplePattern);
            }

            @Override
            public void onPatternDetected(List<MaterialLockView.Cell> pattern, String SimplePattern) {
                super.onPatternDetected(pattern, SimplePattern);
                Log.e("PatternActivity", SimplePattern);

                if (isRegistered) {
                    patternValidator(SimplePattern);
                } else {
                    if (SimplePattern.length() >= 4) {
                        PatternActivity.this.pattern.setDisplayMode(MaterialLockView.DisplayMode.Correct);

                        clearPattern();

                        DialogAlternativePassCode resetDialog = new DialogAlternativePassCode(PatternActivity.this, SimplePattern);
                        resetDialog.setCancelable(false);
                        resetDialog.show();
                    } else {

                        PatternActivity.this.pattern.setDisplayMode(MaterialLockView.DisplayMode.Wrong);
                        clearPattern();
                        Toast.makeText(PatternActivity.this, "Please connect at least four dots", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void patternValidator(String simplePattern) {
        if (simplePattern.trim().equals(savedPattern)) {
            pattern.setDisplayMode(MaterialLockView.DisplayMode.Correct);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(PatternActivity.this,TheDetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }, 200);

        } else {
            pattern.setDisplayMode(MaterialLockView.DisplayMode.Wrong);
            clearPattern();
        }
    }

    public void clearPattern() {
        if (pattern != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pattern.clearPattern();
                }
            }, 500);
        }
    }

    @Override
    public void onBackPressed() {
        if(sharedPref.getPattern()!=null)
        {
            sharedPref.setPrefs(sharedPref.getPattern(),true,sharedPref.getResetPasscode());
        }
        prefsLog();
        super.onBackPressed();
    }
}