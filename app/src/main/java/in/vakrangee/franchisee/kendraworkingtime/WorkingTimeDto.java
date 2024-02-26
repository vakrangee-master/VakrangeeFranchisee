package in.vakrangee.franchisee.kendraworkingtime;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WorkingTimeDto implements Serializable {

    @SerializedName("opening_time")
    private String openingTime;

    @SerializedName("closing_time")
    private String closingTime;

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }
}
