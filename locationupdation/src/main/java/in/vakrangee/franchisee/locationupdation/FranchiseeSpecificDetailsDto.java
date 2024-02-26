package in.vakrangee.franchisee.locationupdation;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FranchiseeSpecificDetailsDto implements Serializable {

    @SerializedName("kendra_location_status")
    private String kendraLocationStatus;

    @SerializedName("model_name")
    private String modelName;

    @SerializedName("frachisee_address")
    private String frachiseeAddress;

    @SerializedName("vkId")
    private String vkId;

    @SerializedName("name")
    private String name;

    @SerializedName("mobile_no")
    private String mobileNo;

    public String getKendraLocationStatus() {
        return kendraLocationStatus;
    }

    public void setKendraLocationStatus(String kendraLocationStatus) {
        this.kendraLocationStatus = kendraLocationStatus;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getFrachiseeAddress() {
        return frachiseeAddress;
    }

    public void setFrachiseeAddress(String frachiseeAddress) {
        this.frachiseeAddress = frachiseeAddress;
    }

    public String getVkId() {
        return vkId;
    }

    public void setVkId(String vkId) {
        this.vkId = vkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
