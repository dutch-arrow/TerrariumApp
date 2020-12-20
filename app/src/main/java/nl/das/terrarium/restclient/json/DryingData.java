package nl.das.terrarium.restclient.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DryingData {

    @SerializedName("start_fan_period")
    @Expose
    private Integer startFanPeriod;
    @SerializedName("fan_period")
    @Expose
    private Integer fanPeriod;

    public Integer getStartFanPeriod() {
        return startFanPeriod;
    }

    public void setStartFanPeriod(Integer startFanPeriod) {
        this.startFanPeriod = startFanPeriod;
    }

    public Integer getFanPeriod() {
        return fanPeriod;
    }

    public void setFanPeriod(Integer fanPeriod) {
        this.fanPeriod = fanPeriod;
    }

}