package com.locker.tinylocker.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import com.amnix.materiallockview.MaterialLockView;
import com.locker.tinylocker.R;
import com.locker.tinylocker.interfaces.iDeleteEntry;
import com.locker.tinylocker.model.SharedPref;

import java.util.List;

/**
 * Created by Prasanna on 12/19/2016.
 */
public class PatternDialog extends Dialog {

    private Activity activity;
    private SharedPref prefs;
    private String savedPattern;
    private MaterialLockView pattern;
    private iDeleteEntry delete;

    public PatternDialog(Activity context, iDeleteEntry delete) {
        super(context);
        this.activity=context;
        this.delete=delete;
        prefs=new SharedPref(activity);
        savedPattern=prefs.getPattern();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pattern_dialog);

        pattern=(MaterialLockView) findViewById(R.id.pattern_dialog_id);

        pattern.setTactileFeedbackEnabled(false);

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

                validatePatternOnDialog(SimplePattern);
            }
        });
    }

    private void validatePatternOnDialog(String simplePattern) {
        if(simplePattern.equals(savedPattern))
        {
            pattern.setDisplayMode(MaterialLockView.DisplayMode.Correct);
            dismissDialog();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    delete.deleteEntry();
                }
            }, 200);
        }
        else
        {
            Toast.makeText(activity, "Wrong pattern!", Toast.LENGTH_SHORT).show();
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

    public void dismissDialog()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 200);
    }
}