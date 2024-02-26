package in.vakrangee.franchisee.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.Connection;

public class MyAttendanceSystemViewPager extends AppCompatActivity {

    Toolbar toolbar;


    TelephonyManager telephonyManager;
    String TAG = "Response";
    String diplayServerResopnse;

    public ViewPager viewPager;

    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attendance_system_view_pager);

        toolbar = (Toolbar) findViewById(R.id.toolbarimage);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.myAttendance);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
        connection = new Connection(getApplicationContext());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_image);


        tabLayout.addTab(tabLayout.newTab().setText(R.string.myAttendance));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.outdoorduty));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager_image);
        final ViewPagerAdapterAttendanceSystem adapter = new ViewPagerAdapterAttendanceSystem
                (getApplicationContext(), getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);


        // int s = ((My_vakranggekendra_image) ).getSomeVariable();


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });


    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyAttendanceSystemViewPager.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {

                    super.onBackPressed();
                    Intent intent = new Intent(this, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);


                } else {
                    Intent intent = new Intent(this, DashboardActivity.class);
                    startActivity(intent);

                }

                // Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent intent = new Intent(MyAttendanceSystemViewPager.this, DashboardActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
//        return true;
//    }


}
