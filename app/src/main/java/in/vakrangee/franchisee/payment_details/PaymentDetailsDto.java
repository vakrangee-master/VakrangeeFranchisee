package in.vakrangee.franchisee.payment_details;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentDetailsDto implements Serializable {

    @SerializedName("transaction_id")
    private String transactionId;

    @SerializedName("amount")
    private String amount;

    @SerializedName("transfer_mode")
    private String transferMode;

    @SerializedName("remitter_name")
    private String remitterName;

    @SerializedName("from_bank_name")
    private String fromBankName;

    @SerializedName("ecms_inward_dump_id")
    private String ecmsInwardDumpId;

    @SerializedName("utr")
    private String UTR;

    @SerializedName("from_account_number")
    private String fromAccountNumber;

    @SerializedName("remitter_ifsc")
    private String remitterIFSC;

    @SerializedName("nbin_code")
    private String nbinCode;

    @SerializedName("date_time")
    private String dateTime;

    @SerializedName("virtual_account")
    private String virtualAccount;

    @SerializedName("narration")
    private String narration;

    @SerializedName("is_more_opened")
    private int isMoreOpened;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransferMode() {
        return transferMode;
    }

    public void setTransferMode(String transferMode) {
        this.transferMode = transferMode;
    }

    public String getRemitterName() {
        return remitterName;
    }

    public void setRemitterName(String remitterName) {
        this.remitterName = remitterName;
    }

    public String getFromBankName() {
        return fromBankName;
    }

    public void setFromBankName(String fromBankName) {
        this.fromBankName = fromBankName;
    }

    public String getEcmsInwardDumpId() {
        return ecmsInwardDumpId;
    }

    public void setEcmsInwardDumpId(String ecmsInwardDumpId) {
        this.ecmsInwardDumpId = ecmsInwardDumpId;
    }

    public String getUTR() {
        return UTR;
    }

    public void setUTR(String UTR) {
        this.UTR = UTR;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public String getRemitterIFSC() {
        return remitterIFSC;
    }

    public void setRemitterIFSC(String remitterIFSC) {
        this.remitterIFSC = remitterIFSC;
    }

    public String getNbinCode() {
        return nbinCode;
    }

    public void setNbinCode(String nbinCode) {
        this.nbinCode = nbinCode;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getVirtualAccount() {
        return virtualAccount;
    }

    public void setVirtualAccount(String virtualAccount) {
        this.virtualAccount = virtualAccount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public int getIsMoreOpened() {
        return isMoreOpened;
    }

    public void setIsMoreOpened(int isMoreOpened) {
        this.isMoreOpened = isMoreOpened;
    }
}
