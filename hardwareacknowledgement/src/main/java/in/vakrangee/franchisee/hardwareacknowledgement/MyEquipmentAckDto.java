package in.vakrangee.franchisee.hardwareacknowledgement;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyEquipmentAckDto implements Serializable {

    @SerializedName("NextGenFranchiseeApplicationId")
    private String Id;

    @SerializedName("ApplicationNo")
    private String applicationNo;

    @SerializedName("VKID")
    private String VKID;

    @SerializedName("FranchiseePicFile")
    private String franchiseePicBase64;

    @SerializedName("FranchiseeName")
    private String franchiseeName;

    @SerializedName("Address")
    private String address;

    @SerializedName("AckDetails")
    private String ackDetails;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getVKID() {
        return VKID;
    }

    public void setVKID(String VKID) {
        this.VKID = VKID;
    }

    public String getFranchiseeName() {
        return franchiseeName;
    }

    public void setFranchiseeName(String franchiseeName) {
        this.franchiseeName = franchiseeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAckDetails() {
        return ackDetails;
    }

    public void setAckDetails(String ackDetails) {
        this.ackDetails = ackDetails;
    }

    public String getFranchiseePicBase64() {
        return franchiseePicBase64;
    }

    public void setFranchiseePicBase64(String franchiseePicBase64) {
        this.franchiseePicBase64 = franchiseePicBase64;
    }
}
