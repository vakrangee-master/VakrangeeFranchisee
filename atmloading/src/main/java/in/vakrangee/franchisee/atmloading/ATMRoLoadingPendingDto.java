package in.vakrangee.franchisee.atmloading;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ATMRoLoadingPendingDto implements Serializable {


    @SerializedName("atm_ro_cash_receipt_date")
    private String atmRoCashReceiptDate;
    @SerializedName("amount")
    private String amount;
    @SerializedName("atm_cash_ro_name")
    private String atmCashRoName;
    @SerializedName("atm_ro_cash_receipt_no")
    private String atmRoCashReceiptNo;
    @SerializedName("status")
    private String status;


    /*@SerializedName("retail_outlet_cash_no")
    private String retailOutletCashNo;

    @SerializedName("retail_outlet_cash_date")
    private String retailOutletCashDate;

    @SerializedName("retail_name")
    private String retailName;

    @SerializedName("amount")
    private String amount;

    @SerializedName("status")
    private String status;


    public String getRetailOutletCashNo() {
        return retailOutletCashNo;
    }

    public void setRetailOutletCashNo(String retailOutletCashNo) {
        this.retailOutletCashNo = retailOutletCashNo;
    }

    public String getRetailOutletCashDate() {
        return retailOutletCashDate;
    }

    public void setRetailOutletCashDate(String retailOutletCashDate) {
        this.retailOutletCashDate = retailOutletCashDate;
    }

    public String getRetailName() {
        return retailName;
    }

    public void setRetailName(String retailName) {
        this.retailName = retailName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
*/

    public String getAtmRoCashReceiptDate() {
        return atmRoCashReceiptDate;
    }

    public void setAtmRoCashReceiptDate(String atmRoCashReceiptDate) {
        this.atmRoCashReceiptDate = atmRoCashReceiptDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAtmCashRoName() {
        return atmCashRoName;
    }

    public void setAtmCashRoName(String atmCashRoName) {
        this.atmCashRoName = atmCashRoName;
    }

    public String getAtmRoCashReceiptNo() {
        return atmRoCashReceiptNo;
    }

    public void setAtmRoCashReceiptNo(String atmRoCashReceiptNo) {
        this.atmRoCashReceiptNo = atmRoCashReceiptNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSelected() {
        return isSelected;
    }

    //checkbox check
    private boolean isSelected;


    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
