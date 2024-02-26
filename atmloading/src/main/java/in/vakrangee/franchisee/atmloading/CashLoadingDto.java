package in.vakrangee.franchisee.atmloading;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CashLoadingDto implements Serializable {

    //cash_loading
    @SerializedName("cash_loading_cassette1_2000")
    private String cashLoadingCassette1_2000;
    @SerializedName("cash_loading_cassette2_500")
    private String cashLoadingCassette2_500;
    @SerializedName("cash_loading_cassette3_100")
    private String cashLoadingCassette3_100;
    @SerializedName("cash_loading_cassette4_100")
    private String cashLoadingCassette4_100;

    @SerializedName("cash_loading_total_note")
    private String cashLoadingTotalNote;
    @SerializedName("cash_loading_total_amount")
    private String cashLoadingTotalAmount;


    //cashLoading

    public String getCashLoadingCassette1_2000() {
        return cashLoadingCassette1_2000;
    }

    public void setCashLoadingCassette1_2000(String cashLoadingCassette1_2000) {
        this.cashLoadingCassette1_2000 = cashLoadingCassette1_2000;
    }

    public String getCashLoadingCassette2_500() {
        return cashLoadingCassette2_500;
    }

    public void setCashLoadingCassette2_500(String cashLoadingCassette2_500) {
        this.cashLoadingCassette2_500 = cashLoadingCassette2_500;
    }

    public String getCashLoadingCassette3_100() {
        return cashLoadingCassette3_100;
    }

    public void setCashLoadingCassette3_100(String cashLoadingCassette3_100) {
        this.cashLoadingCassette3_100 = cashLoadingCassette3_100;
    }

    public String getCashLoadingCassette4_100() {
        return cashLoadingCassette4_100;
    }

    public void setCashLoadingCassette4_100(String cashLoadingCassette4_100) {
        this.cashLoadingCassette4_100 = cashLoadingCassette4_100;
    }

    public String getCashLoadingTotalNote() {
        return cashLoadingTotalNote;
    }

    public void setCashLoadingTotalNote(String cashLoadingTotalNote) {
        this.cashLoadingTotalNote = cashLoadingTotalNote;
    }

    public String getCashLoadingTotalAmount() {
        return cashLoadingTotalAmount;
    }

    public void setCashLoadingTotalAmount(String cashLoadingTotalAmount) {
        this.cashLoadingTotalAmount = cashLoadingTotalAmount;
    }

}
