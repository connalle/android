package com.connalle.android.application.notepad.tables;

import com.connalle.android.application.notepad.utils.Constants;

//Model of the Notes Table
public class NotesTable {
    public static final String NAME = "Notes";
    public static final String ATTR_ID = "notesId";
    public static final String ATTR_TITLE = "title";
    public static final String ATTR_CONTENT = "notesContent";
    public static final String ATTR_FILEEXTENSION = "fileExtension";
    public static final String ATTR_TAGS = "tags";
    public static final String ATTR_CREATED_DATE = "dateCreated";
    public static final String ATTR_UPDATED_DATE = "dateUpdated";
    public static final String ATTR_STARRED_STATUS = "starredStatus";
    public static final String ATTR_REMINDER_SET = "reminderSet";
    public static final String[] ATTR_ALL = new String[]{ATTR_ID, ATTR_TITLE, ATTR_CONTENT, ATTR_FILEEXTENSION, ATTR_CREATED_DATE, ATTR_UPDATED_DATE, ATTR_STARRED_STATUS, ATTR_REMINDER_SET};
    public static final String QUERY_CREATE_TABLE = "CREATE TABLE "
            + NAME + "("
            + ATTR_ID + " TEXT PRIMARY KEY,"
            + ATTR_TITLE + " TEXT,"
            + ATTR_CONTENT + " TEXT,"
            + ATTR_FILEEXTENSION + " TEXT,"
            //+ ATTR_TAGS + "TEXT,"
            + ATTR_CREATED_DATE + " TEXT,"
            + ATTR_UPDATED_DATE + " TEXT,"
            + ATTR_STARRED_STATUS + " INTEGER,"
            + ATTR_REMINDER_SET + " TEXT"
            + ")";
    private final String TAG = Constants.APP_TAG + ":" + getClass().getSimpleName();
    private String notesId;
    private String title;
    private String notesContent;
    private String fileExtension;
    private String tags;
    private String dateCreated;
    private String dateUpdated;
    private Integer starredStatus;
    private String reminderSet;

    public String getNotesId() {
        return notesId;
    }

    public void setNotesId(String notesId) {
        this.notesId = notesId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotesContent() {
        return notesContent;
    }

    public void setNotesContent(String notesContent) {
        this.notesContent = notesContent;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Integer getStarredStatus() {
        return starredStatus;
    }

    public void setStarredStatus(Integer starredStatus) {
        this.starredStatus = starredStatus;
    }

    public String getReminderSet() {
        return reminderSet;
    }

    public void setReminderSet(String reminderSet) {
        this.reminderSet = reminderSet;
    }
}
