package in.vakrangee.core.model;

import android.content.Context;

/**
 * Created by Nileshd on 1/31/2017.
 */
public class LocationKendraDataModel {

    Context context;

    String id;
    String type;
    String scope;
    String statusNo;
    String status;
    String mobileNo;
    String emailId;
    String fUploadedCount;
    String vkId;
    String latitude;
    String longitude;
    String kendraRange;

    public LocationKendraDataModel(String id, String type, String scope){
        this.id = id;
        this.type = type;
        this.scope = scope;
    }

    public LocationKendraDataModel(String id, String type, String scope,String status, String mobileNo,String emailId, String statusNo, String fUploadedCount, String vkId, String latitude, String longitude, String kendraRange) {
        this.id = id;
        this.type = type;
        this.scope = scope;
        this.statusNo = statusNo;
        this.status = status;
        this.mobileNo = mobileNo;
        this.emailId = emailId;
        this.fUploadedCount = fUploadedCount;
        this.vkId = vkId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.kendraRange = kendraRange;
    }

    public LocationKendraDataModel(Context context) {
        this.context = context;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getStatusNo() {
        return statusNo;
    }

    public void setStatusNo(String statusNo) {
        this.statusNo = statusNo;
    }

    public String getfUploadedCount() {
        return fUploadedCount;
    }

    public void setfUploadedCount(String fUploadedCount) {
        this.fUploadedCount = fUploadedCount;
    }

    public String getVkId() {
        return vkId;
    }

    public void setVkId(String vkId) {
        this.vkId = vkId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getKendraRange() {
        return kendraRange;
    }

    public void setKendraRange(String kendraRange) {
        this.kendraRange = kendraRange;
    }
}
