package in.vakrangee.franchisee.gwr.dashboard;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GWRDashboardDto implements Serializable {

    @SerializedName("is_activity_over")
    private String isActivityOver;

    @SerializedName("is_gwr_vkid")
    private String isGWRVKId;

    @SerializedName("is_inauguration_done")
    private String isInAugurationDone;

    @SerializedName("guinness_franchisee_id")
    private String guinnessFranchiseeId;

    @SerializedName("nextgen_inauguration_date_time")
    private String nextGenInAugDateTime;

    @SerializedName("vkid")
    private String vkid;

    @SerializedName("mac_address")
    private String mac_address;

    @SerializedName("fr_name")
    private String fr_name;

    @SerializedName("time_up")
    private String time_up;

    @SerializedName("inauguration_image_id")
    private String inaugurationImageId;

    @SerializedName("inauguration_image")
    private String inaugurationImage;

    @SerializedName("device_date_time")
    private String deviceDateTime;

    @SerializedName("is_event_day_opened")
    private String isEventDayOpened;

    @SerializedName("event_day_msg")
    private String eventDayMsg;

    public String getIsActivityOver() {
        return isActivityOver;
    }

    public void setIsActivityOver(String isActivityOver) {
        this.isActivityOver = isActivityOver;
    }

    public String getIsGWRVKId() {
        return isGWRVKId;
    }

    public void setIsGWRVKId(String isGWRVKId) {
        this.isGWRVKId = isGWRVKId;
    }

    public String getIsInAugurationDone() {
        return isInAugurationDone;
    }

    public void setIsInAugurationDone(String isInAugurationDone) {
        this.isInAugurationDone = isInAugurationDone;
    }

    public String getGuinnessFranchiseeId() {
        return guinnessFranchiseeId;
    }

    public void setGuinnessFranchiseeId(String guinnessFranchiseeId) {
        this.guinnessFranchiseeId = guinnessFranchiseeId;
    }

    public String getNextGenInAugDateTime() {
        return nextGenInAugDateTime;
    }

    public void setNextGenInAugDateTime(String nextGenInAugDateTime) {
        this.nextGenInAugDateTime = nextGenInAugDateTime;
    }

    public String getVkid() {
        return vkid;
    }

    public void setVkid(String vkid) {
        this.vkid = vkid;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    public String getFr_name() {
        return fr_name;
    }

    public void setFr_name(String fr_name) {
        this.fr_name = fr_name;
    }

    public String getTime_up() {
        return time_up;
    }

    public void setTime_up(String time_up) {
        this.time_up = time_up;
    }

    public String getInaugurationImageId() {
        return inaugurationImageId;
    }

    public void setInaugurationImageId(String inaugurationImageId) {
        this.inaugurationImageId = inaugurationImageId;
    }

    public String getInaugurationImage() {
        return inaugurationImage;
    }

    public void setInaugurationImage(String inaugurationImage) {
        this.inaugurationImage = inaugurationImage;
    }

    public String getDeviceDateTime() {
        return deviceDateTime;
    }

    public void setDeviceDateTime(String deviceDateTime) {
        this.deviceDateTime = deviceDateTime;
    }

    public String getIsEventDayOpened() {
        return isEventDayOpened;
    }

    public void setIsEventDayOpened(String isEventDayOpened) {
        this.isEventDayOpened = isEventDayOpened;
    }

    public String getEventDayMsg() {
        return eventDayMsg;
    }

    public void setEventDayMsg(String eventDayMsg) {
        this.eventDayMsg = eventDayMsg;
    }
}
