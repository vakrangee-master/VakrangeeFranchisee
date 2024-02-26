package in.vakrangee.franchisee.documentmanager;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShopEstablishmentCertificateDto implements Serializable {

    @SerializedName("shop_est_certificate_no")
    private String shop_est_certificate_no;

    @SerializedName("shop_est_issuing_authority")
    private String shop_est_issuing_authority;

    @SerializedName("state_id")
    private String state_id;

    @SerializedName("district_id")
    private String district_id;

    @SerializedName("vtc")
    private String vtc;

    @SerializedName("name_on_shop_est_certificate")
    private String nameOnshopEstCertificate;

    @SerializedName("shop_est_issuing_date")
    private String shopEstIssuingDate;

    @SerializedName("shop_est_valid_from_date")
    private String shopEstValidFromDate;

    @SerializedName("shop_est_expiry_date")
    private String shopEstExpiryDate;

    //--START Image
    @SerializedName("shop_est_certificate_scan_copy_id")
    private String shopEstCertificateScanId;

    @SerializedName("shop_est_certificate_scan_base64")
    private String shopEstCertificateScanBase64;

    @SerializedName("shop_est_certificate_img_ext")
    private String shopEstCertificateScanExt;
    //-- END Image

    @SerializedName("IsEditable")
    private String IsEditable;

    private transient Bitmap bitmap;
    private transient android.net.Uri Uri;

    private transient boolean IsChangedPhoto;

    public String getNameOnShopEstCertificate() {
        return nameOnshopEstCertificate;
    }

    public void setNameOnShopEstCertificate(String nameOnshopEstCertificate) {
        this.nameOnshopEstCertificate = nameOnshopEstCertificate;
    }

    public String getShopEstIssuingDate() {
        return shopEstIssuingDate;
    }

    public void setShopEstIssuingDate(String shopEstIssuingDate) {
        this.shopEstIssuingDate = shopEstIssuingDate;
    }

    public String getShopEstValidFromDate() {
        return shopEstValidFromDate;
    }

    public void setShopEstValidFromDate(String shopEstValidFromDate) {
        this.shopEstValidFromDate = shopEstValidFromDate;
    }

    public String getShopEstExpiryDate() {
        return shopEstExpiryDate;
    }

    public void setShopEstExpiryDate(String shopEstExpiryDate) {
        this.shopEstExpiryDate = shopEstExpiryDate;
    }

    public String getShopEstCertificateScanId() {
        return shopEstCertificateScanId;
    }

    public void setShopEstCertificateScanId(String shopEstCertificateScanId) {
        this.shopEstCertificateScanId = shopEstCertificateScanId;
    }

    public String getShopEstCertificateScanBase64() {
        return shopEstCertificateScanBase64;
    }

    public void setShopEstCertificateScanBase64(String shopEstCertificateScanBase64) {
        this.shopEstCertificateScanBase64 = shopEstCertificateScanBase64;
    }

    public String getShopEstCertificateScanExt() {
        return shopEstCertificateScanExt;
    }

    public void setShopEstCertificateScanExt(String shopEstCertificateScanExt) {
        this.shopEstCertificateScanExt = shopEstCertificateScanExt;
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

    public String getShop_est_certificate_no() {
        return shop_est_certificate_no;
    }

    public void setShop_est_certificate_no(String shop_est_certificate_no) {
        this.shop_est_certificate_no = shop_est_certificate_no;
    }

    public String getShop_est_issuing_authority() {
        return shop_est_issuing_authority;
    }

    public void setShop_est_issuing_authority(String shop_est_issuing_authority) {
        this.shop_est_issuing_authority = shop_est_issuing_authority;
    }

    public String getState() {
        return state_id;
    }

    public void setState(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict() {
        return district_id;
    }

    public void setDistrict(String district_id) {
        this.district_id = district_id;
    }

    public String getVtc() {
        return vtc;
    }

    public void setVtc(String vtc) {
        this.vtc = vtc;
    }
}
