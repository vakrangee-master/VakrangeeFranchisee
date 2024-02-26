package in.vakrangee.franchisee.payment_details;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RaiseDisputeDto implements Serializable {

    @SerializedName("franchisee_fee_credit_request_id")
    private String franchiseeFeeCreditRequestId;

    @SerializedName("payment_date")
    private String paymentDate;

    @SerializedName("bank_id")
    private String bankId;

    @SerializedName("bank_name")
    private String bankName;

    @SerializedName("ifsc_code")
    private String ifscCode;

    @SerializedName("account_holder_name")
    private String accountHolderName;

    @SerializedName("account_number")
    private String accountNumber;

    @SerializedName("transfer_mode")
    private String transferMode;

    @SerializedName("transfer_mode_id")
    private String transferModeId;

    @SerializedName("utr")
    private String utr;

    @SerializedName("rrn")
    private String rrn;

    @SerializedName("amount")
    private String amount;

    @SerializedName("proof_image_id")
    private String proofPicId;

    @SerializedName("proof_pic_base64")
    private String proofPicBase64;

    @SerializedName("proof_image_ext")
    private String proofExt;

    @SerializedName("is_Editable")
    private String isEditable;

    @SerializedName("raised_date")
    private String raisedDate;

    @SerializedName("remarks")
    private String remarks;

    /**
     *  0 - Pending - Orange
     *  1 - Verified = Green
     *  2 - Rejected - Red
     *
     */
    @SerializedName("status")
    private String status;

    @SerializedName("status_desc")
    private String statusDesc;

    @SerializedName("is_more_opened")
    private int isMoreOpened;

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getTransferModeId() {
        return transferModeId;
    }

    public void setTransferModeId(String transferModeId) {
        this.transferModeId = transferModeId;
    }

    public String getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(String isEditable) {
        this.isEditable = isEditable;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getUtr() {
        return utr;
    }

    public void setUtr(String utr) {
        this.utr = utr;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProofPicId() {
        return proofPicId;
    }

    public void setProofPicId(String proofPicId) {
        this.proofPicId = proofPicId;
    }

    public String getProofPicBase64() {
        return proofPicBase64;
    }

    public void setProofPicBase64(String proofPicBase64) {
        this.proofPicBase64 = proofPicBase64;
    }

    public String getProofExt() {
        return proofExt;
    }

    public void setProofExt(String proofExt) {
        this.proofExt = proofExt;
    }

    public String getTransferMode() {
        return transferMode;
    }

    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getRaisedDate() {
        return raisedDate;
    }

    public void setRaisedDate(String raisedDate) {
        this.raisedDate = raisedDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIsMoreOpened() {
        return isMoreOpened;
    }

    public void setIsMoreOpened(int isMoreOpened) {
        this.isMoreOpened = isMoreOpened;
    }

    public String getFranchiseeFeeCreditRequestId() {
        return franchiseeFeeCreditRequestId;
    }

    public void setFranchiseeFeeCreditRequestId(String franchiseeFeeCreditRequestId) {
        this.franchiseeFeeCreditRequestId = franchiseeFeeCreditRequestId;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
