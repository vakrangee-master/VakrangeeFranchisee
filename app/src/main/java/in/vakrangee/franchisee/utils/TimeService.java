package in.vakrangee.franchisee.utils;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.application.VakrangeeKendraApplication;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.team.geofencing.GeofenceErrorMessages;
import in.vakrangee.team.geofencing.GeofenceEventHandler;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nileshd on 6/20/2016.
 */
public class TimeService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        OnCompleteListener<Void> {

    private static final String TAG = "DemoService";
    //private final int INTERVAL = 60 * 1000;           1min
    private final int INTERVAL = 180 * 1000;            // 3mins

    private Timer timer = new Timer();
    Location mLastLocation;

    TextView txtnew, txthelloa;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String latitude, longitude, arrcury, altitude;
    String stringPassedToThisService;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static final String LOGSERVICE = "#######";
    LocationManager locationManager;
    private VakrangeeKendraApplication vkmsApp;
    private static GeofenceEventHandler geofenceEventHandler;
    private String vkId;
    private Connection connection;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        buildGoogleApiClient();
        Log.i(LOGSERVICE, "onCreate");

        if (stringPassedToThisService != null) {

            //  Toast.makeText(this, "Service is created", Toast.LENGTH_SHORT).show();
            Log.e("Service is created", stringPassedToThisService);
            // DO SOMETHING WITH THE STRING PASSED
        }

        // Application and Geofence Event Handler
        connection = new Connection(getApplicationContext());
        vkmsApp = (VakrangeeKendraApplication) getApplication();       // VKMS App
        geofenceEventHandler = new GeofenceEventHandler(TimeService.this, null);
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand");

        // Register Location Receiver to Send Location to Server.
       /* receiver = new LocationReceiver();
        registerReceiver(receiver, prepareIntentFilter());*/

        if (intent != null) {
            vkId = intent.getStringExtra("VKID");

            // Get vkId From Preferences
            if (TextUtils.isEmpty(vkId))
                vkId = getSharedPreferences("CommonPrefs", MODE_PRIVATE).getString("USER_VKID", null);

            Log.e(TAG, "VKID :" + vkId);

            // Check Still VKID is empty
            if (TextUtils.isEmpty(vkId)) {
                Log.e(TAG, "TimeService Started - VKId is should not be null.");
                return super.onStartCommand(intent, flags, startId);
            }
        }

        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // Print a log

                try {
                    mGoogleApiClient.connect();
                    // Log.d(APP_TAG,intent.getStringExtra("test"));
                    Log.d(TAG, "Start to do an action");
                    // Toast.makeText(TimeService.this, latitude, Toast.LENGTH_SHORT).show();
                    Log.e("latitude", latitude + "\n longitude" + longitude + "Accurcy" + arrcury + "Altitude" + altitude);
                    Intent filterRes = new Intent();
                    filterRes.setAction("xxx.yyy.intent.action.LOCATION");
                    filterRes.putExtra("latitude", latitude);
                    filterRes.putExtra("longitude", longitude);
                    filterRes.putExtra("getAccuracy", arrcury);
                    filterRes.putExtra("getAltitude", altitude);

                    sendBroadcast(filterRes);

                    //-- START: Prepare for Geofencing
                    if (latitude != null && longitude != null) {

                        if (TextUtils.isEmpty(vkId))
                            return;

                        Log.e(TAG, "vkmsApp.isGeofencingEntered() : " + vkmsApp.isGeofencingEntered());
                        if (!vkmsApp.isGeofencingEntered()) {
                            if (geofenceEventHandler.checkPermissions()) {

                                geofenceEventHandler.addGeofences(latitude, longitude, vkId, GeofenceEventHandler.FROM_FRANCHISEE_APP);
                                Log.e(TAG, "Testing: added Geofences.");
                            }
                        }
                    }
                    //-- END

                } catch (Exception e) {
                    e.getMessage();
                    Log.d(TAG, "catch block");
                }

            }
        }, 0, INTERVAL);

        // Execute an action after period time
        return super.onStartCommand(intent, flags, startId);

        //return START_STICKY;


    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOGSERVICE, "onConnected" + bundle);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        final Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (l != null) {

            latitude = String.valueOf(l.getLatitude());
            longitude = String.valueOf(l.getLongitude());
            arrcury = String.valueOf(l.getAccuracy());
            altitude = String.valueOf(l.getAltitude());
        }


        startLocationUpdate();
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub


        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePI);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOGSERVICE, "onDestroy - Estou sendo destruido ");

        try {
            Intent intent = new Intent(this, TimeService.class);
            intent.putExtra("VKID", connection.getVkid());
            startService(intent);
            mGoogleApiClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOGSERVICE, "onConnectionSuspended " + i);

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(LOGSERVICE, "onConnectionFailed ");

    }

    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void startLocationUpdate() {
        initLocationRequest();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng mLocation = (new LatLng(location.getLatitude(), location.getLongitude()));

        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        arrcury = String.valueOf(location.getAccuracy());
        altitude = String.valueOf(location.getAltitude());


    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        geofenceEventHandler.nonePendingTASK();
        if (task.isSuccessful()) {
            geofenceEventHandler.updateGeofencesAdded(!geofenceEventHandler.getGeofencesAdded());
            //setButtonsEnabledState();

            int messageId = geofenceEventHandler.getGeofencesAdded() ? R.string.geofences_added : R.string.geofences_removed;
            Log.e(TAG, "Geofence onComplete : " + getString(messageId));
            //Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this, task.getException());
            Log.w(TAG, errorMessage);
        }
    }
    //endregion
}