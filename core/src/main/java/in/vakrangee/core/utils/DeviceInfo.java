package in.vakrangee.core.utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;

public class DeviceInfo {

    private static final String TAG = "DeviceInfo";
    private static DeviceInfo deviceInfo;
    private static TelephonyManager telephonyManager;

    // Device Info
    private static String imei, deviceId, simNo;

    private DeviceInfo() {}

    public static DeviceInfo getInstance(@NonNull  Context context) {

        if(deviceInfo == null) {

            deviceInfo = new DeviceInfo();
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = EncryptionUtil.encryptString(telephonyManager.getDeviceId(), context);
            deviceId = EncryptionUtil.encryptString(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID), context);
            simNo = EncryptionUtil.encryptString(telephonyManager.getSimSerialNumber(), context);
        }

        return deviceInfo;
    }

    public String getIMEI() {
        return imei;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getSimNo() {
        return simNo;
    }

}