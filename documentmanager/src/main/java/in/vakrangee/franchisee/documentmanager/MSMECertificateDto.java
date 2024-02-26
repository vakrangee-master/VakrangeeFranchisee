package in.vakrangee.franchisee.documentmanager;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MSMECertificateDto implements Serializable {

    @SerializedName("msme_certificate_no")
    private String msme_certificate_no;

    @SerializedName("msme_issuing_authority")
    private String msme_issuing_authority;

    @SerializedName("state_id")
    private String state_id;

    @SerializedName("district_id")
    private String district_id;

    @SerializedName("name_on_msme_certificate")
    private String nameOnMSMECertificate;

    @SerializedName("msme_issuing_date")
    private String msmeIssuingDate;

    @SerializedName("msme_valid_from_date")
    private String msmeValidFromDate;

    @SerializedName("msme_expiry_date")
    private String msmeExpiryDate;

    //--START Image
    @SerializedName("msme_certificate_scan_copy_id")
    private String msmeCertificateScanId;

    @SerializedName("msme_certificate_scan_base64")
    private String msmeCertificateScanBase64;

    @SerializedName("msme_certificate_img_ext")
    private String msmeCertificateScanExt;
    //-- END Image

    @SerializedName("IsEditable")
    private String IsEditable;

    private transient Bitmap bitmap;
    private transient android.net.Uri Uri;

    private transient boolean IsChangedPhoto;

    public String getNameOnMSMECertificate() {
        return nameOnMSMECertificate;
    }

    public void setNameOnMSMECertificate(String nameOnMSMECertificate) {
        this.nameOnMSMECertificate = nameOnMSMECertificate;
    }

    public String getMsmeIssuingDate() {
        return msmeIssuingDate;
    }

    public void setMsmeIssuingDate(String msmeIssuingDate) {
        this.msmeIssuingDate = msmeIssuingDate;
    }

    public String getMsmeValidFromDate() {
        return msmeValidFromDate;
    }

    public void setMsmeValidFromDate(String msmeValidFromDate) {
        this.msmeValidFromDate = msmeValidFromDate;
    }

    public String getMsmeExpiryDate() {
        return msmeExpiryDate;
    }

    public void setMsmeExpiryDate(String msmeExpiryDate) {
        this.msmeExpiryDate = msmeExpiryDate;
    }

    public String getMsmeCertificateScanId() {
        return msmeCertificateScanId;
    }

    public void setMsmeCertificateScanId(String msmeCertificateScanId) {
        this.msmeCertificateScanId = msmeCertificateScanId;
    }

    public String getMsmeCertificateScanBase64() {
        return msmeCertificateScanBase64;
    }

    public void setMsmeCertificateScanBase64(String msmeCertificateScanBase64) {
        this.msmeCertificateScanBase64 = msmeCertificateScanBase64;
    }

    public String getMsmeCertificateScanExt() {
        return msmeCertificateScanExt;
    }

    public void setMsmeCertificateScanExt(String msmeCertificateScanExt) {
        this.msmeCertificateScanExt = msmeCertificateScanExt;
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

    public String getMsme_certificate_no() {
        return msme_certificate_no;
    }

    public void setMsme_certificate_no(String msme_certificate_no) {
        this.msme_certificate_no = msme_certificate_no;
    }

    public String getMsme_issuing_authority() {
        return msme_issuing_authority;
    }

    public void setMsme_issuing_authority(String msme_issuing_authority) {
        this.msme_issuing_authority = msme_issuing_authority;
    }

    public String getState() {
        return state_id;
    }

    public void setState(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict() {
        return district_id;
    }

    public void setDistrict(String district_id) {
        this.district_id = district_id;
    }
}
