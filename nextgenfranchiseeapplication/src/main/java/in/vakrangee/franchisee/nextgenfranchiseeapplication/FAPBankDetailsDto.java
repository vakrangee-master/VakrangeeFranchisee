package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FAPBankDetailsDto implements Serializable {

    @SerializedName("BankNameId")
    private String bankName;

    @SerializedName("AccountHolderName")
    private String accountHolderName;

    @SerializedName("BranchName")
    private String branchName;

    @SerializedName("AccountNumber")
    private String accountNumber;

    @SerializedName("accountTypeId")
    private String accountType;

    @SerializedName("IFSCCode")
    private String iFSCCode;

    @SerializedName("ChequeFileId")
    private String ChequeFileId;

    @SerializedName("ChequeFileExt")
    private String ChequeFileExt;

    @SerializedName("ChequeBase64")
    private String passBookChequeBase64;

    @SerializedName("is_bank_stmt_available")
    private String isBankStmtAvailable;

    @SerializedName("is_bank_stmt_valid")
    private String isBankStmtValid;

    @SerializedName("is_bank_stmt_valid_msg")
    private String isBankStmtValidMsg;

    @SerializedName("is_bank_stmt_new_way")
    private String isBankStmtNewWay;

    private transient String passBookChequeFileName;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getiFSCCode() {
        return iFSCCode;
    }

    public void setiFSCCode(String iFSCCode) {
        this.iFSCCode = iFSCCode;
    }

    public String getPassBookChequeBase64() {
        return passBookChequeBase64;
    }

    public void setPassBookChequeBase64(String passBookChequeBase64) {
        this.passBookChequeBase64 = passBookChequeBase64;
    }

    public String getPassBookChequeFileName() {
        return passBookChequeFileName;
    }

    public void setPassBookChequeFileName(String passBookChequeFileName) {
        this.passBookChequeFileName = passBookChequeFileName;
    }

    public String getChequeFileId() {
        return ChequeFileId;
    }

    public void setChequeFileId(String chequeFileId) {
        ChequeFileId = chequeFileId;
    }

    public String getChequeFileExt() {
        return ChequeFileExt;
    }

    public void setChequeFileExt(String chequeFileExt) {
        ChequeFileExt = chequeFileExt;
    }

    public String getIsBankStmtAvailable() {
        return isBankStmtAvailable;
    }

    public void setIsBankStmtAvailable(String isBankStmtAvailable) {
        this.isBankStmtAvailable = isBankStmtAvailable;
    }

    public String getIsBankStmtValid() {
        return isBankStmtValid;
    }

    public void setIsBankStmtValid(String isBankStmtValid) {
        this.isBankStmtValid = isBankStmtValid;
    }

    public String getIsBankStmtValidMsg() {
        return isBankStmtValidMsg;
    }

    public void setIsBankStmtValidMsg(String isBankStmtValidMsg) {
        this.isBankStmtValidMsg = isBankStmtValidMsg;
    }

    public String getIsBankStmtNewWay() {
        return isBankStmtNewWay;
    }

    public void setIsBankStmtNewWay(String isBankStmtNewWay) {
        this.isBankStmtNewWay = isBankStmtNewWay;
    }

}
