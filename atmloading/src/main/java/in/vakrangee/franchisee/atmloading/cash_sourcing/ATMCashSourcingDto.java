package in.vakrangee.franchisee.atmloading.cash_sourcing;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ATMCashSourcingDto implements Serializable {

    @SerializedName("cash_sourcing_id")
    private String cashSourcingId;

    @SerializedName("min_source_date")
    private String minSourceDate;

    @SerializedName("source_date")
    private String sourceDate;

    @SerializedName("bank_reference_no")
    private String bankReferenceNo;

    @SerializedName("atm_id")
    private String atmId;

    @SerializedName("d_50")
    private String d50NoteCount;

    @SerializedName("d_100")
    private String d100NoteCount;

    @SerializedName("d_200")
    private String d200NoteCount;

    @SerializedName("d_500")
    private String d500NoteCount;

    @SerializedName("d_2000")
    private String d2000NoteCount;

    @SerializedName("amount")
    private String amount;

    @SerializedName("loaded_amount")
    private String loadedAmount;

    @SerializedName("balance_amount")
    private String balanceAmount;

    @SerializedName("status")
    private String status;

    /**
     * 1- Uploaded
     * 0 - Cancelled
     */
    @SerializedName("status_code")
    private String statusCode;

    @SerializedName("cash_sourcing_scan_copy_id")
    private String cashSourcingScanCopyId;

    @SerializedName("cash_sourcing_ext")
    private String cashSourcingExt;

    @SerializedName("cash_sourcing_base64")
    private String cashSourcingBase64;

    @SerializedName("IsEditable")
    private String IsEditable;

    private transient Bitmap bitmap;
    private transient android.net.Uri Uri;
    private transient boolean IsChangedPhoto;

    public String getSourceDate() {
        return sourceDate;
    }

    public void setSourceDate(String sourceDate) {
        this.sourceDate = sourceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCashSourcingId() {
        return cashSourcingId;
    }

    public void setCashSourcingId(String cashSourcingId) {
        this.cashSourcingId = cashSourcingId;
    }

    public String getCashSourcingScanCopyId() {
        return cashSourcingScanCopyId;
    }

    public void setCashSourcingScanCopyId(String cashSourcingScanCopyId) {
        this.cashSourcingScanCopyId = cashSourcingScanCopyId;
    }

    public String getCashSourcingExt() {
        return cashSourcingExt;
    }

    public void setCashSourcingExt(String cashSourcingExt) {
        this.cashSourcingExt = cashSourcingExt;
    }

    public String getCashSourcingBase64() {
        return cashSourcingBase64;
    }

    public void setCashSourcingBase64(String cashSourcingBase64) {
        this.cashSourcingBase64 = cashSourcingBase64;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getBankReferenceNo() {
        return bankReferenceNo;
    }

    public void setBankReferenceNo(String bankReferenceNo) {
        this.bankReferenceNo = bankReferenceNo;
    }

    public String getAtmId() {
        return atmId;
    }

    public void setAtmId(String atmId) {
        this.atmId = atmId;
    }

    public String getD50NoteCount() {
        return d50NoteCount;
    }

    public void setD50NoteCount(String d50NoteCount) {
        this.d50NoteCount = d50NoteCount;
    }

    public String getD100NoteCount() {
        return d100NoteCount;
    }

    public void setD100NoteCount(String d100NoteCount) {
        this.d100NoteCount = d100NoteCount;
    }

    public String getD200NoteCount() {
        return d200NoteCount;
    }

    public void setD200NoteCount(String d200NoteCount) {
        this.d200NoteCount = d200NoteCount;
    }

    public String getD500NoteCount() {
        return d500NoteCount;
    }

    public void setD500NoteCount(String d500NoteCount) {
        this.d500NoteCount = d500NoteCount;
    }

    public String getD2000NoteCount() {
        return d2000NoteCount;
    }

    public void setD2000NoteCount(String d2000NoteCount) {
        this.d2000NoteCount = d2000NoteCount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMinSourceDate() {
        return minSourceDate;
    }

    public void setMinSourceDate(String minSourceDate) {
        this.minSourceDate = minSourceDate;
    }

    public String getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getLoadedAmount() {
        return loadedAmount;
    }

    public void setLoadedAmount(String loadedAmount) {
        this.loadedAmount = loadedAmount;
    }
}
