package in.vakrangee.franchisee.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class UpdateUserLatLngService extends Service {

    private static final String TAG = "UpdateUserLatLngService";
    private GPSTracker gpsTracker;
    private boolean isAdhoc = false;
    private Timer timer = new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int INTERVAL = intent.getIntExtra("INTERVAL", 0);
        if (INTERVAL > 0) {
            timer.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {
                    updateUserLocation();
                }
            }, 0, INTERVAL);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gpsTracker = new GPSTracker(this);

        // Get App MODE
        isAdhoc = Constants.ENABLE_ADHOC_MODE || Constants.ENABLE_FRANCHISEE_MODE || Constants.ENABLE_FRANCHISEE_LOGIN;
    }

    public void updateUserLocation() {

        if (!gpsTracker.canGetLocation())
            return;

        //STEP 1: Get VKId
        Connection connection = new Connection(this);
        String Id = null;
        String vkId = connection.getVkid();
        Id = vkId;
        if (TextUtils.isEmpty(vkId)) {
            Log.e(TAG, "[VKId is null].");
            Id = CommonUtils.getEnquiryId(this);
            //return;
        }

        //STEP 2: Get Latitude
        Double latitude = gpsTracker.getLatitude();
        if (latitude == null) {
            Log.e(TAG, "[Latitude is null].");
            return;
        }

        //STEP 3: Get Longitude
        Double longitude = gpsTracker.getLongitude();
        if (longitude == null) {
            Log.e(TAG, "[Longitude is null].");
            return;
        }

        //Service Call
        String lat = String.valueOf(latitude);
        String longi = String.valueOf(longitude);

        String response = null;
        if (isAdhoc) {
            Log.e(TAG, "updateUserLatLong Called.");
            response = WebService.updateUserLatLong(Id, lat, longi);
            Log.e(TAG, "UpdateUserLatLong Response: " + response);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();  // Terminates this timer, discarding any currently scheduled tasks.
            timer.purge();
        }
        stopSelf();
    }
}
