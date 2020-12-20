package nl.das.terrarium.restclient.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Device {

    @SerializedName("device")
    @Expose
    private String device;
    @SerializedName("nr_of_timers")
    @Expose
    private Integer nrOfTimers;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Integer getNrOfTimers() {
        return nrOfTimers;
    }

    public void setNrOfTimers(Integer nrOfTimers) {
        this.nrOfTimers = nrOfTimers;
    }

}