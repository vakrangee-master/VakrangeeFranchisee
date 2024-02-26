package in.vakrangee.simcarddetail.simcarddetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SimcardDetailsDto implements Serializable {


    @SerializedName("atm_sim_service_provider_id")
    private String atmSimServiceProviderId;


    @SerializedName("atm_sim_number")
    private String atmSimNumber;


    @SerializedName("atm_sim_imsi_number")
    private String atmSimIMSINumber;

    //simcard cover
    @SerializedName("simcard_cover_photo_base64")
    private String atmSimcardCoverPhotoBase64;


    @SerializedName("atm_sim_cover_image_id")
    private String atmSimCoverImageId;

    @SerializedName("simcard_cover_photo_ext")
    private String simcardCoverPhotoExt;

    //simcard photo
    @SerializedName("atm_simcard_sim_photo_base64")
    private String atmSimcardPhotoBase64;

    @SerializedName("atm_sim_image_id")
    private String atmSimImageId;

    @SerializedName("simcard_photo_ext")
    private String simcardPhotoExt;

    //qrcode scan
    @SerializedName("atm_sim_qr_code_detail")
    private String atmSimQrCodeDetail;

    @SerializedName("simcard_qrcode_photo_id")
    private String simcardQRcodePhotoId;

    @SerializedName("IsEditable")
    private String IsEditable;

    @SerializedName("atm_id")
    private String atmId;

    @SerializedName("status")
    private String status;

    @SerializedName("atm_sim_service_detail_id")
    private String atmSimServiceDetailId;

    public String getAtmSimServiceProviderId() {
        return atmSimServiceProviderId;
    }

    public void setAtmSimServiceProviderId(String atmSimServiceProviderId) {
        this.atmSimServiceProviderId = atmSimServiceProviderId;
    }

    public String getAtmSimNumber() {
        return atmSimNumber;
    }

    public void setAtmSimNumber(String atmSimNumber) {
        this.atmSimNumber = atmSimNumber;
    }

    public String getAtmSimIMSINumber() {
        return atmSimIMSINumber;
    }

    public void setAtmSimIMSINumber(String atmSimIMSINumber) {
        this.atmSimIMSINumber = atmSimIMSINumber;
    }

    public String getAtmSimcardCoverPhotoBase64() {
        return atmSimcardCoverPhotoBase64;
    }

    public void setAtmSimcardCoverPhotoBase64(String atmSimcardCoverPhotoBase64) {
        this.atmSimcardCoverPhotoBase64 = atmSimcardCoverPhotoBase64;
    }


    public String getAtmSimcardPhotoBase64() {
        return atmSimcardPhotoBase64;
    }

    public void setAtmSimcardPhotoBase64(String atmSimcardPhotoBase64) {
        this.atmSimcardPhotoBase64 = atmSimcardPhotoBase64;
    }


    public String getAtmSimQrCodeDetail() {
        return atmSimQrCodeDetail;
    }

    public void setAtmSimQrCodeDetail(String atmSimQrCodeDetail) {
        this.atmSimQrCodeDetail = atmSimQrCodeDetail;
    }



    public String getAtmSimCoverImageId() {
        return atmSimCoverImageId;
    }

    public void setAtmSimCoverImageId(String atmSimCoverImageId) {
        this.atmSimCoverImageId = atmSimCoverImageId;
    }

    public String getAtmSimImageId() {
        return atmSimImageId;
    }

    public void setAtmSimImageId(String atmSimImageId) {
        this.atmSimImageId = atmSimImageId;
    }


    public String getSimcardQRcodePhotoId() {
        return simcardQRcodePhotoId;
    }

    public void setSimcardQRcodePhotoId(String simcardQRcodePhotoId) {
        this.simcardQRcodePhotoId = simcardQRcodePhotoId;
    }

    public String getSimcardCoverPhotoExt() {
        return simcardCoverPhotoExt;
    }

    public void setSimcardCoverPhotoExt(String simcardCoverPhotoExt) {
        this.simcardCoverPhotoExt = simcardCoverPhotoExt;
    }

    public String getSimcardPhotoExt() {
        return simcardPhotoExt;
    }

    public void setSimcardPhotoExt(String simcardPhotoExt) {
        this.simcardPhotoExt = simcardPhotoExt;
    }


    public String getIsEditable() {
        return IsEditable;
    }

    public void setIsEditable(String isEditable) {
        IsEditable = isEditable;
    }

    public String getAtmId() {
        return atmId;
    }

    public void setAtmId(String atmId) {
        this.atmId = atmId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAtmSimServiceDetailId() {
        return atmSimServiceDetailId;
    }

    public void setAtmSimServiceDetailId(String atmSimServiceDetailId) {
        this.atmSimServiceDetailId = atmSimServiceDetailId;
    }
}
