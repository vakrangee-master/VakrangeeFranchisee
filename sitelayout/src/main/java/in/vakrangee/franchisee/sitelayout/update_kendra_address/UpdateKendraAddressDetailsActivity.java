package in.vakrangee.franchisee.sitelayout.update_kendra_address;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.vakrangee.franchisee.sitelayout.R;

public class UpdateKendraAddressDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private UpdateKendraAddressFragment fragUpdateAddress;
    private UpdateAddressDetailsDto addressDetailsDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);

        //set toolbar name
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + getResources().getString(R.string.lbl_update_address) + "</small>"));

        }

        addressDetailsDto = (UpdateAddressDetailsDto) getIntent().getSerializableExtra("ADDRESS_DETAILS");

        fragUpdateAddress = (UpdateKendraAddressFragment) getSupportFragmentManager().findFragmentById(R.id.fragUpdateAddress);
        fragUpdateAddress.reloadData(addressDetailsDto);
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
