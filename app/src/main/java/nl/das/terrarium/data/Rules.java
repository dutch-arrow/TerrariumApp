package nl.das.terrarium.data;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import nl.das.terrarium.restclient.json.Program;
import nl.das.terrarium.restclient.json.ProgramData;

public class Rules extends BaseObservable {
    private boolean active;
    private String timeOfDay;
    private String tempIdeal;
    private String humIdeal;
    private Rule[] ruleArray = new Rule[4];

    public Rules() {
        active = false;
        timeOfDay = "";
        tempIdeal ="0";
        humIdeal = "0";
        ruleArray[0] = new Rule();
        ruleArray[1] = new Rule();
        ruleArray[2] = new Rule();
        ruleArray[3] = new Rule();
    }

    public Rules(Program program) {
        active = program.getActive().equalsIgnoreCase("yes");
        timeOfDay = program.getTimeOfDay();
        tempIdeal = program.getTempIdeal().toString();
        humIdeal = program.getHumIdeal().toString();
        ruleArray[0] = new Rule(program.getProgramData().get(0));
        ruleArray[1] = new Rule(program.getProgramData().get(1));
        ruleArray[2] = new Rule(program.getProgramData().get(2));
        ruleArray[3] = new Rule(program.getProgramData().get(3));
    }

    @Bindable
    public boolean getActive() { return active; }
    public void setActive(boolean active) { }
    public void saveActive(boolean active) { this.active = active; notifyPropertyChanged(BR.active);}

    @Bindable
    public String getTimeOfDay() { return timeOfDay; }
    public void setTimeOfDay(String timeOfDay) { }
    public void saveTimeOfDay(String timeOfDay) { this.timeOfDay = timeOfDay; notifyPropertyChanged(BR.timeOfDay);}

    @Bindable
    public String getTempIdeal() { return tempIdeal; }
    public void setTempIdeal(String tempIdeal) { }
    public void saveTempIdeal(String tempIdeal) { this.tempIdeal = tempIdeal; notifyPropertyChanged(BR.tempIdeal); }

    @Bindable
    public String getHumIdeal() { return humIdeal; }
    public void setHumIdeal(String humIdeal) { }
    public void saveHumIdeal(String humIdeal) { this.humIdeal = humIdeal; notifyPropertyChanged(BR.humIdeal); }

    public Rule getRule(int nr) {
        return ruleArray[nr];
    }
    public void setRule(int nr, Rule rule) { ruleArray[nr] = rule; notifyChange(); }

    public Program toProgram() {
        List<ProgramData> pds = new ArrayList<>();
        pds.add(ruleArray[0].toProgramData());
        pds.add(ruleArray[1].toProgramData());
        pds.add(ruleArray[2].toProgramData());
        pds.add(ruleArray[3].toProgramData());
        return new Program(active ? "on" : "off", timeOfDay, Integer.parseInt(humIdeal), Integer.parseInt(tempIdeal), pds);
    }
}
