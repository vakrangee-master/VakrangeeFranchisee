package in.vakrangee.franchisee.franchiseelogin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.activity.DashboardActivity;
import in.vakrangee.supercore.franchisee.commongui.report.ReportHandlerListener;
import in.vakrangee.supercore.franchisee.franchiseelogin.FranchiseeLoginChecksDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class NextGenFranchiseeEnquiryDetailsActivity extends AppCompatActivity {

    private static final String TAG = "NextGenFranchiseeEnquiryDetailsActivity";
    private FranchiseeEnquiryHandlerFragment franchiseeEnquiryHandlerFragment;
    private Logger logger;
    private Toolbar toolbar;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;
    private String mobEmailID;
    private static final String ReportHeader = "";
    private TextView txtHeader;
    private MaterialButton layoutProceed;
    private FranchiseeEnquiryDto enquiryDto;
    private FranchiseeAuthenticationRepository franchiseeAuthRepo;
    private Connection connection;
    private AsyncGetVKIDByEnquiryId asyncGetVKIDByEnquiryId = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_franchisee_enquiry_details);

        //Initialize data
        logger = Logger.getInstance(this);
        connection = new Connection(this);
        franchiseeAuthRepo = new FranchiseeAuthenticationRepository(this);
        FranchiseeLoginChecksDto loginChecksDto = CommonUtils.getFranchiseeLoginDataFromPreferences(NextGenFranchiseeEnquiryDetailsActivity.this);
        if (loginChecksDto != null)
            mobEmailID = loginChecksDto.getUserName();

        //widgets
        layoutProceed = findViewById(R.id.textviewProceed);
        txtHeader = findViewById(R.id.txtHeader);
        txtHeader.setText(Html.fromHtml("<medium> Select a Enquiry to Proceed Further </medium>"));
        toolbar = (Toolbar) findViewById(R.id.toolbarAcknowledgement);
        franchiseeEnquiryHandlerFragment = (FranchiseeEnquiryHandlerFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentFranchiseeEnqiryDetails);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            String title = getString(R.string.franchisee_enquiry_details);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        layoutProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (enquiryDto == null) {
                    showMessage("Please select any 1 Enquiry to Proceed further.");
                    return;
                }

                if (!InternetConnection.isNetworkAvailable(NextGenFranchiseeEnquiryDetailsActivity.this)) {
                    showMessage("No Internet Connection.");
                    return;
                }

                FranchiseeLoginChecksDto loginChecksDto = CommonUtils.getFranchiseeLoginDataFromPreferences(NextGenFranchiseeEnquiryDetailsActivity.this);
                if (loginChecksDto != null) {
                    loginChecksDto.setNextGenEnquiryId(enquiryDto.getNextGenFranchiseeEnquiryId());
                    CommonUtils.setFranchiseeLoginDataIntoPreferences(NextGenFranchiseeEnquiryDetailsActivity.this, loginChecksDto);

                    asyncGetVKIDByEnquiryId = new AsyncGetVKIDByEnquiryId();
                    asyncGetVKIDByEnquiryId.execute();
                }
            }
        });
    }

    public class AsyncGetVKIDByEnquiryId extends AsyncTask<Void, String, Void> {

        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NextGenFranchiseeEnquiryDetailsActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            FranchiseeLoginChecksDto loginChecksDto = CommonUtils.getFranchiseeLoginDataFromPreferences(NextGenFranchiseeEnquiryDetailsActivity.this);
            if (loginChecksDto == null || loginChecksDto.getNextGenEnquiryId() == null)
                return null;

            response = franchiseeAuthRepo.getVKIDByEnquiryId(loginChecksDto.getNextGenEnquiryId());

            if (!TextUtils.isEmpty(response)) {
                StringTokenizer st1 = new StringTokenizer(response, "\\|");
                String key = st1.nextToken();

                String vkId = "";
                if (key.equalsIgnoreCase("ERROR")) {
                    vkId = "";
                } else {
                    vkId = st1.nextToken().toUpperCase();
                }

                //Insert VKID in to table
                connection.deleteTableinfo();
                connection.insertIntoDB(vkId, "", "", "");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }

            Intent intent = new Intent(NextGenFranchiseeEnquiryDetailsActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (franchiseeEnquiryHandlerFragment != null)
            refreshFranchiseeEnquiryFragment();
    }

    /**
     * Refresh data
     */
    private void refreshFranchiseeEnquiryFragment() {

        Bundle bundle = new Bundle();
        bundle.putString("MOB_EMAIL", mobEmailID);
        bundle.putString("ReportHeader", ReportHeader);
        franchiseeEnquiryHandlerFragment.refresh(bundle);
        franchiseeEnquiryHandlerFragment.setOnRowClickListener(new ReportHandlerListener() {
            @Override
            public void onRowClick(Object data) {
                enquiryDto = (FranchiseeEnquiryDto) data;
            }
        });
    }

    public void showMessage(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(this);

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

    public void backPressed() {
        showYesNoMessage("Are you sure you want to exit?");

    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backPressed();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (asyncGetVKIDByEnquiryId != null && !asyncGetVKIDByEnquiryId.isCancelled()) {
            asyncGetVKIDByEnquiryId.cancel(true);
        }
    }

    public void showYesNoMessage(String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        if (alert == null) {
            alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            alert = null;

                            //Exit from App
                            CommonUtils.clearFranchiseeLoginDataFromPreferences(NextGenFranchiseeEnquiryDetailsActivity.this);
                            Intent i = new Intent(NextGenFranchiseeEnquiryDetailsActivity.this, FranchiseeLoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            alert = null;
                        }
                    });

            alert = alertDialogBuilder.create();
            alert.show();
        }
    }

}
