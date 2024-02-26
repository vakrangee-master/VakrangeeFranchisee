package in.vakrangee.franchisee.workinprogress;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import in.vakrangee.supercore.franchisee.commongui.imagegallery.ImageDto;

public class WIPStatusDto implements Serializable {

    @SerializedName("date")
    private String date;

    @SerializedName("status")
    private String status;

    @SerializedName("photos")
    private String photosCount;

    public List<ImageDto> imagesList;

    public WIPStatusDto() {
        this.imagesList = imagesList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhotosCount() {
        return photosCount;
    }

    public void setPhotosCount(String photosCount) {
        this.photosCount = photosCount;
    }
}
