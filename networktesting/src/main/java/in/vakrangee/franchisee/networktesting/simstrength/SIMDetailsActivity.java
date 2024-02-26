package in.vakrangee.franchisee.networktesting.simstrength;

import android.graphics.Color;
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
import in.vakrangee.franchisee.networktesting.R;

public class SIMDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    private SIMDetailsDto simDetailsDto;
    private TextView txtPhoneNumber, txtNetwork, txtMobileNetworkState, txtOperatorInfo, txtServiceStatus, txtSignalStrength,
            txtMobileVoiceNetworkType, txtMobileDataNetworkType, txtRoaming;
    private TextView txtNoDataMsg;
    private LinearLayout layoutSIMDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_details);

        //Initialize
        toolbar = (Toolbar) findViewById(R.id.toolbarNetworkTesting);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        txtNoDataMsg = findViewById(R.id.txtNoDataMsg);
        layoutSIMDetails = findViewById(R.id.layoutSIMDetails);

        simDetailsDto = (SIMDetailsDto) getIntent().getSerializableExtra("SIM_DETAILS_DTO");
        if (getSupportActionBar() != null) {
            String title = simDetailsDto.getSimSlotName();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        //Widgets
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtNetwork = findViewById(R.id.txtNetwork);
        txtMobileNetworkState = findViewById(R.id.txtMobileNetworkState);
        txtOperatorInfo = findViewById(R.id.txtOperatorInfo);
        txtServiceStatus = findViewById(R.id.txtServiceStatus);
        txtSignalStrength = findViewById(R.id.txtSignalStrength);
        txtMobileVoiceNetworkType = findViewById(R.id.txtMobileVoiceNetworkType);
        txtMobileDataNetworkType = findViewById(R.id.txtMobileDataNetworkType);
        txtRoaming = findViewById(R.id.txtRoaming);

        reloadData();
    }

    private void reloadData() {

        if (TextUtils.isEmpty(simDetailsDto.getOperatorInfo())) {
            txtNoDataMsg.setVisibility(View.VISIBLE);
            layoutSIMDetails.setVisibility(View.GONE);
        } else {
            txtNoDataMsg.setVisibility(View.GONE);
            layoutSIMDetails.setVisibility(View.VISIBLE);
        }

        String number = TextUtils.isEmpty(simDetailsDto.getLineNumber()) ? "N/A" : simDetailsDto.getLineNumber();
        txtPhoneNumber.setText(number);

        String network = TextUtils.isEmpty(simDetailsDto.getNetwork()) ? "N/A" : simDetailsDto.getNetwork();
        txtNetwork.setText(network);

        String networkState = TextUtils.isEmpty(simDetailsDto.getMobNetworkState()) ? "N/A" : simDetailsDto.getMobNetworkState();
        txtMobileNetworkState.setText(networkState);

        String operatorInfo = TextUtils.isEmpty(simDetailsDto.getOperatorInfo()) ? "N/A" : simDetailsDto.getOperatorInfo();
        txtOperatorInfo.setText(operatorInfo);

        String serviceState = TextUtils.isEmpty(simDetailsDto.getServiceState()) ? "N/A" : simDetailsDto.getServiceState();
        txtServiceStatus.setText(serviceState);

        String signalStrength = TextUtils.isEmpty(simDetailsDto.getSignalStrength()) ? "N/A" : simDetailsDto.getSignalStrength();
        txtSignalStrength.setText(signalStrength);

        String networkType = TextUtils.isEmpty(simDetailsDto.getMobVoiceNetworkType()) ? "N/A" : simDetailsDto.getMobVoiceNetworkType();
        txtMobileVoiceNetworkType.setText(networkType);

        String networkDataType = TextUtils.isEmpty(simDetailsDto.getMobDataNetworkType()) ? "N/A" : simDetailsDto.getMobDataNetworkType();
        txtMobileDataNetworkType.setText(networkDataType);

        String roaming = TextUtils.isEmpty(simDetailsDto.getRoaming()) ? "N/A" : simDetailsDto.getRoaming();
        txtRoaming.setText(roaming);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        int itemId = item.getItemId();
        if (itemId == android.R.id.home || itemId == R.id.action_home_dashborad) {
            backPressed();
        }
        return true;
    }

}
