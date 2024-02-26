package in.vakrangee.franchisee.networktesting.speedtest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SIMDetailsDto implements Serializable {

    @SerializedName("sim_operator_name")
    private String simOperatorName;

    @SerializedName("sim_mobile_data_enabled")
    private boolean simMobileDataEnabled;

    public String getSimOperatorName() {
        return simOperatorName;
    }

    public void setSimOperatorName(String simOperatorName) {
        this.simOperatorName = simOperatorName;
    }

    public boolean isSimMobileDataEnabled() {
        return simMobileDataEnabled;
    }

    public void setSimMobileDataEnabled(boolean simMobileDataEnabled) {
        this.simMobileDataEnabled = simMobileDataEnabled;
    }
}
