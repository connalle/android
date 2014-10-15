package com.connalle.android.application.notepad.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.connalle.android.application.notepad.R;

public class DateTimePickerFragment extends DialogFragment implements View.OnClickListener {

    DateTimePickerCommunicator datePickerCommunicator;
    DatePicker datePicker;
    TimePicker timePicker;
    Button set, cancel;

    public static DateTimePickerFragment GetInstance() {
        return new DateTimePickerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Set Date and Time");
        View view = inflater.inflate(R.layout.fragment_date_picker, null);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        set = (Button) view.findViewById(R.id.btn_set);
        cancel = (Button) view.findViewById(R.id.btn_cancel);
        set.setOnClickListener(this);
        cancel.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        datePickerCommunicator = (DateTimePickerCommunicator) activity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_set:
                dismiss();
                datePickerCommunicator.onDateTimeSelected(datePicker.getYear(),
                        datePicker.getMonth(), datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                break;

            case R.id.btn_cancel:
                dismiss();
                datePickerCommunicator.onDateTimeSelected(-1, -1, -1, -1, -1);
                break;

            default:
                datePickerCommunicator.onDateTimeSelected(-1, -1, -1, -1, -1);
        }
    }

    public interface DateTimePickerCommunicator {
        public void onDateTimeSelected(int year, int month, int day, int hour, int min);
    }
}
