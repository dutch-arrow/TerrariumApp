package nl.das.terrarium.restclient;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import nl.das.terrarium.RequestQueueSingleton;
import nl.das.terrarium.VoidRequest;
import nl.das.terrarium.data.Rule;
import nl.das.terrarium.data.Rules;
import nl.das.terrarium.data.ToDo;
import nl.das.terrarium.dialogs.NotificationDialog;
import nl.das.terrarium.dialogs.WaitSpinner;
import nl.das.terrarium.restclient.json.Program;
import nl.das.terrarium.restclient.json.Programs;

/**
 * A ViewModel that is also an Observable,
 * to be used with the Data Binding Library.
 */
public class ProgramViewModel {

    private static ProgramViewModel instance;
    private Rules[] rules = new Rules[2];
    private WaitSpinner wait;
    private static int counter;

    private ProgramViewModel(Context context) {
        wait = new WaitSpinner(context);
        wait.start();
        counter = 0;
        rules[0] = new Rules();
        rules[1] = new Rules();
        loadPrograms(context, 0);
        loadPrograms(context, 1);
    }

    public static ProgramViewModel getInstance(Context context) {
        if (instance == null) {
            instance = new ProgramViewModel(context);
        }
        return instance;
    }

    public Rules getRules(int dorn) {
        return rules[dorn];
    }

    public void refresh(Context context, int dorn) {
        loadPrograms(context, dorn);
    }

    private void loadPrograms(Context context, int dorn) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String url = "http://" + prefs.getString("terrarium_ip_address", "") + "/rule/" + dorn;
        Log.i("Terrarium", "Execute GET request " + url);
        // Request programs.
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                (Response.Listener<JSONObject>) response -> {
                    try {
                        Gson gson = new Gson();
                        Program prog = gson.fromJson(response.toString(), Programs.class).getProgram();
                        Rules r = new Rules(prog);
                        rules[dorn].saveActive(r.getActive());
                        rules[dorn].saveTimeOfDay(r.getTimeOfDay());
                        rules[dorn].saveTempIdeal(r.getTempIdeal());
                        rules[dorn].saveHumIdeal(r.getHumIdeal());
                        String value = r.getRule(0).getValue();
                        if (value.equalsIgnoreCase("0")) {
                            rules[dorn].getRule(0).saveValue(value);
                        } else {
                            rules[dorn].getRule(0).saveValue(value.substring(1));
                        }
                        rules[dorn].getRule(0).getAction(0).setDevice(r.getRule(0).getAction(0).getDevice());
                        rules[dorn].getRule(0).getAction(0).saveOnPeriod(r.getRule(0).getAction(0).getOnPeriod());
                        rules[dorn].getRule(0).getAction(1).setDevice(r.getRule(0).getAction(1).getDevice());
                        rules[dorn].getRule(0).getAction(1).saveOnPeriod(r.getRule(0).getAction(1).getOnPeriod());
                        rules[dorn].getRule(1).saveValue(r.getRule(1).getValue());
                        rules[dorn].getRule(1).getAction(0).setDevice(r.getRule(1).getAction(0).getDevice());
                        rules[dorn].getRule(1).getAction(0).saveOnPeriod(r.getRule(1).getAction(0).getOnPeriod());
                        rules[dorn].getRule(1).getAction(1).setDevice(r.getRule(1).getAction(1).getDevice());
                        rules[dorn].getRule(1).getAction(1).saveOnPeriod(r.getRule(1).getAction(1).getOnPeriod());
                        value = r.getRule(2).getValue();
                        if (value.equalsIgnoreCase("0")) {
                            rules[dorn].getRule(2).saveValue(value);
                        } else {
                            rules[dorn].getRule(2).saveValue(value.substring(1));
                        }
                        rules[dorn].getRule(2).getAction(0).setDevice(r.getRule(2).getAction(0).getDevice());
                        rules[dorn].getRule(2).getAction(0).saveOnPeriod(r.getRule(2).getAction(0).getOnPeriod());
                        rules[dorn].getRule(2).getAction(1).setDevice(r.getRule(2).getAction(1).getDevice());
                        rules[dorn].getRule(2).getAction(1).saveOnPeriod(r.getRule(2).getAction(1).getOnPeriod());
                        rules[dorn].getRule(3).saveValue(r.getRule(3).getValue());
                        rules[dorn].getRule(3).getAction(0).setDevice(r.getRule(3).getAction(0).getDevice());
                        rules[dorn].getRule(3).getAction(0).saveOnPeriod(r.getRule(3).getAction(0).getOnPeriod());
                        rules[dorn].getRule(3).getAction(1).setDevice(r.getRule(3).getAction(1).getDevice());
                        rules[dorn].getRule(3).getAction(1).saveOnPeriod(r.getRule(3).getAction(1).getOnPeriod());
                        if (counter == 1) {
                            wait.dismiss();
                        } else {
                            counter++;
                        }
                        Log.i("Terrarium", "Received " + (dorn == 0 ? "day" : "night") + " program");
                    }
                    catch (JsonSyntaxException e) {
                        wait.dismiss();
                        new NotificationDialog(context, "Error", "Response bevat fouten:\n" + e.getMessage()).show();
                    }
                },
                (Response.ErrorListener) error -> {
                    Log.i("Terrarium", "Error " + (dorn == 0 ? "day" : "night") + " program: " + error.getMessage());
                    wait.dismiss();
                    new NotificationDialog(context, "Error in " + (dorn == 0 ? "day" : "night") + " program", "Kontakt met Terrarium Control Unit verloren.").show();
                }
        );
        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(context).add(jsonArrayRequest);
        Log.i("Terrarium", "GET request is queued");
    }

    public void saveProgram(Context context, int dorn) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String url = "http://" + prefs.getString("terrarium_ip_address", "") + "/rule/" + dorn;
        Log.i("Terrarium","Execute PUT request " + url);
        Gson gson = new Gson();
        Programs progs = new Programs(rules[dorn].toProgram());
        // Make sure the "below" values are negative
        if (progs.getProgram().getProgramData().get(0).getValue() > 0) {
            progs.getProgram().getProgramData().get(0).setValue(progs.getProgram().getProgramData().get(0).getValue() * -1);
        }
        if (progs.getProgram().getProgramData().get(2).getValue() > 0) {
            progs.getProgram().getProgramData().get(2).setValue(progs.getProgram().getProgramData().get(2).getValue() * -1);
        }
        String json = gson.toJson(progs);
        Log.i("Terrarium","json" + json);
        // Request sensors
        VoidRequest jsonRequest = new VoidRequest(Request.Method.PUT, url, json,
                response -> Log.i("Terrarium", "Saved " + (dorn == 0 ? "day" : "night") + " program"),
                error -> {
                    Log.i("Terrarium","Error " + (dorn == 0 ? "day" : "night") + " program: " + error.getMessage());
                    new NotificationDialog(context, "Error in " + (dorn == 0 ? "day" : "night") + " program", "Kontakt met Terrarium Control Unit verloren.").show();
                }
        );
        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(context).add(jsonRequest);
    }
}
