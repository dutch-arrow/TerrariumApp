package nl.das.terrarium.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import nl.das.terrarium.R;
import nl.das.terrarium.dialogs.NotificationDialog;
import nl.das.terrarium.dialogs.WaitSpinner;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        onSharedPreferenceChanged(sharedPreferences, "terrarium_ip_address");
    }

    @Override
    public void onResume() {
        //register the preferenceChange listener
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference instanceof EditTextPreference) {
            String ip = sharedPreferences.getString(key, "");
            preference.setSummary(ip);
            // Check if IP address is reachable
            final WaitSpinner wait = new WaitSpinner(requireActivity());
            wait.start();
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = "http://" + ip + "/sensors";
            Log.i("Terrarium","Execute request " + url);
            // Request a string response from the provided URL.
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("Terrarium", "Terrarium Control Unit found on the network.");
                            wait.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            wait.dismiss();
                            new NotificationDialog(getContext(), "Error", "Terrarium Control Unit is niet bekend op het netwerk met dit IP adres.").show();
                        }
                    }
            );
            // Add the request to the RequestQueue.
            queue.add(jsonRequest);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister the preference change listener
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}