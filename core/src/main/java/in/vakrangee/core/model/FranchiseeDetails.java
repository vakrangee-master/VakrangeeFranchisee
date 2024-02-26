package in.vakrangee.core.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import in.vakrangee.core.model.FranchiseeRemarkDetails;
import in.vakrangee.core.model.FranchiseeTimeLineDetails;

public class FranchiseeDetails implements Serializable {
    private String NextGenFranchiseeApplicationId;
    private String NextGenFranchiseeApplicationNo;
    private String NextgenSiteVisitId;
    private String VKID;
    private String FranchiseeName;
    private String VAddress;
    private String VTC;
    private String District;
    private String State;
    private String Pincode;
    private String MobileNumber;
    private String FranchiseePicFile;
    private String LocationStatus;
    private String vakrangeeLocationId;
    private String Latitude;
    private String Longitude;
    private String FrontageImageId;
    private String LeftWallImageId;
    private String FrontWallImageId;
    private String RightWallImageId;
    private String BackWallImageId;
    private String CeilingImageId;
    private String FloorImageId;
    private String FrontageLeftImageId;
    private String FrontageRightImageId;
    private String FrontageOppositeImageId;
    private String WidthInch;
    private String DepthInch;
    private String CeilingHeightInch;
    private String FrontageInch;
    private String AreaSqFeet;
    private String PremiseLocated;
    private String PremiseFloor;
    private String PremiseStructure;
    private String PremiseRoof;
    private String FrontageObstructed;
    private String Footpath;
    private String Bathroom;
    private String Pantry;

    // Additional Property - Added As per New Changes (10th July 2018)
    private String Pillers;
    private String Windows;
    private String Beam;
    private String AdjacentShops;
    private String MultipleEntries;
    //private String SiteLayoutSketch;
    // --END

    private String SiteVisitedAddressId;
    private String SiteVisitAddress1;
    private String SiteVisitAddress2;
    private String SiteVisitLandmark;
    private String SiteVisitState;
    private String SiteVisitDistrict;
    private String SiteVisitVTC;
    private String SiteVisitWardNo;
    private String SiteVisitPincode;
    private String PremiseLevel;
    private String PremiseShape;
    private String ClosestBankBranch1NbinCode;
    private String ClosestBankBranch2NbinCode;
    private String ClosestBankBranch3NbinCode;
    private String ClosestAtm1NbinCode;
    private String ClosestAtm2NbinCode;
    private String ClosestAtm3NbinCode;

    @SerializedName("Remarks")
    private String Remarks;

    @SerializedName("FranchiseeRemarks")
    private String franchiseeComments;       // Only Set By User

    @SerializedName("Status")
    private int resubmitStatus = 0; // Only For Re-Submit [1 - Re-Submit]

    //region Additional Data
    private String consentStatus;
    @SerializedName(value = "welcomeMailStatus", alternate = {"welcommeMailStatus"})
    private String welcomeMailStatus;
    private String callReceivedStatus;
    private String logisticsPaymentStatus;
    private String logisticsPaymentDate;
    private String gstRegisteredStatus;
    private String gstNumber;
    private String gstImage;
    private String gstImageId;
    private String gstAddress;
    private String FrontagePhoto10ftImageId;
    private String MainSignboardInch;
    private String EntranceInch;
    private String siteLayoutSketch;
    //endregion

    // Additional Property
    private transient int status = -1;
    private transient String statusDesc;
    private transient boolean allowToEdit = true;
    private transient FranchiseeRemarkDetails franchiseeRemarkDetails;
    private transient boolean isNeedToBeReviewed = false;
    private transient boolean isProfileReviewed = false, isLocationReviewed = false, isPhotoReviewed = false, isSiteVisitReviewed = false;

    //region Addition properties for Commencement
    @SerializedName("NextgenSiteWorkCommencementId")
    private String nextgenSiteWorkCommencementId;
    @SerializedName("SiteCommencementStartDate")
    private String commencementStartDate;
    @SerializedName("SiteCommencementEstimatedEndDate")
    private String commencementEstimatedEndDate;
    @SerializedName("CommencementMinDate")
    private String commencementMinDate;
    @SerializedName("SiteInteriorBrandingStatus")
    private String interiorBrandingStatus;
    //endregion

    //region NextGen Site Work In Progress
    @SerializedName("NextgenSiteWorkInProgressId")
    private String NextgenSiteWorkInProgressId;
    private String wipVisitDateTime;
    private int wipWorkOnTrack;

    @SerializedName("wipRevisedExpectedDateOfCompletion")
    private String wipRevisedExpectedDateOfCompletion;
    private String wipImages;                                                          // JSON Array
    private String wipFieldForceRemarks;
    private String wipMileStoneId;  //[0 - Unscheduled]
    private String wipMileStoneDetail;

    private String wipLastVisitedDateTime;
    private String wipLastVisitedBy;

    @SerializedName(value = "locationRange", alternate = "wipLocationRange")
    private int wipLocationRange;

    //endregion

    //region Interior Work Status
    private String interiorWorkStatus;
    private String completionDate;
    private String expectedCompletionDate;
    //endregion

    //region Properties for
    private String expectedDateOfSiteIdentification;
    private String siteNotIdentifiedRemarks;
    private String consentExitReason;   // For e.g. 1|1|1|1
    private String consentExitRemarks;
    //endregion

    @SerializedName("wipDetails")
    private String wipDetails;
    //endregion

    //region Site Readiness and Verification
    @SerializedName("designElements")
    private String designElements;

    @SerializedName("brandingElements")
    private String branding_element_details;

    @SerializedName("isKendraPhotoAllowed")
    private String isKendraPhotoAllowed;

    @SerializedName("isBrandingAllowed")
    private String isBrandingAllowed;

    @SerializedName("locationAccuracy")
    private int locationAccuracy;

    @SerializedName("kendraRange")
    private int kendraRange;

    @SerializedName("isAddressCheck")
    private int IsAddressChecked;

    @SerializedName("provisionalLengthMainSignboard")
    private String provisionalLengthMainSignboard;
    @SerializedName("provisionalWidthMainSignboard")
    private String provisionalWidth;


    //region Site Readiness - Work Commencement
    private String readinessWorkCommencementStatus; // 0 - Not Started | 1 - Started
    private String readinessWorkCommencementDate; // if 0 then Expected Date of Work Start else if 1 then Work Started On
    private String readinessWorkCommencementMinDate;
    private String readinessWorkCount; // if > 0 then allow to eadit workcommencement status and date.
    //endregion
    //endregion

    //region Image Ids
    private String frImageId; // Frontage
    private String lwImageId; // Left Wall
    private String fwImageId; // Front Wall
    private String rwImageId; // Right Wall
    private String bwImageId; // Back Wall
    private String ceImageId; // Ceiling
    private String flImageId; // Floor
    private String frlImageId; // Frontage Left
    private String frrImageId; // Frontage Right
    private String froImageId; // Frontage Opposite
    private String fr10ImageId; // Frontage 10 feet
    //endregion

    //region NextGen Site Work Completion Intimation
    @SerializedName("workCompletionDate")
    private String workCompletionDate;
    //endregion

    //region Address Data
    private String addressProofType;        // Address Type Id
    private String addressProofNo;          // Address Proof Number
    private String addressProofImageId;     // Address Image Id
    private String addressProofImage;       // Address Proof Image
    //endregion

    //hardware delivery address
    @SerializedName("hardware_delivery_address")
    private String hardwareDeliveryAddress;


    public String getConsentStatus() {
        return consentStatus;
    }

    public void setConsentStatus(String consentStatus) {
        this.consentStatus = consentStatus;
    }

    public String getWelcomeMailStatus() {
        return welcomeMailStatus;
    }

    public void setWelcomeMailStatus(String welcomeMailStatus) {
        this.welcomeMailStatus = welcomeMailStatus;
    }

    public String getCallReceivedStatus() {
        return callReceivedStatus;
    }

    public void setCallReceivedStatus(String callReceivedStatus) {
        this.callReceivedStatus = callReceivedStatus;
    }

    public String getLogisticsPaymentStatus() {
        return logisticsPaymentStatus;
    }

    public void setLogisticsPaymentStatus(String logisticsPaymentStatus) {
        this.logisticsPaymentStatus = logisticsPaymentStatus;
    }

    public String getLogisticsPaymentDate() {
        return logisticsPaymentDate;
    }

    public void setLogisticsPaymentDate(String logisticsPaymentDate) {
        this.logisticsPaymentDate = logisticsPaymentDate;
    }

    public String getSiteLayoutSketch() {
        return siteLayoutSketch;
    }

    public void setSiteLayoutSketch(String siteLayoutSketch) {
        this.siteLayoutSketch = siteLayoutSketch;
    }

    public String getGstRegisteredStatus() {
        return gstRegisteredStatus;
    }

    public void setGstRegisteredStatus(String gstRegisteredStatus) {
        this.gstRegisteredStatus = gstRegisteredStatus;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getGstImage() {
        return gstImage;
    }

    public void setGstImage(String gstImage) {
        this.gstImage = gstImage;
    }

    public String getGstAddress() {
        return gstAddress;
    }

    public void setGstAddress(String gstAddress) {
        this.gstAddress = gstAddress;
    }

    public String getGstImageId() {
        return gstImageId;
    }

    public void setGstImageId(String gstImageId) {
        this.gstImageId = gstImageId;
    }

    public String getFrontagePhoto10ftImageId() {
        return FrontagePhoto10ftImageId;
    }

    public void setFrontagePhoto10ftImageId(String frontagePhoto10ftImageId) {
        FrontagePhoto10ftImageId = frontagePhoto10ftImageId;
    }

    public String getMainSignboardInch() {
        return MainSignboardInch;
    }

    public void setMainSignboardInch(String mainSignboardInch) {
        MainSignboardInch = mainSignboardInch;
    }

    public String getEntranceInch() {
        return EntranceInch;
    }

    public void setEntranceInch(String entranceInch) {
        EntranceInch = entranceInch;
    }

    public String getNextGenFranchiseeApplicationId() {
        return NextGenFranchiseeApplicationId;
    }

    public void setNextGenFranchiseeApplicationId(String nextGenFranchiseeApplicationId) {
        NextGenFranchiseeApplicationId = nextGenFranchiseeApplicationId;
    }

    public String getNextGenFranchiseeApplicationNo() {
        return NextGenFranchiseeApplicationNo;
    }

    public void setNextGenFranchiseeApplicationNo(String nextGenFranchiseeApplicationNo) {
        NextGenFranchiseeApplicationNo = nextGenFranchiseeApplicationNo;
    }

    public String getNextgenSiteVisitId() {
        return NextgenSiteVisitId;
    }

    public void setNextgenSiteVisitId(String nextgenSiteVisitId) {
        NextgenSiteVisitId = nextgenSiteVisitId;
    }

    public String getVKID() {
        return VKID;
    }

    public void setVKID(String VKID) {
        this.VKID = VKID;
    }

    public String getFranchiseeName() {
        return FranchiseeName;
    }

    public void setFranchiseeName(String franchiseeName) {
        FranchiseeName = franchiseeName;
    }

    public String getVAddress() {
        return VAddress;
    }

    public void setVAddress(String VAddress) {
        this.VAddress = VAddress;
    }

    public String getVTC() {
        return VTC;
    }

    public void setVTC(String VTC) {
        this.VTC = VTC;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getFranchiseePicFile() {
        return FranchiseePicFile;
    }

    public void setFranchiseePicFile(String franchiseePicFile) {
        FranchiseePicFile = franchiseePicFile;
    }

    public String getLocationStatus() {
        return LocationStatus;
    }

    public void setLocationStatus(String locationStatus) {
        LocationStatus = locationStatus;
    }

    public String getVakrangeeLocationId() {
        return vakrangeeLocationId;
    }

    public void setVakrangeeLocationId(String vakrangeeLocationId) {
        this.vakrangeeLocationId = vakrangeeLocationId;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getFrontageImageId() {
        return FrontageImageId;
    }

    public void setFrontageImageId(String frontageImageId) {
        FrontageImageId = frontageImageId;
    }

    public String getLeftWallImageId() {
        return LeftWallImageId;
    }

    public void setLeftWallImageId(String leftWallImageId) {
        LeftWallImageId = leftWallImageId;
    }

    public String getFrontWallImageId() {
        return FrontWallImageId;
    }

    public void setFrontWallImageId(String frontWallImageId) {
        FrontWallImageId = frontWallImageId;
    }

    public String getRightWallImageId() {
        return RightWallImageId;
    }

    public void setRightWallImageId(String rightWallImageId) {
        RightWallImageId = rightWallImageId;
    }

    public String getBackWallImageId() {
        return BackWallImageId;
    }

    public void setBackWallImageId(String backWallImageId) {
        BackWallImageId = backWallImageId;
    }

    public String getCeilingImageId() {
        return CeilingImageId;
    }

    public void setCeilingImageId(String ceilingImageId) {
        CeilingImageId = ceilingImageId;
    }

    public String getFloorImageId() {
        return FloorImageId;
    }

    public void setFloorImageId(String floorImageId) {
        FloorImageId = floorImageId;
    }

    public String getFrontageLeftImageId() {
        return FrontageLeftImageId;
    }

    public void setFrontageLeftImageId(String frontageLeftImageId) {
        FrontageLeftImageId = frontageLeftImageId;
    }

    public String getFrontageRightImageId() {
        return FrontageRightImageId;
    }

    public void setFrontageRightImageId(String frontageRightImageId) {
        FrontageRightImageId = frontageRightImageId;
    }

    public String getFrontageOppositeImageId() {
        return FrontageOppositeImageId;
    }

    public void setFrontageOppositeImageId(String frontageOppositeImageId) {
        FrontageOppositeImageId = frontageOppositeImageId;
    }

    public String getWidthInch() {
        return WidthInch;
    }

    public void setWidthInch(String widthInch) {
        WidthInch = widthInch;
    }

    public String getDepthInch() {
        return DepthInch;
    }

    public void setDepthInch(String depthInch) {
        DepthInch = depthInch;
    }

    public String getCeilingHeightInch() {
        return CeilingHeightInch;
    }

    public void setCeilingHeightInch(String ceilingHeightInch) {
        CeilingHeightInch = ceilingHeightInch;
    }

    public String getFrontageInch() {
        return FrontageInch;
    }

    public void setFrontageInch(String frontageInch) {
        FrontageInch = frontageInch;
    }

    public String getAreaSqFeet() {
        return AreaSqFeet;
    }

    public void setAreaSqFeet(String areaSqFeet) {
        AreaSqFeet = areaSqFeet;
    }

    public String getPremiseLocated() {
        return PremiseLocated;
    }

    public void setPremiseLocated(String premiseLocated) {
        PremiseLocated = premiseLocated;
    }

    public String getPremiseFloor() {
        return PremiseFloor;
    }

    public void setPremiseFloor(String premiseFloor) {

        PremiseFloor = premiseFloor;
    }

    public String getPremiseStructure() {
        return PremiseStructure;
    }

    public void setPremiseStructure(String premiseStructure) {
        PremiseStructure = premiseStructure;
    }

    public String getPremiseRoof() {
        return PremiseRoof;
    }

    public void setPremiseRoof(String premiseRoof) {
        PremiseRoof = premiseRoof;
    }

    public String getFrontageObstructed() {
        return FrontageObstructed;
    }

    public void setFrontageObstructed(String frontageObstructed) {
        FrontageObstructed = frontageObstructed;
    }

    public String getFootpath() {
        return Footpath;
    }

    public void setFootpath(String footpath) {
        Footpath = footpath;
    }

    public String getBathroom() {
        return Bathroom;
    }

    public void setBathroom(String bathroom) {
        Bathroom = bathroom;
    }

    public String getPantry() {
        return Pantry;
    }

    public void setPantry(String pantry) {
        Pantry = pantry;
    }

    // Additional Property - As per new changes - 10th July 2017

    public String getPillers() {
        return Pillers;
    }

    public void setPillers(String pillers) {
        Pillers = pillers;
    }

    public String getWindows() {
        return Windows;
    }

    public void setWindows(String windows) {
        Windows = windows;
    }

    public String getBeam() {
        return Beam;
    }

    public void setBeam(String beam) {
        Beam = beam;
    }

    public String getAdjacentShops() {
        return AdjacentShops;
    }

    public void setAdjacentShops(String adjacentShops) {
        AdjacentShops = adjacentShops;
    }

    public String getMultipleEntries() {
        return MultipleEntries;
    }

    public void setMultipleEntries(String multipleEntries) {
        MultipleEntries = multipleEntries;
    }

    //END

    public String getSiteVisitedAddressId() {
        return SiteVisitedAddressId;
    }

    public void setSiteVisitedAddressId(String siteVisitedAddressId) {
        SiteVisitedAddressId = siteVisitedAddressId;
    }

    public String getSiteVisitAddress1() {
        return SiteVisitAddress1;
    }

    public void setSiteVisitAddress1(String siteVisitAddress1) {
        SiteVisitAddress1 = siteVisitAddress1;
    }

    public String getSiteVisitAddress2() {
        return SiteVisitAddress2;
    }

    public void setSiteVisitAddress2(String siteVisitAddress2) {
        SiteVisitAddress2 = siteVisitAddress2;
    }

    public String getSiteVisitLandmark() {
        return SiteVisitLandmark;
    }

    public void setSiteVisitLandmark(String siteVisitLandmark) {
        SiteVisitLandmark = siteVisitLandmark;
    }

    public String getSiteVisitState() {
        return SiteVisitState;
    }

    public void setSiteVisitState(String siteVisitState) {
        SiteVisitState = siteVisitState;
    }

    public String getSiteVisitDistrict() {
        return SiteVisitDistrict;
    }

    public void setSiteVisitDistrict(String siteVisitDistrict) {
        SiteVisitDistrict = siteVisitDistrict;
    }

    public String getSiteVisitVTC() {
        return SiteVisitVTC;
    }

    public void setSiteVisitVTC(String siteVisitVTC) {
        SiteVisitVTC = siteVisitVTC;
    }

    public String getSiteVisitWardNo() {
        return SiteVisitWardNo;
    }

    public void setSiteVisitWardNo(String siteVisitWardNo) {
        SiteVisitWardNo = siteVisitWardNo;
    }

    public String getSiteVisitPincode() {
        return SiteVisitPincode;
    }

    public void setSiteVisitPincode(String siteVisitPincode) {
        SiteVisitPincode = siteVisitPincode;
    }

    public String getPremiseLevel() {
        return PremiseLevel;
    }

    public void setPremiseLevel(String premiseLevel) {
        PremiseLevel = premiseLevel;
    }

    public String getPremiseShape() {
        return PremiseShape;
    }

    public void setPremiseShape(String premiseShape) {
        PremiseShape = premiseShape;
    }

    public String getClosestBankBranch1NbinCode() {
        return ClosestBankBranch1NbinCode;
    }

    public void setClosestBankBranch1NbinCode(String closestBankBranch1NbinCode) {
        ClosestBankBranch1NbinCode = closestBankBranch1NbinCode;
    }

    public String getClosestBankBranch2NbinCode() {
        return ClosestBankBranch2NbinCode;
    }

    public void setClosestBankBranch2NbinCode(String closestBankBranch2NbinCode) {
        ClosestBankBranch2NbinCode = closestBankBranch2NbinCode;
    }

    public String getClosestBankBranch3NbinCode() {
        return ClosestBankBranch3NbinCode;
    }

    public void setClosestBankBranch3NbinCode(String closestBankBranch3NbinCode) {
        ClosestBankBranch3NbinCode = closestBankBranch3NbinCode;
    }

    public String getClosestAtm1NbinCode() {
        return ClosestAtm1NbinCode;
    }

    public void setClosestAtm1NbinCode(String closestAtm1NbinCode) {
        ClosestAtm1NbinCode = closestAtm1NbinCode;
    }

    public String getClosestAtm2NbinCode() {
        return ClosestAtm2NbinCode;
    }

    public void setClosestAtm2NbinCode(String closestAtm2NbinCode) {
        ClosestAtm2NbinCode = closestAtm2NbinCode;
    }

    public String getClosestAtm3NbinCode() {
        return ClosestAtm3NbinCode;
    }

    public void setClosestAtm3NbinCode(String closestAtm3NbinCode) {
        ClosestAtm3NbinCode = closestAtm3NbinCode;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    // Get Time Line of Franchisee Site
    public List<FranchiseeTimeLineDetails> getTimeLineList() {

        // For NextGen Site Visit
        //Remarks = "{\"remarks\":[{\"id\":\"23\",\"nextgen_site_visit_id\":\"1069\",\"nextgen_site_visit_status\":\"5\",\"nextgen_site_visit_description\":\"GeoCordinates Updated\",\"nextgen_site_visit_remarks\":\"Remarks1\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh\",\"date_time\":\"14-04-2018 15:27:54\"},{\"id\":\"24\",\"nextgen_site_visit_id\":\"1069\",\"nextgen_site_visit_status\":\"15\",\"nextgen_site_visit_description\":\"Updated1\",\"nextgen_site_visit_remarks\":\"Remarks2\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh1\",\"date_time\":\"154-04-2018 15:27:54\"},{\"id\":\"23\",\"nextgen_site_visit_id\":\"1069\",\"nextgen_site_visit_status\":\"5\",\"nextgen_site_visit_description\":\"GeoCordinates Updated\",\"nextgen_site_visit_remarks\":\"Remarks1\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh\",\"date_time\":\"14-04-2018 15:27:54\"},{\"id\":\"24\",\"nextgen_site_visit_id\":\"1069\",\"nextgen_site_visit_status\":\"15\",\"nextgen_site_visit_description\":\"Updated1\",\"nextgen_site_visit_remarks\":\"Remarks2\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh1\",\"date_time\":\"154-04-2018 15:27:54\"},{\"id\":\"23\",\"nextgen_site_visit_id\":\"1069\",\"nextgen_site_visit_status\":\"5\",\"nextgen_site_visit_description\":\"GeoCordinates Updated\",\"nextgen_site_visit_remarks\":\"Remarks1\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh\",\"date_time\":\"14-04-2018 15:27:54\"},{\"id\":\"24\",\"nextgen_site_visit_id\":\"1069\",\"nextgen_site_visit_status\":\"15\",\"nextgen_site_visit_description\":\"Updated1\",\"nextgen_site_visit_remarks\":\"Remarks2\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh1\",\"date_time\":\"154-04-2018 15:27:54\"},{\"id\":\"23\",\"nextgen_site_visit_id\":\"1069\",\"nextgen_site_visit_status\":\"5\",\"nextgen_site_visit_description\":\"GeoCordinates Updated\",\"nextgen_site_visit_remarks\":\"Remarks1\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh\",\"date_time\":\"14-04-2018 15:27:54\"},{\"id\":\"24\",\"nextgen_site_visit_id\":\"1069\",\"nextgen_site_visit_status\":\"15\",\"nextgen_site_visit_description\":\"Updated1\",\"nextgen_site_visit_remarks\":\"Remarks2\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh1\",\"date_time\":\"154-04-2018 15:27:54\"},{\"id\":\"23\",\"nextgen_site_visit_id\":\"1069\",\"nextgen_site_visit_status\":\"5\",\"nextgen_site_visit_description\":\"GeoCordinates Updated\",\"nextgen_site_visit_remarks\":\"Remarks1\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh\",\"date_time\":\"14-04-2018 15:27:54\"},{\"id\":\"24\",\"nextgen_site_visit_id\":\"1069\",\"nextgen_site_visit_status\":\"3\",\"nextgen_site_visit_description\":\"Updated1\",\"nextgen_site_visit_remarks\":\"LOCATION:Invalid Location  PREMISE_SHAPE: shape ic FRONTAGE_IMAGE: image is blurr STATE: state is incorrect HEIGHT: height is not ok\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh1\",\"date_time\":\"154-04-2018 15:27:54\"}]}";
        // For NextGen Work Commencement
        // Remarks = "{\"remarks\":[{\"id\":\"23\",\"nextgen_site_work_commencement_id\":\"1069\",\"nextgen_site_work_status\":\"5\",\"nextgen_site_work_description\":\"GeoCordinates Updated\",\"nextgen_site_work_remarks\":\"Remarks1\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh\",\"date_time\":\"14-04-2018 15:27:54\"},{\"id\":\"24\",\"nextgen_site_work_commencement_id\":\"1069\",\"nextgen_site_work_status\":\"15\",\"nextgen_site_work_description\":\"Updated1\",\"nextgen_site_work_remarks\":\"Remarks2\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh1\",\"date_time\":\"154-04-2018 15:27:54\"},{\"id\":\"23\",\"nextgen_site_work_commencement_id\":\"1069\",\"nextgen_site_work_status\":\"5\",\"nextgen_site_work_description\":\"GeoCordinates Updated\",\"nextgen_site_work_remarks\":\"Remarks1\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh\",\"date_time\":\"14-04-2018 15:27:54\"},{\"id\":\"24\",\"nextgen_site_work_commencement_id\":\"1069\",\"nextgen_site_work_status\":\"15\",\"nextgen_site_work_description\":\"Updated1\",\"nextgen_site_work_remarks\":\"Remarks2\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh1\",\"date_time\":\"154-04-2018 15:27:54\"},{\"id\":\"23\",\"nextgen_site_work_commencement_id\":\"1069\",\"nextgen_site_work_status\":\"5\",\"nextgen_site_work_description\":\"GeoCordinates Updated\",\"nextgen_site_work_remarks\":\"Remarks1\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh\",\"date_time\":\"14-04-2018 15:27:54\"},{\"id\":\"24\",\"nextgen_site_work_commencement_id\":\"1069\",\"nextgen_site_work_status\":\"15\",\"nextgen_site_work_description\":\"Updated1\",\"nextgen_site_work_remarks\":\"Remarks2\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh1\",\"date_time\":\"154-04-2018 15:27:54\"},{\"id\":\"23\",\"nextgen_site_work_commencement_id\":\"1069\",\"nextgen_site_work_status\":\"5\",\"nextgen_site_work_description\":\"GeoCordinates Updated\",\"nextgen_site_work_remarks\":\"Remarks1\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh\",\"date_time\":\"14-04-2018 15:27:54\"},{\"id\":\"24\",\"nextgen_site_work_commencement_id\":\"1069\",\"nextgen_site_work_status\":\"15\",\"nextgen_site_work_description\":\"Updated1\",\"nextgen_site_work_remarks\":\"Remarks2\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh1\",\"date_time\":\"154-04-2018 15:27:54\"},{\"id\":\"23\",\"nextgen_site_work_commencement_id\":\"1069\",\"nextgen_site_work_status\":\"5\",\"nextgen_site_work_description\":\"GeoCordinates Updated\",\"nextgen_site_work_remarks\":\"Remarks1\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh\",\"date_time\":\"14-04-2018 15:27:54\"},{\"id\":\"24\",\"nextgen_site_work_commencement_id\":\"1069\",\"nextgen_site_work_status\":\"3\",\"nextgen_site_work_description\":\"Updated1\",\"nextgen_site_work_remarks\":\"LOCATION:Invalid Location  PREMISE_SHAPE: shape ic FRONTAGE_IMAGE: image is blurr STATE: state is incorrect HEIGHT: height is not ok\",\"user_id\":\"0\",\"user_name\":\"Deepak Singh1\",\"date_time\":\"154-04-2018 15:27:54\"}]}";

        //Check Remark is Empty
        if (TextUtils.isEmpty(getRemarks()))
            return null;

        try {
            JSONObject jsonObject = new JSONObject(getRemarks());
            JSONArray jsonArray = jsonObject.getJSONArray("remarks");

            Gson gson = new GsonBuilder().create();
            return gson.fromJson(jsonArray.toString(), new TypeToken<List<FranchiseeTimeLineDetails>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Additional Property
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public boolean isAllowToEdit() {
        return allowToEdit;
    }

    public void setAllowToEdit(boolean allowToEdit) {
        this.allowToEdit = allowToEdit;
    }

    public boolean isNeedToBeReviewed() {
        return isNeedToBeReviewed;
    }

    public void setNeedToBeReviewed(boolean needToBeReviewed) {
        isNeedToBeReviewed = needToBeReviewed;
    }

    public FranchiseeRemarkDetails getFranchiseeRemarkDetails() {
        return franchiseeRemarkDetails;
    }

    public void setFranchiseeRemarkDetails(FranchiseeRemarkDetails franchiseeRemarkDetails) {
        this.franchiseeRemarkDetails = franchiseeRemarkDetails;
    }

    public String getFranchiseeComments() {
        return franchiseeComments;
    }

    public void setFranchiseeComments(String franchiseeComments) {
        this.franchiseeComments = franchiseeComments;
    }

    public int getResubmitStatus() {
        return resubmitStatus;
    }

    public void setResubmitStatus(int resubmitStatus) {
        this.resubmitStatus = resubmitStatus;
    }

    //region Additional Property for Site Commencement
    public String getNextgenSiteWorkCommencementId() {
        return nextgenSiteWorkCommencementId;
    }

    public void setNextgenSiteWorkCommencementId(String nextgenSiteWorkCommencementId) {
        this.nextgenSiteWorkCommencementId = nextgenSiteWorkCommencementId;
    }

    public String getCommencementStartDate() {
        return commencementStartDate;
    }

    public void setCommencementStartDate(String commencementStartDate) {
        this.commencementStartDate = commencementStartDate;
    }

    public String getCommencementEstimatedEndDate() {
        return commencementEstimatedEndDate;
    }

    public void setCommencementEstimatedEndDate(String commencementEstimatedEndDate) {
        this.commencementEstimatedEndDate = commencementEstimatedEndDate;
    }

    public String getCommencementMinDate() {
        return commencementMinDate;
    }

    public void setCommencementMinDate(String commencementMinDate) {
        this.commencementMinDate = commencementMinDate;
    }

    //endregion

    //region NextGen Site Work In Progress
    public String getNextgenSiteWorkInProgressId() {
        return NextgenSiteWorkInProgressId;
    }

    public void setNextgenSiteWorkInProgressId(String nextgenSiteWorkInProgressId) {
        NextgenSiteWorkInProgressId = nextgenSiteWorkInProgressId;
    }

    public String getWipVisitDateTime() {
        return wipVisitDateTime;
    }

    public void setWipVisitDateTime(String wipVisitDateTime) {
        this.wipVisitDateTime = wipVisitDateTime;
    }

    public int getWipWorkOnTrack() {
        return wipWorkOnTrack;
    }

    public void setWipWorkOnTrack(int wipWorkOnTrack) {
        this.wipWorkOnTrack = wipWorkOnTrack;
    }

    public String getWipRevisedExpectedDateOfCompletion() {
        return wipRevisedExpectedDateOfCompletion;
    }

    public void setWipRevisedExpectedDateOfCompletion(String wipRevisedExpectedDateOfCompletion) {
        this.wipRevisedExpectedDateOfCompletion = wipRevisedExpectedDateOfCompletion;
    }

    public String getWipImages() {
        return wipImages;
    }

    public void setWipImages(String wipImages) {
        this.wipImages = wipImages;
    }

    public String getWipDetails() {
        return wipDetails;
    }

    public void setWipDetails(String wipDetails) {
        this.wipDetails = wipDetails;
    }

    public String getWipFieldForceRemarks() {
        return wipFieldForceRemarks;
    }

    public void setWipFieldForceRemarks(String wipFieldForceRemarks) {
        this.wipFieldForceRemarks = wipFieldForceRemarks;
    }

    //endregion

    //region NextGen Site Work Completion Intimation
    public String getWorkCompletionDate() {
        return workCompletionDate;
    }

    public void setWorkCompletionDate(String workCompletionDate) {
        this.workCompletionDate = workCompletionDate;
    }

    public String getWipLastVisitedDateTime() {
        return wipLastVisitedDateTime;
    }

    public void setWipLastVisitedDateTime(String wipLastVisitedDateTime) {
        this.wipLastVisitedDateTime = wipLastVisitedDateTime;
    }

    public String getWipLastVisitedBy() {
        return wipLastVisitedBy;
    }

    public void setWipLastVisitedBy(String wipLastVisitedBy) {
        this.wipLastVisitedBy = wipLastVisitedBy;
    }

    public int getWipLocationRange() {
        return wipLocationRange;
    }

    public void setWipLocationRange(int wipLocationRange) {
        this.wipLocationRange = wipLocationRange;
    }

    //endregion


    public String getInteriorWorkStatus() {
        return interiorWorkStatus;
    }

    public void setInteriorWorkStatus(String interiorWorkStatus) {
        this.interiorWorkStatus = interiorWorkStatus;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public String getExpectedCompletionDate() {
        return expectedCompletionDate;
    }

    public void setExpectedCompletionDate(String expectedCompletionDate) {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    public boolean isProfileReviewed() {
        return isProfileReviewed;
    }

    public void setProfileReviewed(boolean profileReviewed) {
        isProfileReviewed = profileReviewed;
    }

    public boolean isLocationReviewed() {
        return isLocationReviewed;
    }

    public void setLocationReviewed(boolean locationReviewed) {
        isLocationReviewed = locationReviewed;
    }

    public boolean isPhotoReviewed() {
        return isPhotoReviewed;
    }

    public void setPhotoReviewed(boolean photoReviewed) {
        isPhotoReviewed = photoReviewed;
    }

    public boolean isSiteVisitReviewed() {
        return isSiteVisitReviewed;
    }

    public void setSiteVisitReviewed(boolean siteVisitReviewed) {
        isSiteVisitReviewed = siteVisitReviewed;
    }

    public String getExpectedDateOfSiteIdentification() {
        return expectedDateOfSiteIdentification;
    }

    public void setExpectedDateOfSiteIdentification(String expectedDateOfSiteIdentification) {
        this.expectedDateOfSiteIdentification = expectedDateOfSiteIdentification;
    }

    public String getSiteNotIdentifiedRemarks() {
        return siteNotIdentifiedRemarks;
    }

    public void setSiteNotIdentifiedRemarks(String siteNotIdentifiedRemarks) {
        this.siteNotIdentifiedRemarks = siteNotIdentifiedRemarks;
    }

    public String getConsentExitReason() {
        return consentExitReason;
    }

    public void setConsentExitReason(String consentExitReason) {
        this.consentExitReason = consentExitReason;
    }

    public String getConsentExitRemarks() {
        return consentExitRemarks;
    }

    public void setConsentExitRemarks(String consentExitRemarks) {
        this.consentExitRemarks = consentExitRemarks;
    }

    public String getInteriorBrandingStatus() {
        return interiorBrandingStatus;
    }

    public void setInteriorBrandingStatus(String interiorBrandingStatus) {
        this.interiorBrandingStatus = interiorBrandingStatus;
    }

    public String getWipMileStoneId() {
        return wipMileStoneId;
    }

    public void setWipMileStoneId(String wipMileStoneId) {
        this.wipMileStoneId = wipMileStoneId;
    }

    public String getWipMileStoneDetail() {
        return wipMileStoneDetail;
    }

    public void setWipMileStoneDetail(String wipMileStoneDetail) {
        this.wipMileStoneDetail = wipMileStoneDetail;
    }

    public String getBranding_element_details() {
        return branding_element_details;
    }

    public void setBranding_element_details(String branding_element_details) {
        this.branding_element_details = branding_element_details;
    }

    public String getFrImageId() {
        return frImageId;
    }

    public void setFrImageId(String frImageId) {
        this.frImageId = frImageId;
    }

    public String getLwImageId() {
        return lwImageId;
    }

    public void setLwImageId(String lwImageId) {
        this.lwImageId = lwImageId;
    }

    public String getFwImageId() {
        return fwImageId;
    }

    public void setFwImageId(String fwImageId) {
        this.fwImageId = fwImageId;
    }

    public String getRwImageId() {
        return rwImageId;
    }

    public void setRwImageId(String rwImageId) {
        this.rwImageId = rwImageId;
    }

    public String getBwImageId() {
        return bwImageId;
    }

    public void setBwImageId(String bwImageId) {
        this.bwImageId = bwImageId;
    }

    public String getCeImageId() {
        return ceImageId;
    }

    public void setCeImageId(String ceImageId) {
        this.ceImageId = ceImageId;
    }

    public String getFlImageId() {
        return flImageId;
    }

    public void setFlImageId(String flImageId) {
        this.flImageId = flImageId;
    }

    public String getFrlImageId() {
        return frlImageId;
    }

    public void setFrlImageId(String frlImageId) {
        this.frlImageId = frlImageId;
    }

    public String getFrrImageId() {
        return frrImageId;
    }

    public void setFrrImageId(String frrImageId) {
        this.frrImageId = frrImageId;
    }

    public String getFroImageId() {
        return froImageId;
    }

    public void setFroImageId(String froImageId) {
        this.froImageId = froImageId;
    }

    public String getFr10ImageId() {
        return fr10ImageId;
    }

    public void setFr10ImageId(String fr10ImageId) {
        this.fr10ImageId = fr10ImageId;
    }

    public String getDesignElements() {
        return designElements;
    }

    public void setDesignElements(String designElements) {
        this.designElements = designElements;
    }

    public String getIsKendraPhotoAllowed() {
        return isKendraPhotoAllowed;
    }

    public void setIsKendraPhotoAllowed(String isKendraPhotoAllowed) {
        this.isKendraPhotoAllowed = isKendraPhotoAllowed;
    }

    public String getAddressProofType() {
        return addressProofType;
    }

    public void setAddressProofType(String addressProofType) {
        this.addressProofType = addressProofType;
    }

    public String getAddressProofNo() {
        return addressProofNo;
    }

    public void setAddressProofNo(String addressProofNo) {
        this.addressProofNo = addressProofNo;
    }

    public String getAddressProofImageId() {
        return addressProofImageId;
    }

    public void setAddressProofImageId(String addressProofImageId) {
        this.addressProofImageId = addressProofImageId;
    }

    public String getAddressProofImage() {
        return addressProofImage;
    }

    public void setAddressProofImage(String addressProofImage) {
        this.addressProofImage = addressProofImage;
    }

    public String getReadinessWorkCommencementStatus() {
        return readinessWorkCommencementStatus;
    }

    public void setReadinessWorkCommencementStatus(String readinessWorkCommencementStatus) {
        this.readinessWorkCommencementStatus = readinessWorkCommencementStatus;
    }

    public String getReadinessWorkCommencementDate() {
        return readinessWorkCommencementDate;
    }

    public void setReadinessWorkCommencementDate(String readinessWorkCommencementDate) {
        this.readinessWorkCommencementDate = readinessWorkCommencementDate;
    }

    public String getReadinessWorkCommencementMinDate() {
        return readinessWorkCommencementMinDate;
    }

    public void setReadinessWorkCommencementMinDate(String readinessWorkCommencementMinDate) {
        this.readinessWorkCommencementMinDate = readinessWorkCommencementMinDate;
    }

    public String getReadinessWorkCount() {
        return readinessWorkCount;
    }

    public void setReadinessWorkCount(String readinessWorkCount) {
        this.readinessWorkCount = readinessWorkCount;
    }

    public String getIsBrandingAllowed() {
        return isBrandingAllowed;
    }

    public void setIsBrandingAllowed(String isBrandingAllowed) {
        this.isBrandingAllowed = isBrandingAllowed;
    }

    public int getKendraRange() {
        return kendraRange;
    }

    public void setKendraRange(int kendraRange) {
        this.kendraRange = kendraRange;
    }

    public int getLocationAccuracy() {
        return locationAccuracy;
    }

    public void setLocationAccuracy(int locationAccuracy) {
        this.locationAccuracy = locationAccuracy;
    }

    public int getIsAddressChecked() {
        return IsAddressChecked;
    }

    public void setIsAddressChecked(int isAddressChecked) {
        IsAddressChecked = isAddressChecked;
    }

    //--

    public String getProvisionalLengthMainSignboard() {
        return provisionalLengthMainSignboard;
    }

    public void setProvisionalLengthMainSignboard(String provisionalLengthMainSignboard) {
        this.provisionalLengthMainSignboard = provisionalLengthMainSignboard;
    }


  /*  public String getProvisionalLength() {
        return provisionalLength;
    }

    public void setProvisionalLength(String provisionalLength) {
        this.provisionalLength = provisionalLength;
    }*/

    public String getProvisionalWidth() {
        return provisionalWidth;
    }

    public void setProvisionalWidth(String provisionalWidth) {
        this.provisionalWidth = provisionalWidth;
    }

    public String getHardwareDeliveryAddress() {
        return hardwareDeliveryAddress;
    }

    public void setHardwareDeliveryAddress(String hardwareDeliveryAddress) {
        this.hardwareDeliveryAddress = hardwareDeliveryAddress;
    }
}
