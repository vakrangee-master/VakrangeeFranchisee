package in.vakrangee.franchisee.bcadetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BCABankDetailsDto implements Serializable {

    @SerializedName("bank_name_id")
    private String bankName;

    @SerializedName("bank_account_no")
    private String bankAccountNo;

    @SerializedName("branch_name")
    private String branchName;

    @SerializedName("bc_cbs_customer_id")
    private String bcCBSCustomerId;

    @SerializedName("ifcs_code")
    private String ifscCode;

    @SerializedName("sol_id")
    private String solId;

    @SerializedName("bca_saving_account_no")
    private String savingACNo;

    @SerializedName("bca_saving_account_name")
    private String savingACHolderName;

    @SerializedName("bca_saving_account_ifsc")
    private String savingACIfscCode;

    @SerializedName("ssa_id")
    private String ssa;

    @SerializedName("cif_no")
    private String cifNumber;

    @SerializedName("bca_city_code")
    private String cityCode;

    @SerializedName("bank_address")
    private String bankBranchAddress;

    @SerializedName("contact_no")
    private String phoneNumber;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBcCBSCustomerId() {
        return bcCBSCustomerId;
    }

    public void setBcCBSCustomerId(String bcCBSCustomerId) {
        this.bcCBSCustomerId = bcCBSCustomerId;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getSolId() {
        return solId;
    }

    public void setSolId(String solId) {
        this.solId = solId;
    }

    public String getSavingACNo() {
        return savingACNo;
    }

    public void setSavingACNo(String savingACNo) {
        this.savingACNo = savingACNo;
    }

    public String getSsa() {
        return ssa;
    }

    public void setSsa(String ssa) {
        this.ssa = ssa;
    }

    public String getCifNumber() {
        return cifNumber;
    }

    public void setCifNumber(String cifNumber) {
        this.cifNumber = cifNumber;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getBankBranchAddress() {
        return bankBranchAddress;
    }

    public void setBankBranchAddress(String bankBranchAddress) {
        this.bankBranchAddress = bankBranchAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSavingACHolderName() {
        return savingACHolderName;
    }

    public void setSavingACHolderName(String savingACHolderName) {
        this.savingACHolderName = savingACHolderName;
    }

    public String getSavingACIfscCode() {
        return savingACIfscCode;
    }

    public void setSavingACIfscCode(String savingACIfscCode) {
        this.savingACIfscCode = savingACIfscCode;
    }
}
