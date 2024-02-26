package in.vakrangee.franchisee.activity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableBadgeDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.mindorks.nybus.event.Channel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import in.vakrangee.franchisee.BuildConfig;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.adhoc.AdhocLoginActivity;
import in.vakrangee.franchisee.atmloading.cash_loading.ATMCashLoadingActivity;
import in.vakrangee.franchisee.atmloading.cash_sourcing.ATMCashSourcingActivity;
import in.vakrangee.franchisee.atmtechlivechecklist.ATMTechLiveCheckListActivity;
import in.vakrangee.franchisee.bcadetails.AsyncGetBCAEntryDetails;
import in.vakrangee.franchisee.bcadetails.pre.AsyncGetPreBCADetails;
import in.vakrangee.franchisee.delivery_address.DeliveryAddressActivity;
import in.vakrangee.franchisee.documentmanager.DocumentManagerActivity;
import in.vakrangee.franchisee.fragment.DashboardMenuFragement;
import in.vakrangee.franchisee.fragment.Fragment_Main_Dashboard;
import in.vakrangee.franchisee.franchiseelogin.FranchiseeAuthenticationRepository;
import in.vakrangee.franchisee.franchiseelogin.FranchiseeEnquiryDto;
import in.vakrangee.franchisee.franchiseelogin.FranchiseeEnquiryHandlerFragment;
import in.vakrangee.franchisee.franchiseelogin.FranchiseeLoginActivity;
import in.vakrangee.franchisee.franchiseelogin.NextGenFranchiseeEnquiryDetailsActivity;
import in.vakrangee.franchisee.gstdetails.GSTDetailsActivity;
import in.vakrangee.franchisee.gwr.AsyncGetGWR;
import in.vakrangee.franchisee.gwr.attendance.AsyncGetGWRAttendanceDetails;
import in.vakrangee.franchisee.gwr.dashboard.AsyncGetGWRDashboardData;
import in.vakrangee.franchisee.gwr.dashboard.GWRDashboardDto;
import in.vakrangee.franchisee.gwr.dashboard.GWRDashboardFragment;
import in.vakrangee.franchisee.gwr.event_day_activity.GWREventDayActivity;
import in.vakrangee.franchisee.gwr.event_photos.AsyncGetGWREventPhotos;
import in.vakrangee.franchisee.gwr.evidence.UploadEvidenceFilesService;
import in.vakrangee.franchisee.gwr.propreitary_activity.GWRPropreitaryActivity;
import in.vakrangee.franchisee.gwr.witness_statement.AsyncGetUploadWitnessStatement;
import in.vakrangee.franchisee.hardwareacknowledgement.KendraAcknowledgementActivity;
import in.vakrangee.franchisee.helpline.FloatingHelplineService;
import in.vakrangee.franchisee.kendra_final_photo.AsyncGetKendraFinalPhoto;
import in.vakrangee.franchisee.kendraworkingtime.KendraWorkingTimeActivity;
import in.vakrangee.franchisee.loandocument.LoanDocumentActivity;
import in.vakrangee.franchisee.loandocument.loan_sanction.UBILoanSanctionActivity;
import in.vakrangee.franchisee.loandocument.upload_ack.UploadAcknowledgementActivity;
import in.vakrangee.franchisee.locationupdation.KendraFrontagePhotoActivity;
import in.vakrangee.franchisee.networktesting.simstrength.NetworkTestingActivity;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.AsyncGetFranchiseeApplicationByEnquiryId;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.CustomNextGenApplicationFirstScreenDialog;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.NextGenFranchiseeApplicationActivity;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.newexistingfranchisee.AsyncNewExistingFranchiseeChecks;
import in.vakrangee.franchisee.payment_details.PaymentDetailsActivity;
import in.vakrangee.franchisee.payment_history.PaymentHistoryActivity;
import in.vakrangee.franchisee.phasechecks.AsyncGetFranchiseePhaseInfo;
import in.vakrangee.franchisee.service.UpdateUserLatLngService;
import in.vakrangee.franchisee.sitelayout.activity.MyVakrangeeKendraLocationDetailsNextGen;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncIsDrawerItemAllowed;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncNextGenFranchiseeDetails;
import in.vakrangee.franchisee.sitelayout.finalrmapproval.AsyncGetFinalRMApprovalDetails;
import in.vakrangee.franchisee.support.SupportTicketActivity;
import in.vakrangee.franchisee.tracking_hardware.TrackingHardwareActivity;
import in.vakrangee.franchisee.utils.TimeService;
import in.vakrangee.franchisee.workinprogress.wipchatview.AsyncGetWIPChatViewStatus;
import in.vakrangee.simcarddetail.simcarddetails.SimcardDetailsActivity;
import in.vakrangee.supercore.franchisee.baseutils.BaseAppCompatActivity;
import in.vakrangee.supercore.franchisee.commongui.CustomWebViewDialog;
import in.vakrangee.supercore.franchisee.franchiseelogin.FranchiseeLoginChecksDto;
import in.vakrangee.supercore.franchisee.model.GeoCordinates;
import in.vakrangee.supercore.franchisee.model.Globals;
import in.vakrangee.supercore.franchisee.model.MyVKMaster;
import in.vakrangee.supercore.franchisee.phasechecks.PhaseInfoDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.IPermission;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.PermissionHandler;
import in.vakrangee.supercore.franchisee.utils.SharedPrefUtils;
import in.vakrangee.supercore.franchisee.utils.network.ConnectivityChangeReceiver;
import in.vakrangee.supercore.franchisee.utils.network.EventData;
import in.vakrangee.supercore.franchisee.utils.network.NetworkHealthHandler;
import in.vakrangee.supercore.franchisee.webservice.WebService;
import in.vakrangee.team.franchiseeexit.exit_evidence.FrExitEvidenceCollectionActivity;

public class DashboardActivity extends BaseAppCompatActivity {

    private static final int LAST_IDENTIFIER = 220;
    private AccountHeader headerResult = null;
    private Drawer drawer = null;
    IProfile profile;
    private  WebView mWebview;
    String TAG = "Response";
    int k = 0;
    private DashboardActivity.AsyncGetVKIDByEnquiryId asyncGetVKIDByEnquiryId = null;
    private FranchiseeEnquiryDto enquiryDto;
    ProgressDialog progress;
    String diplayServerResopnse;
    private ProgressDialog progressDialog;

    private static final int PROFILE_SETTING = 1;
    private boolean opened_1 = false;
    private boolean opened_2 = false;
    String getVkid;
    private CustomWebViewDialog customWebViewDialog = null;
    private FranchiseeAuthenticationRepository franchiseeAuthRepo;
    private Logger logger;

    TelephonyManager tel;
    boolean doubleBackToExitPressedOnce = false;
    Typeface tf_r, tf_icon;
    Button reload;
    private boolean isMinimized = false;
    InternetConnection internetConnection;
    TextView vlId, vlName;
    GeoCordinates geoCordinates;
    String[] txtLatlong;
    String latitude, longtiude;
    int widthg = 0;


    private final static int DELAY = 10;
    private final Handler handler = new Handler();
    private final Timer timer = new Timer();
    private Uri picUriCap, picUriGallery;

    private final int GALLERY_ACTIVITY_CODE = 200;
    private final int RESULT_CROP = 400;
    String titleCaseValue;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    public static MyVKMaster myVKMaster;
    TelephonyManager telephonyManager;
    final int CAMERA_CAPTURE = 1;
    //captured picture uri
    int PIC_CROP = 3;
    private Uri picUri;
    private PermissionHandler permissionHandler;

    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    private Connection connection;
    private DeprecateHandler deprecateHandler;
    private ConnectivityChangeReceiver receiver;
    private boolean IsVKIDAvailable = false;
    private Typeface clanPro;
    ImageView imgEditProfile;
    private AsyncGetGWRDashboardData asyncGetGWRDashboardData = null;
    private LinearLayout layoutGWRDashboard;
    private GWRDashboardFragment fragmentGWRDashboard;
    private boolean IsEventDayOpened = false;
    private String eventDayMsg = "";
    private AsyncGetFranchiseePhaseInfo asyncGetFranchiseePhaseInfo = null;
    private TextView txtPhaseName;
    private AsyncNewExistingFranchiseeChecks asyncNewExistingFranchiseeChecks = null;
    private AsyncIsDrawerItemAllowed asyncIsDrawerItemAllowed;
    private TextView txtVakrangee, txtKendra, txtConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.activity_dashboard);

        clanPro = Typeface.createFromAsset(getAssets(), "ClanPro-Bold.otf");
        imgEditProfile = (ImageView) findViewById(R.id.editprofile);
        txtPhaseName = findViewById(R.id.txtPhaseName);

        final Typeface fonta = Typeface.createFromAsset(getApplicationContext().getAssets(), "ClanPro-Medium.otf");
        txtVakrangee = findViewById(R.id.txtVakrangee);
        txtKendra = findViewById(R.id.txtKendra);
        txtConnect = findViewById(R.id.txtConnect);

        txtVakrangee.setTypeface(fonta, Typeface.BOLD_ITALIC);
        txtKendra.setTypeface(fonta, Typeface.BOLD_ITALIC);
        txtConnect.setTypeface(fonta, Typeface.BOLD_ITALIC);


        locationpermission();
        camerapermission();
        connection = new Connection(getApplicationContext());
        franchiseeAuthRepo = new FranchiseeAuthenticationRepository(this);
        FranchiseeLoginChecksDto loginChecksDto = CommonUtils.getFranchiseeLoginDataFromPreferences(DashboardActivity.this);

        permissionHandler = new PermissionHandler(this);
        deprecateHandler = new DeprecateHandler(this);
        receiver = new ConnectivityChangeReceiver();

        String fcmId = SharedPrefUtils.getInstance(getApplicationContext()).getFCMId();
        Log.e(TAG, "Testing: FCMId: " + fcmId);

        //Register
        NYBus.get().register(this, Channel.TWO);

        //connection.openDatabase();
        final String vkId = connection.getVkid();
        IsVKIDAvailable = !TextUtils.isEmpty(vkId) ? true : false;

        //Require Permission for Overlay
   //     displayHelplineFlaoting();

        //region Start Update LatLong Service
        boolean IsServiceToBeStarted = CommonUtils.IsUpdateLatLngToBeStarted(this);
        int serviceInterval = CommonUtils.getUpdateLatLngInterval(this);
        if (IsServiceToBeStarted) {
            Intent intent = new Intent(DashboardActivity.this, UpdateUserLatLngService.class);
            intent.putExtra("INTERVAL", serviceInterval);
            startService(intent);
        }
        //endregion

        internetConnection = new InternetConnection(DashboardActivity.this);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        loadFragment(new DashboardMenuFragement(DashboardActivity.this));
        if (customWebViewDialog != null && customWebViewDialog.isShowing()) {
            return;
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.app_name);
        }

        try {
            tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String username = connection.getUsernameVkid();
            vlId = (TextView) findViewById(R.id.vlid);
            vlName = (TextView) findViewById(R.id.vlname);

            try {
                String[] words = username.toString().split(" ");
                StringBuilder sb = new StringBuilder();
                if (words[0].length() > 0) {
                    sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
                    for (int i = 1; i < words.length; i++) {
                        sb.append(" ");
                        sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
                    }
                }
                titleCaseValue = sb.toString();

            } catch (Exception e) {
                e.getMessage();
            }
           //      final String vkid = SharedPrefUtils.getInstance(this).getVKId();

            String name = connection.getUsernameVkid();

            // Save USER VKID into Preferences For Location Tracking and Geofencing. | By : Dpk | On : 8th Feb 2019
            SharedPreferences appSettings = getSharedPreferences("CommonPrefs", MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = appSettings.edit();
            prefEditor.putString("USER_VKID", vkid);
            prefEditor.commit();
            //--END

            vlId.setText(vkid);

            if (titleCaseValue == null) {
                vlName.setText(name);
            } else {
                vlName.setText(titleCaseValue);
            }

            //TODO : Nilesh - ERROR show
            if (!BuildConfig.DEBUG) {
                Crashlytics.setUserIdentifier(vkid);
                Crashlytics.setUserName(vkid);
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.primary_dark));
            }
            asyncGetVKIDByEnquiryId = new AsyncGetVKIDByEnquiryId();
            asyncGetVKIDByEnquiryId.execute();
            List<IDrawerItem> iDrawerItems = new LinkedList<>();
            //  SecondaryDrawerItem item2 = new SecondaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1).withIconColorRes(R.color.md_grey_600).withTextColorRes(R.color.primary_dark);
            List<ExpandableBadgeDrawerItem> expandableBadgeDrawerItems = new LinkedList<>();
            iDrawerItems.add(new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1));

            iDrawerItems.add(new SecondaryDrawerItem().withName("Nextgen Franchisee Application").withIdentifier(209));
            iDrawerItems.add(new SecondaryDrawerItem().withName("NextGen Site Detail").withIdentifier(5));

            //iDrawerItems.add(new SecondaryDrawerItem().withName("Work Commencement").withIdentifier(201));     // 201 - Code
            iDrawerItems.add(new SecondaryDrawerItem().withName("Work In Progress").withIdentifier(202));     // 202 - Code

            //Independent of VKID
            if (connection.getVkid().startsWith("VL") || connection.getVkid().startsWith("VA")) {
                iDrawerItems.add(new SecondaryDrawerItem().withName("Work Completion Verification").withIdentifier(204));
            } else {
                //iDrawerItems.add(new SecondaryDrawerItem().withName("Work Completion Intimation").withIdentifier(204));
                iDrawerItems.add(new ExpandableDrawerItem().withName("Work Completion Intimation")
                        .withIdentifier(2000).withSetSelected(false).withSelectable(false).withSelectable(false)
                        .withSubItems(
                        new SecondaryDrawerItem().withName("MANDATORY BRANDING COMPLETED").withLevel(2).withIdentifier(2010).withSelectable(false),
                        new SecondaryDrawerItem().withName("KENDRA INTERIORS COMPLETED").withLevel(2).withIdentifier(2011).withSelectable(false),
                        new SecondaryDrawerItem().withName("INAUGURATION /RE-LAUNCH COMPLETED").withLevel(2).withIdentifier(2012).withSelectable(false)));
            }

            iDrawerItems.add(new SecondaryDrawerItem().withName("Privacy Policy").withIdentifier(1003));     // 202 - Code

/*

            if (!connection.getVkid().startsWith("VL") && !connection.getVkid().startsWith("VA")) {
                iDrawerItems.add(new SecondaryDrawerItem().withName("Update GST Details").withIdentifier(206));
            }

            *//*if (!connection.getVkid().startsWith("VL") && !connection.getVkid().startsWith("VA")) {
                iDrawerItems.add(new SecondaryDrawerItem().withName(getResources().getString(R.string.agreement_dispatch)).withIdentifier(208));
            }*//*
            if (!connection.getVkid().startsWith("VL") && !connection.getVkid().startsWith("VA")) {
                iDrawerItems.add(new SecondaryDrawerItem().withName(getResources().getString(R.string.tracking_hardware)).withIdentifier(220));
            }

            if (!connection.getVkid().startsWith("VL") && !connection.getVkid().startsWith("VA")) {
                iDrawerItems.add(new SecondaryDrawerItem().withName(getResources().getString(R.string.next_gen_kendra_acknowledgement)).withIdentifier(221));
            }

            *//*if (!connection.getVkid().startsWith("VL") && !connection.getVkid().startsWith("VA")) {
                iDrawerItems.add(new SecondaryDrawerItem().withName(getResources().getString(R.string.atm_loading)).withIdentifier(224));
            }*//*

            if (!connection.getVkid().startsWith("VL") && !connection.getVkid().startsWith("VA")) {
                iDrawerItems.add(new SecondaryDrawerItem().withName(getResources().getString(R.string.delivery_address)).withIdentifier(225));
            }

            if (!connection.getVkid().startsWith("VL") && !connection.getVkid().startsWith("VA")) {
                iDrawerItems.add(new SecondaryDrawerItem().withName(getResources().getString(R.string.payment_history)).withIdentifier(226));
            }
            iDrawerItems.add(new SecondaryDrawerItem().withName("Document Manager").withIdentifier(211));
            //iDrawerItems.add(new SecondaryDrawerItem().withName(getResources().getString(R.string.simcardDetails)).withIdentifier(208));

            iDrawerItems.add(new SecondaryDrawerItem().withName("BCA Detail").withIdentifier(301));

            if (IsVKIDAvailable) {

                //  iDrawerItems.add(new SecondaryDrawerItem().withName("BCA Detail Entry").withIdentifier(222));

                //iDrawerItems.add(new SecondaryDrawerItem().withName("Site Completion Intimation").withIdentifier(205));
                *//*if (!connection.getVkid().startsWith("VL") && !connection.getVkid().startsWith("VA")) {
                    iDrawerItems.add(new SecondaryDrawerItem().withName("Kendra Final Photo").withIdentifier(6));
                }*//*

             *//*if (!connection.getVkid().startsWith("VL") && !connection.getVkid().startsWith("VA")) {
                    iDrawerItems.add(new SecondaryDrawerItem().withName("My HelpLine").withIdentifier(207));
                }*//*

                //iDrawerItems.add(new SecondaryDrawerItem().withName("Hardware Acknowledgement").withIdentifier(210));

                *//* Commented on 8th Feb by Vasundhara-- Done with the GWR module
                String text = String.valueOf(Html.fromHtml("NextGen-3300<sup>+</sup> Launch"));
                iDrawerItems.add(new ExpandableDrawerItem().withName(text).withIdentifier(1000).withSetSelected(false).withSelectable(false).withSubItems(
                        new SecondaryDrawerItem().withName("Preparatory Activity").withLevel(2).withIdentifier(218).withSelectable(false),
                        new SecondaryDrawerItem().withName("Event Day Activity").withLevel(2).withIdentifier(219).withSelectable(false)

                        //,new SecondaryDrawerItem().withName("Activity").withLevel(2).withIdentifier(212).withSelectable(false),
                        //new SecondaryDrawerItem().withName("Witness Profile").withLevel(2).withIdentifier(213).withSelectable(false),
                        //new SecondaryDrawerItem().withName("Cameraman Engagement").withLevel(2).withIdentifier(216).withSelectable(false),
                        //new SecondaryDrawerItem().withName("Attendance").withLevel(2).withIdentifier(214).withSelectable(false),
                        //new SecondaryDrawerItem().withName("Upload Witness Statement").withLevel(2).withIdentifier(215).withSelectable(false),
                        //new SecondaryDrawerItem().withName("Event Day Photos").withLevel(2).withIdentifier(217).withSelectable(false)
                ));*//*

                // iDrawerItems.add(new SecondaryDrawerItem().withName("Kendra Working Time").withIdentifier(228));
                *//*iDrawerItems.add(new SecondaryDrawerItem().withName("Guinness Activity").withIdentifier(212));
                iDrawerItems.add(new SecondaryDrawerItem().withName("Guinness Witness").withIdentifier(213));
                iDrawerItems.add(new SecondaryDrawerItem().withName("Guinness Attendance").withIdentifier(214));*//*


                //iDrawerItems.add(new SecondaryDrawerItem().withName("Support Ticket").withIdentifier(203));     // 203 - Code

                iDrawerItems.add(new SecondaryDrawerItem().withName(getResources().getString(R.string.title_network_testing)).withIdentifier(227));

            }

            *//* if (!connection.getVkid().startsWith("VL") && !connection.getVkid().startsWith("VA")) {
                iDrawerItems.add(new SecondaryDrawerItem().withName("Training Video").withIdentifier(208));
            }*//*
            //iDrawerItems.add(new SecondaryDrawerItem().withName(getResources().getString(R.string.kendra_verification_updation)).withIdentifier(223));
            iDrawerItems.add(new SecondaryDrawerItem().withName(getResources().getString(R.string.payment_details)).withIdentifier(300));
            iDrawerItems.add(new SecondaryDrawerItem().withName(getResources().getString(R.string.atm_checklist)).withIdentifier(302));

            if (!connection.getVkid().startsWith("VL") && !connection.getVkid().startsWith("VA")) {
                iDrawerItems.add(new SecondaryDrawerItem().withName("My HelpLine").withIdentifier(207));
            }*/

            int pixels = CommonUtils.getScreenDensityWidth(this);
            int width = (int) (pixels * 0.75);
            drawer = new DrawerBuilder()
                    .withActivity(this)
                    //.withToolbar(toolbar).withDrawerWidthDp(220).withSliderBackgroundColorRes(R.color.navigationcolor)
                    .withToolbar(toolbar).withDrawerWidthDp(width).withSliderBackgroundColorRes(R.color.navigationcolor)
                    .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                    .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                        @Override
                        public boolean onNavigationClickListener(View clickedView) {
                            DashboardActivity.this.finish();
                            return true;
                        }
                    })


                    .addStickyDrawerItems(
                            new SecondaryDrawerItem().withName(R.string.SignOut).withIcon(FontAwesome.Icon.faw_sign_out)

                    ).addDrawerItems(

                    )
                    .withOnDrawerItemClickListener(
                            new Drawer.OnDrawerItemClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                    // do something with the clicked item :D
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    //displayView(position);
                                    if (drawerItem != null) {
                                        if (drawerItem.getIdentifier() == 1) {
                                            fragmentTransaction.replace(R.id.main, new Fragment_Main_Dashboard());
                                        }

                                        if (drawerItem.getIdentifier() == 5) {
                                            getNextGenSiteDeatils(DashboardActivity.this);
                                        }
                                        if (drawerItem.getIdentifier() == 6) {
                                            switchToKendraFinalPhoto();
                                        }
                                        if (position == -1) {
                                            AsyncLogout task = new AsyncLogout();
                                            task.execute();

                                            stopService(new Intent(DashboardActivity.this, FloatingHelplineService.class));
                                            stopService(new Intent(DashboardActivity.this, UpdateUserLatLngService.class));
                                            Connection c = new Connection(DashboardActivity.this);
                                            c.setTokenIdnull();

                                            progress = new ProgressDialog(DashboardActivity.this);
                                            progress.setTitle(getResources().getString(R.string.LogoutyourAccount));
                                            progress.setMessage(getResources().getString(R.string.PleaseWait));
                                            progress.setCancelable(false);
                                            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                            progress.show();

                                            if (Constants.ENABLE_FRANCHISEE_MODE || Constants.ENABLE_ADHOC_MODE) {
                                                if (Constants.ENABLE_FRANCHISEE_LOGIN) {
                                                    CommonUtils.clearFranchiseeLoginDataFromPreferences(DashboardActivity.this);
                                                    Intent i = new Intent(DashboardActivity.this, FranchiseeLoginActivity.class);
                                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(i);
                                                    finish();
                                                } else {
                                                    Intent i = new Intent(DashboardActivity.this, AdhocLoginActivity.class);
                                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            } else {
                                                Intent i = new Intent(DashboardActivity.this, MainActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(i);
                                                finish();
                                            }
                                        }
                                        // Handling Click of Work Commencement. [11-06-2018 By Dpk]
                                        if (drawerItem.getIdentifier() == 201) {

                                            if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
                                                AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
                                            } else {
                                                Connection connection = new Connection(getApplicationContext());
                                                String vkid = connection.getVkid();
                                                if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
                                                    AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
                                                } else if (vkid.toUpperCase().startsWith("VL") || vkid.toUpperCase().startsWith("VA")) {
                                                    Intent intent = new Intent(DashboardActivity.this, MyVakrangeeKendraLocationDetailsNextGen.class);
                                                    intent.putExtra("MODE", Constants.NEXTGEN_SITE_WORK_COMMENCEMENT);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Globals sharedData = Globals.getInstance();
                                                    sharedData.setValue(0);
                                                    new AsyncNextGenFranchiseeDetails(Constants.NEXTGEN_SITE_WORK_COMMENCEMENT, DashboardActivity.this, vkid).execute(vkid);
                                                    // new AsyncGetmyVakrangeeKendraTimingsResponseNextGen(DashboardActivity.this).execute(vkidd, TokenID, imei, deviceid, simserialnumber);
                                                }
                                            }
                                        }
                                        //NextGen Site Work In Progress
                                        if (drawerItem.getIdentifier() == 202) {
                                            switchToNextGenWorkInProgress();
                                        }
                                        //Support Ticket
                                        if (drawerItem.getIdentifier() == 203) {
                                            Intent intent = new Intent(DashboardActivity.this, SupportTicketActivity.class);
                                            intent.putExtra("IsBackVisible", true);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }

                                        // Site Readiness
                                        if (drawerItem.getIdentifier() == 204) {
                                            switcingTOLocationDetails(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION);
                                        }
                                        if (drawerItem.getIdentifier() == 2010) {
                                            switcingTOLocationDetails(Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION);
                                        }
                                        if (drawerItem.getIdentifier() == 2011) {
                                            switcingTOLocationDetails(Constants.NEXTGEN_SITE_KENDRA_INTERIORS_COMPLETED);
                                        }
                                        if (drawerItem.getIdentifier() == 2012) {
                                            switcingTOLocationDetails(Constants.NEXTGEN_SITE_INAUGURATION_RELUNCH_COMPLETED);
                                        }

                                        // Work Completion Intimation
                                        if (drawerItem.getIdentifier() == 205) {
                                            //Site Completion Intimation
                                            switcingTOLocationDetails(Constants.NEXTGEN_WORK_COMPLETION_INTIMATION);
                                        }

                                        //Update Franchisee Details
                                        if (drawerItem.getIdentifier() == 206) {
                                            Intent intent = new Intent(DashboardActivity.this, GSTDetailsActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                        //Update Franchisee Details
                                        if (drawerItem.getIdentifier() == 207) {
                                            Intent intent = new Intent(DashboardActivity.this, MyhelpLineActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                        if (drawerItem.getIdentifier() == 208) {
                                            Intent intent = new Intent(DashboardActivity.this, SimcardDetailsActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            /*Intent intent = new Intent(DashboardActivity.this, AgreementDispatchActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();*/
                                          /*  Intent intent = new Intent(DashboardActivity.this, MyTrainingVideoActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();*/
                                        }

                                        // Nextgen Franchisee Application
                                        if (drawerItem.getIdentifier() == 209) {
                                            getNextGenFranhiseeApp(DashboardActivity.this);
                                        }

                                        // Nextgen Vakrangee Kendra Acknowledgement
                                        if (drawerItem.getIdentifier() == 210) {
                                            Intent intent = new Intent(DashboardActivity.this, KendraAcknowledgementActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            // finish();
                                        }
                                        if (drawerItem.getIdentifier() == 211) {
                                            Intent intent = new Intent(DashboardActivity.this, DocumentManagerActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            //finish();
                                        }

                                        if (drawerItem.getIdentifier() == 212) {
                                            switchToGWR();
                                        }
                                        if (drawerItem.getIdentifier() == 213) {
                                            switchToGWRWitness("Witness");
                                            //Intent intent = new Intent(DashboardActivity.this, GWRActivity.class);
                                            //startActivity(intent);
                                        }
                                        if (drawerItem.getIdentifier() == 216) {
                                            switchToGWRWitness("Camera");
                                            //Intent intent = new Intent(DashboardActivity.this, GWRActivity.class);
                                            //startActivity(intent);
                                        }

                                        if (drawerItem.getIdentifier() == 214) {
                                            switchToGWRAttendanceActivity();

                                        }

                                        if (drawerItem.getIdentifier() == 215) {
                                            switchToGWRUploadWitnessStatement();
                                        }

                                        if (drawerItem.getIdentifier() == 217) {
                                            switchToGWREventPhotos();
                                        }

                                        if (drawerItem.getIdentifier() == 218) {
                                            Intent intent = new Intent(DashboardActivity.this, GWRPropreitaryActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                        }

                                        if (drawerItem.getIdentifier() == 219) {
                                            if (!IsEventDayOpened) {
                                                if (!TextUtils.isEmpty(eventDayMsg))
                                                    showMessage(eventDayMsg);

                                            } else {
                                                Intent intent = new Intent(DashboardActivity.this, GWREventDayActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);

                                            }
                                        }

                                        if (drawerItem.getIdentifier() == 220) {
                                            Intent intent = new Intent(DashboardActivity.this, TrackingHardwareActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                        }
                                        if (drawerItem.getIdentifier() == 221) {
                                            Intent intent = new Intent(DashboardActivity.this, KendraAcknowledgementActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                        }

                                        if (drawerItem.getIdentifier() == 222) {
                                            getBCAEntryDetails(DashboardActivity.this);
                                           /*
                                            Intent intent = new Intent(DashboardActivity.this, BCADetailEntryStartUpActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();*/
                                        }
                                        if (drawerItem.getIdentifier() == 223) {
                                            Intent intent = new Intent(DashboardActivity.this, KendraFrontagePhotoActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }

                                        if (drawerItem.getIdentifier() == 225) {
                                            Intent intent = new Intent(DashboardActivity.this, DeliveryAddressActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                        }
                                        if (drawerItem.getIdentifier() == 226) {
                                            Intent intent = new Intent(DashboardActivity.this, PaymentHistoryActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                        }
                                        if (drawerItem.getIdentifier() == 227) {
                                            Intent intent = null;
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                                intent = new Intent(DashboardActivity.this, NetworkTestingActivity.class);
                                            }
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            //finish();
                                        }

                                        if (drawerItem.getIdentifier() == 228) {
                                            Intent intent = new Intent(DashboardActivity.this, KendraWorkingTimeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                        }
                                        if (drawerItem.getIdentifier() == 300) {
                                            Intent intent = new Intent(DashboardActivity.this, PaymentDetailsActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                        }
                                        if (drawerItem.getIdentifier() == 301) {
                                            getPreBCAEntryDetails(DashboardActivity.this);

                                        }

                                        if (drawerItem.getIdentifier() == 302) {
                                            Intent intent = new Intent(DashboardActivity.this, ATMTechLiveCheckListActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                        }
                                        if (drawerItem.getIdentifier() == 3031) {
                                            Intent intent = new Intent(DashboardActivity.this, LoanDocumentActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                        }
                                        if (drawerItem.getIdentifier() == 3032) {
                                            Intent intent = new Intent(DashboardActivity.this, UploadAcknowledgementActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                        }
                                        if (drawerItem.getIdentifier() == 3033) {
                                            Intent intent = new Intent(DashboardActivity.this, UBILoanSanctionActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                        }

                                        if (drawerItem.getIdentifier() == 2241) {
                                            Intent intent = new Intent(DashboardActivity.this, ATMCashSourcingActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                        }

                                        if (drawerItem.getIdentifier() == 2242) {
                                            Intent intent = new Intent(DashboardActivity.this, ATMCashLoadingActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                        }

                                        if (drawerItem.getIdentifier() == 304) {
                                            getFinalRMApprovalDetails(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION);

                                        }

                                        if (drawerItem.getIdentifier() == 305) {
                                            String FrEnquiryId = CommonUtils.getEnquiryId(DashboardActivity.this);
                                            getSpecificFrEvidenceList(null, FrEnquiryId);

                                        }
                                        if (drawerItem.getIdentifier() == 1003) {
                                           String URL = "https://www.vakrangee.in/privacy.html";
                                            Intent i = new Intent(DashboardActivity.this, WebviewActivity.class);
                                            i.putExtra("path",URL);
                                            startActivity(i);

                                            //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.vakrangee.in/privacy.html"));
                                           // startActivity(browserIntent);
                                        }
                                    }
                                    return false;
                                }

                            }

                    )
                    .withSavedInstance(savedInstanceState)
                    .build();
            for (IDrawerItem iDrawerItem : iDrawerItems) {
                drawer.addItem(iDrawerItem);
            }
            for (ExpandableBadgeDrawerItem expandableBadgeDrawerItema : expandableBadgeDrawerItems) {
                drawer.addItem(expandableBadgeDrawerItema);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Get GWR Dashboard Data
        //loadGWRDetails();         //Commented on 8th Feb by Vasundhara-- Done with the GWR module

        //Handle Phase wise Menus
        handlePhaseWiseMenus();

        //if VKID show Kendra Acknowledge
    //    checkVKIDFound();

        //is drawer item allowed or not
        isDrawerItemAllowed();

        //Dashboard
        if (!TextUtils.isEmpty(connection.getVkid())) {

            Intent intent = new Intent(this, TimeService.class);
            intent.putExtra("VKID", connection.getVkid());
            startService(intent);
        }
    }

    private void backPressed() {
        finish();

    }

    private void isDrawerItemAllowed() {
        asyncIsDrawerItemAllowed = new AsyncIsDrawerItemAllowed(DashboardActivity.this, new AsyncIsDrawerItemAllowed.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.Warning));
                        return;
                    }
                    //Response
                    if (result.startsWith("ERROR|")) {
                        result = result.replace("ERROR|", "");
                        if (TextUtils.isEmpty(result)) {
                            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.Warning));
                        } else {
                            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, result);
                        }
                    } else if (result.startsWith("OKAY|")) {
                        result = result.replace("OKAY|", "");

                        JSONArray jsonarray = new JSONArray(result);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String name = jsonobject.getString("name");
                            String display = jsonobject.getString("display");
                            String menu_id = jsonobject.getString("menu_id");

                            if (!TextUtils.isEmpty(display) && display.equalsIgnoreCase("Y")) {
                                if (drawer.getDrawerItem(Long.parseLong(menu_id)) == null) {
                                    if (menu_id.equalsIgnoreCase("303")) {
                                        drawer.addItem(new ExpandableDrawerItem().withName("UBI Loan").withIdentifier(303).withSetSelected(false).withSelectable(false).withSubItems(
                                                new SecondaryDrawerItem().withName("Upload Loan Document").withLevel(2).withIdentifier(3031).withSelectable(false),
                                                new SecondaryDrawerItem().withName("Upload Acknowledgement").withLevel(2).withIdentifier(3032).withSelectable(false),
                                                new SecondaryDrawerItem().withName("Upload Sanction Letter").withLevel(2).withIdentifier(3033).withSelectable(false)

                                        ));
                                    } else  if (menu_id.equalsIgnoreCase("224")) {
                                        drawer.addItem(new ExpandableDrawerItem().withName("ATM Activity").withIdentifier(224).withSetSelected(false).withSelectable(false).withSubItems(
                                                new SecondaryDrawerItem().withName("Cash Withdrawal").withLevel(2).withIdentifier(2241).withSelectable(false),
                                                new SecondaryDrawerItem().withName("Cash Loading").withLevel(2).withIdentifier(2242).withSelectable(false)
                                        ));
                                    } else {
                                        drawer.addItem(new SecondaryDrawerItem().withName(name).withIdentifier(Long.parseLong(menu_id)));
                                    }
                                }
                            } else {
                                drawer.removeItem(Long.parseLong(menu_id));
                            }

                            if (!TextUtils.isEmpty(name)) {
                                drawer.updateName(Long.parseLong(menu_id), new com.mikepenz.materialdrawer.holder.StringHolder(name));
                            }
                        }
                    } else {
                        AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.Warning));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.Warning));
                }
            }
        });
        asyncIsDrawerItemAllowed.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

    }

    private void getSpecificFrEvidenceList(String userVKId, String FrEnquiryId){
        Intent intent = new Intent(DashboardActivity.this, FrExitEvidenceCollectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("USER_VKID",userVKId);
        intent.putExtra("FR_ENQUIRY_ID",FrEnquiryId);
        startActivity(intent);
    }

    //region if VKID found -show Kendra Acknowledge barcode data
    private void checkVKIDFound() {
        if (!TextUtils.isEmpty(connection.getVkid())) {
            drawer.updateName(221, new com.mikepenz.materialdrawer.holder.StringHolder(getResources().getString(R.string.next_gen_kendra_acknowledgement)));
        } else {
            drawer.removeItem(221);
        }
    }
    //endregion

    private void handlePhaseWiseMenus() {
        asyncGetFranchiseePhaseInfo = new AsyncGetFranchiseePhaseInfo(DashboardActivity.this, new AsyncGetFranchiseePhaseInfo.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        executePhase0();
                        return;
                    }

                    Gson gson = new Gson();
                    PhaseInfoDto phaseInfoDto = gson.fromJson(result, PhaseInfoDto.class);
                    if (phaseInfoDto == null) {
                        executePhase0();
                        return;
                    }

                    txtPhaseName.setText(phaseInfoDto.getNextGenPhaseName());

                    //Handle Menus
                    CommonUtils.setFranchiseePhaseInfoIntoPreferences(DashboardActivity.this, phaseInfoDto);
                    if (TextUtils.isEmpty(phaseInfoDto.getNextGenPhaseCode()) || phaseInfoDto.getNextGenPhaseCode().equalsIgnoreCase(Constants.PHASE_0)) {
                        //FOR Phase-0: Remove "Work Commencement" and "Work In Progress", Rename "Site Readiness Updation"
                        executePhase0();
                    }

                    if (phaseInfoDto.getNextGenPhaseCode().equalsIgnoreCase(Constants.PHASE_1)) {
                        //FOR Phase-1: Add "Work Commencement" and "Work In Progress", Rename "Work Completion Intimation"

                        drawer.updateName(204, new com.mikepenz.materialdrawer.holder.StringHolder("Kendra Work Completion Intimation"));
                      /*  int readinessPos = drawer.getPosition(204);
                        List<IDrawerItem> iDrawerItems = drawer.getDrawerItems();
                        iDrawerItems.set(readinessPos, new SecondaryDrawerItem().withName("Work Completion Intimation").withIdentifier(204));*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        asyncGetFranchiseePhaseInfo.execute("");
    }

    private void executePhase0() {
        //FOR Phase-0: Remove "Work Commencement" and "Work In Progress", Rename "Site Readiness Updation"
        drawer.removeItem(201);
        drawer.removeItem(202);


        drawer.updateName(204, new com.mikepenz.materialdrawer.holder.StringHolder("Site Readiness Updation"));

        /*int readinessPos = drawer.getPosition(204);
        List<IDrawerItem> iDrawerItems = drawer.getDrawerItems();
        iDrawerItems.set(readinessPos, new SecondaryDrawerItem().withName("Site Readiness Updation").withIdentifier(204));*/

    }

    private void loadGWRDetails() {
        layoutGWRDashboard = findViewById(R.id.layoutGWRDashboard);
        fragmentGWRDashboard = (GWRDashboardFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentGWRDashboard);

        if (TextUtils.isEmpty(connection.getVkid())) {
            drawer.removeItem(1000);
            layoutGWRDashboard.setVisibility(View.GONE);
        } else {
            asyncGetGWRDashboardData = new AsyncGetGWRDashboardData(DashboardActivity.this, connection.getVkid(), new AsyncGetGWRDashboardData.Callback() {
                @Override
                public void onResult(String result) {
                    try {
                        if (TextUtils.isEmpty(result)) {
                            drawer.removeItem(1000);
                            layoutGWRDashboard.setVisibility(View.GONE);
                            return;
                        }

                        Gson gson = new Gson();
                        GWRDashboardDto dashboardDto = gson.fromJson(result, GWRDashboardDto.class);
                        if (dashboardDto == null)
                            return;

                        //Check if Activity not Over and Is GWR VkId, then display GWR menus and Fragment
                        String IsActivityOver = TextUtils.isEmpty(dashboardDto.getIsActivityOver()) ? "1" : dashboardDto.getIsActivityOver();
                        String IsGWRVkId = TextUtils.isEmpty(dashboardDto.getIsGWRVKId()) ? "0" : dashboardDto.getIsGWRVKId();
                        String IsEventDay = TextUtils.isEmpty(dashboardDto.getIsEventDayOpened()) ? "0" : dashboardDto.getIsEventDayOpened();
                        eventDayMsg = dashboardDto.getEventDayMsg();
                        IsEventDayOpened = IsEventDay.equalsIgnoreCase("0") ? false : true;

                        if (IsActivityOver.equalsIgnoreCase("1")) {
                            drawer.removeItem(1000);
                            layoutGWRDashboard.setVisibility(View.GONE);

                        } else if (IsGWRVkId.equalsIgnoreCase("0")) {
                            drawer.removeItem(1000);
                            layoutGWRDashboard.setVisibility(View.GONE);

                        } else {       //Send InAuguration Status to Fragment
                            layoutGWRDashboard.setVisibility(View.VISIBLE);
                            fragmentGWRDashboard.refresh(dashboardDto);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            asyncGetGWRDashboardData.execute("");
        }

        //Start Service to upload Raw Images
        startService(new Intent(DashboardActivity.this, UploadEvidenceFilesService.class));
    }

    public void getNextGenSiteDeatils(Context context) {
        Connection connection = new Connection(context);
        String vkid = connection.getVkid();
        IsVKIDAvailable = !TextUtils.isEmpty(vkid) ? true : false;
        if (!InternetConnection.isNetworkAvailable(context)) {
            AlertDialogBoxInfo.alertDialogShow(context, context.getResources().getString(R.string.internetCheck));

        } else if (vkid.toUpperCase().startsWith("VL") || vkid.toUpperCase().startsWith("VH")) {
            // Intent intent = new Intent(DashboardActivity.this, NextGenPhotoViewPager.class);
            Intent intent = new Intent(context, MyVakrangeeKendraLocationDetailsNextGen.class);
            //  Intent intent = new Intent(DashboardActivity.this, NextGenPhotoViewPager.class);
            intent.putExtra("MODE", Constants.NEXTGEN_SITE_VISIT);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        } else {
            Globals sharedData = Globals.getInstance();
            sharedData.setValue(0);
            if (!IsVKIDAvailable) {
                new AsyncNextGenFranchiseeDetails(Constants.NEXTGEN_SITE_VISIT, context, "").execute("");
            } else {
                new AsyncNextGenFranchiseeDetails(Constants.NEXTGEN_SITE_VISIT, context, vkid).execute(vkid);
            }
            // new AsyncGetmyVakrangeeKendraTimingsResponseNextGen(DashboardActivity.this).execute(vkidd, TokenID, imei, deviceid, simserialnumber);
        }

    }

    public void getNextGenFranhiseeApp(Context context) {

        FranchiseeLoginChecksDto loginChecksDto = CommonUtils.getFranchiseeLoginDataFromPreferences(context);
        if (loginChecksDto != null && !TextUtils.isEmpty(loginChecksDto.getNextGenEnquiryId()) && !loginChecksDto.getNextGenEnquiryId().equalsIgnoreCase("0")) {

            //new AsyncGetFranchiseeApplicationByEnquiryId(context).execute(loginChecksDto.getNextGenEnquiryId());

            //Commented for next release--- Vassundhara 21st Feb 2019
            boolean IsNewExistingCheckDone = false;
            if (IsNewExistingCheckDone) {
                new AsyncGetFranchiseeApplicationByEnquiryId(context).execute(loginChecksDto.getNextGenEnquiryId());
            } else {
                //Display Dialog
                displayNewOrExistingFranchiseeDialog(context, loginChecksDto.getNextGenEnquiryId());
            }
        } else {
            showMessage("Something went wrong. Please logOut and try it again.");
        }
    }

    private void displayNewOrExistingFranchiseeDialog(final Context context, final String enquiryId) {
        asyncNewExistingFranchiseeChecks = new AsyncNewExistingFranchiseeChecks(context, Constants.FROM_ISREQUIRE_CHECK, null, new AsyncNewExistingFranchiseeChecks.Callback() {
            @Override
            public void onResult(String result) {
                try {
                    if (TextUtils.isEmpty(result)) {
                        AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                        return;
                    }

                    if (result.startsWith("ERROR")) {
                        String msg = result.replace("ERROR|", "");
                        msg = TextUtils.isEmpty(msg) ? "Something went wrong. Please try again later." : msg;
                        AlertDialogBoxInfo.alertDialogShow(context, msg);
                        return;
                    }

                    if (result.equalsIgnoreCase("OKAY|Y")) {
                        CustomNextGenApplicationFirstScreenDialog dialog = new CustomNextGenApplicationFirstScreenDialog(context);
                        dialog.setCancelable(false);
                        dialog.show();

                    } else if (result.equalsIgnoreCase("OKAY|N")) {
                        new AsyncGetFranchiseeApplicationByEnquiryId(context).execute(enquiryId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertDialogBoxInfo.alertDialogShow(context, getResources().getString(R.string.Warning));
                }
            }
        });
        asyncNewExistingFranchiseeChecks.execute("");
    }

    private void loadFragment(DashboardMenuFragement dashboardMenuFragement) {
        // create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.container, dashboardMenuFragement);
        fragmentTransaction.commit(); // save the changes
    }

    private void camerapermission() {
        try {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(DashboardActivity.this
                    ,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this,
                        Manifest.permission.CAMERA)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(DashboardActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_LOCATION);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        } catch (Exception e) {
            e.getMessage();
            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.permissionnoallow));

        }
    }

    private void locationpermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(DashboardActivity.this
                ,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(DashboardActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == SYSTEM_ALERT_WINDOW_PERMISSION) {
                if (IsVKIDAvailable) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.canDrawOverlays(DashboardActivity.this)) {
                            startService(new Intent(DashboardActivity.this, FloatingHelplineService.class));
                        }
                    }
                }
            } /*else {
                Toast.makeText(getApplicationContext(), "image not capture", Toast.LENGTH_LONG).show();
            }*/
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                try {
                    profile.withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_android).backgroundColorRes(R.color.accent).sizeDp(48).paddingDp(4));
                    headerResult.updateProfileByIdentifier(profile);

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    this.finish();
                    return true;
                } catch (Exception e) {
                    e.getMessage();
                }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onBackPressed() {

        if (drawer != null && drawer.isDrawerOpen()) {
            Log.e("drawer close", "onBackPressed");
            drawer.closeDrawer();
        } else {
            Log.e("My Tags", "onBackPressed");
            k++;
            if (k == 1) {
                if (drawer != null)
                    drawer.closeDrawer();
                Toast.makeText(DashboardActivity.this, "Please press again to exit.", Toast.LENGTH_SHORT).show();
            } else {
                AsyncLogout task = new AsyncLogout();
                task.execute();
                Connection c = new Connection(DashboardActivity.this);
                c.setTokenIdnull();
                if (Constants.ENABLE_FRANCHISEE_MODE || Constants.ENABLE_ADHOC_MODE) {

                    if (Constants.ENABLE_FRANCHISEE_LOGIN) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        finish();

                    } else {
                        Intent i = new Intent(DashboardActivity.this, AdhocLoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
                Log.e("logout App", "onBackPressed");

            }
        }
    }

    private class AsyncLogout extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            logout();
            return null;
        }

        private void logout() {
            try {
                String vkid = EncryptionUtil.encryptString(getVkid, getApplicationContext());
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                final String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceId, getApplicationContext());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

                String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
                String simopertaor = EncryptionUtil.encryptString(simSerial, getApplicationContext());

                diplayServerResopnse = WebService.logOutFranchisee(vkid, imei, deviceid, simopertaor);

                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);
            } catch (Exception e) {
                Log.d(TAG, "Catch" + e.getMessage());
            }
        }


        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            Log.e(TAG + "Already Register ", diplayServerResopnse);
            try {
                /**
                 * METHOD: logOutFranchisee
                 * RESPONSE:
                 * 1. Log Out Successfull. :
                 *    ELSE : Warning! Oops, some error encountered. Please try again.
                 */

                if (diplayServerResopnse == null) {
                    AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.Warning));
                    String message = null;
                    Log.i("TAG", ((message == null) ? "string null" : message));
                } else if (diplayServerResopnse.equals("Log Out Successfull.")) {
                    Log.e(TAG + "OKAY ", diplayServerResopnse);
                } else {
                    AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.Warning));
                    Log.e(TAG + "Issue in Server ", diplayServerResopnse);
                }
            } catch (Exception e) {
                Log.d(TAG, "Error" + e);
                AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.Warning));
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //  isMinimized = true;

        if (fragmentGWRDashboard != null)
            fragmentGWRDashboard.stopGWR();
    }

    @Override
    public void onPause() {
        super.onPause();
        // isMinimized = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    String vkid;
    String vkidd;
    String TokenID;

    public void switchToNextGenWorkInProgress() {
        /*Intent intent = new Intent(DashboardActivity.this, WorkInProgressChatViewActivity.class);
        intent.putExtra("MODE", Constants.NEXT_GEN_WORK_IN_PROGRESS);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();*/

        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
        } else {
            new AsyncGetWIPChatViewStatus(DashboardActivity.this).execute("");
        }

        //region Old Work In Progress
       /*
       if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
        } else {

            Connection connection = new Connection(getApplicationContext());
            vkid = connection.getVkid();
            vkidd = EncryptionUtil.encryptString(connection.getVkid(), getApplicationContext());
            TokenID = EncryptionUtil.encryptString(connection.getTokenId(), getApplicationContext());

            permissionHandler.requestPermission(imgEditProfile, Manifest.permission.READ_PHONE_STATE, getString(R.string.needs_permission_phone_state_msg), new IPermission() {
                @Override
                public void IsPermissionGranted(boolean IsGranted) {
                    if (IsGranted) {
                        DeviceInfo deviceInfo = DeviceInfo.getInstance(DashboardActivity.this);

                        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
                            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
                        } else if (vkid.toUpperCase().startsWith("VL") || vkid.toUpperCase().startsWith("VA")) {

                            Intent intent = new Intent(DashboardActivity.this, MyVakrangeeKendraLocationDetailsNextGen.class);
                            intent.putExtra("MODE", Constants.NEXT_GEN_WORK_IN_PROGRESS);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        } else {
                            new AsyncNextGenFranchiseeDetails(Constants.NEXT_GEN_WORK_IN_PROGRESS, DashboardActivity.this, vkid).execute(vkid);
                        }
                    }
                }
            });
        }*/
        //endregion
    }

    public void switchToGWRWitness(final String name) {
        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
        } else {

            Connection connection = new Connection(getApplicationContext());
            vkid = connection.getVkid();
            permissionHandler.requestPermission(imgEditProfile, Manifest.permission.READ_PHONE_STATE, getString(R.string.needs_permission_phone_state_msg), new IPermission() {
                @Override
                public void IsPermissionGranted(boolean IsGranted) {
                    if (IsGranted) {
                        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
                            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
                        } else {
                            new AsyncGetGWR(DashboardActivity.this, vkid).execute(name);
                        }
                    }
                }
            });

        }
    }

    public void switchToGWR() {
        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
        } else {

            Connection connection = new Connection(getApplicationContext());
            vkid = connection.getVkid();
            permissionHandler.requestPermission(imgEditProfile, Manifest.permission.READ_PHONE_STATE, getString(R.string.needs_permission_phone_state_msg), new IPermission() {
                @Override
                public void IsPermissionGranted(boolean IsGranted) {
                    if (IsGranted) {
                        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
                            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
                        } else {
                            new AsyncGetGWR(DashboardActivity.this, vkid).execute("");
                        }
                    }
                }
            });
        }
    }

    public void switchToGWRAttendanceActivity() {
        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
        } else {

            Connection connection = new Connection(getApplicationContext());
            vkid = connection.getVkid();
            permissionHandler.requestPermission(imgEditProfile, Manifest.permission.READ_PHONE_STATE, getString(R.string.needs_permission_phone_state_msg), new IPermission() {
                @Override
                public void IsPermissionGranted(boolean IsGranted) {
                    if (IsGranted) {
                        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
                            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
                        } else {
                            new AsyncGetGWRAttendanceDetails(DashboardActivity.this, vkid).execute("");
                        }
                    }
                }
            });
        }
    }

    public void switchToGWRUploadWitnessStatement() {
        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
        } else {

            Connection connection = new Connection(getApplicationContext());
            vkid = connection.getVkid();
            permissionHandler.requestPermission(imgEditProfile, Manifest.permission.READ_PHONE_STATE, getString(R.string.needs_permission_phone_state_msg), new IPermission() {
                @Override
                public void IsPermissionGranted(boolean IsGranted) {
                    if (IsGranted) {
                        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
                            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
                        } else {
                            new AsyncGetUploadWitnessStatement(DashboardActivity.this, vkid).execute("");
                        }
                    }
                }
            });
        }
    }

    public void switchToGWREventPhotos() {
        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
        } else {

            Connection connection = new Connection(getApplicationContext());
            vkid = connection.getVkid();
            permissionHandler.requestPermission(imgEditProfile, Manifest.permission.READ_PHONE_STATE, getString(R.string.needs_permission_phone_state_msg), new IPermission() {
                @Override
                public void IsPermissionGranted(boolean IsGranted) {
                    if (IsGranted) {
                        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
                            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
                        } else {
                            new AsyncGetGWREventPhotos(DashboardActivity.this, vkid).execute("");
                        }
                    }
                }
            });
        }
    }

    public void switchToKendraFinalPhoto() {
        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
        } else {

            Connection connection = new Connection(getApplicationContext());
            vkid = connection.getVkid();
            permissionHandler.requestPermission(imgEditProfile, Manifest.permission.READ_PHONE_STATE, getString(R.string.needs_permission_phone_state_msg), new IPermission() {
                @Override
                public void IsPermissionGranted(boolean IsGranted) {
                    if (IsGranted) {
                        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
                            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
                        } else {
                            new AsyncGetKendraFinalPhoto(DashboardActivity.this, vkid).execute("");
                        }
                    }
                }
            });
        }
    }

    public boolean switcingTOLocationDetails(String mode) {
        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
        } else {
            Connection connection = new Connection(getApplicationContext());
            // connection.openDatabase();
            String vkid = connection.getVkid();
            if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
                AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
            } else if (vkid.toUpperCase().startsWith("VL") || vkid.toUpperCase().startsWith("VA")) {
                // Intent intent = new Intent(DashboardActivity.this, NextGenPhotoViewPager.class);
                Intent intent = new Intent(DashboardActivity.this, MyVakrangeeKendraLocationDetailsNextGen.class);
                intent.putExtra("MODE", mode);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            } else {
                Globals sharedData = Globals.getInstance();
                sharedData.setValue(0);
                new AsyncNextGenFranchiseeDetails(mode, DashboardActivity.this, vkid).execute(vkid);
                //new AsyncGetmyVakrangeeKendraTimingsResponseNextGen(DashboardActivity.this).execute(vkidd, TokenID, imei, deviceid, simserialnumber);

            }
        }
        return false;
    }

    public void displayHelplineFlaoting() {

        if (!connection.getVkid().startsWith("VL") && !connection.getVkid().startsWith("VA")) {
            if (checkDrawOverlayPermission()) {
                if (IsVKIDAvailable) {
                    startService(new Intent(DashboardActivity.this, FloatingHelplineService.class));
                }
            }
        }
    }

    public boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
            return false;
        } else {
            return true;
        }
    }

    @Subscribe(channelId = Channel.TWO)
    public void onEvent(EventData event) {
        //displayConnectivitySnackbar(event.getData());
        String actionMsg = getString(R.string.close_text);
        NetworkHealthHandler.displaySnackBar(findViewById(android.R.id.content), event.getData(), actionMsg, deprecateHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, NetworkHealthHandler.prepareIntentFilter());
        CommonUtils.checkNotification(DashboardActivity.this);
    }

    @Override
    protected void onDestroy() {
        // Unregister
        unregisterReceiver(receiver);
        NYBus.get().unregister(this, Channel.TWO);
        super.onDestroy();
        if (asyncIsDrawerItemAllowed != null && !asyncIsDrawerItemAllowed.isCancelled()) {
            asyncIsDrawerItemAllowed.cancel(true);
        }
    }

    public void getBCAEntryDetails(Context context) {
        if (!IsVKIDAvailable) {
            showMessage("No VKID available.");
            return;
        }

        new AsyncGetBCAEntryDetails(context).execute("");
    }

    public void getPreBCAEntryDetails(Context context) {
       /* if (!IsVKIDAvailable) {
            showMessage("No VKID available.");
            return;
        }
*/
        new AsyncGetPreBCADetails(context).execute("");
    }

    public boolean getFinalRMApprovalDetails(String mode) {
        if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
            AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
        } else {
            Connection connection = new Connection(getApplicationContext());
            String vkid = connection.getVkid();
            if (!internetConnection.isNetworkAvailable(getApplicationContext())) {
                AlertDialogBoxInfo.alertDialogShow(DashboardActivity.this, getResources().getString(R.string.internetCheck));
            }
            Globals sharedData = Globals.getInstance();
            sharedData.setValue(0);
            new AsyncGetFinalRMApprovalDetails(mode, DashboardActivity.this, vkid).execute(vkid);

        }
        return false;
    }

    public class AsyncGetVKIDByEnquiryId extends AsyncTask<Void, String, Void> {

        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DashboardActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            FranchiseeLoginChecksDto loginChecksDto = CommonUtils.getFranchiseeLoginDataFromPreferences(DashboardActivity.this);
            if (loginChecksDto == null || loginChecksDto.getNextGenEnquiryId() == null)
                return null;

            response = franchiseeAuthRepo.getVKIDByEnquiryId(loginChecksDto.getNextGenEnquiryId());

            if (!TextUtils.isEmpty(response)) {
                StringTokenizer st1 = new StringTokenizer(response, "\\|");
                String key = st1.nextToken();

                String vkId = "";
                if (key.equalsIgnoreCase("ERROR")) {
                    vkId = "";
                } else {
                    vkId = st1.nextToken().toUpperCase();
                }

                //Insert VKID in to table
                connection.deleteTableinfo();
                connection.insertIntoDB(vkId, "", "", "");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }

            /*Intent intent = new Intent(NextGenFranchiseeEnquiryDetailsActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();*/

        }
    }

}

