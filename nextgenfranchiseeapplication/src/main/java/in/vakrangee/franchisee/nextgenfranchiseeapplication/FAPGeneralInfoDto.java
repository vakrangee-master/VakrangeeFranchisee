package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FAPGeneralInfoDto implements Serializable {

    @SerializedName("IsPanAvailable")
    private String isHavingPANCard;

    @SerializedName("PanCardHolderName")
    private String nameOnPANCard;

    @SerializedName("PanNumber")
    private String panNumber1;

    @SerializedName("PanFileId")
    private String PanFileId;

    @SerializedName("PanFileExt")
    private String PanFileExt;

    @SerializedName("AadhaarEID")
    private String isAadhaarEID;

    @SerializedName("AadhaarNumber")
    private String AadhaarNumber1;

    @SerializedName("AadhaarCardHolderName")
    private String nameOnAadhaarCard;

    @SerializedName("AadhaarFileId")
    private String AadhaarFileId;

    @SerializedName("AadhaarFileExt")
    private String AadhaarFileExt;

    @SerializedName("AddressProofTypeId")
    private String addressProofType;

    @SerializedName("AddressProofNumber")
    private String addressProofNumber1;

    @SerializedName("AddressProofFileId")
    private String AddressProofFileId;

    @SerializedName("AddressProofFileExt")
    private String AddressProofFileExt;

    @SerializedName("GSTINCerticateName")
    private String gSTINName;

    @SerializedName("GSTINNumber")
    private String gSTINNumber;

    @SerializedName("GSTINStateId")
    private String gSTINState;

    @SerializedName("GSTINUploadFileId")
    private String GSTINUploadFileId;

    @SerializedName("GstinUploadFileExt")
    private String GstinUploadFileExt;

    @SerializedName("GSTINUploadBase64")
    private String gstINUploadBase64;

    @SerializedName("PANCardBase64")
    private String panCardCopyImageBase64;

    @SerializedName("AadhaarCardBase64")
    private String adhaarCardCopyImageBase64;

    @SerializedName("AddressProofBase64")
    private String addressProofCopyImageBase64;

    private transient String gstINUploadFileName;
    private transient String panCardCopyFileName;
    private transient String adhaarCardCopyFileName;
    private transient String addressProofCopyFileName;

    @SerializedName("gstin_applied_date")
    private String gstinAppliedDate;

    @SerializedName("gstin_expected_date")
    private String gstinExpectedDate;

    @SerializedName("gstin_issue_date")
    private String gstinIssueDate;

    @SerializedName("msme_number")
    private String msmeNumber;

    @SerializedName("msme_image_id")
    private String msmeImageId;

    @SerializedName("msme_image_base64")
    private String msmeImageBase64;

    @SerializedName("msme_image_ext")
    private String msmeImageExt;

    @SerializedName("msme_issue_date")
    private String msmeIssueDate;

    @SerializedName("name_on_msme")
    private String nameOnMsme;

    @SerializedName("cibil_score")
    private String cibilScore;

    @SerializedName("cibil_report_min_date")
    private String cibilReportMinDate;

    @SerializedName("cibil_report_date")
    private String cibilReportDate;

    @SerializedName("cibil_image_id")
    private String cibilImageId;

    @SerializedName("cibil_base64")
    private String cibilBase64;

    @SerializedName("cibilImgExt")
    private String cibilExt;

    @SerializedName("police_verification_min_date")
    private String bcaPoliceVerificationMinDate;

    @SerializedName("police_verification_date")
    private String bcaPoliceVerificationDate;

    @SerializedName("police_verification_image_id")
    private String bcaPoliceVerificationId;

    @SerializedName("police_verification_base64")
    private String bcaPoliceVerificationBase64;

    @SerializedName("policeVerificationImgExt")
    private String bcaPoliceVerificationExt;

    public String getIsHavingPANCard() {
        return isHavingPANCard;
    }

    public void setIsHavingPANCard(String isHavingPANCard) {
        this.isHavingPANCard = isHavingPANCard;
    }

    public String getNameOnPANCard() {
        return nameOnPANCard;
    }

    public void setNameOnPANCard(String nameOnPANCard) {
        this.nameOnPANCard = nameOnPANCard;
    }

    public String getPanCardCopyImageBase64() {
        return panCardCopyImageBase64;
    }

    public void setPanCardCopyImageBase64(String panCardCopyImageBase64) {
        this.panCardCopyImageBase64 = panCardCopyImageBase64;
    }

    public String getIsAadhaarEID() {
        return isAadhaarEID;
    }

    public void setIsAadhaarEID(String isAadhaarEID) {
        this.isAadhaarEID = isAadhaarEID;
    }

    public String getNameOnAadhaarCard() {
        return nameOnAadhaarCard;
    }

    public void setNameOnAadhaarCard(String nameOnAadhaarCard) {
        this.nameOnAadhaarCard = nameOnAadhaarCard;
    }

    public String getAdhaarCardCopyImageBase64() {
        return adhaarCardCopyImageBase64;
    }

    public void setAdhaarCardCopyImageBase64(String adhaarCardCopyImageBase64) {
        this.adhaarCardCopyImageBase64 = adhaarCardCopyImageBase64;
    }

    public String getAddressProofType() {
        return addressProofType;
    }

    public void setAddressProofType(String addressProofType) {
        this.addressProofType = addressProofType;
    }

    public String getAddressProofCopyImageBase64() {
        return addressProofCopyImageBase64;
    }

    public void setAddressProofCopyImageBase64(String addressProofCopyImageBase64) {
        this.addressProofCopyImageBase64 = addressProofCopyImageBase64;
    }

    public String getgSTINNumber() {
        return gSTINNumber;
    }

    public void setgSTINNumber(String gSTINNumber) {
        this.gSTINNumber = gSTINNumber;
    }

    public String getGstINUploadBase64() {
        return gstINUploadBase64;
    }

    public void setGstINUploadBase64(String gstINUploadBase64) {
        this.gstINUploadBase64 = gstINUploadBase64;
    }

    public String getPanNumber1() {
        return panNumber1;
    }

    public void setPanNumber1(String panNumber1) {
        this.panNumber1 = panNumber1;
    }


    public String getAadhaarNumber1() {
        return AadhaarNumber1;
    }

    public void setAadhaarNumber1(String aadhaarNumber1) {
        AadhaarNumber1 = aadhaarNumber1;
    }


    public String getAddressProofNumber1() {
        return addressProofNumber1;
    }

    public void setAddressProofNumber1(String addressProofNumber1) {
        this.addressProofNumber1 = addressProofNumber1;
    }


    public String getPanCardCopyFileName() {
        return panCardCopyFileName;
    }

    public void setPanCardCopyFileName(String panCardCopyFileName) {
        this.panCardCopyFileName = panCardCopyFileName;
    }

    public String getAdhaarCardCopyFileName() {
        return adhaarCardCopyFileName;
    }

    public void setAdhaarCardCopyFileName(String adhaarCardCopyFileName) {
        this.adhaarCardCopyFileName = adhaarCardCopyFileName;
    }

    public String getAddressProofCopyFileName() {
        return addressProofCopyFileName;
    }

    public void setAddressProofCopyFileName(String addressProofCopyFileName) {
        this.addressProofCopyFileName = addressProofCopyFileName;
    }

    public String getGstINUploadFileName() {
        return gstINUploadFileName;
    }

    public void setGstINUploadFileName(String gstINUploadFileName) {
        this.gstINUploadFileName = gstINUploadFileName;
    }

    public String getPanFileId() {
        return PanFileId;
    }

    public void setPanFileId(String panFileId) {
        PanFileId = panFileId;
    }

    public String getAadhaarFileId() {
        return AadhaarFileId;
    }

    public void setAadhaarFileId(String aadhaarFileId) {
        AadhaarFileId = aadhaarFileId;
    }

    public String getAddressProofFileId() {
        return AddressProofFileId;
    }

    public void setAddressProofFileId(String addressProofFileId) {
        AddressProofFileId = addressProofFileId;
    }

    public String getGSTINUploadFileId() {
        return GSTINUploadFileId;
    }

    public void setGSTINUploadFileId(String GSTINUploadFileId) {
        this.GSTINUploadFileId = GSTINUploadFileId;
    }

    public String getPanFileExt() {
        return PanFileExt;
    }

    public void setPanFileExt(String panFileExt) {
        PanFileExt = panFileExt;
    }

    public String getAadhaarFileExt() {
        return AadhaarFileExt;
    }

    public void setAadhaarFileExt(String aadhaarFileExt) {
        AadhaarFileExt = aadhaarFileExt;
    }

    public String getAddressProofFileExt() {
        return AddressProofFileExt;
    }

    public void setAddressProofFileExt(String addressProofFileExt) {
        AddressProofFileExt = addressProofFileExt;
    }

    public String getGstinUploadFileExt() {
        return GstinUploadFileExt;
    }

    public void setGstinUploadFileExt(String gstinUploadFileExt) {
        GstinUploadFileExt = gstinUploadFileExt;
    }

    public String getgSTINName() {
        return gSTINName;
    }

    public void setgSTINName(String gSTINName) {
        this.gSTINName = gSTINName;
    }

    public String getgSTINState() {
        return gSTINState;
    }

    public void setgSTINState(String gSTINState) {
        this.gSTINState = gSTINState;
    }

    public String getGstinAppliedDate() {
        return gstinAppliedDate;
    }

    public void setGstinAppliedDate(String gstinAppliedDate) {
        this.gstinAppliedDate = gstinAppliedDate;
    }

    public String getGstinExpectedDate() {
        return gstinExpectedDate;
    }

    public void setGstinExpectedDate(String gstinExpectedDate) {
        this.gstinExpectedDate = gstinExpectedDate;
    }

    public String getGstinIssueDate() {
        return gstinIssueDate;
    }

    public void setGstinIssueDate(String gstinIssueDate) {
        this.gstinIssueDate = gstinIssueDate;
    }

    public String getMsmeNumber() {
        return msmeNumber;
    }

    public void setMsmeNumber(String msmeNumber) {
        this.msmeNumber = msmeNumber;
    }

    public String getMsmeImageId() {
        return msmeImageId;
    }

    public void setMsmeImageId(String msmeImageId) {
        this.msmeImageId = msmeImageId;
    }

    public String getMsmeIssueDate() {
        return msmeIssueDate;
    }

    public void setMsmeIssueDate(String msmeIssueDate) {
        this.msmeIssueDate = msmeIssueDate;
    }

    public String getNameOnMsme() {
        return nameOnMsme;
    }

    public void setNameOnMsme(String nameOnMsme) {
        this.nameOnMsme = nameOnMsme;
    }

    public String getMsmeImageBase64() {
        return msmeImageBase64;
    }

    public void setMsmeImageBase64(String msmeImageBase64) {
        this.msmeImageBase64 = msmeImageBase64;
    }

    public String getMsmeImageExt() {
        return msmeImageExt;
    }

    public void setMsmeImageExt(String msmeImageExt) {
        this.msmeImageExt = msmeImageExt;
    }

    public String getCibilScore() {
        return cibilScore;
    }

    public void setCibilScore(String cibilScore) {
        this.cibilScore = cibilScore;
    }

    public String getCibilReportMinDate() {
        return cibilReportMinDate;
    }

    public void setCibilReportMinDate(String cibilReportMinDate) {
        this.cibilReportMinDate = cibilReportMinDate;
    }

    public String getCibilReportDate() {
        return cibilReportDate;
    }

    public void setCibilReportDate(String cibilReportDate) {
        this.cibilReportDate = cibilReportDate;
    }

    public String getBcaPoliceVerificationMinDate() {
        return bcaPoliceVerificationMinDate;
    }

    public void setBcaPoliceVerificationMinDate(String bcaPoliceVerificationMinDate) {
        this.bcaPoliceVerificationMinDate = bcaPoliceVerificationMinDate;
    }

    public String getBcaPoliceVerificationDate() {
        return bcaPoliceVerificationDate;
    }

    public void setBcaPoliceVerificationDate(String bcaPoliceVerificationDate) {
        this.bcaPoliceVerificationDate = bcaPoliceVerificationDate;
    }

    public String getBcaPoliceVerificationId() {
        return bcaPoliceVerificationId;
    }

    public void setBcaPoliceVerificationId(String bcaPoliceVerificationId) {
        this.bcaPoliceVerificationId = bcaPoliceVerificationId;
    }

    public String getBcaPoliceVerificationBase64() {
        return bcaPoliceVerificationBase64;
    }

    public void setBcaPoliceVerificationBase64(String bcaPoliceVerificationBase64) {
        this.bcaPoliceVerificationBase64 = bcaPoliceVerificationBase64;
    }

    public String getBcaPoliceVerificationExt() {
        return bcaPoliceVerificationExt;
    }

    public void setBcaPoliceVerificationExt(String bcaPoliceVerificationExt) {
        this.bcaPoliceVerificationExt = bcaPoliceVerificationExt;
    }

    public String getCibilImageId() {
        return cibilImageId;
    }

    public void setCibilImageId(String cibilImageId) {
        this.cibilImageId = cibilImageId;
    }

    public String getCibilBase64() {
        return cibilBase64;
    }

    public void setCibilBase64(String cibilBase64) {
        this.cibilBase64 = cibilBase64;
    }

    public String getCibilExt() {
        return cibilExt;
    }

    public void setCibilExt(String cibilExt) {
        this.cibilExt = cibilExt;
    }

}
