package com.connalle.android.application.notepad.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.connalle.android.application.notepad.R;
import com.connalle.android.application.notepad.utils.Constants;

//TODO:Change design of the dialog box
public class DeleteDialogFragment extends DialogFragment implements View.OnClickListener {
    private final String TAG = Constants.APP_TAG + ":" + getClass().getSimpleName();

    DeleteDialogCommunicator dialogCommunicator;
    Button ok, cancel;
    TextView title;

    public static DeleteDialogFragment GetInstance(String title) {
        DeleteDialogFragment dialogFragment = new DeleteDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_dialog, null);
        ok = (Button) view.findViewById(R.id.btn_ok);
        cancel = (Button) view.findViewById(R.id.btn_cancel);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);

        title = (TextView) view.findViewById(R.id.tv_title);
        title.setText(getArguments().getString("title"));

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dialogCommunicator = (DeleteDialogCommunicator) activity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                dismiss();
                dialogCommunicator.onDialogMessage(String.valueOf(R.string.ok));
                break;

            case R.id.btn_cancel:
                dismiss();
                dialogCommunicator.onDialogMessage(String.valueOf(R.string.cancel));
                break;

            default:
                Log.w(TAG, "Invalid button clicked");
        }
    }

    public interface DeleteDialogCommunicator {
        public void onDialogMessage(String message);
    }
}
