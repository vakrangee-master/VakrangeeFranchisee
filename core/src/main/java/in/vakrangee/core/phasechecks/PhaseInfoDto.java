package in.vakrangee.core.phasechecks;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PhaseInfoDto implements Serializable {

    @SerializedName("nextgen_phase_id")
    private String nextGenPhaseId;

    @SerializedName("nextgen_franchisee_application_id")
    private String nextGenAppId;

    @SerializedName("nextgen_phase_code")
    private String nextGenPhaseCode;

    @SerializedName("nextgen_phase_name")
    private String nextGenPhaseName;

    public String getNextGenPhaseId() {
        return nextGenPhaseId;
    }

    public void setNextGenPhaseId(String nextGenPhaseId) {
        this.nextGenPhaseId = nextGenPhaseId;
    }

    public String getNextGenAppId() {
        return nextGenAppId;
    }

    public void setNextGenAppId(String nextGenAppId) {
        this.nextGenAppId = nextGenAppId;
    }

    public String getNextGenPhaseCode() {
        return nextGenPhaseCode;
    }

    public void setNextGenPhaseCode(String nextGenPhaseCode) {
        this.nextGenPhaseCode = nextGenPhaseCode;
    }

    public String getNextGenPhaseName() {
        return nextGenPhaseName;
    }

    public void setNextGenPhaseName(String nextGenPhaseName) {
        this.nextGenPhaseName = nextGenPhaseName;
    }
}
