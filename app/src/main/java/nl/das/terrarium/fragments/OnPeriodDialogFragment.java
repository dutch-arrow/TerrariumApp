package nl.das.terrarium.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import nl.das.terrarium.R;

public class OnPeriodDialogFragment extends DialogFragment  implements TextView.OnEditorActionListener {
    private String device;
    private EditText mEditText;

    public OnPeriodDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static OnPeriodDialogFragment newInstance(String device) {
        OnPeriodDialogFragment frag = new OnPeriodDialogFragment();
        Bundle args = new Bundle();
        args.putString("device", device);
        frag.setArguments(args);
        return frag;
    }

    // Interface that must be implemented by the StateFragment class
    // So that the result can be communicated back.
    public interface OnPeriodDialogListener {
        public void onSave(String device, int onPeriod);
    }

    // Fires whenever the textfield has an action performed
    // In this case, when the "Done" button is pressed
    // REQUIRES a 'soft keyboard' (virtual keyboard)
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            OnPeriodDialogListener listener = (OnPeriodDialogListener) getTargetFragment();
            String nr = mEditText.getText().toString();
            if (nr.isEmpty()) {
                listener.onSave(device, 0);
            } else {
                listener.onSave(device, Integer.parseInt(nr));
            }
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_onperiod, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditText = (EditText) view.findViewById(R.id.edtTextOnPeriod);
        // Fetch arguments from bundle and set title
        device= getArguments().getString("device");
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);
    }
}
