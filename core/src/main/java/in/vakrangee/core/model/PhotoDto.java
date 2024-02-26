package in.vakrangee.core.model;

import android.graphics.Bitmap;
import android.net.Uri;

public class PhotoDto {
    private int id;
    private String name;
    private String photoId;
    private String imageBase64;
    private Bitmap bitmap;
    private String remarks;
    private String capturedDateTime;
    private Uri uri;
    private boolean IsChangedPhoto;

    public PhotoDto(){}

    public PhotoDto(int id, String name, String imageBase64) {
        this.id = id;
        this.name = name;
        this.imageBase64 = imageBase64;
    }

    public PhotoDto(int id, String name, Bitmap bitmap) {
        this.id = id;
        this.name = name;
        this.bitmap = bitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
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

    public String getCapturedDateTime() {
        return capturedDateTime;
    }

    public void setCapturedDateTime(String capturedDateTime) {
        this.capturedDateTime = capturedDateTime;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public boolean isChangedPhoto() {
        return IsChangedPhoto;
    }

    public void setChangedPhoto(boolean changedPhoto) {
        IsChangedPhoto = changedPhoto;
    }

}
