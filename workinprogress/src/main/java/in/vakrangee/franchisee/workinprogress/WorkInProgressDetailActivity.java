package in.vakrangee.franchisee.workinprogress;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import in.vakrangee.supercore.franchisee.commongui.report.ReportHandlerFragment;
import in.vakrangee.supercore.franchisee.commongui.report.ReportHandlerListener;
import in.vakrangee.supercore.franchisee.model.BrandingElementDto;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.model.Globals;
import in.vakrangee.supercore.franchisee.support.WorkStatusDto;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeviceInfo;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.GPSTracker;
import in.vakrangee.supercore.franchisee.utils.JSONUtils;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.webservice.WebService;


public class WorkInProgressDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "WorkInProgressDetailActivity";
    private FranchiseeDetails franchiseeDetails, selFranchiseeDetails;
    private String FROM, MODE;
    private Connection connection;
    private Toolbar toolbar;
    private TextView NextGenFranchiseeApplicationNo, VKID, FranchiseeName;
    private LinearLayout parentLinearlytAddButton;
    private ReportHandlerFragment detailFragment;
    private Logger logger;
    private ReportDataAsync reportDataAsync = null;
    private static final String ReportJSONKey = "ReportData";
    private static final String ReportHeader = "";
    private static final String JSON_KEY_ALLOW_EDIT = "is_editable";
    private static final String JSON_KEY_NEXT_GEN_PROGRESS_ID = "nextgen_site_work_wip_status_id";
    private static final String JSON_KEY_DESIGN_ELEMENTS = "designElements";
    private static final String EDIT = "E";
    private boolean IsEditable;
    private AsyncNextGenWorkInProgressDetail asyncNextGenWorkInProgressDetail = null;
    private DeviceInfo deviceInfo;
    private ImageView profilepic;
    private TextView textviewAdd, txtAddress;
    private boolean isAdhoc = false;
    private GPSTracker gpsTracker;
    private RecyclerView recyclerViewCategory;
    private LinearLayout layoutCategoryStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_in_progress_details);

        //Initialize data
        logger = Logger.getInstance(this);
        franchiseeDetails = new FranchiseeDetails();
        connection = new Connection(getApplicationContext());
        deviceInfo = DeviceInfo.getInstance(WorkInProgressDetailActivity.this);
        gpsTracker = new GPSTracker(WorkInProgressDetailActivity.this);

        //Get Data from Intent
        franchiseeDetails = (FranchiseeDetails) getIntent().getSerializableExtra("FranchiseeDetails");
        FROM = getIntent().getStringExtra(Constants.NEXT_GEN_LOCATION_DETAIL);
        MODE = getIntent().getStringExtra(Constants.NEXT_GEN_WORK_IN_PROGRESS);

        // Get App MODE
        isAdhoc = Constants.ENABLE_ADHOC_MODE || Constants.ENABLE_FRANCHISEE_MODE;

        //Widgets
        layoutCategoryStatus = findViewById(R.id.layoutCategoryStatus);
        recyclerViewCategory = findViewById(R.id.recyclerViewCategory);
        detailFragment = (ReportHandlerFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentReport);
        parentLinearlytAddButton = findViewById(R.id.parentLinearlytAddButton);
        NextGenFranchiseeApplicationNo = (TextView) findViewById(R.id.frachicessno);
        VKID = (TextView) findViewById(R.id.vkid_name);
        FranchiseeName = (TextView) findViewById(R.id.frachicessname);
        toolbar = (Toolbar) findViewById(R.id.toolbarimage);
        profilepic = (ImageView) findViewById(R.id.profilepic);
        textviewAdd = findViewById(R.id.textviewAdd);
        txtAddress = findViewById(R.id.address);

        textviewAdd.setText(new SpannableStringBuilder(new String(new char[]{0x002B}) + "  " + getResources().getString(R.string.status)));

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            String title = getString(R.string.nextgen_work_in_progress);
            getSupportActionBar().setTitle(Html.fromHtml("<small>" + title + "</small>"));
        }

        NextGenFranchiseeApplicationNo.setText(franchiseeDetails.getNextGenFranchiseeApplicationNo() == null ? "UNKNOWN" : franchiseeDetails.getNextGenFranchiseeApplicationNo());
        VKID.setText(franchiseeDetails.getVKID());
        FranchiseeName.setText(franchiseeDetails.getFranchiseeName());
        txtAddress.setText(franchiseeDetails.getVAddress());
        if (franchiseeDetails.getFranchiseePicFile() != null) {
            Bitmap img = CommonUtils.StringToBitMap(franchiseeDetails.getFranchiseePicFile());
            profilepic.setImageBitmap(img);
        }

        //Listeners
        parentLinearlytAddButton.setOnClickListener(this);

        //Set Category list
        setCategoryAdapter();
    }

    public void setCategoryAdapter() {
        List<BrandingElementDto> brandingElementList = new ArrayList<BrandingElementDto>();

        //Prepare Branding list
        try {
            if (!TextUtils.isEmpty(franchiseeDetails.getBranding_element_details())) {

                JSONObject jsonObject = new JSONObject(franchiseeDetails.getBranding_element_details());
                JSONArray jsonArray = jsonObject.getJSONArray(JSON_KEY_DESIGN_ELEMENTS);

                Gson gson = new GsonBuilder().create();
                brandingElementList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<BrandingElementDto>>() {
                }.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Set Adapter
        ItemCategoryAdapter adapter = new ItemCategoryAdapter(this, brandingElementList, new ItemCategoryAdapter.IElementStatus() {
            @Override
            public void getSelectedStatus(WorkStatusDto workStatusDto, int pos) {
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCategory.setAdapter(adapter);
        recyclerViewCategory.setNestedScrollingEnabled(false);
    }

    @Override
    public void onBackPressed() {

        if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {
            super.onBackPressed();

            try {
                Intent myIntent = new Intent(this, Class.forName("in.vakrangee.franchisee.activity.MyVakrangeeKendraLocationDetailsNextGen"));
                myIntent.putExtra("MODE", Constants.NEXT_GEN_WORK_IN_PROGRESS);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(myIntent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
           /* Intent intent = new Intent(this, MyVakrangeeKendraLocationDetailsNextGen.class);
            intent.putExtra("MODE", Constants.NEXT_GEN_WORK_IN_PROGRESS);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);*/
        } else {
           /* Intent intent = new Intent(WorkInProgressDetailActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();*/

            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (connection.getVkid().toUpperCase().startsWith("VL") || connection.getVkid().toUpperCase().startsWith("VA")) {
                    super.onBackPressed();
                  /*  Intent intent = new Intent(this, MyVakrangeeKendraLocationDetailsNextGen.class);
                    intent.putExtra("MODE", Constants.NEXT_GEN_WORK_IN_PROGRESS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);*/

                    try {
                        Intent myIntent = new Intent(this, Class.forName("in.vakrangee.franchisee.activity.MyVakrangeeKendraLocationDetailsNextGen"));
                        myIntent.putExtra("MODE", Constants.NEXT_GEN_WORK_IN_PROGRESS);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(myIntent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    /*Intent intent = new Intent(WorkInProgressDetailActivity.this, DashboardActivity.class);
                    startActivity(intent);*/
                    finish();
                }
                break;
        }
        return true;
    }

    public void selectFragment(int i) {
        Globals sharedData = Globals.getInstance();
        sharedData.setValue(i);
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();

        if (Id == R.id.parentLinearlytAddButton) {
            jumpToWorkInProgressScreen(franchiseeDetails, true);
        }
    }

    public void jumpToWorkInProgressScreen(FranchiseeDetails franchiseeDetails, boolean IsEditable) {
        Intent intent = new Intent(this, NextGenWorkInProgressActivity.class);
        intent.putExtra("FROM", Constants.NEXT_GEN_LOCATION_DETAIL);
        intent.putExtra("MODE", Constants.NEXT_GEN_WORK_IN_PROGRESS);
        intent.putExtra("IS_EDITABLE", IsEditable);
        intent.putExtra("FranchiseeDetails", (Serializable) franchiseeDetails);
        startActivity(intent);
    }

    /**
     * Refresh data
     */
    private void refreshReportFragment(String response) {

        Bundle bundle = new Bundle();
        bundle.putString("Response", response);
        bundle.putString("ReportJSONKey", ReportJSONKey);
        bundle.putString("ReportHeader", ReportHeader);
        detailFragment.refresh(bundle);
        detailFragment.setOnRowClickListener(new ReportHandlerListener() {
            @Override
            public void onRowClick(Object data) {
                Map<String, String> rowData = (Map<String, String>) data;
                String edit = rowData.get(JSON_KEY_ALLOW_EDIT);
                IsEditable = edit.equalsIgnoreCase(EDIT) ? true : false;

                String nextgenSiteWorkInProgressId = rowData.get(JSON_KEY_NEXT_GEN_PROGRESS_ID);
                String faId = NextGenFranchiseeApplicationNo.getText().toString();

                if (!TextUtils.isEmpty(faId) && !TextUtils.isEmpty(nextgenSiteWorkInProgressId)) {
                    selFranchiseeDetails = franchiseeDetails;
                    asyncNextGenWorkInProgressDetail = new AsyncNextGenWorkInProgressDetail(WorkInProgressDetailActivity.this, faId, nextgenSiteWorkInProgressId, new AsyncNextGenWorkInProgressDetail.IGetFranchiseeDetails() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void getFranchiseeDetail(FranchiseeDetails selectedFranchiseeDetails, String errorMsg) {
                            if (TextUtils.isEmpty(errorMsg)) {
                                try {
                                    //Compress Franchisee Profile Pic
                                    if (selectedFranchiseeDetails != null && !TextUtils.isEmpty(selectedFranchiseeDetails.getFranchiseePicFile())) {

                                        String picFile = selectedFranchiseeDetails.getFranchiseePicFile();
                                        Bitmap bitmap = CommonUtils.StringToBitMap(picFile);

                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                                        String profilePic = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                                        selectedFranchiseeDetails.setFranchiseePicFile(profilePic);
                                    }

                                    selFranchiseeDetails = selectedFranchiseeDetails;
                                    jumpToWorkInProgressScreen(selFranchiseeDetails, IsEditable);

                                } catch (Exception e) {
                                    Log.e(TAG, "Fail in compressing Profile pic. \n " + e.toString());
                                }
                            } else {
                                AlertDialogBoxInfo.alertDialogShow(WorkInProgressDetailActivity.this, getResources().getString(R.string.Warning));
                            }
                        }
                    });
                    asyncNextGenWorkInProgressDetail.execute("");
                } else {
                    Toast.makeText(WorkInProgressDetailActivity.this, "No Franchisee Application No. found. ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Get Report Data
     */
    public class ReportDataAsync extends AsyncTask<String, Integer, String> {

        private boolean IsCached;
        String responseData;

        public ReportDataAsync(boolean IsCached) {
            this.IsCached = IsCached;
        }

        @Override
        protected String doInBackground(String... params) {

            connection = new Connection(WorkInProgressDetailActivity.this);
            String tempVkId = connection.getVkid();
            String Vkid = EncryptionUtil.encryptString(tempVkId, WorkInProgressDetailActivity.this);
            String TokenId = EncryptionUtil.encryptString(connection.getTokenId(), WorkInProgressDetailActivity.this);
            String fAId = EncryptionUtil.encryptString(franchiseeDetails.getNextGenFranchiseeApplicationNo(), WorkInProgressDetailActivity.this);
            String serverResponse = null;

            if (isAdhoc) {
                serverResponse = WebService.getAllNextGenSiteWorkInProgressDetail(tempVkId, franchiseeDetails.getNextGenFranchiseeApplicationNo());

            } else {
                serverResponse = WebService.getAllNextGenSiteWorkInProgressDetail(Vkid, TokenId, deviceInfo.getIMEI(), deviceInfo.getDeviceId(), deviceInfo.getSimNo(), fAId);
            }

            try {
                if (TextUtils.isEmpty(serverResponse))
                    return responseData;

                StringTokenizer tokens1 = new StringTokenizer(serverResponse, "|");
                String first1 = tokens1.nextToken();
                String second1 = tokens1.nextToken();
                System.out.println("second1: " + second1);
                String ab = second1.substring(1, second1.length() - 1);
                String strJson = ab.replace("\r\n", "");
                franchiseeDetails = JSONUtils.toJson(FranchiseeDetails.class, strJson);
            } catch (Exception e) {
                logger.writeError(TAG, "ReportHandler: Error: " + e.toString());
            }

            //responseData = "{\"ReportData\":{\"Header\":[{\"ColumnOrder\":\"0\",\"Header\":\"id\",\"HeaderValue\":\"ID\",\"IsDefaultWidth\":\"false\"},{\"ColumnOrder\":\"0\",\"Header\":\"nextgen_site_work_wip_status_id\",\"HeaderValue\":\"Wip Status Id\",\"IsDefaultWidth\":\"false\"},{\"ColumnOrder\":\"1\",\"Header\":\"visit_date_time\",\"HeaderValue\":\"Visit Date\",\"IsDefaultWidth\":\"true\"},{\"ColumnOrder\":3,\"Header\":\"work_on_track\",\"HeaderValue\":\"Status\",\"IsDefaultWidth\":\"false\"},{\"ColumnOrder\":4,\"Header\":\"revised_expected_date_of_completion\",\"HeaderValue\":\"Revised Expected Date\",\"IsDefaultWidth\":\"false\"},{\"ColumnOrder\":0,\"Header\":\"field_force_remarks\",\"HeaderValue\":\"Field Force Remarks\",\"IsDefaultWidth\":\"false\"},{\"ColumnOrder\":0,\"Header\":\"vl_remarks\",\"HeaderValue\":\"VL Remarks\",\"IsDefaultWidth\":\"false\"},{\"ColumnOrder\":\"0\",\"Header\":\"is_editable\",\"HeaderValue\":\"Is Editable\",\"IsDefaultWidth\":\"false\"},{\"ColumnOrder\":\"2\",\"Header\":\"nextgen_tis_milestone\",\"HeaderValue\":\"Status Type\",\"IsDefaultWidth\":\"false\"}],\"Result\":[{\"id\":\"1\",\"nextgen_site_work_wip_status_id\":\"81\",\"visit_date_time\":\"01-08-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"31-08-2018\",\"field_force_remarks\":\"ok\",\"vl_remarks\":\"\",\"is_editable\":\"E\",\"nextgen_tis_milestone\":\"UnScheduled\"},{\"id\":\"2\",\"nextgen_site_work_wip_status_id\":\"79\",\"visit_date_time\":\"01-08-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"ok\",\"vl_remarks\":\"\",\"is_editable\":\"N\",\"nextgen_tis_milestone\":\"UnScheduled\"}]}}";
            responseData = franchiseeDetails.getWipDetails();
            //responseData = "{\"ReportData\":{\"Header\":[{\"ColumnOrder\":\"0\",\"Header\":\"id\",\"HeaderValue\":\"ID\",\"IsDefaultWidth\":\"false\"},{\"ColumnOrder\":\"0\",\"Header\":\"nextgen_site_work_wip_status_id\",\"HeaderValue\":\"Wip Status Id\",\"IsDefaultWidth\":\"false\"},{\"ColumnOrder\":\"1\",\"Header\":\"visit_date_time\",\"HeaderValue\":\"Visit Date\",\"IsDefaultWidth\":\"true\"},{\"ColumnOrder\":2,\"Header\":\"work_on_track\",\"HeaderValue\":\"Status\",\"IsDefaultWidth\":\"true\"},{\"ColumnOrder\":3,\"Header\":\"revised_expected_date_of_completion\",\"HeaderValue\":\"Revised Expected Date\",\"IsDefaultWidth\":\"false\"},{\"ColumnOrder\":0,\"Header\":\"field_force_remarks\",\"HeaderValue\":\"Field Force Remarks\",\"IsDefaultWidth\":\"false\"},{\"ColumnOrder\":0,\"Header\":\"vl_remarks\",\"HeaderValue\":\"VL Remarks\",\"IsDefaultWidth\":\"false\"},{\"ColumnOrder\":\"0\",\"Header\":\"is_editable\",\"HeaderValue\":\"Is Editable\",\"IsDefaultWidth\":\"false\"}],\"Result\":[{\"id\":\"1\",\"nextgen_site_work_wip_status_id\":\"61\",\"visit_date_time\":\"04-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"\",\"vl_remarks\":\"\",\"is_editable\":\"E\"},{\"id\":\"2\",\"nextgen_site_work_wip_status_id\":\"60\",\"visit_date_time\":\"04-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"04-07-2018\",\"field_force_remarks\":\"test2\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"3\",\"nextgen_site_work_wip_status_id\":\"59\",\"visit_date_time\":\"04-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"okks\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"4\",\"nextgen_site_work_wip_status_id\":\"58\",\"visit_date_time\":\"04-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"ok\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"5\",\"nextgen_site_work_wip_status_id\":\"57\",\"visit_date_time\":\"04-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"ok\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"6\",\"nextgen_site_work_wip_status_id\":\"47\",\"visit_date_time\":\"04-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"ok\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"7\",\"nextgen_site_work_wip_status_id\":\"46\",\"visit_date_time\":\"03-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"03-07-2018\",\"field_force_remarks\":\"mkskjz\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"8\",\"nextgen_site_work_wip_status_id\":\"45\",\"visit_date_time\":\"03-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"03-07-2018\",\"field_force_remarks\":\"mkskjz\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"9\",\"nextgen_site_work_wip_status_id\":\"44\",\"visit_date_time\":\"03-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"03-07-2018\",\"field_force_remarks\":\"mkskjz\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"10\",\"nextgen_site_work_wip_status_id\":\"43\",\"visit_date_time\":\"03-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"mkskjz\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"11\",\"nextgen_site_work_wip_status_id\":\"42\",\"visit_date_time\":\"03-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"12\",\"nextgen_site_work_wip_status_id\":\"41\",\"visit_date_time\":\"03-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"13\",\"nextgen_site_work_wip_status_id\":\"40\",\"visit_date_time\":\"03-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"fgh\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"14\",\"nextgen_site_work_wip_status_id\":\"39\",\"visit_date_time\":\"03-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"15\",\"nextgen_site_work_wip_status_id\":\"38\",\"visit_date_time\":\"03-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"04-07-2018\",\"field_force_remarks\":\"Testing. 123\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"16\",\"nextgen_site_work_wip_status_id\":\"37\",\"visit_date_time\":\"03-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"04-07-2018\",\"field_force_remarks\":\"Testing.\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"17\",\"nextgen_site_work_wip_status_id\":\"36\",\"visit_date_time\":\"03-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"03-07-2018\",\"field_force_remarks\":\"jksk Test\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"18\",\"nextgen_site_work_wip_status_id\":\"35\",\"visit_date_time\":\"03-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"03-07-2018\",\"field_force_remarks\":\"jksk\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"19\",\"nextgen_site_work_wip_status_id\":\"33\",\"visit_date_time\":\"03-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"20\",\"nextgen_site_work_wip_status_id\":\"32\",\"visit_date_time\":\"03-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"21\",\"nextgen_site_work_wip_status_id\":\"31\",\"visit_date_time\":\"02-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"test\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"22\",\"nextgen_site_work_wip_status_id\":\"30\",\"visit_date_time\":\"02-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"test\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"23\",\"nextgen_site_work_wip_status_id\":\"29\",\"visit_date_time\":\"02-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"02-07-2018\",\"field_force_remarks\":\"fcb\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"24\",\"nextgen_site_work_wip_status_id\":\"28\",\"visit_date_time\":\"02-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"04-07-2018\",\"field_force_remarks\":\"vhjkj\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"25\",\"nextgen_site_work_wip_status_id\":\"27\",\"visit_date_time\":\"02-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"04-07-2018\",\"field_force_remarks\":\"vhjkj\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"26\",\"nextgen_site_work_wip_status_id\":\"26\",\"visit_date_time\":\"02-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"02-07-2018\",\"field_force_remarks\":\"vhjkj\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"27\",\"nextgen_site_work_wip_status_id\":\"25\",\"visit_date_time\":\"02-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"08-01-0008\",\"field_force_remarks\":\"fdxg\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"28\",\"nextgen_site_work_wip_status_id\":\"24\",\"visit_date_time\":\"02-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"08-01-0008\",\"field_force_remarks\":\"ffgg\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"29\",\"nextgen_site_work_wip_status_id\":\"23\",\"visit_date_time\":\"02-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"08-01-0008\",\"field_force_remarks\":\"gddf\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"30\",\"nextgen_site_work_wip_status_id\":\"20\",\"visit_date_time\":\"02-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"08-01-0008\",\"field_force_remarks\":\"bjkj\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"31\",\"nextgen_site_work_wip_status_id\":\"19\",\"visit_date_time\":\"02-07-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"08-01-0008\",\"field_force_remarks\":\"bjkj\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"32\",\"nextgen_site_work_wip_status_id\":\"18\",\"visit_date_time\":\"02-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"33\",\"nextgen_site_work_wip_status_id\":\"17\",\"visit_date_time\":\"02-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"34\",\"nextgen_site_work_wip_status_id\":\"13\",\"visit_date_time\":\"02-07-2018\",\"work_on_track\":\"On Track\",\"revised_expected_date_of_completion\":\"\",\"field_force_remarks\":\"\",\"vl_remarks\":\"\",\"is_editable\":\"N\"},{\"id\":\"43\",\"nextgen_site_work_wip_status_id\":\"4\",\"visit_date_time\":\"30-06-2018\",\"work_on_track\":\"Rescheduled\",\"revised_expected_date_of_completion\":\"02-07-2018\",\"field_force_remarks\":\"\",\"vl_remarks\":\"\",\"is_editable\":\"N\"}]}}";
            logger.writeError(TAG, "ReportHandler: Data: " + responseData);

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Bundle bundle = new Bundle();
            bundle.putString("ReportHeader", ReportHeader);
            detailFragment.init(bundle);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            refreshReportFragment(responseData);
            /*try {
                -//Old Before Service: responseData = "{\"PerformanceTrend\": {\"Header\": [{\"ColumnOrder\": 1,\"Header\": \"PrefPara\",\"HeaderValue\": \"Pref Para\",\"IsDefaultWidth\": false},{\"ColumnOrder\": 2,\"Header\": \"Apr\",\"HeaderValue\": \"Apr\",\"IsDefaultWidth\": false},{\"ColumnOrder\": 3,\"Header\": \"May\",\"HeaderValue\": \"May\",\"IsDefaultWidth\": false},{\"ColumnOrder\": 4,\"Header\": \"Jun\",\"HeaderValue\": \"Jun\",\"IsDefaultWidth\": false}],\"Result\": [{\"PrefPara\": \"Dr Call Average\",\"Apr\": \"40\",\"May\": \"30\",\"Jun\": \"1.9\"},{\"PrefPara\": \"Dr Coverage\",\"Apr\": \"40\",\"May\": \"30\",\"Jun\": \"1.9\"},{\"PrefPara\": \"Chem Call Average\",\"Apr\": \"40\",\"May\": \"30\",\"Jun\": \"1.9\"},{\"PrefPara\": \"Chem Coverage\",\"Apr\": \"40\",\"May\": \"30\",\"Jun\": \"1.9\"},{\"PrefPara\": \"POB\",\"Apr\": \"13\",\"May\": \"30\",\"Jun\": \"1.9\"},{\"PrefPara\": \"Sync Frequency\",\"Apr\": \"13\",\"May\": \"30\",\"Jun\": \"1.9\"},{\"PrefPara\": \"Sync Frequency\",\"Apr\": \"13\",\"May\": \"30\",\"Jun\": \"1.9\"},{\"PrefPara\": \"Sync Frequency\",\"Apr\": \"13\",\"May\": \"30\",\"Jun\": \"1.9\"},{\"PrefPara\": \"Dr Call Average\",\"Apr\": \"13\",\"May\": \"30\",\"Jun\": \"1.9\"},{\"PrefPara\": \"Dr Call Average\",\"Apr\": \"13\",\"May\": \"30\",\"Jun\": \"1.9\"}]}}";
                //After Before Service: responseData = "{\"Header\":[{\"ColumnOrder\":\"1\",\"Header\":\"Name\",\"HeaderValue\":\"PrefPara\",\"IsDefaultWidth\":\"false\"},{\"ColumnOrder\":2,\"Header\":\"Sep\",\"HeaderValue\":\"Sep\",\"IsDefaultWidth\":\"false\"},{\"ColumnOrder\":3,\"Header\":\"Oct\",\"HeaderValue\":\"Oct\",\"IsDefaultWidth\":\"false\"},{\"ColumnOrder\":4,\"Header\":\"Nov\",\"HeaderValue\":\"Nov\",\"IsDefaultWidth\":\"false\"}],\"Result\":[{\"Name\":\"DrCallAverage\",\"Sep\":12,\"Oct\":12.28,\"Nov\":10.12},{\"Name\":\"DrCoverage\",\"Sep\":12,\"Oct\":12.28,\"Nov\":10.12},{\"Name\":\"ChemCallAverage\",\"Sep\":12,\"Oct\":12.28,\"Nov\":10.12},{\"Name\":\"ChemCoverage\",\"Sep\":12,\"Oct\":12.28,\"Nov\":10.12},{\"Name\":\"SyncFrequency\",\"Sep\":12,\"Oct\":12.28,\"Nov\":10.12}]}";
                if (responseData != null) {
                    JSONObject jsonResponse = new JSONObject(responseData);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("PerformanceTrend", jsonResponse);
                    responseData = jsonObject.toString();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                logger.ERR(TAG, "PerformanceTrend: Error: " + e.toString());
            }*/
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reportDataAsync != null && !reportDataAsync.isCancelled()) {
            reportDataAsync.cancel(true);
        }

        if (asyncNextGenWorkInProgressDetail != null && !asyncNextGenWorkInProgressDetail.isCancelled()) {
            asyncNextGenWorkInProgressDetail.cancel(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Get Report Data
        reportDataAsync = new ReportDataAsync(false);
        reportDataAsync.execute("");

        //setDateTime();

    }

 /*   //TODO: Remove below code, added for testing
    private DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    public void setDateTime() {

        String dateTime = dateTimeFormat.format(gpsTracker.getDateTime());
        Log.d(TAG, "Testing: DateTime: " + dateTime + " milliseconds: " + gpsTracker.getDateTime());

        //Set Device and GPS Date Time
        String deviceDateTime = dateTimeFormat.format(System.currentTimeMillis());
        Log.d(TAG, "Testing: Device DateTime: " + deviceDateTime + " milliseconds: " + System.currentTimeMillis());

        String gpsDateTime = gpsTracker.getFormattedDateTime();
        Log.d(TAG, "Testing: GPS DateTime: " + gpsDateTime);
        //txtDeviceDateTime.setText("Device DateTime: " + deviceDateTime);
        //txtGPSDateTime.setText("GPS DateTime: " + gpsDateTime);
    }*/
}
