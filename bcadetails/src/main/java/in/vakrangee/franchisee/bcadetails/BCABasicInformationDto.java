package in.vakrangee.franchisee.bcadetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BCABasicInformationDto implements Serializable {

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

    @SerializedName("bca_spouse_title_id")
    private String bcaSpouseSalution;

    @SerializedName("bca_spouse_first_name")
    private String bcaSpouseFirstName;

    @SerializedName("bca_spouse_middle_name")
    private String bcaSpouseMiddleName;

    @SerializedName("bca_spouse_last_name")
    private String bcaSpouseLastName;

    @SerializedName("bca_mother_name")
    private String motherName;

    @SerializedName("bca_year_passing")
    private String yearOfPassing;

    @SerializedName("bca_qualification_id")
    private String qualification;

    @SerializedName("bca_relation_id")
    private String religion;

    @SerializedName("iibf_cert_no")
    private String iIBFCertificateNumber;

    @SerializedName("date_of_passing")
    private String dateOfPassing;

    @SerializedName("bca_ability")
    private String bcAbility;

    @SerializedName("bca_dob")
    private String bcaDOB;

    @SerializedName("bca_contact_address")
    private String bcaContactAddress;

    @SerializedName("bca_mobile_no")
    private String bcaMobileNumber;

    @SerializedName("category_id")
    private String category;

    @SerializedName("state_id")
    private String state;

    @SerializedName("district_id")
    private String district;

    @SerializedName("block_id")
    private String block;

    @SerializedName("vtc_id")
    private String vtc;

    @SerializedName("locality")
    private String locality;

    @SerializedName("pincode")
    private String pincode;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("bca_image_id")
    private String profilePicId;

    @SerializedName("profile_pic_base64")
    private String profilePicBase64;

    @SerializedName("bca_image_ext")
    private String profileExt;

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

    public String getBcaSpouseSalution() {
        return bcaSpouseSalution;
    }

    public void setBcaSpouseSalution(String bcaSpouseSalution) {
        this.bcaSpouseSalution = bcaSpouseSalution;
    }

    public String getBcaSpouseFirstName() {
        return bcaSpouseFirstName;
    }

    public void setBcaSpouseFirstName(String bcaSpouseFirstName) {
        this.bcaSpouseFirstName = bcaSpouseFirstName;
    }

    public String getBcaSpouseMiddleName() {
        return bcaSpouseMiddleName;
    }

    public void setBcaSpouseMiddleName(String bcaSpouseMiddleName) {
        this.bcaSpouseMiddleName = bcaSpouseMiddleName;
    }

    public String getBcaSpouseLastName() {
        return bcaSpouseLastName;
    }

    public void setBcaSpouseLastName(String bcaSpouseLastName) {
        this.bcaSpouseLastName = bcaSpouseLastName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getYearOfPassing() {
        return yearOfPassing;
    }

    public void setYearOfPassing(String yearOfPassing) {
        this.yearOfPassing = yearOfPassing;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getiIBFCertificateNumber() {
        return iIBFCertificateNumber;
    }

    public void setiIBFCertificateNumber(String iIBFCertificateNumber) {
        this.iIBFCertificateNumber = iIBFCertificateNumber;
    }

    public String getDateOfPassing() {
        return dateOfPassing;
    }

    public void setDateOfPassing(String dateOfPassing) {
        this.dateOfPassing = dateOfPassing;
    }

    public String getBcAbility() {
        return bcAbility;
    }

    public void setBcAbility(String bcAbility) {
        this.bcAbility = bcAbility;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
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

    public String getProfilePicId() {
        return profilePicId;
    }

    public void setProfilePicId(String profilePicId) {
        this.profilePicId = profilePicId;
    }

    public String getProfilePicBase64() {
        return profilePicBase64;
    }

    public void setProfilePicBase64(String profilePicBase64) {
        this.profilePicBase64 = profilePicBase64;
    }

    public String getProfileExt() {
        return profileExt;
    }

    public void setProfileExt(String profileExt) {
        this.profileExt = profileExt;
    }

}
