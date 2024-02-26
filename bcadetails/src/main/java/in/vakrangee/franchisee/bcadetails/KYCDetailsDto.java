package in.vakrangee.franchisee.bcadetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KYCDetailsDto implements Serializable {

    //region Basic Information
    @SerializedName("bca_salutation")
    private String bcaSalution;

    @SerializedName("bca_first_name")
    private String bcaFirstName;

    @SerializedName("bca_middle_name")
    private String bcaMiddleName;

    @SerializedName("bca_last_name")
    private String bcaLastName;

    @SerializedName("bca_father_salutation")
    private String bcaFatherNameSalution;

    @SerializedName("bca_father_first_name")
    private String bcaFatherFirstName;

    @SerializedName("bca_father_middle_name")
    private String bcaFatherMiddleName;

    @SerializedName("bca_father_last_name")
    private String bcaFatherLastName;

    @SerializedName("gender")
    private String gender;

    @SerializedName("marital_status")
    private String maritalStatus;

    @SerializedName("bca_dob")
    private String bcaDOB;

    @SerializedName("bca_contact_address")
    private String bcaContactAddress;

    @SerializedName("bca_mobile_no")
    private String bcaMobileNumber;

    @SerializedName("state_id")
    private String state;

    @SerializedName("district_id")
    private String district;

    @SerializedName("block_id")
    private String block;

    @SerializedName("vtc_id")
    private String vtc;

    @SerializedName("pincode")
    private String pincode;

    @SerializedName("bca_image_id")
    private String profilePicId;

    //endregion

    //region Supporting Information
    @SerializedName("email_id")
    private String emailId;

    @SerializedName("name_as_pan_card")
    private String nameAsPanCard;

    @SerializedName("aadhaar_no")
    private String aadhaarNumber;

    @SerializedName("pan_number")
    private String panNumber;

    @SerializedName("aadhaar_card_scan_copy_id")
    private String aadhaarCardScanId;

    @SerializedName("pan_card_scan_copy_id")
    private String panCardScanId;

    //endregion

    public String getBcaSalution() {
        return bcaSalution;
    }

    public void setBcaSalution(String bcaSalution) {
        this.bcaSalution = bcaSalution;
    }

    public String getBcaFirstName() {
        return bcaFirstName;
    }

    public void setBcaFirstName(String bcaFirstName) {
        this.bcaFirstName = bcaFirstName;
    }

    public String getBcaMiddleName() {
        return bcaMiddleName;
    }

    public void setBcaMiddleName(String bcaMiddleName) {
        this.bcaMiddleName = bcaMiddleName;
    }

    public String getBcaLastName() {
        return bcaLastName;
    }

    public void setBcaLastName(String bcaLastName) {
        this.bcaLastName = bcaLastName;
    }

    public String getBcaFatherNameSalution() {
        return bcaFatherNameSalution;
    }

    public void setBcaFatherNameSalution(String bcaFatherNameSalution) {
        this.bcaFatherNameSalution = bcaFatherNameSalution;
    }

    public String getBcaFatherFirstName() {
        return bcaFatherFirstName;
    }

    public void setBcaFatherFirstName(String bcaFatherFirstName) {
        this.bcaFatherFirstName = bcaFatherFirstName;
    }

    public String getBcaFatherMiddleName() {
        return bcaFatherMiddleName;
    }

    public void setBcaFatherMiddleName(String bcaFatherMiddleName) {
        this.bcaFatherMiddleName = bcaFatherMiddleName;
    }

    public String getBcaFatherLastName() {
        return bcaFatherLastName;
    }

    public void setBcaFatherLastName(String bcaFatherLastName) {
        this.bcaFatherLastName = bcaFatherLastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getBcaDOB() {
        return bcaDOB;
    }

    public void setBcaDOB(String bcaDOB) {
        this.bcaDOB = bcaDOB;
    }

    public String getBcaContactAddress() {
        return bcaContactAddress;
    }

    public void setBcaContactAddress(String bcaContactAddress) {
        this.bcaContactAddress = bcaContactAddress;
    }

    public String getBcaMobileNumber() {
        return bcaMobileNumber;
    }

    public void setBcaMobileNumber(String bcaMobileNumber) {
        this.bcaMobileNumber = bcaMobileNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getVtc() {
        return vtc;
    }

    public void setVtc(String vtc) {
        this.vtc = vtc;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getProfilePicId() {
        return profilePicId;
    }

    public void setProfilePicId(String profilePicId) {
        this.profilePicId = profilePicId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getNameAsPanCard() {
        return nameAsPanCard;
    }

    public void setNameAsPanCard(String nameAsPanCard) {
        this.nameAsPanCard = nameAsPanCard;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getAadhaarCardScanId() {
        return aadhaarCardScanId;
    }

    public void setAadhaarCardScanId(String aadhaarCardScanId) {
        this.aadhaarCardScanId = aadhaarCardScanId;
    }

    public String getPanCardScanId() {
        return panCardScanId;
    }

    public void setPanCardScanId(String panCardScanId) {
        this.panCardScanId = panCardScanId;
    }
}
