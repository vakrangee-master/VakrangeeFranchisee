package in.vakrangee.franchisee.atmloading;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import butterknife.ButterKnife;
import in.vakrangee.supercore.franchisee.nextgenfranchiseeapplication.CustomFranchiseeApplicationSpinnerDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;

public class ATMRoCashLoadingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Toolbar toolbar;
    private Button btnSubmit;
    private Spinner spinnerATMID;
    private Spinner spinnerStatus;

    private Context context;
    private List<CustomFranchiseeApplicationSpinnerDto> atmIdList;
    private List<CustomFranchiseeApplicationSpinnerDto> statusList;
    private ATMLoadingRepository atmLoadingRepository;
    private Connection connection;
    private AsyntaskSpinnerData asyntaskSpinnerData;
    private String selectATMID, selectStatusID;
    private ATMRoCashLoadingFragment atmRoCashLoadingFragment;
    private AsyncGetATMRoCashLoadingDetails asyncGetATMRoCashLoadingDetails;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmro_cash_loading);
        this.context = ATMRoCashLoadingActivity.this;
        //Initialize data
        bindViewId();
        ButterKnife.bind(this);
        atmLoadingRepository = new ATMLoadingRepository(context);
        connection = new Connection(context);

        //setup toolbar
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            String title = getResources().getString(R.string.atm_loading);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (TextUtils.isEmpty(selectATMID)) {
                        Toast.makeText(context, "please select ATM ID ", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(selectStatusID)) {
                        Toast.makeText(context, "please select Status ID ", Toast.LENGTH_SHORT).show();
                    } else {
                        callGetRoCashLoading(selectATMID, selectStatusID);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        //call asyntask spinner data
        asyntaskSpinnerData = new AsyntaskSpinnerData();
        asyntaskSpinnerData.execute();

        //call fragment
        atmRoCashLoadingFragment = (ATMRoCashLoadingFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentATMROCashLoading);
    }

    private void bindViewId() {
        toolbar = findViewById(R.id.toolbar);
        btnSubmit = findViewById(R.id.btnSubmit);
        spinnerATMID = findViewById(R.id.spinnerATMID);
        spinnerStatus = findViewById(R.id.spinnerStatus);
    }

    private void callGetRoCashLoading(String selectATMID, final String selectStatusID) {
        asyncGetATMRoCashLoadingDetails = new AsyncGetATMRoCashLoadingDetails(context, selectATMID, selectStatusID, new AsyncGetATMRoCashLoadingDetails.Callback() {
            @Override
            public void onResult(String result) {
                atmRoCashLoadingFragment.reloadData(result, selectStatusID);
            }

        });
        asyncGetATMRoCashLoadingDetails.execute();
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    public void backPressed() {
      /*  Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();*/

        finish();

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        int Id = adapterView.getId();
        if (Id == R.id.spinnerATMID) {
            if (position > 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerATMID.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    selectATMID = entityDto.getId();
                    //deliveryAddressDto.setComState(entityDto.getId());
                }
            } else {
                selectATMID = null;
            }
        }
        if (Id == R.id.spinnerStatus) {
            if (position > 0) {
                CustomFranchiseeApplicationSpinnerDto entityDto = (CustomFranchiseeApplicationSpinnerDto) spinnerStatus.getItemAtPosition(position);
                if (!entityDto.getId().equals("0")) {
                    selectStatusID = entityDto.getId();
                    //deliveryAddressDto.setComState(entityDto.getId());
                }
            } else {
                selectStatusID = null;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    //region call atmid and status
    private class AsyntaskSpinnerData extends AsyncTask<String, Void, String> {

        String tmpVkId = connection.getVkid();
        String enquiryId = CommonUtils.getEnquiryId(context);
        String vkIdOrEnquiryId = TextUtils.isEmpty(tmpVkId) ? enquiryId : tmpVkId;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(ATMRoCashLoadingActivity.this);
            progress.setTitle(R.string.pleaseWait);
            progress.setMessage(getResources().getString(R.string.pleaseWait));
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            //STEP 1: State List
            atmIdList = atmLoadingRepository.getATMIDList(vkIdOrEnquiryId);
            statusList = atmLoadingRepository.getStatusList(vkIdOrEnquiryId);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.dismiss();
            bindSpinner();

        }
    }

    private void bindSpinner() {
        CommonUtils.spinner_focusablemode(spinnerATMID);
        CommonUtils.spinner_focusablemode(spinnerStatus);

        spinnerATMID.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, atmIdList));
        int posATM = atmLoadingRepository.getSelectedPos(atmIdList, "0");
        spinnerATMID.setSelection(posATM);
        spinnerATMID.setOnItemSelectedListener(this);

        spinnerStatus.setAdapter(new ArrayAdapter<CustomFranchiseeApplicationSpinnerDto>(context, android.R.layout.simple_spinner_dropdown_item, statusList));
        int posStatus = atmLoadingRepository.getSelectedPos(statusList, "0");
        spinnerStatus.setSelection(posStatus);
        spinnerStatus.setOnItemSelectedListener(this);
    }
    //endregion


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (asyntaskSpinnerData != null && !asyntaskSpinnerData.isCancelled()) {
            asyntaskSpinnerData.cancel(true);
        }
        if (asyncGetATMRoCashLoadingDetails != null && !asyncGetATMRoCashLoadingDetails.isCancelled()) {
            asyncGetATMRoCashLoadingDetails.cancel(true);
        }

    }
}
