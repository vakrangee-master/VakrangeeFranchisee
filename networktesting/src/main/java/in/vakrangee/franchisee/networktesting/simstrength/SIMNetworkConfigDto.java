package in.vakrangee.franchisee.networktesting.simstrength;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SIMNetworkConfigDto implements Serializable {

    @SerializedName("total_time_in_milliseconds")
    private int TOTAL_TIME_IN_MS;

    @SerializedName("interval_in_milliseconds")
    private int INTERVAL_IN_MS;

    public int getTOTAL_TIME_IN_MS() {
        return TOTAL_TIME_IN_MS;
    }

    public void setTOTAL_TIME_IN_MS(int TOTAL_TIME_IN_MS) {
        this.TOTAL_TIME_IN_MS = TOTAL_TIME_IN_MS;
    }

    public int getINTERVAL_IN_MS() {
        return INTERVAL_IN_MS;
    }

    public void setINTERVAL_IN_MS(int INTERVAL_IN_MS) {
        this.INTERVAL_IN_MS = INTERVAL_IN_MS;
    }

}
