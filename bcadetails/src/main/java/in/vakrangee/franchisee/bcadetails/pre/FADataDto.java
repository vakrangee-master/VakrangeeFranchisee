package in.vakrangee.franchisee.bcadetails.pre;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FADataDto implements Serializable {

    @SerializedName("gender")
    private String gender;

    @SerializedName("fTitleId")
    private String fTitleId;

    @SerializedName("fFName")
    private String fFName;

    @SerializedName("fMName")
    private String fMName;

    @SerializedName("fLName")
    private String fLName;

    @SerializedName("frTitleId")
    private String frTitleId;

    @SerializedName("frFName")
    private String frFName;

    @SerializedName("frMName")
    private String frMName;

    @SerializedName("frLname")
    private String frLname;

    @SerializedName("frMobileNumber")
    private String frMobileNumber;

    @SerializedName("frAltMobileNumber")
    private String frAltMobileNumber;

    @SerializedName("frEmailId")
    private String frEmailId;

    @SerializedName("dob")
    private String dob;

    @SerializedName("maritalStatus")
    private String maritalStatus;

    @SerializedName("sTitleId")
    private String spouseTitleId;

    @SerializedName("sFName")
    private String spouseFName;

    @SerializedName("sMName")
    private String spouseMName;

    @SerializedName("sLName")
    private String spouseLName;

    @SerializedName("qualification")
    private String qualification;

    @SerializedName("panCardHolderName")
    private String panCardHolderName;

    @SerializedName("pannumber")
    private String pannumber;

    @SerializedName("panFile")
    private String panFile;

    @SerializedName("comAddress1")
    private String comAddress1;

    @SerializedName("comAddress2")
    private String comAddress2;

    @SerializedName("comLandmark")
    private String comLandmark;

    @SerializedName("comStateCode")
    private String comStateCode;

    @SerializedName("comDistrictCode")
    private String comDistrictCode;

    @SerializedName("comVillageCode")
    private String comVillageCode;

    @SerializedName("comPincode")
    private String comPincode;

    //@TODO: Not Set
    @SerializedName("vStateCode")
    private String vStateCode;

    @SerializedName("vDistrictCode")
    private String vDistrictCode;

    public String getComVillageCode() {
        return comVillageCode;
    }

    public void setComVillageCode(String comVillageCode) {
        this.comVillageCode = comVillageCode;
    }

    public String getPanCardHolderName() {
        return panCardHolderName;
    }

    public void setPanCardHolderName(String panCardHolderName) {
        this.panCardHolderName = panCardHolderName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getfFName() {
        return fFName;
    }

    public void setfFName(String fFName) {
        this.fFName = fFName;
    }

    public String getFrMName() {
        return frMName;
    }

    public void setFrMName(String frMName) {
        this.frMName = frMName;
    }

    public String getfTitleId() {
        return fTitleId;
    }

    public void setfTitleId(String fTitleId) {
        this.fTitleId = fTitleId;
    }

    public String getFrAltMobileNumber() {
        return frAltMobileNumber;
    }

    public void setFrAltMobileNumber(String frAltMobileNumber) {
        this.frAltMobileNumber = frAltMobileNumber;
    }

    public String getFrEmailId() {
        return frEmailId;
    }

    public void setFrEmailId(String frEmailId) {
        this.frEmailId = frEmailId;
    }

    public String getComPincode() {
        return comPincode;
    }

    public void setComPincode(String comPincode) {
        this.comPincode = comPincode;
    }

    public String getComDistrictCode() {
        return comDistrictCode;
    }

    public void setComDistrictCode(String comDistrictCode) {
        this.comDistrictCode = comDistrictCode;
    }

    public String getPanFile() {
        return panFile;
    }

    public void setPanFile(String panFile) {
        this.panFile = panFile;
    }

    public String getfMName() {
        return fMName;
    }

    public void setfMName(String fMName) {
        this.fMName = fMName;
    }

    public String getFrFName() {
        return frFName;
    }

    public void setFrFName(String frFName) {
        this.frFName = frFName;
    }

    public String getFrMobileNumber() {
        return frMobileNumber;
    }

    public void setFrMobileNumber(String frMobileNumber) {
        this.frMobileNumber = frMobileNumber;
    }

    public String getComStateCode() {
        return comStateCode;
    }

    public void setComStateCode(String comStateCode) {
        this.comStateCode = comStateCode;
    }

    public String getFrLname() {
        return frLname;
    }

    public void setFrLname(String frLname) {
        this.frLname = frLname;
    }

    public String getPannumber() {
        return pannumber;
    }

    public void setPannumber(String pannumber) {
        this.pannumber = pannumber;
    }

    public String getFrTitleId() {
        return frTitleId;
    }

    public void setFrTitleId(String frTitleId) {
        this.frTitleId = frTitleId;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getComLandmark() {
        return comLandmark;
    }

    public void setComLandmark(String comLandmark) {
        this.comLandmark = comLandmark;
    }

    public String getfLName() {
        return fLName;
    }

    public void setfLName(String fLName) {
        this.fLName = fLName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public String getComAddress1() {
        return comAddress1;
    }

    public void setComAddress1(String comAddress1) {
        this.comAddress1 = comAddress1;
    }

    public String getComAddress2() {
        return comAddress2;
    }

    public void setComAddress2(String comAddress2) {
        this.comAddress2 = comAddress2;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getSpouseTitleId() {
        return spouseTitleId;
    }

    public void setSpouseTitleId(String spouseTitleId) {
        this.spouseTitleId = spouseTitleId;
    }

    public String getSpouseFName() {
        return spouseFName;
    }

    public void setSpouseFName(String spouseFName) {
        this.spouseFName = spouseFName;
    }

    public String getSpouseMName() {
        return spouseMName;
    }

    public void setSpouseMName(String spouseMName) {
        this.spouseMName = spouseMName;
    }

    public String getSpouseLName() {
        return spouseLName;
    }

    public void setSpouseLName(String spouseLName) {
        this.spouseLName = spouseLName;
    }
}
