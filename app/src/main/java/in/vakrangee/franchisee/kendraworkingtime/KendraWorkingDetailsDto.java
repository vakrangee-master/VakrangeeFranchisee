package in.vakrangee.franchisee.kendraworkingtime;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KendraWorkingDetailsDto implements Serializable {

    @SerializedName("day")
    private String day;

    @SerializedName("interval")
    private String interval;

    @SerializedName("working_time")
    public List<WorkingTimeDto> workingTimeList;

    public KendraWorkingDetailsDto(){
        workingTimeList = new ArrayList<WorkingTimeDto>();
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}
