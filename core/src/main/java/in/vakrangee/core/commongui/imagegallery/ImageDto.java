package in.vakrangee.core.commongui.imagegallery;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageDto implements Serializable {

    @SerializedName("id")
    private transient String Id;

    @SerializedName("nextgen_site_work_wip_status_id")
    private String nextgen_site_work_wip_status_id;

    @SerializedName("nextgen_site_work_wip_images_id")
    private String nextgen_site_work_wip_images_id;

    @SerializedName(value = "imageBase64", alternate = {"nextgen_site_work_wip_image"})
    private String imageBase64;

    private String Name;

    @SerializedName("branding_element_id")
    private String branding_element_Id;

    @SerializedName("branding_element_name")
    private String branding_element_name;

    private transient Uri Uri;
    private transient Bitmap bitmap;

    @SerializedName(value = "capturedDateTime", alternate = {"date_time"})
    private String capturedDateTime;

    @SerializedName("field_force_remarks")
    private String remarks;

    @SerializedName("vl_remarks")
    private String vl_remarks;

    @SerializedName("nextgen_equip_images_id")
    private String nextgen_equip_images_id;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public android.net.Uri getUri() {
        return Uri;
    }

    public void setUri(android.net.Uri uri) {
        Uri = uri;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getCapturedDateTime() {
        return capturedDateTime;
    }

    public void setCapturedDateTime(String capturedDateTime) {
        this.capturedDateTime = capturedDateTime;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNextgen_site_work_wip_status_id() {
        return nextgen_site_work_wip_status_id;
    }

    public void setNextgen_site_work_wip_status_id(String nextgen_site_work_wip_status_id) {
        this.nextgen_site_work_wip_status_id = nextgen_site_work_wip_status_id;
    }

    public String getVl_remarks() {
        return vl_remarks;
    }

    public void setVl_remarks(String vl_remarks) {
        this.vl_remarks = vl_remarks;
    }

    public String getNextgen_site_work_wip_images_id() {
        return nextgen_site_work_wip_images_id;
    }

    public void setNextgen_site_work_wip_images_id(String nextgen_site_work_wip_images_id) {
        this.nextgen_site_work_wip_images_id = nextgen_site_work_wip_images_id;
    }

    public String getBranding_element_Id() {
        return branding_element_Id;
    }

    public void setBranding_element_Id(String branding_element_Id) {
        this.branding_element_Id = branding_element_Id;
    }

    public String getBranding_element_name() {
        return branding_element_name;
    }

    public void setBranding_element_name(String branding_element_name) {
        this.branding_element_name = branding_element_name;
    }

    public String getNextgen_equip_images_id() {
        return nextgen_equip_images_id;
    }

    public void setNextgen_equip_images_id(String nextgen_equip_images_id) {
        this.nextgen_equip_images_id = nextgen_equip_images_id;
    }
}
