package in.vakrangee.franchisee.atmtechlivechecklist;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ATMTechLiveCheckListActivity extends AppCompatActivity {

    private Toolbar toolbarATMTechLive;
    private ATMTechLiveCheckListFragment fragATMTechLiveDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm_techlive_checklist);

        //set toolbar name
        toolbarATMTechLive = findViewById(R.id.toolbarATMTechLive);
        setSupportActionBar(toolbarATMTechLive);
        if (getSupportActionBar() != null) {
            toolbarATMTechLive.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + getResources().getString(R.string.atm_checklist) + "</small>"));

        }

        fragATMTechLiveDetails = (ATMTechLiveCheckListFragment) getSupportFragmentManager().findFragmentById(R.id.fragATMTechLiveDetails);
        fragATMTechLiveDetails.reloadData();

    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    public void backPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_serach, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search_main);
        searchItem.setVisible(false);
        MenuItem home = menu.findItem(R.id.action_home_dashborad);
        home.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            backPressed();
        }
        return true;
    }
}
