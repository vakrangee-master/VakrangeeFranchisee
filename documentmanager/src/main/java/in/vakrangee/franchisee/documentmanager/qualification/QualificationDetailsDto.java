package in.vakrangee.franchisee.documentmanager.qualification;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QualificationDetailsDto implements Serializable {

    @SerializedName("qualification_id")
    private String qualificationId;

    @SerializedName("institute_name")
    private String instituteName;

    @SerializedName("year_of_passing")
    private String yearOfPassing;

    @SerializedName("qualification_scan_copy_id")
    private String qualificationScanId;

    @SerializedName("qualification_scan_base64")
    private String qualificationScanBase64;

    @SerializedName("qualification_img_ext")
    private String qualificationScanExt;

    @SerializedName("IsEditable")
    private String IsEditable;

    private transient Bitmap bitmap;
    private transient android.net.Uri Uri;

    private transient boolean IsChangedPhoto;

    public String getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(String qualificationId) {
        this.qualificationId = qualificationId;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getYearOfPassing() {
        return yearOfPassing;
    }

    public void setYearOfPassing(String yearOfPassing) {
        this.yearOfPassing = yearOfPassing;
    }

    public String getQualificationScanId() {
        return qualificationScanId;
    }

    public void setQualificationScanId(String qualificationScanId) {
        this.qualificationScanId = qualificationScanId;
    }

    public String getQualificationScanBase64() {
        return qualificationScanBase64;
    }

    public void setQualificationScanBase64(String qualificationScanBase64) {
        this.qualificationScanBase64 = qualificationScanBase64;
    }

    public String getQualificationScanExt() {
        return qualificationScanExt;
    }

    public void setQualificationScanExt(String qualificationScanExt) {
        this.qualificationScanExt = qualificationScanExt;
    }

    public String getIsEditable() {
        return IsEditable;
    }

    public void setIsEditable(String isEditable) {
        IsEditable = isEditable;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public android.net.Uri getUri() {
        return Uri;
    }

    public void setUri(android.net.Uri uri) {
        Uri = uri;
    }

    public boolean isChangedPhoto() {
        return IsChangedPhoto;
    }

    public void setChangedPhoto(boolean changedPhoto) {
        IsChangedPhoto = changedPhoto;
    }
}
