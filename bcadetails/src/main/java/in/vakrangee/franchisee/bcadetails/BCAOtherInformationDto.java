package in.vakrangee.franchisee.bcadetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BCAOtherInformationDto implements Serializable {

    @SerializedName("supervisor_code")
    private String supervisorCode;

    @SerializedName("supervisor_name")
    private String supervisorName;

    @SerializedName("supervisor_email_id")
    private String supervisorEmailID;

    @SerializedName("supervisor_mobile_number")
    private String supervisorMobileNo;

    @SerializedName("device_type")
    private String deviceType;

    @SerializedName("device_serial_number")
    private String deviceSerialNo;

    public String getSupervisorCode() {
        return supervisorCode;
    }

    public void setSupervisorCode(String supervisorCode) {
        this.supervisorCode = supervisorCode;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getSupervisorEmailID() {
        return supervisorEmailID;
    }

    public void setSupervisorEmailID(String supervisorEmailID) {
        this.supervisorEmailID = supervisorEmailID;
    }

    public String getSupervisorMobileNo() {
        return supervisorMobileNo;
    }

    public void setSupervisorMobileNo(String supervisorMobileNo) {
        this.supervisorMobileNo = supervisorMobileNo;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceSerialNo() {
        return deviceSerialNo;
    }

    public void setDeviceSerialNo(String deviceSerialNo) {
        this.deviceSerialNo = deviceSerialNo;
    }
}
