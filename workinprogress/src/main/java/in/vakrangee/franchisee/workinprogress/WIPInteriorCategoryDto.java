package in.vakrangee.franchisee.workinprogress;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WIPInteriorCategoryDto implements Serializable {

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
    public List<WIPCheckListDto> checkList;

    public WIPInteriorCategoryDto() {
        checkList = new ArrayList<WIPCheckListDto>();
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
}
