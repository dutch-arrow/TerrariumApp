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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import nl.das.terrarium.R;
import nl.das.terrarium.VoidRequest;
import nl.das.terrarium.data.Rules;
import nl.das.terrarium.databinding.FragmentPrgHumRulesBinding;
import nl.das.terrarium.dialogs.NotificationDialog;
import nl.das.terrarium.dialogs.WaitSpinner;
import nl.das.terrarium.restclient.ProgramViewModel;
import nl.das.terrarium.restclient.json.Program;
import nl.das.terrarium.restclient.json.Programs;

public class ProgramHumRulesFragment extends Fragment {

    private static int progNr;
    private static Button btnSave;
    private EditText edtValue1;
    private EditText edtValue2;
    private Spinner ddnActionDevice[] = new Spinner[4];
    private RadioGroup rbgPeriod[] = new RadioGroup[4];
    private RadioButton rbnActionIdeal[] = new RadioButton[4];
    private RadioButton rbnActionPeriod[] = new RadioButton[4];
    private EditText edtActionPeriod[] = new EditText[4];

    private int lastPos[] = new int[4];
    private boolean userSelect[] = new boolean[4];

    private ProgramViewModel model;
    private Rules rules = new Rules();
    private InputMethodManager imm;
    private String[] devices;
    private String[] devicesLabel;

    public ProgramHumRulesFragment(int dayOrNight, Button save) {
        progNr = dayOrNight;
        btnSave = save;
    }

    public static ProgramHumRulesFragment newInstance(int dayOrNight, Button save) {
        return new ProgramHumRulesFragment(dayOrNight, save);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the ViewModel.
        model = ProgramViewModel.getInstance(requireActivity());
        rules = model.getRules(progNr);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view and obtain an instance of the binding class.
        FragmentPrgHumRulesBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_prg_hum_rules, container, false);
        // Assign the component to a property in the binding class.
        binding.setRule1(rules.getRule(2));
        binding.setRule2(rules.getRule(3));
        imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        devices = getResources().getStringArray(R.array.devices_array);
        devicesLabel = getResources().getStringArray(R.array.devices_label_array);
        edtValue1 = (EditText) view.findViewById(R.id.pg_edtValue_1);
        edtValue1.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                imm.hideSoftInputFromWindow(edtValue1.getWindowToken(), 0);
                btnSave.setEnabled(true);
                v.clearFocus();
            }
            return false;
        });
        edtValue1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String value = String.valueOf(edtValue1.getText()).trim();
                    if (value.trim().length() > 0) {
                        try {
                            int rv = Integer.parseInt(value);
                            if (rv >= 40 && rv <= 99) {
                                model.getRules(progNr).getRule(2).saveValue((-rv) + "");
                            } else {
                                edtValue1.setError("Vochtigheid moet een getal tussen 40 en 99 zijn.");
                            }
                        } catch (NumberFormatException e) {
                            edtValue1.setError("Vochtigheid moet een getal tussen 40 en 99 zijn.");
                        }
                    } else {
                        model.getRules(progNr).getRule(2).saveValue("");
                    }
                }
            }
        });
        edtValue2 = (EditText) view.findViewById(R.id.pg_edtValue_2);
        edtValue2.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                imm.hideSoftInputFromWindow(edtValue2.getWindowToken(), 0);
                btnSave.setEnabled(true);
                v.clearFocus();
            }
            return false;
        });
        edtValue2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String value = String.valueOf(edtValue2.getText()).trim();
                    if (value.trim().length() > 0) {
                        try {
                            int rv = Integer.parseInt(value);
                            if (rv >= 40 && rv <= 99) {
                                model.getRules(progNr).getRule(3).saveValue(rv + "");
                            } else {
                                edtValue2.setError("Vochtigheid moet een getal tussen 40 en 99 zijn.");
                            }
                        } catch (NumberFormatException e) {
                            edtValue2.setError("Vochtigheid moet een getal tussen 40 en 99 zijn.");
                        }
                    } else {
                        model.getRules(progNr).getRule(3).saveValue("");
                    }
                }
            }
        });
        for (int a = 0; a < 4; a++) {
            int fa = a;
            int resId = getResources().getIdentifier("pg_ddnDevices_" + (a + 1), "id", getContext().getPackageName());
            ddnActionDevice[a] = view.findViewById(resId);
            resId = getResources().getIdentifier("pg_rbgPeriod_" + (a + 1), "id", getContext().getPackageName());
            rbgPeriod[a] = view.findViewById(resId);
            resId = getResources().getIdentifier("pg_rbtPeriodIdeal_" + (a + 1), "id", getContext().getPackageName());
            rbnActionIdeal[a] = view.findViewById(resId);
            resId = getResources().getIdentifier("pg_rbtPeriodSec_" + (a + 1), "id", getContext().getPackageName());
            rbnActionPeriod[a] = view.findViewById(resId);
            resId = getResources().getIdentifier("pg_edtPeriod_" + (a + 1), "id", getContext().getPackageName());
            edtActionPeriod[a] = view.findViewById(resId);

            ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.devices_label_array, R.layout.spinner_list);
            ddnActionDevice[a].setAdapter(adapter);
            ddnActionDevice[a].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (lastPos[fa] != position) {
                        if (position == 0 && userSelect[fa]) {
                            rbgPeriod[fa].setEnabled(false);
                            rbnActionIdeal[fa].setEnabled(false);
                            edtActionPeriod[fa].setText("");
                            edtActionPeriod[fa].setEnabled(false);
                            model.getRules(progNr).getRule((fa / 2) + 2).getAction(fa % 2).setDevice(devices[position]);
                            rbnActionPeriod[fa].setEnabled(false);
                            btnSave.setEnabled(true);
                        } else if (position > 0 && userSelect[fa]) {
                            btnSave.setEnabled(true);
                            rbgPeriod[fa].setEnabled(true);
                            rbnActionIdeal[fa].setEnabled(true);
                            rbnActionPeriod[fa].setEnabled(true);
                            edtActionPeriod[fa].setEnabled(true);
                            model.getRules(progNr).getRule((fa / 2) + 2).getAction(fa % 2).setDevice(devices[position]);
                        } else if (position > 0 && !userSelect[fa]) {
                            rbgPeriod[fa].setEnabled(true);
                            rbnActionIdeal[fa].setEnabled(true);
                            rbnActionPeriod[fa].setEnabled(true);
                            edtActionPeriod[fa].setEnabled(true);
                        }
                        lastPos[fa] = position;
                        userSelect[fa] = true;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            rbnActionIdeal[a].setOnClickListener(v -> {
                model.getRules(progNr).getRule((fa / 2) + 2).getAction(fa % 2).saveOnPeriod("-2");
                edtActionPeriod[fa].setText("");
                edtActionPeriod[fa].setEnabled(false);
                rbnActionPeriod[fa].setChecked(false);
                rbnActionIdeal[fa].setChecked(true);
                btnSave.setEnabled(true);
            });
            rbnActionPeriod[a].setOnClickListener(v -> {
                edtActionPeriod[fa].setEnabled(true);
                rbnActionIdeal[fa].setChecked(false);
                btnSave.setEnabled(true);
            });

            edtActionPeriod[a].setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ddnActionDevice[fa].requestFocus();
                    btnSave.setEnabled(true);
                    imm.hideSoftInputFromWindow(edtActionPeriod[fa].getWindowToken(), 0);
                    v.clearFocus();
                }
                return false;
            });
            edtActionPeriod[a].setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    String value = String.valueOf(edtActionPeriod[fa].getText()).trim();
                    if (value.trim().length() > 0) {
                        try {
                            int rv = Integer.parseInt(value);
                            if (rv >= 0 && rv <= 3600) {
                                model.getRules(progNr).getRule((fa / 2) + 2).getAction(fa % 2).saveOnPeriod(rv + "");
                            } else {
                                edtActionPeriod[fa].setError("Periode moet een getal tussen 0 en 3600 zijn.");
                            }
                        } catch (NumberFormatException e) {
                            edtActionPeriod[fa].setError("Periode moet een getal tussen 0 en 3600 zijn.");
                        }
                    }
                }
            });
        }
    }

    private String getDeviceFromLabel(String device) {
        for (int i = 0; i < devicesLabel.length; i++) {
            if (devicesLabel[i].equalsIgnoreCase(device)) {
                return devices[i];
            }
        }
        return "";
    }
}
