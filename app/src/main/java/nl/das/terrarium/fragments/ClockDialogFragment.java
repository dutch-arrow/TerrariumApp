package nl.das.terrarium.fragments;

import android.os.Bundle;
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
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nl.das.terrarium.R;

public class ClockDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText edtDatetime;

    public static ClockDialogFragment newInstance() {
        ClockDialogFragment frag = new ClockDialogFragment();
        return frag;
    }

    // Interface that must be implemented by the StateFragment class
    // So that the result can be communicated back.
    public interface ClockDialogListener {
        public void onClockSave(String dateTime);
    }
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            ClockDialogListener listener = (ClockDialogListener) getTargetFragment();
            String dt = edtDatetime.getText().toString();
            if (!dt.isEmpty()) {
                // Convert format '06/10/2020 15:30' to '2020-10-06T15:30:00'
                try {
                    SimpleDateFormat fmtchk = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Date dattim = fmtchk.parse(dt);
                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    String dtout = fmt.format(dattim);
                    listener.onClockSave(dtout);
                    // Close the dialog and return back to the parent activity
                    dismiss();
                    return true;
                }
                catch (ParseException e) {
                    edtDatetime.setError("Datum formaat is onjuist");
                }
            } else {
                // Close the dialog and return back to the parent activity
                dismiss();
                return true;
            }
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_clock, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtDatetime = (EditText) view.findViewById(R.id.dlg_edtClock);
        // Show soft keyboard automatically and request focus to field
        edtDatetime.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        edtDatetime.setOnEditorActionListener(this);
    }
}
