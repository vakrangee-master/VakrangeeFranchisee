package in.vakrangee.franchisee.gwr.gwrwitness_camera;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WitnessListDto implements Serializable {


    @SerializedName("guinness_witness_id")
    @Expose
    private String guinnessWitnessId;
    @SerializedName("vkid")
    @Expose
    private String vkid;
    @SerializedName("witness_title")
    @Expose
    private String witnessTitle;
    @SerializedName("witness_f_name")
    @Expose
    private String witnessFName;
    @SerializedName("witness_m_name")
    @Expose
    private String witnessMName;
    @SerializedName("witness_l_name")
    @Expose
    private String witnessLName;
    @SerializedName("witness_address_id")
    @Expose
    private String witnessAddressId;
    @SerializedName("address_line_1")
    @Expose
    private String addressLine1;
    @SerializedName("address_line_2")
    @Expose
    private String addressLine2;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("vtc")
    @Expose
    private String vtc;
    @SerializedName("pin_code")
    @Expose
    private String pinCode;
    @SerializedName("witness_organization_name")
    @Expose
    private String witnessOrganizationName;
    @SerializedName("witness_nationality")
    @Expose
    private String witnessNationality;
    @SerializedName("witness_mobile_number")
    @Expose
    private String witnessMobileNumber;
    @SerializedName("witness_alt_mobile_number")
    @Expose
    private String witnessAltMobileNumber;
    @SerializedName("witness_landline_number")
    @Expose
    private String witnessLandlineNumber;
    @SerializedName("witness_email_id")
    @Expose
    private String witnessEmailId;
    @SerializedName("witness_occupation")
    @Expose
    private String witnessOccupation;
    @SerializedName("witness_business_card_image_id")
    @Expose
    private String witnessBusinessCardImageId;
    @SerializedName("witness_business_card_image_file_ext")
    @Expose
    private String witnessBusinessCardImageFileExt;
    @SerializedName("witness_pic_id")
    @Expose
    private String witnessPicId;
    @SerializedName("witness_pic_file_ext")
    @Expose
    private String witnessPicFileExt;

    @SerializedName("witness_business_card_image")
    @Expose
    private String witnessBusinessCardImage;
    @SerializedName("witness_pic")
    @Expose
    private String witnessPic;
    @SerializedName("witness_statement_image")
    @Expose
    private String witnessStatementImage;
    @SerializedName("witness_statement_image_file_ext")
    @Expose
    private String witnessStatementImageFileExt;
    @SerializedName("status")
    private String statusWitness;

    @SerializedName("status_msg")
    private String statusMsg;


    public String getGuinnessWitnessId() {
        return guinnessWitnessId;
    }

    public void setGuinnessWitnessId(String guinnessWitnessId) {
        this.guinnessWitnessId = guinnessWitnessId;
    }

    public String getVkid() {
        return vkid;
    }

    public void setVkid(String vkid) {
        this.vkid = vkid;
    }

    public String getWitnessTitle() {
        return witnessTitle;
    }

    public void setWitnessTitle(String witnessTitle) {
        this.witnessTitle = witnessTitle;
    }

    public String getWitnessFName() {
        return witnessFName;
    }

    public void setWitnessFName(String witnessFName) {
        this.witnessFName = witnessFName;
    }

    public String getWitnessMName() {
        return witnessMName;
    }

    public void setWitnessMName(String witnessMName) {
        this.witnessMName = witnessMName;
    }

    public String getWitnessLName() {
        return witnessLName;
    }

    public void setWitnessLName(String witnessLName) {
        this.witnessLName = witnessLName;
    }

    public String getWitnessAddressId() {
        return witnessAddressId;
    }

    public void setWitnessAddressId(String witnessAddressId) {
        this.witnessAddressId = witnessAddressId;
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

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getWitnessOrganizationName() {
        return witnessOrganizationName;
    }

    public void setWitnessOrganizationName(String witnessOrganizationName) {
        this.witnessOrganizationName = witnessOrganizationName;
    }

    public String getWitnessNationality() {
        return witnessNationality;
    }

    public void setWitnessNationality(String witnessNationality) {
        this.witnessNationality = witnessNationality;
    }

    public String getWitnessMobileNumber() {
        return witnessMobileNumber;
    }

    public void setWitnessMobileNumber(String witnessMobileNumber) {
        this.witnessMobileNumber = witnessMobileNumber;
    }

    public String getWitnessAltMobileNumber() {
        return witnessAltMobileNumber;
    }

    public void setWitnessAltMobileNumber(String witnessAltMobileNumber) {
        this.witnessAltMobileNumber = witnessAltMobileNumber;
    }

    public String getWitnessLandlineNumber() {
        return witnessLandlineNumber;
    }

    public void setWitnessLandlineNumber(String witnessLandlineNumber) {
        this.witnessLandlineNumber = witnessLandlineNumber;
    }

    public String getWitnessEmailId() {
        return witnessEmailId;
    }

    public void setWitnessEmailId(String witnessEmailId) {
        this.witnessEmailId = witnessEmailId;
    }

    public String getWitnessOccupation() {
        return witnessOccupation;
    }

    public void setWitnessOccupation(String witnessOccupation) {
        this.witnessOccupation = witnessOccupation;
    }

    public String getWitnessBusinessCardImageId() {
        return witnessBusinessCardImageId;
    }

    public void setWitnessBusinessCardImageId(String witnessBusinessCardImageId) {
        this.witnessBusinessCardImageId = witnessBusinessCardImageId;
    }

    public String getWitnessBusinessCardImageFileExt() {
        return witnessBusinessCardImageFileExt;
    }

    public void setWitnessBusinessCardImageFileExt(String witnessBusinessCardImageFileExt) {
        this.witnessBusinessCardImageFileExt = witnessBusinessCardImageFileExt;
    }

    public String getWitnessPicId() {
        return witnessPicId;
    }

    public void setWitnessPicId(String witnessPicId) {
        this.witnessPicId = witnessPicId;
    }

    public String getWitnessPicFileExt() {
        return witnessPicFileExt;
    }

    public void setWitnessPicFileExt(String witnessPicFileExt) {
        this.witnessPicFileExt = witnessPicFileExt;
    }

    public String getWitnessBusinessCardImage() {
        return witnessBusinessCardImage;
    }

    public void setWitnessBusinessCardImage(String witnessBusinessCardImage) {
        this.witnessBusinessCardImage = witnessBusinessCardImage;
    }

    public String getWitnessPic() {
        return witnessPic;
    }

    public void setWitnessPic(String witnessPic) {
        this.witnessPic = witnessPic;
    }

    public String getWitnessStatementImage() {
        return witnessStatementImage;
    }

    public void setWitnessStatementImage(String witnessStatementImage) {
        this.witnessStatementImage = witnessStatementImage;
    }

    public String getWitnessStatementImageFileExt() {
        return witnessStatementImageFileExt;
    }

    public void setWitnessStatementImageFileExt(String witnessStatementImageFileExt) {
        this.witnessStatementImageFileExt = witnessStatementImageFileExt;
    }

    public String getStatusWitness() {
        return statusWitness;
    }

    public void setStatusWitness(String statusWitness) {
        this.statusWitness = statusWitness;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
