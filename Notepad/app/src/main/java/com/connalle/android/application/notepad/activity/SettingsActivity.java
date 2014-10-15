package com.connalle.android.application.notepad.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import com.connalle.android.application.notepad.R;
import com.connalle.android.application.notepad.utils.Constants;
import com.connalle.android.application.notepad.utils.Utility;

public class SettingsActivity extends Activity implements RadioGroup.OnCheckedChangeListener {
    private final String TAG = Constants.APP_TAG + ":" + getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        RadioGroup radioGroupTheme = (RadioGroup) findViewById(R.id.rg_theme_select);
        radioGroupTheme.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        Log.d(TAG, Utility.GetMethodName());
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        switch (id) {
            case R.id.radio_red:
                Log.d(TAG, "Red Theme Selected");
                editor.putInt(Constants.PREFERENCE_THEME, R.style.NotePadApp_Red);
                break;

            case R.id.radio_blue:
                Log.d(TAG, "Blue Theme Selected");
                editor.putInt(Constants.PREFERENCE_THEME, R.style.NotePadApp_Blue);
                break;

            case R.id.radio_green:
                Log.d(TAG, "Green Theme Selected");
                editor.putInt(Constants.PREFERENCE_THEME, R.style.NotePadApp_Green);
                break;

            case R.id.radio_purple:
                Log.d(TAG, "Purple Theme Selected");
                editor.putInt(Constants.PREFERENCE_THEME, R.style.NotePadApp_Purple);
                break;

            case R.id.radio_orange:
                Log.d(TAG, "Orange Theme Selected");
                editor.putInt(Constants.PREFERENCE_THEME, R.style.NotePadApp_Orange);
                break;

            default:
        }

        editor.commit();
        finish();
        startActivity(getIntent());
    }
}
