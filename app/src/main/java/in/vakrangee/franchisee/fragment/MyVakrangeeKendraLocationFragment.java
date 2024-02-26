package in.vakrangee.franchisee.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.MyVakrangeeKendraPhotoViewPager;
import in.vakrangee.franchisee.task.AsyncGetmyVakrangeeKendraTimingsResponse;
import in.vakrangee.franchisee.task.AsyncGetmyVakrangeeKendraTimingsResponseParticular;
import in.vakrangee.franchisee.sitelayout.asyntask.FetchAddressIntentService;
import in.vakrangee.supercore.franchisee.impl.GeoCordinatesImpl;
import in.vakrangee.supercore.franchisee.model.GeoCordinates;
import in.vakrangee.supercore.franchisee.model.MyVKMaster;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.AppUtilsforLocationService;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.webservice.WebService;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

//

/**
 * Created by Nileshd on 12/30/2016.
 */
@SuppressLint("ValidFragment")
public class MyVakrangeeKendraLocationFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    View view;
    double latLong;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static String TAG = "MAP LOCATION";
    //Context mContext;
    TextView mLocationMarkerText;
    private LatLng mCenterLatLong;

    Toolbar toolbar;
    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private AddressResultReceiver mResultReceiver;
    /**
     * The formatted location address.
     */
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;
    EditText mLocationAddress;
    TextView mLocationText;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    Toolbar mToolbar;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    String Lati, Longi;

    Button btnGetDrawLine;
    Button btnChangeMap;
    ImageView btnShareinfo;
    String strAccurcy;
    String strlat, strLong;
    String strlatlong;
    private int imageParentWidth = -1;
    private int imageParentHeight = -1;
    private int imageHeight = -1;
    private int centerX = -1;
    private int centerY = -1;
    int desiredAccuracy;
    Context context;
    MyVKMaster myVKMasterMain;
    ProgressDialog progress;
    TelephonyManager telephonyManager;
    String diplayServerResopnse;
    Connection connection;
    InternetConnection internetConnection;

    @SuppressLint("ValidFragment")
    public MyVakrangeeKendraLocationFragment(MyVKMaster myVKMaster) {

        this.myVKMasterMain = myVKMaster;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");
        View view = inflater.inflate(R.layout.fragment_vakrangeekendra_location, container, false);

        try {


            telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);


            connection = new Connection(getActivity());
            // connection.openDatabase();


            internetConnection = new InternetConnection(getActivity());
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            //   SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);

            telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

            mLocationMarkerText = (TextView) view.findViewById(R.id.locationMarkertext);
            mLocationAddress = (EditText) view.findViewById(R.id.Address);
            mLocationText = (TextView) view.findViewById(R.id.Locality);
            mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        getSupportActionBar().setTitle(getResources().getString(R.string.myOutlets));

            btnGetDrawLine = (Button) view.findViewById(R.id.getDrawLine);


            btnShareinfo = (ImageView) view.findViewById(R.id.btnShareinfo);
            btnShareinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupdisplay();
                }
            });
            mLocationText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    openAutocompleteActivity();

                }


            });

            ToggleButton toggleButton = (ToggleButton) view.findViewById(R.id.btnChangeMap);
            assert toggleButton != null;
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {

                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                    } else {


                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    }

                }
            });
            MapView mapView = (MapView) view.findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);

            // mapFragment.getMapAsync(this);
            mResultReceiver = new AddressResultReceiver(new Handler());

            if (checkPlayServices()) {
                // If this check succeeds, proceed with normal processing.
                // Otherwise, prompt user to get valid Play Services APK.
                if (!AppUtilsforLocationService.isLocationEnabled(getActivity())) {
                    // notify user
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setMessage("Location not enabled!");
                    dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        }
                    });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // TODO Auto-generated method stub

                        }
                    });
                    dialog.show();
                }
                buildGoogleApiClient();
            } else {
                Toast.makeText(getActivity(), "Location not supported in this device", Toast.LENGTH_SHORT).show();
            }


            return view;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    private void popupdisplay() {
        try {

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //inflate layout from xml. you must create an xml layout file in res/layout first
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View layout = inflater.inflate(R.layout.popmvakrangeelocation, null);
            builder.setView(layout);

            TextView txtlatlong = (TextView) layout.findViewById(R.id.txtlat);
            txtlatlong.setTypeface(Typeface.SANS_SERIF);

            TextView txtaccuracy = (TextView) layout.findViewById(R.id.txtaccuracy);
            TextView txtAddress = (TextView) layout.findViewById(R.id.txtAddress);
            TextView txtlong = (TextView) layout.findViewById(R.id.txtlong);
            txtaccuracy.setText(strAccurcy);
            txtlatlong.setText(strlat);
            txtlong.setText(strLong);


            txtAddress.setText(mLocationAddress.getText().toString());

            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (strlat == null) {
                        Toast.makeText(getActivity(), "Please Turn on GPS setting", Toast.LENGTH_SHORT).show();
                    } else if (internetConnection.isConnectingToInternet() == false) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.internetCheck));

                    } else {
                        progress = new ProgressDialog(getActivity());
                        progress.setTitle(R.string.updateLocation);
                        progress.setMessage(getResources().getString(R.string.pleaseWait));
                        progress.setCancelable(false);
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.show();
//
                        AsyncMyVakrangeeKendraLocation myRequest = new AsyncMyVakrangeeKendraLocation();
                        myRequest.execute();


                        dialog.dismiss();
                        ((MyVakrangeeKendraPhotoViewPager) getActivity()).selectFragment(0);
                    }


                }
            });

            builder.setNegativeButton("Cancel  ", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();

        } catch (Exception e) {
            e.getMessage();

        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;


        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera postion change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;
                float currentZoom = 19.0f;
                try {


                    //  mMap.clear();

                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);

                    startIntentService(mLocation);
                    mLocationMarkerText.setText("Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude);
                    strlat = String.valueOf(mCenterLatLong.latitude);
                    strLong = String.valueOf(mCenterLatLong.longitude);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {


            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        try {


            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                changeMap(mLastLocation);
                Log.d(TAG, "ON connected");

            } else
                try {
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            try {
                LocationRequest mLocationRequest = new LocationRequest();
                //mLocationRequest.setInterval(10000);
                // mLocationRequest.setFastestInterval(5000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            } catch (Exception e) {
                e.printStackTrace();
            }
            btnGetDrawLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Connection connection = new Connection(getActivity());
                    // connection.openDatabase();

                    //  ArrayList<String> data = connection.GetAllValues();
                    List<GeoCordinates> GeoCordinatesProvider = new GeoCordinatesImpl().getServiceProvider(Lati, Longi);

                    ArrayList<LatLng> coordList = new ArrayList<LatLng>();

                    for (GeoCordinates gc : GeoCordinatesProvider) {
                        coordList.add(new LatLng(Double.valueOf(gc.getLatitude()), Double.valueOf(gc.getLongitude())));

                    }
                    mMap.addPolyline(new PolylineOptions().addAll(coordList).width(5.0f).color(Color.BLUE));
                }
            });

        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {

            if (location != null)

                changeMap(location);
            // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }

    public void changeMap(final Location location) {

        Log.d(TAG, "Reaching map" + mMap);


        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }


            if (myVKMasterMain != null && myVKMasterMain.getLatitude() != null && !myVKMasterMain.getLatitude().isEmpty() && !myVKMasterMain.getLatitude().equals("null")) {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                double lati = Double.parseDouble(myVKMasterMain.getLatitude());
                double logi = Double.parseDouble(myVKMasterMain.getLongtitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lati, logi), 19.0f));


            } else {

                // check if map is created successfully or not
                if (mMap != null) {
                    mMap.getUiSettings().setZoomControlsEnabled(false);
                    LatLng latLong;


                    latLong = new LatLng(location.getLatitude(), location.getLongitude());

                    //   CameraPosition cameraPosition = new CameraPosition.Builder().target(latLong).zoom(19f).tilt(70).build();

                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    // mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 19.0f));


                    mLocationMarkerText.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude() + "Accurcy" + location.getAccuracy());
                    startIntentService(location);
                    Log.e("Sorry No Lat Long ", String.valueOf(location.getAccuracy()));
                    // startService(new Intent(this, TimeService.class));
                    final String passdata = location.getLatitude() + ":" + location.getLongitude() + ":" + location.getAccuracy() + ":" + location.getAltitude();

                    strAccurcy = String.valueOf(location.getAccuracy());
                    strlatlong = String.valueOf((location.getLatitude()));


//                if (strAccurcy.length() <= 15.0) {
//                    Log.d(" number less 300" + "", "");
//                    popupdisplay();
//                } else {
//                    Log.d(" number grater 300" + "", "");
//                }
                    if (!AppUtilsforLocationService.isLocationEnabled(getActivity())) {
                        // notify user
                        Log.e("Please Location Disable ", "Sorry Disable Location");
                    } else {

                        if (passdata == null) {
                            Log.e("Sorry No Lat Long ", passdata);
                        } else {
                            // Intent serviceIntent = new Intent(getApplicationContext(), TimeService.class);
                            //Intent serviceIntent = new Intent(getApplicationContext(), TimeService.class);
                            // serviceIntent.putExtra("Lat", passdata);


                            // startService(serviceIntent);


                        }

                    }


                } else {

                    Toast.makeText(getActivity(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Sorry! unable detect locationz", Toast.LENGTH_SHORT).show();
        }
    }


    public static String geoTag(String filename, double latitude, double longitude) {
        ExifInterface exif;
        LatLng latLong;
        //  mLocationMarkerText.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude());

        try {
            exif = new ExifInterface(filename);
            int num1Lat = (int) Math.floor(latitude);
            int num2Lat = (int) Math.floor((latitude - num1Lat) * 60);
            double num3Lat = (latitude - ((double) num1Lat + ((double) num2Lat / 60))) * 3600000;

            int num1Lon = (int) Math.floor(longitude);
            int num2Lon = (int) Math.floor((longitude - num1Lon) * 60);
            double num3Lon = (longitude - ((double) num1Lon + ((double) num2Lon / 60))) * 3600000;

            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, num1Lat + "/1," + num2Lat + "/1," + num3Lat + "/1000");
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, num1Lon + "/1," + num2Lon + "/1," + num3Lon + "/1000");


            if (latitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
            }

            if (longitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            }

            exif.saveAttributes();
        } catch (IOException e) {
            Log.e("PictureActivity", e.getLocalizedMessage());
        }

        return filename;
    }


    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(AppUtilsforLocationService.LocationConstants.RESULT_DATA_KEY);

            mAreaOutput = resultData.getString(AppUtilsforLocationService.LocationConstants.LOCATION_DATA_AREA);

            mCityOutput = resultData.getString(AppUtilsforLocationService.LocationConstants.LOCATION_DATA_CITY);
            mStateOutput = resultData.getString(AppUtilsforLocationService.LocationConstants.LOCATION_DATA_STREET);

            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == AppUtilsforLocationService.LocationConstants.SUCCESS_RESULT) {
                //  showToast(getString(R.string.address_found));


            }


        }

    }

    /**
     * Updates the address in the UI.
     */
    protected void displayAddressOutput() {
        //  mLocationAddressTextView.setText(mAddressOutput);
        try {
            if (mAreaOutput != null)
                // mLocationText.setText(mAreaOutput+ "");

                mLocationAddress.setText(mAddressOutput);

            //mLocationText.setText(mAreaOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService(Location mLocation) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(AppUtilsforLocationService.LocationConstants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(AppUtilsforLocationService.LocationConstants.LOCATION_DATA_EXTRA, mLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        getActivity().startService(intent);
    }


    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {


            // Check that the result was from the autocomplete widget.
            if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
                if (resultCode == RESULT_OK) {
                    // Get the user's selected place from the Intent.
                    Place place = PlaceAutocomplete.getPlace(getActivity(), data);

                    // TODO call location based filter


                    LatLng latLong;


                    latLong = place.getLatLng();

                    //mLocationText.setText(place.getImagetype() + "");

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLong).zoom(19f).tilt(70).build();

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                }


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }


    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getActivity());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }


        @Override
        protected void onPostExecute(List<Address> addresses) {


            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(getActivity(), "No Location found", Toast.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            // mMap.clear();
            try {
                // Adding Markers on Google Map for each matching address
                for (int i = 0; i < addresses.size(); i++) {

                    Address address = (Address) addresses.get(i);

                    // Creating an instance of GeoPoint, to display in Google Map
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    String addressText = String.format("%s, %s",
                            address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                            address.getCountryName());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    // markerOptions.title(addressText);
                    markerOptions.draggable(true);
                    mMap.getUiSettings().setMapToolbarEnabled(false);
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                    mMap.addMarker(markerOptions);

                    // Locate the first location
                    if (i == 0)
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    //Toast.makeText(MyOutletsActitvity.this,getCompleteAdressString(address.getLatitude(), address.getLongitude()),Toast.LENGTH_SHORT).show();
                    mLocationAddress.setText(getCompleteAdressString(address.getLatitude(), address.getLongitude()));

                }
            } catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("LongLogTag")
    private String getCompleteAdressString(double LATITUDE, double LONGITUDE) {

        String strAdd = "";

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE,
                    LONGITUDE, 1);

            if (addresses != null) {

                android.location.Address returnedAddress = addresses.get(0);

                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {

                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            ",");
                }

                strAdd = strReturnedAddress.toString();

                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }


    private class AsyncMyVakrangeeKendraLocation extends AsyncTask<Void, Void, Void> {


        String a = strlat;
        String l = strLong;
        String vkLatitude = EncryptionUtil.encryptString(a, getActivity());
        String vkLongitude = EncryptionUtil.encryptString(l, getActivity());


        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");


        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                String checkboxMon;
                String checkboxTue;
                String checkboxWed;
                String checkboxThu;
                String checkboxFri;
                String checkboxSat;
                String checkboxSun;


                String checkm = myVKMasterMain.getMondayTimings();
                if (checkm.equals("uncheck")) {
                    checkboxMon = "Y";
                } else {
                    checkboxMon = "N";
                }

                String checktue = myVKMasterMain.getTuesdayTimings();
                if (checktue.equals("uncheck")) {
                    checkboxTue = "Y";
                } else {
                    checkboxTue = "N";
                }

                String checkWed = myVKMasterMain.getWednesdayTimings();
                if (checkWed.equals("uncheck")) {
                    checkboxWed = "Y";
                } else {
                    checkboxWed = "N";
                }

                String checkthu = myVKMasterMain.getThursdayTimings();
                if (checkthu.equals("uncheck")) {
                    checkboxThu = "Y";
                } else {
                    checkboxThu = "N";
                }
                String checkfri = myVKMasterMain.getFridayTimings();
                if (checkfri.equals("uncheck")) {
                    checkboxFri = "Y";
                } else {
                    checkboxFri = "N";
                }

                String checksat = myVKMasterMain.getSaturdayTimings();
                if (checksat.equals("uncheck")) {
                    checkboxSat = "Y";
                } else {
                    checkboxSat = "N";
                }
                String checksun = myVKMasterMain.getSundayTimings();
                if (checksun.equals("uncheck")) {
                    checkboxSun = "Y";
                } else {
                    checkboxSun = "N";
                }


                String mondayO = EncryptionUtil.encryptString(myVKMasterMain.getStrOpenmMon(), getActivity());
                String mondayC = EncryptionUtil.encryptString(myVKMasterMain.getStrCloseMon(), getActivity());
                String tuesdayO = EncryptionUtil.encryptString(myVKMasterMain.getStrOpenTue(), getActivity());
                String tuesdayC = EncryptionUtil.encryptString(myVKMasterMain.getStrCloseTue(), getActivity());
                String wednesdayO = EncryptionUtil.encryptString(myVKMasterMain.getStrOpenWed(), getActivity());
                String wednesdayC = EncryptionUtil.encryptString(myVKMasterMain.getStrCloseWed(), getActivity());
                String thursdayO = EncryptionUtil.encryptString(myVKMasterMain.getStrOpenThu(), getActivity());
                String thursdayC = EncryptionUtil.encryptString(myVKMasterMain.getStrCloseThu(), getActivity());
                String fridayO = EncryptionUtil.encryptString(myVKMasterMain.getStrOpenFri(), getActivity());
                String fridayC = EncryptionUtil.encryptString(myVKMasterMain.getStrCloseFri(), getActivity());
                String saturdayO = EncryptionUtil.encryptString(myVKMasterMain.getStrOpenSat(), getActivity());
                String saturdayC = EncryptionUtil.encryptString(myVKMasterMain.getStrCloseSat(), getActivity());
                String sundayO = EncryptionUtil.encryptString(myVKMasterMain.getStrOpenSun(), getActivity());
                String sundayC = EncryptionUtil.encryptString(myVKMasterMain.getStrCloseSun(), getActivity());


                //byte[] bytea = byteArray;
                // String frontage = bytea != null ? (EncryptionUtil.encodeBase64(bytea)) : "";


                String isMondayClosed = EncryptionUtil.encryptString(checkboxMon, getActivity());
                String isTuesdayClosed = EncryptionUtil.encryptString(checkboxTue, getActivity());
                String isWednesdayClosed = EncryptionUtil.encryptString(checkboxWed, getActivity());
                String isThursdayClosed = EncryptionUtil.encryptString(checkboxThu, getActivity());
                String isFridayClosed = EncryptionUtil.encryptString(checkboxFri, getActivity());
                String isSaturdayClosed = EncryptionUtil.encryptString(checkboxSat, getActivity());
                String isSundayClosed = EncryptionUtil.encryptString(checkboxSun, getActivity());

//                String leftWall = myVKMasterMain.getLeftWallThumb();
//                String frontage = myVKMasterMain.getFrontageThumb();
//                String frontWall = myVKMasterMain.getFrontWallThumb();
//                String rightWall = myVKMasterMain.getRightWallThumb();
//                String backWall = myVKMasterMain.getBackWallThumb();
//                String ceiling = myVKMasterMain.getCeilingThumb();
//                String floor = myVKMasterMain.getFloorThumb();
//                String extra1 = myVKMasterMain.getExtraThumb1();
//                String extra2 = myVKMasterMain.getExtraThumb2();
//                String extra3 = myVKMasterMain.getExtraThumb3();

                String leftWall = null;
                String frontage = null;
                String frontWall = null;
                String rightWall = null;
                String backWall = null;
                String ceiling = null;
                String floor = null;
                String extra1 = null;
                String extra2 = null;
                String extra3 = null;

                Connection connection = new Connection(getActivity());
                String vkid = connection.getVkid();
                String tokenId = connection.getTokenId();
//
                String deviceIdget = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceIdget, getActivity());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getActivity());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getActivity());

                String simSerial = CommonUtils.getSimSerialNumber(getActivity());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getActivity());

                String vkidd = EncryptionUtil.encryptString(vkid, getActivity());
                String TokenId = EncryptionUtil.encryptString(tokenId, getActivity());


                String UserID = EncryptionUtil.encryptString(vkid, getActivity());
                String VKID = EncryptionUtil.encryptString(myVKMasterMain.getUserId(), getActivity());


                if (vkid.toUpperCase().startsWith("VL") || vkid.toUpperCase().startsWith("VA")) {

                    diplayServerResopnse = WebService.myVakrangeeKendraParticular(UserID, VKID, TokenId, imei, deviceid, simserialnumber,
                            isMondayClosed, isTuesdayClosed, isWednesdayClosed, isThursdayClosed, isFridayClosed, isSaturdayClosed, isSundayClosed,
                            mondayO, mondayC, tuesdayO, tuesdayC, wednesdayO, wednesdayC, thursdayO, thursdayC, fridayO, fridayC, saturdayO, saturdayC,
                            sundayO, sundayC, vkLatitude, vkLongitude, frontage, leftWall, frontWall, rightWall, backWall, ceiling, floor, extra1, extra2, extra3);


                } else {
                    diplayServerResopnse = WebService.myVakrangeeKendra(vkidd, TokenId, imei, deviceid, simserialnumber,
                            isMondayClosed, isTuesdayClosed, isWednesdayClosed, isThursdayClosed, isFridayClosed, isSaturdayClosed, isSundayClosed,
                            mondayO, mondayC, tuesdayO, tuesdayC, wednesdayO, wednesdayC, thursdayO, thursdayC, fridayO, fridayC, saturdayO, saturdayC,
                            sundayO, sundayC, vkLatitude, vkLongitude, frontage, leftWall, frontWall, rightWall, backWall, ceiling, floor, extra1, extra2, extra3);


                }

                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));

                e.getMessage();


            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            progress.dismiss();
            // progress.dismiss();
            try {


                if (diplayServerResopnse.equals("My Vakrangee Kendra information added successfully.")) {

                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.imageuploadsuccessful));


                    //  Log.e(TAG + "Please Null error", diplayServerResopnse);
                } else if (diplayServerResopnse.equals("Vakrangee Kendra data updated successfully.")) {
                    // AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.imageuploadsuccessful));
                    String vkidd = EncryptionUtil.encryptString(connection.getVkid(), getActivity());
                    String TokenID = EncryptionUtil.encryptString(connection.getTokenId(), getActivity());

                    String deviceIdget = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                    String deviceid = EncryptionUtil.encryptString(deviceIdget, getActivity());

                    String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getActivity());
                    String imei = EncryptionUtil.encryptString(deviceIDAndroid, getActivity());

                    String simSerial = CommonUtils.getSimSerialNumber(getActivity());
                    String simserialnumber = EncryptionUtil.encryptString(simSerial, getActivity());

                    String VKID = EncryptionUtil.encryptString(myVKMasterMain.getUserId(), getActivity());
                    if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {
                        new AsyncGetmyVakrangeeKendraTimingsResponseParticular(getActivity(), myVKMasterMain.getUserId()).execute(vkidd, VKID, TokenID, imei, deviceid, simserialnumber);

                    } else {
                        new AsyncGetmyVakrangeeKendraTimingsResponse(getActivity()).execute(vkidd, TokenID, imei, deviceid, simserialnumber);

                    }
                } else {
                    Log.e("Error in Server", diplayServerResopnse);
                    // Toast.makeText(getApplicationContext(), "Error OTP ", Toast.LENGTH_SHORT).show();
                    //AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));

                }
            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));

                e.printStackTrace();
            }

        }


    }


}
