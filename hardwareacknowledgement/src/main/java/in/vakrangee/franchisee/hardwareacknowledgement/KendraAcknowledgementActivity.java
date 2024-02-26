package in.vakrangee.franchisee.hardwareacknowledgement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import in.vakrangee.supercore.franchisee.commongui.report.ReportHandlerListener;
import in.vakrangee.supercore.franchisee.commongui.status.NameStatusHandlerFragment;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.ImageUtils;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class KendraAcknowledgementActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "KendraAcknowledgementActivity";
    private Connection connection;
    private Toolbar toolbar;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alert;
    private Logger logger;
    private ReportDataAsync reportDataAsync = null;
    private static final String JSON_KEY_ALLOW_EDIT = "is_editable";
    private static final String JSON_KEY_ALLOW_SERVICE_CALL = "is_service_call";
    private static final String JSON_KEY_NEXTGEN_EQUIP_ACK_ID = "nextgen_vakrangee_kendra_equipments_acknowledgement_id";
    private static final String JSON_KEY_NEXTGEN_KENDRA_EQUIP_ID = "nextgen_vakrangee_kendra_equipments_id";
    private static final String JSON_KEY_NEXTGEN_STANDARD_EQUIP_ID = "nextgen_standard_equipment_id";
    private static final String EDIT = "E";
    private boolean IsEditable;

    private static final String REPORT_JSON_KEY = "ReportData";
    private static final String REPORT_HEADER = "";
    private NameStatusHandlerFragment fragmentAcknowledgementDetails;
    private ImageView profilepic;
    private TextView txtAddress;
    private TextView NextGenFranchiseeApplicationNo, VKID, FranchiseeName;
    private KendraAcknowledgementRepository kendraAckRepo;
    private MyEquipmentAckDto myEquipmentAckDto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kendra_acknowledgement);

        //Initialize data
        logger = Logger.getInstance(this);
        connection = new Connection(getApplicationContext());
        kendraAckRepo = new KendraAcknowledgementRepository(this);

        //Widgets
        NextGenFranchiseeApplicationNo = (TextView) findViewById(R.id.frachicessno);
        VKID = (TextView) findViewById(R.id.vkid_name);
        FranchiseeName = (TextView) findViewById(R.id.frachicessname);
        profilepic = (ImageView) findViewById(R.id.profilepic);
        txtAddress = findViewById(R.id.address);
        toolbar = (Toolbar) findViewById(R.id.toolbarAcknowledgement);
        fragmentAcknowledgementDetails = (NameStatusHandlerFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentAcknowledgementDetails);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            String title = getString(R.string.my_equipment_acknowledgement);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }


    }

    /**
     * Get Report Data
     */
    public class ReportDataAsync extends AsyncTask<String, Integer, String> {

        private boolean isCached;
        String responseData;
        String msg;

        public ReportDataAsync(boolean IsCached) {
            this.isCached = IsCached;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Bundle bundle = new Bundle();
            bundle.putString("ReportHeader", REPORT_HEADER);
            fragmentAcknowledgementDetails.init(bundle);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String vkId = connection.getVkid();
                String serverResponse = kendraAckRepo.getGetKendraAllEquipmentAckData(vkId);

                if (TextUtils.isEmpty(serverResponse))
                    return serverResponse;

                Gson gson = new GsonBuilder().create();
                List<MyEquipmentAckDto> list = gson.fromJson(serverResponse, new TypeToken<ArrayList<MyEquipmentAckDto>>() {
                }.getType());
                if (list.size() > 0)
                    myEquipmentAckDto = list.get(0);

                responseData = myEquipmentAckDto.getAckDetails();

                logger.writeError(TAG, "ReportHandler: isCached: " + isCached + " Data: " + responseData);

            } catch (Exception e) {
                e.printStackTrace();
                logger.writeError(TAG, "ReportHandler: Error: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            refreshReportFragment(responseData);

            //Set Franchisee Information
            if (myEquipmentAckDto != null) {
                NextGenFranchiseeApplicationNo.setText(myEquipmentAckDto.getApplicationNo() == null ? "UNKNOWN" : myEquipmentAckDto.getApplicationNo());
                VKID.setText(myEquipmentAckDto.getVKID());
                FranchiseeName.setText(myEquipmentAckDto.getFranchiseeName());
                txtAddress.setText(myEquipmentAckDto.getAddress());
                if (myEquipmentAckDto.getFranchiseePicBase64() != null) {
                    Bitmap bitmap = CommonUtils.StringToBitMap(myEquipmentAckDto.getFranchiseePicBase64());
                    bitmap = ImageUtils.getResizedBitmap(bitmap);
                    profilepic.setImageBitmap(bitmap);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reportDataAsync != null && !reportDataAsync.isCancelled()) {
            reportDataAsync.cancel(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Get Report Data
        reportDataAsync = new ReportDataAsync(false);
        reportDataAsync.execute("");

    }

    /**
     * Refresh data
     */
    private void refreshReportFragment(String response) {

        Bundle bundle = new Bundle();
        bundle.putString("Response", response);
        bundle.putString("ReportJSONKey", REPORT_JSON_KEY);
        bundle.putString("ReportHeader", REPORT_HEADER);
        fragmentAcknowledgementDetails.refresh(bundle);
        fragmentAcknowledgementDetails.setOnRowClickListener(new ReportHandlerListener() {
            @Override
            public void onRowClick(Object data) {
                Map<String, String> rowData = (Map<String, String>) data;
                String edit = rowData.get(JSON_KEY_ALLOW_EDIT);
                IsEditable = edit.equalsIgnoreCase(EDIT) ? true : false;

                String serviceCall = rowData.get(JSON_KEY_ALLOW_SERVICE_CALL);
                boolean IsServiceCall = serviceCall.equalsIgnoreCase("1") ? true : false;
                String materialName = rowData.get("material_name");
                String statusDesc = rowData.get("status_desc");
                String statusCode = rowData.get("status");
                String materialCode = rowData.get("material_code");
                String assetsTracking = rowData.get("assets_tracking");

                String euipAckId = rowData.get(JSON_KEY_NEXTGEN_EQUIP_ACK_ID);
                String kendraEuipId = rowData.get(JSON_KEY_NEXTGEN_KENDRA_EQUIP_ID);
                String standEquiId = rowData.get(JSON_KEY_NEXTGEN_STANDARD_EQUIP_ID);

                if (IsEditable) {
                    Intent intent = new Intent(KendraAcknowledgementActivity.this, NextGenVakrangeeKendraAcknowledgementActivity.class);
                    intent.putExtra("STATUS_CODE", statusCode);
                    intent.putExtra("MATERIAL_NAME", materialName);
                    intent.putExtra("MATERIAL_CODE", materialCode);
                    intent.putExtra("IsServiceCall", IsServiceCall);
                    intent.putExtra("EQUIP_ACK_ID", euipAckId);
                    intent.putExtra("KENDRA_EQUIP_ID", kendraEuipId);
                    intent.putExtra("STANDARD_EQUIP_ID", standEquiId);
                    intent.putExtra("ASSETS_TRACKING", assetsTracking);
                    startActivity(intent);
                } else {
                    String msg = "You will not be able to acknowledge kendra details, as your status is " + statusDesc;
                    showMessage(msg);
                }
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
        if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {
            super.onBackPressed();

            try {
                Intent myIntent = new Intent(this, Class.forName("in.vakrangee.franchisee.activity.MyVakrangeeKendraLocationDetailsNextGen"));
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(myIntent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            finish();
        }
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

            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        //Do Nothing
    }
}
