package in.vakrangee.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GWREventPhotoDto implements Serializable {

    @SerializedName("guinness_event_photo_type_master_id")
    @Expose
    private String guinnessEventPhotoTypeMasterId;
    @SerializedName("guinness_event_photo_type_name")
    @Expose
    private String guinnessEventPhotoTypeName;
    @SerializedName("guinness_event_photo_master_id")
    @Expose
    private String guinnessEventPhotoMasterId;
    @SerializedName("event_photo_id")
    @Expose
    private String eventPhotoId;
    @SerializedName("event_photo")
    @Expose
    private String eventPhoto;
    @SerializedName("status")
    private String statusEventDayPhoto;
    @SerializedName("status_msg")
    private String statusMsg;


    public String getGuinnessEventPhotoTypeMasterId() {
        return guinnessEventPhotoTypeMasterId;
    }

    public void setGuinnessEventPhotoTypeMasterId(String guinnessEventPhotoTypeMasterId) {
        this.guinnessEventPhotoTypeMasterId = guinnessEventPhotoTypeMasterId;
    }

    public String getGuinnessEventPhotoTypeName() {
        return guinnessEventPhotoTypeName;
    }

    public void setGuinnessEventPhotoTypeName(String guinnessEventPhotoTypeName) {
        this.guinnessEventPhotoTypeName = guinnessEventPhotoTypeName;
    }

    public String getGuinnessEventPhotoMasterId() {
        return guinnessEventPhotoMasterId;
    }

    public void setGuinnessEventPhotoMasterId(String guinnessEventPhotoMasterId) {
        this.guinnessEventPhotoMasterId = guinnessEventPhotoMasterId;
    }

    public String getEventPhotoId() {
        return eventPhotoId;
    }

    public void setEventPhotoId(String eventPhotoId) {
        this.eventPhotoId = eventPhotoId;
    }

    public String getEventPhoto() {
        return eventPhoto;
    }

    public void setEventPhoto(String eventPhoto) {
        this.eventPhoto = eventPhoto;
    }

    public String getStatusEventDayPhoto() {
        return statusEventDayPhoto;
    }

    public void setStatusEventDayPhoto(String statusEventDayPhoto) {
        this.statusEventDayPhoto = statusEventDayPhoto;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
