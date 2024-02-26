package in.vakrangee.franchisee.delivery_address;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeliveryAddressDto implements Serializable {

    @SerializedName("provisional_length_main_signboard")
    private String provisionalLengthMainSignboard;
    @SerializedName("provisional_width_main_signboard")
    private String provisionalWidth;
    @SerializedName("state_id")
    private String stateId;
    @SerializedName("district_id")
    private String districtId;
    @SerializedName("vtc_id")
    private String vtcId;
    @SerializedName("address_line_1")
    private String addressLine1;
    @SerializedName("address_line_2")
    private String addressLine2;
    @SerializedName("landmark")
    private String landmark;
    @SerializedName("area")
    private String area;
    @SerializedName("locality")
    private String locality;
    @SerializedName("pin_code")
    private String pinCode;
    @SerializedName("is_editable")
    private String isEditable;
    @SerializedName("is_flex_allowed")
    private String isFlex;
    @SerializedName("delivery_address_type")
    private String addressDeliveryType;
    @SerializedName("delivery_address_id")
    private String addressDeliveryId;

    public String getProvisionalLengthMainSignboard() {
        return provisionalLengthMainSignboard;
    }

    public void setProvisionalLengthMainSignboard(String provisionalLengthMainSignboard) {
        this.provisionalLengthMainSignboard = provisionalLengthMainSignboard;
    }

    public String getProvisionalWidth() {
        return provisionalWidth;
    }

    public void setProvisionalWidth(String provisionalWidth) {
        this.provisionalWidth = provisionalWidth;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getVtcId() {
        return vtcId;
    }

    public void setVtcId(String vtcId) {
        this.vtcId = vtcId;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(String isEditable) {
        this.isEditable = isEditable;
    }

    public String getIsFlex() {
        return isFlex;
    }

    public void setIsFlex(String isFlex) {
        this.isFlex = isFlex;
    }

    public String getAddressDeliveryType() {
        return addressDeliveryType;
    }

    public void setAddressDeliveryType(String addressDeliveryType) {
        this.addressDeliveryType = addressDeliveryType;
    }

    public String getAddressDeliveryId() {
        return addressDeliveryId;
    }

    public void setAddressDeliveryId(String addressDeliveryId) {
        this.addressDeliveryId = addressDeliveryId;
    }
}
