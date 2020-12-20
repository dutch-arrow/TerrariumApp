package nl.das.terrarium.restclient.json;

import androidx.lifecycle.ViewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class State  extends ViewModel {

    @SerializedName("device")
    @Expose
    private String device;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("end_time")
    @Expose
    private String endTime;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
       return "Device: " + device + ", state:" + state + ", endtime: " + endTime;
    }
}