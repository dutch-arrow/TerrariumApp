package nl.das.terrarium.fragments;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.das.terrarium.R;
import nl.das.terrarium.data.TimersItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimersFragment extends Fragment {

    private static Map<String, List<TimersItem>> timers = new HashMap<>();
    private Button btnLight1;
    private Button btnLight2;
    private Button btnLight3;
    private Button btnLight4;
    private Button btnLight5;
    private Button btnLight6;
    private Button btnPump;
    private Button btnSprayer;
    private Button btnMist;
    private Button btnFanIn;
    private Button btnFanOut;

    public TimersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     */
    public static TimersFragment newInstance() {
        TimersFragment fragment = new TimersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timers, parent, false).getRootView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int backcolorSel = getResources().getColor(R.color.colorPrimaryDark, null);
        int backcolor = getResources().getColor(R.color.notActive, null);
        final Button[] btnCurrent = new Button[1];
        btnLight1 = (Button) view.findViewById(R.id.dev_btnLight1);
        btnLight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLight1.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolorSel, PorterDuff.Mode.SRC));
                btnLight1.setTextColor(getResources().getColor(R.color.white, null));
                if( btnCurrent[0] != null && btnCurrent[0] != btnLight1) {
                    btnCurrent[0].getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolor, PorterDuff.Mode.SRC));
                    btnCurrent[0].setTextColor(getResources().getColor(R.color.black, null));
                }
                btnCurrent[0] = btnLight1;
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.list_frame, TimersListFragment.newInstance("light1"));
                ft.commit();
            }
        });
        btnLight1.performClick();
        btnLight2 = (Button)view.findViewById(R.id.dev_btnLight2);
        btnLight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLight2.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolorSel, PorterDuff.Mode.SRC));
                btnLight2.setTextColor(getResources().getColor(R.color.white, null));
                if( btnCurrent[0] != null && btnCurrent[0] != btnLight2) {
                    btnCurrent[0].getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolor, PorterDuff.Mode.SRC));
                    btnCurrent[0].setTextColor(getResources().getColor(R.color.black, null));
                }
                btnCurrent[0] = btnLight2;
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.list_frame, TimersListFragment.newInstance("light2"));
                ft.commit();
            }
        });
        btnLight3 = (Button)view.findViewById(R.id.dev_btnLight3);
        btnLight3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLight3.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolorSel, PorterDuff.Mode.SRC));
                btnLight3.setTextColor(getResources().getColor(R.color.white, null));
                if( btnCurrent[0] != null && btnCurrent[0] != btnLight3) {
                    btnCurrent[0].getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolor, PorterDuff.Mode.SRC));
                    btnCurrent[0].setTextColor(getResources().getColor(R.color.black, null));
                }
                btnCurrent[0] = btnLight3;
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.list_frame, TimersListFragment.newInstance("light3"));
                ft.commit();
            }
        });
        btnLight4 = (Button)view.findViewById(R.id.dev_btnLight4);
        btnLight4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLight4.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolorSel, PorterDuff.Mode.SRC));
                btnLight4.setTextColor(getResources().getColor(R.color.white, null));
                if( btnCurrent[0] != null && btnCurrent[0] != btnLight4) {
                    btnCurrent[0].getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolor, PorterDuff.Mode.SRC));
                    btnCurrent[0].setTextColor(getResources().getColor(R.color.black, null));
                }
                btnCurrent[0] = btnLight4;
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.list_frame, TimersListFragment.newInstance("light4"));
                ft.commit();
            }
        });
        btnLight5 = (Button)view.findViewById(R.id.dev_btnLight5);
        btnLight5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLight5.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolorSel, PorterDuff.Mode.SRC));
                btnLight5.setTextColor(getResources().getColor(R.color.white, null));
                if( btnCurrent[0] != null && btnCurrent[0] != btnLight5) {
                    btnCurrent[0].getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolor, PorterDuff.Mode.SRC));
                    btnCurrent[0].setTextColor(getResources().getColor(R.color.black, null));
                }
                btnCurrent[0] = btnLight5;
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.list_frame, TimersListFragment.newInstance("light5"));
                ft.commit();
            }
        });
        btnLight6 = (Button)view.findViewById(R.id.dev_btnLight6);
        btnLight6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLight6.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolorSel, PorterDuff.Mode.SRC));
                btnLight6.setTextColor(getResources().getColor(R.color.white, null));
                if( btnCurrent[0] != null && btnCurrent[0] != btnLight6) {
                    btnCurrent[0].getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolor, PorterDuff.Mode.SRC));
                    btnCurrent[0].setTextColor(getResources().getColor(R.color.black, null));
                }
                btnCurrent[0] = btnLight6;
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.list_frame, TimersListFragment.newInstance("light6"));
                ft.commit();
            }
        });
        btnPump = (Button)view.findViewById(R.id.dev_btnPump);
        btnPump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPump.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolorSel, PorterDuff.Mode.SRC));
                btnPump.setTextColor(getResources().getColor(R.color.white, null));
                if( btnCurrent[0] != null && btnCurrent[0] != btnPump) {
                    btnCurrent[0].getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolor, PorterDuff.Mode.SRC));
                    btnCurrent[0].setTextColor(getResources().getColor(R.color.black, null));
                }
                btnCurrent[0] = btnPump;
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.list_frame, TimersListFragment.newInstance("pump"));
                ft.commit();
            }
        });
        btnSprayer = (Button)view.findViewById(R.id.dev_btnSprayer);
        btnSprayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSprayer.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolorSel, PorterDuff.Mode.SRC));
                btnSprayer.setTextColor(getResources().getColor(R.color.white, null));
                if( btnCurrent[0] != null && btnCurrent[0] != btnSprayer) {
                    btnCurrent[0].getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolor, PorterDuff.Mode.SRC));
                    btnCurrent[0].setTextColor(getResources().getColor(R.color.black, null));
                }
                btnCurrent[0] = btnSprayer;
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.list_frame, TimersListFragment.newInstance("sprayer"));
                ft.commit();
            }
        });
        btnMist = (Button)view.findViewById(R.id.dev_btnMist);
        btnMist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMist.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolorSel, PorterDuff.Mode.SRC));
                btnMist.setTextColor(getResources().getColor(R.color.white, null));
                if( btnCurrent[0] != null && btnCurrent[0] != btnMist) {
                    btnCurrent[0].getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolor, PorterDuff.Mode.SRC));
                    btnCurrent[0].setTextColor(getResources().getColor(R.color.black, null));
                }
                btnCurrent[0] = btnMist;
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.list_frame, TimersListFragment.newInstance("mist"));
                ft.commit();
            }
        });
        btnFanIn = (Button)view.findViewById(R.id.dev_btnFanIn);
        btnFanIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFanIn.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolorSel, PorterDuff.Mode.SRC));
                btnFanIn.setTextColor(getResources().getColor(R.color.white, null));
                if( btnCurrent[0] != null && btnCurrent[0] != btnFanIn) {
                    btnCurrent[0].getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolor, PorterDuff.Mode.SRC));
                    btnCurrent[0].setTextColor(getResources().getColor(R.color.black, null));
                }
                btnCurrent[0] = btnFanIn;
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.list_frame, TimersListFragment.newInstance("fan_in"));
                ft.commit();
            }
        });
        btnFanOut = (Button)view.findViewById(R.id.dev_btnFanOut);
        btnFanOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFanOut.getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolorSel, PorterDuff.Mode.SRC));
                btnFanOut.setTextColor(getResources().getColor(R.color.white, null));
                if( btnCurrent[0] != null && btnCurrent[0] != btnFanOut) {
                    btnCurrent[0].getBackground().mutate().setColorFilter(new PorterDuffColorFilter(backcolor, PorterDuff.Mode.SRC));
                    btnCurrent[0].setTextColor(getResources().getColor(R.color.black, null));
                }
                btnCurrent[0] = btnFanOut;
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.list_frame, TimersListFragment.newInstance("fan_out"));
                ft.commit();
            }
        });
    }
}