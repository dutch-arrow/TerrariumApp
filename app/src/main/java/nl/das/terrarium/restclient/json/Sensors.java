package nl.das.terrarium.restclient.json;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sensors {

    @SerializedName("clock")
    @Expose
    private String clock;
    @SerializedName("rules")
    @Expose
    private String rules;
    @SerializedName("sensors")
    @Expose
    private List<Sensor> sensors = null;

    public String getClock() {
        return clock;
    }

    public void setClock(String clock) {
        this.clock = clock;
    }

    public String getRules() { return rules; }

    public void setRules(String rules) { this.rules = rules; }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

}