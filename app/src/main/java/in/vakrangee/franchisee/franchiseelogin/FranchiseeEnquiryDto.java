package in.vakrangee.franchisee.franchiseelogin;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FranchiseeEnquiryDto implements Serializable {

    @SerializedName("NextGenFranchiseeEnquiryId")
    private String NextGenFranchiseeEnquiryId;

    @SerializedName("Name")
    private String Name;

    @SerializedName("EnquiryDate")
    private String EnquiryDate;

    @SerializedName("EmailId")
    private String EmailId;
    @SerializedName("MobileNo")
    private String MobileNo;
    @SerializedName("Address")
    private String Address;
    @SerializedName("Status")
    private String Status; // FA Status

    private transient boolean IsSelected;

    public String getNextGenFranchiseeEnquiryId() {
        return NextGenFranchiseeEnquiryId;
    }

    public void setNextGenFranchiseeEnquiryId(String nextGenFranchiseeEnquiryId) {
        NextGenFranchiseeEnquiryId = nextGenFranchiseeEnquiryId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEnquiryDate() {
        return EnquiryDate;
    }

    public void setEnquiryDate(String enquiryDate) {
        EnquiryDate = enquiryDate;
    }

    public boolean isSelected() {
        return IsSelected;
    }

    public void setSelected(boolean selected) {
        IsSelected = selected;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
