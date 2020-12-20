package nl.das.terrarium.data;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.List;

import nl.das.terrarium.restclient.json.Action;
import nl.das.terrarium.restclient.json.ProgramData;

public class Rule extends BaseObservable {
    private String value = "";
    private ToDo[] actionArray = new ToDo[2];

    public Rule() {
        value = "";
        actionArray[0] = new ToDo();
        actionArray[1] = new ToDo();
    }

    public Rule(ProgramData programData) {
        value = programData.getValue().toString();
        actionArray[0] = new ToDo(programData.getActions().get(0));
        actionArray[1] = new ToDo(programData.getActions().get(1));
    }

    @Bindable
    public String getValue() { return value; }
    public void setValue(String value) { }
    public void saveValue(String value) { this.value = value; notifyPropertyChanged(BR.value);}

    public ToDo getAction(int nr) { return actionArray[nr]; }
    public void setAction(int nr, ToDo action) { this.actionArray[nr] = action; notifyChange();}

    public ProgramData toProgramData() {
        int intval = Integer.parseInt(value.length() > 0 ? value : "0");
        List<Action> actions = new ArrayList<>();
        int onPeriod = Integer.parseInt(actionArray[0].getOnPeriod().length() > 0 ? actionArray[0].getOnPeriod() : "0");
        if (actionArray[0].getDevice().equalsIgnoreCase("no device")) {
            onPeriod = 0;
        }
        actions.add(new Action(actionArray[0].getDevice(), onPeriod));
        onPeriod = Integer.parseInt(actionArray[1].getOnPeriod().length() > 0 ? actionArray[1].getOnPeriod() : "0");
        if (actionArray[1].getDevice().equalsIgnoreCase("no device")) {
            onPeriod = 0;
        }
        actions.add(new Action(actionArray[1].getDevice(), onPeriod));
        return new ProgramData(intval, actions);
    }
}
