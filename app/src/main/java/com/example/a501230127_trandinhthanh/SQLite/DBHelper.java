package com.example.a501230127_trandinhthanh.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    final public static String DATABASE_NAME = "QLTOUR.db";
    final public static int DB_VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='TOUR'", null);
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            if (count == 0) {
                String createTableQuery = "CREATE TABLE TOUR (" +
                        "MaTour TEXT PRIMARY KEY, " +
                        "TenTour TEXT, " +
                        "Gia INTEGER)";
                db.execSQL(createTableQuery);
            }
        }
        cursor.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

