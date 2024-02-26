package in.vakrangee.franchisee.documentmanager;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IIBFCertificationDto implements Serializable {

    @SerializedName("bca_code")
    private String bcaCode;

    @SerializedName("membership_number")
    private String membershipNumber;

    @SerializedName("certificate_number")
    private String certificateNumber;

    @SerializedName("date_of_examination")
    private String dateOfExamination;


    @SerializedName("iibf_certificate_scan_copy_id")
    private String iibfCertificateScanId;

    @SerializedName("iibf_certificate_scan_base64")
    private String iibfCertificateScanBase64;

    @SerializedName("iibf_certificate_img_ext")
    private String iibfCertificateScanExt;

    @SerializedName("IsEditable")
    private String IsEditable;

    private transient Bitmap bitmap;
    private transient android.net.Uri Uri;

    private transient boolean IsChangedPhoto;

    public String getBcaCode() {
        return bcaCode;
    }

    public void setBcaCode(String bcaCode) {
        this.bcaCode = bcaCode;
    }

    public String getMembershipNumber() {
        return membershipNumber;
    }

    public void setMembershipNumber(String membershipNumber) {
        this.membershipNumber = membershipNumber;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getDateOfExamination() {
        return dateOfExamination;
    }

    public void setDateOfExamination(String dateOfExamination) {
        this.dateOfExamination = dateOfExamination;
    }

    public String getIibfCertificateScanId() {
        return iibfCertificateScanId;
    }

    public void setIibfCertificateScanId(String iibfCertificateScanId) {
        this.iibfCertificateScanId = iibfCertificateScanId;
    }

    public String getIibfCertificateScanBase64() {
        return iibfCertificateScanBase64;
    }

    public void setIibfCertificateScanBase64(String iibfCertificateScanBase64) {
        this.iibfCertificateScanBase64 = iibfCertificateScanBase64;
    }

    public String getIibfCertificateScanExt() {
        return iibfCertificateScanExt;
    }

    public void setIibfCertificateScanExt(String iibfCertificateScanExt) {
        this.iibfCertificateScanExt = iibfCertificateScanExt;
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
}
