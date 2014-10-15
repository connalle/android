package com.connalle.android.application.notepad.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.connalle.android.application.notepad.dal.NotesDataBaseAdapter;
import com.connalle.android.application.notepad.tables.NotesTable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by vicky on 10/7/2014.
 */
public class FileHelper {
    private static final String TAG = Constants.APP_TAG + ":" + FileHelper.class.getSimpleName();

    public static ArrayList<Uri> saveFiles(Set<Long> noteIds) {
        //Get the context
        Context context = NotepadApplication.applicationContext;
        File tempDirectory = context.getCacheDir();

        NotesDataBaseAdapter adapter = new NotesDataBaseAdapter(context);

        ArrayList<Uri> files = new ArrayList<Uri>();

        Log.d(TAG, "Temp Directory Path is " + tempDirectory.getAbsolutePath());

        for (Long noteID : noteIds) {
            NotesTable note = adapter.get(noteID);
            String fileName;

            if (null == note.getTitle() || note.getTitle().isEmpty()) {
                fileName = Utility.GenerateFileName(note.getNotesContent(), note.getNotesId());
            } else {
                fileName = Utility.GenerateFileName(note.getTitle(), note.getNotesId());
            }

            fileName += note.getFileExtension();

            File file = new File(tempDirectory, fileName);
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(note.getTitle());
                writer.write(note.getNotesContent());
                writer.close();
            } catch (IOException e) {
                Log.e(TAG, "Failed to create file with ID " + noteID);
            }

            files.add(Uri.fromFile(file));
        }

        return files;
    }

    public static void deleteFiles(ArrayList<Uri> filePaths) {
        for (Uri path : filePaths) {
            File file = new File(path.getPath());
            file.delete();
        }
    }

    public void writeFileToExternalStorage(String filemane, String data) {

    }

    public List<File> readFilesFromExternalStorage() {
        List<File> files = null;

        return files;
    }

    //Checks if external storage is available for read and write
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    //Checks if external storage is available to at least read
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}
