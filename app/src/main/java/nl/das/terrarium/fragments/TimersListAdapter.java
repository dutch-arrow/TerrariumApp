package nl.das.terrarium.fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nl.das.terrarium.BR;
import nl.das.terrarium.R;
import nl.das.terrarium.data.TimersItem;

public class TimersListAdapter extends RecyclerView.Adapter<TimersListAdapter.TimersItemViewHolder> {
    private List<TimersItem> timers;
    private Button btnSave;
    private EditText edtTimeOn;
    private EditText edtTimeOff;
    private EditText edtPeriod;
    private EditText edtRepeat;
    private InputMethodManager imm;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public  class TimersItemViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        public TimersItemViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Object obj) {
            binding.setVariable(BR.timerItem, obj);
            binding.executePendingBindings();
        }
    }

    public TimersListAdapter(final List<TimersItem> timers, Button btnSave) {
        this.timers = timers;
        this.btnSave = btnSave;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TimersItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_timer, parent, false);
        imm = (InputMethodManager)parent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return new TimersItemViewHolder(binding);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(TimersItemViewHolder holder, int position) {
        holder.bind(timers.get(position));
        ((TextView) holder.itemView.findViewById(R.id.it_tvwTimer)).setText("Timer " + timers.get(position).getIndex());

        edtTimeOn = ((EditText) holder.itemView.findViewById(R.id.it_edtTimeOn));
        edtTimeOn.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                imm.hideSoftInputFromWindow(edtTimeOn.getWindowToken(), 0);Log.i("Terrarium", "setOnEditorActionListener - before check");
                if (checkTime(edtTimeOn)) {
                    timers.get(position).saveTimeOn(edtTimeOn.getText().toString());
                    edtTimeOff.requestFocus();
                    btnSave.setEnabled(true);
                }
            }
            return false;
        });

        edtTimeOff = ((EditText) holder.itemView.findViewById(R.id.it_edtTimeOff));
        edtTimeOff.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                imm.hideSoftInputFromWindow(edtTimeOff.getWindowToken(), 0);
                if (checkTime(edtTimeOff)) {
                    timers.get(position).saveTimeOff(edtTimeOff.getText().toString());
                    edtRepeat.requestFocus();
                    btnSave.setEnabled(true);
                }
            }
            return false;
        });

        edtRepeat = (EditText) holder.itemView.findViewById(R.id.it_edtRepeat);
        edtRepeat.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                imm.hideSoftInputFromWindow(edtRepeat.getWindowToken(), 0);
                String value = String.valueOf(edtRepeat.getText()).trim();
                try {
                    int rv = Integer.parseInt(value);
                    if (rv < 1 || rv > 7) {
                        edtRepeat.setError("Herhaling moet een getal tussen 1 en 7 zijn.");
                    }
                }
                catch (NumberFormatException e) {
                    edtRepeat.setError("Herhaling moet een getal tussen 1 en 7 zijn.");
                }
                if (edtRepeat.getError() == null) {
                    timers.get(position).saveRepeat(edtRepeat.getText().toString());
                    edtPeriod.requestFocus();
                    btnSave.setEnabled(true);
                }
            }
            return false;
        });

        edtPeriod = (EditText) holder.itemView.findViewById(R.id.it_edtPeriod);
        edtPeriod.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                imm.hideSoftInputFromWindow(edtPeriod.getWindowToken(), 0);
                String value = String.valueOf(edtPeriod.getText()).trim();
                try {
                    int val = Integer.parseInt(value);
                    if (val < 0 || val > 3600) {
                        edtPeriod.setError("Periode moet een getal tussen 0 en 3600 zijn.");
                    }
                }
                catch (NumberFormatException e) {
                    edtPeriod.setError("Periode moet een getal tussen 0 en 3600 zijn.");
                }
                if (edtPeriod.getError() == null) {
                    timers.get(position).savePeriod(edtPeriod.getText().toString());
                    edtTimeOn.requestFocus();
                    btnSave.setEnabled(true);
                }
            }
            return false;
        });
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return timers.size();
    }

    public boolean checkTime(EditText field) {
        Log.i("Terrarium", "Check time input");
        String value = String.valueOf(field.getText()).trim();
        if (value.length() != 0) {
            String[] parts = value.split("\\.");
            if (parts.length == 2) {
                try {
                    int hr = Integer.parseInt(parts[0].trim());
                    if (hr < 0 || hr > 23) {
                        field.setError("Uuropgave moet tussen 0 en 23 zijn");
                    }
                } catch (NumberFormatException e) {
                    field.setError("Uuropgave is geen getal");
                }
                try {
                    int min = Integer.parseInt(parts[1].trim());
                    if (min < 0 || min > 59) {
                        field.setError("Minutenopgave moet tussen 0 en 59 zijn");
                    }
                } catch (NumberFormatException e) {
                    field.setError("Minutenopgave is geen getal");
                }
            } else {
                field.setError("Tijdopgave is niet juist. Formaat: hh.mm");
            }
        }
        return field.getError() == null;
    }
}
