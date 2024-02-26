package in.vakrangee.franchisee.loandocument.model;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UploadAcknowledgementDto implements Serializable {

    @SerializedName("branch_name")
    private String branchName;

    @SerializedName("franchisee_loan_request_id")
    private String franchiseeLoanRequestId;

    @SerializedName("loan_submission_date")
    private String loanSubmissionDate;

    @SerializedName("acknowledgement_type")
    private String acknowledgementType;

    @SerializedName("upload_ack_date")
    private String uploadAckDate;

    @SerializedName("upload_ack_scan_copy_id")
    private String uploadAckScanId;

    @SerializedName("upload_ack_scan_base64")
    private String uploadAckScanBase64;

    @SerializedName("upload_ack_img_ext")
    private String uploadAckScanExt;

    @SerializedName("is_emailed")
    private String isEmailed;

    @SerializedName("emailed_date")
    private String emailedDate;

    @SerializedName("min_emailed_date")
    private String minEmailedDate;

    @SerializedName("IsEditable")
    private String IsEditable;

    private transient Bitmap bitmap;
    private transient android.net.Uri Uri;
    private transient boolean IsChangedPhoto;

    public String getUploadAckDate() {
        return uploadAckDate;
    }

    public void setUploadAckDate(String uploadAckDate) {
        this.uploadAckDate = uploadAckDate;
    }

    public String getUploadAckScanId() {
        return uploadAckScanId;
    }

    public void setUploadAckScanId(String uploadAckScanId) {
        this.uploadAckScanId = uploadAckScanId;
    }

    public String getUploadAckScanBase64() {
        return uploadAckScanBase64;
    }

    public void setUploadAckScanBase64(String uploadAckScanBase64) {
        this.uploadAckScanBase64 = uploadAckScanBase64;
    }

    public String getUploadAckScanExt() {
        return uploadAckScanExt;
    }

    public void setUploadAckScanExt(String uploadAckScanExt) {
        this.uploadAckScanExt = uploadAckScanExt;
    }

    public String getIsEditable() {
        return IsEditable;
    }

    public void setIsEditable(String isEditable) {
        IsEditable = isEditable;
    }

    public boolean isChangedPhoto() {
        return IsChangedPhoto;
    }

    public void setChangedPhoto(boolean changedPhoto) {
        IsChangedPhoto = changedPhoto;
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

    public String getLoanSubmissionDate() {
        return loanSubmissionDate;
    }

    public void setLoanSubmissionDate(String loanSubmissionDate) {
        this.loanSubmissionDate = loanSubmissionDate;
    }

    public String getFranchiseeLoanRequestId() {
        return franchiseeLoanRequestId;
    }

    public void setFranchiseeLoanRequestId(String franchiseeLoanRequestId) {
        this.franchiseeLoanRequestId = franchiseeLoanRequestId;
    }

    public String getAcknowledgementType() {
        return acknowledgementType;
    }

    public void setAcknowledgementType(String acknowledgementType) {
        this.acknowledgementType = acknowledgementType;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getIsEmailed() {
        return isEmailed;
    }

    public void setIsEmailed(String isEmailed) {
        this.isEmailed = isEmailed;
    }

    public String getEmailedDate() {
        return emailedDate;
    }

    public void setEmailedDate(String emailedDate) {
        this.emailedDate = emailedDate;
    }

    public String getMinEmailedDate() {
        return minEmailedDate;
    }

    public void setMinEmailedDate(String minEmailedDate) {
        this.minEmailedDate = minEmailedDate;
    }
}
