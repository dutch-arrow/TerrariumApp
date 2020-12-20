package nl.das.terrarium.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import nl.das.terrarium.RequestQueueSingleton;
import nl.das.terrarium.TerrariumApp;
import nl.das.terrarium.VoidRequest;
import nl.das.terrarium.data.TimersItem;
import nl.das.terrarium.dialogs.NotificationDialog;
import nl.das.terrarium.dialogs.WaitSpinner;
import nl.das.terrarium.R;
import nl.das.terrarium.restclient.json.Properties;
import nl.das.terrarium.restclient.json.Timer;
import nl.das.terrarium.restclient.TimersViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimersListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimersListFragment extends Fragment {

    private String deviceID;
    private Button btnSave;
    private List<TimersItem> items = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Properties props;
    private TimersViewModel model;

    private WaitSpinner wait;

    public TimersListFragment() {
        // Required empty public constructor
    }

    public static TimersListFragment newInstance(String device) {
        TimersListFragment fragment = new TimersListFragment();
        Bundle args = new Bundle();
        args.putString("deviceID", device);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        props = TerrariumApp.props;
        deviceID = getArguments().getString("deviceID");
        // Get the ViewModel.
        model = new ViewModelProvider(this).get(TimersViewModel.class);
        // Create the observer which updates the UI.
        Observer<List<TimersItem>> timersObserver = timers -> {
            // Update the UI
            if (timers != null) {
                Log.i("Terrarium", "List of timers have changed.");
                updateTimers(timers);
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        wait = new WaitSpinner(requireActivity());
        wait.start();
        model.getTimers(deviceID).observe(this, timersObserver);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timers_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnSave = (Button) view.findViewById(R.id.ti_btnSave);
        btnSave.setEnabled(false);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDeviceTimers();
                btnSave.setEnabled(false);
            }
        });
        Button btnRefresh = (Button) view.findViewById(R.id.ti_btnRefresh);
        btnRefresh.setEnabled(true);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.refresh(deviceID);
                btnSave.setEnabled(false);
            }
        });
        if (items.size() == 0) {
            int nr = 0;
            for (int i = 0; i < nr; i++) {
                items.add(new TimersItem(
                        new Timer(deviceID, i+1, "on", 24, 60, 0, 0),
                        new Timer(deviceID, i+1,  "on", 24, 60, 0, 0),
                        false));
            }
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.ti_ListView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TimersListAdapter(items, btnSave);
        recyclerView.setAdapter(adapter);
    }

    private void updateTimers(List<TimersItem> items) {
        this.items = items;
        adapter = new TimersListAdapter(items, btnSave);
        recyclerView.setAdapter(adapter);
        wait.dismiss();
    }

    private void saveDeviceTimers() {
        Log.i("Terrarium", "Saving " + items.size() + " timer items");
        List<Timer> devTimers = new ArrayList<>();
        for (TimersItem ti : items) {
            List<Timer> tl = ti.toTimers();
            devTimers.addAll(tl);
        }
        Gson gson = new Gson();
        String json = gson.toJson(devTimers);
        wait.start();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String url = "http://" + prefs.getString("terrarium_ip_address", "") + "/timers/" + deviceID;
        Log.i("Terrarium","Execute PUT request " + url);
        // Save timers
        VoidRequest req = new VoidRequest(Request.Method.PUT, url, json,
                (Response.Listener<Void>) response -> {
                    Log.i("Terrarium", "The timers have been saved.");
                    Log.i("Terrarium", json);
                    wait.dismiss();
                },
                (Response.ErrorListener) error -> {
                    wait.dismiss();
                    Log.i("Terrarium","Error " + error.getMessage());
                    new NotificationDialog(getContext(), "Error", "Kontakt met Terrarium Control Unit verloren.").show();
                }
        );
        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(getContext()).add(req);
    }

}