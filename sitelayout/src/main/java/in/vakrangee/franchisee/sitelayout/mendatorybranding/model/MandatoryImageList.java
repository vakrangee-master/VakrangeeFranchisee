package in.vakrangee.franchisee.sitelayout.mendatorybranding.model;

import com.jsoniter.annotation.JsonProperty;

import java.util.ArrayList;

public class MandatoryImageList {

    @JsonProperty("imgBytes")
    public String imgBytes;
    @JsonProperty("categoryId")
    public String categoryId;
    @JsonProperty("categoryName")
    public String categoryName;
    @JsonProperty("subCategoryName")
    public String subCategoryName;
    @JsonProperty("status")
    public String status = "0";
    public ArrayList<MandatoryImageList> imagelist;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getimgBytes() {
        return imgBytes;
    }

    public void setimgBytes(String imgBytes) {
        this.imgBytes = imgBytes;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;

    }


    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }
    public ArrayList<MandatoryImageList> getImageList() {
        return imagelist;
    }

    public void setImagelistList(ArrayList<MandatoryImageList> imagelist) {
        this.imagelist = imagelist;
    }

}