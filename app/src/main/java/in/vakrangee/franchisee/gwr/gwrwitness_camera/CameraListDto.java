package in.vakrangee.franchisee.gwr.gwrwitness_camera;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CameraListDto implements Serializable {

    @SerializedName("guinness_cameraman_id")
    @Expose
    private String guinnessCameramanId;
    @SerializedName("vkid")
    @Expose
    private String vkid;
    @SerializedName("cameraman_title")
    @Expose
    private String cameramanTitle;
    @SerializedName("cameraman_f_name")
    @Expose
    private String cameramanFName;
    @SerializedName("cameraman_m_name")
    @Expose
    private String cameramanMName;
    @SerializedName("cameraman_l_name")
    @Expose
    private String cameramanLName;
    @SerializedName("cameraman_address_id")
    @Expose
    private String cameramanAddressId;
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
    @SerializedName("cameraman_organization_name")
    @Expose
    private String cameramanOrganizationName;
    @SerializedName("cameraman_nationality")
    @Expose
    private String cameramanNationality;
    @SerializedName("cameraman_mobile_number")
    @Expose
    private String cameramanMobileNumber;
    @SerializedName("cameraman_alt_mobile_number")
    @Expose
    private String cameramanAltMobileNumber;
    @SerializedName("cameraman_landline_number")
    @Expose
    private String cameramanLandlineNumber;
    @SerializedName("cameraman_email_id")
    @Expose
    private String cameramanEmailId;
    @SerializedName("cameraman_pic_id")
    @Expose
    private String cameramanPicId;
    @SerializedName("cameraman_pic_file_ext")
    @Expose
    private String cameramanPicFileExt;
    @SerializedName("cameraman_business_card_image_id")
    @Expose
    private String cameramanBusinessCardImageId;
    @SerializedName("cameraman_business_card_image_file_ext")
    @Expose
    private String cameramanBusinessCardImageFileExt;
    @SerializedName("make")
    @Expose
    private String make;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("is_date_time_stamping")
    @Expose
    private Integer is_datetime_stamping;
    @SerializedName("cameraman_business_card_image")
    @Expose
    private String cameramanBusinessCardImage;
    @SerializedName("cameraman_pic")
    @Expose
    private String cameramanPic;
    @SerializedName("status")
    private String statusCameraman;

    @SerializedName("status_msg")
    private String statusMsg;

    public String getGuinnessCameramanId() {
        return guinnessCameramanId;
    }

    public void setGuinnessCameramanId(String guinnessCameramanId) {
        this.guinnessCameramanId = guinnessCameramanId;
    }

    public String getVkid() {
        return vkid;
    }

    public void setVkid(String vkid) {
        this.vkid = vkid;
    }

    public String getCameramanTitle() {
        return cameramanTitle;
    }

    public void setCameramanTitle(String cameramanTitle) {
        this.cameramanTitle = cameramanTitle;
    }

    public String getCameramanFName() {
        return cameramanFName;
    }

    public void setCameramanFName(String cameramanFName) {
        this.cameramanFName = cameramanFName;
    }

    public String getCameramanMName() {
        return cameramanMName;
    }

    public void setCameramanMName(String cameramanMName) {
        this.cameramanMName = cameramanMName;
    }

    public String getCameramanLName() {
        return cameramanLName;
    }

    public void setCameramanLName(String cameramanLName) {
        this.cameramanLName = cameramanLName;
    }

    public String getCameramanAddressId() {
        return cameramanAddressId;
    }

    public void setCameramanAddressId(String cameramanAddressId) {
        this.cameramanAddressId = cameramanAddressId;
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

    public String getCameramanOrganizationName() {
        return cameramanOrganizationName;
    }

    public void setCameramanOrganizationName(String cameramanOrganizationName) {
        this.cameramanOrganizationName = cameramanOrganizationName;
    }

    public String getCameramanNationality() {
        return cameramanNationality;
    }

    public void setCameramanNationality(String cameramanNationality) {
        this.cameramanNationality = cameramanNationality;
    }

    public String getCameramanMobileNumber() {
        return cameramanMobileNumber;
    }

    public void setCameramanMobileNumber(String cameramanMobileNumber) {
        this.cameramanMobileNumber = cameramanMobileNumber;
    }

    public String getCameramanAltMobileNumber() {
        return cameramanAltMobileNumber;
    }

    public void setCameramanAltMobileNumber(String cameramanAltMobileNumber) {
        this.cameramanAltMobileNumber = cameramanAltMobileNumber;
    }

    public String getCameramanLandlineNumber() {
        return cameramanLandlineNumber;
    }

    public void setCameramanLandlineNumber(String cameramanLandlineNumber) {
        this.cameramanLandlineNumber = cameramanLandlineNumber;
    }

    public String getCameramanEmailId() {
        return cameramanEmailId;
    }

    public void setCameramanEmailId(String cameramanEmailId) {
        this.cameramanEmailId = cameramanEmailId;
    }

    public String getCameramanPicId() {
        return cameramanPicId;
    }

    public void setCameramanPicId(String cameramanPicId) {
        this.cameramanPicId = cameramanPicId;
    }


    public String getCameramanBusinessCardImageId() {
        return cameramanBusinessCardImageId;
    }

    public void setCameramanBusinessCardImageId(String cameramanBusinessCardImageId) {
        this.cameramanBusinessCardImageId = cameramanBusinessCardImageId;
    }


    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setIs_datetime_stamping(Integer is_datetime_stamping) {
        this.is_datetime_stamping = is_datetime_stamping;
    }

    public Integer isIs_datetime_stamping() {
        return is_datetime_stamping;
    }

    public String getCameramanBussinessCardImageFileExt() {
        return cameramanBusinessCardImageFileExt;
    }

    public void setCameramanBussinessCardImageFileExt(String cameramanBussinessCardImageFileExt) {
        this.cameramanBusinessCardImageFileExt = cameramanBussinessCardImageFileExt;
    }

    public String getCameramanPicFileExt() {
        return cameramanPicFileExt;
    }

    public void setCameramanPicFileExt(String cameramanPicFileExt) {
        this.cameramanPicFileExt = cameramanPicFileExt;
    }

    public String getCameramanBusinessCardImage() {
        return cameramanBusinessCardImage;
    }

    public void setCameramanBusinessCardImage(String cameramanBusinessCardImage) {
        this.cameramanBusinessCardImage = cameramanBusinessCardImage;
    }

    public String getCameramanPic() {
        return cameramanPic;
    }

    public void setCameramanPic(String cameramanPic) {
        this.cameramanPic = cameramanPic;
    }

    public String getStatusCameraman() {
        return statusCameraman;
    }

    public void setStatusCameraman(String statusCameraman) {
        this.statusCameraman = statusCameraman;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
