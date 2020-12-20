package nl.das.terrarium.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nl.das.terrarium.R;
import nl.das.terrarium.RequestQueueSingleton;
import nl.das.terrarium.dialogs.NotificationDialog;
import nl.das.terrarium.dialogs.WaitSpinner;
import nl.das.terrarium.restclient.json.Sensor;
import nl.das.terrarium.restclient.json.Sensors;
import nl.das.terrarium.restclient.json.State;
import nl.das.terrarium.restclient.StateViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StateFragment extends Fragment implements OnPeriodDialogFragment.OnPeriodDialogListener, ClockDialogFragment.ClockDialogListener {

    private int onPeriod;
    private StateViewModel model;

    private TextView tvwDateTime;
    private TextView tvwTHum;
    private TextView tvwTTemp;
    private TextView tvwRHum;
    private TextView tvwRTemp;
    private TextView tvwRules;

    private SwitchCompat swLamp1;
    private TextView tvwLamp1;
    private SwitchCompat swLamp2;
    private TextView tvwLamp2;
    private SwitchCompat swLamp3;
    private TextView tvwLamp3;
    private SwitchCompat swLamp4;
    private TextView tvwLamp4;
    private SwitchCompat swLamp5;
    private TextView tvwLamp5;
    private SwitchCompat swLamp6;
    private TextView tvwLamp6;
    private SwitchCompat swPump;
    private TextView tvwPump;
    private SwitchCompat swSprayer;
    private TextView tvwSprayer;
    private SwitchCompat swMist;
    private TextView tvwMist;
    private SwitchCompat swFanIn;
    private TextView tvwFanIn;
    private SwitchCompat swFanOut;
    private TextView tvwFanOut;

    private WaitSpinner wait;
    private StateFragment instance;

    public StateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment
     */
    public static StateFragment newInstance() {
        StateFragment fragment = new StateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        // Get the ViewModel.
        model = new ViewModelProvider(this).get(StateViewModel.class);
        // Create the observer which updates the UI.
        Observer<List<State>> stateObserver = states -> {
            // Update the UI
            if (states != null) {
                updateState(states);
            }
        };
        Observer<Sensors> sensorsObserver = sensors -> {
            // Update the UI
            if (sensors != null) {
                updateSensors(sensors);
                model.getState().observe(instance, stateObserver);
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        wait = new WaitSpinner(requireActivity());
        wait.start();
        model.getSensors().observe(this, sensorsObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_state, parent, false).getRootView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn = view.findViewById(R.id.refreshButton);
        btn.setOnClickListener(v -> {
            Log.i("Terrarium", "refresh State");
            wait.start();
            model.refresh();
        });
        Button btnClock = view.findViewById(R.id.st_btnClock);
        btnClock.setOnClickListener(v -> {
            Log.i("Terrarium", "set Clock");
            // Create an instance of the dialog fragment and show it
            ClockDialogFragment dlgClock = ClockDialogFragment.newInstance();
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            // SETS the target fragment for use later when sending results
            dlgClock.setTargetFragment(this, 300);
            dlgClock.show(fm, "ClockDialogFragment");
        });

        tvwDateTime = view.findViewById(R.id.st_tvwDateTime);
        tvwTHum = view.findViewById(R.id.st_tvwThum);
        tvwTTemp = view.findViewById(R.id.st_tvwTtemp);
        tvwRHum = view.findViewById(R.id.st_tvwRhum);
        tvwRTemp = view.findViewById(R.id.st_tvwRtemp);
        tvwRules = view.findViewById(R.id.st_tvwRules);

        swLamp1 = view.findViewById(R.id.switchLamp1);
        tvwLamp1 = view.findViewById(R.id.textViewLamp1);
        swLamp2 = view.findViewById(R.id.switchLamp2);
        tvwLamp2 = view.findViewById(R.id.textViewLamp2);
        swLamp3 = view.findViewById(R.id.switchLamp3);
        tvwLamp3 = view.findViewById(R.id.textViewLamp3);
        swLamp4 = view.findViewById(R.id.switchLamp4);
        tvwLamp4 = view.findViewById(R.id.textViewLamp4);
        swLamp5 = view.findViewById(R.id.switchLamp5);
        tvwLamp5 = view.findViewById(R.id.textViewLamp5);
        swLamp6 = view.findViewById(R.id.switchLamp6);
        tvwLamp6 = view.findViewById(R.id.textViewLamp6);
        swPump = view.findViewById(R.id.switchPump);
        tvwPump = view.findViewById(R.id.textViewPump);
        swSprayer = view.findViewById(R.id.switchSprayer);
        tvwSprayer = view.findViewById(R.id.textViewSprayer);
        swMist = view.findViewById(R.id.switchMist);
        tvwMist = view.findViewById(R.id.textViewMist);
        swFanIn = view.findViewById(R.id.switchFanIn);
        tvwFanIn = view.findViewById(R.id.textViewFanIn);
        swFanOut = view.findViewById(R.id.switchFanOut);
        tvwFanOut = view.findViewById(R.id.textViewFanOut);

        String devices[] = {"light1", "light2", "light3", "light4", "light5", "light6", "pump", "sprayer", "mist", "fan_in", "fan_out"};
        int switches[] = {R.id.switchLamp1, R.id.switchLamp2, R.id.switchLamp3, R.id.switchLamp4, R.id.switchLamp5, R.id.switchLamp6, R.id.switchPump, R.id.switchSprayer, R.id.switchMist,
                R.id.switchFanIn, R.id.switchFanOut};
        for (int sw = 0; sw < switches.length; sw++) {
            SwitchCompat s = view.findViewById(switches[sw]);
            String dev = devices[sw];
            s.setOnClickListener(v -> {
                if (s.isChecked()) {
                    switchDeviceOn(dev);
                } else {
                    switchDeviceOff(dev);
                }
            });
        }
    }

    public void updateSensors(Sensors sensors) {
        tvwDateTime.setText(sensors.getClock());
        String rls = "geen";
        switch (sensors.getRules()) {
            case "day":
                rls = "dag";
                break;
            case "night":
                rls = "nacht";
                break;
            case "off":
                rls = "uit";
                break;
            case "no":
                rls = "geen";
                break;
        }
        tvwRules.setText(rls);
        for (Sensor sensor: sensors.getSensors()) {
            if (sensor.getLocation().equalsIgnoreCase("room")) {
                tvwRHum.setText(sensor.getHumidity() + "");
                tvwRTemp.setText(sensor.getTemperature() + "");
            } else {
                tvwTHum.setText(sensor.getHumidity() + "");
                tvwTTemp.setText(sensor.getTemperature() + "");
            }
        }
    }

    public void updateState(List<State> states) {
        for (State s : states) {
            switch (s.getDevice()) {
                case "light1":
                    swLamp1.setChecked(s.getState().equalsIgnoreCase("on"));
                    tvwLamp1.setText(translateEndTime(s.getEndTime()));
                    break;
                case "light2":
                    swLamp2.setChecked(s.getState().equalsIgnoreCase("on"));
                    tvwLamp2.setText(translateEndTime(s.getEndTime()));
                    break;
                case "light3":
                    swLamp3.setChecked(s.getState().equalsIgnoreCase("on"));
                    tvwLamp3.setText(translateEndTime(s.getEndTime()));
                    break;
                case "light4":
                    swLamp4.setChecked(s.getState().equalsIgnoreCase("on"));
                    tvwLamp4.setText(translateEndTime(s.getEndTime()));
                    break;
                case "light5":
                    swLamp5.setChecked(s.getState().equalsIgnoreCase("on"));
                    tvwLamp5.setText(translateEndTime(s.getEndTime()));
                    break;
                case "light6":
                    swLamp6.setChecked(s.getState().equalsIgnoreCase("on"));
                    tvwLamp6.setText(translateEndTime(s.getEndTime()));
                    break;
                case "pump":
                    swPump.setChecked(s.getState().equalsIgnoreCase("on"));
                    tvwPump.setText(translateEndTime(s.getEndTime()));
                    break;
                case "sprayer":
                    swSprayer.setChecked(s.getState().equalsIgnoreCase("on"));
                    tvwSprayer.setText(translateEndTime(s.getEndTime()));
                    break;
                case "mist":
                    swMist.setChecked(s.getState().equalsIgnoreCase("on"));
                    tvwMist.setText(translateEndTime(s.getEndTime()));
                    break;
                case "fan_in":
                    swFanIn.setChecked(s.getState().equalsIgnoreCase("on"));
                    tvwFanIn.setText(translateEndTime(s.getEndTime()));
                    break;
                case "fan_out":
                    swFanOut.setChecked(s.getState().equalsIgnoreCase("on"));
                    tvwFanOut.setText(translateEndTime(s.getEndTime()));
                    break;
            }
        }
        wait.dismiss();
    }

    public void switchDeviceOn(String device) {
        // Show dialog to ask for on period
        // Create an instance of the dialog fragment and show it
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        OnPeriodDialogFragment editNameDialogFragment = OnPeriodDialogFragment.newInstance(device);
        // SETS the target fragment for use later when sending results
        editNameDialogFragment.setTargetFragment(this, 300);
        editNameDialogFragment.show(fm, "OnPeriodDialogFragment");
    }

    @Override
    public void onSave(String device, int onPeriod) {
        final WaitSpinner wait = new WaitSpinner(requireActivity());
        wait.start();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String url = "http://" + prefs.getString("terrarium_ip_address", "");
        if (onPeriod == 0) {
            url +=  "/device/" + device  + "/on";
        } else {
            url +=  "/device/" + device  + "/on/" + onPeriod;
        }
        Log.i("Terrarium","Execute PUT request " + url);
        // Set device on.
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.PUT, url,
                (Response.Listener<String>) response -> {
                    Log.i("Terrarium", "Set device " + device + " on");
                    wait.dismiss();
                },
                (Response.ErrorListener) error -> {
                    wait.dismiss();
                    Log.i("Terrarium","Error " + error.getMessage());
                    new NotificationDialog(getContext(), "Error", "Kontakt met Terrarium Control Unit verloren.").show();
                }
        );
        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(getContext()).add(jsonObjectRequest);
    }

    public void switchDeviceOff(String device) {
        final WaitSpinner wait = new WaitSpinner(requireActivity());
        wait.start();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String url = "http://" + prefs.getString("terrarium_ip_address", "") + "/device/" + device  + "/off";
        Log.i("Terrarium","Execute PUT request " + url);
        // Switch device off.
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.PUT, url,
                (Response.Listener<String>) response1 -> {
                    Log.i("Terrarium", "Switch device " + device + " off");
                    wait.dismiss();
                },
                (Response.ErrorListener) error -> {
                    wait.dismiss();
                    Log.i("Terrarium","Error " + error.getMessage());
                    new NotificationDialog(getContext(), "Error", "Kontakt met Terrarium Control Unit verloren.").show();
                }
        );
        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(getContext()).add(jsonArrayRequest);
    }

    private String translateEndTime(String endTime) {
        if (endTime.equalsIgnoreCase("no endtime")) {
            return "geen eindtijd";
        } else if (endTime.equalsIgnoreCase("until ideal temperature is reached")) {
            return "tot ideale temperatuur bereikt is";
        } else if (endTime.equalsIgnoreCase("until ideal humidity is reached")) {
            return "tot ideale vochtigheidsgraad bereikt is";
        } else {
            return endTime;
        }
    }

    @Override
    public void onClockSave(String dateTime) {
        final WaitSpinner wait = new WaitSpinner(requireActivity());
        wait.start();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String url = "http://" + prefs.getString("terrarium_ip_address", "") + "/setdate/" + dateTime;
        Log.i("Terrarium","Execute PUT request " + url);
        // Switch device off.
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
            (Response.Listener<String>) response1 -> {
                Log.i("Terrarium", "The clock has been set");
                try {
                    SimpleDateFormat fmtin = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date dtin = fmtin.parse(dateTime);
                    SimpleDateFormat fmtout = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                    String dtout = fmtout.format(dtin);
                    tvwDateTime.setText(dtout);
                }
                catch (ParseException e) {
                    // Cannot be
                }
                wait.dismiss();
            },
            (Response.ErrorListener) error -> {
                wait.dismiss();
                Log.i("Terrarium","Error " + error.getMessage());
                new NotificationDialog(getContext(), "Error", "Kontakt met Terrarium Control Unit verloren.").show();
            }
        );
        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(getContext()).add(jsonArrayRequest);
    }
}