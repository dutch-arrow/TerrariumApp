package nl.das.terrarium;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import nl.das.terrarium.dialogs.NotificationDialog;
import nl.das.terrarium.dialogs.WaitSpinner;
import nl.das.terrarium.fragments.HelpFragment;
import nl.das.terrarium.fragments.ProgramFragment;
import nl.das.terrarium.fragments.SettingsFragment;
import nl.das.terrarium.fragments.StateFragment;
import nl.das.terrarium.fragments.TimersFragment;
import nl.das.terrarium.restclient.json.Properties;

public class TerrariumApp extends AppCompatActivity {

    public static List<String> devices = Arrays.asList("light1", "light2", "light3", "light4", "light5", "light6", "pump", "sprayer", "mist", "fan_in", "fan_out");
    public static Properties props;
    private Toolbar mTopToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTopToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mTopToolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_state_item) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame, new StateFragment());
            ft.commit();
            return true;
        }
        if (id == R.id.menu_timers_item) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame, new TimersFragment());
            ft.commit();
            return true;
        }
        if (id == R.id.menu_program_item) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame, ProgramFragment.newInstance());
            ft.commit();
            return true;
        }
        if (id == R.id.menu_config_item) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame, new SettingsFragment());
            ft.commit();
            return true;
        }
        if (id == R.id.menu_help_item) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame, new HelpFragment());
            ft.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        final WaitSpinner wait = new WaitSpinner(this);
        wait.start();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = "http://" + prefs.getString("terrarium_ip_address", "") + "/properties";
        Log.i("Terrarium","Execute request " + url);
        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Gson gson = new Gson();
                    props = gson.fromJson(response.toString(), Properties.class);
                    Log.i("Terrarium", "Terrarium Control Unit found on the network.");
                    wait.dismiss();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_frame, new StateFragment());
                    ft.commit();
                },
                error -> {
                    wait.dismiss();
//                    new NotificationDialog(this, "Error", "Terrarium Control Unit is niet bekend op het netwerk met dit IP adres.").show();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_frame, new SettingsFragment());
                    ft.commit();
                }
        );
        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(getApplication()).add(jsonRequest);
        super.onStart();
    }
}