package nl.das.terrarium.data;

import android.util.Log;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.databinding.library.baseAdapters.BR;

import nl.das.terrarium.restclient.json.Action;

public class ToDo extends BaseObservable {
    private String device;
    private String onPeriod;
    private String[] devices = {"no device","light1","light2","light3","light4","light5","light6","pump","sprayer","mist","fan_in","fan_out"};
    public int pos = 0;
    public boolean onPeriodCheck;
    public boolean idealCheck;

    public ToDo() {
        device = "no device";
        onPeriod = "0";
    }

    public ToDo(Action action) {
        this.device = action.getDevice();
        String p = action.getOnPeriod().toString();
        if (p.equalsIgnoreCase("0")) {
            this.onPeriod = "";
        } else {
            this.onPeriod = p;
        }
    }

    @Bindable
    public String getDevice() { return device; }
    public void setDevice(String device) {
        this.device = device;
        this.pos = getPosFromDevice();
        notifyPropertyChanged(BR.device);
        notifyPropertyChanged(BR.pos);
    }

    @Bindable
    public int getPos() { return pos;}
    public void setPos(int pos) { this.pos = pos; notifyPropertyChanged(BR.pos);}

    @BindingAdapter(value = "selection")
    public static void setSelection(Spinner spinner, int pos_old, int pos_new) {
        if (pos_old != pos_new) {
            spinner.setSelection(pos_new,true);
        }
    }

    @Bindable
    public String getOnPeriod() { return this.onPeriod; }
    public void setOnPeriod(String onPeriod) { }
    public void saveOnPeriod(String onPeriod) {
        if (onPeriod.length() > 0 && !onPeriod.equalsIgnoreCase("0")) {
            if (onPeriod.startsWith("-")) {
                setOnPeriodCheck(false);
                setIdealCheck(true);
            } else {
                setOnPeriodCheck(true);
                setIdealCheck(false);
            }
        } else {
            setOnPeriodCheck(false);
            setIdealCheck(false);
        }
        this.onPeriod = onPeriod;
    }

    @Bindable
    public boolean getOnPeriodCheck() { return this.onPeriodCheck; }
    public void setOnPeriodCheck(boolean check) {
        this.onPeriodCheck = check;
        notifyPropertyChanged(BR.onPeriodCheck);
    }

    @Bindable
    public boolean getIdealCheck() {
        return this.idealCheck;
    }
    public void setIdealCheck(boolean check) {
        this.idealCheck = check;
        notifyPropertyChanged(BR.idealCheck);
        if (check) {
            this.onPeriod = "";
            notifyPropertyChanged(BR.onPeriod);
        }
    }

    @BindingAdapter(value = "check")
    public static void setCheck(RadioButton btn, boolean check_old, boolean check) {
        btn.setChecked(check);
    }

    public Action toAction() {
        return new Action(device, Integer.parseInt(onPeriod));
    }

    private int getPosFromDevice() {
        for (int i = 0; i < devices.length; i++) {
            if (devices[i].equalsIgnoreCase((device))) {
                return i;
            }
        }
        return 0;
    }

}
