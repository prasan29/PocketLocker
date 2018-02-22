package com.locker.tinylocker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prasanna on 12/19/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "POCKET_LOCKER_db";
    static String UNIQUE_NAME = "UNIQUE_NAME_TABLE";
    static String DETAILS = "DETAILS";
    private static DataBaseHelper dbHelper = null;

    public DataBaseHelper(Context context) {
        super(context, context.getExternalFilesDir(DATABASE_NAME)
                .getAbsolutePath() + File.separator + DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void initInstance(Context context) {
        if (dbHelper == null)
            dbHelper = new DataBaseHelper(context);
    }

    public static DataBaseHelper getInstance() {
        return dbHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS '" + UNIQUE_NAME + "'(ID Integer PRIMARY KEY AUTOINCREMENT, UNIQUE_NAME text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS '" + DETAILS + "'(ID Integer PRIMARY KEY AUTOINCREMENT,UNIQUE_NAME_WITH_DETAILS text, details text)");
    }

    public void insertDetails(String unique_name, String details) {
        SQLiteDatabase sdb = getWritableDatabase();
        ContentValues uniqueNameContent = new ContentValues();
        ContentValues detailsContent = new ContentValues();
        try {
            uniqueNameContent.put("UNIQUE_NAME", unique_name);
            sdb.insertWithOnConflict(UNIQUE_NAME, null, uniqueNameContent, SQLiteDatabase.CONFLICT_REPLACE);

            detailsContent.put("UNIQUE_NAME_WITH_DETAILS", unique_name);
            detailsContent.put("details", details);
            sdb.insertWithOnConflict(DETAILS, null, detailsContent, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sdb.close();
    }

    public String getDETAILS(String uniqueName) {
        String details = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT details FROM " + DETAILS + " WHERE UNIQUE_NAME_WITH_DETAILS ='" + uniqueName + "';", null);

        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    try {
                        details = cursor.getString(cursor.getColumnIndex("details"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

            db.close();
        }
        return details;
    }

    public List<String> getUniqueNames() {
        List<String> uniqueNames = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + UNIQUE_NAME, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    uniqueNames.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }

        return uniqueNames;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void deleteEntry(String uniqueName) {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM " + DETAILS + " WHERE UNIQUE_NAME_WITH_DETAILS = '" + uniqueName + "' ");
        db.execSQL("DELETE FROM " + UNIQUE_NAME + " WHERE UNIQUE_NAME = '" + uniqueName + "' ");
        db.close();
    }

    public void updateDetails(String uniqueName, String details) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put("details", details);

        db.update(DETAILS, content, "UNIQUE_NAME_WITH_DETAILS = ?", new String[]{uniqueName});

        db.close();
    }

    public void clearDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DETAILS, null, null);
        db.delete(UNIQUE_NAME, null, null);

        db.close();
    }
}