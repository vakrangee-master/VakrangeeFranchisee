package in.vakrangee.franchisee.sitelayout.sitecompletion;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.activity.MyVakrangeeKendraLocationDetailsNextGen;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.Globals;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;

public class WorkCompletionIntimationActivity extends AppCompatActivity {

    Toolbar toolbar;
    FranchiseeDetails franchiseeDetails;
    Connection connection;
    public ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_gen_web_site_view_pager);

        franchiseeDetails = new FranchiseeDetails();
        connection = new Connection(getApplicationContext());

        franchiseeDetails = (FranchiseeDetails) getIntent().getSerializableExtra("FranchiseeDetails");
        toolbar = (Toolbar) findViewById(R.id.toolbarimage);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Work Completion Intimation");
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_image);

        //tabLayout.addTab(tabLayout.newTab().setText(R.string.profile));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.titleSiteWorkCompletion));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager_image);
        final WorkCompletionViewPager adapter = new WorkCompletionViewPager(WorkCompletionIntimationActivity.this, getSupportFragmentManager(), tabLayout.getTabCount(), franchiseeDetails);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

        String from = getIntent().getStringExtra("FROM");
        if (!TextUtils.isEmpty(from) && from.equalsIgnoreCase("NEXT_GEN_LOCATION_DETAIL")) {
            selectFragment(0);
        } else {
            Globals sharedData = Globals.getInstance();
            int n = sharedData.getValue();
            viewPager.setCurrentItem(n, true);
        }

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        if (connection.getVkid().toUpperCase().startsWith("VL")
                || connection.getVkid().toUpperCase().startsWith("VA")) {
            super.onBackPressed();
            Intent intent = new Intent(this, MyVakrangeeKendraLocationDetailsNextGen.class);
            intent.putExtra("MODE", Constants.NEXTGEN_WORK_COMPLETION_INTIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
           /* Intent intent = new Intent(WorkCompletionIntimationActivity.this, DashboardActivity.class);
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
                    intent.putExtra("MODE", Constants.NEXTGEN_WORK_COMPLETION_INTIMATION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                 /*   Intent intent = new Intent(WorkCompletionIntimationActivity.this, DashboardActivity.class);
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
}
