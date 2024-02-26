package in.vakrangee.franchisee.documentmanager;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class
PANCardDetailsDto implements Serializable {

    @SerializedName("name_as_pan_card")
    private String nameAsPanCard;

    @SerializedName("pan_number")
    private String panNumber;

    @SerializedName("pan_card_scan_copy_id")
    private String panCardScanId;

    @SerializedName("pan_card_scan_base64")
    private String panCardScanBase64;

    @SerializedName("pan_card_img_ext")
    private String panCardScanExt;

    @SerializedName("IsEditable")
    private String IsEditable;

    private transient Bitmap bitmap;
    private transient android.net.Uri Uri;

    private transient boolean IsChangedPhoto;

    public String getNameAsPanCard() {
        return nameAsPanCard;
    }

    public void setNameAsPanCard(String nameAsPanCard) {
        this.nameAsPanCard = nameAsPanCard;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getPanCardScanId() {
        return panCardScanId;
    }

    public void setPanCardScanId(String panCardScanId) {
        this.panCardScanId = panCardScanId;
    }

    public String getPanCardScanBase64() {
        return panCardScanBase64;
    }

    public void setPanCardScanBase64(String panCardScanBase64) {
        this.panCardScanBase64 = panCardScanBase64;
    }

    public String getPanCardScanExt() {
        return panCardScanExt;
    }

    public void setPanCardScanExt(String panCardScanExt) {
        this.panCardScanExt = panCardScanExt;
    }

    public String getIsEditable() {
        return IsEditable;
    }

    public void setIsEditable(String isEditable) {
        IsEditable = isEditable;
    }

    public boolean isChangedPhoto() {
        return IsChangedPhoto;
    }

    public void setChangedPhoto(boolean changedPhoto) {
        IsChangedPhoto = changedPhoto;
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
}
