package in.vakrangee.franchisee.hardwareacknowledgement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;

public class NextGenVakrangeeKendraAcknowledgementActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    Connection connection;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;
    private Context context;

    private KendraAcknowledgementFragment fragmentKendraAcknowledgement;
    private boolean IsServiceCall = false;
    private String materialName = null;
    private String statusCode = null;
    private String materialCode = null;
    private String euipAckId = "0";
    private String kendraEuipId = "0";
    private String standEquiId = "0";
    private String assetsTracking = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vakrangee_kendra_acknowledgement);

        this.context = this;
        connection = new Connection(getApplicationContext());

        //Get Data from Intent
        IsServiceCall = getIntent().getBooleanExtra("IsServiceCall", false);
        materialName = getIntent().getStringExtra("MATERIAL_NAME");
        materialCode = getIntent().getStringExtra("MATERIAL_CODE");
        statusCode = getIntent().getStringExtra("STATUS_CODE");
        euipAckId = getIntent().getStringExtra("EQUIP_ACK_ID");
        kendraEuipId = getIntent().getStringExtra("KENDRA_EQUIP_ID");
        standEquiId = getIntent().getStringExtra("STANDARD_EQUIP_ID");
        assetsTracking = getIntent().getStringExtra("ASSETS_TRACKING");

        toolbar = (Toolbar) findViewById(R.id.toolbarFranchiseeApplication);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            String title = getString(R.string.next_gen_kendra_acknowledgement);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        fragmentKendraAcknowledgement = (KendraAcknowledgementFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentKendraAcknowledgement);
        fragmentKendraAcknowledgement.reloadData(null, statusCode, materialCode, materialName, IsServiceCall, euipAckId, kendraEuipId, standEquiId, assetsTracking);
    }

    public void showMessage(String msg) {
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
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backPressed();
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
       //Do Nothing
    }

}
