package nl.das.terrarium.restclient;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import nl.das.terrarium.RequestQueueSingleton;
import nl.das.terrarium.dialogs.NotificationDialog;
import nl.das.terrarium.restclient.json.Sensors;
import nl.das.terrarium.restclient.json.State;

public class StateViewModel extends AndroidViewModel {
    private MutableLiveData<List<State>> states;
    private MutableLiveData<Sensors> sensors;

    public StateViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Sensors> getSensors() {
        if (sensors == null) {
            sensors = new MutableLiveData<Sensors>();
            loadSensors();
        }
        return sensors;
    }

    public LiveData<List<State>> getState() {
        if (states == null) {
            states = new MutableLiveData<List<State>>();
            loadState();
        }
        return states;
    }

    public void refresh() {
        loadState();
    }

    private void loadState() {
//        CountDownLatch cdl = new CountDownLatch(1);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication().getApplicationContext());
        String url = "http://" + prefs.getString("terrarium_ip_address", "") + "/state";
        Log.i("Terrarium","Execute GET request " + url);
        // Request state.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                (Response.Listener<JSONArray>) response1 -> {
                    Log.i("Terrarium", "Retrieved " + response1.length() + " states");
                    Gson gson = new Gson();
                    try {
                        states.setValue(gson.fromJson(response1.toString(), new TypeToken<List<State>>() {}.getType()));
//                        cdl.countDown();
                    } catch (JsonSyntaxException e) {
//                        cdl.countDown();
                        new NotificationDialog(getApplication().getApplicationContext(), "Error", "State response contains errors:\n" + e.getMessage()).show();
                    }
                },
                (Response.ErrorListener) error -> {
//                    cdl.countDown();
                    Log.i("Terrarium","Error " + error.getMessage());
                    new NotificationDialog(getApplication().getApplicationContext(), "Error", "Kontakt met Terrarium Control Unit verloren.").show();
                }
        );
        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(getApplication()).add(jsonArrayRequest);
//        try {
//            cdl.await(5, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    private void loadSensors() {
//        CountDownLatch cdl = new CountDownLatch(1);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication().getApplicationContext());
        String url = "http://" + prefs.getString("terrarium_ip_address", "") + "/sensors";
        Log.i("Terrarium","Execute GET request " + url);
        // Request sensors
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Log.i("Terrarium", "Retrieved sensors");
                    Gson gson = new Gson();
                    try {
                        sensors.setValue(gson.fromJson(response.toString(), Sensors.class));
//                        cdl.countDown();
                    } catch (JsonSyntaxException e) {
//                        cdl.countDown();
                        new NotificationDialog(getApplication().getApplicationContext(), "Error", "Sensor response contains errors:\n" + e.getMessage()).show();
                    }
                },
                error -> {
//                    cdl.countDown();
                    Log.i("Terrarium","Error " + error.getMessage());
                    new NotificationDialog(getApplication().getApplicationContext(), "Error", "Kontakt met Terrarium Control Unit verloren.").show();
                }
        );
        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(getApplication()).add(jsonRequest);
//        try {
//            cdl.await(5, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
