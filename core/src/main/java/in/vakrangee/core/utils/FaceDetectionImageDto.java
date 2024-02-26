package in.vakrangee.core.utils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FaceDetectionImageDto implements Serializable {
    @SerializedName("ext")
    private String ext;
    @SerializedName(value = "image_base64")
    private String image_base64;
    @SerializedName("id")
    private String id;


    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getImage_base64() {
        return image_base64;
    }

    public void setImage_base64(String image_base64) {
        this.image_base64 = image_base64;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
