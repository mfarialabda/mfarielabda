package com.example.exammobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "student.db";
    private static final int DB_VERSION = 2;

    private static final String TABLE = "students";
    private static final String COL_ROW_ID = "_id";
    private static final String COL_NAME = "name";
    private static final String COL_STUDENT_ID = "student_id";
    private static final String COL_MAJOR = "major";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " ("
                + COL_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_STUDENT_ID + " TEXT NOT NULL UNIQUE, "
                + COL_MAJOR + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // ADD
    public boolean insertStudent(String name, String studentId, String major) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_STUDENT_ID, studentId);
        cv.put(COL_MAJOR, major);
        return db.insert(TABLE, null, cv) != -1;
    }

    // EDIT (berdasarkan Student ID)
    public boolean updateStudent(String studentId, String name, String major) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_MAJOR, major);
        return db.update(TABLE, cv, COL_STUDENT_ID + "=?", new String[]{studentId}) > 0;
    }

    // DELETE (berdasarkan Student ID)
    public boolean deleteStudent(String studentId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE, COL_STUDENT_ID + "=?", new String[]{studentId}) > 0;
    }

    // VIEW: semua data jadi satu teks
    public String getAllAsText() {
        StringBuilder sb = new StringBuilder();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE, null, null, null, null, null, COL_NAME + " ASC");
        int no = 1;
        while (c.moveToNext()) {
            sb.append(no++).append(". ")
                    .append(c.getString(c.getColumnIndexOrThrow(COL_NAME))).append("\n")
                    .append("    ID: ").append(c.getString(c.getColumnIndexOrThrow(COL_STUDENT_ID)))
                    .append("  |  ").append(c.getString(c.getColumnIndexOrThrow(COL_MAJOR)))
                    .append("\n\n");
        }
        c.close();
        return sb.toString();
    }
}