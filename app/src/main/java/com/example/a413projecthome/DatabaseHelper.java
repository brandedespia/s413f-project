package com.example.a413projecthome;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mahjong.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_NAME = "db";

    // Column names
    private static final String COLUMN_PPROG = "pprog";
    private static final String COLUMN_TPROG = "tprog";

    // Create table SQL
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_PPROG + " INTEGER, " +
            COLUMN_TPROG + " INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table
        db.execSQL(CREATE_TABLE);

        // Insert initial row with pprog=1 and tprog=1
        ContentValues values = new ContentValues();
        values.put(COLUMN_PPROG, 1);
        values.put(COLUMN_TPROG, 1);
        db.insert(TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    /**
     * Get puzzle progress
     */
    public int getPuzzleProgress() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_PPROG},
                null, null, null, null, null);

        int pprog = 1; // Default value
        if (cursor != null && cursor.moveToFirst()) {
            pprog = cursor.getInt(0);
            cursor.close();
        }
        return pprog;
    }

    /**
     * Get tutorial progress
     */
    public int getTutorialProgress() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_TPROG},
                null, null, null, null, null);

        int tprog = 1; // Default value
        if (cursor != null && cursor.moveToFirst()) {
            tprog = cursor.getInt(0);
            cursor.close();
        }
        return tprog;
    }

    /**
     * Update puzzle progress
     */
    public boolean updatePuzzleProgress(int newProgress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PPROG, newProgress);

        int rows = db.update(TABLE_NAME, values, null, null);
        return rows > 0;
    }

    /**
     * Update tutorial progress
     */
    public boolean updateTutorialProgress(int newProgress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TPROG, newProgress);

        int rows = db.update(TABLE_NAME, values, null, null);
        return rows > 0;
    }

    /**
     * Get both progress values
     */
    public int[] getProgress() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_PPROG, COLUMN_TPROG},
                null, null, null, null, null);

        int[] progress = new int[]{1, 1}; // Default values
        if (cursor != null && cursor.moveToFirst()) {
            progress[0] = cursor.getInt(0); // pprog
            progress[1] = cursor.getInt(1); // tprog
            cursor.close();
        }
        return progress;
    }

    /**
     * Reset progress to initial values
     */
    public void resetProgress() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PPROG, 1);
        values.put(COLUMN_TPROG, 1);
        db.update(TABLE_NAME, values, null, null);
    }
}

