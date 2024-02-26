package in.vakrangee.franchisee.loandocument.model;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UploadSanctionDto implements Serializable {

    @SerializedName("min_loan_sanction_letter_date")
    private String minLoanSanctionLetterDate;

    @SerializedName("loan_sanction_letter_date")
    private String loanSanctionLetterDate;

    @SerializedName("max_term_loan_sanction_amount")
    private String maxTermLoanSanctionAmount;

    @SerializedName("max_working_capital_sanction_amount")
    private String maxWorkingCapitalSanctionAmount;

    @SerializedName("term_loan_sanction_amount")
    private String termLoanSanctionAmount;

    @SerializedName("working_capital_sanction_amount")
    private String workingCapitalSanctionAmount;

    @SerializedName("franchisee_loan_request_id")
    private String franchiseeLoanRequestId;

    @SerializedName("upload_sanction_scan_copy_id")
    private String uploadSanctionScanId;

    @SerializedName("upload_sanction_scan_base64")
    private String uploadSanctionScanBase64;

    @SerializedName("upload_sanction_img_ext")
    private String uploadSanctionScanExt;

    @SerializedName("IsEditable")
    private String IsEditable;

    private transient Bitmap bitmap;
    private transient android.net.Uri Uri;
    private transient boolean IsChangedPhoto;

    public String getFranchiseeLoanRequestId() {
        return franchiseeLoanRequestId;
    }

    public void setFranchiseeLoanRequestId(String franchiseeLoanRequestId) {
        this.franchiseeLoanRequestId = franchiseeLoanRequestId;
    }

    public String getUploadSanctionScanId() {
        return uploadSanctionScanId;
    }

    public void setUploadSanctionScanId(String uploadSanctionScanId) {
        this.uploadSanctionScanId = uploadSanctionScanId;
    }

    public String getUploadSanctionScanBase64() {
        return uploadSanctionScanBase64;
    }

    public void setUploadSanctionScanBase64(String uploadSanctionScanBase64) {
        this.uploadSanctionScanBase64 = uploadSanctionScanBase64;
    }

    public String getUploadSanctionScanExt() {
        return uploadSanctionScanExt;
    }

    public void setUploadSanctionScanExt(String uploadSanctionScanExt) {
        this.uploadSanctionScanExt = uploadSanctionScanExt;
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

    public String getMinLoanSanctionLetterDate() {
        return minLoanSanctionLetterDate;
    }

    public void setMinLoanSanctionLetterDate(String minLoanSanctionLetterDate) {
        this.minLoanSanctionLetterDate = minLoanSanctionLetterDate;
    }

    public String getLoanSanctionLetterDate() {
        return loanSanctionLetterDate;
    }

    public void setLoanSanctionLetterDate(String loanSanctionLetterDate) {
        this.loanSanctionLetterDate = loanSanctionLetterDate;
    }

    public String getTermLoanSanctionAmount() {
        return termLoanSanctionAmount;
    }

    public void setTermLoanSanctionAmount(String termLoanSanctionAmount) {
        this.termLoanSanctionAmount = termLoanSanctionAmount;
    }

    public String getWorkingCapitalSanctionAmount() {
        return workingCapitalSanctionAmount;
    }

    public void setWorkingCapitalSanctionAmount(String workingCapitalSanctionAmount) {
        this.workingCapitalSanctionAmount = workingCapitalSanctionAmount;
    }

    public String getMaxTermLoanSanctionAmount() {
        return maxTermLoanSanctionAmount;
    }

    public void setMaxTermLoanSanctionAmount(String maxTermLoanSanctionAmount) {
        this.maxTermLoanSanctionAmount = maxTermLoanSanctionAmount;
    }

    public String getMaxWorkingCapitalSanctionAmount() {
        return maxWorkingCapitalSanctionAmount;
    }

    public void setMaxWorkingCapitalSanctionAmount(String maxWorkingCapitalSanctionAmount) {
        this.maxWorkingCapitalSanctionAmount = maxWorkingCapitalSanctionAmount;
    }
}
