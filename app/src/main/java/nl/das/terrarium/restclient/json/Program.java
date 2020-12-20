package nl.das.terrarium.restclient.json;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Program {

    @SerializedName("active")
    @Expose
    private String active;
    @SerializedName("time_of_day")
    @Expose
    private String timeOfDay;
    @SerializedName("temp_ideal")
    @Expose
    private Integer tempIdeal;
    @SerializedName("hum_ideal")
    @Expose
    private Integer humIdeal;
    @SerializedName("program_data")
    @Expose
    private List<ProgramData> programData = null;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public Integer getTempIdeal() {
        return tempIdeal;
    }

    public void setTempIdeal(Integer tempIdeal) {
        this.tempIdeal = tempIdeal;
    }

    public Integer getHumIdeal() {
        return humIdeal;
    }

    public void setHumIdeal(Integer humIdeal) {
        this.humIdeal = humIdeal;
    }

    public List<ProgramData> getProgramData() {
        return programData;
    }

    public void setProgramData(List<ProgramData> programData) {
        this.programData = programData;
    }

    public Program(String active, String tod, int hum, int temp, List<ProgramData> pds) {
        this.active = active;
        this.timeOfDay = tod;
        this.tempIdeal = temp;
        this.humIdeal = hum;
        this.programData = pds;
    }

}