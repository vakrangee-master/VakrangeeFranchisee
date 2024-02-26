package in.vakrangee.franchisee.hardwareacknowledgement;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KendraAcknowledgementDto implements Serializable{

    @SerializedName("nextgen_vakrangee_kendra_equipments_acknowledgement_id")
    private String Id;

    @SerializedName("nextgen_vakrangee_kendra_equipments_id")
    private String nextgen_vakrangee_kendra_equipments_id;

    @SerializedName("nextgen_standard_equipment_id")
    private String nextgen_standard_equipment_id;

    @SerializedName("brand_id")
    private String brandName;

    private String series;

    private String model;

    @SerializedName("status_code")
    private String statusCode;

    @SerializedName("serial_no")
    private String serialNo;

    @SerializedName("received_condition")
    private String goodsCondition;

    @SerializedName("received_date")
    private String receivedDate;

    @SerializedName("remarks")
    private String remarks;

    @SerializedName("equipmentImages")
    private String equipmentImages;

    @SerializedName("equipment_image_id")
    private String equipmentPicFileId;

    @SerializedName("equipment_image_Ext")
    private String equipmentPicExt;

    @SerializedName("equipment_image_base64")
    private String equipmentPicBase64;

    private transient String equipmentPicName;

    @SerializedName("equipment_packaging_label_image_id")
    private String equipmentPackagingFileId;

    @SerializedName("equipment_packaging_label_image_Ext")
    private String equipmentPackagingExt;

    @SerializedName("equipment_packaging_label_image_base64")
    private String equipmentPackagingBase64;

    private transient String equipmentPackagingName;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getGoodsCondition() {
        return goodsCondition;
    }

    public void setGoodsCondition(String goodsCondition) {
        this.goodsCondition = goodsCondition;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEquipmentPicFileId() {
        return equipmentPicFileId;
    }

    public void setEquipmentPicFileId(String equipmentPicFileId) {
        this.equipmentPicFileId = equipmentPicFileId;
    }

    public String getEquipmentPicExt() {
        return equipmentPicExt;
    }

    public void setEquipmentPicExt(String equipmentPicExt) {
        this.equipmentPicExt = equipmentPicExt;
    }

    public String getEquipmentPicBase64() {
        return equipmentPicBase64;
    }

    public void setEquipmentPicBase64(String equipmentPicBase64) {
        this.equipmentPicBase64 = equipmentPicBase64;
    }

    public String getEquipmentPicName() {
        return equipmentPicName;
    }

    public void setEquipmentPicName(String equipmentPicName) {
        this.equipmentPicName = equipmentPicName;
    }

    public String getEquipmentPackagingFileId() {
        return equipmentPackagingFileId;
    }

    public void setEquipmentPackagingFileId(String equipmentPackagingFileId) {
        this.equipmentPackagingFileId = equipmentPackagingFileId;
    }

    public String getEquipmentPackagingExt() {
        return equipmentPackagingExt;
    }

    public void setEquipmentPackagingExt(String equipmentPackagingExt) {
        this.equipmentPackagingExt = equipmentPackagingExt;
    }

    public String getEquipmentPackagingBase64() {
        return equipmentPackagingBase64;
    }

    public void setEquipmentPackagingBase64(String equipmentPackagingBase64) {
        this.equipmentPackagingBase64 = equipmentPackagingBase64;
    }

    public String getEquipmentPackagingName() {
        return equipmentPackagingName;
    }

    public void setEquipmentPackagingName(String equipmentPackagingName) {
        this.equipmentPackagingName = equipmentPackagingName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNextgen_vakrangee_kendra_equipments_id() {
        return nextgen_vakrangee_kendra_equipments_id;
    }

    public void setNextgen_vakrangee_kendra_equipments_id(String nextgen_vakrangee_kendra_equipments_id) {
        this.nextgen_vakrangee_kendra_equipments_id = nextgen_vakrangee_kendra_equipments_id;
    }

    public String getNextgen_standard_equipment_id() {
        return nextgen_standard_equipment_id;
    }

    public void setNextgen_standard_equipment_id(String nextgen_standard_equipment_id) {
        this.nextgen_standard_equipment_id = nextgen_standard_equipment_id;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getEquipmentImages() {
        return equipmentImages;
    }

    public void setEquipmentImages(String equipmentImages) {
        this.equipmentImages = equipmentImages;
    }
}
