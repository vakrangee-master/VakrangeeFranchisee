package in.vakrangee.franchisee.bcadetails.pre;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PreBCADetailsDto implements Serializable {

    @SerializedName("isSameAsFranchiseeVisible")
    private String isSameAsFranchiseeVisible;

    @SerializedName("sameAsFranchisee")
    private String sameAsFranchisee;

    @SerializedName("fa_data")
    private String faData;

    @SerializedName("personal_details")
    private String personalDetails;

    @SerializedName("location_details")
    private String locationDetails;

    @SerializedName("fa_status_detail")
    private String statusDetail;

    @SerializedName("fr_mobile_number")
    private String frMobileNumber;

    @SerializedName("v_state_code")
    private String vStateCode;

    @SerializedName("v_district_code")
    private String vDistrictCode;

    public String getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(String personalDetails) {
        this.personalDetails = personalDetails;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public String getIsSameAsFranchiseeVisible() {
        return isSameAsFranchiseeVisible;
    }

    public void setIsSameAsFranchiseeVisible(String isSameAsFranchiseeVisible) {
        this.isSameAsFranchiseeVisible = isSameAsFranchiseeVisible;
    }

    public String getSameAsFranchisee() {
        return sameAsFranchisee;
    }

    public void setSameAsFranchisee(String sameAsFranchisee) {
        this.sameAsFranchisee = sameAsFranchisee;
    }

    public String getFaData() {
        return faData;
    }

    public void setFaData(String faData) {
        this.faData = faData;
    }

    public String getFrMobileNumber() {
        return frMobileNumber;
    }

    public void setFrMobileNumber(String frMobileNumber) {
        this.frMobileNumber = frMobileNumber;
    }

    public String getvStateCode() {
        return vStateCode;
    }

    public void setvStateCode(String vStateCode) {
        this.vStateCode = vStateCode;
    }

    public String getvDistrictCode() {
        return vDistrictCode;
    }

    public void setvDistrictCode(String vDistrictCode) {
        this.vDistrictCode = vDistrictCode;
    }
}
