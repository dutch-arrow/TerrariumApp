package nl.das.terrarium.restclient.json;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties {

    @SerializedName("nr_of_timers")
    @Expose
    private Integer nrOfTimers;
    @SerializedName("nr_of_programs")
    @Expose
    private Integer nrOfPrograms;
    @SerializedName("devices")
    @Expose
    private List<Device> devices = null;

    public Integer getNrOfTimers() {
        return nrOfTimers;
    }

    public void setNrOfTimers(Integer nrOfTimers) {
        this.nrOfTimers = nrOfTimers;
    }

    public Integer getNrOfPrograms() {
        return nrOfPrograms;
    }

    public void setNrOfPrograms(Integer nrOfPrograms) {
        this.nrOfPrograms = nrOfPrograms;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}