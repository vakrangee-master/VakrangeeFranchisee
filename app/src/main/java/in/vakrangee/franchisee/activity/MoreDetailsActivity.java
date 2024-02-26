package in.vakrangee.franchisee.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.adapter.CustomExpandableListAdapter;
import in.vakrangee.franchisee.fragment.ExpandableListDataSource;
import in.vakrangee.franchisee.fragment.FragmentNavigationManager;
import in.vakrangee.supercore.franchisee.ifc.MoreDetailsNavigationManagerIfc;

public class MoreDetailsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private String[] items;
    Toolbar toolbar;
    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mExpandableListAdapter;
    private List<String> mExpandableListTitle;
    private MoreDetailsNavigationManagerIfc mNavigationManager;

    private Map<String, List<String>> mExpandableListData;
    int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);


        toolbar = (Toolbar) findViewById(R.id.toolbarmore);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.monitoring_dashboard);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        mExpandableListView = (ExpandableListView) findViewById(R.id.navList);
        mNavigationManager = FragmentNavigationManager.obtain(this);

        initItems();

        LayoutInflater inflater = getLayoutInflater();
        //View listHeaderView = inflater.inflate(R.layout.nav_header, null, false);
        // mExpandableListView.addHeaderView(listHeaderView);

        mExpandableListData = ExpandableListDataSource.getData(this);
        mExpandableListTitle = new ArrayList(mExpandableListData.keySet());

        addDrawerItems();
        setupDrawer();

        if (savedInstanceState == null) {
            selectFirstItemAsDefault();
        }


    }


    private void initItems() {
        items = getResources().getStringArray(R.array.all_atm_list);
    }

    private void addDrawerItems() {
        mExpandableListAdapter = new CustomExpandableListAdapter(this, mExpandableListTitle, mExpandableListData);
        mExpandableListView.setAdapter(mExpandableListAdapter);
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                getSupportActionBar().setTitle(mExpandableListTitle.get(groupPosition).toString());
            }
        });

        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                getSupportActionBar().setTitle("film_genres");
            }
        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String selectedItem = ((List) (mExpandableListData.get(mExpandableListTitle.get(groupPosition))))
                        .get(childPosition).toString();
                getSupportActionBar().setTitle(selectedItem);

                try {
                    if (items[0].equals(mExpandableListTitle.get(groupPosition))) {
                        mNavigationManager.showFragmentAction(selectedItem);
                    } else if (items[1].equals(mExpandableListTitle.get(groupPosition))) {
                        mNavigationManager.showFragmentComedy(selectedItem);
                    } else if (items[2].equals(mExpandableListTitle.get(groupPosition))) {
                        mNavigationManager.showFragmentDrama(selectedItem);
                    } else if (items[3].equals(mExpandableListTitle.get(groupPosition))) {
                        mNavigationManager.showFragmentMusical(selectedItem);
                    } else if (items[4].equals(mExpandableListTitle.get(groupPosition))) {
                        mNavigationManager.showFragmentThriller(selectedItem);
                    } else if (items[5].equals(mExpandableListTitle.get(groupPosition))) {
                        mNavigationManager.showFragmentThriller(selectedItem);
                    } else if (items[6].equals(mExpandableListTitle.get(groupPosition))) {
                        mNavigationManager.showFragmentThriller(selectedItem);
                    } else if (items[7].equals(mExpandableListTitle.get(groupPosition))) {
                        mNavigationManager.showFragmentThriller(selectedItem);
                    } else {
                        throw new IllegalArgumentException("Not supported fragment type");
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }


    private void selectFirstItemAsDefault() {
        if (mNavigationManager != null) {
            String firstActionMovie = getResources().getStringArray(R.array.all_atm_list)[0];
            mNavigationManager.showFragmentAction(firstActionMovie);
            getSupportActionBar().setTitle(firstActionMovie);
        }
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.film_genres);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_home_dashborad:
                Intent i = new Intent(MoreDetailsActivity.this, DashboardActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }


    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            Intent i = new Intent(MoreDetailsActivity.this, Monitoring_Dashboard.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(i);
            finish();
        }
    }
}
