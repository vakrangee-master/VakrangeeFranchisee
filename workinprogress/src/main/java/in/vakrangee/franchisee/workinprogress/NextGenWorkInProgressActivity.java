package in.vakrangee.franchisee.workinprogress;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.Globals;
import in.vakrangee.supercore.franchisee.utils.Connection;

public class NextGenWorkInProgressActivity extends AppCompatActivity {

    private static final String TAG = "NextGenWorkInProgressActivity";

    Toolbar toolbar;
    FranchiseeDetails franchiseeDetails;
    Connection connection;
    public ViewPager viewPager;
    private boolean IsEditable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_gen_web_site_view_pager);

        franchiseeDetails = new FranchiseeDetails();
        connection = new Connection(getApplicationContext());

        franchiseeDetails = (FranchiseeDetails) getIntent().getSerializableExtra("FranchiseeDetails");
        IsEditable = getIntent().getBooleanExtra("IS_EDITABLE", false);

        toolbar = (Toolbar) findViewById(R.id.toolbarimage);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.nextgen_work_in_progress);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_image);

        //tabLayout.addTab(tabLayout.newTab().setText(R.string.profile));
        //tabLayout.addTab(tabLayout.newTab().setText(R.string.vakrangeekendralocation));
        tabLayout.addTab(tabLayout.newTab().setText("WIP Interior"));
        //tabLayout.addTab(tabLayout.newTab().setText(R.string.nextgen_work_in_progress_status));
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        viewPager = (ViewPager) findViewById(R.id.pager_image);
        viewPager.setOffscreenPageLimit(1);
        final NextGenWorkInProgressViewPager adapter = new NextGenWorkInProgressViewPager(NextGenWorkInProgressActivity.this, getSupportFragmentManager(), tabLayout.getTabCount(), franchiseeDetails, IsEditable);
        viewPager.setAdapter(adapter);

        String from = getIntent().getStringExtra("FROM");
        if (!TextUtils.isEmpty(from) && from.equalsIgnoreCase("NEXT_GEN_LOCATION_DETAIL")) {
            selectFragment(0);
        } else {
            Globals sharedData = Globals.getInstance();
            int n = sharedData.getValue();
            viewPager.setCurrentItem(n, true);
        }

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

    @Override
    public void onBackPressed() {
        finish();
        viewPager.setCurrentItem(0, true);

        /*if (connection.getVkid().toUpperCase().startsWith("VL")
                || connection.getVkid().toUpperCase().startsWith("VA")) {
            super.onBackPressed();
            Intent intent = new Intent(this, MyVakrangeeKendraLocationDetailsNextGen.class);
            intent.putExtra("MODE", Constants.NEXT_GEN_WORK_IN_PROGRESS);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            Intent intent = new Intent(NextGenWorkInProgressActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            viewPager.setCurrentItem(0, true);
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
               /* if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {

                    super.onBackPressed();
                    Intent intent = new Intent(this, MyVakrangeeKendraLocationDetailsNextGen.class);
                    intent.putExtra("MODE", Constants.NEXT_GEN_WORK_IN_PROGRESS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(NextGenWorkInProgressActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }*/
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
