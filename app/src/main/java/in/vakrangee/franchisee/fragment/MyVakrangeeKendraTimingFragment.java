package in.vakrangee.franchisee.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.MyVakrangeeKendraPhotoViewPager;
import in.vakrangee.franchisee.sitelayout.asyntask.FetchAddressIntentService;
import in.vakrangee.franchisee.task.AsyncGetmyVakrangeeKendraTimingsResponse;
import in.vakrangee.franchisee.task.AsyncGetmyVakrangeeKendraTimingsResponseParticular;
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
public class MyVakrangeeKendraTimingFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    Connection connection;
    View view;

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
    int Time_PICKER_ID = 0;


    Button btnSubmittime;
    //String dataUnderline;
    ProgressDialog progress;
    String strOpenmMon = "09:00 AM", strCloseMon = "09:00 PM";
    String strOpenTue = "09:00 AM", strCloseTue = "09:00 PM";
    String strOpenWed = "09:00 AM", strCloseWed = "09:00 PM";
    String strOpenThu = "09:00 AM", strCloseThu = "09:00 PM";
    String strOpenFri = "09:00 AM", strCloseFri = "09:00 PM";
    String strOpenSat = "09:00 AM", strCloseSat = "09:00 PM";
    String strOpenSun = "09:00 AM", strCloseSun = "09:00 PM";
    private String imgPath;
    // SpannableString contentOpen, contentClose;

    Context context;
    TelephonyManager telephonyManager;
    String diplayServerResopnse;
    MyVKMaster myVKMaster;
    //-----------------------------------

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
    String latitude; // latitude
    String longitude; // longitude
    String Lati, Longi;

    Button btnGetDrawLine;
    Button btnChangeMap;
    ImageView btnShareinfo;
    String strAccurcy;
    String strlatlong;
    // String dataUnderlineclose;
    InternetConnection internetConnection;

    @SuppressLint("ValidFragment")
    public MyVakrangeeKendraTimingFragment(MyVKMaster myVKMaster) {
        this.myVKMaster = myVKMaster;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");
        View view = inflater.inflate(R.layout.fragment_vakrangeekendra_timing, container, false);

        try {


            connection = new Connection(getActivity());

//

            telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);


            connection = new Connection(getActivity());
            //connection.openDatabase();

            //dataUnderline = "09:00 AM";
            //contentOpen = new SpannableString(dataUnderline);
            // contentOpen.setSpan(new UnderlineSpan(), 0, dataUnderline.length(), 0);

            //  dataUnderlineclose = "09:00 PM";
            //contentClose = new SpannableString(dataUnderlineclose);
            // contentClose.setSpan(new UnderlineSpan(), 0, dataUnderlineclose.length(), 0);


            btnSubmittime = (Button) view.findViewById(R.id.btnvakrangeTimeSubmit);
            txtOpenMon = (TextView) view.findViewById(R.id.txtOpenMon);
            txtCloseMon = (TextView) view.findViewById(R.id.txtCloseMon);


            txtOpenTue = (TextView) view.findViewById(R.id.txtOpenTue);
            txtCloseTue = (TextView) view.findViewById(R.id.txtCloseTue);

            txtOpenWed = (TextView) view.findViewById(R.id.txtOpenWed);
            txtCloseWed = (TextView) view.findViewById(R.id.txtCloseWed);

            txtOpenThu = (TextView) view.findViewById(R.id.txtOpenThu);
            txtCloseThu = (TextView) view.findViewById(R.id.txtCloseThu);

            txtOpenFri = (TextView) view.findViewById(R.id.txtOpenFri);
            txtCloseFri = (TextView) view.findViewById(R.id.txtCloseFri);

            txtOpenSat = (TextView) view.findViewById(R.id.txtOpenSat);
            txtCloseSat = (TextView) view.findViewById(R.id.txtCloseSat);

            txtOpenSun = (TextView) view.findViewById(R.id.txtOpenSun);
            txtCloseSun = (TextView) view.findViewById(R.id.txtCloseSun);


            checkBoxMon = (CheckBox) view.findViewById(R.id.checkBoxMon);
            checkBoxTue = (CheckBox) view.findViewById(R.id.checkBoxTue);
            checkBoxWed = (CheckBox) view.findViewById(R.id.checkBoxWed);
            checkBoxThu = (CheckBox) view.findViewById(R.id.checkBoxThu);
            checkBoxFri = (CheckBox) view.findViewById(R.id.checkBoxFri);
            checkBoxSat = (CheckBox) view.findViewById(R.id.checkBoxSat);
            checkBoxSun = (CheckBox) view.findViewById(R.id.checkBoxSun);


            txtOpenMon.setText(myVKMaster.getStrOpenmMon());
            txtOpenTue.setText(myVKMaster.getStrOpenTue());
            txtOpenWed.setText(myVKMaster.getStrOpenWed());
            txtOpenThu.setText(myVKMaster.getStrOpenThu());
            txtOpenFri.setText(myVKMaster.getStrOpenFri());
            txtOpenSat.setText(myVKMaster.getStrOpenSat());
            txtOpenSun.setText(myVKMaster.getStrOpenSat());

            txtCloseMon.setText(myVKMaster.getStrCloseMon());
            txtCloseTue.setText(myVKMaster.getStrCloseTue());
            txtCloseWed.setText(myVKMaster.getStrCloseWed());
            txtCloseThu.setText(myVKMaster.getStrCloseThu());
            txtCloseFri.setText(myVKMaster.getStrCloseFri());
            txtCloseSat.setText(myVKMaster.getStrCloseSat());
            txtCloseSun.setText(myVKMaster.getStrCloseSun());

            Log.e("monday ", myVKMaster.getMondayTimings());

            String checkm = myVKMaster.getMondayTimings();
            if (checkm.equals("uncheck")) {

                checkBoxMon.setChecked(false);

            } else {
                checkBoxMon.setChecked(true);
            }

            String checktue = myVKMaster.getTuesdayTimings();
            if (checktue.equals("uncheck")) {
                checkBoxTue.setChecked(false);
            } else {
                checkBoxTue.setChecked(true);
            }

            String checkwed = myVKMaster.getWednesdayTimings();
            if (checkwed.equals("uncheck")) {
                checkBoxWed.setChecked(false);
            } else {
                checkBoxWed.setChecked(true);
            }

            String checkth = myVKMaster.getThursdayTimings();
            if (checkth.equals("uncheck")) {
                checkBoxThu.setChecked(false);
            } else {
                checkBoxThu.setChecked(true);
            }

            String checkfri = myVKMaster.getFridayTimings();
            if (checkfri.equals("uncheck")) {
                checkBoxFri.setChecked(false);
            } else {
                checkBoxFri.setChecked(true);
            }

            String checkSat = myVKMaster.getSaturdayTimings();
            if (checkSat.equals("uncheck")) {
                checkBoxSat.setChecked(false);
            } else {
                checkBoxSat.setChecked(true);
            }

            String checkSun = myVKMaster.getSundayTimings();
            if (checkSun.equals("uncheck")) {
                checkBoxSun.setChecked(false);
            } else {
                checkBoxSun.setChecked(true);
            }


            // txtOpenWed.setText(myVKMaster.getStrOpenWed());
            //txtOpenThu.setText(myVKMaster.getStrOpenThu());
            // txtOpenFri.setText(myVKMaster.getStrOpenFri());
            // txtOpenSat.setText(myVKMaster.getStrOpenSat());
            // txtOpenSun.setText(myVKMaster.getStrOpenSun());
            //txtCloseMon.setText(myVKMaster.getStrCloseMon());
            // txtCloseTue.setText(myVKMaster.getStrCloseTue());
            //txtCloseWed.setText(myVKMaster.getStrCloseWed());
            //txtCloseThu.setText(myVKMaster.getStrCloseThu());
            // txtCloseFri.setText(myVKMaster.getStrCloseFri());
            // txtCloseSat.setText(myVKMaster.getStrCloseSat());
            // txtCloseSun.setText(myVKMaster.getStrCloseSun());


            checkBoxMon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        txtOpenMon.setClickable(true);
                        txtCloseMon.setClickable(true);
                        txtOpenMon.setTextColor(getResources().getColor(R.color.gray));
                        txtCloseMon.setTextColor(getResources().getColor(R.color.gray));

                        // txtOpenMon.setText(contentOpen);
                        // txtCloseMon.setText(contentClose);

                    } else {
// txtOpenMon.setText(contentOpen);
                        //  txtCloseMon.setText(contentClose);
                        txtOpenMon.setClickable(false);
                        txtCloseMon.setClickable(false);

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
                        // txtOpenTue.setText(contentOpen);
                        // txtCloseTue.setText(contentClose);

                    } else {


                        txtOpenTue.setClickable(false);
                        txtCloseTue.setClickable(false);
                        //  txtOpenTue.setText(contentOpen);
                        //  txtCloseTue.setText(contentClose);
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

                        //txtOpenWed.setText(contentOpen);
                        // txtCloseWed.setText(contentClose);

                    } else {


                        txtOpenWed.setClickable(false);
                        txtCloseWed.setClickable(false);
                        //txtOpenWed.setText(contentOpen);
                        // txtCloseWed.setText(contentClose);
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
                        // txtOpenThu.setText(contentOpen);
                        // txtCloseThu.setText(contentClose);

                    } else {


                        txtOpenThu.setClickable(false);
                        txtCloseThu.setClickable(false);
                        //txtOpenThu.setText(contentOpen);
                        // txtCloseThu.setText(contentClose);
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
                        //txtOpenFri.setText(contentOpen);
                        // txtCloseFri.setText(contentClose);

                    } else {

                        txtOpenFri.setClickable(false);
                        txtCloseFri.setClickable(false);
                        // txtOpenFri.setText(contentOpen);
                        // txtCloseFri.setText(contentClose);
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

                        //txtOpenSat.setText(contentOpen);
                        //  txtCloseSat.setText(contentClose);

                    } else {
                        txtOpenSat.setClickable(false);
                        txtCloseSat.setClickable(false);
                        // txtOpenSat.setText(contentOpen);
                        // txtCloseSat.setText(contentClose);
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

                        // txtOpenSun.setText(contentOpen);
                        //  txtCloseSun.setText(contentClose);

                    } else {

                        txtOpenSun.setClickable(false);
                        txtCloseSun.setClickable(false);
                        //  txtOpenSun.setText(contentOpen);
                        //  txtCloseSun.setText(contentClose);

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
            //new AsyncGetmyVakrangeeKendraTimingsResponse(getActivity(), myVKMaster).execute(vkidd, TokenId, imei, deviceid, simserialnumber);


            btnSubmittime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
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


                        if (a == true && b == true && c == true && c == true && d == true && e == true && f == true && g == true) {


                            //----------------------------Pass date to server -----
                            try {
                                connection = new Connection(getActivity());
                                //connection.openDatabase();


                                internetConnection = new InternetConnection(getActivity());

                                if (latitude == null) {
                                    Toast.makeText(getActivity(), "Please Turn on GPS setting", Toast.LENGTH_SHORT).show();
                                } else if (internetConnection.isConnectingToInternet() == false) {
                                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.internetCheck));

                                } else {
                                    AsyncMyVakrangeeKendra myRequest = new AsyncMyVakrangeeKendra();
                                    myRequest.execute();


                                }


                            } catch (Exception ex) {
                                ex.getMessage();


                            }
                        } else {
                            Toast.makeText(getActivity(), "Please Check Your Outlets Timing", Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

            });

            txtOpenMon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    createDialog(0).show();

                }
            });
            txtCloseMon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    createDialog(1).show();

                }
            });
            txtOpenTue.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    createDialog(2).show();

                }
            });
            txtCloseTue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog(3).show();

                }
            });
            txtOpenWed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog(4).show();

                }
            });
            txtCloseWed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog(5).show();

                }
            });
            txtOpenThu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog(6).show();

                }
            });
            txtCloseThu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog(7).show();

                }
            });
            txtOpenFri.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog(8).show();

                }
            });
            txtCloseFri.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog(9).show();

                }
            });
            txtOpenSat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog(10).show();

                }
            });
            txtCloseSat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog(11).show();

                }
            });
            txtOpenSun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog(12).show();

                }
            });
            txtCloseSun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialog(13).show();

                }
            });
            /*.........End custom title section........*/

            //-----------------------------------------

            mLocationMarkerText = (TextView) view.findViewById(R.id.locationMarkertext);
            mLocationAddress = (EditText) view.findViewById(R.id.Address);
            mLocationText = (TextView) view.findViewById(R.id.Locality);
            btnGetDrawLine = (Button) view.findViewById(R.id.getDrawLine);


            btnShareinfo = (ImageView) view.findViewById(R.id.btnShareinfo);
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


            //----------------------------------------


        } catch (Exception e) {
            e.getMessage();
        }
        return view;
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

    public Dialog createDialog(int id) {
        Time_PICKER_ID = id;
        switch (id) {
            case 0:
                return new CustomTimePickerDialog(getActivity(), TimePickerListener, hour, minute, false);
            case 1:
                return new CustomTimePickerDialog(getActivity(), TimePickerListener, hour, minute, false);
            case 2:
                return new CustomTimePickerDialog(getActivity(), TimePickerListener, hour, minute, false);
            case 3:
                return new CustomTimePickerDialog(getActivity(), TimePickerListener, hour, minute, false);
            case 4:
                return new CustomTimePickerDialog(getActivity(), TimePickerListener, hour, minute, false);
            case 5:
                return new CustomTimePickerDialog(getActivity(), TimePickerListener, hour, minute, false);
            case 6:
                return new CustomTimePickerDialog(getActivity(), TimePickerListener, hour, minute, false);
            case 7:
                return new CustomTimePickerDialog(getActivity(), TimePickerListener, hour, minute, false);
            case 8:
                return new CustomTimePickerDialog(getActivity(), TimePickerListener, hour, minute, false);
            case 9:
                return new CustomTimePickerDialog(getActivity(), TimePickerListener, hour, minute, false);
            case 10:
                return new CustomTimePickerDialog(getActivity(), TimePickerListener, hour, minute, false);
            case 11:
                return new CustomTimePickerDialog(getActivity(), TimePickerListener, hour, minute, false);
            case 12:
                return new CustomTimePickerDialog(getActivity(), TimePickerListener, hour, minute, false);
            case 13:
                return new CustomTimePickerDialog(getActivity(), TimePickerListener, hour, minute, false);


        }
        return null;
    }

    private CustomTimePickerDialog.OnTimeSetListener TimePickerListener =
            new CustomTimePickerDialog.OnTimeSetListener() {

                // while dialog box is closed, below method is called.
                @SuppressLint("SetTextI18n")
                public void onTimeSet(TimePicker view, int hour, int minute) {

                    SpannableString spannableString;
                    switch (Time_PICKER_ID) {
                        case 0:


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
                            //  spannableString = new SpannableString(txtOpenMon.getText().toString());
                            // spannableString.setSpan(new UnderlineSpan(), 0, txtOpenMon.getText().toString().length(), 0);
                            // txtOpenMon.setText(spannableString);


                            break;

                        case 1:

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

                            //spannableString = new SpannableString(txtCloseMon.getText().toString());
                            // spannableString.setSpan(new UnderlineSpan(), 0, txtCloseMon.getText().toString().length(), 0);
                            // txtCloseMon.setText(spannableString);

                            break;
                        case 2:
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
                            // spannableString = new SpannableString(txtOpenTue.getText().toString());
                            //  spannableString.setSpan(new UnderlineSpan(), 0, txtOpenTue.getText().toString().length(), 0);
                            //txtOpenTue.setText(spannableString);
                            break;

                        case 3:
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
                            // spannableString = new SpannableString(txtCloseTue.getText().toString());
                            //  spannableString.setSpan(new UnderlineSpan(), 0, txtCloseTue.getText().toString().length(), 0);
                            //  txtCloseTue.setText(spannableString);
                            break;

                        case 4:
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
                            // spannableString = new SpannableString(txtOpenWed.getText().toString());
                            // spannableString.setSpan(new UnderlineSpan(), 0, txtOpenWed.getText().toString().length(), 0);
                            // txtOpenWed.setText(spannableString);
                            break;
                        case 5:
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
                            // spannableString = new SpannableString(txtCloseWed.getText().toString());
                            // spannableString.setSpan(new UnderlineSpan(), 0, txtCloseWed.getText().toString().length(), 0);
                            //  txtCloseWed.setText(spannableString);
                            break;
                        case 6:
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
                            //  spannableString = new SpannableString(txtOpenThu.getText().toString());
                            // spannableString.setSpan(new UnderlineSpan(), 0, txtOpenThu.getText().toString().length(), 0);
                            //  txtOpenThu.setText(spannableString);
                            break;
                        case 7:
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
                            //  spannableString = new SpannableString(txtCloseThu.getText().toString());
                            //  spannableString.setSpan(new UnderlineSpan(), 0, txtCloseThu.getText().toString().length(), 0);
                            //  txtCloseThu.setText(spannableString);
                            break;

                        case 8:
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
                            // spannableString = new SpannableString(txtOpenFri.getText().toString());
                            //  spannableString.setSpan(new UnderlineSpan(), 0, txtOpenFri.getText().toString().length(), 0);
                            //  txtOpenFri.setText(spannableString);
                            break;

                        case 9:
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
                            // spannableString = new SpannableString(txtCloseFri.getText().toString());
                            // spannableString.setSpan(new UnderlineSpan(), 0, txtCloseFri.getText().toString().length(), 0);
                            // txtCloseFri.setText(spannableString);
                            break;
                        case 10:
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
                            // spannableString = new SpannableString(txtOpenSat.getText().toString());
                            // spannableString.setSpan(new UnderlineSpan(), 0, txtOpenSat.getText().toString().length(), 0);
                            //  txtOpenSat.setText(spannableString);
                            break;
                        case 11:
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
                            // spannableString = new SpannableString(txtCloseSat.getText().toString());
                            // spannableString.setSpan(new UnderlineSpan(), 0, txtCloseSat.getText().toString().length(), 0);
                            // txtCloseSat.setText(spannableString);
                            break;
                        case 12:
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
                            //  spannableString = new SpannableString(txtOpenSun.getText().toString());
                            // spannableString.setSpan(new UnderlineSpan(), 0, txtOpenSun.getText().toString().length(), 0);
                            //txtOpenSun.setText(spannableString);
                            break;

                        case 13:
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
                            //spannableString = new SpannableString(txtCloseSun.getText().toString());
                            //spannableString.setSpan(new UnderlineSpan(), 0, txtCloseSun.getText().toString().length(), 0);
                            //txtCloseSun.setText(spannableString);
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        timePickerInput = v.getId();
        getActivity().showDialog(Time_PICKER_ID);
    }


    public class CustomTimePickerDialog extends TimePickerDialog {
        private final boolean mIs24HourView = false;
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


    private class AsyncMyVakrangeeKendra extends AsyncTask<Void, Void, Void> {


        //-------------------CheckBox click event
        String checkboxMon = checkBoxMon.isChecked() ? "N" : "Y";
        String checkboxTue = checkBoxTue.isChecked() ? "N" : "Y";
        String checkboxWed = checkBoxWed.isChecked() ? "N" : "Y";
        String checkboxThu = checkBoxThu.isChecked() ? "N" : "Y";
        String checkboxFri = checkBoxFri.isChecked() ? "N" : "Y";
        String checkboxSat = checkBoxSat.isChecked() ? "N" : "Y";
        String checkboxSun = checkBoxSun.isChecked() ? "N" : "Y";

        String isMondayClosed = EncryptionUtil.encryptString(checkboxMon, getActivity());
        String isTuesdayClosed = EncryptionUtil.encryptString(checkboxTue, getActivity());
        String isWednesdayClosed = EncryptionUtil.encryptString(checkboxWed, getActivity());
        String isThursdayClosed = EncryptionUtil.encryptString(checkboxThu, getActivity());
        String isFridayClosed = EncryptionUtil.encryptString(checkboxFri, getActivity());
        String isSaturdayClosed = EncryptionUtil.encryptString(checkboxSat, getActivity());
        String isSundayClosed = EncryptionUtil.encryptString(checkboxSun, getActivity());

        //---------------------Edittext value When user select Time.

        String mondayO = EncryptionUtil.encryptString(txtOpenMon.getText().toString(), getActivity());
        String mondayC = EncryptionUtil.encryptString(txtCloseMon.getText().toString(), getActivity());
        String tuesdayO = EncryptionUtil.encryptString(txtOpenTue.getText().toString(), getActivity());
        String tuesdayC = EncryptionUtil.encryptString(txtCloseTue.getText().toString(), getActivity());
        String wednesdayO = EncryptionUtil.encryptString(txtOpenWed.getText().toString(), getActivity());
        String wednesdayC = EncryptionUtil.encryptString(txtCloseWed.getText().toString(), getActivity());
        String thursdayO = EncryptionUtil.encryptString(txtOpenThu.getText().toString(), getActivity());
        String thursdayC = EncryptionUtil.encryptString(txtCloseThu.getText().toString(), getActivity());
        String fridayO = EncryptionUtil.encryptString(txtOpenFri.getText().toString(), getActivity());
        String fridayC = EncryptionUtil.encryptString(txtCloseFri.getText().toString(), getActivity());
        String saturdayO = EncryptionUtil.encryptString(txtOpenSat.getText().toString(), getActivity());
        String saturdayC = EncryptionUtil.encryptString(txtCloseSat.getText().toString(), getActivity());
        String sundayO = EncryptionUtil.encryptString(txtOpenSun.getText().toString(), getActivity());
        String sundayC = EncryptionUtil.encryptString(txtCloseSun.getText().toString(), getActivity());

        //-----------------Image Caputure Value and Lat , Long

        String lat = latitude;
        String log = longitude;
        String vkLatitude = EncryptionUtil.encryptString(myVKMaster.getLatitude(), getActivity());
        String vkLongitude = EncryptionUtil.encryptString(myVKMaster.getLongtitude(), getActivity());
        /*
        byte[] encodeByte1 = connection.getImagewithname("frontage");
        byte[] encodeByte2 = connection.getImagewithname("leftWall");
        byte[] encodeByte3 = connection.getImagewithname("frontWall");
        byte[] encodeByte4 = connection.getImagewithname("rightWall");
        byte[] encodeByte5 = connection.getImagewithname("backWall");
        byte[] encodeByte6 = connection.getImagewithname("ceiling");
        byte[] encodeByte7 = connection.getImagewithname("floor");
        byte[] getEncodeExtra1 = connection.getImagewithname("extra1");
        byte[] getEncodeExtra2 = connection.getImagewithname("extra2");
        byte[] getEncodeExtra3 = connection.getImagewithname("extra3");
*/
        byte[] encodeByte1;
        byte[] encodeByte2;
        byte[] encodeByte3;
        byte[] encodeByte4;
        byte[] encodeByte5;
        byte[] encodeByte6;
        byte[] encodeByte7;
        byte[] getEncodeExtra1;
        byte[] getEncodeExtra2;
        byte[] getEncodeExtra3;


        String frontage = encodeByte1 != null ? (EncryptionUtil.encodeBase64(encodeByte1)) : "";
        String leftWall = encodeByte2 != null ? (EncryptionUtil.encodeBase64(encodeByte2)) : "";
        String frontWall = encodeByte3 != null ? (EncryptionUtil.encodeBase64(encodeByte3)) : "";
        String rightWall = encodeByte4 != null ? (EncryptionUtil.encodeBase64(encodeByte4)) : "";
        String backWall = encodeByte5 != null ? (EncryptionUtil.encodeBase64(encodeByte5)) : "";
        String ceiling = encodeByte6 != null ? (EncryptionUtil.encodeBase64(encodeByte6)) : "";
        String floor = encodeByte7 != null ? (EncryptionUtil.encodeBase64(encodeByte7)) : "";

        String extra1 = getEncodeExtra1 != null ? (EncryptionUtil.encodeBase64(getEncodeExtra1)) : "";
        String extra2 = getEncodeExtra2 != null ? (EncryptionUtil.encodeBase64(getEncodeExtra2)) : "";
        String extra3 = getEncodeExtra3 != null ? (EncryptionUtil.encodeBase64(getEncodeExtra3)) : "";

        //this is the method to query

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");

            progress = new ProgressDialog(getActivity());
            progress.setTitle(R.string.updateTiming);
            progress.setMessage(getResources().getString(R.string.pleaseWait));
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {


                Connection connection = new Connection(getActivity());
                String vkid = connection.getVkid();
                String tokenId = connection.getTokenId();

                String deviceIdget = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceIdget, getActivity());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getActivity());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getActivity());

                String simSerial = CommonUtils.getSimSerialNumber(getActivity());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getActivity());


                String UserID = EncryptionUtil.encryptString(vkid, getActivity());
                String TokenId = EncryptionUtil.encryptString(tokenId, getActivity());
                String VKID = EncryptionUtil.encryptString(myVKMaster.getUserId(), getActivity());

                if (vkid.toUpperCase().startsWith("VL") || vkid.toUpperCase().startsWith("VA")) {

                    diplayServerResopnse = WebService.myVakrangeeKendraParticular(UserID, VKID, TokenId, imei, deviceid, simserialnumber,
                            isMondayClosed, isTuesdayClosed, isWednesdayClosed, isThursdayClosed, isFridayClosed, isSaturdayClosed, isSundayClosed,
                            mondayO, mondayC, tuesdayO, tuesdayC, wednesdayO, wednesdayC, thursdayO, thursdayC, fridayO, fridayC, saturdayO, saturdayC,
                            sundayO, sundayC, vkLatitude, vkLongitude, frontage, leftWall, frontWall, rightWall, backWall, ceiling, floor, extra1, extra2, extra3);


                } else {
                    diplayServerResopnse = WebService.myVakrangeeKendra(UserID, TokenId, imei, deviceid, simserialnumber,
                            isMondayClosed, isTuesdayClosed, isWednesdayClosed, isThursdayClosed, isFridayClosed, isSaturdayClosed, isSundayClosed,
                            mondayO, mondayC, tuesdayO, tuesdayC, wednesdayO, wednesdayC, thursdayO, thursdayC, fridayO, fridayC, saturdayO, saturdayC,
                            sundayO, sundayC, vkLatitude, vkLongitude, frontage, leftWall, frontWall, rightWall, backWall, ceiling, floor, extra1, extra2, extra3);

                }


                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));

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
                    //  AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.imageuploadsuccessful));
                    String vkidd = EncryptionUtil.encryptString(connection.getVkid(), getActivity());
                    String TokenID = EncryptionUtil.encryptString(connection.getTokenId(), getActivity());

                    String deviceIdget = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                    String deviceid = EncryptionUtil.encryptString(deviceIdget, getActivity());

                    String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getActivity());
                    String imei = EncryptionUtil.encryptString(deviceIDAndroid, getActivity());

                    String simSerial = CommonUtils.getSimSerialNumber(getActivity());
                    String simserialnumber = EncryptionUtil.encryptString(simSerial, getActivity());

                    String VKID = EncryptionUtil.encryptString(myVKMaster.getUserId(), getActivity());
                    if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {

                        new AsyncGetmyVakrangeeKendraTimingsResponseParticular(getActivity(), myVKMaster.getUserId()).execute(vkidd, VKID, TokenID, imei, deviceid, simserialnumber);


                    } else {
                        new AsyncGetmyVakrangeeKendraTimingsResponse(getActivity()).execute(vkidd, TokenID, imei, deviceid, simserialnumber);

                    }


                } else if (diplayServerResopnse.equals("My Vakrangee Kendra information added successfully.")) {
                    // AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.imageuploadsuccessful));
                    String vkidd = EncryptionUtil.encryptString(connection.getVkid(), getActivity());
                    String TokenID = EncryptionUtil.encryptString(connection.getTokenId(), getActivity());

                    String deviceIdget = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                    String deviceid = EncryptionUtil.encryptString(deviceIdget, getActivity());

                    String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getActivity());
                    String imei = EncryptionUtil.encryptString(deviceIDAndroid, getActivity());

                    String simSerial = CommonUtils.getSimSerialNumber(getActivity());
                    String simserialnumber = EncryptionUtil.encryptString(simSerial, getActivity());
                    String VKID = EncryptionUtil.encryptString(myVKMaster.getUserId(), getActivity());
                    //  new AsyncGetmyVakrangeeKendraTimingsResponse(getActivity()).execute(vkidd, TokenID, imei, deviceid, simserialnumber);
                    new AsyncGetmyVakrangeeKendraTimingsResponseParticular(getActivity(), myVKMaster.getUserId()).execute(vkidd, VKID, TokenID, imei, deviceid, simserialnumber);


                } else {
                    Log.e(TAG + "Error in Server", diplayServerResopnse);
                    // Toast.makeText(getApplicationContext(), "Error OTP ", Toast.LENGTH_SHORT).show();
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));

                }


                ((MyVakrangeeKendraPhotoViewPager) getActivity()).selectFragment(2);
            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));

                e.printStackTrace();
            }

        }


    }


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

    public void changeMap(final Location location) {

        Log.d(TAG, "Reaching map" + mMap);


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

        try {


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

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Sorry! unable detect locationz", Toast.LENGTH_SHORT).show();
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
                    //connection.openDatabase();
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
    public void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null)
                changeMap(location);
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            //arrcury = String.valueOf(location.getAccuracy());
            //altitude = String.valueOf(location.getAltitude());
            // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
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
