package in.vakrangee.core.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AgreementDispatchDto implements Serializable {

    @SerializedName("agreementDispatchType")
    private String agreementDispatchType;

    @SerializedName("courier_name")
    private String courier_name;

    @SerializedName("image_base64")
    private String image_base64;

    @SerializedName("image_id")
    private String image_id;

    @SerializedName("employee_id")
    private String employee_id;

    @SerializedName("awb_no")
    private String awb_no;
    //common
    @SerializedName(value = "date", alternate = "dispatch_date")
    private String date;

    @SerializedName("remarks")
    private String remarks;

    @SerializedName("nextgen_vakrangee_kendra_agreement_inward_id")
    private String nextgen_vakrangee_kendra_agreement_inward_id;

    @SerializedName("nextgen_vakrangee_kendra_agreement_dispatch_id")
    private String nextgen_vakrangee_kendra_agreement_dispatch_id;

    @SerializedName("min_courier_date")
    private String min_courier_date;

    public String getAgreementDispatchType() {
        return agreementDispatchType;
    }

    public void setAgreementDispatchType(String agreementDispatchType) {
        this.agreementDispatchType = agreementDispatchType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCourier_name() {
        return courier_name;
    }

    public void setCourier_name(String courier_name) {
        this.courier_name = courier_name;
    }

    public String getImage_base64() {
        return image_base64;
    }

    public void setImage_base64(String image_base64) {
        this.image_base64 = image_base64;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_iid) {
        this.image_id = image_iid;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAwb_no() {
        return awb_no;
    }

    public void setAwb_no(String awb_no) {
        this.awb_no = awb_no;
    }

    public String getNextgen_vakrangee_kendra_agreement_inward_id() {
        return nextgen_vakrangee_kendra_agreement_inward_id;
    }

    public void setNextgen_vakrangee_kendra_agreement_inward_id(String nextgen_vakrangee_kendra_agreement_inward_id) {
        this.nextgen_vakrangee_kendra_agreement_inward_id = nextgen_vakrangee_kendra_agreement_inward_id;
    }

    public String getNextgen_vakrangee_kendra_agreement_dispatch_id() {
        return nextgen_vakrangee_kendra_agreement_dispatch_id;
    }

    public void setNextgen_vakrangee_kendra_agreement_dispatch_id(String nextgen_vakrangee_kendra_agreement_dispatch_id) {
        this.nextgen_vakrangee_kendra_agreement_dispatch_id = nextgen_vakrangee_kendra_agreement_dispatch_id;
    }

    public String getMin_courier_date() {
        return min_courier_date;
    }

    public void setMin_courier_date(String min_courier_date) {
        this.min_courier_date = min_courier_date;
    }
}
