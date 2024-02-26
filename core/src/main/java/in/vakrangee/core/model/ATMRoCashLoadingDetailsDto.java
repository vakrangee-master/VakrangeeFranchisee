package in.vakrangee.core.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ATMRoCashLoadingDetailsDto implements Serializable {

    @SerializedName("counter_after_load_cassette_2")
    private String counterAfterLoadCassette2;
    @SerializedName("counter_after_load_cassette_3")
    private String counterAfterLoadCassette3;
    @SerializedName("counter_after_load_cassette_4")
    private String counterAfterLoadCassette4;
    @SerializedName("switch_before_loading_receipt_image")
    private String switchBeforeLoadingReceiptImage;
    @SerializedName("opening_cassette_3")
    private String openingCassette3;
    @SerializedName("purge_cassette_4")
    private String purgeCassette4;
    @SerializedName("counter_before_load_cassette_3")
    private String counterBeforeLoadCassette3;
    @SerializedName("opening_cassette_2")
    private String openingCassette2;
    @SerializedName("purge_cassette_3")
    private String purgeCassette3;
    @SerializedName("counter_before_load_cassette_4")
    private String counterBeforeLoadCassette4;
    @SerializedName("purge_cassette_2")
    private String purgeCassette2;
    @SerializedName("counter_before_load_cassette_1")
    private String counterBeforeLoadCassette1;
    @SerializedName("opening_cassette_4")
    private String openingCassette4;
    @SerializedName("purge_cassette_1")
    private String purgeCassette1;
    @SerializedName("counter_before_load_cassette_2")
    private String counterBeforeLoadCassette2;
    @SerializedName("counter_after_load_cassette_1")
    private String counterAfterLoadCassette1;
    @SerializedName("switch_after_loading_receipt_image")
    private String switchAfterLoadingReceiptImage;
    @SerializedName("opening_cassette_1")
    private String openingCassette1;
    @SerializedName("date_time")
    private String dateTime;
    @SerializedName("balance")
    private String balance;
    @SerializedName("vk_id")
    private String vkId;
    @SerializedName("atm_after_loading_receipt_image")
    private String atmAfterLoadingReceiptImage;
    @SerializedName("difference_remarks")
    private String differenceRemarks;
    @SerializedName("load_type_b")
    private String loadTypeB;
    @SerializedName("load_type_c")
    private String loadTypeC;
    @SerializedName("load_type_a")
    private String loadTypeA;
    @SerializedName("allow_editing")
    private String allowEditing;
    @SerializedName("switch_after_load_cassette_1")
    private String switchAfterLoadCassette1;
    @SerializedName("switch_after_load_cassette_4")
    private String switchAfterLoadCassette4;
    @SerializedName("switch_after_load_cassette_3")
    private String switchAfterLoadCassette3;
    @SerializedName("switch_after_load_cassette_2")
    private String switchAfterLoadCassette2;
    @SerializedName("loading_date")
    private String loadingDate;
    @SerializedName("load_cassette_4")
    private String loadCassette4;
    @SerializedName("atm_ro_cash_loading_id")
    private String atmRoCashLoadingId;
    @SerializedName("load_cassette_2")
    private String loadCassette2;
    @SerializedName("switch_before_load_cassette_1")
    private String switchBeforeLoadCassette1;
    @SerializedName("load_cassette_3")
    private String loadCassette3;
    @SerializedName("switch_before_load_cassette_2")
    private String switchBeforeLoadCassette2;
    @SerializedName("switch_before_load_cassette_3")
    private String switchBeforeLoadCassette3;
    @SerializedName("atm_before_loading_receipt_image")
    private String atmBeforeLoadingReceiptImage;
    @SerializedName("load_cassette_1")
    private String loadCassette1;
    @SerializedName("switch_before_load_cassette_4")
    private String switchBeforeLoadCassette4;
    @SerializedName("dispense_cassette_3")
    private String dispenseCassette3;
    @SerializedName("dispense_cassette_4")
    private String dispenseCassette4;
    @SerializedName("atm_id")
    private String atmId;
    @SerializedName("dispense_cassette_1")
    private String dispenseCassette1;
    @SerializedName("remarks")
    private String remarks;
    @SerializedName("dispense_cassette_2")
    private String dispenseCassette2;


    //for image

    @SerializedName("ext")
    private String ext;
    @SerializedName("id")
    private String id;


    private transient Bitmap bitmap;
    private transient android.net.Uri Uri;
    private transient String Name;
    private transient boolean IsChangedPhoto;

    @SerializedName("physical_image_id")
    private String physicalImageId;
    @SerializedName("set_physcial_image")
    private String setPhysicalImage;

    //for total amount save
    private String totalPhysicalNote;
    private String totalPhysicalAmount;

    private String totalPurgeNote;
    private String totalPurgeAmount;

    private String totalCashNote;
    private String totalCashAmount;

    public String getCounterAfterLoadCassette2() {
        return counterAfterLoadCassette2;
    }

    public void setCounterAfterLoadCassette2(String counterAfterLoadCassette2) {
        this.counterAfterLoadCassette2 = counterAfterLoadCassette2;
    }

    public String getCounterAfterLoadCassette3() {
        return counterAfterLoadCassette3;
    }

    public void setCounterAfterLoadCassette3(String counterAfterLoadCassette3) {
        this.counterAfterLoadCassette3 = counterAfterLoadCassette3;
    }

    public String getCounterAfterLoadCassette4() {
        return counterAfterLoadCassette4;
    }

    public void setCounterAfterLoadCassette4(String counterAfterLoadCassette4) {
        this.counterAfterLoadCassette4 = counterAfterLoadCassette4;
    }

    public String getSwitchBeforeLoadingReceiptImage() {
        return switchBeforeLoadingReceiptImage;
    }

    public void setSwitchBeforeLoadingReceiptImage(String switchBeforeLoadingReceiptImage) {
        this.switchBeforeLoadingReceiptImage = switchBeforeLoadingReceiptImage;
    }

    public String getOpeningCassette3() {
        return openingCassette3;
    }

    public void setOpeningCassette3(String openingCassette3) {
        this.openingCassette3 = openingCassette3;
    }

    public String getPurgeCassette4() {
        return purgeCassette4;
    }

    public void setPurgeCassette4(String purgeCassette4) {
        this.purgeCassette4 = purgeCassette4;
    }

    public String getCounterBeforeLoadCassette3() {
        return counterBeforeLoadCassette3;
    }

    public void setCounterBeforeLoadCassette3(String counterBeforeLoadCassette3) {
        this.counterBeforeLoadCassette3 = counterBeforeLoadCassette3;
    }

    public String getOpeningCassette2() {
        return openingCassette2;
    }

    public void setOpeningCassette2(String openingCassette2) {
        this.openingCassette2 = openingCassette2;
    }

    public String getPurgeCassette3() {
        return purgeCassette3;
    }

    public void setPurgeCassette3(String purgeCassette3) {
        this.purgeCassette3 = purgeCassette3;
    }

    public String getCounterBeforeLoadCassette4() {
        return counterBeforeLoadCassette4;
    }

    public void setCounterBeforeLoadCassette4(String counterBeforeLoadCassette4) {
        this.counterBeforeLoadCassette4 = counterBeforeLoadCassette4;
    }

    public String getPurgeCassette2() {
        return purgeCassette2;
    }

    public void setPurgeCassette2(String purgeCassette2) {
        this.purgeCassette2 = purgeCassette2;
    }

    public String getCounterBeforeLoadCassette1() {
        return counterBeforeLoadCassette1;
    }

    public void setCounterBeforeLoadCassette1(String counterBeforeLoadCassette1) {
        this.counterBeforeLoadCassette1 = counterBeforeLoadCassette1;
    }

    public String getOpeningCassette4() {
        return openingCassette4;
    }

    public void setOpeningCassette4(String openingCassette4) {
        this.openingCassette4 = openingCassette4;
    }

    public String getPurgeCassette1() {
        return purgeCassette1;
    }

    public void setPurgeCassette1(String purgeCassette1) {
        this.purgeCassette1 = purgeCassette1;
    }

    public String getCounterBeforeLoadCassette2() {
        return counterBeforeLoadCassette2;
    }

    public void setCounterBeforeLoadCassette2(String counterBeforeLoadCassette2) {
        this.counterBeforeLoadCassette2 = counterBeforeLoadCassette2;
    }

    public String getCounterAfterLoadCassette1() {
        return counterAfterLoadCassette1;
    }

    public void setCounterAfterLoadCassette1(String counterAfterLoadCassette1) {
        this.counterAfterLoadCassette1 = counterAfterLoadCassette1;
    }

    public String getSwitchAfterLoadingReceiptImage() {
        return switchAfterLoadingReceiptImage;
    }

    public void setSwitchAfterLoadingReceiptImage(String switchAfterLoadingReceiptImage) {
        this.switchAfterLoadingReceiptImage = switchAfterLoadingReceiptImage;
    }

    public String getOpeningCassette1() {
        return openingCassette1;
    }

    public void setOpeningCassette1(String openingCassette1) {
        this.openingCassette1 = openingCassette1;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getVkId() {
        return vkId;
    }

    public void setVkId(String vkId) {
        this.vkId = vkId;
    }

    public String getAtmAfterLoadingReceiptImage() {
        return atmAfterLoadingReceiptImage;
    }

    public void setAtmAfterLoadingReceiptImage(String atmAfterLoadingReceiptImage) {
        this.atmAfterLoadingReceiptImage = atmAfterLoadingReceiptImage;
    }

    public String getDifferenceRemarks() {
        return differenceRemarks;
    }

    public void setDifferenceRemarks(String differenceRemarks) {
        this.differenceRemarks = differenceRemarks;
    }

    public String getLoadTypeB() {
        return loadTypeB;
    }

    public void setLoadTypeB(String loadTypeB) {
        this.loadTypeB = loadTypeB;
    }

    public String getLoadTypeC() {
        return loadTypeC;
    }

    public void setLoadTypeC(String loadTypeC) {
        this.loadTypeC = loadTypeC;
    }

    public String getLoadTypeA() {
        return loadTypeA;
    }

    public void setLoadTypeA(String loadTypeA) {
        this.loadTypeA = loadTypeA;
    }

    public String getAllowEditing() {
        return allowEditing;
    }

    public void setAllowEditing(String allowEditing) {
        this.allowEditing = allowEditing;
    }

    public String getSwitchAfterLoadCassette1() {
        return switchAfterLoadCassette1;
    }

    public void setSwitchAfterLoadCassette1(String switchAfterLoadCassette1) {
        this.switchAfterLoadCassette1 = switchAfterLoadCassette1;
    }

    public String getSwitchAfterLoadCassette4() {
        return switchAfterLoadCassette4;
    }

    public void setSwitchAfterLoadCassette4(String switchAfterLoadCassette4) {
        this.switchAfterLoadCassette4 = switchAfterLoadCassette4;
    }

    public String getSwitchAfterLoadCassette3() {
        return switchAfterLoadCassette3;
    }

    public void setSwitchAfterLoadCassette3(String switchAfterLoadCassette3) {
        this.switchAfterLoadCassette3 = switchAfterLoadCassette3;
    }

    public String getSwitchAfterLoadCassette2() {
        return switchAfterLoadCassette2;
    }

    public void setSwitchAfterLoadCassette2(String switchAfterLoadCassette2) {
        this.switchAfterLoadCassette2 = switchAfterLoadCassette2;
    }

    public String getLoadingDate() {
        return loadingDate;
    }

    public void setLoadingDate(String loadingDate) {
        this.loadingDate = loadingDate;
    }

    public String getLoadCassette4() {
        return loadCassette4;
    }

    public void setLoadCassette4(String loadCassette4) {
        this.loadCassette4 = loadCassette4;
    }

    public String getAtmRoCashLoadingId() {
        return atmRoCashLoadingId;
    }

    public void setAtmRoCashLoadingId(String atmRoCashLoadingId) {
        this.atmRoCashLoadingId = atmRoCashLoadingId;
    }

    public String getLoadCassette2() {
        return loadCassette2;
    }

    public void setLoadCassette2(String loadCassette2) {
        this.loadCassette2 = loadCassette2;
    }

    public String getSwitchBeforeLoadCassette1() {
        return switchBeforeLoadCassette1;
    }

    public void setSwitchBeforeLoadCassette1(String switchBeforeLoadCassette1) {
        this.switchBeforeLoadCassette1 = switchBeforeLoadCassette1;
    }

    public String getLoadCassette3() {
        return loadCassette3;
    }

    public void setLoadCassette3(String loadCassette3) {
        this.loadCassette3 = loadCassette3;
    }

    public String getSwitchBeforeLoadCassette2() {
        return switchBeforeLoadCassette2;
    }

    public void setSwitchBeforeLoadCassette2(String switchBeforeLoadCassette2) {
        this.switchBeforeLoadCassette2 = switchBeforeLoadCassette2;
    }

    public String getSwitchBeforeLoadCassette3() {
        return switchBeforeLoadCassette3;
    }

    public void setSwitchBeforeLoadCassette3(String switchBeforeLoadCassette3) {
        this.switchBeforeLoadCassette3 = switchBeforeLoadCassette3;
    }

    public String getAtmBeforeLoadingReceiptImage() {
        return atmBeforeLoadingReceiptImage;
    }

    public void setAtmBeforeLoadingReceiptImage(String atmBeforeLoadingReceiptImage) {
        this.atmBeforeLoadingReceiptImage = atmBeforeLoadingReceiptImage;
    }

    public String getLoadCassette1() {
        return loadCassette1;
    }

    public void setLoadCassette1(String loadCassette1) {
        this.loadCassette1 = loadCassette1;
    }

    public String getSwitchBeforeLoadCassette4() {
        return switchBeforeLoadCassette4;
    }

    public void setSwitchBeforeLoadCassette4(String switchBeforeLoadCassette4) {
        this.switchBeforeLoadCassette4 = switchBeforeLoadCassette4;
    }

    public String getDispenseCassette3() {
        return dispenseCassette3;
    }

    public void setDispenseCassette3(String dispenseCassette3) {
        this.dispenseCassette3 = dispenseCassette3;
    }

    public String getDispenseCassette4() {
        return dispenseCassette4;
    }

    public void setDispenseCassette4(String dispenseCassette4) {
        this.dispenseCassette4 = dispenseCassette4;
    }

    public String getAtmId() {
        return atmId;
    }

    public void setAtmId(String atmId) {
        this.atmId = atmId;
    }

    public String getDispenseCassette1() {
        return dispenseCassette1;
    }

    public void setDispenseCassette1(String dispenseCassette1) {
        this.dispenseCassette1 = dispenseCassette1;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDispenseCassette2() {
        return dispenseCassette2;
    }

    public void setDispenseCassette2(String dispenseCassette2) {
        this.dispenseCassette2 = dispenseCassette2;
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isChangedPhoto() {
        return IsChangedPhoto;
    }

    public void setChangedPhoto(boolean changedPhoto) {
        IsChangedPhoto = changedPhoto;
    }

    public String getPhysicalImageId() {
        return physicalImageId;
    }

    public void setPhysicalImageId(String physicalImageId) {
        this.physicalImageId = physicalImageId;
    }

    public String getSetPhysicalImage() {
        return setPhysicalImage;
    }

    public void setSetPhysicalImage(String setPhysicalImage) {
        this.setPhysicalImage = setPhysicalImage;
    }


    public String getTotalPhysicalNote() {
        return totalPhysicalNote;
    }

    public void setTotalPhysicalNote(String totalPhysicalNote) {
        this.totalPhysicalNote = totalPhysicalNote;
    }

    public String getTotalPhysicalAmount() {
        return totalPhysicalAmount;
    }

    public void setTotalPhysicalAmount(String totalPhysicalAmount) {
        this.totalPhysicalAmount = totalPhysicalAmount;
    }

    public String getTotalPurgeNote() {
        return totalPurgeNote;
    }

    public void setTotalPurgeNote(String totalPurgeNote) {
        this.totalPurgeNote = totalPurgeNote;
    }

    public String getTotalPurgeAmount() {
        return totalPurgeAmount;
    }

    public void setTotalPurgeAmount(String totalPurgeAmount) {
        this.totalPurgeAmount = totalPurgeAmount;
    }

    public String getTotalCashNote() {
        return totalCashNote;
    }

    public void setTotalCashNote(String totalCashNote) {
        this.totalCashNote = totalCashNote;
    }

    public String getTotalCashAmount() {
        return totalCashAmount;
    }

    public void setTotalCashAmount(String totalCashAmount) {
        this.totalCashAmount = totalCashAmount;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
