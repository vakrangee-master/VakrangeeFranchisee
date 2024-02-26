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
import in.vakrangee.franchisee.adapter.ViewPagerAdapterVakrangeePhoto;
import in.vakrangee.supercore.franchisee.model.Globals;
import in.vakrangee.supercore.franchisee.model.MyVKMaster;
import in.vakrangee.supercore.franchisee.utils.Connection;

public class MyVakrangeeKendraPhotoViewPager extends AppCompatActivity {
    Toolbar toolbar;
    String MondayOpeninigTime, MondayClosingTime;
    MyVKMaster myVKMaster;
    Connection connection;
    TelephonyManager telephonyManager;
    String TAG = "Response";
    String diplayServerResopnse;

    public ViewPager viewPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vakrangee_kendra_photo_view_pager);
        myVKMaster = new MyVKMaster();
        connection = new Connection(getApplicationContext());


        myVKMaster = (MyVKMaster) getIntent().getSerializableExtra("myVKMaster");


        toolbar = (Toolbar) findViewById(R.id.toolbarimage);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.myVakrangeeKendra);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_image);


        tabLayout.addTab(tabLayout.newTab().setText(R.string.vakrangeekendralocation));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.vakrangeekendraimage));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.vakrangeekendratime));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager_image);
        final ViewPagerAdapterVakrangeePhoto adapter = new ViewPagerAdapterVakrangeePhoto
                (getApplicationContext(), getSupportFragmentManager(), tabLayout.getTabCount(), myVKMaster);
        viewPager.setAdapter(adapter);


        // int s = ((My_vakranggekendra_image) ).getSomeVariable();

        Globals sharedData = Globals.getInstance();
        int n = sharedData.getValue();
        viewPager.setCurrentItem(n, true);

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


        if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {

            super.onBackPressed();
            Intent intent = new Intent(this, MyVakrangeeKendraLocationDetails.class);
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
            Intent intent = new Intent(MyVakrangeeKendraPhotoViewPager.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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
                    Intent intent = new Intent(this, MyVakrangeeKendraLocationDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);


                } else {
                    Intent intent = new Intent(MyVakrangeeKendraPhotoViewPager.this, DashboardActivity.class);
                    startActivity(intent);

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
//Set username from text inside edittext


        //My_vakranggekendra_image model = new My_vakranggekendra_image(i);

        //model.setSomeVariable(i);

        // ((My_vakranggekendra_image) getApplicationContext()).setSomeVariable(i);
    }


}

