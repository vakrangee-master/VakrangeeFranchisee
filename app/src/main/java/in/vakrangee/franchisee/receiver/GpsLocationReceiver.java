package in.vakrangee.franchisee.receiver;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.event.Channel;

import in.vakrangee.supercore.franchisee.utils.network.EventData;

public class GpsLocationReceiver extends BroadcastReceiver {

    private final static String TAG = GpsLocationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {

            Log.e(TAG, "Location Provider Changed.");

            ContentResolver contentResolver = context.getContentResolver();
            // Find out what the settings say about which providers are enabled
            int mode = Settings.Secure.getInt(
                    contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);

            if (mode == Settings.Secure.LOCATION_MODE_OFF) {
                Log.e(TAG, "GPS Location Provided is Off");
                EventData event = new EventData("PROVIDERS_CHANGED", "GPS is not enable.");
                NYBus.get().post(event, Channel.THREE);
            }

//            String locationMode = "UNKNOWN";
//            if (mode != Settings.Secure.LOCATION_MODE_OFF) {
//                if (mode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY) {
//                    locationMode = "High accuracy. Uses GPS, Wi-Fi, and mobile networks to determine location";
//                } else if (mode == Settings.Secure.LOCATION_MODE_SENSORS_ONLY) {
//                    locationMode = "Device only. Uses GPS to determine location";
//                } else if (mode == Settings.Secure.LOCATION_MODE_BATTERY_SAVING) {
//                    locationMode = "Battery saving. Uses Wi-Fi and mobile networks to determine location";
//                }
//            }
//
//            Log.e(TAG, "Location Mode: "+locationMode);
        }
    }
}