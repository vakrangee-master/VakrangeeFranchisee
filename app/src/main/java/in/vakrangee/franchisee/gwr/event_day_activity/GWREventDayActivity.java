package in.vakrangee.franchisee.gwr.event_day_activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.supercore.franchisee.commongui.CustomViewPager;
import in.vakrangee.supercore.franchisee.model.Globals;
import in.vakrangee.supercore.franchisee.utils.Connection;

public class GWREventDayActivity extends AppCompatActivity {

    private static final String TAG = "GWREventDayActivity";
    Toolbar toolbar;
    Connection connection;
    public CustomViewPager viewPager;
    private boolean IsEditable;
    private Context context;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gwr_eventday);

        this.context = GWREventDayActivity.this;
        connection = new Connection(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbarEventDay);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            String title = "Event Day Activity";
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_eventday);

        //tabLayout.addTab(tabLayout.newTab().setText("Activity"));
        tabLayout.addTab(tabLayout.newTab().setText("Event Day Photos"));
        tabLayout.addTab(tabLayout.newTab().setText("Upload Witness Statement"));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.orange));
        viewPager = (CustomViewPager) findViewById(R.id.eventday_pager);
        viewPager.setOffscreenPageLimit(3);
        final GWREventDayViewPager adapter = new GWREventDayViewPager(context, getSupportFragmentManager(), tabLayout.getTabCount(), IsEditable);
        viewPager.setAdapter(adapter);

        Globals sharedData = Globals.getInstance();
        int n = sharedData.getValue();
        viewPager.setCurrentItem(n, true);

        Log.e(TAG, "Testing: Tabs Count: " + tabLayout.getTabCount());

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No Use
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }

    public void showMessage(String msg) {
        //String msg = "Please use this application to capture site information at proposed Vakrangee Kendra Site only. This application tracks your GPS location hence all the data or photographs captured have embedded GPS location of the place where it has been captured. If this application used for capturing data or images at any other location other than proposed Vakrangee Kendra Site, all the data or images captured will be invalid.";

        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(context);

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
        backPressed();
    }

    public void backPressed() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backPressed();
                break;
        }
        return true;
    }

    public void selectFragment(int i) {
        viewPager.setCurrentItem(i, true);
        Globals sharedData = Globals.getInstance();
        sharedData.setValue(i);
    }
}
