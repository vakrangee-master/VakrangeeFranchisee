package in.vakrangee.franchisee.bcadetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BCASupportingInformationDto implements Serializable {

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

    @SerializedName("aadhaar_card_scan_base64")
    private String aadhaarCardScanBase64;

    @SerializedName("aadhaar_img_ext")
    private String aadhaarCardScanExt;

    @SerializedName("pan_card_scan_copy_id")
    private String panCardScanId;

    @SerializedName("pan_card_scan_base64")
    private String panCardScanBase64;

    @SerializedName("pan_card_img_ext")
    private String panCardScanExt;

    @SerializedName("settlement_image_id")
    private String settlemtAccountCopyId;

    @SerializedName("settlemt_ac_copy_base64")
    private String settlemtAccountCopyBase64;

    @SerializedName("settlementImgExt")
    private String settlemtAccountCopyExt;

    @SerializedName("outlet_add_proof_id")
    private String outletAddProofId;

    @SerializedName("outlet_add_proof_base64")
    private String outletAddProofBase64;

    @SerializedName("outletAddImgExt")
    private String outletAddProofExt;

    @SerializedName("iibf_cert_copy_id")
    private String iibfCertificationCopyId;

    @SerializedName("iibf_certification_copy_base64")
    private String iibfCertificationCopyBase64;

    @SerializedName("iibfcertImgExt")
    private String iibfCertificationCopyExt;

    @SerializedName("bca_police_verification_image_id")
    private String bcaPoliceVerificationId;

    @SerializedName("bca_police_verification_base64")
    private String bcaPoliceVerificationBase64;

    @SerializedName("policaVerificationImgExt")
    private String bcaPoliceVerificationExt;

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

    public String getAadhaarCardScanBase64() {
        return aadhaarCardScanBase64;
    }

    public void setAadhaarCardScanBase64(String aadhaarCardScanBase64) {
        this.aadhaarCardScanBase64 = aadhaarCardScanBase64;
    }

    public String getAadhaarCardScanExt() {
        return aadhaarCardScanExt;
    }

    public void setAadhaarCardScanExt(String aadhaarCardScanExt) {
        this.aadhaarCardScanExt = aadhaarCardScanExt;
    }

    public String getPanCardScanId() {
        return panCardScanId;
    }

    public void setPanCardScanId(String panCardScanId) {
        this.panCardScanId = panCardScanId;
    }

    public String getPanCardScanBase64() {
        return panCardScanBase64;
    }

    public void setPanCardScanBase64(String panCardScanBase64) {
        this.panCardScanBase64 = panCardScanBase64;
    }

    public String getPanCardScanExt() {
        return panCardScanExt;
    }

    public void setPanCardScanExt(String panCardScanExt) {
        this.panCardScanExt = panCardScanExt;
    }

    public String getSettlemtAccountCopyId() {
        return settlemtAccountCopyId;
    }

    public void setSettlemtAccountCopyId(String settlemtAccountCopyId) {
        this.settlemtAccountCopyId = settlemtAccountCopyId;
    }

    public String getSettlemtAccountCopyBase64() {
        return settlemtAccountCopyBase64;
    }

    public void setSettlemtAccountCopyBase64(String settlemtAccountCopyBase64) {
        this.settlemtAccountCopyBase64 = settlemtAccountCopyBase64;
    }

    public String getSettlemtAccountCopyExt() {
        return settlemtAccountCopyExt;
    }

    public void setSettlemtAccountCopyExt(String settlemtAccountCopyExt) {
        this.settlemtAccountCopyExt = settlemtAccountCopyExt;
    }

    public String getOutletAddProofId() {
        return outletAddProofId;
    }

    public void setOutletAddProofId(String outletAddProofId) {
        this.outletAddProofId = outletAddProofId;
    }

    public String getOutletAddProofBase64() {
        return outletAddProofBase64;
    }

    public void setOutletAddProofBase64(String outletAddProofBase64) {
        this.outletAddProofBase64 = outletAddProofBase64;
    }

    public String getOutletAddProofExt() {
        return outletAddProofExt;
    }

    public void setOutletAddProofExt(String outletAddProofExt) {
        this.outletAddProofExt = outletAddProofExt;
    }

    public String getIibfCertificationCopyId() {
        return iibfCertificationCopyId;
    }

    public void setIibfCertificationCopyId(String iibfCertificationCopyId) {
        this.iibfCertificationCopyId = iibfCertificationCopyId;
    }

    public String getIibfCertificationCopyBase64() {
        return iibfCertificationCopyBase64;
    }

    public void setIibfCertificationCopyBase64(String iibfCertificationCopyBase64) {
        this.iibfCertificationCopyBase64 = iibfCertificationCopyBase64;
    }

    public String getIibfCertificationCopyExt() {
        return iibfCertificationCopyExt;
    }

    public void setIibfCertificationCopyExt(String iibfCertificationCopyExt) {
        this.iibfCertificationCopyExt = iibfCertificationCopyExt;
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
}
