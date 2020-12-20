package nl.das.terrarium.data;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.ArrayList;
import java.util.List;

import nl.das.terrarium.restclient.json.Timer;

public class TimersItem  extends BaseObservable {
    private String deviceId;
    private int index;
    private int timeOnHour;
    private int timeOnMinute;
    private int timeOffHour;
    private int timeOffMinute;
    private int repeat;
    private int period;
    private boolean filled;

    public TimersItem(Timer timerOn, Timer timerOff, boolean filled) {
        this.deviceId = timerOn.getDevice();
        this.index = timerOn.getIndex();
        this.timeOnHour = timerOn.getHour();
        this.timeOnMinute = timerOn.getMinute();
        this.timeOffHour = timerOff.getHour();
        this.timeOffMinute = timerOff.getMinute();
        this.repeat = timerOn.getRepeat() + 1;
        this.period = timerOn.getPeriod();
        this.filled = filled;
    }

    public boolean isFilled() { return filled; }
    public void setFilled(boolean filled) { this.filled = filled; }

    @Bindable
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) {  }
    public void saveDeviceId(String deviceId) { this.deviceId = deviceId; }

    @Bindable
    public int getIndex() { return index; }
    public void setIndex(int index) {  }
    public void saveIndex(int index) {
        this.index = index;
    }

    @SuppressLint("DefaultLocale")
    @Bindable
    public String getTimeOn() {
        if (timeOnHour == 24 && timeOnMinute == 60) {
            return "";
        } else {
            return String.format("%02d.%02d", timeOnHour, timeOnMinute);
        }
    }
    public void setTimeOn(String timeString) {  }
    public void saveTimeOn(String timeString) {
        Log.i("Terrarium", "saveTimeOn - " + timeString);
        if (timeString.length() == 0) {
            timeOnHour = 24;
            timeOnMinute = 60;
        } else {
            String[] parts = timeString.split("\\.");
            timeOnHour = Integer.parseInt(parts[0]);
            timeOnMinute = Integer.parseInt(parts[1]);
        }
    }

    @SuppressLint("DefaultLocale")
    @Bindable
    public String getTimeOff() {
        if (timeOffHour == 24 && timeOffMinute == 60) {
            return "";
        } else {
            return String.format("%02d.%02d", timeOffHour, timeOffMinute);
        }
    }
    public void setTimeOff(String timeString) {

    }
    public void saveTimeOff(String timeString) {
        if (timeString.length() == 0) {
            timeOffHour = 24;
            timeOffMinute = 60;
        } else {
            String[] parts = timeString.split("\\.");
            timeOffHour = Integer.parseInt(parts[0]);
            timeOffMinute = Integer.parseInt(parts[1]);
        }
    }

    @Bindable
    public String getRepeat() { return repeat + ""; }
    public void setRepeat(String repeat) { }
    public void saveRepeat(String repeat) { this.repeat = Integer.parseInt(repeat); }

    @Bindable
    public String getPeriod() { return period + ""; }
    public void setPeriod(String period) {

    }
    public void savePeriod(String period) { this.period = Integer.parseInt(period); }

    public List<Timer> toTimers() {
        List<Timer> tmrs = new ArrayList<>();
        tmrs.add(new Timer(this.deviceId, this.index, "on", this.timeOnHour, this.timeOnMinute, this.repeat - 1, this.period));
        tmrs.add(new Timer(this.deviceId, this.index, "off", this.timeOffHour, this.timeOffMinute, 0, 0));
        return tmrs;
    }
}
