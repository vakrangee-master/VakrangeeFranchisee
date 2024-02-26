package in.vakrangee.franchisee.atmloading.cash_loading;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PendingCashLoadingDto implements Serializable {

    @SerializedName("atm_id")
    private String atmId;

    @SerializedName("cash_sourcing_id")
    private String cashSourcingId;

    @SerializedName("source_date")
    private String sourceDate;

    @SerializedName("sourced_amount")
    private String sourcedAmount;

    @SerializedName("cash_loaded")
    private String cashLoaded;

    @SerializedName("balance_cash")
    private String balanceCash;

    @SerializedName("IsEditable")
    private String IsEditable;

    @SerializedName("pending_amount")
    private String pendingAmount;

    @SerializedName("min_loading_date_actual")
    private String minLoadingDate;

    @SerializedName("remaining_loading_note_count_c1")
    private String remaining_loading_note_count_c1;

    @SerializedName("remaining_loading_note_count_c2")
    private String remaining_loading_note_count_c2;

    @SerializedName("remaining_loading_note_count_c3_c4")
    private String remaining_loading_note_count_c3_c4;

    public String getSourceDate() {
        return sourceDate;
    }

    public void setSourceDate(String sourceDate) {
        this.sourceDate = sourceDate;
    }

    public String getSourcedAmount() {
        return sourcedAmount;
    }

    public void setSourcedAmount(String sourcedAmount) {
        this.sourcedAmount = sourcedAmount;
    }

    public String getCashLoaded() {
        return cashLoaded;
    }

    public void setCashLoaded(String cashLoaded) {
        this.cashLoaded = cashLoaded;
    }

    public String getBalanceCash() {
        return balanceCash;
    }

    public void setBalanceCash(String balanceCash) {
        this.balanceCash = balanceCash;
    }

    public String getIsEditable() {
        return IsEditable;
    }

    public void setIsEditable(String isEditable) {
        IsEditable = isEditable;
    }

    public String getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(String pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getMinLoadingDate() {
        return minLoadingDate;
    }

    public void setMinLoadingDate(String minLoadingDate) {
        this.minLoadingDate = minLoadingDate;
    }

    public String getAtmId() {
        return atmId;
    }

    public void setAtmId(String atmId) {
        this.atmId = atmId;
    }

    public String getCashSourcingId() {
        return cashSourcingId;
    }

    public void setCashSourcingId(String cashSourcingId) {
        this.cashSourcingId = cashSourcingId;
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
