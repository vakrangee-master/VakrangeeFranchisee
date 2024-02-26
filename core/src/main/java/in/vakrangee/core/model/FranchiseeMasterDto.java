package in.vakrangee.core.model;

import java.io.Serializable;

public class FranchiseeMasterDto implements Serializable {

    String VKID;
    String UserName;
    String EmailId;
    String MobileNumber;
    String TokenId;
    String Status;
    String MahavitranBillUnitNo;
    String ProfileImage;
    String Tracking;

    public String getVKID() {
        return VKID;
    }

    public void setVKID(String VKID) {
        this.VKID = VKID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getTokenId() {
        return TokenId;
    }

    public void setTokenId(String tokenId) {
        TokenId = tokenId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMahavitranBillUnitNo() {
        return MahavitranBillUnitNo;
    }

    public void setMahavitranBillUnitNo(String mahavitranBillUnitNo) {
        MahavitranBillUnitNo = mahavitranBillUnitNo;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getTracking() {
        return Tracking;
    }

    public void setTracking(String tracking) {
        Tracking = tracking;
    }
}
