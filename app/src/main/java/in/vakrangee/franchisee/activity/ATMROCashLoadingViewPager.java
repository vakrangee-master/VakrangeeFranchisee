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

/**
 * Created by nileshd on 6/16/2017.
 */
public class ATMROCashLoadingViewPager extends AppCompatActivity {

    Toolbar toolbar;


    TelephonyManager telephonyManager;
    String TAG = "Response";
    String diplayServerResopnse;

    public ViewPager viewPager;

    Connection connection;
    TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm_ro_cashloading_view_pager);

        toolbar = (Toolbar) findViewById(R.id.toolbarimage);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("ATM RO Cash Loading");


        }
        connection = new Connection(getApplicationContext());
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_image);

        tabLayout.addTab(tabLayout.newTab().setText("Retail Outlet Cash "));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager_image);
        final ViewPagerAdapterATmRoCash adapter = new ViewPagerAdapterATmRoCash
                (getApplicationContext(), getSupportFragmentManager(), tabLayout.getTabCount(), viewPager, tabLayout);

        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        viewPager.setOffscreenPageLimit(limit);
        viewPager.setAdapter(adapter);

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
        Intent intent = new Intent(ATMROCashLoadingViewPager.this, DashboardActivity.class);
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
}
