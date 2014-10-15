package com.connalle.android.application.notepad.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.connalle.android.application.notepad.tables.NotesTable;
import com.connalle.android.application.notepad.utils.Constants;


public abstract class DataBaseAdapter {
    private static final String DATABASE_NAME = "Notepad";
    private static final int DATABASE_VERSION = 1;
    private final String TAG = Constants.APP_TAG + ":" + getClass().getSimpleName();
    DataBaseHelper dataBaseHelper;

    protected DataBaseAdapter(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
    }

    protected abstract Object insert(Object o);

    protected abstract Object update(Object o);

    protected abstract Object get(Object o);

    protected abstract Object delete(Object o);

    protected SQLiteDatabase getWritableDatabase() {
        return dataBaseHelper.getWritableDatabase();
    }

    protected SQLiteDatabase getReadableDatabase() {
        return dataBaseHelper.getReadableDatabase();
    }

    protected void closeDatabase() {
        dataBaseHelper.close();
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {

        DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(NotesTable.QUERY_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NotesTable.NAME);
        }
    }
}
