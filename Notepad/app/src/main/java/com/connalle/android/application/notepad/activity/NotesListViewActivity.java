package com.connalle.android.application.notepad.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.connalle.android.application.notepad.R;
import com.connalle.android.application.notepad.dal.NotesDataBaseAdapter;
import com.connalle.android.application.notepad.fragment.DeleteDialogFragment;
import com.connalle.android.application.notepad.tables.NotesTable;
import com.connalle.android.application.notepad.utils.Constants;
import com.connalle.android.application.notepad.utils.NotepadApplication;
import com.connalle.android.application.notepad.utils.Utility;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotesListViewActivity extends Activity implements View.OnClickListener,
        View.OnLongClickListener, DeleteDialogFragment.DeleteDialogCommunicator {
    private final String TAG = Constants.APP_TAG + ":" + getClass().getSimpleName();

    NotesDataBaseAdapter mNotesDataBaseAdapter;
    Set<String> mSelectedNotes;
    ActionMode mActionMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list_view);

        initialize();
        constructLayout(getNotes());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, Utility.GetMethodName());
        constructLayout(getNotes());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, Utility.GetMethodName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, Utility.GetMethodName());
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, Utility.GetMethodName() + " Selected Ids = " + getString(mSelectedNotes));
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_notes_list_single:
                Log.d(TAG, "Note ID = " + view.getTag());
                if (mSelectedNotes.isEmpty()) {
                    view.setBackgroundColor(Color.parseColor(getString(R.color.layoutPressed)));
                    Log.d(TAG, "Opening note with id " + view.getTag());
                    intent = new Intent(getApplicationContext(), NoteViewActivity.class);
                    intent.putExtra(Constants.NOTE_TASK, Constants.NOTE_OPEN);
                    intent.putExtra(Constants.NOTE_ID, (String) view.getTag());
                    startActivity(intent);
                }
                break;

            default:
                Log.w(TAG, "Invalid Button Id");
        }
    }

    @Override
    public boolean onLongClick(View view) {
        Log.d(TAG, Utility.GetMethodName());
        if (null == mActionMode) {
            NotesLongPressActionModeCallback callback = new NotesLongPressActionModeCallback();
            mActionMode = startActionMode(callback);
            mActionMode.setTitle("Select Notes");
        }

        switch (view.getId()) {
            case R.id.rl_notes_list_single:
                Log.d(TAG, "Note ID = " + view.getTag());
                if (mSelectedNotes.contains(view.getTag())) {
                    view.setBackgroundColor(Color.parseColor(getString(R.color.defaultBackground)));
                    mSelectedNotes.remove(view.getTag());
                } else {
                    view.setBackgroundColor(Color.parseColor(getString(R.color.layoutLongPressed)));
                    mSelectedNotes.add((String) view.getTag());
                }
                break;

            default:
                Log.w(TAG, "Invalid Button Id");
        }
        return true;
    }

    private void initialize() {
        Log.d(TAG, Utility.GetMethodName());

        //Set the application context so that other classes can use it.
        NotepadApplication.applicationContext = getApplicationContext();

        //Initialize Database Adapter
        mNotesDataBaseAdapter = new NotesDataBaseAdapter(getApplicationContext());

        //Initialize Selected Note List
        mSelectedNotes = new HashSet<String>();

        ActionBar mActionBar = getActionBar();
        mActionBar.setTitle("Notes");
    }

    private void constructLayout(List<NotesTable> notes) {
        Log.d(TAG, Utility.GetMethodName());
        LayoutInflater layoutInflater = getLayoutInflater();

        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_activity_notes_list);
        ll.removeAllViews();
        for (NotesTable note : notes) {
            View noteView = layoutInflater.inflate(R.layout.activity_notes_list_view_single, null);
            RelativeLayout rlNoteSingle = (RelativeLayout) noteView.findViewById(R.id.rl_notes_list_single);
            rlNoteSingle.setOnClickListener(this);
            rlNoteSingle.setOnLongClickListener(this);
            rlNoteSingle.setTag(note.getNotesId());

            TextView tvTitle = (TextView) noteView.findViewById(R.id.tv_notes_list_single_title);
            TextView tvOverview = (TextView) noteView.findViewById(R.id.tv_notes_list_single_overview);
            TextView tvDateModified = (TextView) noteView.findViewById(R.id.tv_notes_list_single_date_modified);
            ImageView ivStarred = (ImageView) noteView.findViewById(R.id.iv_starred);
            ImageView ivReminder = (ImageView) noteView.findViewById(R.id.iv_reminder);

            tvTitle.setText(note.getTitle());
            tvOverview.setText(note.getNotesContent());
            tvDateModified.setText(note.getDateUpdated());

            if (Constants.STARRED_HIGH.equals(note.getStarredStatus())) {
                ivStarred.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_important_dark));
            } else if (Constants.STARRED_MEDIUM.equals(note.getStarredStatus())) {
                ivStarred.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_half_important_dark));
            } else if (Constants.STARRED_LOW.equals(note.getStarredStatus())) {
                ivStarred.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_not_important_dark));
            } else {
                ivStarred.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_not_important_dark));
            }

            if ("false".equals(note.getReminderSet())) {
                ivReminder.setVisibility(View.GONE);
            }

            ll.addView(rlNoteSingle);
        }
    }

    private List<NotesTable> getNotes() {
        Log.d(TAG, Utility.GetMethodName());
        return mNotesDataBaseAdapter.getAll();
    }

    private void deleteNotes() {
        Log.d(TAG, Utility.GetMethodName());


        for (String id : mSelectedNotes) {
            Log.d(TAG, "Deleting Id " + id);
            mNotesDataBaseAdapter.delete(id);
        }

        mSelectedNotes.clear();
        constructLayout(getNotes());
    }

    private String getString(Set<String> longSet) {
        return longSet.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.noteslist_default, menu);

        // Return true to display menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_addnote:
                Log.d(TAG, "Opening blank note to add");
                intent = new Intent(getApplicationContext(), NoteViewActivity.class);
                intent.putExtra(Constants.NOTE_TASK, Constants.NOTE_ADD);
                startActivity(intent);
                break;

            case R.id.action_searchnotes:
                break;

            case R.id.action_settings:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog(String title) {
        FragmentManager manager = getFragmentManager();
        DeleteDialogFragment dialogFragment = DeleteDialogFragment.GetInstance(title);
        dialogFragment.show(manager, "Delete Dialog");
    }

    @Override
    public void onDialogMessage(String message) {
        if (String.valueOf(R.string.ok).equals(message)) {
            deleteNotes();
            mActionMode.finish();
            return;
        }

        if (String.valueOf(R.string.cancel).equals(message)) {
            mActionMode.finish();
            return;
        }
    }

    class NotesLongPressActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.noteslist_longpress, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            Intent intent;
            switch (menuItem.getItemId()) {
                case R.id.action_discardnotes:
                    showDialog("Are you sure about deleting notes?");
                    break;

                //TODO:Thinking of removing the share of multiple
                //notes as only few options are available
                case R.id.action_sharenotes:
                    // Set the share intent
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                    intent.setType("text/plain");

                    //Start the selected activity
                    startActivity(Intent.createChooser(intent, "Share Notes Using..."));
                    break;

                case R.id.action_exportnotes:
                    break;

                default:
                    Log.w(TAG, "Item Not Found");
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            Log.e(TAG, Utility.GetMethodName());
            mActionMode = null;
            mSelectedNotes.clear();
            LinearLayout layout = (LinearLayout) findViewById(R.id.ll_activity_notes_list);
            for (int id = 0; id < layout.getChildCount(); id++) {
                View view = layout.getChildAt(id);
                view.setBackgroundColor(Color.parseColor(getString(R.color.defaultBackground)));
            }
        }
    }
}
