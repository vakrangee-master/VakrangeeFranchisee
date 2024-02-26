package in.vakrangee.franchisee.sitelayout.finalrmapproval;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.mindorks.nybus.event.Channel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.ResultIPC;
import in.vakrangee.franchisee.sitelayout.activity.MyVakrangeeKendraLocationDetailsNextGen;
import in.vakrangee.supercore.franchisee.commongui.CustomViewPager;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.Globals;
import in.vakrangee.supercore.franchisee.utils.AppUtilsforLocationService;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.network.ConnectivityChangeReceiver;
import in.vakrangee.supercore.franchisee.utils.network.EventData;
import in.vakrangee.supercore.franchisee.utils.network.NetworkHealthHandler;

public class MainFinalRMApprovalActivity extends AppCompatActivity {

    private static final String TAG = "SiteReadinessActivity";
    Toolbar toolbar;
    FranchiseeDetails franchiseeDetails;
    Connection connection;
    public CustomViewPager viewPager;
    private boolean IsEditable;
    private boolean IsWorkStarted = false;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;
    private String msg = "To Proceed further, Please update Work Commencement Date (Start Date) in Profile (First Tab).";
    private DeprecateHandler deprecateHandler;
    private boolean IsBrandingAllowed = false;
    private ConnectivityChangeReceiver receiver;
    private String phaseCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_rm_approval);
        franchiseeDetails = new FranchiseeDetails();
        connection = new Connection(getApplicationContext());
        deprecateHandler = new DeprecateHandler(this);
        receiver = new ConnectivityChangeReceiver();

        //Get Intent Data
        int sync = getIntent().getIntExtra("BIG_DATA:SYNC_CODE", -1);
        franchiseeDetails = (FranchiseeDetails) ResultIPC.get().getLargeData(sync);
        //franchiseeDetails = (FranchiseeDetails) getIntent().getSerializableExtra("FranchiseeDetails");
        IsEditable = getIntent().getBooleanExtra("IS_EDITABLE", false);
        int brandingStatus = TextUtils.isEmpty(franchiseeDetails.getIsBrandingAllowed()) ? 0 : Integer.parseInt(franchiseeDetails.getIsBrandingAllowed());
        IsBrandingAllowed = brandingStatus == 1 ? true : false;

        setWorkStartedStatus();

        //Register
        NYBus.get().register(this, Channel.TWO, Channel.THREE);

        //Get Phase Info
        phaseCode = TextUtils.isEmpty(CommonUtils.getPhaseCode(this)) ? Constants.PHASE_0 : CommonUtils.getPhaseCode(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            String title = "ATM Work Completion Intimation";
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.profile));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.go_live));

        if (!TextUtils.isEmpty(franchiseeDetails.getIsKendraPhotoAllowed()) && franchiseeDetails.getIsKendraPhotoAllowed().equalsIgnoreCase("1")) {
            tabLayout.addTab(tabLayout.newTab().setText(R.string.vakrangeekendraimage));
        }

        viewPager = (CustomViewPager) findViewById(R.id.rmPager);
        viewPager.setOffscreenPageLimit(3);
        final FinalRMApprovalViewPager adapter = new FinalRMApprovalViewPager(MainFinalRMApprovalActivity.this, getSupportFragmentManager(), tabLayout.getTabCount(), franchiseeDetails, IsEditable);
        viewPager.setAdapter(adapter);

        String from = getIntent().getStringExtra("FROM");
        if (!TextUtils.isEmpty(from) && from.equalsIgnoreCase("NEXT_GEN_LOCATION_DETAIL")) {
            selectFragment(0);
        } else {
            Globals sharedData = Globals.getInstance();
            int n = sharedData.getValue();
            viewPager.setCurrentItem(n, true);
        }

        Log.e(TAG, "Testing: Tabs Count: " + tabLayout.getTabCount());

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (phaseCode.equalsIgnoreCase(Constants.PHASE_1)) {
                    viewPager.setCurrentItem(tab.getPosition());
                } else {
                    if (!IsWorkStarted)
                        showMessage(msg);
                    else
                        viewPager.setCurrentItem(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No Use
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (phaseCode.equalsIgnoreCase(Constants.PHASE_1)) {
                    viewPager.setCurrentItem(tab.getPosition());
                } else {
                    if (!IsWorkStarted)
                        showMessage(msg);
                    else
                        viewPager.setCurrentItem(tab.getPosition());
                }
            }
        });

        viewPager.setPagingEnabled(IsWorkStarted);

    }

    public void setWorkStartedStatus() {
        if ((connection.getVkid().startsWith("VL") || connection.getVkid().startsWith("VA"))) {
            IsWorkStarted = true;
        } else {
            int status = TextUtils.isEmpty(franchiseeDetails.getReadinessWorkCommencementStatus()) ? 0 : Integer.parseInt(franchiseeDetails.getReadinessWorkCommencementStatus());
            String date = franchiseeDetails.getReadinessWorkCommencementDate();
            if (status == 1 && !TextUtils.isEmpty(date))
                IsWorkStarted = true;
            else
                IsWorkStarted = false;
        }
        if (viewPager != null)
            viewPager.setPagingEnabled(IsWorkStarted);
    }

    public void showMessage(String msg) {
        //String msg = "Please use this application to capture site information at proposed Vakrangee Kendra Site only. This application tracks your GPS location hence all the data or photographs captured have embedded GPS location of the place where it has been captured. If this application used for capturing data or images at any other location other than proposed Vakrangee Kendra Site, all the data or images captured will be invalid.";

        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(MainFinalRMApprovalActivity.this);

            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert = null;
                        }
                    });
            alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    @Override
    public void onBackPressed() {
        //finish();
        //viewPager.setCurrentItem(0, true);

        if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {
            super.onBackPressed();
            Intent intent = new Intent(this, MyVakrangeeKendraLocationDetailsNextGen.class);
            intent.putExtra("MODE", Constants.NEXTGEN_SITE_READINESS_AND_VERIFICATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            /*Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/

            finish();
            viewPager.setCurrentItem(0, true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {
                    super.onBackPressed();
                    Intent intent = new Intent(this, MyVakrangeeKendraLocationDetailsNextGen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                    /*Intent intent = new Intent(SiteReadinessActivity.this, DashboardActivity.class);
                    startActivity(intent);*/
                    finish();
                }
                break;
        }
        return true;
    }

    public void selectFragment(int i) {
        viewPager.setCurrentItem(i, true);
        Globals sharedData = Globals.getInstance();
        sharedData.setValue(i);
    }

    public void setFranchiseeDetails(FranchiseeDetails franchiseeDetails) {
        this.franchiseeDetails = franchiseeDetails;
        setWorkStartedStatus();
    }

    public FranchiseeDetails getFranchiseeDetails() {
        return franchiseeDetails;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, NetworkHealthHandler.prepareIntentFilter());
        // Check GPS Status
        checkGPSStatus();
    }

    @Override
    protected void onDestroy() {
        // Unregister
        unregisterReceiver(receiver);
        NYBus.get().unregister(this, Channel.TWO, Channel.THREE);
        super.onDestroy();
    }

    @Subscribe(channelId = Channel.TWO)
    public void onEvent(EventData event) {
        String actionMsg = getString(R.string.close_text);
        NetworkHealthHandler.displaySnackBar(findViewById(android.R.id.content), event.getData(), actionMsg, deprecateHandler);
    }

    @Subscribe(channelId = Channel.THREE)
    public void onLocationProviderChange(EventData event) {
        if (event != null) {
            if (!TextUtils.isEmpty(event.getKey())) {
                if (event.getKey().equalsIgnoreCase("PROVIDERS_CHANGED")) {
                    checkGPSStatus();
                }
            }
        }
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    private AlertDialog.Builder locationDialog;

    public void checkGPSStatus() {
        Log.e(TAG, "<<<<< Check GPS Status Called. >>>>>");
        if (!AppUtilsforLocationService.isLocationEnabled(this)) {
            try {
                // notify user
                if (locationDialog == null) {
                    locationDialog = new AlertDialog.Builder(this);
                    locationDialog.setTitle("Enable Location");
                    locationDialog.setMessage("Your Location Setting is set to \"Off\". Please Enable Location to use this app.");
                    locationDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                            paramDialogInterface.cancel();
                            locationDialog = null;
                        }
                    });
                    locationDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            paramDialogInterface.dismiss();
                            locationDialog = null;
                            checkGPSStatus();
                        }
                    });
                    locationDialog.setCancelable(false);
                    locationDialog.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
