package nl.das.terrarium.restclient.json;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProgramData {

    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("actions")
    @Expose
    private List<Action> actions = null;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public ProgramData(int value, List<Action> actions) {
        this.value = value;
        this.actions = actions;
    }

}
