package in.vakrangee.franchisee.gwr.attendance;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AttendanceDetailsDto implements Serializable {


    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("vtc")
    private String vtc;

    @SerializedName("district")
    private String district;

    @SerializedName("state")
    private String state;

    @SerializedName("pin_code")
    private String pin_code;

    @SerializedName("organization_name")
    private String organization_name;

    @SerializedName("occupation")
    private String occupation;

    @SerializedName("pic_id")
    private String pic_id;

    @SerializedName("status")
    private String status;

    @SerializedName("attendance_image_id")
    private String attendanceImageId;

    @SerializedName("attendance_id")
    private String attendanceId;

    @SerializedName("attendance_image")
    private String imgAttendBase64;

    @SerializedName("captured_date_time")
    private String capturedDateTime;

    @SerializedName("captured_latitude")
    private String latitude;

    @SerializedName("captured_longitude")
    private String longitude;

    @SerializedName("witness_statement_image_file_ext")
    private String extension;

    @SerializedName("fileName")
    private String fileName;

    @SerializedName("witness_statement_image_id")
    private String witnessStatementImageId;

    @SerializedName("witness_statement_image")
    private String witnessStatementImage;

    @SerializedName("witness_statement_upload_date_time")
    private String witnessStatementUploadDateTime;

    private transient Uri elementUri;
    private transient Bitmap elementImgBitmap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVtc() {
        return vtc;
    }

    public void setVtc(String vtc) {
        this.vtc = vtc;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getPic_id() {
        return pic_id;
    }

    public void setPic_id(String pic_id) {
        this.pic_id = pic_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImgAttendBase64() {
        return imgAttendBase64;
    }

    public void setImgAttendBase64(String imgAttendBase64) {
        this.imgAttendBase64 = imgAttendBase64;
    }

    public String getCapturedDateTime() {
        return capturedDateTime;
    }

    public void setCapturedDateTime(String capturedDateTime) {
        this.capturedDateTime = capturedDateTime;
    }

    public Uri getElementUri() {
        return elementUri;
    }

    public void setElementUri(Uri elementUri) {
        this.elementUri = elementUri;
    }

    public Bitmap getElementImgBitmap() {
        return elementImgBitmap;
    }

    public void setElementImgBitmap(Bitmap elementImgBitmap) {
        this.elementImgBitmap = elementImgBitmap;
    }

    public String getAttendanceImageId() {
        return attendanceImageId;
    }

    public void setAttendanceImageId(String attendanceImageId) {
        this.attendanceImageId = attendanceImageId;
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getWitnessStatementImageId() {
        return witnessStatementImageId;
    }

    public void setWitnessStatementImageId(String witnessStatementImageId) {
        this.witnessStatementImageId = witnessStatementImageId;
    }

    public String getWitnessStatementImage() {
        return witnessStatementImage;
    }

    public void setWitnessStatementImage(String witnessStatementImage) {
        this.witnessStatementImage = witnessStatementImage;
    }

    public String getWitnessStatementUploadDateTime() {
        return witnessStatementUploadDateTime;
    }

    public void setWitnessStatementUploadDateTime(String witnessStatementUploadDateTime) {
        this.witnessStatementUploadDateTime = witnessStatementUploadDateTime;
    }
}
