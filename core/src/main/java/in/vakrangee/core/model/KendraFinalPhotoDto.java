package in.vakrangee.core.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KendraFinalPhotoDto implements Serializable {

    @SerializedName("nextgen_photo_type_master_id")
    private String nextgen_photo_type_master_id;
    @SerializedName("nextgen_photo_type_name")
    private String nextgen_photo_type_name;
    @SerializedName("nextgen_kendra_final_photo_id")
    private String nextgen_kendra_final_photo_id;
    @SerializedName("nextgen_franchisee_application_id")
    private String nextgen_franchisee_application_id;
    @SerializedName("vkid")
    private String vkid;
    @SerializedName("photo_id")
    private String photo_id;
    @SerializedName("photo")
    private String photo;

    public String getNextgen_photo_type_master_id() {
        return nextgen_photo_type_master_id;
    }

    public void setNextgen_photo_type_master_id(String nextgen_photo_type_master_id) {
        this.nextgen_photo_type_master_id = nextgen_photo_type_master_id;
    }

    public String getNextgen_photo_type_name() {
        return nextgen_photo_type_name;
    }

    public void setNextgen_photo_type_name(String nextgen_photo_type_name) {
        this.nextgen_photo_type_name = nextgen_photo_type_name;
    }

    public String getNextgen_kendra_final_photo_id() {
        return nextgen_kendra_final_photo_id;
    }

    public void setNextgen_kendra_final_photo_id(String nextgen_kendra_final_photo_id) {
        this.nextgen_kendra_final_photo_id = nextgen_kendra_final_photo_id;
    }

    public String getNextgen_franchisee_application_id() {
        return nextgen_franchisee_application_id;
    }

    public void setNextgen_franchisee_application_id(String nextgen_franchisee_application_id) {
        this.nextgen_franchisee_application_id = nextgen_franchisee_application_id;
    }

    public String getVkid() {
        return vkid;
    }

    public void setVkid(String vkid) {
        this.vkid = vkid;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
