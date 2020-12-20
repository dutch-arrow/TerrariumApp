package nl.das.terrarium.restclient.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Programs {

    @SerializedName("program")
    @Expose
    private Program program;

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Programs(Program prog) {
        this.program = prog;
    }

}