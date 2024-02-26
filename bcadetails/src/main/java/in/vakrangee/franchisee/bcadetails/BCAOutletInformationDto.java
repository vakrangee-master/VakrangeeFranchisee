package in.vakrangee.franchisee.bcadetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BCAOutletInformationDto implements Serializable {

    @SerializedName("location_type")
    private String locationType;

    @SerializedName("outlet_state_id")
    private String state;

    @SerializedName("outlet_division_id")
    private String division;

    @SerializedName("outlet_district_id")
    private String district;

    @SerializedName("outlet_tehsil_id")
    private String block;

    @SerializedName("outlet_village_id")
    private String vtc;

    @SerializedName("outlet_ward_id")
    private String ward;

    @SerializedName("outlet_address")
    private String bcaOutletAddress;

    @SerializedName("outlet_locality")
    private String locality;

    @SerializedName("outlet_pin_code")
    private String pincode;

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getVtc() {
        return vtc;
    }

    public void setVtc(String vtc) {
        this.vtc = vtc;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getBcaOutletAddress() {
        return bcaOutletAddress;
    }

    public void setBcaOutletAddress(String bcaOutletAddress) {
        this.bcaOutletAddress = bcaOutletAddress;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
