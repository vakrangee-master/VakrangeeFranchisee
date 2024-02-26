package in.vakrangee.franchisee.sitelayout.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.vakrangee.supercore.franchisee.model.SiteReadinessCheckListDto;

public class SiteReadinessCategoryDto implements Serializable {

    private transient boolean IsChangedPhoto;

    @SerializedName("element_id")
    private String Id;

    @SerializedName("element_name")
    private String name;

    @SerializedName("element_description")
    private String desc;

    /**
     * Status
     * 0 - Default
     * 1 - Send back for Correction
     * 2 - Approved
     */
    @SerializedName("element_status")
    private String status;

    @SerializedName("sub_cat")
    public List<SiteReadinessCheckListDto> checkList;

    public SiteReadinessCategoryDto() {
        checkList = new ArrayList<SiteReadinessCheckListDto>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public boolean isChangedPhoto() {
        return IsChangedPhoto;
    }

    public void setChangedPhoto(boolean changedPhoto) {
        IsChangedPhoto = changedPhoto;
    }

    //changes Robin
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    private String photo;

}
