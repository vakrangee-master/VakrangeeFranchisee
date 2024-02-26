package in.vakrangee.core.gstdetails;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GSTINDTO {

    @SerializedName("gstEntityName")
    @Expose
    private String gstEntityName;
    @SerializedName("gstNumber")
    @Expose
    private String gstNumber;
    @SerializedName("gstImageId")
    @Expose
    private String gstImageId;
    /*   @SerializedName("gstAddress")
       @Expose
       private String gstAddress;
   */
    @SerializedName("gstImage")
    @Expose
    private String gstImage;
    @SerializedName("gstin_certificate_img_ext")
    private String gstImageExt;

    @SerializedName("gstAddressLine1")
    @Expose
    private String gstAddressLine1;
    @SerializedName("gstAddressLine2")
    @Expose
    private String gstAddressLine2;
    @SerializedName("gstLandmark")
    @Expose
    private String gstLandmark;
    @SerializedName("gstLocality")
    @Expose
    private String gstLocality;
    @SerializedName("gstArea")
    @Expose
    private String gstArea;
    @SerializedName("gstVtc")
    @Expose
    private String gstVtc;
    @SerializedName("gstDistrict")
    @Expose
    private String gstDistrict;
    @SerializedName("gstState")
    @Expose
    private String gstState;
    @SerializedName("gstPinCode")
    @Expose
    private String gstPinCode;

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

    public GSTINDTO() {
    }

    public String getGstEntityName() {
        return gstEntityName;
    }

    public void setGstEntityName(String gstEntityName) {
        this.gstEntityName = gstEntityName;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getGstImageId() {
        return gstImageId;
    }

    public void setGstImageId(String gstImageId) {
        this.gstImageId = gstImageId;
    }

    public String getGstImage() {
        return gstImage;
    }

    public void setGstImage(String gstImage) {
        this.gstImage = gstImage;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getGstAddressLine1() {
        return gstAddressLine1;
    }

    public void setGstAddressLine1(String gstAddressLine1) {
        this.gstAddressLine1 = gstAddressLine1;
    }

    public String getGstAddressLine2() {
        return gstAddressLine2;
    }

    public void setGstAddressLine2(String gstAddressLine2) {
        this.gstAddressLine2 = gstAddressLine2;
    }

    public String getGstLandmark() {
        return gstLandmark;
    }

    public void setGstLandmark(String gstLandmark) {
        this.gstLandmark = gstLandmark;
    }

    public String getGstLocality() {
        return gstLocality;
    }

    public void setGstLocality(String gstLocality) {
        this.gstLocality = gstLocality;
    }

    public String getGstArea() {
        return gstArea;
    }

    public void setGstArea(String gstArea) {
        this.gstArea = gstArea;
    }

    public String getGstVtc() {
        return gstVtc;
    }

    public void setGstVtc(String gstVtc) {
        this.gstVtc = gstVtc;
    }

    public String getGstDistrict() {
        return gstDistrict;
    }

    public void setGstDistrict(String gstDistrict) {
        this.gstDistrict = gstDistrict;
    }

    public String getGstState() {
        return gstState;
    }

    public void setGstState(String gstState) {
        this.gstState = gstState;
    }

    public String getGstPinCode() {
        return gstPinCode;
    }

    public void setGstPinCode(String gstPinCode) {
        this.gstPinCode = gstPinCode;
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

    public Bitmap getBitmapReceipt() {
        return bitmapReceipt;
    }

    public void setBitmapReceipt(Bitmap bitmapReceipt) {
        this.bitmapReceipt = bitmapReceipt;
    }

    public android.net.Uri getReceiptUri() {
        return receiptUri;
    }

    public void setReceiptUri(android.net.Uri receiptUri) {
        this.receiptUri = receiptUri;
    }

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    public boolean isReceiptImgChanged() {
        return IsReceiptImgChanged;
    }

    public void setReceiptImgChanged(boolean receiptImgChanged) {
        IsReceiptImgChanged = receiptImgChanged;
    }

    public String getIsEditable() {
        return IsEditable;
    }

    public void setIsEditable(String isEditable) {
        IsEditable = isEditable;
    }

    public String getGstImageExt() {
        return gstImageExt;
    }

    public void setGstImageExt(String gstImageExt) {
        this.gstImageExt = gstImageExt;
    }
}
