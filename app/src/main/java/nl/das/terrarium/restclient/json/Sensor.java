package nl.das.terrarium.restclient.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sensor {

    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("humidity")
    @Expose
    private Integer humidity;
    @SerializedName("temperature")
    @Expose
    private Integer temperature;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

}