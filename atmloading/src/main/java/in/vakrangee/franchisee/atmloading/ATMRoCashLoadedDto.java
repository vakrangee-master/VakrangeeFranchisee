package in.vakrangee.franchisee.atmloading;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ATMRoCashLoadedDto implements Serializable {
    @SerializedName("atm_ro_cash_receipt_id")
    private String atmRoCashReceiptId;
    @SerializedName("atm_ro_cash_receipt_image")
    private String atmRoCashReceiptImage;
    @SerializedName("St")
    private String St;
    @SerializedName("atm_ro_cash_loading_id")
    private String atmRoCashLoadingId;
    @SerializedName("Loading Date")
    private String lodingDate;
    @SerializedName("status")
    private String status;

    public String getAtmRoCashReceiptId() {
        return atmRoCashReceiptId;
    }

    public void setAtmRoCashReceiptId(String atmRoCashReceiptId) {
        this.atmRoCashReceiptId = atmRoCashReceiptId;
    }

    public String getAtmRoCashReceiptImage() {
        return atmRoCashReceiptImage;
    }

    public void setAtmRoCashReceiptImage(String atmRoCashReceiptImage) {
        this.atmRoCashReceiptImage = atmRoCashReceiptImage;
    }

    public String getSt() {
        return St;
    }

    public void setSt(String st) {
        St = st;
    }

    public String getAtmRoCashLoadingId() {
        return atmRoCashLoadingId;
    }

    public void setAtmRoCashLoadingId(String atmRoCashLoadingId) {
        this.atmRoCashLoadingId = atmRoCashLoadingId;
    }

    public String getLodingDate() {
        return lodingDate;
    }

    public void setLodingDate(String lodingDate) {
        this.lodingDate = lodingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
