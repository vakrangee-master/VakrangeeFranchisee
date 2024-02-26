package in.vakrangee.franchisee.networktesting.speedtest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InternetSpeedDetailsDto implements Serializable {

    public InternetSpeedDetailsDto() {
        simDetailsList = new ArrayList<SIMDetailsDto>();
    }

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("end_time")
    private String endTime;

    @SerializedName("wifi_status")
    private boolean wifiStatus;

    @SerializedName("wifi_ssid")
    private String wifiSSID;

    @SerializedName("wifi_mac_address")
    private String wifiMacAddress;

    @SerializedName("preferred_sim_internet")
    private String preferredSIMInternet;

    @SerializedName("ping_ms")
    private String pingMs;

    @SerializedName("upload_speed")
    private String uploadSpeed;

    @SerializedName("download_speed")
    private String downloadSpeed;

    @SerializedName("sim_details")
    public List<SIMDetailsDto> simDetailsList;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isWifiStatus() {
        return wifiStatus;
    }

    public void setWifiStatus(boolean wifiStatus) {
        this.wifiStatus = wifiStatus;
    }

    public String getWifiSSID() {
        return wifiSSID;
    }

    public void setWifiSSID(String wifiSSID) {
        this.wifiSSID = wifiSSID;
    }

    public String getWifiMacAddress() {
        return wifiMacAddress;
    }

    public void setWifiMacAddress(String wifiMacAddress) {
        this.wifiMacAddress = wifiMacAddress;
    }

    public String getPreferredSIMInternet() {
        return preferredSIMInternet;
    }

    public void setPreferredSIMInternet(String preferredSIMInternet) {
        this.preferredSIMInternet = preferredSIMInternet;
    }

    public String getPingMs() {
        return pingMs;
    }

    public void setPingMs(String pingMs) {
        this.pingMs = pingMs;
    }

    public String getUploadSpeed() {
        return uploadSpeed;
    }

    public void setUploadSpeed(String uploadSpeed) {
        this.uploadSpeed = uploadSpeed;
    }

    public String getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(String downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

}
