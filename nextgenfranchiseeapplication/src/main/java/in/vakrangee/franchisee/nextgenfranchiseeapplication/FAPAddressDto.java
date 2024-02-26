package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FAPAddressDto implements Serializable {

    @SerializedName("IsSamePermanentAddress")
    private String IsSamePermanentAddress;

    @SerializedName("comAddressLine1")
    private String comAddressLine1;

    @SerializedName("comAddressLine2")
    private String comAdressLine2;

    @SerializedName("comLandMark")
    private String comLandmark;

    @SerializedName("comStateId")
    private String comState;

    @SerializedName("comDistrictId")
    private String comDistrict;

    @SerializedName("comVillageId")
    private String comVTC;

    @SerializedName("comPincode")
    private String comPincode;

    @SerializedName("perAddressLine1")
    private String perAddressLine1;

    @SerializedName("perAddressLine2")
    private String perAddressLine2;

    @SerializedName("perLandMark")
    private String perLandmark;

    @SerializedName("perStateId")
    private String perState;

    @SerializedName("perDistrictId")
    private String perDistrict;

    @SerializedName("perVillageId")
    private String perVTC;

    @SerializedName("perPincode")
    private String perPincode;

    @SerializedName("AddressProofTypeId")
    private String addressProofType;

    @SerializedName("AddressProofNumber")
    private String addressProofNumber1;

    @SerializedName("AddressProofFileId")
    private String AddressProofFileId;

    @SerializedName("AddressProofFileExt")
    private String AddressProofFileExt;

    @SerializedName("AddressProofBase64")
    private String addressProofCopyImageBase64;

    private transient String addressProofCopyFileName;

    public String getComAddressLine1() {
        return comAddressLine1;
    }

    public void setComAddressLine1(String comAddressLine1) {
        this.comAddressLine1 = comAddressLine1;
    }

    public String getComAdressLine2() {
        return comAdressLine2;
    }

    public void setComAdressLine2(String comAdressLine2) {
        this.comAdressLine2 = comAdressLine2;
    }

    public String getComLandmark() {
        return comLandmark;
    }

    public void setComLandmark(String comLandmark) {
        this.comLandmark = comLandmark;
    }

    public String getComState() {
        return comState;
    }

    public void setComState(String comState) {
        this.comState = comState;
    }

    public String getComDistrict() {
        return comDistrict;
    }

    public void setComDistrict(String comDistrict) {
        this.comDistrict = comDistrict;
    }

    public String getComVTC() {
        return comVTC;
    }

    public void setComVTC(String comVTC) {
        this.comVTC = comVTC;
    }

    public String getComPincode() {
        return comPincode;
    }

    public void setComPincode(String comPincode) {
        this.comPincode = comPincode;
    }

    public String getPerAddressLine1() {
        return perAddressLine1;
    }

    public void setPerAddressLine1(String perAddressLine1) {
        this.perAddressLine1 = perAddressLine1;
    }

    public String getPerAddressLine2() {
        return perAddressLine2;
    }

    public void setPerAddressLine2(String perAddressLine2) {
        this.perAddressLine2 = perAddressLine2;
    }

    public String getPerLandmark() {
        return perLandmark;
    }

    public void setPerLandmark(String perLandmark) {
        this.perLandmark = perLandmark;
    }

    public String getPerState() {
        return perState;
    }

    public void setPerState(String perState) {
        this.perState = perState;
    }

    public String getPerDistrict() {
        return perDistrict;
    }

    public void setPerDistrict(String perDistrict) {
        this.perDistrict = perDistrict;
    }

    public String getPerVTC() {
        return perVTC;
    }

    public void setPerVTC(String perVTC) {
        this.perVTC = perVTC;
    }

    public String getPerPincode() {
        return perPincode;
    }

    public void setPerPincode(String perPincode) {
        this.perPincode = perPincode;
    }

    public String getIsSamePermanentAddress() {
        return IsSamePermanentAddress;
    }

    public void setIsSamePermanentAddress(String isSamePermanentAddress) {
        IsSamePermanentAddress = isSamePermanentAddress;
    }

    public String getAddressProofType() {
        return addressProofType;
    }

    public void setAddressProofType(String addressProofType) {
        this.addressProofType = addressProofType;
    }

    public String getAddressProofNumber1() {
        return addressProofNumber1;
    }

    public void setAddressProofNumber1(String addressProofNumber1) {
        this.addressProofNumber1 = addressProofNumber1;
    }

    public String getAddressProofFileId() {
        return AddressProofFileId;
    }

    public void setAddressProofFileId(String addressProofFileId) {
        AddressProofFileId = addressProofFileId;
    }

    public String getAddressProofFileExt() {
        return AddressProofFileExt;
    }

    public void setAddressProofFileExt(String addressProofFileExt) {
        AddressProofFileExt = addressProofFileExt;
    }

    public String getAddressProofCopyImageBase64() {
        return addressProofCopyImageBase64;
    }

    public void setAddressProofCopyImageBase64(String addressProofCopyImageBase64) {
        this.addressProofCopyImageBase64 = addressProofCopyImageBase64;
    }

    public String getAddressProofCopyFileName() {
        return addressProofCopyFileName;
    }

    public void setAddressProofCopyFileName(String addressProofCopyFileName) {
        this.addressProofCopyFileName = addressProofCopyFileName;
    }
}
