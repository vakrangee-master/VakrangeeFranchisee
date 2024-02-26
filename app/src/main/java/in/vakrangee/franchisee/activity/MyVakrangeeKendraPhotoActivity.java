package in.vakrangee.franchisee.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
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
import java.io.File;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.sitelayout.asyntask.FetchAddressIntentService;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.AppUtilsforLocationService;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.webservice.WebService;

//

public class MyVakrangeeKendraPhotoActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {
    Toolbar toolbar;
    ProgressDialog progress;

    TextView txtOpenMon, txtCloseMon;
    TextView txtOpenTue, txtCloseTue;
    TextView txtOpenWed, txtCloseWed;
    TextView txtOpenThu, txtCloseThu;
    TextView txtOpenFri, txtCloseFri;
    TextView txtOpenSat, txtCloseSat;
    TextView txtOpenSun, txtCloseSun;

    CheckBox checkBoxMon, checkBoxTue, checkBoxWed, checkBoxThu, checkBoxFri, checkBoxSat, checkBoxSun;


    private Calendar mCalen;
    private int hourOfDay;
    private int minute;
    private int ampm;
    int timePickerInput;
    int hour;
    private static final int Time_PICKER_ID = 0;


    Button btnSubmittime;
    String dataUnderline;

    String strOpenmMon = "09:00 AM", strCloseMon = "09:00 PM";
    String strOpenTue = "09:00 AM", strCloseTue = "09:00 PM";
    String strOpenWed = "09:00 AM", strCloseWed = "09:00 PM";
    String strOpenThu = "09:00 AM", strCloseThu = "09:00 PM";
    String strOpenFri = "09:00 AM", strCloseFri = "09:00 PM";
    String strOpenSat = "09:00 AM", strCloseSat = "09:00 PM";
    String strOpenSun = "09:00 AM", strCloseSun = "09:00 PM";
    private String imgPath;

    // ------UploadImageActivity.java--------------------------------------------


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
    String latitude, longitude, arrcury, altitude;
    SpannableString contentOpen, contentClose;
    Connection connection;
    String diplayServerResopnse;
    TelephonyManager telephonyManager;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private String selectedImagePath = "";
    byte[] frontImage;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vakrangee_kendra_photo);

        //-------------------------------------------------------
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
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
        camerapermission();
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
                        Intent myIntent = new Intent(MyVakrangeeKendraPhotoActivity.this, DashboardActivity.class);
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


        connection = new Connection(getApplicationContext());
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

        btnwall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (getApplicationContext().getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_CAMERA)) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri(1));
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
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri(2));
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
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri(3));
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
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri(4));
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
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri(5));
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
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri(6));
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
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri(7));
                    startActivityForResult(cameraIntent, 7);
                } else {
                    Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
                }


            }
        });

        //---------------------------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.outlestimeing);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        dataUnderline = "09:00 AM";
        contentOpen = new SpannableString(dataUnderline);
        contentOpen.setSpan(new UnderlineSpan(), 0, dataUnderline.length(), 0);

        String dataUnderlineclose = "09:00 PM";
        contentClose = new SpannableString(dataUnderlineclose);
        contentClose.setSpan(new UnderlineSpan(), 0, dataUnderlineclose.length(), 0);

        // set current time into output textview
        btnSubmittime = (Button) findViewById(R.id.btnSubmittime);

        txtOpenMon = (TextView) findViewById(R.id.txtOpenMon);
        txtCloseMon = (TextView) findViewById(R.id.txtCloseMon);


        txtOpenTue = (TextView) findViewById(R.id.txtOpenTue);
        txtCloseTue = (TextView) findViewById(R.id.txtCloseTue);

        txtOpenWed = (TextView) findViewById(R.id.txtOpenWed);
        txtCloseWed = (TextView) findViewById(R.id.txtCloseWed);

        txtOpenThu = (TextView) findViewById(R.id.txtOpenThu);
        txtCloseThu = (TextView) findViewById(R.id.txtCloseThu);

        txtOpenFri = (TextView) findViewById(R.id.txtOpenFri);
        txtCloseFri = (TextView) findViewById(R.id.txtCloseFri);

        txtOpenSat = (TextView) findViewById(R.id.txtOpenSat);
        txtCloseSat = (TextView) findViewById(R.id.txtCloseSat);

        txtOpenSun = (TextView) findViewById(R.id.txtOpenSun);
        txtCloseSun = (TextView) findViewById(R.id.txtCloseSun);


        checkBoxMon = (CheckBox) findViewById(R.id.checkBoxMon);
        checkBoxTue = (CheckBox) findViewById(R.id.checkBoxTue);
        checkBoxWed = (CheckBox) findViewById(R.id.checkBoxWed);
        checkBoxThu = (CheckBox) findViewById(R.id.checkBoxThu);
        checkBoxFri = (CheckBox) findViewById(R.id.checkBoxFri);
        checkBoxSat = (CheckBox) findViewById(R.id.checkBoxSat);
        checkBoxSun = (CheckBox) findViewById(R.id.checkBoxSun);


        txtOpenMon.setText(contentOpen);
        txtOpenTue.setText(contentOpen);
        txtOpenWed.setText(contentOpen);
        txtOpenThu.setText(contentOpen);
        txtOpenFri.setText(contentOpen);
        txtOpenSat.setText(contentOpen);
        txtOpenSun.setText(contentOpen);

        txtCloseMon.setText(contentClose);
        txtCloseTue.setText(contentClose);
        txtCloseWed.setText(contentClose);
        txtCloseThu.setText(contentClose);
        txtCloseFri.setText(contentClose);
        txtCloseSat.setText(contentClose);
        txtCloseSun.setText(contentClose);

        checkBoxMon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtOpenMon.setClickable(true);
                    txtCloseMon.setClickable(true);
                    txtOpenMon.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseMon.setTextColor(getResources().getColor(R.color.gray));

                    txtOpenMon.setText(contentOpen);
                    txtCloseMon.setText(contentClose);

                } else {

                    txtOpenMon.setClickable(false);
                    txtCloseMon.setClickable(false);
                    txtOpenMon.setText(contentOpen);
                    txtCloseMon.setText(contentClose);
                    txtOpenMon.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseMon.setTextColor(getResources().getColor(R.color.gray));
                    txtOpenMon.setError(null);
                    txtCloseMon.setError(null);
                }

            }
        });

        checkBoxTue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtOpenTue.setClickable(true);
                    txtCloseTue.setClickable(true);
                    txtOpenTue.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseTue.setTextColor(getResources().getColor(R.color.gray));
                    txtOpenTue.setText(contentOpen);
                    txtCloseTue.setText(contentClose);

                } else {


                    txtOpenTue.setClickable(false);
                    txtCloseTue.setClickable(false);
                    txtOpenTue.setText(contentOpen);
                    txtCloseTue.setText(contentClose);
                    txtOpenTue.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseTue.setTextColor(getResources().getColor(R.color.gray));

                    txtOpenTue.setError(null);
                    txtCloseTue.setError(null);
                }

            }
        });

        checkBoxWed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtOpenWed.setClickable(true);
                    txtCloseWed.setClickable(true);
                    txtOpenWed.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseWed.setTextColor(getResources().getColor(R.color.gray));

                    txtOpenWed.setText(contentOpen);
                    txtCloseWed.setText(contentClose);

                } else {


                    txtOpenWed.setClickable(false);
                    txtCloseWed.setClickable(false);
                    txtOpenWed.setText(contentOpen);
                    txtCloseWed.setText(contentClose);
                    txtOpenWed.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseWed.setTextColor(getResources().getColor(R.color.gray));
                    txtOpenWed.setError(null);
                    txtCloseWed.setError(null);
                }

            }
        });

        checkBoxThu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtOpenThu.setClickable(true);
                    txtCloseThu.setClickable(true);
                    txtOpenThu.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseThu.setTextColor(getResources().getColor(R.color.gray));
                    txtOpenThu.setText(contentOpen);
                    txtCloseThu.setText(contentClose);

                } else {


                    txtOpenThu.setClickable(false);
                    txtCloseThu.setClickable(false);
                    txtOpenThu.setText(contentOpen);
                    txtCloseThu.setText(contentClose);
                    txtOpenThu.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseThu.setTextColor(getResources().getColor(R.color.gray));
                    txtOpenThu.setError(null);
                    txtCloseThu.setError(null);
                }

            }
        });

        checkBoxFri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtOpenFri.setClickable(true);
                    txtCloseFri.setClickable(true);
                    txtOpenFri.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseFri.setTextColor(getResources().getColor(R.color.gray));
                    txtOpenFri.setText(contentOpen);
                    txtCloseFri.setText(contentClose);

                } else {

                    txtOpenFri.setClickable(false);
                    txtCloseFri.setClickable(false);
                    txtOpenFri.setText(contentOpen);
                    txtCloseFri.setText(contentClose);
                    txtOpenFri.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseFri.setTextColor(getResources().getColor(R.color.gray));
                    txtOpenFri.setError(null);
                    txtCloseFri.setError(null);
                }

            }
        });

        checkBoxSat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    txtOpenSat.setClickable(true);
                    txtCloseSat.setClickable(true);
                    txtOpenSat.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseSat.setTextColor(getResources().getColor(R.color.gray));

                    txtOpenSat.setText(contentOpen);
                    txtCloseSat.setText(contentClose);

                } else {
                    txtOpenSat.setClickable(false);
                    txtCloseSat.setClickable(false);
                    txtOpenSat.setText(contentOpen);
                    txtCloseSat.setText(contentClose);
                    txtOpenSat.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseSat.setTextColor(getResources().getColor(R.color.gray));

                }

            }
        });
        checkBoxSun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtOpenSun.setClickable(true);
                    txtCloseSun.setClickable(true);
                    txtOpenSun.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseSun.setTextColor(getResources().getColor(R.color.gray));

                    txtOpenSun.setText(contentOpen);
                    txtCloseSun.setText(contentClose);

                } else {

                    txtOpenSun.setClickable(false);
                    txtCloseSun.setClickable(false);
                    txtOpenSun.setText(contentOpen);
                    txtCloseSun.setText(contentClose);

                    txtOpenSun.setTextColor(getResources().getColor(R.color.gray));
                    txtCloseSun.setTextColor(getResources().getColor(R.color.gray));
                    txtOpenSun.setError(null);
                    txtCloseSun.setError(null);
                }

            }
        });


        mCalen = Calendar.getInstance();


        hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
        minute = mCalen.get(Calendar.MINUTE);
        ampm = mCalen.get(Calendar.AM_PM);


        txtOpenMon.setOnClickListener(this);
        txtCloseMon.setOnClickListener(this);

        txtOpenTue.setOnClickListener(this);
        txtCloseTue.setOnClickListener(this);


        txtOpenWed.setOnClickListener(this);
        txtCloseWed.setOnClickListener(this);

        txtOpenThu.setOnClickListener(this);
        txtCloseThu.setOnClickListener(this);

        txtOpenFri.setOnClickListener(this);
        txtCloseFri.setOnClickListener(this);

        txtOpenSat.setOnClickListener(this);
        txtCloseSat.setOnClickListener(this);

        txtOpenSun.setOnClickListener(this);
        txtCloseSun.setOnClickListener(this);


        btnSubmittime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String openm = txtOpenMon.getText().toString();
                String closem = txtCloseMon.getText().toString();
                String openTu = txtOpenTue.getText().toString();
                String closeTu = txtCloseTue.getText().toString();
                boolean a = CheckDates(openm, closem);
                boolean b = CheckDates(openTu, closeTu);
                boolean c = CheckDates(txtOpenWed.getText().toString(), txtCloseWed.getText().toString());
                boolean d = CheckDates(txtOpenThu.getText().toString(), txtCloseThu.getText().toString());
                boolean e = CheckDates(txtOpenFri.getText().toString(), txtCloseFri.getText().toString());
                boolean f = CheckDates(txtOpenSat.getText().toString(), txtCloseSat.getText().toString());
                boolean g = CheckDates(txtOpenSun.getText().toString(), txtCloseSun.getText().toString());
                Log.e("a", txtOpenMon.getText().toString());
                Log.e("b", txtCloseMon.getText().toString());
                Log.e("c", String.valueOf(c));
                Log.e("d", String.valueOf(d));
                Log.e("e", String.valueOf(e));
                Log.e("f", String.valueOf(f));
                Log.e("g", String.valueOf(g));

                if (a == true) {
                    // Toast.makeText(MyVakrangeeKendraPhotoActivity.this, "true", Toast.LENGTH_SHORT).show();
                    txtOpenMon.setError(null);
                    txtCloseMon.setError(null);
                } else {
                    txtOpenMon.setError("");
                    txtCloseMon.setError("");
                    // Toast.makeText(MyVakrangeeKendraPhotoActivity.this, strOpenmMon + "not match" + strCloseMon + "in Monday", Toast.LENGTH_SHORT).show();
                }


                if (b == true) {
                    txtOpenTue.setError(null);
                    txtCloseTue.setError(null);
                    //  Toast.makeText(MyVakrangeeKendraPhotoActivity.this, "true", Toast.LENGTH_SHORT).show();
                } else {
                    txtOpenTue.setError("");
                    txtCloseTue.setError("");
                    // Toast.makeText(MyVakrangeeKendraPhotoActivity.this, strOpenTue + "not match" + strCloseTue + "in Tuesday", Toast.LENGTH_SHORT).show();
                }


                if (c == true) {

                    txtOpenWed.setError(null);
                    txtCloseWed.setError(null);
                    //Toast.makeText(MyVakrangeeKendraPhotoActivity.this, "true", Toast.LENGTH_SHORT).show();
                } else {
                    txtOpenWed.setError("");
                    txtCloseWed.setError("");
                    // Toast.makeText(MyVakrangeeKendraPhotoActivity.this, strOpenWed + "not match" + strCloseWed + "in Wednesday", Toast.LENGTH_SHORT).show();
                }

                if (d == true) {

                    txtOpenThu.setError(null);
                    txtCloseThu.setError(null);
                    // Toast.makeText(MyVakrangeeKendraPhotoActivity.this, "true", Toast.LENGTH_SHORT).show();
                } else {
                    txtOpenThu.setError("");
                    txtCloseThu.setError("");
                    //Toast.makeText(MyVakrangeeKendraPhotoActivity.this, strOpenThu + "not match" + strCloseThu + "in Thursday", Toast.LENGTH_SHORT).show();
                }

                if (e == true) {
                    txtOpenFri.setError(null);
                    txtCloseFri.setError(null);
                    // Toast.makeText(MyVakrangeeKendraPhotoActivity.this, "true", Toast.LENGTH_SHORT).show();
                } else {
                    txtOpenFri.setError("");
                    txtCloseFri.setError("");
                    // Toast.makeText(MyVakrangeeKendraPhotoActivity.this, strOpenFri + "not match" + strCloseFri + "in Friday", Toast.LENGTH_SHORT).show();
                }


                if (f == true) {
                    txtOpenSat.setError(null);
                    txtCloseSat.setError(null);
                    // Toast.makeText(MyVakrangeeKendraPhotoActivity.this, "true", Toast.LENGTH_SHORT).show();
                } else {
                    txtOpenSat.setError("");
                    txtCloseSat.setError("");
                    // Toast.makeText(MyVakrangeeKendraPhotoActivity.this, strOpenSat + "not match" + strCloseSat + " In Saturday", Toast.LENGTH_SHORT).show();
                }

                if (g == true) {
                    txtOpenSun.setError(null);
                    txtCloseSun.setError(null);
                    //  Toast.makeText(MyVakrangeeKendraPhotoActivity.this, "true", Toast.LENGTH_SHORT).show();
                } else {
                    txtOpenSun.setError("");
                    txtCloseSun.setError("");
                    //Toast.makeText(MyVakrangeeKendraPhotoActivity.this, strOpenSun + "not match" + strCloseSun + "in Sunday", Toast.LENGTH_SHORT).show();
                }

                byte[] encodeByte1 = connection.getImage(1);
                getlati = Exif.getText().toString().trim();

                if (a == true && b == true && c == true && c == true && d == true && e == true && f == true && g == true) {
                    if (encodeByte1 == null) {
                        AlertDialogBoxInfo.alertDialogShow(MyVakrangeeKendraPhotoActivity.this, getResources().getString(R.string.frontImage));

                        // imageView1.setVisibility(View.INVISIBLE);
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte1, 0, encodeByte1.length);
                        imageView1.setImageBitmap(bitmap);

                        //----------------------------Pass date to server -----
                        try {


                            progress = new ProgressDialog(MyVakrangeeKendraPhotoActivity.this);
                            progress.setTitle(R.string.transcationprogress);
                            progress.setMessage(getResources().getString(R.string.pleaseWait));
                            progress.setCancelable(false);
                            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progress.show();

                            AsyncMyVakrangeeKendra myRequest = new AsyncMyVakrangeeKendra();
                            myRequest.execute();
                        } catch (Exception ex) {
                            ex.getMessage();
                        }

                    }
                } else {
                    Toast.makeText(MyVakrangeeKendraPhotoActivity.this, "Please Check Your Outlets Timing", Toast.LENGTH_SHORT).show();

                }

            }

        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public Uri setImageUri(int i) {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        performCropa(imgUri, i);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    private void performCropa(Uri picUri, int i) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
//set crop properties
            cropIntent.putExtra("crop", "true");
//indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
//indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
//retrieve data on return
            cropIntent.putExtra("return-data", true);
//start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, i);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public String getImagePath() {
        return imgPath;
    }

    private void camerapermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MyVakrangeeKendraPhotoActivity.this
                ,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MyVakrangeeKendraPhotoActivity.this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MyVakrangeeKendraPhotoActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
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


    private boolean CheckDates(String startDate, String endDate) {
        boolean b = false;

        try {
            //Date mToday = new Date();

            SimpleDateFormat sqlDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            SimpleDateFormat parseFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            SimpleDateFormat parseFormat1 = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

            Date start = parseFormat.parse(("30/07/1990 " + startDate));
            Date end = parseFormat1.parse(("30/07/1990 " + endDate));


            //String startTime24=displayFormat.format(startTime);
            //startTime24 = "1990-07-30 " + startTime24;


            //String endTime24=displayFormat.format(endTime);
            //endTime24 = "1990-07-30 " + endTime24;

            //String curTime = sqlDateTime.format(mToday);

            //Date start = sqlDateTime.parse(startTime24);
            //Date end = sqlDateTime.parse(endTime24);


            //Date userDate = sqlDateTime.parse(curTime);

//            if (end.before(start)) {
//                Calendar mCal = Calendar.getInstance();
//                mCal.setTime(end);
//                mCal.add(Calendar.DAY_OF_YEAR, 1);
//                end.setTime(mCal.getTimeInMillis());
//            }


            if (start.after(end)) {
                b = false;
            } else if (start.equals(end)) {
                b = false;
            } else {
                b = true;
            }

        } catch (ParseException e) {
            // Invalid date was entered
        } catch (Exception e) {
            e.getMessage();
        }
        return b;
    }


    public void onBackPressed() {

        Intent intent = new Intent(MyVakrangeeKendraPhotoActivity.this, MyVakrangeeKendra.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MyVakrangeeKendraPhotoActivity.this, MyVakrangeeKendra.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                break;
        }
        return true;
    }


    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case Time_PICKER_ID:

                return new CustomTimePickerDialog(this, TimePickerListener, hourOfDay, minute, false);
        }
        return null;
    }


    private CustomTimePickerDialog.OnTimeSetListener TimePickerListener =
            new CustomTimePickerDialog.OnTimeSetListener() {

                // while dialog box is closed, below method is called.
                @SuppressLint("SetTextI18n")
                public void onTimeSet(TimePicker view, int hour, int minute) {

                    SpannableString spannableString;
                    switch (timePickerInput) {
                        case R.id.txtOpenMon:


                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);

                            String strHrsToShow = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr = (ampm == 0) ? "AM" : "PM";
                            //strOpenmMon = strHrsToShow + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr;

                            strOpenmMon = convertTo24Hour(strHrsToShow + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr);
                            Log.e("OutdoorStartDate", strOpenmMon + " " + ampmStr);
                            txtOpenMon.setText(strOpenmMon);

                            //-------------
                            spannableString = new SpannableString(txtOpenMon.getText().toString());
                            spannableString.setSpan(new UnderlineSpan(), 0, txtOpenMon.getText().toString().length(), 0);
                            txtOpenMon.setText(spannableString);


                            break;

                        case R.id.txtCloseMon:

                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format2 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow2 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr2 = (ampm == 0) ? "AM" : "PM";
                            //strCloseMon = strHrsToShow2 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr2;

                            strCloseMon = convertTo24Hour(strHrsToShow2 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr2);
                            txtCloseMon.setText(strCloseMon);
                            Log.e("OutdoorStartDate Close", strCloseMon + " " + ampmStr2);
                            //---------spannableString------------------
                            spannableString = new SpannableString(txtCloseMon.getText().toString());
                            spannableString.setSpan(new UnderlineSpan(), 0, txtCloseMon.getText().toString().length(), 0);
                            txtCloseMon.setText(spannableString);

                            break;
                        case R.id.txtOpenTue:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format3 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String ampmStr3 = (ampm == 0) ? "AM" : "PM";
                            String strHrsToShow3 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            strOpenTue = convertTo24Hour(strHrsToShow3 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr3);
                            txtOpenTue.setText(strOpenTue);
                            //---------spannableString------------------
                            spannableString = new SpannableString(txtOpenTue.getText().toString());
                            spannableString.setSpan(new UnderlineSpan(), 0, txtOpenTue.getText().toString().length(), 0);
                            txtOpenTue.setText(spannableString);
                            break;

                        case R.id.txtCloseTue:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format4 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String ampmStr4 = (ampm == 0) ? "AM" : "PM";
                            String strHrsToShow4 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            strCloseTue = convertTo24Hour(strHrsToShow4 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr4);
                            txtCloseTue.setText(strCloseTue);
                            //---------spannableString------------------
                            spannableString = new SpannableString(txtCloseTue.getText().toString());
                            spannableString.setSpan(new UnderlineSpan(), 0, txtCloseTue.getText().toString().length(), 0);
                            txtCloseTue.setText(spannableString);
                            break;

                        case R.id.txtOpenWed:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format5 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow5 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr5 = (ampm == 0) ? "AM" : "PM";
                            strOpenWed = convertTo24Hour(strHrsToShow5 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr5);
                            txtOpenWed.setText(strOpenWed);
                            //---------spannableString------------------
                            spannableString = new SpannableString(txtOpenWed.getText().toString());
                            spannableString.setSpan(new UnderlineSpan(), 0, txtOpenWed.getText().toString().length(), 0);
                            txtOpenWed.setText(spannableString);
                            break;
                        case R.id.txtCloseWed:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format6 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow6 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr6 = (ampm == 0) ? "AM" : "PM";
                            strCloseWed = convertTo24Hour(strHrsToShow6 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr6);
                            txtCloseWed.setText(strCloseWed);
                            //---------spannableString------------------
                            spannableString = new SpannableString(txtCloseWed.getText().toString());
                            spannableString.setSpan(new UnderlineSpan(), 0, txtCloseWed.getText().toString().length(), 0);
                            txtCloseWed.setText(spannableString);
                            break;
                        case R.id.txtOpenThu:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format7 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow7 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr7 = (ampm == 0) ? "AM" : "PM";
                            strOpenThu = convertTo24Hour(strHrsToShow7 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr7);
                            txtOpenThu.setText(strOpenThu);
                            //---------spannableString------------------
                            spannableString = new SpannableString(txtOpenThu.getText().toString());
                            spannableString.setSpan(new UnderlineSpan(), 0, txtOpenThu.getText().toString().length(), 0);
                            txtOpenThu.setText(spannableString);
                            break;
                        case R.id.txtCloseThu:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format8 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow8 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr8 = (ampm == 0) ? "AM" : "PM";
                            strCloseThu = convertTo24Hour(strHrsToShow8 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr8);
                            txtCloseThu.setText(strCloseThu);
                            //---------spannableString------------------
                            spannableString = new SpannableString(txtCloseThu.getText().toString());
                            spannableString.setSpan(new UnderlineSpan(), 0, txtCloseThu.getText().toString().length(), 0);
                            txtCloseThu.setText(spannableString);
                            break;

                        case R.id.txtOpenFri:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format9 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow9 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr9 = (ampm == 0) ? "AM" : "PM";
                            strOpenFri = convertTo24Hour(strHrsToShow9 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr9);
                            txtOpenFri.setText(strOpenFri);
                            //---------spannableString------------------
                            spannableString = new SpannableString(txtOpenFri.getText().toString());
                            spannableString.setSpan(new UnderlineSpan(), 0, txtOpenFri.getText().toString().length(), 0);
                            txtOpenFri.setText(spannableString);
                            break;

                        case R.id.txtCloseFri:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format10 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow10 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr10 = (ampm == 0) ? "AM" : "PM";
                            strCloseFri = convertTo24Hour(strHrsToShow10 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr10);
                            txtCloseFri.setText(strCloseFri);
                            //---------spannableString------------------
                            spannableString = new SpannableString(txtCloseFri.getText().toString());
                            spannableString.setSpan(new UnderlineSpan(), 0, txtCloseFri.getText().toString().length(), 0);
                            txtCloseFri.setText(spannableString);
                            break;
                        case R.id.txtOpenSat:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format11 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShr11 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmSt11 = (ampm == 0) ? "AM" : "PM";
                            strOpenSat = convertTo24Hour(strHrsToShr11 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmSt11);
                            txtOpenSat.setText(strOpenSat);
                            //---------spannableString------------------
                            spannableString = new SpannableString(txtOpenSat.getText().toString());
                            spannableString.setSpan(new UnderlineSpan(), 0, txtOpenSat.getText().toString().length(), 0);
                            txtOpenSat.setText(spannableString);
                            break;
                        case R.id.txtCloseSat:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format12 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow12 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr12 = (ampm == 0) ? "AM" : "PM";
                            strCloseSat = convertTo24Hour(strHrsToShow12 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr12);
                            txtCloseSat.setText(strCloseSat);
                            //---------spannableString------------------
                            spannableString = new SpannableString(txtCloseSat.getText().toString());
                            spannableString.setSpan(new UnderlineSpan(), 0, txtCloseSat.getText().toString().length(), 0);
                            txtCloseSat.setText(spannableString);
                            break;
                        case R.id.txtOpenSun:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);
                            int hour12format13 = mCalen.get(Calendar.HOUR);
                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow13 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmSt13 = (ampm == 0) ? "AM" : "PM";
                            strOpenSun = convertTo24Hour(strHrsToShow13 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmSt13);
                            txtOpenSun.setText(strOpenSun);
                            //---------spannableString------------------
                            spannableString = new SpannableString(txtOpenSun.getText().toString());
                            spannableString.setSpan(new UnderlineSpan(), 0, txtOpenSun.getText().toString().length(), 0);
                            txtOpenSun.setText(spannableString);
                            break;

                        case R.id.txtCloseSun:
                            mCalen.set(Calendar.HOUR_OF_DAY, hour);
                            mCalen.set(Calendar.MINUTE, minute);

                            hourOfDay = mCalen.get(Calendar.HOUR_OF_DAY);
                            minute = mCalen.get(Calendar.MINUTE);
                            ampm = mCalen.get(Calendar.AM_PM);
                            String strHrsToShow14 = (mCalen.get(Calendar.HOUR) == 0) ? "12" : mCalen.get(Calendar.HOUR) + "";
                            String ampmStr14 = (ampm == 0) ? "AM" : "PM";
                            strCloseSun = convertTo24Hour(strHrsToShow14 + ":" + (minute < 10 ? "0" + minute : minute) + " " + ampmStr14);
                            txtCloseSun.setText(strCloseSun);
                            //---------spannableString------------------
                            spannableString = new SpannableString(txtCloseSun.getText().toString());
                            spannableString.setSpan(new UnderlineSpan(), 0, txtCloseSun.getText().toString().length(), 0);
                            txtCloseSun.setText(spannableString);
                            break;
                    }

                }
            };

    public static String convertTo24Hour(String Time) {
        DateFormat f1 = new SimpleDateFormat("hh:mm a"); //11:00 pm
        Log.e("time", Time);
        Date d = null;
        try {
            d = f1.parse(Time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("hh:mm a");
        String x = f2.format(d); // "23:00"
        Log.e("x", x);
        return x;
    }


    @Override
    public void onClick(View v) {
        timePickerInput = v.getId();
        showDialog(Time_PICKER_ID);
    }


    //=--------------------------------------------


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
                            selectedImagePath = getImagePath();
                            //---------------------
                            Bitmap bitmap = decodeFile(selectedImagePath);
                            imageView1.setPadding(8, 8, 8, 8);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                            byte[] byteArray = stream.toByteArray();
                            frontImage = byteArray;
                            connection.insertintoImageMaster(1, byteArray);
                            imageView1.setImageBitmap(decodeFile(selectedImagePath));
                            //--------------------


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

                    selectedImagePath = getImagePath();
                    //---------------------
                    Bitmap bitmap = decodeFile(selectedImagePath);
                    imageView2.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    byte[] byteArray = stream.toByteArray();
                    frontImage = byteArray;
                    connection.insertintoImageMaster(2, byteArray);
                    imageView2.setImageBitmap(decodeFile(selectedImagePath));


                   /* Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    imageView2.setImageBitmap(bitmap);
                    imageView2.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    connection.insertintoImageMasterWithLatlong(1, byteArray, vikid, currentDateandTime, getlati);
                    connection.insertintoImageMaster(2, byteArray);*/
                } else {
                    Toast.makeText(this, "Error while capturing Image", Toast.LENGTH_LONG).show();
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    selectedImagePath = getImagePath();
                    //---------------------
                    Bitmap bitmap = decodeFile(selectedImagePath);
                    imageView3.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    byte[] byteArray = stream.toByteArray();
                    frontImage = byteArray;
                    connection.insertintoImageMaster(3, byteArray);
                    imageView3.setImageBitmap(decodeFile(selectedImagePath));

                    /*Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    imageView3.setImageBitmap(bitmap);
                    imageView3.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    connection.insertintoImageMaster(3, byteArray);*/
                } else {
                    Toast.makeText(this, "Error while capturing Image", Toast.LENGTH_LONG).show();
                }
                break;
            case 4:
                if (resultCode == RESULT_OK) {
                    selectedImagePath = getImagePath();
                    //---------------------
                    Bitmap bitmap = decodeFile(selectedImagePath);
                    imageView4.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    byte[] byteArray = stream.toByteArray();
                    frontImage = byteArray;
                    connection.insertintoImageMaster(4, byteArray);
                    imageView4.setImageBitmap(decodeFile(selectedImagePath));


                    /*Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    imageView4.setImageBitmap(bitmap);
                    imageView4.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    connection.insertintoImageMaster(4, byteArray);*/

                } else {
                    Toast.makeText(this, "Error while capturing Image", Toast.LENGTH_LONG).show();
                }
                break;
            case 5:
                if (resultCode == RESULT_OK) {

                    selectedImagePath = getImagePath();
                    //---------------------
                    Bitmap bitmap = decodeFile(selectedImagePath);
                    imageView5.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    byte[] byteArray = stream.toByteArray();
                    frontImage = byteArray;
                    connection.insertintoImageMaster(5, byteArray);
                    imageView5.setImageBitmap(decodeFile(selectedImagePath));

                    /*
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    imageView5.setImageBitmap(bitmap);
                    imageView5.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    connection.insertintoImageMaster(5, byteArray);*/
                } else {
                    Toast.makeText(this, "Error while capturing Image", Toast.LENGTH_LONG).show();
                }
                break;
            case 6:
                if (resultCode == RESULT_OK) {

                    selectedImagePath = getImagePath();
                    //---------------------
                    Bitmap bitmap = decodeFile(selectedImagePath);
                    imageView6.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    byte[] byteArray = stream.toByteArray();
                    frontImage = byteArray;
                    connection.insertintoImageMaster(6, byteArray);
                    imageView6.setImageBitmap(decodeFile(selectedImagePath));

                    /*
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    imageView6.setImageBitmap(bitmap);
                    imageView6.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    connection.insertintoImageMaster(6, byteArray);*/
                } else {
                    Toast.makeText(this, "Error while capturing Image", Toast.LENGTH_LONG).show();
                }
                break;
            case 7:
                if (resultCode == RESULT_OK) {

                    selectedImagePath = getImagePath();
                    //---------------------
                    Bitmap bitmap = decodeFile(selectedImagePath);
                    imageView7.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    byte[] byteArray = stream.toByteArray();
                    frontImage = byteArray;
                    connection.insertintoImageMaster(7, byteArray);
                    imageView7.setImageBitmap(decodeFile(selectedImagePath));

                    /*
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    imageView7.setImageBitmap(bitmap);
                    imageView7.setPadding(8, 8, 8, 8);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    connection.insertintoImageMaster(7, byteArray);*/
                } else {
                    Toast.makeText(this, "Error while capturing Image", Toast.LENGTH_LONG).show();
                }
                break;

        }


    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

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
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            arrcury = String.valueOf(location.getAccuracy());
            altitude = String.valueOf(location.getAltitude());


            // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

                    //  btnSubmit.setVisibility(View.VISIBLE);


                } else {
                    // btnSubmit.setVisibility(View.INVISIBLE);
                    //Toast.makeText(this, "Accucry not match ,Please wait...", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(MyVakrangeeKendraPhotoActivity.this, "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

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


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MyVakrangeeKendraPhoto Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
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

            //displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == AppUtilsforLocationService.LocationConstants.SUCCESS_RESULT) {
                //  showToast(getString(R.string.address_found));


            }


        }

    }


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

    private class AsyncMyVakrangeeKendra extends AsyncTask<Void, Void, Void> {

        // ----------Common paramter
        Connection connection = new Connection(MyVakrangeeKendraPhotoActivity.this);
        String vkid = connection.getVkid();
        String tokenId = connection.getTokenId();
        String vkidd = EncryptionUtil.encryptString(vkid, getApplicationContext());
        String TokenId = EncryptionUtil.encryptString(tokenId, getApplicationContext());

        String deviceIdget = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());

        String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
        String imei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

        String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
        String simserialnumber = EncryptionUtil.encryptString(simSerial, getApplicationContext());

        //-------------------CheckBox click event
        String checkboxMon = checkBoxMon.isChecked() ? "N" : "Y";
        String checkboxTue = checkBoxTue.isChecked() ? "N" : "Y";
        String checkboxWed = checkBoxWed.isChecked() ? "N" : "Y";
        String checkboxThu = checkBoxThu.isChecked() ? "N" : "Y";
        String checkboxFri = checkBoxFri.isChecked() ? "N" : "Y";
        String checkboxSat = checkBoxSat.isChecked() ? "N" : "Y";
        String checkboxSun = checkBoxSun.isChecked() ? "N" : "Y";

        String isMondayClosed = EncryptionUtil.encryptString(checkboxMon, getApplicationContext());
        String isTuesdayClosed = EncryptionUtil.encryptString(checkboxTue, getApplicationContext());
        String isWednesdayClosed = EncryptionUtil.encryptString(checkboxWed, getApplicationContext());
        String isThursdayClosed = EncryptionUtil.encryptString(checkboxThu, getApplicationContext());
        String isFridayClosed = EncryptionUtil.encryptString(checkboxFri, getApplicationContext());
        String isSaturdayClosed = EncryptionUtil.encryptString(checkboxSat, getApplicationContext());
        String isSundayClosed = EncryptionUtil.encryptString(checkboxSun, getApplicationContext());

        //---------------------Edittext value When user select Time.

        String mondayO = EncryptionUtil.encryptString(txtOpenMon.getText().toString(), getApplicationContext());
        String mondayC = EncryptionUtil.encryptString(txtCloseMon.getText().toString(), getApplicationContext());
        String tuesdayO = EncryptionUtil.encryptString(txtOpenTue.getText().toString(), getApplicationContext());
        String tuesdayC = EncryptionUtil.encryptString(txtCloseTue.getText().toString(), getApplicationContext());
        String wednesdayO = EncryptionUtil.encryptString(txtOpenWed.getText().toString(), getApplicationContext());
        String wednesdayC = EncryptionUtil.encryptString(txtCloseWed.getText().toString(), getApplicationContext());
        String thursdayO = EncryptionUtil.encryptString(txtOpenThu.getText().toString(), getApplicationContext());
        String thursdayC = EncryptionUtil.encryptString(txtCloseThu.getText().toString(), getApplicationContext());
        String fridayO = EncryptionUtil.encryptString(txtOpenFri.getText().toString(), getApplicationContext());
        String fridayC = EncryptionUtil.encryptString(txtCloseFri.getText().toString(), getApplicationContext());
        String saturdayO = EncryptionUtil.encryptString(txtOpenSat.getText().toString(), getApplicationContext());
        String saturdayC = EncryptionUtil.encryptString(txtCloseSat.getText().toString(), getApplicationContext());
        String sundayO = EncryptionUtil.encryptString(txtOpenSun.getText().toString(), getApplicationContext());
        String sundayC = EncryptionUtil.encryptString(txtCloseSun.getText().toString(), getApplicationContext());

        //-----------------Image Caputure Value and Lat , Long

        String vkLatitude = EncryptionUtil.encryptString(latitude, getApplicationContext());
        String vkLongitude = EncryptionUtil.encryptString(longitude, getApplicationContext());

        byte[] encodeByte1 = connection.getImage(1);
        byte[] encodeByte2 = connection.getImage(2);
        byte[] encodeByte3 = connection.getImage(3);
        byte[] encodeByte4 = connection.getImage(4);
        byte[] encodeByte5 = connection.getImage(5);
        byte[] encodeByte6 = connection.getImage(6);
        byte[] encodeByte7 = connection.getImage(7);


        String frontage = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
        String leftWall = encodeByte2 != null ? (EncryptionUtil.encodeBase64(encodeByte2)) : "";
        String frontWall = encodeByte3 != null ? (EncryptionUtil.encodeBase64(encodeByte3)) : "";
        String rightWall = encodeByte4 != null ? (EncryptionUtil.encodeBase64(encodeByte4)) : "";
        String backWall = encodeByte5 != null ? (EncryptionUtil.encodeBase64(encodeByte5)) : "";
        String ceiling = encodeByte6 != null ? (EncryptionUtil.encodeBase64(encodeByte6)) : "";
        String floor = encodeByte7 != null ? (EncryptionUtil.encodeBase64(encodeByte7)) : "";

        String extra1 = encodeByte7 != null ? (EncryptionUtil.encodeBase64(encodeByte7)) : "";
        String extra2 = encodeByte7 != null ? (EncryptionUtil.encodeBase64(encodeByte7)) : "";
        String extra3 = encodeByte7 != null ? (EncryptionUtil.encodeBase64(encodeByte7)) : "";

        //this is the method to query

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {


                Connection connection = new Connection(MyVakrangeeKendraPhotoActivity.this);
                String vkid = connection.getVkid();
                String tokenId = connection.getTokenId();

                // int val = mServiceProvider.getServiceId();
//
                String deviceIdget = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

                String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getApplicationContext());

                String vkidd = EncryptionUtil.encryptString(vkid, getApplicationContext());
                String TokenId = EncryptionUtil.encryptString(tokenId, getApplicationContext());


                diplayServerResopnse = WebService.myVakrangeeKendra(vkidd, TokenId, imei, deviceid, simserialnumber,
                        isMondayClosed, isTuesdayClosed, isWednesdayClosed, isThursdayClosed, isFridayClosed, isSaturdayClosed, isSundayClosed,
                        mondayO, mondayC, tuesdayO, tuesdayC, wednesdayO, wednesdayC, thursdayO, thursdayC, fridayO, fridayC, saturdayO, saturdayC,
                        sundayO, sundayC, vkLatitude, vkLongitude, frontage, leftWall, frontWall, rightWall, backWall, ceiling, floor, extra1, extra2, extra3);

                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(MyVakrangeeKendraPhotoActivity.this, getResources().getString(R.string.Warning));

                e.printStackTrace();
                String message = null;
                Log.e(TAG + "Error", message);
                Log.i("TAG", ((message == null) ? "string null" : message));

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            progress.dismiss();
            // progress.dismiss();
            try {


                if (diplayServerResopnse == null) {

                    String message = null;
                    Log.i("TAG", ((message == null) ? "string null" : message));

                    //  Log.e(TAG + "Please Null error", diplayServerResopnse);
                } else if (diplayServerResopnse.equals("Vakrangee Kendra data updated successfully.")) {
                    AlertDialogBoxInfo.alertDialogShow(MyVakrangeeKendraPhotoActivity.this,
                            getResources().getString(R.string.imageuploadsuccessful));


                } else {
                    Log.e(TAG + "Error in Server", diplayServerResopnse);
                    // Toast.makeText(getApplicationContext(), "Error OTP ", Toast.LENGTH_SHORT).show();
                    AlertDialogBoxInfo.alertDialogShow(MyVakrangeeKendraPhotoActivity.this, getResources().getString(R.string.Warning));

                }
            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(MyVakrangeeKendraPhotoActivity.this, getResources().getString(R.string.Warning));

                e.printStackTrace();
            }

        }


    }


    public class CustomTimePickerDialog extends TimePickerDialog {

        private final static int TIME_PICKER_INTERVAL = 30;
        private TimePicker mTimePicker;
        private final OnTimeSetListener mTimeSetListener;

        public CustomTimePickerDialog(Context context, OnTimeSetListener listener,
                                      int hourOfDay, int minute, boolean is24HourView) {
            super(context, TimePickerDialog.THEME_HOLO_LIGHT, null, hourOfDay,
                    minute / TIME_PICKER_INTERVAL, is24HourView);
            mTimeSetListener = listener;
        }

        @Override
        public void updateTime(int hourOfDay, int minuteOfHour) {
            mTimePicker.setCurrentHour(hourOfDay);
            mTimePicker.setCurrentMinute(minuteOfHour / TIME_PICKER_INTERVAL);
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case BUTTON_POSITIVE:
                    if (mTimeSetListener != null) {
                        mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                                mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
                    }
                    break;
                case BUTTON_NEGATIVE:
                    cancel();
                    break;
            }
        }

        @Override
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            try {
                Class<?> classForid = Class.forName("com.android.internal.R$id");
                Field timePickerField = classForid.getField("timePicker");
                mTimePicker = (TimePicker) findViewById(timePickerField.getInt(null));
                Field field = classForid.getField("minute");

                NumberPicker minuteSpinner = (NumberPicker) mTimePicker
                        .findViewById(field.getInt(null));
                minuteSpinner.setMinValue(0);
                minuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
                List<String> displayedValues = new ArrayList<>();
                for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                    displayedValues.add(String.format("%02d", i));
                }
                minuteSpinner.setDisplayedValues(displayedValues
                        .toArray(new String[displayedValues.size()]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //---------------------------------------------------
}