package in.vakrangee.franchisee.locationupdation;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KendraSavePhotoDto implements Serializable {

    private transient boolean IsChangedPhoto;

    private transient Bitmap bitmap;

    @SerializedName("is_editable")
    private String IsEditable;

    @SerializedName("nextgen_franchisee_application_id")
    private String nextgenFranchiseeApplicationId;

    @SerializedName("varkrangee_kendra_location_verification_id")
    private String varkrangeeKendraLocationVerificationId;

    @SerializedName("addressline1")
    private String addressline1;

    @SerializedName("addressline2")
    private String addressline2;

    @SerializedName("landmark")
    private String landmark;

    @SerializedName("pincode")
    private String pincode;

    @SerializedName("state_id")
    private String stateId;

    @SerializedName("district_id")
    private String districtId;

    @SerializedName("vtc_id")
    private String vtcId;

    @SerializedName("vkId")
    private String vkId;

    @SerializedName("frontage_base64")
    private String fortageImage;

    @SerializedName("frontage_image_id")
    private String frontageImageId;

    @SerializedName("frontage_image_ext")
    private String fortageImageExtension;

    @SerializedName("face_detection_count")
    private String faceDetectionCount;

    @SerializedName("franchisee_name")
    private String franchiseeName;

    @SerializedName("email_id")
    private String emailId;

    @SerializedName("mobile_no")
    private String mobileNo;

    @SerializedName("kendra_application_status")
    private String kendraApplicationStatus;

    @SerializedName("franchisee_profile_image_id")
    private String franchiseeProfileImageId;

    //add
    @SerializedName("frachisee_address")
    private String frachiseeAddress;

    @SerializedName("accuracy")
    private String accuracy;

    @SerializedName("kendra_location_status")
    private String kendraLocationStatus;

    @SerializedName("model_name")
    private String modelName;

    @SerializedName("name")
    private String name;

    @SerializedName("fr_vkid")
    private String fr_vkid;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("fr_profile_id")
    private String frProfileId;

    public boolean isChangedPhoto() {
        return IsChangedPhoto;
    }

    public void setChangedPhoto(boolean changedPhoto) {
        IsChangedPhoto = changedPhoto;
    }

    public String getFortageImage() {
        return fortageImage;
    }

    public void setFortageImage(String fortageImage) {
        this.fortageImage = fortageImage;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }



  /*  public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }*/

    public String getAddressline1() {
        return addressline1;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

    public String getAddressline2() {
        return addressline2;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getVtcId() {
        return vtcId;
    }

    public void setVtcId(String vtcId) {
        this.vtcId = vtcId;
    }

    public String getVkId() {
        return vkId;
    }

    public void setVkId(String vkId) {
        this.vkId = vkId;
    }

    public String getFrontageImageId() {
        return frontageImageId;
    }

    public void setFrontageImageId(String frontageImageId) {
        this.frontageImageId = frontageImageId;
    }

    public String getFortageImageExtension() {
        return fortageImageExtension;
    }

    public void setFortageImageExtension(String fortageImageExtension) {
        this.fortageImageExtension = fortageImageExtension;
    }

    public String getFaceDetectionCount() {
        return faceDetectionCount;
    }

    public void setFaceDetectionCount(String faceDetectionCount) {
        this.faceDetectionCount = faceDetectionCount;
    }

    public String getFranchiseeName() {
        return franchiseeName;
    }

    public void setFranchiseeName(String franchiseeName) {
        this.franchiseeName = franchiseeName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getKendraApplicationStatus() {
        return kendraApplicationStatus;
    }

    public void setKendraApplicationStatus(String kendraApplicationStatus) {
        this.kendraApplicationStatus = kendraApplicationStatus;
    }

    public String getFranchiseeProfileImageId() {
        return franchiseeProfileImageId;
    }

    public void setFranchiseeProfileImageId(String franchiseeProfileImageId) {
        this.franchiseeProfileImageId = franchiseeProfileImageId;
    }

    public String getFrachiseeAddress() {
        return frachiseeAddress;
    }

    public void setFrachiseeAddress(String frachiseeAddress) {
        this.frachiseeAddress = frachiseeAddress;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getKendraLocationStatus() {
        return kendraLocationStatus;
    }

    public void setKendraLocationStatus(String kendraLocationStatus) {
        this.kendraLocationStatus = kendraLocationStatus;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFr_vkid() {
        return fr_vkid;
    }

    public void setFr_vkid(String fr_vkid) {
        this.fr_vkid = fr_vkid;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getFrProfileId() {
        return frProfileId;
    }

    public void setFrProfileId(String frProfileId) {
        this.frProfileId = frProfileId;
    }

    public String getNextgenFranchiseeApplicationId() {
        return nextgenFranchiseeApplicationId;
    }

    public void setNextgenFranchiseeApplicationId(String nextgenFranchiseeApplicationId) {
        this.nextgenFranchiseeApplicationId = nextgenFranchiseeApplicationId;
    }

    public String getVarkrangeeKendraLocationVerificationId() {
        return varkrangeeKendraLocationVerificationId;
    }

    public void setVarkrangeeKendraLocationVerificationId(String varkrangeeKendraLocationVerificationId) {
        this.varkrangeeKendraLocationVerificationId = varkrangeeKendraLocationVerificationId;
    }

    public String getIsEditable() {
        return IsEditable;
    }

    public void setIsEditable(String isEditable) {
        IsEditable = isEditable;
    }
}
