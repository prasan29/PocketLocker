package com.locker.tinylocker;

import android.app.Application;

import com.locker.tinylocker.database.DataBaseHelper;

/**
 * Created by Prasanna on 12/30/2016.
 */
public class PocketLockerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DataBaseHelper.initInstance(this);
    }

    public static PocketLockerApplication getInstance()
    {
        return new PocketLockerApplication();
    }
}