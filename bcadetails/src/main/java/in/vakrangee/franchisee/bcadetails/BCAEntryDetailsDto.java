package in.vakrangee.franchisee.bcadetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BCAEntryDetailsDto implements Serializable {

    @SerializedName("isSameAsFranchiseeVisible")
    private String isSameAsFranchiseeVisible;

    @SerializedName("sameAsFranchisee")
    private String sameAsFranchisee;

    @SerializedName("vklId")
    private String vklId;

    @SerializedName("bcaId")
    private String bcaId;

    @SerializedName("kycId")
    private String kycId;

    @SerializedName("agreementId")
    private String agreementId;

    @SerializedName("bankInfo")
    private String bankInfoDetail;

    @SerializedName("basicInfo")
    private String basicInfoDetail;

    @SerializedName("outletInfo")
    private String outletInfoDetail;

    @SerializedName("supportingInfo")
    private String supportingInfoDetail;

    @SerializedName("otherInfo")
    private String otherInfoDetail;

    @SerializedName("bankInfoStatus")
    private String bankInfoStatus;

    @SerializedName("basicInfoStatus")
    private String basicInfoStatus;

    @SerializedName("outletInfoStatus")
    private String outletInfoStatus;

    @SerializedName("supportingInfoStatus")
    private String supportingInfoStatus;

    @SerializedName("otherInfoStatus")
    private String otherInfoStatus;

    @SerializedName("bca_status_detail")
    private String statusDetail;

    @SerializedName("kyc_detail")
    private String kycDetail;

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getKycId() {
        return kycId;
    }

    public void setKycId(String kycId) {
        this.kycId = kycId;
    }

    public String getSameAsFranchisee() {
        return sameAsFranchisee;
    }

    public void setSameAsFranchisee(String sameAsFranchisee) {
        this.sameAsFranchisee = sameAsFranchisee;
    }

    public String getKycDetail() {
        return kycDetail;
    }

    public void setKycDetail(String kycDetail) {
        this.kycDetail = kycDetail;
    }

    public String getBasicInfoDetail() {
        return basicInfoDetail;
    }

    public void setBasicInfoDetail(String basicInfoDetail) {
        this.basicInfoDetail = basicInfoDetail;
    }

    public String getBasicInfoStatus() {
        return basicInfoStatus;
    }

    public void setBasicInfoStatus(String basicInfoStatus) {
        this.basicInfoStatus = basicInfoStatus;
    }

    public String getBankInfoDetail() {
        return bankInfoDetail;
    }

    public void setBankInfoDetail(String bankInfoDetail) {
        this.bankInfoDetail = bankInfoDetail;
    }

    public String getBankInfoStatus() {
        return bankInfoStatus;
    }

    public void setBankInfoStatus(String bankInfoStatus) {
        this.bankInfoStatus = bankInfoStatus;
    }

    public String getOutletInfoDetail() {
        return outletInfoDetail;
    }

    public void setOutletInfoDetail(String outletInfoDetail) {
        this.outletInfoDetail = outletInfoDetail;
    }

    public String getOutletInfoStatus() {
        return outletInfoStatus;
    }

    public void setOutletInfoStatus(String outletInfoStatus) {
        this.outletInfoStatus = outletInfoStatus;
    }

    public String getSupportingInfoDetail() {
        return supportingInfoDetail;
    }

    public void setSupportingInfoDetail(String supportingInfoDetail) {
        this.supportingInfoDetail = supportingInfoDetail;
    }

    public String getSupportingInfoStatus() {
        return supportingInfoStatus;
    }

    public void setSupportingInfoStatus(String supportingInfoStatus) {
        this.supportingInfoStatus = supportingInfoStatus;
    }

    public String getOtherInfoDetail() {
        return otherInfoDetail;
    }

    public void setOtherInfoDetail(String otherInfoDetail) {
        this.otherInfoDetail = otherInfoDetail;
    }

    public String getOtherInfoStatus() {
        return otherInfoStatus;
    }

    public void setOtherInfoStatus(String otherInfoStatus) {
        this.otherInfoStatus = otherInfoStatus;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public String getBcaId() {
        return bcaId;
    }

    public void setBcaId(String bcaId) {
        this.bcaId = bcaId;
    }

    public String getVklId() {
        return vklId;
    }

    public void setVklId(String vklId) {
        this.vklId = vklId;
    }

    public String getIsSameAsFranchiseeVisible() {
        return isSameAsFranchiseeVisible;
    }

    public void setIsSameAsFranchiseeVisible(String isSameAsFranchiseeVisible) {
        this.isSameAsFranchiseeVisible = isSameAsFranchiseeVisible;
    }
}
