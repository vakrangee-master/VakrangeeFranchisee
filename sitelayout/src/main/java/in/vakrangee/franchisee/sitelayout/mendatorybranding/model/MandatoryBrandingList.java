package in.vakrangee.franchisee.sitelayout.mendatorybranding.model;

import com.jsoniter.annotation.JsonProperty;

public class MandatoryBrandingList{
    @JsonProperty("nextgen_site_work_id")
    public int nextgen_site_work_id;
    @JsonProperty("kendra_category_name")
    public String kendra_category_name;
    @JsonProperty("kendra_sub_category_mandatory")
    public String kendra_sub_category_mandatory;
    @JsonProperty("nextgen_site_work_name")
    public String nextgen_site_work_name;
    @JsonProperty("nextgen_site_work_kendra_category_id")
    public int nextgen_site_work_kendra_category_id;
    @JsonProperty("kendra_sub_category_name")
    public String kendra_sub_category_name;
    @JsonProperty("nextgen_site_work_kendra_sub_category_id")
    public int nextgen_site_work_kendra_sub_category_id;
    public int categoryId;
    @JsonProperty("status")
    public String status = "0";


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked;
    public String imageByte;
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageByte() {
        return imageByte;
    }

    public void setImageByte(String imageByte) {
        this.imageByte = imageByte;
    }



    public int getNextgen_site_work_id() {
        return nextgen_site_work_id;
    }

    public void setNextgen_site_work_id(int nextgen_site_work_id) {
        this.nextgen_site_work_id = nextgen_site_work_id;
    }

    public String getKendra_category_name() {
        return kendra_category_name;
    }

    public void setKendra_category_name(String kendra_category_name) {
        this.kendra_category_name = kendra_category_name;
    }

    public String getKendra_sub_category_mandatory() {
        return kendra_sub_category_mandatory;
    }

    public void setKendra_sub_category_mandatory(String kendra_sub_category_mandatory) {
        this.kendra_sub_category_mandatory = kendra_sub_category_mandatory;
    }

    public String getNextgen_site_work_name() {
        return nextgen_site_work_name;
    }

    public void setNextgen_site_work_name(String nextgen_site_work_name) {
        this.nextgen_site_work_name = nextgen_site_work_name;
    }

    public int getNextgen_site_work_kendra_category_id() {
        return nextgen_site_work_kendra_category_id;
    }

    public void setNextgen_site_work_kendra_category_id(int nextgen_site_work_kendra_category_id) {
        this.nextgen_site_work_kendra_category_id = nextgen_site_work_kendra_category_id;
    }

    public String getKendra_sub_category_name() {
        return kendra_sub_category_name;
    }

    public void setKendra_sub_category_name(String kendra_sub_category_name) {
        this.kendra_sub_category_name = kendra_sub_category_name;
    }

    public int getNextgen_site_work_kendra_sub_category_id() {
        return nextgen_site_work_kendra_sub_category_id;
    }

    public void setNextgen_site_work_kendra_sub_category_id(int nextgen_site_work_kendra_sub_category_id) {
        this.nextgen_site_work_kendra_sub_category_id = nextgen_site_work_kendra_sub_category_id;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}