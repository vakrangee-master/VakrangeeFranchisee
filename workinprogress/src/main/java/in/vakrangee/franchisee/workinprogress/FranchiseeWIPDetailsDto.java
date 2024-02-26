package in.vakrangee.franchisee.workinprogress;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FranchiseeWIPDetailsDto implements Serializable {

    @SerializedName("CommencementMinDate")
    private String CommencementMinDate;

    @SerializedName("VTC")
    private String VTC;

    @SerializedName("Latitude")
    private String Latitude;

    @SerializedName("SiteCommencementStartDate")
    private String SiteCommencementStartDate;

    @SerializedName("VAddress")
    private String VAddress;

    @SerializedName("vakrangeeLocationId")
    private String vakrangeeLocationId;

    @SerializedName("wipDetails")
    private String wipDetails;

    @SerializedName("SiteCommencementEstimatedEndDate")
    private String SiteCommencementEstimatedEndDate;

    @SerializedName("brandingElements")
    private String brandingElements;

    @SerializedName("wipLocationRange")
    private String wipLocationRange;

    @SerializedName("ServerDateTime")
    private String ServerDateTime;

    @SerializedName("NextgenSiteWorkCommencementId")
    private String NextgenSiteWorkCommencementId;

    @SerializedName("wipLastVisitedDateTime")
    private String wipLastVisitedDateTime;

    @SerializedName("NextGenFranchiseeApplicationNo")
    private String NextGenFranchiseeApplicationNo;

    @SerializedName("wipMileStoneDetail")
    private String wipMileStoneDetail;

    @SerializedName("Longitude")
    private String Longitude;

    @SerializedName("FranchiseeName")
    private String FranchiseeName;

    @SerializedName("NextGenFranchiseeApplicationId")
    private String NextGenFranchiseeApplicationId;

    @SerializedName("MobileNumber")
    private String MobileNumber;

    @SerializedName("State")
    private String State;

    @SerializedName("VKID")
    private String VKID;

    @SerializedName("LocationStatus")
    private String LocationStatus;

    @SerializedName("District")
    private String District;

    @SerializedName("FranchiseePicFile")
    private String FranchiseePicFile;

    @SerializedName("Pincode")
    private String Pincode;

    public String getCommencementMinDate() {
        return CommencementMinDate;
    }

    public void setCommencementMinDate(String commencementMinDate) {
        CommencementMinDate = commencementMinDate;
    }

    public String getVTC() {
        return VTC;
    }

    public void setVTC(String VTC) {
        this.VTC = VTC;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getSiteCommencementStartDate() {
        return SiteCommencementStartDate;
    }

    public void setSiteCommencementStartDate(String siteCommencementStartDate) {
        SiteCommencementStartDate = siteCommencementStartDate;
    }

    public String getVAddress() {
        return VAddress;
    }

    public void setVAddress(String VAddress) {
        this.VAddress = VAddress;
    }

    public String getVakrangeeLocationId() {
        return vakrangeeLocationId;
    }

    public void setVakrangeeLocationId(String vakrangeeLocationId) {
        this.vakrangeeLocationId = vakrangeeLocationId;
    }

    public String getWipDetails() {
        return wipDetails;
    }

    public void setWipDetails(String wipDetails) {
        this.wipDetails = wipDetails;
    }

    public String getSiteCommencementEstimatedEndDate() {
        return SiteCommencementEstimatedEndDate;
    }

    public void setSiteCommencementEstimatedEndDate(String siteCommencementEstimatedEndDate) {
        SiteCommencementEstimatedEndDate = siteCommencementEstimatedEndDate;
    }

    public String getBrandingElements() {
        return brandingElements;
    }

    public void setBrandingElements(String brandingElements) {
        this.brandingElements = brandingElements;
    }

    public String getWipLocationRange() {
        return wipLocationRange;
    }

    public void setWipLocationRange(String wipLocationRange) {
        this.wipLocationRange = wipLocationRange;
    }

    public String getServerDateTime() {
        return ServerDateTime;
    }

    public void setServerDateTime(String serverDateTime) {
        ServerDateTime = serverDateTime;
    }

    public String getNextgenSiteWorkCommencementId() {
        return NextgenSiteWorkCommencementId;
    }

    public void setNextgenSiteWorkCommencementId(String nextgenSiteWorkCommencementId) {
        NextgenSiteWorkCommencementId = nextgenSiteWorkCommencementId;
    }

    public String getWipLastVisitedDateTime() {
        return wipLastVisitedDateTime;
    }

    public void setWipLastVisitedDateTime(String wipLastVisitedDateTime) {
        this.wipLastVisitedDateTime = wipLastVisitedDateTime;
    }

    public String getNextGenFranchiseeApplicationNo() {
        return NextGenFranchiseeApplicationNo;
    }

    public void setNextGenFranchiseeApplicationNo(String nextGenFranchiseeApplicationNo) {
        NextGenFranchiseeApplicationNo = nextGenFranchiseeApplicationNo;
    }

    public String getWipMileStoneDetail() {
        return wipMileStoneDetail;
    }

    public void setWipMileStoneDetail(String wipMileStoneDetail) {
        this.wipMileStoneDetail = wipMileStoneDetail;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getFranchiseeName() {
        return FranchiseeName;
    }

    public void setFranchiseeName(String franchiseeName) {
        FranchiseeName = franchiseeName;
    }

    public String getNextGenFranchiseeApplicationId() {
        return NextGenFranchiseeApplicationId;
    }

    public void setNextGenFranchiseeApplicationId(String nextGenFranchiseeApplicationId) {
        NextGenFranchiseeApplicationId = nextGenFranchiseeApplicationId;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getVKID() {
        return VKID;
    }

    public void setVKID(String VKID) {
        this.VKID = VKID;
    }

    public String getLocationStatus() {
        return LocationStatus;
    }

    public void setLocationStatus(String locationStatus) {
        LocationStatus = locationStatus;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getFranchiseePicFile() {
        return FranchiseePicFile;
    }

    public void setFranchiseePicFile(String franchiseePicFile) {
        FranchiseePicFile = franchiseePicFile;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }
}
