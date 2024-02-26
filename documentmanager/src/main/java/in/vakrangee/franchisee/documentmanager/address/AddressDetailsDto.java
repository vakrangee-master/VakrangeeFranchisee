package in.vakrangee.franchisee.documentmanager.address;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddressDetailsDto implements Serializable {

    @SerializedName("is_permanent_address")
    private String isPermanentAddress;

    @SerializedName("address_type")
    private String addressType;

    @SerializedName("address_line1")
    private String addressLine1;

    @SerializedName("address_line2")
    private String addressLine2;

    @SerializedName("landmark")
    private String landmark;

    @SerializedName("state_id")
    private String state;

    @SerializedName("district_id")
    private String district;

    @SerializedName("vtc_id")
    private String vtc;

    @SerializedName("pincode")
    private String pincode;

    @SerializedName("address_proof_type")
    private String addressProofType;

    @SerializedName("address_proof_scan_copy_id")
    private String addressProofScanId;

    @SerializedName("address_proof_scan_base64")
    private String addressProofScanBase64;

    @SerializedName("address_proof_img_ext")
    private String addressProofScanExt;

    @SerializedName("IsEditable")
    private String IsEditable;

    private transient Bitmap bitmap;
    private transient android.net.Uri Uri;
    private transient boolean IsChangedPhoto;

    public String getAddressProofType() {
        return addressProofType;
    }

    public void setAddressProofType(String addressProofType) {
        this.addressProofType = addressProofType;
    }

    public String getAddressProofScanId() {
        return addressProofScanId;
    }

    public void setAddressProofScanId(String addressProofScanId) {
        this.addressProofScanId = addressProofScanId;
    }

    public String getAddressProofScanBase64() {
        return addressProofScanBase64;
    }

    public void setAddressProofScanBase64(String addressProofScanBase64) {
        this.addressProofScanBase64 = addressProofScanBase64;
    }

    public String getAddressProofScanExt() {
        return addressProofScanExt;
    }

    public void setAddressProofScanExt(String addressProofScanExt) {
        this.addressProofScanExt = addressProofScanExt;
    }

    public String getIsEditable() {
        return IsEditable;
    }

    public void setIsEditable(String isEditable) {
        IsEditable = isEditable;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public android.net.Uri getUri() {
        return Uri;
    }

    public void setUri(android.net.Uri uri) {
        Uri = uri;
    }

    public boolean isChangedPhoto() {
        return IsChangedPhoto;
    }

    public void setChangedPhoto(boolean changedPhoto) {
        IsChangedPhoto = changedPhoto;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getVtc() {
        return vtc;
    }

    public void setVtc(String vtc) {
        this.vtc = vtc;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getIsPermanentAddress() {
        return isPermanentAddress;
    }

    public void setIsPermanentAddress(String isPermanentAddress) {
        this.isPermanentAddress = isPermanentAddress;
    }
}
