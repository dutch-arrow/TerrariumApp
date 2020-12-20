package nl.das.terrarium.restclient.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Action {

    @SerializedName("device")
    @Expose
    private String device;
    @SerializedName("on_period")
    @Expose
    private Integer onPeriod;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Integer getOnPeriod() {
        return onPeriod;
    }

    public void setOnPeriod(Integer onPeriod) {
        this.onPeriod = onPeriod;
    }

    public Action(String device, int onPeriod) {
        this.device = device;
        this.onPeriod = onPeriod;
    }

}
