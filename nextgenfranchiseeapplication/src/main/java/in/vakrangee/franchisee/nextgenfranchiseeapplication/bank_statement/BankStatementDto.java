package in.vakrangee.franchisee.nextgenfranchiseeapplication.bank_statement;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BankStatementDto implements Serializable {

    @SerializedName("fa_bank_id")
    private String faBankId;

    @SerializedName("title")
    private String title;

    @SerializedName("bank_stmt_min_start_date")
    private String bankStmtMinStartDate;

    @SerializedName("bank_stmt_start_date")
    private String bankStmtStartDate;

    @SerializedName("bank_stmt_end_date")
    private String bankStmtEndDate;

    @SerializedName("bank_stmt_password")
    private String bankStmtPassword;

    @SerializedName("bank_stmt_image_id")
    private String bankStmtImageId;

    @SerializedName("bank_stmt_image_ext")
    private String bankStmtImageExt;

    @SerializedName("bank_stmt_image_base64")
    private String bankStmtImageBase64;

    private transient Bitmap bitmap;
    private transient android.net.Uri Uri;
    private transient boolean IsChangedPhoto;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFaBankId() {
        return faBankId;
    }

    public void setFaBankId(String faBankId) {
        this.faBankId = faBankId;
    }

    public String getBankStmtStartDate() {
        return bankStmtStartDate;
    }

    public void setBankStmtStartDate(String bankStmtStartDate) {
        this.bankStmtStartDate = bankStmtStartDate;
    }

    public String getBankStmtEndDate() {
        return bankStmtEndDate;
    }

    public void setBankStmtEndDate(String bankStmtEndDate) {
        this.bankStmtEndDate = bankStmtEndDate;
    }

    public String getBankStmtPassword() {
        return bankStmtPassword;
    }

    public void setBankStmtPassword(String bankStmtPassword) {
        this.bankStmtPassword = bankStmtPassword;
    }

    public String getBankStmtImageId() {
        return bankStmtImageId;
    }

    public void setBankStmtImageId(String bankStmtImageId) {
        this.bankStmtImageId = bankStmtImageId;
    }

    public String getBankStmtImageExt() {
        return bankStmtImageExt;
    }

    public void setBankStmtImageExt(String bankStmtImageExt) {
        this.bankStmtImageExt = bankStmtImageExt;
    }

    public String getBankStmtImageBase64() {
        return bankStmtImageBase64;
    }

    public void setBankStmtImageBase64(String bankStmtImageBase64) {
        this.bankStmtImageBase64 = bankStmtImageBase64;
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

    public String getBankStmtMinStartDate() {
        return bankStmtMinStartDate;
    }

    public void setBankStmtMinStartDate(String bankStmtMinStartDate) {
        this.bankStmtMinStartDate = bankStmtMinStartDate;
    }
}
