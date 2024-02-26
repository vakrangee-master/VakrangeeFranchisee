package in.vakrangee.franchisee.sitelayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.mindorks.nybus.event.Channel;

import in.vakrangee.franchisee.sitelayout.activity.MyVakrangeeKendraLocationDetailsNextGen;
import in.vakrangee.supercore.franchisee.commongui.CustomViewPager;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.Globals;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.network.ConnectivityChangeReceiver;
import in.vakrangee.supercore.franchisee.utils.network.EventData;
import in.vakrangee.supercore.franchisee.utils.network.NetworkHealthHandler;

public class NextGenPhotoViewPager extends AppCompatActivity {


    Toolbar toolbar;
    //    String MondayOpeninigTime, MondayClosingTime;
    FranchiseeDetails franchiseeDetails;
    Connection connection;
//    TelephonyManager telephonyManager;
//    String TAG = "Response";
//    String diplayServerResopnse;

    public CustomViewPager viewPager;

    private boolean isAdhoc = false;
    private NextGenViewPager adapter;
    private DeprecateHandler deprecateHandler;
    private ConnectivityChangeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_gen_web_site_view_pager);

        // Get App MODE
        isAdhoc = Constants.ENABLE_ADHOC_MODE;

        franchiseeDetails = new FranchiseeDetails();
        connection = new Connection(getApplicationContext());
        deprecateHandler = new DeprecateHandler(this);
        receiver = new ConnectivityChangeReceiver();

        franchiseeDetails = (FranchiseeDetails) getIntent().getSerializableExtra("FranchiseeDetails");
        toolbar = (Toolbar) findViewById(R.id.toolbarimage);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {

            if (isAdhoc && !Constants.ENABLE_FRANCHISEE_MODE) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowHomeEnabled(false);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            String title = "NextGen Site Detail";
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_image);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.profile));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.vakrangeekendralocation));
        //tabLayout.addTab(tabLayout.newTab().setText(R.string.vakrangeekendraimage));
        //tabLayout.addTab(tabLayout.newTab().setText(R.string.nextgen_sitevisit));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (CustomViewPager) findViewById(R.id.pager_image);
        viewPager.setPagingEnabled(IsConsonentAgreed());
        adapter = new NextGenViewPager(NextGenPhotoViewPager.this, getSupportFragmentManager(), tabLayout.getTabCount(), franchiseeDetails);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        //Hide viewpager
        // ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);

        //viewPager.setOffscreenPageLimit(adapter.getCount() - 1);  // NOT WORKING
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            // optional
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            // optional
            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    if (!TextUtils.isEmpty(franchiseeDetails.getLatitude()) && TextUtils.isEmpty(franchiseeDetails.getLongitude())) {

                    }
                }
            }

            // optional
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        String from = getIntent().getStringExtra("FROM");
        if (!TextUtils.isEmpty(from) && from.equalsIgnoreCase("NEXT_GEN_LOCATION_DETAIL")) {
            selectFragment(0);
        } else {

            // int s = ((My_vakranggekendra_image) ).getSomeVariable();
            Globals sharedData = Globals.getInstance();
            int n = sharedData.getValue();
            viewPager.setCurrentItem(n, true);
        }

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                boolean IsConsentAgreed = IsConsonentAgreed();
                if (IsConsentAgreed)
                    viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                boolean IsConsentAgreed = IsConsonentAgreed();
                if (IsConsentAgreed)
                    viewPager.setCurrentItem(tab.getPosition());
            }
        });

        //Register
        NYBus.get().register(this, Channel.TWO);
    }

    @Override
    public void onBackPressed() {

        if (isAdhoc && !Constants.ENABLE_FRANCHISEE_MODE) {

          /*  Intent i = new Intent(NextGenPhotoViewPager.this, AdhocLoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);*/
            Toast.makeText(getApplicationContext(), "Login Exit", Toast.LENGTH_SHORT).show();
            finish();


            /*try {

                final AlertDialog.Builder builder = new AlertDialog.Builder(NextGenPhotoViewPager.this);
                //inflate layout from xml. you must create an xml layout file in res/layout first
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.popmvakrangeelocation, null);
                builder.setView(layout);

                TextView txtVerify = (TextView) layout.findViewById(R.id.pleaseVerfity);
                txtVerify.setTypeface(Typeface.SANS_SERIF);
                txtVerify.setText("Are you sure want to exit ?");


                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        *//*Intent i = new Intent(NextGenPhotoViewPager.this, AdhocLoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);*//*
                        finish();


                    }
                });

                builder.setNegativeButton("No  ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();

            } catch (Exception e) {
                e.getMessage();

            }*/

        } else {
            if (Constants.ENABLE_FRANCHISEE_LOGIN) {
                /*Intent intent = new Intent(NextGenPhotoViewPager.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                finish();
            } else {
                if (connection.getVkid().toUpperCase().startsWith("VL")
                        || connection.getVkid().toUpperCase().startsWith("VA")) {

                    super.onBackPressed();
                    Intent intent = new Intent(this, MyVakrangeeKendraLocationDetailsNextGen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
//            super.onBackPressed();
//
//            viewPager.setCurrentItem(0, true);
//
//            Intent intent = new Intent(MyVakrangeeKendraPhotoViewPager.this, MyVakrangeeKendraLocationDetails.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);

                } else {
                    /*Intent intent = new Intent(NextGenPhotoViewPager.this, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                    finish();
                    viewPager.setCurrentItem(0, true);
                }
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Constants.ENABLE_FRANCHISEE_LOGIN) {
                    /*Intent intent = new Intent(NextGenPhotoViewPager.this, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                    finish();
                } else {
                    if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {

                        super.onBackPressed();
                        Intent intent = new Intent(this, MyVakrangeeKendraLocationDetailsNextGen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    } else {
                        /*Intent intent = new Intent(NextGenPhotoViewPager.this, DashboardActivity.class);
                        startActivity(intent);*/
                        fileList();
                    }
                }
                // Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    public void selectFragment(int i) {
        viewPager.setCurrentItem(i, true);
        //MyVakrangeeKendraPhotoViewPager myuser = new MyVakrangeeKendraPhotoViewPager();
        Globals sharedData = Globals.getInstance();
        sharedData.setValue(i);
    }

    public boolean IsConsonentAgreed() {
        boolean IsValid = false;

        if (!TextUtils.isEmpty(franchiseeDetails.getConsentStatus()) && franchiseeDetails.getConsentStatus().equalsIgnoreCase("1")
                && !TextUtils.isEmpty(franchiseeDetails.getLocationStatus()) && franchiseeDetails.getLocationStatus().equalsIgnoreCase("1")) {
            IsValid = true;
        }

        String msg = null;
        if (!IsValid) {
            if (!TextUtils.isEmpty(franchiseeDetails.getLocationStatus()) && franchiseeDetails.getLocationStatus().equalsIgnoreCase("0")) {
                msg = "Since Proposed Vakrangee Kendra site is not identified yet, you can not update site location data.";
            } else if (!TextUtils.isEmpty(franchiseeDetails.getConsentStatus()) && franchiseeDetails.getConsentStatus().equalsIgnoreCase("0")) {
                msg = "You will not be able to move next, If you are not agreed for vakrangee business guidelines then you will not able to move further.";
            }

            showMessage(msg);
        }
        return IsValid;
    }

    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;

    public void showMessage(String msg) {
        //String msg = "Please use this application to capture site information at proposed Vakrangee Kendra Site only. This application tracks your GPS location hence all the data or photographs captured have embedded GPS location of the place where it has been captured. If this application used for capturing data or images at any other location other than proposed Vakrangee Kendra Site, all the data or images captured will be invalid.";

        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(NextGenPhotoViewPager.this);

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


    //region Show Alert Message
    private void showAlertOkMessage(String msg) {

        if (TextUtils.isEmpty(msg))
            return;

        Toast.makeText(NextGenPhotoViewPager.this, msg, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NextGenPhotoViewPager.this);
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        alertDialogBuilder.show();
    }
    //endregion

    public void setFranchiseeDetails(FranchiseeDetails franchiseeDetails) {
        this.franchiseeDetails = franchiseeDetails;
        boolean IsAgreed = IsConsonentAgreed();
        viewPager.setPagingEnabled(IsAgreed);

    }

    public FranchiseeDetails getFranchiseeDetails() {
        return franchiseeDetails;
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

