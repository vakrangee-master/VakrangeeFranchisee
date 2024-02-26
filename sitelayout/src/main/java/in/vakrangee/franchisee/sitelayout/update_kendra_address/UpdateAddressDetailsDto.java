package in.vakrangee.franchisee.sitelayout.update_kendra_address;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateAddressDetailsDto implements Serializable {

    @SerializedName("IsEditable")
    private String IsEditable;

    @SerializedName("is_address_edit_allow")
    private String is_address_edit_allow;

    @SerializedName("address_edit_allow_msg")
    private String address_edit_allow_msg;

    @SerializedName("AddressLine1")
    private String addressLine1;

    @SerializedName("AddressLine2")
    private String addressLine2;

    @SerializedName("LandMark")
    private String landMark;

    @SerializedName("StateId")
    private String state;

    @SerializedName("DistrictId")
    private String district;

    @SerializedName("VillageId")
    private String VTC;

    @SerializedName("municipal_corporation_id")
    private String municipalCorporationId;

    @SerializedName("WardNumber")
    private String wardNo;

    @SerializedName("lg_ward_detail_id")
    private String wardDetailId;

    @SerializedName("Pincode")
    private String pinCode;

    @SerializedName("OutLetLocationFileId")
    private String OutLetLocationFileId;

    @SerializedName("OutletLocationFileExt")
    private String OutletLocationFileExt;

    @SerializedName("KendraLocationBase64")
    private String kendraLocAddressProofBase64;

    private transient String kendraLocAddressProofFileName;
    private transient Bitmap bitmap_LOC;
    private transient android.net.Uri Uri_LOC;
    private transient boolean IsChangedPhoto_LOC;

    @SerializedName("PremiseOwnershipTypeId")
    private String premiseOwnerShipType;

    @SerializedName("OwnedById")
    private String ownedBy;

    @SerializedName("OwnerProofFileId")
    private String OwnerProofFileId;

    @SerializedName("OwnershipProofFileExt")
    private String OwnershipProofFileExt;

    @SerializedName("OwnershipProofBase64")
    private String rentAgreementBase64;

    private transient String rentAgreementFileName;
    private transient Bitmap bitmap_RENT;
    private transient android.net.Uri Uri_RENT;
    private transient boolean IsChangedPhoto_RENT;

    @SerializedName("NOCProofFileId")
    private String NOCProofFileId;

    @SerializedName("NOCProofFileExt")
    private String NOCProofFileExt;

    @SerializedName("NOCBase64")
    private String landLordSocietyNOCBase64;

    private transient String landLordSocietyNOCFileName;

    private transient Bitmap bitmap_NOC;
    private transient android.net.Uri Uri_NOC;
    private transient boolean IsChangedPhoto_NOC;

    public String getOutLetLocationFileId() {
        return OutLetLocationFileId;
    }

    public void setOutLetLocationFileId(String outLetLocationFileId) {
        OutLetLocationFileId = outLetLocationFileId;
    }

    public String getOutletLocationFileExt() {
        return OutletLocationFileExt;
    }

    public void setOutletLocationFileExt(String outletLocationFileExt) {
        OutletLocationFileExt = outletLocationFileExt;
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

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
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

    public String getVTC() {
        return VTC;
    }

    public void setVTC(String VTC) {
        this.VTC = VTC;
    }

    public String getMunicipalCorporationId() {
        return municipalCorporationId;
    }

    public void setMunicipalCorporationId(String municipalCorporationId) {
        this.municipalCorporationId = municipalCorporationId;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getWardDetailId() {
        return wardDetailId;
    }

    public void setWardDetailId(String wardDetailId) {
        this.wardDetailId = wardDetailId;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getKendraLocAddressProofBase64() {
        return kendraLocAddressProofBase64;
    }

    public void setKendraLocAddressProofBase64(String kendraLocAddressProofBase64) {
        this.kendraLocAddressProofBase64 = kendraLocAddressProofBase64;
    }

    public String getKendraLocAddressProofFileName() {
        return kendraLocAddressProofFileName;
    }

    public void setKendraLocAddressProofFileName(String kendraLocAddressProofFileName) {
        this.kendraLocAddressProofFileName = kendraLocAddressProofFileName;
    }

    public String getIsEditable() {
        return IsEditable;
    }

    public void setIsEditable(String isEditable) {
        IsEditable = isEditable;
    }

    public Bitmap getBitmap_LOC() {
        return bitmap_LOC;
    }

    public void setBitmap_LOC(Bitmap bitmap_LOC) {
        this.bitmap_LOC = bitmap_LOC;
    }

    public Uri getUri_LOC() {
        return Uri_LOC;
    }

    public void setUri_LOC(Uri uri_LOC) {
        Uri_LOC = uri_LOC;
    }

    public boolean isChangedPhoto_LOC() {
        return IsChangedPhoto_LOC;
    }

    public void setChangedPhoto_LOC(boolean changedPhoto_LOC) {
        IsChangedPhoto_LOC = changedPhoto_LOC;
    }

    public String getPremiseOwnerShipType() {
        return premiseOwnerShipType;
    }

    public void setPremiseOwnerShipType(String premiseOwnerShipType) {
        this.premiseOwnerShipType = premiseOwnerShipType;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    public String getOwnerProofFileId() {
        return OwnerProofFileId;
    }

    public void setOwnerProofFileId(String ownerProofFileId) {
        OwnerProofFileId = ownerProofFileId;
    }

    public String getOwnershipProofFileExt() {
        return OwnershipProofFileExt;
    }

    public void setOwnershipProofFileExt(String ownershipProofFileExt) {
        OwnershipProofFileExt = ownershipProofFileExt;
    }

    public String getRentAgreementBase64() {
        return rentAgreementBase64;
    }

    public void setRentAgreementBase64(String rentAgreementBase64) {
        this.rentAgreementBase64 = rentAgreementBase64;
    }

    public String getRentAgreementFileName() {
        return rentAgreementFileName;
    }

    public void setRentAgreementFileName(String rentAgreementFileName) {
        this.rentAgreementFileName = rentAgreementFileName;
    }

    public Bitmap getBitmap_RENT() {
        return bitmap_RENT;
    }

    public void setBitmap_RENT(Bitmap bitmap_RENT) {
        this.bitmap_RENT = bitmap_RENT;
    }

    public Uri getUri_RENT() {
        return Uri_RENT;
    }

    public void setUri_RENT(Uri uri_RENT) {
        Uri_RENT = uri_RENT;
    }

    public boolean isChangedPhoto_RENT() {
        return IsChangedPhoto_RENT;
    }

    public void setChangedPhoto_RENT(boolean changedPhoto_RENT) {
        IsChangedPhoto_RENT = changedPhoto_RENT;
    }

    public String getNOCProofFileId() {
        return NOCProofFileId;
    }

    public void setNOCProofFileId(String NOCProofFileId) {
        this.NOCProofFileId = NOCProofFileId;
    }

    public String getNOCProofFileExt() {
        return NOCProofFileExt;
    }

    public void setNOCProofFileExt(String NOCProofFileExt) {
        this.NOCProofFileExt = NOCProofFileExt;
    }

    public String getLandLordSocietyNOCBase64() {
        return landLordSocietyNOCBase64;
    }

    public void setLandLordSocietyNOCBase64(String landLordSocietyNOCBase64) {
        this.landLordSocietyNOCBase64 = landLordSocietyNOCBase64;
    }

    public String getLandLordSocietyNOCFileName() {
        return landLordSocietyNOCFileName;
    }

    public void setLandLordSocietyNOCFileName(String landLordSocietyNOCFileName) {
        this.landLordSocietyNOCFileName = landLordSocietyNOCFileName;
    }

    public Bitmap getBitmap_NOC() {
        return bitmap_NOC;
    }

    public void setBitmap_NOC(Bitmap bitmap_NOC) {
        this.bitmap_NOC = bitmap_NOC;
    }

    public Uri getUri_NOC() {
        return Uri_NOC;
    }

    public void setUri_NOC(Uri uri_NOC) {
        Uri_NOC = uri_NOC;
    }

    public boolean isChangedPhoto_NOC() {
        return IsChangedPhoto_NOC;
    }

    public void setChangedPhoto_NOC(boolean changedPhoto_NOC) {
        IsChangedPhoto_NOC = changedPhoto_NOC;
    }

    public String getIs_address_edit_allow() {
        return is_address_edit_allow;
    }

    public void setIs_address_edit_allow(String is_address_edit_allow) {
        this.is_address_edit_allow = is_address_edit_allow;
    }

    public String getAddress_edit_allow_msg() {
        return address_edit_allow_msg;
    }

    public void setAddress_edit_allow_msg(String address_edit_allow_msg) {
        this.address_edit_allow_msg = address_edit_allow_msg;
    }
}
