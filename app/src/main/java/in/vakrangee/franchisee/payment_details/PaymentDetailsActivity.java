package in.vakrangee.franchisee.payment_details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;

public class PaymentDetailsActivity extends AppCompatActivity {

    private Toolbar toolbarPaymentDetails;

    private PaymentDetailsFragment paymentDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        //set toolbar name
        toolbarPaymentDetails = findViewById(R.id.toolbarPaymentDetails);
        setSupportActionBar(toolbarPaymentDetails);
        if (getSupportActionBar() != null) {
            toolbarPaymentDetails.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + getResources().getString(R.string.payment_details) + "</small>"));

        }

        paymentDetailsFragment = (PaymentDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragPaymentDetails);
        paymentDetailsFragment.reloadData();

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
        switch (item.getItemId()) {
            case android.R.id.home:
                backPressed();
                break;
            case R.id.action_home_dashborad:
                Intent intent = new Intent(PaymentDetailsActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }
        return true;
    }

}
