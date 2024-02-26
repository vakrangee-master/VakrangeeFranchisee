package in.vakrangee.core.utils;

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
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.Timer;
import java.util.TimerTask;

import androidx.core.app.ActivityCompat;

/**
 * Created by nileshd on 6/20/2016.
 */
public class TimeService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private static final String TAG = "DemoService";
    private final int INTERVAL = 60 * 1000;

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
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        //  Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();
        Log.i(LOGSERVICE, "onStartCommand");

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
            startService(new Intent(this, TimeService.class));
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
}