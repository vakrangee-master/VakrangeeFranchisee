package in.vakrangee.core.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.core.app.ActivityCompat;

/**
 * Created by Nileshd on 3/8/2017.
 */
public class GPSTrackerOld extends Service implements LocationListener {

    private static final String TAG = GPSTrackerOld.class.getCanonicalName();

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    long time;      // Get Time
    String strDateTime;
    private float accuracy = 0.0f;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTrackerOld(Context context) {
        this.mContext = context;
        getLocation();
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            time = location.getTime();
                            accuracy = location.getAccuracy();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            if (ActivityCompat.checkSelfPermission((Activity) mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission((Activity) mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return location;
                            }
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                time = location.getTime();
                                accuracy = location.getAccuracy();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission((Activity) mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission((Activity) mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(GPSTrackerOld.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to get UTC Time in milliseconds
     */
    public long getTime() {

        if (location != null) {
            time = location.getTime();
        }

        // return time
        return time;
    }

    /**
     * Function to get Formatted Date Time
     */
    public String getFormattedDateTime() {

        if (location != null) {
            time = location.getTime();

            //SimpleDateFormat sdf = new SimpleDateFormat();
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

            TimeZone tz = TimeZone.getDefault();
            Log.d(TAG, "TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezone id :: " + tz.getID());
            dateFormatter.setTimeZone(TimeZone.getTimeZone(tz.getDisplayName(false, TimeZone.SHORT))); //"UTC", "GMT+05:30"
            strDateTime = dateFormatter.format(new Date(time));
            Log.d(TAG, "GMT+5:30 Formatted DateTime: " + strDateTime);
        }

        return strDateTime;
    }

    /**
     * Get Distance in meters.
     *
     * @param startLatitude
     * @param startLongitude
     * @return
     */
    public float getDistance(double startLatitude, double startLongitude) {
        Location loc1 = new Location("");
        loc1.setLatitude(startLatitude);
        loc1.setLongitude(startLongitude);

        Location loc2 = new Location("");
        loc2.setLatitude(latitude);
        loc2.setLongitude(longitude);
        float distanceInMeters = loc1.distanceTo(loc2);

        Log.d(TAG, "Testing: Franchisee Location: Lat: " + startLatitude + " Long: " + startLongitude);
        Log.d(TAG, "Testing: Current Location: Lat: " + latitude + " Long: " + longitude + " Distance(m): " + distanceInMeters);
        return distanceInMeters;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing circle_cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            time = location.getTime();
            accuracy = location.getAccuracy();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public long getDateTime() {
        long LocationTime = 0;

        /*
        location = getLocation();
        if (location != null) {
            currentTimestamp = location.getTime();
        }
        return currentTimestamp;*/
       /* location = getLocation();
        if (location != null)
            Log.d(TAG, "Testing: Actual location: " + location.getTime());*/

        LocationManager locMan = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
        locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);

        if (locationManager != null) {
            Location locationGPS = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNet = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            long GPSLocationTime = 0;
            if (null != locationGPS) {
                GPSLocationTime = locationGPS.getTime();
            }

            long NetLocationTime = 0;
            if (null != locationNet) {
                NetLocationTime = locationNet.getTime();
            }

            if (0 < GPSLocationTime - NetLocationTime) {
                LocationTime = locationGPS.getTime();
                return LocationTime;
            } else {
                LocationTime = locationNet.getTime();
                return LocationTime;
            }
        }
        return LocationTime;
    }

    public static String getLongitudeDegree(Double longitude) {

        if (longitude == null) return "0/1,0/1,0/1000";
        // You can adapt this to latitude very easily by passing location.getLatitude()
        String[] degMinSec = Location.convert(longitude, Location.FORMAT_SECONDS).split(":");
        return degMinSec[0] + "/1," + degMinSec[1] + "/1," + degMinSec[2] + "/1000";
    }

    public boolean IsGPSEnabled() {
        if (locationManager == null)
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return isGPSEnabled;
    }

    public float getLocationAccuracy() {
        return accuracy;
    }
}
