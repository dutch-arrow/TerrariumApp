package nl.das.terrarium.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import nl.das.terrarium.R;
import nl.das.terrarium.VoidRequest;
import nl.das.terrarium.dialogs.NotificationDialog;
import nl.das.terrarium.dialogs.WaitSpinner;
import nl.das.terrarium.restclient.json.DryingData;

public class ProgramDryingFragment extends Fragment {
    private EditText edtStartAfter;
    private EditText edtPeriod;
    private InputMethodManager imm;
    DryingData data;

    public ProgramDryingFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prg_drying, container, false).getRootView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        Button btnRefresh = view.findViewById(R.id.pg_btnDryRefresh);
        Button btnSave = view.findViewById(R.id.pg_btnDrySave);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDryingData();
                btnSave.setEnabled(false);
            }
        });
        btnSave.setEnabled(false);
        btnSave.setOnClickListener(v -> {
            saveDryingData();
            btnSave.setEnabled(false);
        });

        edtStartAfter = view.findViewById(R.id.pg_edtStartAfter);
        edtStartAfter.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                edtPeriod.requestFocus();
                btnSave.setEnabled(true);
                imm.hideSoftInputFromWindow(edtStartAfter.getWindowToken(), 0);
                v.clearFocus();
            }
            return false;
        });
        edtStartAfter.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                int value = Integer.parseInt(String.valueOf(edtStartAfter.getText()).trim());
                if (!(value > 0 && value <= 120)) {
                    edtStartAfter.setError("Waarde moet tussen 0 en 120 zijn.");
                } else {
                    data.setStartFanPeriod(value);
                }
            }
        });

        edtPeriod = view.findViewById(R.id.pg_edtDryPeriod);
        edtPeriod.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                edtStartAfter.requestFocus();
                btnSave.setEnabled(true);
                imm.hideSoftInputFromWindow(edtPeriod.getWindowToken(), 0);
                v.clearFocus();
            }
            return false;
        });
        edtPeriod.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                int value = Integer.parseInt(String.valueOf(edtPeriod.getText()).trim());
                if (!(value > 0 && value <= 120)) {
                    edtPeriod.setError("Waarde moet tussen 0 en 120 zijn.");
                } else {
                    data.setFanPeriod(value);
                }
            }
        });


        getDryingData();
    }

    private void getDryingData() {
        final WaitSpinner wait = new WaitSpinner(requireActivity());
        wait.start();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String baseUrl = "http://" + prefs.getString("terrarium_ip_address", "") + "/fanperiod";
        Log.i("Terrarium","Execute request " + baseUrl);
        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, null,
                response -> {
                    Gson gson = new Gson();
                    data = gson.fromJson(response.toString(), DryingData.class);
                    Log.i("Terrarium", "Drying data has been retrieved.");
                    edtStartAfter.setText(data.getStartFanPeriod() + "");
                    edtPeriod.setText(data.getFanPeriod() + "");
                    wait.dismiss();
                },
                error -> {
                    wait.dismiss();
                    new NotificationDialog(getContext(), "Error", "Terrarium Control Unit is niet bekend op het netwerk met dit IP adres.").show();
                }
        );
        // Add the request to the RequestQueue.
        Volley.newRequestQueue(getContext()).add(jsonRequest);
    }

    private void saveDryingData() {
        final WaitSpinner wait = new WaitSpinner(requireActivity());
        wait.start();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String baseUrl = "http://" + prefs.getString("terrarium_ip_address", "") + "/fanperiod/" + data.getStartFanPeriod() + "/" + data.getFanPeriod();
        Log.i("Terrarium","Execute request " + baseUrl);
        // Save drying data
        VoidRequest req = new VoidRequest(Request.Method.PUT, baseUrl, null,
                (Response.Listener<Void>) response -> {
                    Log.i("Terrarium", "The drying data have been saved.");
                    wait.dismiss();
                },
                (Response.ErrorListener) error -> {
                    wait.dismiss();
                    Log.i("Terrarium","Error " + error.getMessage());
                    new NotificationDialog(getContext(), "Error", "Kontakt met Terrarium Control Unit verloren.").show();
                }
        );
        // Add the request to the RequestQueue.
        Volley.newRequestQueue(getContext()).add(req);

    }
}
