package in.vakrangee.franchisee.nextgenfranchiseeapplication.stepstepper;

public class HeaderStepperDTO {
    private String StepperName;
    private String StpeerId;
    private boolean IsPartiallyfilled;
    private boolean IsAllEnterValidated;


    public HeaderStepperDTO(String stepperName, String stpeerId, boolean IsPartiallyfilled,
                            boolean IsALlEnterValidated) {
        this.StepperName = stepperName;
        this.StpeerId = stpeerId;
        this.IsPartiallyfilled = IsPartiallyfilled;
        this.IsAllEnterValidated = IsALlEnterValidated;

    }

    public String getStepperName() {
        return StepperName;
    }

    public void setStepperName(String stepperName) {
        StepperName = stepperName;
    }

    public String getStpeerId() {
        return StpeerId;
    }

    public void setStpeerId(String stpeerId) {
        StpeerId = stpeerId;
    }

    public boolean isPartiallyfilled() {
        return IsPartiallyfilled;
    }

    public void setPartiallyfilled(boolean partiallyfilled) {
        IsPartiallyfilled = partiallyfilled;
    }

    public boolean isAllEnterValidated() {
        return IsAllEnterValidated;
    }

    public void setAllEnterValidated(boolean allEnterValidated) {
        IsAllEnterValidated = allEnterValidated;
    }
}
