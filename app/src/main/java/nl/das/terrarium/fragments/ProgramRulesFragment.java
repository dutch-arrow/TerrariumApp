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
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import nl.das.terrarium.R;
import nl.das.terrarium.data.Rules;
import nl.das.terrarium.databinding.FragmentPrgRulesBinding;
import nl.das.terrarium.dialogs.NotificationDialog;
import nl.das.terrarium.dialogs.WaitSpinner;
import nl.das.terrarium.restclient.ProgramViewModel;
import nl.das.terrarium.restclient.json.Action;
import nl.das.terrarium.restclient.json.Program;
import nl.das.terrarium.restclient.json.Programs;

public class ProgramRulesFragment extends Fragment {
    public final static int DAY_PROGRAM = 0;
    public static final int NIGHT_PROGRAM = 1;

    private static int progNr;
    private static int ruleNr;
    private InputMethodManager imm;

    private Button btnRefresh;
    private Button btnSave;
    private SwitchCompat active;
    private EditText edtStart;
    private EditText edtIdealHum;
    private EditText edtIdealTemp;

    private ProgramViewModel model;
    private Rules rules = new Rules();

    public ProgramRulesFragment() { }

    public static ProgramRulesFragment newInstance(int dayOrNight, int rule) {
        ProgramRulesFragment fragment = new ProgramRulesFragment();
        progNr = dayOrNight;
        ruleNr = rule;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ProgramViewModel.getInstance(requireActivity());
        rules = model.getRules(progNr);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view and obtain an instance of the binding class.
        FragmentPrgRulesBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_prg_rules, container, false);
        // Assign the component to a property in the binding class.
        binding.setRules(rules);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        btnRefresh = (Button) view.findViewById(R.id.prg_btnRefresh);
        btnRefresh.setEnabled(true);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.refresh(requireActivity().getApplicationContext(),progNr);
                btnSave.setEnabled(false);
                Log.i("Terrarium","refresh Program");
            }
        });
        btnSave = (Button) view.findViewById(R.id.prg_btnSave);
        btnSave.setEnabled(false);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean thereAreErrors = false;
                for(int index = 0; index < ((ViewGroup) view).getChildCount(); index++) {
                    View nextChild = ((ViewGroup) view).getChildAt(index);
                    nextChild.clearFocus();
                }
                model.saveProgram(requireActivity().getApplicationContext(), progNr);
                btnSave.setEnabled(false);
                Log.i("Terrarium", "saveProgram");
            }
        });
        active = ((SwitchCompat) view.findViewById(R.id.pg_swActive));
        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave.setEnabled(true);
                model.getRules(progNr).saveActive(active.isChecked());
            }
        });

        edtStart = (EditText) view.findViewById(R.id.pg_edtStart);
        edtStart.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                imm.hideSoftInputFromWindow(edtStart.getWindowToken(), 0);
                // Validate Time On
                String value = String.valueOf(edtStart.getText()).trim();
                if (value.length() == 0) {
                    model.getRules(progNr).saveTimeOfDay("");
                    edtIdealHum.requestFocus();
                    btnSave.setEnabled(true);
                } else {
                    String[] parts = value.split("\\.");
                    int hr = 0;
                    int min = 0;
                    if (parts.length == 2) {
                        try {
                            hr = Integer.parseInt(parts[0].trim());
                            if (hr < 0 || hr > 23) {
                                edtStart.setError("Uuropgave moet tussen 0 en 23 zijn");
                            }
                        }
                        catch (NumberFormatException e) {
                            edtStart.setError("Uuropgave is geen getal");
                        }
                        try {
                            min = Integer.parseInt(parts[1].trim());
                            if (min < 0 || min > 59) {
                                edtStart.setError("Minutenopgave moet tussen 0 en 59 zijn");
                            }
                        }
                        catch (NumberFormatException e) {
                            edtStart.setError("Minutenopgave is geen getal");
                        }
                    } else {
                        edtStart.setError("Tijdopgave is niet juist. Formaat: hh.mm");
                    }
                    if (edtStart.getError() != null) {
                        model.getRules(progNr).saveTimeOfDay(value);
                        edtIdealHum.requestFocus();
                        btnSave.setEnabled(true);
                    }
                }
            }
            return false;
        });

        edtIdealHum = (EditText) view.findViewById(R.id.pg_edtHum);
        edtIdealHum.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                imm.hideSoftInputFromWindow(edtIdealHum.getWindowToken(), 0);
                String value = String.valueOf(edtIdealHum.getText()).trim();
                try {
                    int rv = Integer.parseInt(value);
                    if (rv >= 40 && rv <= 99) {
                        model.getRules(progNr).saveHumIdeal(rv + "");
                    } else {
                        edtIdealHum.setError("Vochtigheid moet een getal tussen 40 en 99 zijn.");
                    }
                }
                catch (NumberFormatException e) {
                    edtIdealHum.setError("Vochtigheid moet een getal tussen 40 en 99 zijn.");
                }
                if (edtIdealTemp.getError() == null) {
                    edtIdealTemp.requestFocus();
                    btnSave.setEnabled(true);
                }
            }
            return false;
        });

        edtIdealTemp = (EditText) view.findViewById(R.id.pg_edtTemp);
        edtIdealTemp.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                imm.hideSoftInputFromWindow(edtIdealTemp.getWindowToken(), 0);
                String value = String.valueOf(edtIdealTemp.getText()).trim();
                try {
                    int rv = Integer.parseInt(value);
                    if (rv >= 15 && rv <= 40) {
                        model.getRules(progNr).saveTempIdeal(rv + "");
                    } else {
                        edtIdealTemp.setError("Temperatuur moet een getal tussen 15 en 40 zijn.");
                    }
                }
                catch (NumberFormatException e) {
                    edtIdealTemp.setError("Temperatuur moet een getal tussen 15 en 40 zijn.");
                }
                active.requestFocus();
                btnSave.setEnabled(true);
            }
            return false;
        });
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        switch (ruleNr) {
            case 0:
            case 2:
                ft.replace(R.id.prg_frv_temphum, ProgramTempRulesFragment.newInstance(progNr, btnSave));
                break;
            case 1:
            case 3:
                ft.replace(R.id.prg_frv_temphum, ProgramHumRulesFragment.newInstance(progNr, btnSave));
                break;
        }
        ft.commit();
    }
}
