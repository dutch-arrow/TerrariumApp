package nl.das.terrarium.fragments;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nl.das.terrarium.R;
import nl.das.terrarium.data.Rules;
import nl.das.terrarium.data.TimersItem;
import nl.das.terrarium.dialogs.NotificationDialog;
import nl.das.terrarium.dialogs.WaitSpinner;
import nl.das.terrarium.restclient.ProgramViewModel;
import nl.das.terrarium.restclient.TimersViewModel;
import nl.das.terrarium.restclient.json.Action;
import nl.das.terrarium.restclient.json.Program;
import nl.das.terrarium.restclient.json.ProgramData;
import nl.das.terrarium.restclient.json.Programs;

public class ProgramFragment extends Fragment {

    public ProgramFragment() {
        // Required empty public constructor
    }

    public static ProgramFragment newInstance() {
        ProgramFragment fragment = new ProgramFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_program, container, false).getRootView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int backcolorSel = getResources().getColor(R.color.colorPrimaryDark, null);
        int backcolor = getResources().getColor(R.color.notActive, null);
        final Button[] btnCurrent = new Button[1];
        Button btnDrying = (Button) view.findViewById(R.id.prg_btnDrying);
        btnDrying.setOnClickListener(v -> {
            btnDrying.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolorSel, PorterDuff.Mode.SRC));
            btnDrying.setTextColor(getResources().getColor(R.color.white, null));
            if( btnCurrent[0] != null && btnCurrent[0] != btnDrying) {
                btnCurrent[0].getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolor, PorterDuff.Mode.SRC));
                btnCurrent[0].setTextColor(getResources().getColor(R.color.black, null));
            }
            btnCurrent[0] = btnDrying;
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.program_frame, new ProgramDryingFragment());
            ft.commit();
        });
        Button btnDayProgTemp = (Button) view.findViewById(R.id.prg_btnDayTemp);
        btnDayProgTemp.setOnClickListener(v -> {
            btnDayProgTemp.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolorSel, PorterDuff.Mode.SRC));
            btnDayProgTemp.setTextColor(getResources().getColor(R.color.white, null));
            if( btnCurrent[0] != null && btnCurrent[0] != btnDayProgTemp) {
                btnCurrent[0].getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolor, PorterDuff.Mode.SRC));
                btnCurrent[0].setTextColor(getResources().getColor(R.color.black, null));
            }
            btnCurrent[0] = btnDayProgTemp;
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.program_frame, ProgramRulesFragment.newInstance(ProgramRulesFragment.DAY_PROGRAM, 0));
            ft.commit();
        });
        Button btnDayProgHum = (Button) view.findViewById(R.id.prg_btnDayHum);
        btnDayProgHum.setOnClickListener(v -> {
            btnDayProgHum.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolorSel, PorterDuff.Mode.SRC));
            btnDayProgHum.setTextColor(getResources().getColor(R.color.white, null));
            if( btnCurrent[0] != null && btnCurrent[0] != btnDayProgHum) {
                btnCurrent[0].getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolor, PorterDuff.Mode.SRC));
                btnCurrent[0].setTextColor(getResources().getColor(R.color.black, null));
            }
            btnCurrent[0] = btnDayProgHum;
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.program_frame, ProgramRulesFragment.newInstance(ProgramRulesFragment.DAY_PROGRAM, 1));
            ft.commit();
        });
        Button btnNightProgTemp = (Button) view.findViewById(R.id.prg_btnNightTemp);
        btnNightProgTemp.setOnClickListener(v -> {
            btnNightProgTemp.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolorSel, PorterDuff.Mode.SRC));
            btnNightProgTemp.setTextColor(getResources().getColor(R.color.white, null));
            if( btnCurrent[0] != null && btnCurrent[0] != btnNightProgTemp) {
                btnCurrent[0].getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolor, PorterDuff.Mode.SRC));
                btnCurrent[0].setTextColor(getResources().getColor(R.color.black, null));
            }
            btnCurrent[0] = btnNightProgTemp;
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.program_frame, ProgramRulesFragment.newInstance(ProgramRulesFragment.NIGHT_PROGRAM, 2));
            ft.commit();
        });
        Button btnNightProgHum = (Button) view.findViewById(R.id.prg_btnNightHum);
        btnNightProgHum.setOnClickListener(v -> {
            btnNightProgHum.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolorSel, PorterDuff.Mode.SRC));
            btnNightProgHum.setTextColor(getResources().getColor(R.color.white, null));
            if( btnCurrent[0] != null && btnCurrent[0] != btnNightProgHum) {
                btnCurrent[0].getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolor, PorterDuff.Mode.SRC));
                btnCurrent[0].setTextColor(getResources().getColor(R.color.black, null));
            }
            btnCurrent[0] = btnNightProgHum;
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.program_frame, ProgramRulesFragment.newInstance(ProgramRulesFragment.NIGHT_PROGRAM, 3));
            ft.commit();
        });
    }
}