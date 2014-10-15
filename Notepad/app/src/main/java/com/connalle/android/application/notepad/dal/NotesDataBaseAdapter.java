package com.connalle.android.application.notepad.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.connalle.android.application.notepad.exceptions.DataBaseException;
import com.connalle.android.application.notepad.tables.NotesTable;
import com.connalle.android.application.notepad.utils.Constants;
import com.connalle.android.application.notepad.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class NotesDataBaseAdapter extends DataBaseAdapter {
    private final String TAG = Constants.APP_TAG + ":" + getClass().getSimpleName();

    public NotesDataBaseAdapter(Context context) {
        super(context);
    }

    @Override
    public Long insert(Object object) {
        Log.d(TAG, Utility.GetMethodName());
        NotesTable notes = (NotesTable) object;
        SQLiteDatabase database = getWritableDatabase();

        if (!database.isOpen()) {
            throw new DataBaseException("Database is not open");
        }

        ContentValues values = new ContentValues();
        values.put(NotesTable.ATTR_ID, notes.getNotesId());
        values.put(NotesTable.ATTR_TITLE, notes.getTitle());
        values.put(NotesTable.ATTR_CONTENT, notes.getNotesContent());
        values.put(NotesTable.ATTR_FILEEXTENSION, notes.getFileExtension());
        //values.put(NotesTable.ATTR_TAGS, notes.getTags());
        values.put(NotesTable.ATTR_CREATED_DATE, notes.getDateCreated());
        values.put(NotesTable.ATTR_UPDATED_DATE, notes.getDateUpdated());
        values.put(NotesTable.ATTR_STARRED_STATUS, notes.getStarredStatus());
        values.put(NotesTable.ATTR_REMINDER_SET, notes.getReminderSet());

        return database.insert(NotesTable.NAME, null, values);
    }

    @Override
    public Integer update(Object object) {
        Log.d(TAG, Utility.GetMethodName());
        NotesTable notes = (NotesTable) object;
        SQLiteDatabase database = getWritableDatabase();

        if (!database.isOpen()) {
            throw new DataBaseException("Database is not open");
        }

        ContentValues values = new ContentValues();
        values.put(NotesTable.ATTR_ID, notes.getNotesId());
        values.put(NotesTable.ATTR_TITLE, notes.getTitle());
        values.put(NotesTable.ATTR_CONTENT, notes.getNotesContent());
        values.put(NotesTable.ATTR_FILEEXTENSION, notes.getFileExtension());
        //values.put(NotesTable.ATTR_TAGS, notes.getTags());
        values.put(NotesTable.ATTR_CREATED_DATE, notes.getDateCreated());
        values.put(NotesTable.ATTR_UPDATED_DATE, notes.getDateUpdated());
        values.put(NotesTable.ATTR_STARRED_STATUS, notes.getStarredStatus());
        values.put(NotesTable.ATTR_REMINDER_SET, notes.getReminderSet());

        return database.update(NotesTable.NAME, values, NotesTable.ATTR_ID + " =?", new String[]{String.valueOf(notes.getNotesId())});
    }

    @Override
    public NotesTable get(Object object) {
        Log.d(TAG, Utility.GetMethodName());

        if (null == object)
            return null;

        SQLiteDatabase database = getReadableDatabase();
        String query = NotesTable.ATTR_ID + "=?";
        Cursor c = database.query(NotesTable.NAME, NotesTable.ATTR_ALL, query, new String[]{(String) object}, null, null, null, null);

        NotesTable notesTable = new NotesTable();
        if (c.moveToFirst()) {
            notesTable.setNotesId(c.getString((c.getColumnIndex(NotesTable.ATTR_ID))));
            notesTable.setTitle(c.getString((c.getColumnIndex(NotesTable.ATTR_TITLE))));
            notesTable.setNotesContent(c.getString(c.getColumnIndex(NotesTable.ATTR_CONTENT)));
            notesTable.setFileExtension(c.getString(c.getColumnIndex(NotesTable.ATTR_FILEEXTENSION)));
            //notes.setTags(c.getString(c.getColumnIndex(NotesTable.ATTR_TAGS)));
            notesTable.setDateCreated(c.getString(c.getColumnIndex(NotesTable.ATTR_CREATED_DATE)));
            notesTable.setDateUpdated(c.getString(c.getColumnIndex(NotesTable.ATTR_UPDATED_DATE)));
            notesTable.setStarredStatus(c.getInt(c.getColumnIndex(NotesTable.ATTR_STARRED_STATUS)));
            notesTable.setReminderSet(c.getString(c.getColumnIndex(NotesTable.ATTR_REMINDER_SET)));
        }
        return notesTable;
    }

    public List<NotesTable> getAll() {
        Log.d(TAG, Utility.GetMethodName());
        List<NotesTable> notesTables = new ArrayList<NotesTable>();
        String selectQuery = "SELECT  * FROM " + NotesTable.NAME;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.query(NotesTable.NAME, NotesTable.ATTR_ALL, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                NotesTable notes = new NotesTable();
                notes.setNotesId(c.getString((c.getColumnIndex(NotesTable.ATTR_ID))));
                notes.setTitle(c.getString((c.getColumnIndex(NotesTable.ATTR_TITLE))));
                notes.setNotesContent(c.getString(c.getColumnIndex(NotesTable.ATTR_CONTENT)));
                notes.setFileExtension(c.getString(c.getColumnIndex(NotesTable.ATTR_FILEEXTENSION)));
                //notes.setTags(c.getString(c.getColumnIndex(NotesTable.ATTR_TAGS)));
                notes.setDateCreated(c.getString(c.getColumnIndex(NotesTable.ATTR_CREATED_DATE)));
                notes.setDateUpdated(c.getString(c.getColumnIndex(NotesTable.ATTR_UPDATED_DATE)));
                notes.setStarredStatus(c.getInt(c.getColumnIndex(NotesTable.ATTR_STARRED_STATUS)));
                notes.setReminderSet(c.getString(c.getColumnIndex(NotesTable.ATTR_REMINDER_SET)));
                notesTables.add(notes);
            } while (c.moveToNext());
        }

        return notesTables;
    }

    @Override
    public Integer delete(Object object) {
        Log.d(TAG, Utility.GetMethodName());
        String notesId = (String) object;
        SQLiteDatabase database = getWritableDatabase();

        if (!database.isOpen()) {
            throw new DataBaseException("Database is not open");
        }

        return database.delete(NotesTable.NAME, NotesTable.ATTR_ID + " =?", new String[]{String.valueOf(notesId)});
    }
}
