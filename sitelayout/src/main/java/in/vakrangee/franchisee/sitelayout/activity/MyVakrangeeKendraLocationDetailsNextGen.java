package in.vakrangee.franchisee.sitelayout.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.mindorks.nybus.event.Channel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.vakrangee.franchisee.sitelayout.NextGenPhotoViewPager;
import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.SearchVakrangeeKendraDialog;
import in.vakrangee.franchisee.sitelayout.adapter.AdapterRecyclerViewNextGen;
import in.vakrangee.franchisee.sitelayout.asyntask.AsyncgetLocationDetailsNextGen;
import in.vakrangee.franchisee.sitelayout.mendatorybranding.MandatoryBrandingActivity;
import in.vakrangee.franchisee.sitelayout.sitecommencement.NextGenSiteCommencementActivity;
import in.vakrangee.franchisee.sitelayout.sitecompletion.WorkCompletionIntimationActivity;
import in.vakrangee.franchisee.sitelayout.sitereadiness.SiteReadinessActivity;
import in.vakrangee.franchisee.workinprogress.WorkInProgressDetailActivity;
import in.vakrangee.supercore.franchisee.ifc.OnTaskCompleted;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.LocationKendraDataModel;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.network.ConnectivityChangeReceiver;
import in.vakrangee.supercore.franchisee.utils.network.EventData;
import in.vakrangee.supercore.franchisee.utils.network.NetworkHealthHandler;

public class MyVakrangeeKendraLocationDetailsNextGen extends AppCompatActivity implements OnTaskCompleted {

    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 1;
    Toolbar toolbar;
    ProgressDialog progress;
    TelephonyManager telephonyManager;

    InternetConnection internetConnection;
    private RecyclerView recyclerView;
    private AdapterRecyclerViewNextGen mAdapter;
    ListView listView;
    private TextView emptyView;
    ArrayList<LocationKendraDataModel> dataModels;
    TextView txtSH, txtDL, txtDE, txtBe, txtbelast;
    String getVkid, getTokenId, getimei, getdeviceid, getsimserialnumber;
    private String[] myVakrangeeKendraLocations = new String[5];
    private int NEXTGEN_SITE_TYPE = 1;

    AsyncgetLocationDetailsNextGen asyncTask = new AsyncgetLocationDetailsNextGen(NEXTGEN_SITE_TYPE, MyVakrangeeKendraLocationDetailsNextGen.this, myVakrangeeKendraLocations);

    private SearchVakrangeeKendraDialog searchVakrangeeKendraDialog;

    private Intent intent;
    private boolean isSiteCommencement = false;
    private boolean IsSiteWorkInProgress = false;
    private boolean IsSiteWorkCompletion = false;
    private boolean IsMandatoryBrandingCompleted = false;
    private boolean IsInaugurationRelunchCompleted = false;
    private boolean IsKendraInteriorsCompleted = false;

    private boolean IsSiteReadinessVerification = false;
    private String modetype = null;
    private DeprecateHandler deprecateHandler;
    private ConnectivityChangeReceiver receiver;
    private static final String FRANCHISEE_DETAILS_CONST = "FranchiseeDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vakrangee_kendra_location_details_next_gen);

        handleIntentData();

        toolbar = (Toolbar) findViewById(R.id.toolbarhlep);
        setSupportActionBar(toolbar);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        deprecateHandler = new DeprecateHandler(this);
        receiver = new ConnectivityChangeReceiver();

        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            // If it is Site Readiness - Give Title Name : NextGen Site Readiness
            if (isSiteCommencement) {
                getSupportActionBar().setTitle(R.string.nextgen_sitecommencement);
            } else if (IsSiteWorkInProgress) {
                getSupportActionBar().setTitle(R.string.nextgen_work_in_progress);
            } else if (IsSiteWorkCompletion) {
                getSupportActionBar().setTitle("Work Completion Intimation");
            } else if (IsSiteReadinessVerification) {
                getSupportActionBar().setTitle("Site Readiness Verification");
            } else if (IsMandatoryBrandingCompleted) {
                getSupportActionBar().setTitle("Mandatory Branding Completion");
            }else if (IsKendraInteriorsCompleted) {
                getSupportActionBar().setTitle("Kendra Interiors Completion");
            }else if (IsInaugurationRelunchCompleted) {
                getSupportActionBar().setTitle("INAUGURATION Re-launch Completion");
            } else {
                getSupportActionBar().setTitle(R.string.nextgen_sitevisit);
            }
        }
        emptyView = (TextView) findViewById(R.id.empty_view);
        Connection connection = new Connection(getApplicationContext());

        getVkid = EncryptionUtil.encryptString(connection.getVkid(), getApplicationContext());
        getTokenId = EncryptionUtil.encryptString(connection.getTokenId(), getApplicationContext());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        String deviceIdget = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        getdeviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());

        String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
        getimei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

        String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
        getsimserialnumber = EncryptionUtil.encryptString(simSerial, getApplicationContext());

        internetConnection = new InternetConnection(getApplicationContext());
        txtSH = (TextView) findViewById(R.id.txtname);
        txtDL = (TextView) findViewById(R.id.txtapi);
        txtDE = (TextView) findViewById(R.id.txtversion);
        txtBe = (TextView) findViewById(R.id.txtBe);
        txtbelast = (TextView) findViewById(R.id.txtbelast);

        if (internetConnection.isConnectingToInternet() == false) {
            AlertDialogBoxInfo.alertDialogShow(getApplicationContext(), getResources().getString(R.string.internetCheck));

        } else {

            String scope = EncryptionUtil.encryptString("", getApplicationContext());
            String locationCode = EncryptionUtil.encryptString("0", getApplicationContext());
            myVakrangeeKendraLocations[0] = "Default||0";
            new AsyncgetLocationDetailsNextGen(NEXTGEN_SITE_TYPE, MyVakrangeeKendraLocationDetailsNextGen.this, myVakrangeeKendraLocations).execute(getVkid, getTokenId, getimei, getdeviceid, getsimserialnumber, scope, locationCode);

        }

        asyncTask.delegate = this;

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_kendra_location);

        //Register
        NYBus.get().register(this, Channel.TWO);

        sHClickListener();

        dLClickListener();

        dEClickListener();

        bEClickListener();

        toBeLastClickListener();

    }

    private void handleIntentData() {
        intent = getIntent();
        if (intent != null) {
            modetype = intent.getStringExtra("MODE");
            if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_WORK_COMMENCEMENT)) {
                isSiteCommencement = true;
                NEXTGEN_SITE_TYPE = Constants.NEXTGEN_SITE_COMMENCEMENT_TYPE;
            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXT_GEN_WORK_IN_PROGRESS)) {
                IsSiteWorkInProgress = true;
                NEXTGEN_SITE_TYPE = Constants.NEXTGEN_SITE_WIP_TYPE;
            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_WORK_COMPLETION_INTIMATION)) {
                IsSiteWorkCompletion = true;
                NEXTGEN_SITE_TYPE = Constants.NEXTGEN_WORK_COMPLETION_TYPE;
            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION)) {
                IsSiteReadinessVerification = true;
                NEXTGEN_SITE_TYPE = Constants.NEXTGEN_SITE_READINESS_TYPE;
            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION)) {
                IsMandatoryBrandingCompleted = true;
                NEXTGEN_SITE_TYPE = Constants.NEXTGEN_BRANDING_TYPE;
            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_KENDRA_INTERIORS_COMPLETED)) {
                IsKendraInteriorsCompleted = true;
                NEXTGEN_SITE_TYPE = Constants.NEXTGEN_BRANDING_TYPE;
            } else if (!TextUtils.isEmpty(modetype) && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_INAUGURATION_RELUNCH_COMPLETED)) {
                IsInaugurationRelunchCompleted = true;
                NEXTGEN_SITE_TYPE = Constants.NEXTGEN_BRANDING_TYPE;
            } else {
                NEXTGEN_SITE_TYPE = Constants.NEXTGEN_SITE_VISIT_TYPE;
            }
        }
    }

    private void sHClickListener() {
        txtSH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myVakrangeeKendraLocations[1] = null;
                myVakrangeeKendraLocations[2] = null;
                myVakrangeeKendraLocations[3] = null;
                myVakrangeeKendraLocations[4] = null;

                String scope = EncryptionUtil.encryptString("", getApplicationContext());
                String locationCode = EncryptionUtil.encryptString("0", getApplicationContext());
                new AsyncgetLocationDetailsNextGen(NEXTGEN_SITE_TYPE, MyVakrangeeKendraLocationDetailsNextGen.this, myVakrangeeKendraLocations).execute(getVkid,
                        getTokenId, getimei, getdeviceid, getsimserialnumber, scope, locationCode);

            }
        });

    }

    private void dLClickListener() {
        txtDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVakrangeeKendraLocations[2] = null;
                myVakrangeeKendraLocations[3] = null;
                myVakrangeeKendraLocations[4] = null;

                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[1], "|");
                st1.nextToken();
                String sco1 = st1.nextToken();
                String ty1 = st1.nextToken();
                String scope = EncryptionUtil.encryptString(sco1, getApplicationContext());
                String locationCode = EncryptionUtil.encryptString(ty1, getApplicationContext());
                new AsyncgetLocationDetailsNextGen(NEXTGEN_SITE_TYPE, MyVakrangeeKendraLocationDetailsNextGen.this, myVakrangeeKendraLocations).execute(getVkid, getTokenId, getimei, getdeviceid, getsimserialnumber, scope, locationCode);


            }
        });
    }

    private void dEClickListener() {
        txtDE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVakrangeeKendraLocations[3] = null;
                myVakrangeeKendraLocations[4] = null;
                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[2], "|");
                st1.nextToken();
                String sco1 = st1.nextToken();
                String ty1 = st1.nextToken();
                String scope = EncryptionUtil.encryptString(sco1, getApplicationContext());
                String locationCode = EncryptionUtil.encryptString(ty1, getApplicationContext());
                new AsyncgetLocationDetailsNextGen(NEXTGEN_SITE_TYPE, MyVakrangeeKendraLocationDetailsNextGen.this, myVakrangeeKendraLocations).execute(getVkid,
                        getTokenId, getimei, getdeviceid, getsimserialnumber, scope, locationCode);


            }
        });
    }

    private void bEClickListener() {
        txtBe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVakrangeeKendraLocations[4] = null;

                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[3], "|");
                st1.nextToken();
                String sco1 = st1.nextToken();
                String ty1 = st1.nextToken();
                String scope = EncryptionUtil.encryptString(sco1, getApplicationContext());
                String locationCode = EncryptionUtil.encryptString(ty1, getApplicationContext());
                new AsyncgetLocationDetailsNextGen(NEXTGEN_SITE_TYPE, MyVakrangeeKendraLocationDetailsNextGen.this, myVakrangeeKendraLocations).execute(getVkid,
                        getTokenId, getimei, getdeviceid, getsimserialnumber, scope, locationCode);


            }
        });
    }

    private void toBeLastClickListener() {

        txtbelast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[4], "|");
                st1.nextToken();
                String sco1 = st1.nextToken();
                String ty1 = st1.nextToken();
                String scope = EncryptionUtil.encryptString(sco1, getApplicationContext());
                String locationCode = EncryptionUtil.encryptString(ty1, getApplicationContext());

                new AsyncgetLocationDetailsNextGen(NEXTGEN_SITE_TYPE, MyVakrangeeKendraLocationDetailsNextGen.this, myVakrangeeKendraLocations).execute(getVkid,
                        getTokenId, getimei, getdeviceid, getsimserialnumber, scope, locationCode);


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_serach, menu);
        menu.findItem(R.id.action_search_main);

        return super.onCreateOptionsMenu(menu);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        BackPressMethod();
    }

    private void BackPressMethod() {
        try {

            if (myVakrangeeKendraLocations[0] != null && myVakrangeeKendraLocations[1] != null
                    && myVakrangeeKendraLocations[2] != null && myVakrangeeKendraLocations[3] != null &&
                    myVakrangeeKendraLocations[4] != null) {


                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[0], "|");
                st1.nextToken();

                StringTokenizer st2 = new StringTokenizer(myVakrangeeKendraLocations[1], "|");
                st2.nextToken();

                StringTokenizer st3 = new StringTokenizer(myVakrangeeKendraLocations[2], "|");
                st3.nextToken();

                StringTokenizer st4 = new StringTokenizer(myVakrangeeKendraLocations[3], "|");
                st4.nextToken();
                String scope = st4.nextToken();
                String locationCode = st4.nextToken();

                String scope4 = EncryptionUtil.encryptString(scope, getApplicationContext());
                String locationCode4 = EncryptionUtil.encryptString(locationCode, getApplicationContext());

                StringTokenizer st5 = new StringTokenizer(myVakrangeeKendraLocations[4], "|");
                st5.nextToken();

                new AsyncgetLocationDetailsNextGen(NEXTGEN_SITE_TYPE, MyVakrangeeKendraLocationDetailsNextGen.this, myVakrangeeKendraLocations).execute(getVkid,
                        getTokenId, getimei, getdeviceid, getsimserialnumber, scope4, locationCode4);
                myVakrangeeKendraLocations[4] = null;
                //Second
            } else if (myVakrangeeKendraLocations[0] != null && myVakrangeeKendraLocations[1] != null
                    && myVakrangeeKendraLocations[2] != null && myVakrangeeKendraLocations[3] != null) {

                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[0], "|");
                st1.nextToken();

                StringTokenizer st2 = new StringTokenizer(myVakrangeeKendraLocations[1], "|");
                st2.nextToken();


                StringTokenizer st3 = new StringTokenizer(myVakrangeeKendraLocations[2], "|");
                st3.nextToken();
                String scope = st3.nextToken();
                String locationCode = st3.nextToken();

                String scope3 = EncryptionUtil.encryptString(scope, getApplicationContext());
                String locationCode3 = EncryptionUtil.encryptString(locationCode, getApplicationContext());

                StringTokenizer st4 = new StringTokenizer(myVakrangeeKendraLocations[3], "|");
                st4.nextToken();

                new AsyncgetLocationDetailsNextGen(NEXTGEN_SITE_TYPE, MyVakrangeeKendraLocationDetailsNextGen.this, myVakrangeeKendraLocations).execute(getVkid,
                        getTokenId, getimei, getdeviceid, getsimserialnumber, scope3, locationCode3);
                myVakrangeeKendraLocations[3] = null;
                myVakrangeeKendraLocations[4] = null;

                //Second
            } else if (myVakrangeeKendraLocations[0] != null && myVakrangeeKendraLocations[1] != null
                    && myVakrangeeKendraLocations[2] != null) {

                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[0], "|");
                st1.nextToken();

                StringTokenizer st2 = new StringTokenizer(myVakrangeeKendraLocations[1], "|");
                st2.nextToken();
                String scope = st2.nextToken();
                String locationCode = st2.nextToken();

                String scope1 = EncryptionUtil.encryptString(scope, getApplicationContext());
                String locationCode1 = EncryptionUtil.encryptString(locationCode, getApplicationContext());

                StringTokenizer st3 = new StringTokenizer(myVakrangeeKendraLocations[2], "|");
                st3.nextToken();
                new AsyncgetLocationDetailsNextGen(NEXTGEN_SITE_TYPE, MyVakrangeeKendraLocationDetailsNextGen.this, myVakrangeeKendraLocations).execute(getVkid,
                        getTokenId, getimei, getdeviceid, getsimserialnumber, scope1, locationCode1);
                myVakrangeeKendraLocations[2] = null;
                myVakrangeeKendraLocations[3] = null;
                myVakrangeeKendraLocations[4] = null;

                //Second
            } else if (myVakrangeeKendraLocations[0] != null && myVakrangeeKendraLocations[1] != null) {
                new StringTokenizer(myVakrangeeKendraLocations[0], "|");

                String scope = EncryptionUtil.encryptString("", getApplicationContext());
                String locationCode = EncryptionUtil.encryptString("0", getApplicationContext());

                StringTokenizer st2 = new StringTokenizer(myVakrangeeKendraLocations[1], "|");
                st2.nextToken();

                new AsyncgetLocationDetailsNextGen(NEXTGEN_SITE_TYPE, MyVakrangeeKendraLocationDetailsNextGen.this, myVakrangeeKendraLocations).execute(getVkid,
                        getTokenId, getimei, getdeviceid, getsimserialnumber, scope, locationCode);

                myVakrangeeKendraLocations[1] = null;
                myVakrangeeKendraLocations[2] = null;
                myVakrangeeKendraLocations[3] = null;
                myVakrangeeKendraLocations[4] = null;
                //First
            } else if (myVakrangeeKendraLocations[0] != null) {
                StringTokenizer st1 = new StringTokenizer(myVakrangeeKendraLocations[0], "|");
                st1.nextToken();
                super.onBackPressed();
                finish();
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            BackPressMethod();
        } else if (itemId == R.id.action_home_dashborad) {
            finish();
        } else if (itemId == R.id.action_search_main) {
            if (internetConnection.isConnectingToInternet() == false) {
                AlertDialogBoxInfo.alertDialogShow(MyVakrangeeKendraLocationDetailsNextGen.this, getResources().getString(R.string.internetCheck));
                return false;
            }

            showSearchKendraDialog();
        }
        return true;
    }

    private void showSearchKendraDialog() {
        searchVakrangeeKendraDialog = new SearchVakrangeeKendraDialog(modetype, MyVakrangeeKendraLocationDetailsNextGen.this, new SearchVakrangeeKendraDialog.ISearchVakrangeeKendra() {
            @Override
            public void getFranchiseeDetail(FranchiseeDetails franchiseeDetails, String errMsg) {

                if (!TextUtils.isEmpty(errMsg)) {
                    AlertDialogBoxInfo.alertDialogShow(MyVakrangeeKendraLocationDetailsNextGen.this, errMsg);
                } else {
                    searchVakrangeeKendraDialog.cancelSearchTask();
                    searchVakrangeeKendraDialog.dismiss();

                    if (isSiteCommencement) {
                        Intent intent1 = new Intent(MyVakrangeeKendraLocationDetailsNextGen.this, NextGenSiteCommencementActivity.class);
                        intent1.putExtra("FROM", Constants.NEXT_GEN_LOCATION_DETAIL);
                        intent1.putExtra("MODE", Constants.NEXTGEN_SITE_WORK_COMMENCEMENT);
                        intent1.putExtra(FRANCHISEE_DETAILS_CONST, (Serializable) franchiseeDetails);
                        startActivity(intent1);

                    } else if (IsSiteWorkInProgress) {
                        Intent intent1 = new Intent(MyVakrangeeKendraLocationDetailsNextGen.this, WorkInProgressDetailActivity.class);
                        intent1.putExtra("FROM", Constants.NEXT_GEN_LOCATION_DETAIL);
                        intent1.putExtra("MODE", Constants.NEXT_GEN_WORK_IN_PROGRESS);
                        intent1.putExtra(FRANCHISEE_DETAILS_CONST, (Serializable) franchiseeDetails);
                        startActivity(intent1);

                    } else if (IsSiteWorkCompletion) {
                        Intent intent1 = new Intent(MyVakrangeeKendraLocationDetailsNextGen.this, WorkCompletionIntimationActivity.class);
                        intent1.putExtra("FROM", Constants.NEXT_GEN_LOCATION_DETAIL);
                        intent1.putExtra("MODE", Constants.NEXTGEN_WORK_COMPLETION_INTIMATION);
                        intent1.putExtra(FRANCHISEE_DETAILS_CONST, (Serializable) franchiseeDetails);
                        startActivity(intent1);

                    } else if (IsMandatoryBrandingCompleted) {
                        Intent intent1 = new Intent(MyVakrangeeKendraLocationDetailsNextGen.this, MandatoryBrandingActivity.class);
                        intent1.putExtra("FROM", Constants.NEXT_GEN_LOCATION_DETAIL);
                        intent1.putExtra("MODE", Constants.NEXTGEN_SITE_MANDATORY_BRANDING_VERIFICATION);
                        intent1.putExtra(FRANCHISEE_DETAILS_CONST, (Serializable) franchiseeDetails);
                        startActivity(intent1);

                    } else if (IsKendraInteriorsCompleted) {
                        Intent intent1 = new Intent(MyVakrangeeKendraLocationDetailsNextGen.this, MandatoryBrandingActivity.class);
                        intent1.putExtra("FROM", Constants.NEXT_GEN_LOCATION_DETAIL);
                        intent1.putExtra("MODE", Constants.NEXTGEN_SITE_KENDRA_INTERIORS_COMPLETED);
                        intent1.putExtra(FRANCHISEE_DETAILS_CONST, (Serializable) franchiseeDetails);
                        startActivity(intent1);

                    } else if (IsInaugurationRelunchCompleted) {
                        Intent intent1 = new Intent(MyVakrangeeKendraLocationDetailsNextGen.this, MandatoryBrandingActivity.class);
                        intent1.putExtra("FROM", Constants.NEXT_GEN_LOCATION_DETAIL);
                        intent1.putExtra("MODE", Constants.NEXTGEN_SITE_INAUGURATION_RELUNCH_COMPLETED);
                        intent1.putExtra(FRANCHISEE_DETAILS_CONST, (Serializable) franchiseeDetails);
                        startActivity(intent1);

                    } else if (IsSiteReadinessVerification) {
                        Intent intent1 = new Intent(MyVakrangeeKendraLocationDetailsNextGen.this, SiteReadinessActivity.class);
                        intent1.putExtra("FROM", Constants.NEXT_GEN_LOCATION_DETAIL);
                        intent1.putExtra("MODE", Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION);
                        intent1.putExtra(FRANCHISEE_DETAILS_CONST, (Serializable) franchiseeDetails);
                        startActivity(intent1);

                    } else {
                        Intent intent1 = new Intent(MyVakrangeeKendraLocationDetailsNextGen.this, NextGenPhotoViewPager.class);
                        intent1.putExtra("FROM", Constants.NEXT_GEN_LOCATION_DETAIL);
                        intent1.putExtra(FRANCHISEE_DETAILS_CONST, (Serializable) franchiseeDetails);
                        startActivity(intent1);
                    }
                }
            }
        });
        searchVakrangeeKendraDialog.setCancelable(false);
        searchVakrangeeKendraDialog.show();
    }

    @Override
    public void processFinish(String result) {

        try {
            dataModels = new ArrayList<>();

            StringTokenizer tokens = new StringTokenizer(result, "|");
            tokens.nextToken();
            String second = tokens.nextToken();
            emptyView = (TextView) findViewById(R.id.empty_view_location);
            if (second.equals("No data found.")) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                String third = tokens.nextToken();
                String strJsona = third;
                String strJsonaaa = strJsona.replace("\r\n", "");
                processJsonData(strJsonaaa, second);

            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            AlertDialogBoxInfo.alertDialogShow(MyVakrangeeKendraLocationDetailsNextGen.this, getResources().getString(R.string.Warning));
        }
    }

    private void processJsonData(String strJsonaaa, String second) {
        try {
            JSONArray jsonarray = new JSONArray(strJsonaaa);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);


                String idtype = jsonobject.getString("myModelObject1");
                String name = jsonobject.getString("myModelObject2");

                if (second.equalsIgnoreCase("BE") && modetype.equalsIgnoreCase(Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION)) {

                    String statusNo = TextUtils.isEmpty(jsonobject.getString("myModelObject3")) ? "0" : jsonobject.getString("myModelObject3");
                    String status = TextUtils.isEmpty(jsonobject.getString("myModelObject4")) ? "-" : jsonobject.getString("myModelObject4");
                    String mobileNo = TextUtils.isEmpty(jsonobject.getString("myModelObject5")) ? "-" : jsonobject.getString("myModelObject5");
                    String email = TextUtils.isEmpty(jsonobject.getString("myModelObject6")) ? "-" : jsonobject.getString("myModelObject6");
                    String fUploadCount = TextUtils.isEmpty(jsonobject.getString("myModelObject7")) ? "-" : jsonobject.getString("myModelObject7");
                    String vkId = jsonobject.isNull("myModelObject9") ? "" : jsonobject.getString("myModelObject9");
                    String latitude = jsonobject.isNull("myModelObject10") ? "" : jsonobject.getString("myModelObject10");
                    String longitude = jsonobject.isNull("myModelObject11") ? "" : jsonobject.getString("myModelObject11");
                    String kendraRange = jsonobject.isNull("myModelObject12") ? "0" : jsonobject.getString("myModelObject12");

                    dataModels.add(new LocationKendraDataModel(idtype, name, second, status, mobileNo, email, statusNo, fUploadCount, vkId, latitude, longitude, kendraRange));
                } else {
                    dataModels.add(new LocationKendraDataModel(idtype, name, second));
                }

            }
            // Setup and Handover data to recyclerview
            setNextGenAdapter();

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error Json", e.getMessage());

        }
    }

    private void setNextGenAdapter() {
        mAdapter = new AdapterRecyclerViewNextGen(NEXTGEN_SITE_TYPE, modetype, getApplicationContext(), dataModels, txtSH, txtDL, txtDE, txtBe, txtbelast, myVakrangeeKendraLocations, new AdapterRecyclerViewNextGen.ICallBack() {
            @Override
            public void iCallClick(String mobNo) {
                if (TextUtils.isEmpty(mobNo))
                    return;

                directCall(mobNo);
            }

            @Override
            public void iMailClick(String emailId) {
                if (TextUtils.isEmpty(emailId))
                    return;

                Intent emailintent = new Intent(Intent.ACTION_SENDTO);
                emailintent.setData(Uri.parse("mailto:"));
                emailintent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailId});
                emailintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                emailintent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailintent, "Send mail..."));
                startActivity(emailintent);
            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (dataModels.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    public void directCall(String mobileNo) {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE_CALL);
        } else {
            //Open call function
            Intent intent1 = new Intent(Intent.ACTION_CALL);
            intent1.setData(Uri.parse("tel:" + mobileNo));
            startActivity(intent1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, NetworkHealthHandler.prepareIntentFilter());
    }

    @Override
    protected void onDestroy() {
        // Unregister
        unregisterReceiver(receiver);
        NYBus.get().unregister(this, Channel.TWO);
        super.onDestroy();
    }

    @Subscribe(channelId = Channel.TWO)
    public void onEvent(EventData event) {
        String actionMsg = getString(R.string.close_text);
        NetworkHealthHandler.displaySnackBar(findViewById(android.R.id.content), event.getData(), actionMsg, deprecateHandler);
    }
}