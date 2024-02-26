package in.vakrangee.franchisee.atmloading.cash_loading;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ATMCashLoadingDto implements Serializable {

    @SerializedName("is_next_allowed")
    private String isNextAllowed;

    @SerializedName("cash_loading_id")
    private String cashLoadingId;

    @SerializedName("cash_sourcing_id")
    private String cashSourcingId;

    @SerializedName("min_loading_date")
    private String minLoadingDate;

    @SerializedName("source_date")
    private String sourceDate;

    @SerializedName("atm_id")
    private String atmId;

    @SerializedName("status")
    private String status;

    @SerializedName("amount")
    private String amount;

    @SerializedName("pending_amount")
    private String pendingAmount;

    @SerializedName("loaded_amount")
    private String loadedAmount;

    /**
     * 1 - Loading Pending
     * 2 - Cash Loaded
     */
    @SerializedName("status_code")
    private String statusCode;

    @SerializedName("IsEditable")
    private String IsEditable;

    @SerializedName("loading_date")
    private String loadingDate;

    @SerializedName("physical_c1")
    private String physicalC1;

    @SerializedName("physical_c2")
    private String physicalC2;

    @SerializedName("physical_c3")
    private String physicalC3;

    @SerializedName("physical_c4")
    private String physicalC4;

    @SerializedName("loaded_c1")
    private String loadedC1;

    @SerializedName("loaded_c2")
    private String loadedC2;

    @SerializedName("loaded_c3")
    private String loadedC3;

    @SerializedName("loaded_c4")
    private String loadedC4;

    @SerializedName("removed_c1")
    private String removedC1;

    @SerializedName("removed_c2")
    private String removedC2;

    @SerializedName("removed_c3")
    private String removedC3;

    @SerializedName("removed_c4")
    private String removedC4;

    @SerializedName("closing_c1")
    private String closingC1;

    @SerializedName("closing_c2")
    private String closingC2;

    @SerializedName("closing_c3")
    private String closingC3;

    @SerializedName("closing_c4")
    private String closingC4;

    @SerializedName("atm_counter_before_copy_id")
    private String atmCounterBeforeCopyId;

    @SerializedName("atm_counter_before_ext")
    private String atmCounterBeforeExt;

    @SerializedName("atm_counter_before_base64")
    private String atmCounterBeforeBase64;

    private transient Bitmap atmCounterBeforeBitmap;
    private transient android.net.Uri atmCounterBeforeUri;
    private transient boolean IsChangedPhotoAtmCounterBefore;

    @SerializedName("atm_counter_after_copy_id")
    private String atmCounterAfterCopyId;

    @SerializedName("atm_counter_after_ext")
    private String atmCounterAfterExt;

    @SerializedName("atm_counter_after_base64")
    private String atmCounterAfterBase64;

    private transient Bitmap atmCounterAfterBitmap;
    private transient android.net.Uri atmCounterAfterUri;
    private transient boolean IsChangedPhotoAtmCounterAfter;

    @SerializedName("switch_counter_before_copy_id")
    private String switchCounterBeforeCopyId;

    @SerializedName("switch_counter_before_ext")
    private String switchCounterBeforeExt;

    @SerializedName("switch_counter_before_base64")
    private String switchCounterBeforeBase64;

    private transient Bitmap switchCounterBeforeBitmap;
    private transient android.net.Uri switchCounterBeforeUri;
    private transient boolean IsChangedPhotoSwitchCounterBefore;

    @SerializedName("switch_counter_after_copy_id")
    private String switchCounterAfterCopyId;

    @SerializedName("switch_counter_after_ext")
    private String switchCounterAfterExt;

    @SerializedName("switch_counter_after_base64")
    private String switchCounterAfterBase64;

    private transient Bitmap switchCounterAfterBitmap;
    private transient android.net.Uri switchCounterAfterUri;
    private transient boolean IsChangedPhotoSwitchCounterAfter;

    @SerializedName("remaining_loading_note_count_c1")
    private String remaining_loading_note_count_c1;

    @SerializedName("remaining_loading_note_count_c2")
    private String remaining_loading_note_count_c2;

    @SerializedName("remaining_loading_note_count_c3_c4")
    private String remaining_loading_note_count_c3_c4;

    public String getCashSourcingId() {
        return cashSourcingId;
    }

    public void setCashSourcingId(String cashSourcingId) {
        this.cashSourcingId = cashSourcingId;
    }

    public String getSourceDate() {
        return sourceDate;
    }

    public void setSourceDate(String sourceDate) {
        this.sourceDate = sourceDate;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getIsEditable() {
        return IsEditable;
    }

    public void setIsEditable(String isEditable) {
        IsEditable = isEditable;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @SerializedName("remarks")
    private String remarks;

    public String getPhysicalC1() {
        return physicalC1;
    }

    public void setPhysicalC1(String physicalC1) {
        this.physicalC1 = physicalC1;
    }

    public String getPhysicalC2() {
        return physicalC2;
    }

    public void setPhysicalC2(String physicalC2) {
        this.physicalC2 = physicalC2;
    }

    public String getPhysicalC3() {
        return physicalC3;
    }

    public void setPhysicalC3(String physicalC3) {
        this.physicalC3 = physicalC3;
    }

    public String getPhysicalC4() {
        return physicalC4;
    }

    public void setPhysicalC4(String physicalC4) {
        this.physicalC4 = physicalC4;
    }

    public String getLoadedC1() {
        return loadedC1;
    }

    public void setLoadedC1(String loadedC1) {
        this.loadedC1 = loadedC1;
    }

    public String getLoadedC2() {
        return loadedC2;
    }

    public void setLoadedC2(String loadedC2) {
        this.loadedC2 = loadedC2;
    }

    public String getLoadedC3() {
        return loadedC3;
    }

    public void setLoadedC3(String loadedC3) {
        this.loadedC3 = loadedC3;
    }

    public String getLoadedC4() {
        return loadedC4;
    }

    public void setLoadedC4(String loadedC4) {
        this.loadedC4 = loadedC4;
    }

    public String getRemovedC1() {
        return removedC1;
    }

    public void setRemovedC1(String removedC1) {
        this.removedC1 = removedC1;
    }

    public String getRemovedC2() {
        return removedC2;
    }

    public void setRemovedC2(String removedC2) {
        this.removedC2 = removedC2;
    }

    public String getRemovedC3() {
        return removedC3;
    }

    public void setRemovedC3(String removedC3) {
        this.removedC3 = removedC3;
    }

    public String getRemovedC4() {
        return removedC4;
    }

    public void setRemovedC4(String removedC4) {
        this.removedC4 = removedC4;
    }

    public String getClosingC1() {
        return closingC1;
    }

    public void setClosingC1(String closingC1) {
        this.closingC1 = closingC1;
    }

    public String getClosingC2() {
        return closingC2;
    }

    public void setClosingC2(String closingC2) {
        this.closingC2 = closingC2;
    }

    public String getClosingC3() {
        return closingC3;
    }

    public void setClosingC3(String closingC3) {
        this.closingC3 = closingC3;
    }

    public String getClosingC4() {
        return closingC4;
    }

    public void setClosingC4(String closingC4) {
        this.closingC4 = closingC4;
    }

    public String getAtmCounterBeforeCopyId() {
        return atmCounterBeforeCopyId;
    }

    public void setAtmCounterBeforeCopyId(String atmCounterBeforeCopyId) {
        this.atmCounterBeforeCopyId = atmCounterBeforeCopyId;
    }

    public String getAtmCounterBeforeExt() {
        return atmCounterBeforeExt;
    }

    public void setAtmCounterBeforeExt(String atmCounterBeforeExt) {
        this.atmCounterBeforeExt = atmCounterBeforeExt;
    }

    public String getAtmCounterBeforeBase64() {
        return atmCounterBeforeBase64;
    }

    public void setAtmCounterBeforeBase64(String atmCounterBeforeBase64) {
        this.atmCounterBeforeBase64 = atmCounterBeforeBase64;
    }

    public Bitmap getAtmCounterBeforeBitmap() {
        return atmCounterBeforeBitmap;
    }

    public void setAtmCounterBeforeBitmap(Bitmap atmCounterBeforeBitmap) {
        this.atmCounterBeforeBitmap = atmCounterBeforeBitmap;
    }

    public Uri getAtmCounterBeforeUri() {
        return atmCounterBeforeUri;
    }

    public void setAtmCounterBeforeUri(Uri atmCounterBeforeUri) {
        this.atmCounterBeforeUri = atmCounterBeforeUri;
    }

    public boolean isChangedPhotoAtmCounterBefore() {
        return IsChangedPhotoAtmCounterBefore;
    }

    public void setChangedPhotoAtmCounterBefore(boolean changedPhotoAtmCounterBefore) {
        IsChangedPhotoAtmCounterBefore = changedPhotoAtmCounterBefore;
    }

    public String getAtmCounterAfterCopyId() {
        return atmCounterAfterCopyId;
    }

    public void setAtmCounterAfterCopyId(String atmCounterAfterCopyId) {
        this.atmCounterAfterCopyId = atmCounterAfterCopyId;
    }

    public String getAtmCounterAfterExt() {
        return atmCounterAfterExt;
    }

    public void setAtmCounterAfterExt(String atmCounterAfterExt) {
        this.atmCounterAfterExt = atmCounterAfterExt;
    }

    public String getAtmCounterAfterBase64() {
        return atmCounterAfterBase64;
    }

    public void setAtmCounterAfterBase64(String atmCounterAfterBase64) {
        this.atmCounterAfterBase64 = atmCounterAfterBase64;
    }

    public Bitmap getAtmCounterAfterBitmap() {
        return atmCounterAfterBitmap;
    }

    public void setAtmCounterAfterBitmap(Bitmap atmCounterAfterBitmap) {
        this.atmCounterAfterBitmap = atmCounterAfterBitmap;
    }

    public Uri getAtmCounterAfterUri() {
        return atmCounterAfterUri;
    }

    public void setAtmCounterAfterUri(Uri atmCounterAfterUri) {
        this.atmCounterAfterUri = atmCounterAfterUri;
    }

    public boolean isChangedPhotoAtmCounterAfter() {
        return IsChangedPhotoAtmCounterAfter;
    }

    public void setChangedPhotoAtmCounterAfter(boolean changedPhotoAtmCounterAfter) {
        IsChangedPhotoAtmCounterAfter = changedPhotoAtmCounterAfter;
    }

    public String getSwitchCounterBeforeCopyId() {
        return switchCounterBeforeCopyId;
    }

    public void setSwitchCounterBeforeCopyId(String switchCounterBeforeCopyId) {
        this.switchCounterBeforeCopyId = switchCounterBeforeCopyId;
    }

    public String getSwitchCounterBeforeExt() {
        return switchCounterBeforeExt;
    }

    public void setSwitchCounterBeforeExt(String switchCounterBeforeExt) {
        this.switchCounterBeforeExt = switchCounterBeforeExt;
    }

    public String getSwitchCounterBeforeBase64() {
        return switchCounterBeforeBase64;
    }

    public void setSwitchCounterBeforeBase64(String switchCounterBeforeBase64) {
        this.switchCounterBeforeBase64 = switchCounterBeforeBase64;
    }

    public Bitmap getSwitchCounterBeforeBitmap() {
        return switchCounterBeforeBitmap;
    }

    public void setSwitchCounterBeforeBitmap(Bitmap switchCounterBeforeBitmap) {
        this.switchCounterBeforeBitmap = switchCounterBeforeBitmap;
    }

    public Uri getSwitchCounterBeforeUri() {
        return switchCounterBeforeUri;
    }

    public void setSwitchCounterBeforeUri(Uri switchCounterBeforeUri) {
        this.switchCounterBeforeUri = switchCounterBeforeUri;
    }

    public boolean isChangedPhotoSwitchCounterBefore() {
        return IsChangedPhotoSwitchCounterBefore;
    }

    public void setChangedPhotoSwitchCounterBefore(boolean changedPhotoSwitchCounterBefore) {
        IsChangedPhotoSwitchCounterBefore = changedPhotoSwitchCounterBefore;
    }

    public String getSwitchCounterAfterCopyId() {
        return switchCounterAfterCopyId;
    }

    public void setSwitchCounterAfterCopyId(String switchCounterAfterCopyId) {
        this.switchCounterAfterCopyId = switchCounterAfterCopyId;
    }

    public String getSwitchCounterAfterExt() {
        return switchCounterAfterExt;
    }

    public void setSwitchCounterAfterExt(String switchCounterAfterExt) {
        this.switchCounterAfterExt = switchCounterAfterExt;
    }

    public String getSwitchCounterAfterBase64() {
        return switchCounterAfterBase64;
    }

    public void setSwitchCounterAfterBase64(String switchCounterAfterBase64) {
        this.switchCounterAfterBase64 = switchCounterAfterBase64;
    }

    public Bitmap getSwitchCounterAfterBitmap() {
        return switchCounterAfterBitmap;
    }

    public void setSwitchCounterAfterBitmap(Bitmap switchCounterAfterBitmap) {
        this.switchCounterAfterBitmap = switchCounterAfterBitmap;
    }

    public Uri getSwitchCounterAfterUri() {
        return switchCounterAfterUri;
    }

    public void setSwitchCounterAfterUri(Uri switchCounterAfterUri) {
        this.switchCounterAfterUri = switchCounterAfterUri;
    }

    public boolean isChangedPhotoSwitchCounterAfter() {
        return IsChangedPhotoSwitchCounterAfter;
    }

    public void setChangedPhotoSwitchCounterAfter(boolean changedPhotoSwitchCounterAfter) {
        IsChangedPhotoSwitchCounterAfter = changedPhotoSwitchCounterAfter;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLoadingDate() {
        return loadingDate;
    }

    public void setLoadingDate(String loadingDate) {
        this.loadingDate = loadingDate;
    }

    public String getIsNextAllowed() {
        return isNextAllowed;
    }

    public void setIsNextAllowed(String isNextAllowed) {
        this.isNextAllowed = isNextAllowed;
    }

    public String getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(String pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getLoadedAmount() {
        return loadedAmount;
    }

    public void setLoadedAmount(String loadedAmount) {
        this.loadedAmount = loadedAmount;
    }

    public String getMinLoadingDate() {
        return minLoadingDate;
    }

    public void setMinLoadingDate(String minLoadingDate) {
        this.minLoadingDate = minLoadingDate;
    }

    public String getCashLoadingId() {
        return cashLoadingId;
    }

    public void setCashLoadingId(String cashLoadingId) {
        this.cashLoadingId = cashLoadingId;
    }

    public String getRemaining_loading_note_count_c1() {
        return remaining_loading_note_count_c1;
    }

    public void setRemaining_loading_note_count_c1(String remaining_loading_note_count_c1) {
        this.remaining_loading_note_count_c1 = remaining_loading_note_count_c1;
    }

    public String getRemaining_loading_note_count_c2() {
        return remaining_loading_note_count_c2;
    }

    public void setRemaining_loading_note_count_c2(String remaining_loading_note_count_c2) {
        this.remaining_loading_note_count_c2 = remaining_loading_note_count_c2;
    }

    public String getRemaining_loading_note_count_c3_c4() {
        return remaining_loading_note_count_c3_c4;
    }

    public void setRemaining_loading_note_count_c3_c4(String remaining_loading_note_count_c3_c4) {
        this.remaining_loading_note_count_c3_c4 = remaining_loading_note_count_c3_c4;
    }
}
