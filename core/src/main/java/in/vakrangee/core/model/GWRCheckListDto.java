package in.vakrangee.core.model;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GWRCheckListDto implements Serializable {

    //region For Element Image
    private transient boolean IsChangedPhoto;

    @SerializedName("element_detail_image")
    private String elementImageBase64;

    @SerializedName("guinness_activity_id")
    private String guinness_activity_id;

    @SerializedName("activity_type")
    private String activity_type;

    @SerializedName("guinness_activity_verification_id")
    private String guinness_activity_verification_id;

    @SerializedName("guinness_activity_verification_history_id")
    private String guinness_activity_verification_history_id;

    private transient String capturedDateTime;

    private transient String elementImgRemarks;
    private transient Uri elementUri;
    private transient Bitmap elementImgBitmap;
    //endregion

    @SerializedName("element_detail_id")
    private String Id;

    @SerializedName("guinness_activity_image_id")
    private String image_id;

    @SerializedName("element_name")
    private String elementName;

    @SerializedName(value = "element_descriptions", alternate = "element_description")
    private String desc;

    @SerializedName("mandatory")
    private String mandatory;

    private transient String subElementName;
    private transient String size;
    private transient String quantity;
    @SerializedName("remarks")
    private String remarks;

    @SerializedName("is_correction")
    private String IsCorrection;

    @SerializedName("vl_remarks")
    private String vlRemarks;

    /**
     * 0=Submitted 1=Approved 2=Sent back for correction 3=Resubmitted
     *
     * Editable in case of 0, 2, 3 Not Editable in case of 1
     */
    @SerializedName("activity_status")
    private String status;

    @SerializedName("element_length")
    private String length;

    @SerializedName("element_width")
    private String width;

    @SerializedName("completed")
    private String completed;

    @SerializedName("Is")
    private String Is;

    @SerializedName("branding_element_type")
    private String TYPE;

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getIsCorrection() {
        return IsCorrection;
    }

    public void setIsCorrection(String isCorrection) {
        IsCorrection = isCorrection;
    }

    public String getVlRemarks() {
        return vlRemarks;
    }

    public void setVlRemarks(String vlRemarks) {
        this.vlRemarks = vlRemarks;
    }

    public boolean isIsChangedPhoto() {
        return IsChangedPhoto;
    }

    public void setIsChangedPhoto(boolean IsChangedPhoto) {
        this.IsChangedPhoto = IsChangedPhoto;
    }

    public String getGuinness_activity_id() {
        return guinness_activity_id;
    }

    public void setGuinness_activity_id(String guinness_activity_id) {
        this.guinness_activity_id = guinness_activity_id;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public String getGuinness_activity_verification_id() {
        return guinness_activity_verification_id;
    }

    public void setGuinness_activity_verification_id(String guinness_activity_verification_id) {
        this.guinness_activity_verification_id = guinness_activity_verification_id;
    }

    public String getGuinness_activity_verification_history_id() {
        return guinness_activity_verification_history_id;
    }

    public void setGuinness_activity_verification_history_id(String guinness_activity_verification_history_id) {
        this.guinness_activity_verification_history_id = guinness_activity_verification_history_id;
    }

    public Uri getElementUri() {
        return elementUri;
    }

    public void setElementUri(Uri elementUri) {
        this.elementUri = elementUri;
    }

    public Bitmap getElementImgBitmap() {
        return elementImgBitmap;
    }

    public void setElementImgBitmap(Bitmap elementImgBitmap) {
        this.elementImgBitmap = elementImgBitmap;
    }
}
