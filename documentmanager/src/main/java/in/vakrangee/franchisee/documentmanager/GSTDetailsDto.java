package in.vakrangee.franchisee.documentmanager;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GSTDetailsDto implements Serializable {

    @SerializedName("name_of_entity_gstin")
    private String nameOfGSTINEntity;

    @SerializedName("gstin_number")
    private String gstInNumber;

    @SerializedName("gstin_issue_date")
    private String gstinIssueDate;

    @SerializedName("gstin_address_line1")
    private String gstInAddressLine1;

    @SerializedName("gstin_address_line2")
    private String gstInAddressLine2;

    @SerializedName("landmark")
    private String landmark;

    @SerializedName("area")
    private String area;

    @SerializedName("locality")
    private String locality;

    @SerializedName("state")
    private String state;

    @SerializedName("district")
    private String district;

    @SerializedName("vtc")
    private String vtc;

    @SerializedName("pincode")
    private String pincode;

    @SerializedName("gstin_certificate_img_id")
    private String gstinCertificateImgId;

    @SerializedName("gstin_certificate_img_base64")
    private String gstinCertificateImgBase64;

    @SerializedName("gstin_certificate_img_ext")
    private String gstinCertificateImgExt;

    @SerializedName("IsEditable")
    private String IsEditable;

    @SerializedName("is_gstin_applied")
    private String appliedForGSTIN;

    @SerializedName("gstin_trn_number")
    private String trnNumber;

    @SerializedName("gstin_ack_receipt_img_id")
    private String gstinAckReceiptImgId;

    @SerializedName("gstin_ack_receipt_img_base64")
    private String gstinAckReceiptImgBase64;

    @SerializedName("gstin_ack_receipt_img_ext")
    private String gstinAckReceiptImgExt;

    @SerializedName("gstin_applied_date")
    private String gstApplicationDate;

    @SerializedName("gstin_expected_date")
    private String expectedDateOfGSTApplication;

    private transient Bitmap bitmap, bitmapReceipt;
    private transient android.net.Uri Uri, receiptUri;
    private transient String Name, receiptName;
    private transient boolean IsChangedPhoto, IsReceiptImgChanged;

    public String getNameOfGSTINEntity() {
        return nameOfGSTINEntity;
    }

    public void setNameOfGSTINEntity(String nameOfGSTINEntity) {
        this.nameOfGSTINEntity = nameOfGSTINEntity;
    }

    public String getGstInNumber() {
        return gstInNumber;
    }

    public void setGstInNumber(String gstInNumber) {
        this.gstInNumber = gstInNumber;
    }

    public String getGstInAddressLine1() {
        return gstInAddressLine1;
    }

    public void setGstInAddressLine1(String gstInAddressLine1) {
        this.gstInAddressLine1 = gstInAddressLine1;
    }

    public String getGstInAddressLine2() {
        return gstInAddressLine2;
    }

    public void setGstInAddressLine2(String gstInAddressLine2) {
        this.gstInAddressLine2 = gstInAddressLine2;
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

    public String getGstinCertificateImgId() {
        return gstinCertificateImgId;
    }

    public void setGstinCertificateImgId(String gstinCertificateImgId) {
        this.gstinCertificateImgId = gstinCertificateImgId;
    }

    public String getGstinCertificateImgBase64() {
        return gstinCertificateImgBase64;
    }

    public void setGstinCertificateImgBase64(String gstinCertificateImgBase64) {
        this.gstinCertificateImgBase64 = gstinCertificateImgBase64;
    }

    public String getGstinCertificateImgExt() {
        return gstinCertificateImgExt;
    }

    public void setGstinCertificateImgExt(String gstinCertificateImgExt) {
        this.gstinCertificateImgExt = gstinCertificateImgExt;
    }

    public String getIsEditable() {
        return IsEditable;
    }

    public void setIsEditable(String isEditable) {
        IsEditable = isEditable;
    }

    public String getAppliedForGSTIN() {
        return appliedForGSTIN;
    }

    public void setAppliedForGSTIN(String appliedForGSTIN) {
        this.appliedForGSTIN = appliedForGSTIN;
    }

    public String getTrnNumber() {
        return trnNumber;
    }

    public void setTrnNumber(String trnNumber) {
        this.trnNumber = trnNumber;
    }

    public String getGstinAckReceiptImgId() {
        return gstinAckReceiptImgId;
    }

    public void setGstinAckReceiptImgId(String gstinAckReceiptImgId) {
        this.gstinAckReceiptImgId = gstinAckReceiptImgId;
    }

    public String getGstinAckReceiptImgBase64() {
        return gstinAckReceiptImgBase64;
    }

    public void setGstinAckReceiptImgBase64(String gstinAckReceiptImgBase64) {
        this.gstinAckReceiptImgBase64 = gstinAckReceiptImgBase64;
    }

    public String getGstinAckReceiptImgExt() {
        return gstinAckReceiptImgExt;
    }

    public void setGstinAckReceiptImgExt(String gstinAckReceiptImgExt) {
        this.gstinAckReceiptImgExt = gstinAckReceiptImgExt;
    }

    public String getGstApplicationDate() {
        return gstApplicationDate;
    }

    public void setGstApplicationDate(String gstApplicationDate) {
        this.gstApplicationDate = gstApplicationDate;
    }

    public String getExpectedDateOfGSTApplication() {
        return expectedDateOfGSTApplication;
    }

    public void setExpectedDateOfGSTApplication(String expectedDateOfGSTApplication) {
        this.expectedDateOfGSTApplication = expectedDateOfGSTApplication;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmapReceipt() {
        return bitmapReceipt;
    }

    public void setBitmapReceipt(Bitmap bitmapReceipt) {
        this.bitmapReceipt = bitmapReceipt;
    }

    public android.net.Uri getUri() {
        return Uri;
    }

    public void setUri(android.net.Uri uri) {
        Uri = uri;
    }

    public android.net.Uri getReceiptUri() {
        return receiptUri;
    }

    public void setReceiptUri(android.net.Uri receiptUri) {
        this.receiptUri = receiptUri;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    public boolean isChangedPhoto() {
        return IsChangedPhoto;
    }

    public void setChangedPhoto(boolean changedPhoto) {
        IsChangedPhoto = changedPhoto;
    }

    public boolean isReceiptImgChanged() {
        return IsReceiptImgChanged;
    }

    public void setReceiptImgChanged(boolean receiptImgChanged) {
        IsReceiptImgChanged = receiptImgChanged;
    }

    public String getGstinIssueDate() {
        return gstinIssueDate;
    }

    public void setGstinIssueDate(String gstinIssueDate) {
        this.gstinIssueDate = gstinIssueDate;
    }
}
