package in.vakrangee.core.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FranchiseeTimeLineDetails implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName(value="nextgen_site_visit_id", alternate = {"nextgen_site_work_commencement_id"})
    private String nextgen_site_visit_id;
    @SerializedName(value = "nextgen_site_visit_status", alternate = {"nextgen_site_work_status"})
    private String nextgen_site_visit_status;
    @SerializedName(value = "nextgen_site_visit_description", alternate = {"nextgen_site_work_description"})
    private String nextgen_site_visit_description;
    @SerializedName(value = "nextgen_site_visit_remarks", alternate = {"nextgen_site_work_remarks"})
    private String nextgen_site_visit_remarks;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("user_name")
    private String user_name;
    @SerializedName("date_time")
    private String date_time;
    @SerializedName("color")
    private int color = 0;
    @SerializedName("icon")
    private int icon = 0;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNextgenSiteVisitId() {
        return this.nextgen_site_visit_id;
    }

    public void setNextgenSiteVisitId(String nextgen_site_visit_id) {
        this.nextgen_site_visit_id = nextgen_site_visit_id;
    }


    public String getNextgenSiteVisitStatus() {
        return this.nextgen_site_visit_status;
    }

    public void setNextgenSiteVisitStatus(String nextgen_site_visit_status) {
        this.nextgen_site_visit_status = nextgen_site_visit_status;
    }


    public String getNextgenSiteVisitDescription() {
        return this.nextgen_site_visit_description;
    }

    public void setNextgenSiteVisitDescription(String nextgen_site_visit_description) {
        this.nextgen_site_visit_description = nextgen_site_visit_description;
    }

    public String getNextgenSiteVisitRemarks() {
        return this.nextgen_site_visit_remarks;
    }

    public void setNextgenSiteVisitRemarks(String nextgen_site_visit_remarks) {
        this.nextgen_site_visit_remarks = nextgen_site_visit_remarks;
    }

    public String getUserId() {
        return this.user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getUserName() {
        return this.user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public String getDateTime() {
        return this.date_time;
    }

    public void setDateTime(String date_time) {
        this.date_time = date_time;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

}