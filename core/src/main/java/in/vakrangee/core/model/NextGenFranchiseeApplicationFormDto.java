package in.vakrangee.core.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NextGenFranchiseeApplicationFormDto implements Serializable {

    @SerializedName("nextgenApplicationId")
    private String nextgenApplicationId;

    @SerializedName("nextgenApplicationNo")
    private String nextgenApplicationNo;

    @SerializedName("nextgenEnquiryId")
    private String nextgenEnquiryId;

    @SerializedName("franchiseeDetail")
    private String franchiseeDetails;

    @SerializedName("franchiseeDetailStatus")
    private String franchiseeDetailStatus;

    @SerializedName("address")
    private String address;

    @SerializedName("addressStatus")
    private String addressStatus;

    @SerializedName("contactInfo")
    private String contactInfo;

    @SerializedName("contactInfoStatus")
    private String contactInfoStatus;

    @SerializedName("generalInfo")
    private String generalInfo;

    @SerializedName("generalInfoStatus")
    private String generalInfoStatus;

    @SerializedName("bankDetail")
    private String bankDetails;

    @SerializedName("bankDetailStatus")
    private String bankDetailStatus;

    @SerializedName("proposedVKDetail")
    private String proposedVakrangeeKendraDetail;

    @SerializedName("proposedVKDetailStatus")
    private String proposedVKDetailStatus;

    @SerializedName("references")
    private String references;

    @SerializedName("referencesStatus")
    private String referencesStatus;

    @SerializedName("criteria")
    private String criteria;

    @SerializedName("criteriaStatus")
    private String criteriaStatus;

    @SerializedName("fa_status_detail")
    private String statusDetail;

    @SerializedName("isBCA")
    private String isBCA;

    public String getFranchiseeDetails() {
        return franchiseeDetails;
    }

    public void setFranchiseeDetails(String franchiseeDetails) {
        this.franchiseeDetails = franchiseeDetails;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getGeneralInfo() {
        return generalInfo;
    }

    public void setGeneralInfo(String generalInfo) {
        this.generalInfo = generalInfo;
    }

    public String getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(String bankDetails) {
        this.bankDetails = bankDetails;
    }

    public String getProposedVakrangeeKendraDetail() {
        return proposedVakrangeeKendraDetail;
    }

    public void setProposedVakrangeeKendraDetail(String proposedVakrangeeKendraDetail) {
        this.proposedVakrangeeKendraDetail = proposedVakrangeeKendraDetail;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getNextgenApplicationId() {
        return nextgenApplicationId;
    }

    public void setNextgenApplicationId(String nextgenApplicationId) {
        this.nextgenApplicationId = nextgenApplicationId;
    }

    public String getNextgenApplicationNo() {
        return nextgenApplicationNo;
    }

    public void setNextgenApplicationNo(String nextgenApplicationNo) {
        this.nextgenApplicationNo = nextgenApplicationNo;
    }

    public String getNextgenEnquiryId() {
        return nextgenEnquiryId;
    }

    public void setNextgenEnquiryId(String nextgenEnquiryId) {
        this.nextgenEnquiryId = nextgenEnquiryId;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public String getFranchiseeDetailStatus() {
        return franchiseeDetailStatus;
    }

    public void setFranchiseeDetailStatus(String franchiseeDetailStatus) {
        this.franchiseeDetailStatus = franchiseeDetailStatus;
    }

    public String getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(String addressStatus) {
        this.addressStatus = addressStatus;
    }

    public String getContactInfoStatus() {
        return contactInfoStatus;
    }

    public void setContactInfoStatus(String contactInfoStatus) {
        this.contactInfoStatus = contactInfoStatus;
    }

    public String getGeneralInfoStatus() {
        return generalInfoStatus;
    }

    public void setGeneralInfoStatus(String generalInfoStatus) {
        this.generalInfoStatus = generalInfoStatus;
    }

    public String getBankDetailStatus() {
        return bankDetailStatus;
    }

    public void setBankDetailStatus(String bankDetailStatus) {
        this.bankDetailStatus = bankDetailStatus;
    }

    public String getProposedVKDetailStatus() {
        return proposedVKDetailStatus;
    }

    public void setProposedVKDetailStatus(String proposedVKDetailStatus) {
        this.proposedVKDetailStatus = proposedVKDetailStatus;
    }

    public String getReferencesStatus() {
        return referencesStatus;
    }

    public void setReferencesStatus(String referencesStatus) {
        this.referencesStatus = referencesStatus;
    }

    public String getCriteriaStatus() {
        return criteriaStatus;
    }

    public void setCriteriaStatus(String criteriaStatus) {
        this.criteriaStatus = criteriaStatus;
    }

    public String getIsBCA() {
        return isBCA;
    }

    public void setIsBCA(String isBCA) {
        this.isBCA = isBCA;
    }
}
