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
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.das.terrarium.RequestQueueSingleton;
import nl.das.terrarium.dialogs.NotificationDialog;
import nl.das.terrarium.data.TimersItem;
import nl.das.terrarium.restclient.json.Timer;

public class TimersViewModel extends AndroidViewModel {

    private static final Map<String, MutableLiveData<List<TimersItem>>> timers = new HashMap<>();

    public TimersViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<TimersItem>> getTimers(String device) {
        Log.i("Terrarium", "timers.get(" + device + ")=" + timers.get(device));
        if (timers.get(device) == null) {
            Log.i("Terrarium", "Get the timers for device " + device + " from TCU");
            timers.put(device, new MutableLiveData<List<TimersItem>>());
            loadTimers(device);
        } else {
            Log.i("Terrarium", "Get the timers for device " + device + " from map");
        }
        return timers.get(device);
    }

    public void refresh(String device) {
        loadTimers(device);
    }

    private void loadTimers(String device) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication().getApplicationContext());
        String url = "http://" + prefs.getString("terrarium_ip_address", "") + "/timers/" + device;
        Log.i("Terrarium", "Execute GET request " + url);
        // Request timers.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                (Response.Listener<JSONArray>) response -> {
                    Log.i("Terrarium", "Retrieved " + response.length() + " timers");
                    try {
                        Gson gson = new Gson();
                        List<Timer> ts = gson.fromJson(response.toString(), new TypeToken<List<Timer>>() { }.getType());
                        List<TimersItem> items = new ArrayList<>();
                        for (int i = 0; i < ts.size(); i++) {
                            items.add(new TimersItem(ts.get(i), ts.get(i+1), true));
                            i++;
                        }
                        timers.get(device).setValue(items);
                        Log.i("Terrarium", "Received " + timers.get(device).getValue().size() + " timer items for device " + device);
                    }
                    catch (JsonSyntaxException e) {
                        new NotificationDialog(getApplication(), "Error", "Response bevat fouten:\n" + e.getMessage()).show();
                    }
                },
                (Response.ErrorListener) error -> {
                    if (error.getMessage() == null) {
                        Log.i("Terrarium", "Error " + error.getCause());
                    } else {
                        Log.i("Terrarium", "Error " + error.getMessage());
                    }
                    new NotificationDialog(getApplication(), "Error", "Kontakt met Terrarium Control Unit verloren.").show();
                }
        );
        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(getApplication()).add(jsonArrayRequest);
    }
}
