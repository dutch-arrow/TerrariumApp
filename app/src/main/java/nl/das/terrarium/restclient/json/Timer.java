package nl.das.terrarium.restclient.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Timer {

    @SerializedName("device")
    @Expose
    private String device;
    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("on_off")
    @Expose
    private String onOff;
    @SerializedName("hour")
    @Expose
    private Integer hour;
    @SerializedName("minute")
    @Expose
    private Integer minute;
    @SerializedName("repeat")
    @Expose
    private Integer repeat;
    @SerializedName("period")
    @Expose
    private Integer period;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getOnOff() {
        return onOff;
    }

    public void setOnOff(String onOff) {
        this.onOff = onOff;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getRepeat() {
        return repeat;
    }

    public void setRepeat(Integer repeat) {
        this.repeat = repeat;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Timer() { }

    public Timer(String device, int index, String onoff, int hour, int minute, int repeat, int period) {
        this.device = device;
        this.index = index;
        this.onOff = onoff;
        this.hour = hour;
        this.minute = minute;
        this.repeat = repeat;
        this.period = period;
    }
}