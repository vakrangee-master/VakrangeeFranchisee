package in.vakrangee.franchisee.workinprogress;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WIPCheckListDto implements Serializable {

    public List<WIPStatusDto> statusList;

    //region For Element Image
    private transient boolean IsChangedPhoto;
    private transient Uri elementUri;
    private transient Bitmap elementImgBitmap;

    @SerializedName("element_detail_image")
    private String elementImageBase64;

    @SerializedName("vakrangee_kendra_design_verification_id")
    private String vakrangee_kendra_design_verification_id;

    @SerializedName("vakrangee_kendra_branding_verification_id")
    private String vakrangee_kendra_branding_verification_id;

    private transient String capturedDateTime;

    private transient String elementImgRemarks;
    //endregion

    @SerializedName("element_detail_id")
    private String Id;

    @SerializedName("element_detail_image_id")
    private String image_id;

    @SerializedName("element_name")
    private String elementName;

    @SerializedName(value = "element_descriptions",alternate = "element_description")
    private String desc;

    @SerializedName("mandatory")
    private String mandatory;

    private transient String subElementName;
    private transient String size;
    private transient String quantity;

    @SerializedName("is_correction")
    private String IsCorrection;


    /**
     * 0=Submitted by Franchisee
     * 1=Verified by Field Team
     * 2=Approved by RM               (Disable Edit)
     * 3= Send Back for correction by RM
     * 4=Resubmitted by franchisee
     * 5=Reverified by Field Team
     * 6=Onhold by RM                  (Disable Edit)
     */

    @SerializedName("activity_status")
    private String status;

    @SerializedName("completed")
    private String completed;

    @SerializedName("branding_element_type")
    private String TYPE;

    @SerializedName("remarks")
    private String remarks;

    @SerializedName("vl_remarks")
    private String vlRemarks;

    @SerializedName("element_length")
    private String length;

    @SerializedName("element_width")
    private String width;

    public WIPCheckListDto(){
        statusList = new ArrayList<WIPStatusDto>();
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getSubElementName() {
        return subElementName;
    }

    public void setSubElementName(String subElementName) {
        this.subElementName = subElementName;
    }

    public String getElementImageBase64() {
        return elementImageBase64;
    }

    public void setElementImageBase64(String elementImageBase64) {
        this.elementImageBase64 = elementImageBase64;
    }

    public boolean isChangedPhoto() {
        return IsChangedPhoto;
    }

    public void setChangedPhoto(boolean changedPhoto) {
        IsChangedPhoto = changedPhoto;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Bitmap getElementImgBitmap() {
        return elementImgBitmap;
    }

    public void setElementImgBitmap(Bitmap elementImgBitmap) {
        this.elementImgBitmap = elementImgBitmap;
    }

    public Uri getElementUri() {
        return elementUri;
    }

    public void setElementUri(Uri elementUri) {
        this.elementUri = elementUri;
    }

    public String getCapturedDateTime() {
        return capturedDateTime;
    }

    public void setCapturedDateTime(String capturedDateTime) {
        this.capturedDateTime = capturedDateTime;
    }

    public String getElementImgRemarks() {
        return elementImgRemarks;
    }

    public void setElementImgRemarks(String elementImgRemarks) {
        this.elementImgRemarks = elementImgRemarks;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getVakrangee_kendra_design_verification_id() {
        return vakrangee_kendra_design_verification_id;
    }

    public void setVakrangee_kendra_design_verification_id(String vakrangee_kendra_design_verification_id) {
        this.vakrangee_kendra_design_verification_id = vakrangee_kendra_design_verification_id;
    }

    public String getVakrangee_kendra_branding_verification_id() {
        return vakrangee_kendra_branding_verification_id;
    }

    public void setVakrangee_kendra_branding_verification_id(String vakrangee_kendra_branding_verification_id) {
        this.vakrangee_kendra_branding_verification_id = vakrangee_kendra_branding_verification_id;
    }

    public String getIsCorrection() {
        return IsCorrection;
    }

    public void setIsCorrection(String isCorrection) {
        IsCorrection = isCorrection;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getVlRemarks() {
        return vlRemarks;
    }

    public void setVlRemarks(String vlRemarks) {
        this.vlRemarks = vlRemarks;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
