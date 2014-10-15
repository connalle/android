package com.connalle.android.application.notepad.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.connalle.android.application.notepad.R;
import com.connalle.android.application.notepad.dal.NotesDataBaseAdapter;
import com.connalle.android.application.notepad.fragment.DateTimePickerFragment;
import com.connalle.android.application.notepad.fragment.DeleteDialogFragment;
import com.connalle.android.application.notepad.tables.NotesTable;
import com.connalle.android.application.notepad.utils.Constants;
import com.connalle.android.application.notepad.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteViewActivity extends Activity implements
        DeleteDialogFragment.DeleteDialogCommunicator,
        DateTimePickerFragment.DateTimePickerCommunicator,
        View.OnClickListener {
    private final String TAG = Constants.APP_TAG + ":" + getClass().getSimpleName();

    Integer mCurrentTask;
    NotesDataBaseAdapter mNotesDataBaseAdapter;
    ActionBar mActionBar;
    String mNoteId;
    Integer mStarredStatus;
    String mReminderSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, Utility.GetMethodName());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);

        initialize();
        if (null != mNoteId)
            constructLayout(getNote(mNoteId));
    }

    private void initialize() {
        Log.d(TAG, Utility.GetMethodName());

        //Initialize Database Adapter
        mNotesDataBaseAdapter = new NotesDataBaseAdapter(getApplicationContext());

        mNoteId = getIntent().getExtras().getString(Constants.NOTE_ID);
        mStarredStatus = 3;
        mReminderSet = "false";

        //Initialize task.
        Bundle extras = getIntent().getExtras();
        mCurrentTask = extras.getInt(Constants.NOTE_TASK);

        //Initialize Action Bar
        mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        if (Constants.NOTE_ADD.equals(mCurrentTask)) {
            mActionBar.setTitle("New Note");
            mStarredStatus = Constants.STARRED_LOW;
        } else if (Constants.NOTE_OPEN.equals(mCurrentTask)) {
            mActionBar.setTitle("Note");
            mStarredStatus = getNote(mNoteId).getStarredStatus();
        }

        //Initialize image buttons
        ImageView ivAlarm = (ImageView) findViewById(R.id.iv_note_view_alarm);
        ImageView ivStarred = (ImageView) findViewById(R.id.iv_note_view_starred);
        ivAlarm.setOnClickListener(this);
        ivStarred.setOnClickListener(this);
    }

    private void constructLayout(NotesTable note) {
        Log.d(TAG, Utility.GetMethodName());
        Log.d(TAG, "Notes ID = " + note.getNotesId());

        EditText title = (EditText) findViewById(R.id.et_note_title);
        EditText content = (EditText) findViewById(R.id.et_note_content);
        ImageView ivStarred = (ImageView) findViewById(R.id.iv_note_view_starred);

        title.setText(note.getTitle());
        content.setText(note.getNotesContent());

        if (Constants.STARRED_HIGH.equals(mStarredStatus)) {
            ivStarred.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_important));
        } else if (Constants.STARRED_MEDIUM.equals(mStarredStatus)) {
            ivStarred.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_half_important));
        } else if (Constants.STARRED_LOW.equals(mStarredStatus)) {
            ivStarred.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_not_important));
        } else {
            ivStarred.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_not_important));
        }
    }

    private void addNote(View view) {
        Log.d(TAG, Utility.GetMethodName());
        NotesTable note = new NotesTable();

        //get data from input
        EditText title = (EditText) view.findViewById(R.id.et_note_title);
        EditText content = (EditText) view.findViewById(R.id.et_note_content);

        String date = Utility.GetDateTime();

        if (Utility.IsEmpty(title) && Utility.IsEmpty(content)) {
            Log.w(TAG, "Title and Content are both empty.");
            Toast.makeText(this, "Please enter something to be added.", Toast.LENGTH_SHORT).show();
            return;
        }

        //set the notetable object
        //TODO:set a valid notes id
        note.setNotesId(Utility.GenerateHash(Utility.GetDateTime()));
        note.setTitle(title.getText().toString());
        note.setNotesContent(content.getText().toString());

        //TODO:remove hard coding here
        note.setFileExtension(".txt");
        note.setDateCreated(date);
        note.setDateUpdated(date);
        note.setStarredStatus(mStarredStatus);
        note.setReminderSet(mReminderSet);

        try {
            mNotesDataBaseAdapter.insert(note);
            launchNoteListActivity();
        } catch (Exception e) {
            Log.e(TAG, "Adding Note Failed", e);
        }
    }

    private void updateNote(View view) {
        Log.d(TAG, Utility.GetMethodName());
        NotesTable note = new NotesTable();

        //get data from input
        EditText title = (EditText) view.findViewById(R.id.et_note_title);
        EditText content = (EditText) view.findViewById(R.id.et_note_content);

        String date = Utility.GetDateTime();

        if (Utility.IsEmpty(title) && Utility.IsEmpty(content)) {
            Log.w(TAG, "Title and Content are both empty.");
            Toast.makeText(this, "Please enter something to be added.", Toast.LENGTH_SHORT).show();
            return;
        }

        //set the notetable object
        note.setNotesId(mNoteId);
        note.setTitle(title.getText().toString());
        note.setNotesContent(content.getText().toString());
        //TODO:remove hardcoding here
        note.setFileExtension(".txt");
        note.setDateCreated(date);
        note.setDateUpdated(date);
        note.setStarredStatus(mStarredStatus);
        note.setReminderSet(mReminderSet);

        try {
            mNotesDataBaseAdapter.update(note);
            launchNoteListActivity();
        } catch (Exception e) {
            Log.e(TAG, "Updating Note Failed", e);
        }
    }

    private void deleteNote(String id) {
        Log.d(TAG, Utility.GetMethodName());
        try {
            mNotesDataBaseAdapter.delete(id);
            launchNoteListActivity();
        } catch (Exception e) {
            Log.e(TAG, "Deleting Note Failed", e);
        }
    }

    private NotesTable getNote(String id) {
        return mNotesDataBaseAdapter.get(id);
    }

    private void launchNoteListActivity() {
        Intent intent = new Intent(this, NotesListViewActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.noteview_default, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem menuItem) {
        View view = findViewById(R.id.rl_note_view);
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.action_savenote:
                if (Constants.NOTE_ADD.equals(mCurrentTask))
                    addNote(view);
                else if (Constants.NOTE_OPEN.equals(mCurrentTask))
                    updateNote(view);
                else {
                    Log.e(TAG, "Invalid Task");
                }
                break;

            case R.id.action_discardnote:
                showDialog("Are you sure about deleting the note?");
                break;

            case R.id.action_sharenote:
                // Set the share Intent
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                intent.setType("text/plain");

                //Start the selected Activity
                startActivity(Intent.createChooser(intent, "Share Note Using..."));
                break;

            case R.id.action_exportnote:
                break;

            case R.id.action_settings:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                break;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
        }
        return super.onMenuItemSelected(featureId, menuItem);
    }

    private void showDialog(String title) {
        FragmentManager manager = getFragmentManager();
        DeleteDialogFragment dialogFragment = DeleteDialogFragment.GetInstance(title);
        dialogFragment.show(manager, "Delete Dialog");
    }

    @Override
    public void onDialogMessage(String message) {
        if (String.valueOf(R.string.ok).equals(message)) {
            deleteNote(mNoteId);
            return;
        }

        if (String.valueOf(R.string.cancel).equals(message)) {
            return;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_note_view_alarm:
                DateTimePickerFragment datePicker = DateTimePickerFragment.GetInstance();
                datePicker.show(getFragmentManager(), "Set Date");
                break;

            case R.id.iv_note_view_starred:
                setStarredIcon(view);
                break;

            default:
        }
    }

    private void setStarredIcon(View view) {
        ImageView ivStarred = (ImageView) view.findViewById(R.id.iv_note_view_starred);
        if (Constants.STARRED_HIGH.equals(mStarredStatus)) {
            mStarredStatus = Constants.STARRED_LOW;
            ivStarred.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_not_important));
        } else if (Constants.STARRED_MEDIUM.equals(mStarredStatus)) {
            mStarredStatus = Constants.STARRED_HIGH;
            ivStarred.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_important));
        } else if (Constants.STARRED_LOW.equals(mStarredStatus)) {
            mStarredStatus = Constants.STARRED_MEDIUM;
            ivStarred.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_half_important));
        } else {
            mStarredStatus = Constants.STARRED_LOW;
            ivStarred.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_not_important));
        }
    }

    @Override
    public void onDateTimeSelected(int year, int month, int day, int hour, int min) {
        Log.d(TAG, Utility.GetMethodName());
        Log.d(TAG, "Year = " + year + " : " +
                "Month = " + month + " : " +
                "Day = " + day + " : " +
                "Hour = " + hour + " : " +
                "Min = " + min);
        if (year == -1 || month == -1 || day == -1 || hour == -1 || min == -1) {
            mReminderSet = "false";
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                Constants.DATE_FORMAT, Locale.getDefault());

        Date reminderSetDate = Utility.GetDate(year, month, day, hour, min);
        Date currentDate = new Date();

        if (currentDate.compareTo(reminderSetDate) > 0) {
            Toast.makeText(getApplicationContext(), "Please select proper date", Toast.LENGTH_SHORT).show();
            mReminderSet = "false";
            return;
        }

        mReminderSet = dateFormat.format(reminderSetDate);
        Log.d(TAG, "Reminder set at : " + mReminderSet);
    }
}
