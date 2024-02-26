package in.vakrangee.core.franchiseelogin;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FranchiseeLoginChecksDto implements Serializable {

    @SerializedName("otp_verified")
    private String otpVerified;

    @SerializedName("expiry_time")
    private String expiryTime;

    @SerializedName("nextgen_enquiry_id")
    private String nextGenEnquiryId;

    @SerializedName("username")
    private String userName;

    @SerializedName("user_input_type")
    private String userInputType;

    public String getOtpVerified() {
        return otpVerified;
    }

    public void setOtpVerified(String otpVerified) {
        this.otpVerified = otpVerified;
    }

    public String getNextGenEnquiryId() {
        return nextGenEnquiryId;
    }

    public void setNextGenEnquiryId(String nextGenEnquiryId) {
        this.nextGenEnquiryId = nextGenEnquiryId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserInputType() {
        return userInputType;
    }

    public void setUserInputType(String userInputType) {
        this.userInputType = userInputType;
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }
}
