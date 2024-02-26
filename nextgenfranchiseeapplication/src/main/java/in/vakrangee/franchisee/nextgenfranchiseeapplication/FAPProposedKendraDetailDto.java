package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FAPProposedKendraDetailDto implements Serializable {

    @SerializedName("vkModelTypeId")
    private String vakrangeeKendraModel;

    @SerializedName("nayara_model")
    private String nayaraModel;

    @SerializedName("InvestmentConsent")
    private String IsInvestmentConsent;

    @SerializedName("areaConsent")
    private String IsAreaConsent;

    @SerializedName("SourceOfFundId")
    private String sourceOfFund;

    @SerializedName("PremiseOwnershipTypeId")
    private String premiseOwnerShipType;

    @SerializedName("IsLocationNeedsToBeIdentified")
    private String IsLocationNeedsToBeIdentified;

    @SerializedName("IsLocationNeedsToBeIdentifiedShown")
    private String IsLocationNeedsToBeIdentifiedShown;

    @SerializedName("OwnerProofFileId")
    private String OwnerProofFileId;

    @SerializedName("OwnershipProofFileExt")
    private String OwnershipProofFileExt;

    @SerializedName("NOCProofFileId")
    private String NOCProofFileId;

    @SerializedName("NOCProofFileExt")
    private String NOCProofFileExt;

    @SerializedName("OutLetLocationFileId")
    private String OutLetLocationFileId;

    @SerializedName("OutletLocationFileExt")
    private String OutletLocationFileExt;

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

    @SerializedName("Remarks")
    private String remarks;

    @SerializedName("OwnedById")
    private String ownedBy;

    @SerializedName("OwnershipProofBase64")
    private String rentAgreementBase64;

    @SerializedName("NOCBase64")
    private String landLordSocietyNOCBase64;

    @SerializedName("KendraLocationBase64")
    private String kendraLocAddressProofBase64;

    private transient String rentAgreementFileName;
    private transient String landLordSocietyNOCFileName;
    private transient String kendraLocAddressProofFileName;

    public String getVakrangeeKendraModel() {
        return vakrangeeKendraModel;
    }

    public void setVakrangeeKendraModel(String vakrangeeKendraModel) {
        this.vakrangeeKendraModel = vakrangeeKendraModel;
    }

    public String getSourceOfFund() {
        return sourceOfFund;
    }

    public void setSourceOfFund(String sourceOfFund) {
        this.sourceOfFund = sourceOfFund;
    }

    public String getPremiseOwnerShipType() {
        return premiseOwnerShipType;
    }

    public void setPremiseOwnerShipType(String premiseOwnerShipType) {
        this.premiseOwnerShipType = premiseOwnerShipType;
    }

    public String getRentAgreementBase64() {
        return rentAgreementBase64;
    }

    public void setRentAgreementBase64(String rentAgreementBase64) {
        this.rentAgreementBase64 = rentAgreementBase64;
    }

    public String getLandLordSocietyNOCBase64() {
        return landLordSocietyNOCBase64;
    }

    public void setLandLordSocietyNOCBase64(String landLordSocietyNOCBase64) {
        this.landLordSocietyNOCBase64 = landLordSocietyNOCBase64;
    }

    public String getKendraLocAddressProofBase64() {
        return kendraLocAddressProofBase64;
    }

    public void setKendraLocAddressProofBase64(String kendraLocAddressProofBase64) {
        this.kendraLocAddressProofBase64 = kendraLocAddressProofBase64;
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

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRentAgreementFileName() {
        return rentAgreementFileName;
    }

    public void setRentAgreementFileName(String rentAgreementFileName) {
        this.rentAgreementFileName = rentAgreementFileName;
    }

    public String getLandLordSocietyNOCFileName() {
        return landLordSocietyNOCFileName;
    }

    public void setLandLordSocietyNOCFileName(String landLordSocietyNOCFileName) {
        this.landLordSocietyNOCFileName = landLordSocietyNOCFileName;
    }

    public String getKendraLocAddressProofFileName() {
        return kendraLocAddressProofFileName;
    }

    public void setKendraLocAddressProofFileName(String kendraLocAddressProofFileName) {
        this.kendraLocAddressProofFileName = kendraLocAddressProofFileName;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    public String getIsInvestmentConsent() {
        return IsInvestmentConsent;
    }

    public void setIsInvestmentConsent(String isInvestmentConsent) {
        IsInvestmentConsent = isInvestmentConsent;
    }

    public String getIsAreaConsent() {
        return IsAreaConsent;
    }

    public void setIsAreaConsent(String isAreaConsent) {
        IsAreaConsent = isAreaConsent;
    }

    public String getOwnerProofFileId() {
        return OwnerProofFileId;
    }

    public void setOwnerProofFileId(String ownerProofFileId) {
        OwnerProofFileId = ownerProofFileId;
    }

    public String getNOCProofFileId() {
        return NOCProofFileId;
    }

    public void setNOCProofFileId(String NOCProofFileId) {
        this.NOCProofFileId = NOCProofFileId;
    }

    public String getOutLetLocationFileId() {
        return OutLetLocationFileId;
    }

    public void setOutLetLocationFileId(String outLetLocationFileId) {
        OutLetLocationFileId = outLetLocationFileId;
    }

    public String getOwnershipProofFileExt() {
        return OwnershipProofFileExt;
    }

    public void setOwnershipProofFileExt(String ownershipProofFileExt) {
        OwnershipProofFileExt = ownershipProofFileExt;
    }

    public String getNOCProofFileExt() {
        return NOCProofFileExt;
    }

    public void setNOCProofFileExt(String NOCProofFileExt) {
        this.NOCProofFileExt = NOCProofFileExt;
    }

    public String getOutletLocationFileExt() {
        return OutletLocationFileExt;
    }

    public void setOutletLocationFileExt(String outletLocationFileExt) {
        OutletLocationFileExt = outletLocationFileExt;
    }

    public String getIsLocationNeedsToBeIdentified() {
        return IsLocationNeedsToBeIdentified;
    }

    public void setIsLocationNeedsToBeIdentified(String isLocationNeedsToBeIdentified) {
        IsLocationNeedsToBeIdentified = isLocationNeedsToBeIdentified;
    }

    public String getMunicipalCorporationId() {
        return municipalCorporationId;
    }

    public void setMunicipalCorporationId(String municipalCorporationId) {
        this.municipalCorporationId = municipalCorporationId;
    }

    public String getWardDetailId() {
        return wardDetailId;
    }

    public void setWardDetailId(String wardDetailId) {
        this.wardDetailId = wardDetailId;
    }

    public String getIsLocationNeedsToBeIdentifiedShown() {
        return IsLocationNeedsToBeIdentifiedShown;
    }

    public void setIsLocationNeedsToBeIdentifiedShown(String isLocationNeedsToBeIdentifiedShown) {
        IsLocationNeedsToBeIdentifiedShown = isLocationNeedsToBeIdentifiedShown;
    }

    public String getNayaraModel() {
        return nayaraModel;
    }

    public void setNayaraModel(String nayaraModel) {
        this.nayaraModel = nayaraModel;
    }
}
