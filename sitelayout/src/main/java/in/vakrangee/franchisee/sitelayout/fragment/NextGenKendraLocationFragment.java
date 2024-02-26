package in.vakrangee.franchisee.sitelayout.fragment;

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
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import in.vakrangee.franchisee.sitelayout.NextGenPhotoViewPager;
import in.vakrangee.franchisee.sitelayout.NextGenViewPager;
import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.Utils;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncSaveSiteReadinessVerification;
import in.vakrangee.franchisee.sitelayout.asyntask.FetchAddressIntentService;
import in.vakrangee.franchisee.sitelayout.mendatorybranding.MandatoryBrandingActivity;
import in.vakrangee.franchisee.sitelayout.sitecommencement.NextGenSiteCommencementActivity;
import in.vakrangee.franchisee.sitelayout.sitecommencement.NextGenSiteCommencementViewPager;
import in.vakrangee.franchisee.sitelayout.sitereadiness.SiteReadinessActivity;
import in.vakrangee.supercore.franchisee.commongui.animation.AnimationHanndler;
import in.vakrangee.supercore.franchisee.impl.GeoCordinatesImpl;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.FranchiseeRemarkDetails;
import in.vakrangee.supercore.franchisee.model.GeoCordinates;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.AppUtilsforLocationService;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.GUIUtils;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.JSONUtils;
import in.vakrangee.supercore.franchisee.webservice.WebService;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class NextGenKendraLocationFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    View view;
    double latLong;
    private GoogleMap mMap;
    private Circle mCircle;
    private Marker mMarker;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static String TAG = "MAP LOCATION";
    TextView mLocationMarkerText;
    private LatLng mCenterLatLong;
    Toolbar toolbar;
    private AddressResultReceiver mResultReceiver;

    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;
    EditText mLocationAddress;
    TextView mLocationText;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    Toolbar mToolbar;
    String Lati, Longi;

    Button btnGetDrawLine;
    ImageView btnShareinfo;
    String strAccurcy;
    String latitude, longitude;
    //    String strlat, strLong;
//    String strlatlong;
    FranchiseeDetails franchiseeDetails;
    ProgressDialog progress;
    TelephonyManager telephonyManager;
    String diplayServerResopnse;

    Connection connection;
    InternetConnection internetConnection;

    TextView txtLocationRemarks;
    FrameLayout frameMapLayout;

    //FloatingActionButton fabShareinfo;
    ImageView fabShareinfo;
    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);

    boolean isSiteReadinessOrWorkInProgress = false;
    boolean isSiteReadinessVerification = false;
    private boolean isSiteCommencement = false;
    private boolean isSiteWorkCompletion = false;
    private boolean IsSiteVisit = false;
    private boolean IsKendraLocationCheckEnabled = false;
    private MapView mapView;
    private String modetype;
    private boolean isAdhoc = false;
    private LinearLayout layoutVerified;
    private CheckBox checkBoxProfileVerify;
    private TextView textViewSearchAddress;
    private AsyncSaveSiteReadinessVerification asyncSaveSiteReadinessVerification = null;
    private GPSTracker gpsTracker;
    private AlertDialog alert = null;
    private AsyncValidateLocationChecks asyncValidateLocationChecks = null;
    private static final int LOCATION_TAB_POSITION = 1;
    private static boolean IsCameraPosCalledForFirst = true;
    private boolean IsMarkerChangeAllowed = true;
    private static final String NEXTGEN_PHOTO_VIEW_PAGER_CONST = "NextGenPhotoViewPager";
    private static final String SITE_READINESS_ACTIVITY_CONST = "SiteReadinessActivity";
    private static final String MANDATORY_BRANDING_ACTIVITY_CONST = "MandatoryBrandingActivity";

    public interface IGetResult {
        void onResult(String status, String result);
    }

    public NextGenKendraLocationFragment() {
    }

    @SuppressLint("ValidFragment")
    public NextGenKendraLocationFragment(FranchiseeDetails franchiseeDetails) {

        this.franchiseeDetails = franchiseeDetails;
    }

    public NextGenKendraLocationFragment(String modetype, FranchiseeDetails franchiseeDetails) {

        this.modetype = modetype;
        this.franchiseeDetails = franchiseeDetails;
        if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_WORK_COMMENCEMENT)) {
            isSiteCommencement = true;
        } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXT_GEN_WORK_IN_PROGRESS)) {
            isSiteReadinessOrWorkInProgress = true;
        } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_WORK_COMPLETION_INTIMATION)) {
            isSiteWorkCompletion = true;
        } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION)) {
            isSiteReadinessVerification = true;
            IsKendraLocationCheckEnabled = true;
        }else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION)) {
            //isSiteReadinessVerification = true;
           // IsKendraLocationCheckEnabled = true;
        }else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_INAUGURATION_RELUNCH_COMPLETED)) {
          //  isSiteReadinessVerification = true;
           // IsKendraLocationCheckEnabled = true;
        }else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_KENDRA_INTERIORS_COMPLETED)) {
           // isSiteReadinessVerification = true;
            //IsKendraLocationCheckEnabled = true;
        } else {
            IsSiteVisit = true;
        }
    }

    public NextGenKendraLocationFragment(FranchiseeDetails franchiseeDetails, boolean isSiteReadinessOrWorkInProgress) {
        this.franchiseeDetails = franchiseeDetails;
        this.isSiteReadinessOrWorkInProgress = isSiteReadinessOrWorkInProgress;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_vakrangee_kendra_location_fragment_next_gen, container, false);

        try {

            // Get App MODE
            isAdhoc = Constants.ENABLE_ADHOC_MODE || Constants.ENABLE_FRANCHISEE_MODE;
            gpsTracker = new GPSTracker(getContext());

            telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            connection = new Connection(getActivity());
            internetConnection = new InternetConnection(getActivity());
            telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

            mLocationMarkerText = (TextView) view.findViewById(R.id.locationMarkertext);
            mLocationAddress = (EditText) view.findViewById(R.id.Address);
            mLocationText = (TextView) view.findViewById(R.id.Locality);
            mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
            txtLocationRemarks = (TextView) view.findViewById(R.id.txtLocationRemarks);
            frameMapLayout = (FrameLayout) view.findViewById(R.id.frameMapLayout);
            layoutVerified = view.findViewById(R.id.layoutVerified);
            checkBoxProfileVerify = view.findViewById(R.id.checkBoxProfileVerify);
            textViewSearchAddress = (TextView) view.findViewById(R.id.textViewSearchAddress);

            //Initialize latitude and Longitude
            latitude = franchiseeDetails.getLatitude();
            longitude = franchiseeDetails.getLongitude();
            btnGetDrawLine = (Button) view.findViewById(R.id.getDrawLine);
            mLocationText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    openAutocompleteActivity();

                }


            });
            textViewSearchAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openAutocompleteActivity();
                }
            });
            fabShareinfo = (ImageView) view.findViewById(R.id.fabShareinfo);
            fabShareinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AnimationHanndler.bubbleAnimation(getContext(), v);
                    popupdisplay();
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
            mapView = (MapView) view.findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);

            mResultReceiver = new AddressResultReceiver(new Handler());

            if (checkPlayServices()) {
                // If this check succeeds, proceed with normal processing.
                // Otherwise, prompt user to get valid Play Services APK.
                if (!isSiteReadinessVerification) {
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
                }
                buildGoogleApiClient();
            } else {
                Toast.makeText(getActivity(), "Location not supported in this device", Toast.LENGTH_SHORT).show();
            }

            /*
                This fragment is also used into Site Readiness and Work In Progress module. Need to handle what to be done or not in case of site readiness and Work In Progress
                1. Hide Save Button (fabShareinfo)
                2. No need to perform check Status and Remarks.
            */


            //Hide Update Location button when VA/VL Logins
            if (isSiteReadinessVerification) {
                if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {
                    fabShareinfo.setVisibility(View.INVISIBLE);

                } else {
                    fabShareinfo.setVisibility(View.VISIBLE);
                }
            }

            //TODO: Show Remarks Based on Status
            checkStatusAndRemarks();

            return view;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void IsReviewed(boolean IsReviewed) {
        franchiseeDetails.setNeedToBeReviewed(IsReviewed);
        if (franchiseeDetails.isNeedToBeReviewed())
            layoutVerified.setVisibility(View.VISIBLE);
        else
            layoutVerified.setVisibility(View.GONE);

        checkBoxProfileVerify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                franchiseeDetails.setLocationReviewed(b);

                if (getActivity().getClass().getSimpleName().equalsIgnoreCase(NEXTGEN_PHOTO_VIEW_PAGER_CONST)) {
                    ((NextGenPhotoViewPager) getActivity()).setFranchiseeDetails(franchiseeDetails);
                } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase(SITE_READINESS_ACTIVITY_CONST)) {
                    ((SiteReadinessActivity) getActivity()).setFranchiseeDetails(franchiseeDetails);
                }else if (getActivity().getClass().getSimpleName().equalsIgnoreCase(MANDATORY_BRANDING_ACTIVITY_CONST)) {
                    ((MandatoryBrandingActivity) getActivity()).setFranchiseeDetails(franchiseeDetails);
                } else {
                    ((NextGenSiteCommencementActivity) getActivity()).setFranchiseeDetails(franchiseeDetails);
                }
            }
        });
    }

    //region Check Status and Remarks and Perform Action
    public void checkStatusAndRemarks() {
        // Check Allow to Update Site Visit Data or not.
        if (!franchiseeDetails.isAllowToEdit()) {
            GUIUtils.setViewAndChildrenEnabled(frameMapLayout, false);
            GUIUtils.setViewAndChildrenEnabled(textViewSearchAddress, false);
        }

        // Check Status and Set Remarks
        if ((franchiseeDetails.getStatus() == NextGenViewPager.SITE_SEND_BACK_FOR_CORRECTION) ||
                (isSiteReadinessOrWorkInProgress &&
                        franchiseeDetails.getStatus() == NextGenSiteCommencementViewPager.SITE_SEND_BACK_FOR_CORRECTION)) {
            FranchiseeRemarkDetails franchiseeRemarkDetails = franchiseeDetails.getFranchiseeRemarkDetails();
            if (franchiseeRemarkDetails != null) {
                String locRemarks = franchiseeRemarkDetails.getLOCATION();
                if (!TextUtils.isEmpty(locRemarks)) {
                    txtLocationRemarks.setVisibility(View.VISIBLE);
                    txtLocationRemarks.setText(Html.fromHtml("<b>Location:</b>&nbsp;" + locRemarks));
                    // Animate Remarks

                }
            }
        }
    }
    //endregion

    public void updateProfileData() {
        FranchiseeDetails tempFranchiseeDetails = null;
        if (getActivity().getClass().getSimpleName().equalsIgnoreCase(NEXTGEN_PHOTO_VIEW_PAGER_CONST)) {
            tempFranchiseeDetails = ((NextGenPhotoViewPager) getActivity()).getFranchiseeDetails();

        } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase(SITE_READINESS_ACTIVITY_CONST)) {
            tempFranchiseeDetails = ((SiteReadinessActivity) getActivity()).getFranchiseeDetails();
        } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase(MANDATORY_BRANDING_ACTIVITY_CONST)) {
            tempFranchiseeDetails = ((MandatoryBrandingActivity) getActivity()).getFranchiseeDetails();
        } else {
            tempFranchiseeDetails = ((NextGenSiteCommencementActivity) getActivity()).getFranchiseeDetails();
        }

        if (tempFranchiseeDetails == null)
            return;
        franchiseeDetails.setConsentStatus(tempFranchiseeDetails.getConsentStatus());
        franchiseeDetails.setWelcomeMailStatus(tempFranchiseeDetails.getWelcomeMailStatus());
        franchiseeDetails.setCallReceivedStatus(tempFranchiseeDetails.getCallReceivedStatus());
        franchiseeDetails.setLogisticsPaymentStatus(tempFranchiseeDetails.getLogisticsPaymentStatus());
        franchiseeDetails.setLogisticsPaymentDate(tempFranchiseeDetails.getLogisticsPaymentDate());
        franchiseeDetails.setGstRegisteredStatus(tempFranchiseeDetails.getGstRegisteredStatus());
        franchiseeDetails.setGstNumber(tempFranchiseeDetails.getGstNumber());
        franchiseeDetails.setGstAddress(tempFranchiseeDetails.getGstAddress());
        franchiseeDetails.setGstImage(tempFranchiseeDetails.getGstImage());
        franchiseeDetails.setLocationStatus(tempFranchiseeDetails.getLocationStatus());
    }

    private void popupdisplay() {
        try {

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //inflate layout from xml. you must create an xml layout file in res/layout first
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View layout = inflater.inflate(R.layout.popmvakrangeelocation, null);
            builder.setView(layout);

            TextView txtVerify = (TextView) layout.findViewById(R.id.pleaseVerfity);
            txtVerify.setTypeface(Typeface.SANS_SERIF);
            if (isSiteReadinessOrWorkInProgress) {
                txtVerify.setText("Do you want to save your Vakrangee Kendra Location details, Please confirm ?");
            }

            TextView txtlatlong = (TextView) layout.findViewById(R.id.txtlat);
            txtlatlong.setTypeface(Typeface.SANS_SERIF);

            TextView txtaccuracy = (TextView) layout.findViewById(R.id.txtaccuracy);
            TextView txtAddress = (TextView) layout.findViewById(R.id.txtAddress);
            TextView txtlong = (TextView) layout.findViewById(R.id.txtlong);
            txtaccuracy.setText(strAccurcy);
            txtlatlong.setText(latitude);
            txtlong.setText(longitude);
            franchiseeDetails.setLatitude(latitude);
            franchiseeDetails.setLongitude(longitude);


            txtAddress.setText(mLocationAddress.getText().toString());

            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    updateProfileData();

                    if (latitude == null) {
                        Toast.makeText(getActivity(), "Please Turn on GPS setting", Toast.LENGTH_SHORT).show();
                    } else if (internetConnection.isConnectingToInternet() == false) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.internetCheck));

                    } else if (franchiseeDetails.getLatitude().startsWith("0") || franchiseeDetails.getLongitude().startsWith("0")) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.alert_msg_location_unavailable2));
                    } else {
                        if (IsKendraLocationCheckEnabled) {
                            asyncValidateLocationChecks = new AsyncValidateLocationChecks(getContext(), new IGetResult() {
                                @Override
                                public void onResult(String status, String result) {
                                    //Handle status
                                    handleLocationChecksOfAsync(status, result, dialog);
                                }
                            });
                            asyncValidateLocationChecks.execute("");
                        } else {
                            updateLocation(dialog);
                        }
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

    public void updateLocation(DialogInterface dialog) {

        progress = new ProgressDialog(getActivity());
        if (isSiteReadinessOrWorkInProgress) {
            progress.setTitle(R.string.saveLocation);
        } else {
            progress.setTitle(R.string.updateLocation);
        }
        progress.setMessage(getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        if (isSiteReadinessOrWorkInProgress) {
            progress.dismiss();
            Toast.makeText(getActivity(), "Kendra Location Saved Successfully.", Toast.LENGTH_LONG).show();

        } else {
            Log.d("LATI ", latitude + ":" + longitude);
            AsyncNextgenLocationUpdate myRequest = new AsyncNextgenLocationUpdate();
            myRequest.execute();
            dialog.dismiss();

            if (getActivity().getClass().getSimpleName().equalsIgnoreCase(NEXTGEN_PHOTO_VIEW_PAGER_CONST)) {
                ((NextGenPhotoViewPager) getActivity()).selectFragment(1);
            } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase(SITE_READINESS_ACTIVITY_CONST)) {
                ((SiteReadinessActivity) getActivity()).selectFragment(1);
            } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase(MANDATORY_BRANDING_ACTIVITY_CONST)) {
                ((MandatoryBrandingActivity) getActivity()).selectFragment(1);
            } else {
                ((NextGenSiteCommencementActivity) getActivity()).viewPager.setCurrentItem(1, true);
            }
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
                try {
                    if (IsKendraLocationCheckEnabled) {
                        if (!IsCurrentTabVisible())
                            return;

                        if (!IsCameraPosCalledForFirst) {
                            IsCameraPosCalledForFirst = true;
                            return;
                        }

                        int distance = (int) gpsTracker.getDistance(mCenterLatLong.latitude, mCenterLatLong.longitude);
                        if (distance > franchiseeDetails.getKendraRange()) {

                            String kendraRangeMsg = getContext().getResources().getString(R.string.msg_location_beyond_range);
                            kendraRangeMsg = kendraRangeMsg.replace("{KENDRA_RANGE}", String.valueOf(franchiseeDetails.getKendraRange()));
                            showMessage(kendraRangeMsg);
                            return;
                        }
                    }
                    // Nilesh- if textViewSearchAddress == true then Mapview marker cant change or move center position
                    if (IsMarkerChangeAllowed) {
                        textViewSearchAddress.setVisibility(View.GONE);
                        mMap.getUiSettings().setAllGesturesEnabled(false);
                    }
                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);

                    startIntentService(mLocation);
                    mLocationMarkerText.setText("Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude);
                    latitude = String.valueOf(mCenterLatLong.latitude);
                    longitude = String.valueOf(mCenterLatLong.longitude);

                    // This is only for Work Commencement and Work In Progress Module. [No need to update individual]
                    if (isSiteReadinessOrWorkInProgress && (mCenterLatLong.latitude != 0.0 && mCenterLatLong.longitude != 0.0)
                            && franchiseeDetails.isAllowToEdit()) {
                        franchiseeDetails.setLatitude(latitude);
                        franchiseeDetails.setLongitude(longitude);
                    }

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

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            if (mMap != null && mapView.findViewById(Integer.parseInt("1")) != null) {
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                layoutParams.setMargins(0, 20, 20, 20);
            }

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
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            btnGetDrawLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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

            if (location != null) {
                changeMap(location);

                if (IsKendraLocationCheckEnabled) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (mCircle == null) {
                        drawMarkerWithCircle(latLng);
                    } else {
                        updateMarkerWithCircle(latLng);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Do Nothing
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
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (asyncSaveSiteReadinessVerification != null && !asyncSaveSiteReadinessVerification.isCancelled()) {
            asyncSaveSiteReadinessVerification.cancel(true);
        }

        if (asyncValidateLocationChecks != null && !asyncValidateLocationChecks.isCancelled()) {
            asyncValidateLocationChecks.cancel(true);
        }

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

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
                //Do Nothing
            }
            return false;
        }
        return true;
    }

    @SuppressLint("LongLogTag")
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
            if (franchiseeDetails != null && franchiseeDetails.getLatitude() != null
                    && !franchiseeDetails.getLatitude().isEmpty() && !franchiseeDetails.getLatitude().equals("null")) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                double lati = Double.parseDouble(franchiseeDetails.getLatitude());
                double logi = Double.parseDouble(franchiseeDetails.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lati, logi), 19.0f));
                strAccurcy = String.valueOf(location.getAccuracy());
            } else {

                // check if map is created successfully or not
                if (mMap != null) {
                    mMap.getUiSettings().setZoomControlsEnabled(false);
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                   // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 19.0f));

                    mLocationMarkerText.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude() + "Accurcy" + location.getAccuracy());
                    startIntentService(location);
                    Log.e("Sorry No Lat Long ", String.valueOf(location.getAccuracy()));
                    final String passdata = location.getLatitude() + ":" + location.getLongitude() + ":" + location.getAccuracy() + ":" + location.getAltitude();

                    strAccurcy = String.valueOf(location.getAccuracy());
                    latitude = String.valueOf((location.getLatitude()));
                    longitude = String.valueOf((location.getLongitude()));

                    if (!AppUtilsforLocationService.isLocationEnabled(getActivity())) {
                        // notify user
                        Log.e("Please Location Disable ", "Sorry Disable Location");
                    } else {

                        if (passdata == null) {
                            Log.e("Sorry No Lat Long ", passdata);
                        } else {
                            //Do Nothing
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
                //Do Nothing
            }
        }
    }

    /**
     * Updates the address in the UI.
     */
    protected void displayAddressOutput() {
        try {
            if (mAreaOutput != null)
                // mLocationText.setText(mAreaOutput+ "");

                mLocationAddress.setText(mAddressOutput);
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
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latLong).zoom(19f).tilt(70).build();

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
                PlaceAutocomplete.getStatus(getActivity(), data);
            } else if (resultCode == RESULT_CANCELED) {
                //Do Nothing
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
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

    private class AsyncNextgenLocationUpdate extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {

                if (isSiteReadinessVerification) {
                    franchiseeDetails.setDesignElements(null);
                    franchiseeDetails.setBranding_element_details(null);

                    String jsonData = JSONUtils.toString(franchiseeDetails);

                    Connection connection = new Connection(getContext());
                    String tmpVkId = connection.getVkid();

                    String data = jsonData;   // Work In Progress Data - JSON
                    String type = "2";
                    EncryptionUtil.encryptString(tmpVkId, getContext());

                    if (isAdhoc) {
                        if (!TextUtils.isEmpty(tmpVkId)) {
                            diplayServerResopnse = WebService.nextgenSiteReadinessAndVerificationUpdate1(tmpVkId, data, type);
                        } else {
                            String enquiryId = CommonUtils.getEnquiryId(getContext());
                            diplayServerResopnse = WebService.nextgenSiteReadinessAndVerificationUpdate1(enquiryId, data, type);
                        }
                    }
                } else {
                    Connection connection = new Connection(getActivity());
                    String vkid = connection.getVkid();
                    String tokenId = "123";
                    try {
                        tokenId = connection.getTokenId();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String deviceIdget = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                    String deviceid = EncryptionUtil.encryptString(deviceIdget, getActivity());

                    String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getActivity());
                    String imei = EncryptionUtil.encryptString(deviceIDAndroid, getActivity());

                    String simSerial = CommonUtils.getSimSerialNumber(getActivity());
                    String simserialnumber = EncryptionUtil.encryptString(simSerial, getActivity());


                    String vkidd = EncryptionUtil.encryptString(vkid, getActivity());
                    String TokenId = EncryptionUtil.encryptString(tokenId, getActivity());
                    String type = EncryptionUtil.encryptString("1", getActivity());
                    String jsonData = JSONUtils.toString(franchiseeDetails);
                    String data = jsonData;

                    if (Constants.ENABLE_FRANCHISEE_LOGIN) {
                        if (!TextUtils.isEmpty(vkid)) {
                            diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgenUpdate(vkid, "1", data);
                        } else {
                            String enquiryId = CommonUtils.getEnquiryId(getContext());
                            diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgenUpdate2(enquiryId, "1", data);
                        }
                    } else {
                        if (isAdhoc) {
                            diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgenUpdate(
                                    vkid, "1", data);
                        } else {
                            diplayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgenUpdate(
                                    vkidd, TokenId, imei, deviceid, simserialnumber, type, data);
                        }
                    }
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
            try {
                if (diplayServerResopnse.startsWith("OKAY|")) {
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.imageuploadsuccessful));

                    IsCameraPosCalledForFirst = false;  // Added: 28-09-2018 | By: Dpk | Desc: Avoid alert message after save location.

                    //TODO: Need to refactor below process
                    try {

                        final Connection connection = new Connection(getContext());
                        final String getUserid = franchiseeDetails.getNextGenFranchiseeApplicationNo();
                        String vkIdTemp = connection.getVkid();
                        final String getVkid = EncryptionUtil.encryptString(vkIdTemp, getContext());
                        final String getTokenId = EncryptionUtil.encryptString(connection.getTokenId(), getContext());
                        String deviceIdget = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                        String getdeviceid = EncryptionUtil.encryptString(deviceIdget, getActivity());

                        String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getActivity());
                        String getimei = EncryptionUtil.encryptString(deviceIDAndroid, getActivity());

                        String simSerial = CommonUtils.getSimSerialNumber(getActivity());
                        String getsimserialnumber = EncryptionUtil.encryptString(simSerial, getActivity());

                        final String getid = EncryptionUtil.encryptString(getUserid, getContext());

                        if (isAdhoc) {
                            if (isSiteReadinessVerification) {
                                Utils.reloadReadinessVerificationData(modetype, getContext(), vkIdTemp, franchiseeDetails.getNextGenFranchiseeApplicationId());
                            } else {
                                Utils.updateFranchicess(franchiseeDetails, getContext(), vkIdTemp, vkIdTemp);
                            }
                        } else {
                            Utils.updateFranchicess(franchiseeDetails, getContext(), getVkid, getid, getTokenId, getimei, getdeviceid, getsimserialnumber);
                        }

                        if (getActivity().getClass().getSimpleName().equalsIgnoreCase(NEXTGEN_PHOTO_VIEW_PAGER_CONST)) {
                            ((NextGenPhotoViewPager) getActivity()).viewPager.setCurrentItem(1, true);
                        } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase(SITE_READINESS_ACTIVITY_CONST)) {
                            ((SiteReadinessActivity) getActivity()).viewPager.setCurrentItem(1, true);
                        }else if (getActivity().getClass().getSimpleName().equalsIgnoreCase(MANDATORY_BRANDING_ACTIVITY_CONST)) {
                            ((MandatoryBrandingActivity) getActivity()).viewPager.setCurrentItem(1, true);
                        } else {
                            ((NextGenSiteCommencementActivity) getActivity()).viewPager.setCurrentItem(1, true);
                        }
                    } catch (Exception e) {
                        AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                        e.printStackTrace();
                    }

                } else {
                    Log.e("Error in Server", diplayServerResopnse);
                    Toast.makeText(getActivity(), diplayServerResopnse, Toast.LENGTH_SHORT).show();
                    AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                }

            } catch (Exception e) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getResources().getString(R.string.Warning));
                e.printStackTrace();
            }
        }
    }

    public void showMessage(String msg) {

        if (alert != null && alert.isShowing())
            return;

        alert = new AlertDialog.Builder(getContext()).create();
        alert.setMessage(Html.fromHtml(msg));
        alert.setCancelable(false);
        alert.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert.dismiss();
                IsCameraPosCalledForFirst = false;
                refreshMap();
                alert = null;
            }
        });
        alert.show();
    }

    public void refreshMap() {
        Location mLocation = new Location("");
        mLocation.setLatitude(Double.parseDouble(franchiseeDetails.getLatitude()));
        mLocation.setLongitude(Double.parseDouble(franchiseeDetails.getLongitude()));
        changeMap(mLocation);
    }

    private void updateMarkerWithCircle(LatLng position) {
        mCircle.setCenter(position);

    }

    private void drawMarkerWithCircle(LatLng position) {
        double radiusInMeters = 50.0;
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x66ff0000; //opaque red fill

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(3);
        mCircle = mMap.addCircle(circleOptions);

    }

    public boolean IsLocationWithinAccuracy() {

        float deviceAccuracy = gpsTracker.getLocationAccuracy();
        int accuracy = (int) deviceAccuracy;
        if (accuracy <= franchiseeDetails.getLocationAccuracy())
            return true;

        return false;
    }

    /**
     * Check if Current Location is near to Kendra Location
     *
     * @return
     */
    public boolean IsValidLocation() {
        if (gpsTracker.canGetLocation()) {
            if (TextUtils.isEmpty(franchiseeDetails.getLatitude()) || TextUtils.isEmpty(franchiseeDetails.getLongitude()) || franchiseeDetails.getWipLocationRange() == 0)
                return true;

            int distance = (int) gpsTracker.getDistance(Double.parseDouble(franchiseeDetails.getLatitude()), Double.parseDouble(franchiseeDetails.getLongitude()));
            if (distance <= franchiseeDetails.getWipLocationRange())
                return true;
        }
        return false;
    }

    public class AsyncValidateLocationChecks extends AsyncTask<String, Void, String> {

        private String response;
        private ProgressDialog progress;
        private IGetResult iGetResult;
        private Context context;
        private String status;

        public AsyncValidateLocationChecks(Context context, IGetResult iGetResult) {
            super();
            this.context = context;
            this.iGetResult = iGetResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(context);
            progress.setTitle(R.string.pleaseWait);
            progress.setMessage(context.getResources().getString(R.string.pleaseWait));
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            //STEP 1: Kendra Location and Current Location should be within Location Range
            boolean IsValidLocation = IsValidLocation();
            if (!IsValidLocation) {
                status = "-1";
                return null;
            }

            //STEP 2: Accuracy should be within LocationAccuracy provided
            boolean IsLocationAccurate = IsLocationWithinAccuracy();
            if (!IsLocationAccurate) {
                status = "-2";
                return null;
            }

            int IsAddressChecked = franchiseeDetails.getIsAddressChecked();
            if (IsAddressChecked != 1) {
                status = "0";
                response = "ALLOW";
                return response;
            }

            /**
             * STEP 3: Validate Address with current Location
             */
            //STEP 1: Get VKId
            Connection connection = new Connection(context);
            String vkId = connection.getVkid();

            //STEP 2: Get NextGenApplicationNo
            String appNo = franchiseeDetails.getNextGenFranchiseeApplicationNo();
            if (TextUtils.isEmpty(appNo)) {
                Log.e(TAG, "Failed to validate LatLong with Address.[NextGenApplicationNo is null].");
                status = "-100";
                return status;
            }

            //STEP 3: Get Latitude
            String latitude = franchiseeDetails.getLatitude();
            if (TextUtils.isEmpty(latitude)) {
                Log.e(TAG, "Failed to validate LatLong with Address.[Latitude is null].");
                status = "-100";
                return null;
            }

            //STEP 4: Get Longitude
            String longitude = franchiseeDetails.getLongitude();
            if (TextUtils.isEmpty(longitude)) {
                Log.e(TAG, "Failed to validate LatLong with Address.[Longitude is null].");
                status = "-100";
                return null;
            }

            if (isAdhoc) {

                if (!TextUtils.isEmpty(vkId)) {
                    response = WebService.validateLatLongWithAddress(vkId, appNo, latitude, longitude);
                } else {
                    String enquiryId = CommonUtils.getEnquiryId(context);
                    response = WebService.validateLatLongWithAddress(enquiryId, appNo, latitude, longitude);
                }
                status = "0";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.dismiss();     // Hide Progress Bar

            if (iGetResult != null)
                iGetResult.onResult(status, response);
        }
    }

    public void handleLocationChecksOfAsync(String status, String response, DialogInterface dialogInterface) {
        //Handle status
        try {
            if (TextUtils.isEmpty(status) || status.equalsIgnoreCase("-100")) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.Warning));
                return;
            }

            if (status.equalsIgnoreCase("-1")) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.not_valid_location));
                return;
            }

            if (status.equalsIgnoreCase("-2")) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.msg_improper_location_accuracy));
                return;
            }

            if (response.equalsIgnoreCase("ALLOW")) {
                //Call Location Update method
                updateLocation(dialogInterface);
                return;
            }

            StringTokenizer tokens = new StringTokenizer(response, "|");
            String key = tokens.nextToken();
            if (key.equalsIgnoreCase("ERROR")) {
                AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.Warning));
                return;
            }

            String secondValue = tokens.nextToken();
            if (secondValue.equalsIgnoreCase("N")) {
                String msg = "";
                String thirdValue = tokens.nextToken();
                if (TextUtils.isEmpty(thirdValue)) {
                    msg = getContext().getString(R.string.location_not_match);
                } else {
                    msg = getContext().getString(R.string.msg_improper_address);
                    msg = msg.replace("{ADDRESS}", " <b> " + thirdValue + " </b >");
                }
                AlertDialogBoxInfo.alertDialogShow(getActivity(), msg);
                return;
            }

            //Call Location Update method
            updateLocation(dialogInterface);

        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(getActivity(), getString(R.string.Warning));

        }
    }

    public boolean IsCurrentTabVisible() {

        int tabPos = -1;
        if (getActivity().getClass().getSimpleName().equalsIgnoreCase(NEXTGEN_PHOTO_VIEW_PAGER_CONST)) {
            tabPos = ((NextGenPhotoViewPager) getActivity()).viewPager.getCurrentItem();

        } else if (getActivity().getClass().getSimpleName().equalsIgnoreCase(SITE_READINESS_ACTIVITY_CONST)) {
            tabPos = ((SiteReadinessActivity) getActivity()).viewPager.getCurrentItem();

        }else if (getActivity().getClass().getSimpleName().equalsIgnoreCase(MANDATORY_BRANDING_ACTIVITY_CONST)) {
            tabPos = ((MandatoryBrandingActivity) getActivity()).viewPager.getCurrentItem();

        } else {
            tabPos = ((NextGenSiteCommencementActivity) getActivity()).viewPager.getCurrentItem();
        }

        if (tabPos == LOCATION_TAB_POSITION)
            return true;

        return false;
    }
}

