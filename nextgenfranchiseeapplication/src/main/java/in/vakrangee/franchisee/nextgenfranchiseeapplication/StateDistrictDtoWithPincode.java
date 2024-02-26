package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StateDistrictDtoWithPincode implements Serializable {

    @SerializedName("pincode")
    private String pincode;

    @SerializedName("state_id")
    private String stateId;

    @SerializedName("state_name")
    private String stateName;

    @SerializedName("district_id")
    private String districtId;

    @SerializedName("district_name")
    private String districtName;

    @SerializedName("v_district_code")
    private String districtCode;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }


}
