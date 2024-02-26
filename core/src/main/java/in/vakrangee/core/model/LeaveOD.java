package in.vakrangee.core.model;

/**
 * Created by Nileshd on 1/6/2017.
 */

public class LeaveOD {
    String leaveOdId;
    String leaveTypeId;
    String startDate;
    String endDate;
    String reason;
    String leaveOdStatus;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String time;
    public LeaveOD() {

    }

    public LeaveOD(String leaveOdId, String leaveTypeId, String startDate, String endDate, String reason, String leaveOdStatus) {
        this.leaveOdId = leaveOdId;
        this.leaveTypeId = leaveTypeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.leaveOdStatus = leaveOdStatus;
    }

    public String getLeaveOdId() {
        return leaveOdId;
    }

    public void setLeaveOdId(String leaveOdId) {
        this.leaveOdId = leaveOdId;
    }

    public String getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(String leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLeaveOdStatus() {
        return leaveOdStatus;
    }

    public void setLeaveOdStatus(String leaveOdStatus) {
        this.leaveOdStatus = leaveOdStatus;
    }


    public String ODSerach(String name)
    {
       return name;
    }
}
