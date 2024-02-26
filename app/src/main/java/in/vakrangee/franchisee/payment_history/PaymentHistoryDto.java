package in.vakrangee.franchisee.payment_history;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentHistoryDto implements Serializable {
    //details data
    @SerializedName("frachisee_name")
    private String frachiseeName;
    @SerializedName("frachisee_application_no")
    private String frachiseeApplicationNo;
    @SerializedName("model_type")
    private String modelType;
    @SerializedName("mobile_no")
    private String mobileNo;
    @SerializedName("alter_mobile_no")
    private String alterMobileNo;
    @SerializedName("email_id")
    private String emailId;
    @SerializedName("alter_email_id")
    private String alterEmailId;
    @SerializedName("total_fee")
    private String totalFee;
    @SerializedName("address")
    private String address;

    //adapter data
    @SerializedName("instalment_type")
    private String installmentType;
    @SerializedName("instalment_fee")
    private String installmentFee;
    @SerializedName(value = "instalment_payment_date", alternate = "instalment_date")
    private String instalmentPaymentDate;
    @SerializedName("instalment_step")
    private String installmentStep;


    public String getInstallmentType() {
        return installmentType;
    }

    public void setInstallmentType(String installmentType) {
        this.installmentType = installmentType;
    }

    public String getInstallmentFee() {
        return installmentFee;
    }

    public void setInstallmentFee(String installmentFee) {
        this.installmentFee = installmentFee;
    }

    public String getInstalmentPaymentDate() {
        return instalmentPaymentDate;
    }

    public void setInstalmentPaymentDate(String instalmentPaymentDate) {
        this.instalmentPaymentDate = instalmentPaymentDate;
    }

    public String getInstallmentStep() {
        return installmentStep;
    }

    public void setInstallmentStep(String installmentStep) {
        this.installmentStep = installmentStep;
    }

    public String getFrachiseeName() {
        return frachiseeName;
    }

    public void setFrachiseeName(String frachiseeName) {
        this.frachiseeName = frachiseeName;
    }

    public String getFrachiseeApplicationNo() {
        return frachiseeApplicationNo;
    }

    public void setFrachiseeApplicationNo(String frachiseeApplicationNo) {
        this.frachiseeApplicationNo = frachiseeApplicationNo;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAlterMobileNo() {
        return alterMobileNo;
    }

    public void setAlterMobileNo(String alterMobileNo) {
        this.alterMobileNo = alterMobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAlterEmailId() {
        return alterEmailId;
    }

    public void setAlterEmailId(String alterEmailId) {
        this.alterEmailId = alterEmailId;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
