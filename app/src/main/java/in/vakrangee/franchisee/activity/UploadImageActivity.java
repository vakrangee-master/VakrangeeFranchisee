package in.vakrangee.franchisee.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.sitelayout.asyntask.FetchAddressIntentService;
import in.vakrangee.supercore.franchisee.utils.AppUtilsforLocationService;
import in.vakrangee.supercore.franchisee.utils.Connection;

public class UploadImageActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    Button btnwall1, btnwall2, btnwall3, btnwall4, front, top, floor, btnSubmit;
    Connection db;
    Uri selectedImage;
    Bitmap bmp;
    String picturePath;
    String TAG = "Response";
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7;
    String accurylevel;


    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    Context mContext;

    private LatLng mCenterLatLong;
    Toolbar toolbar;
    private AddressResultReceiver mResultReceiver;
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;
    EditText mLocationAddress;
    TextView mLocationText, mLocationMarkerText, Exif;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    Context context;
    String vikid;
    SimpleDateFormat sdf;
    String getlati;
    String currentDateandTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mContext = this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mLocationMarkerText = (TextView) findViewById(R.id.locationMarkertext);
        mLocationAddress = (EditText) findViewById(R.id.Address);

        mLocationText = (TextView) findViewById(R.id.Locality);
        mLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAutocompleteActivity();

            }


        });
        mapFragment.getMapAsync(this);
        mResultReceiver = new AddressResultReceiver(new Handler());

        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!AppUtilsforLocationService.isLocationEnabled(mContext)) {
                // notify user
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setCancelable(false);
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
                        Intent myIntent = new Intent(UploadImageActivity.this, DashboardActivity.class);
                        startActivity(myIntent);
                    }
                });
                dialog.show();
            }
            buildGoogleApiClient();
        } else {
            Toast.makeText(mContext, "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }


        mLocationMarkerText = (TextView) findViewById(R.id.locationMarkertext);

        Exif = (TextView) findViewById(R.id.exif);


        btnwall1 = (Button) findViewById(R.id.btnwall1);
        btnwall2 = (Button) findViewById(R.id.btnwall2);
        btnwall3 = (Button) findViewById(R.id.btnwall3);
        btnwall4 = (Button) findViewById(R.id.btnwall4);
        front = (Button) findViewById(R.id.btnwall5);
        top = (Button) findViewById(R.id.btnwall6);
        floor = (Button) findViewById(R.id.btnwall7);
        btnSubmit = (Button) findViewById(R.id.btnUploadSubmit);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        imageView6 = (ImageView) findViewById(R.id.imageView6);
        imageView7 = (ImageView) findViewById(R.id.imageView7);


        final Connection connection = new Connection(getApplicationContext());
        // connection.openDatabase();
        final String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        try {


//
            byte[] encodeByte1 = connection.getImage(1);
            byte[] encodeByte2 = connection.getImage(2);
            byte[] encodeByte3 = connection.getImage(3);
            byte[] encodeByte4 = connection.getImage(4);
            byte[] encodeByte5 = connection.getImage(5);
            byte[] encodeByte6 = connection.getImage(6);
            byte[] encodeByte7 = connection.getImage(7);
            // Exif.setText(encodeByte1.toString());
            getlati = Exif.getText().toString().trim();
            if (encodeByte1 == null) {
                // imageView1.setVisibility(View.INVISIBLE);
            } else {
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte1, 0, encodeByte1.length);
                imageView1.setImageBitmap(bitmap);


            }

            if (encodeByte2 == null) {
                // imageView1.setVisibility(View.INVISIBLE);
            } else {
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte2, 0, encodeByte2.length);
                imageView2.setImageBitmap(bitmap);

            }
            if (encodeByte3 == null) {
                // imageView1.setVisibility(View.INVISIBLE);
            } else {
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte3, 0, encodeByte3.length);
                imageView3.setImageBitmap(bitmap);

            }
            if (encodeByte4 == null) {
                // imageView1.setVisibility(View.INVISIBLE);
            } else {
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte4, 0, encodeByte4.length);
                imageView4.setImageBitmap(bitmap);

            }
            if (encodeByte5 == null) {
                // imageView1.setVisibility(View.INVISIBLE);
            } else {
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte5, 0, encodeByte5.length);
                imageView5.setImageBitmap(bitmap);

            }
            if (encodeByte6 == null) {
                // imageView1.setVisibility(View.INVISIBLE);
            } else {
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte6, 0, encodeByte6.length);
                imageView6.setImageBitmap(bitmap);

            }
            if (encodeByte7 == null) {
                // imageView1.setVisibility(View.INVISIBLE);
            } else {
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte7, 0, encodeByte7.length);
                imageView7.setImageBitmap(bitmap);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.uploadImage);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        btnwall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (getApplicationContext().getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_CAMERA)) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    startActivityForResult(cameraIntent, 1);


                } else {
                    Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
                }


            }
        });
        btnwall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (getApplicationContext().getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_CAMERA)) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 2);
                } else {
                    Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
                }


            }
        });

        btnwall3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getApplicationContext().getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_CAMERA)) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
                }


            }
        });
        btnwall4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getApplicationContext().getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_CAMERA)) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 4);
                } else {
                    Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
                }


            }
        });
        front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (getApplicationContext().getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_CAMERA)) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 5);
                } else {
                    Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();

                }

            }
        });
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (getApplicationContext().getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_CAMERA)) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 6);
                } else {
                    Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
                }


            }
        });

        floor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (getApplicationContext().getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_CAMERA)) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 7);
                } else {
                    Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera postion change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;


                //  mMap.clear();

                try {

                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);

                    startIntentService(mLocation);
                    mLocationMarkerText.setText("Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude);
                    Exif.setText(mCenterLatLong.latitude + "," + mCenterLatLong.longitude);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onConnected(Bundle bundle) {
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
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            e.printStackTrace();
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
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
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

        // check if map is created successfully or not
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;


            latLong = new LatLng(location.getLatitude(), location.getLongitude());

            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLong).zoom(19f).tilt(70).build();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            mLocationMarkerText.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude());
            Exif.setText(location.getLatitude() + "," + location.getLongitude());

            startIntentService(location);
            Log.e("getAccuracy", String.valueOf(location.getAccuracy()));

            accurylevel = String.valueOf(location.getAccuracy());

            try {


                double accury = location.getAccuracy();
                if (accury <= 15.0) {

                    btnSubmit.setVisibility(View.VISIBLE);


                } else {
                    btnSubmit.setVisibility(View.INVISIBLE);
                    Toast.makeText(this, "Accucry not match ,Please wait...", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // startService(new Intent(this, TimeService.class));
            final String passdata = location.getLatitude() + ":" + location.getLongitude();


            if (!AppUtilsforLocationService.isLocationEnabled(mContext)) {
                // notify user
                Log.e("Please Location Disable ", "Sorry Disable Location");
            } else {

                if (passdata == null) {
                    Log.e("Sorry No Lat Long ", passdata);
                } else {
                    // Intent serviceIntent = new Intent(getApplicationContext(), TimeService.class);
                    // Intent serviceIntent = new Intent(getApplicationContext(), TimeService.class);
                    // serviceIntent.putExtra("Lat", passdata);


                    //startService(serviceIntent);


                }

            }


        } else {

            Toast.makeText(UploadImageActivity.this, "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService(Location mLocation) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(AppUtilsforLocationService.LocationConstants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(AppUtilsforLocationService.LocationConstants.LOCATION_DATA_EXTRA, mLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Connection connection = new Connection(this);

        super.onActivityResult(requestCode, resultCode, data);
        try {


            if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
                if (resultCode == RESULT_OK) {
                    // Get the user's selected place from the Intent.
                    Place place = PlaceAutocomplete.getPlace(mContext, data);

                    // TODO call location based filter


                    LatLng latLong;


                    latLong = place.getLatLng();

                    //mLocationText.setText(place.getImagetype() + "");

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLong).zoom(19f).tilt(70).build();

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
                    mMap.setMyLocationEnabled(true);
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                }


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(mContext, data);
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {


                    vikid = connection.getVkid();
                    sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
                    currentDateandTime = sdf.format(new Date());


                    try {

                        String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                        if (locationProviders == null || locationProviders.equals("")) {

                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        } else {
                            getlati = Exif.getText().toString().trim();
                            Bundle extras = data.getExtras();
                            Bitmap bitmap = (Bitmap) extras.get("data");
                            imageView1.setImageBitmap(bitmap);
                            imageView1.setPadding(8, 8, 8, 8);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            connection.insertintoImageMasterWithLatlong(1, byteArray, vikid, currentDateandTime, getlati);

                        }


                    } catch (Exception e) {
                        Log.e("cathc Latlong", e.getMessage());
                    }


                } else {
                    Toast.makeText(this, "Image cannot be capturing", Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    imageView2.setImageBitmap(bitmap);
                    imageView2.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    connection.insertintoImageMasterWithLatlong(1, byteArray, vikid, currentDateandTime, getlati);

                    // connection.insertintoImageMaster(2, byteArray);
                } else {
                    Toast.makeText(this, "Error while capturing Image", Toast.LENGTH_LONG).show();
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    imageView3.setImageBitmap(bitmap);
                    imageView3.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    connection.insertintoImageMaster(3, byteArray);
                } else {
                    Toast.makeText(this, "Error while capturing Image", Toast.LENGTH_LONG).show();
                }
                break;
            case 4:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    imageView4.setImageBitmap(bitmap);
                    imageView4.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    connection.insertintoImageMaster(4, byteArray);

                } else {
                    Toast.makeText(this, "Error while capturing Image", Toast.LENGTH_LONG).show();
                }
                break;
            case 5:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    imageView5.setImageBitmap(bitmap);
                    imageView5.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    connection.insertintoImageMaster(5, byteArray);
                } else {
                    Toast.makeText(this, "Error while capturing Image", Toast.LENGTH_LONG).show();
                }
                break;
            case 6:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    imageView6.setImageBitmap(bitmap);
                    imageView6.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    connection.insertintoImageMaster(6, byteArray);
                } else {
                    Toast.makeText(this, "Error while capturing Image", Toast.LENGTH_LONG).show();
                }
                break;
            case 7:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    imageView7.setImageBitmap(bitmap);
                    imageView7.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    connection.insertintoImageMaster(7, byteArray);
                } else {
                    Toast.makeText(this, "Error while capturing Image", Toast.LENGTH_LONG).show();
                }
                break;

        }


    }


    public Bitmap mark(Bitmap src, String watermark) {
        int w = src.getWidth();
        int h = src.getHeight();

        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.WHITE);  //transparent black,change opacity by changing hex value "AA" between "00" and "FF"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system

        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.primary));
        paint.setTextSize(8);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setUnderlineText(false);
        canvas.drawBitmap(src, 0f, 0f, null);
        float height = paint.measureText("yY");
        //should draw background first,order is important
//        int left=0;
//        int right=w;
//        int bottom=h;
//        int top= (int) (bottom-(h*.10));
//        canvas.drawRect(left,top,right,bottom,bgPaint);
        canvas.drawText(dateTime, 20f, height + 15f, paint);
        // canvas.drawText(watermark, 8 , h-8, paint);

        return result;


    }


    private String getRealPathFromURI(Uri tempUri) {
        Cursor cursor = getContentResolver().query(tempUri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private Uri getImageUri(Context applicationContext, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(applicationContext.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    public void onBackPressed() {
        Intent intent = new Intent(UploadImageActivity.this, MyVakrangeeKendra.class);
        startActivity(intent);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(UploadImageActivity.this, MyVakrangeeKendra.class);
                startActivity(intent);


                break;
        }
        return true;
    }


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

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
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
}
