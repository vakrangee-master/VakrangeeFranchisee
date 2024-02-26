package in.vakrangee.franchisee.sitelayout.mendatorybranding.model;

import com.jsoniter.annotation.JsonProperty;

import java.util.ArrayList;

public class ResponseDTO{
    @JsonProperty("Status")
    public String status;
    @JsonProperty("List")
    public ArrayList<MandatoryBrandingList> list;
    public ArrayList<MandatoryImageList> imagelist;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<MandatoryBrandingList> getList() {
        return list;
    }

    public void setList(ArrayList<MandatoryBrandingList> list) {
        this.list = list;
    }

    public ArrayList<MandatoryImageList> getImageList() {
        return imagelist;
    }

    public void setImagelistList(ArrayList<MandatoryImageList> imagelist) {
        this.imagelist = imagelist;
    }
}