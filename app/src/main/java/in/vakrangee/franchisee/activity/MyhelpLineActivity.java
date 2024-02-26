package in.vakrangee.franchisee.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.mindorks.nybus.event.Channel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.adapter.AdapterRecyclerViewMyHelpLine;
import in.vakrangee.franchisee.helpline.MyHelpLineDto;
import in.vakrangee.franchisee.helpline.ServiceListRepository;
import in.vakrangee.supercore.franchisee.model.WalletData;
import in.vakrangee.supercore.franchisee.utils.AlertDialogBoxInfo;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeprecateHandler;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.InternetConnection;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.utils.network.ConnectivityChangeReceiver;
import in.vakrangee.supercore.franchisee.utils.network.EventData;
import in.vakrangee.supercore.franchisee.utils.network.NetworkHealthHandler;
import in.vakrangee.supercore.franchisee.webservice.WebService;
//

public class MyhelpLineActivity extends AppCompatActivity {

    private static final String TAG = MyhelpLineActivity.class.getCanonicalName();
    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 1;
    Toolbar toolbar;
    TelephonyManager telephonyManager;
    //String TAG = "Response";
    String diplayServerResopnse;
    InternetConnection internetConnection;
    private RecyclerView recyclerView;
    private AdapterRecyclerViewMyHelpLine mAdapter;

    private TextView emptyView,textHelpline;

    private AsyncGetHelpLineListData asyncgetMyHelpLine;
    private Logger logger;

    Context context;
    public static boolean active = false;
    private int FROM = 0;
    private DeprecateHandler deprecateHandler;
    private ConnectivityChangeReceiver receiver;
    private ServiceListRepository serviceListRepository;

    //private CustomNavigationBar customNavigationBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myhelp_line);

        toolbar = (Toolbar) findViewById(R.id.toolbarhlep);
        textHelpline = findViewById(R.id.textHelpline);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.myhelpline);

//            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        // customNavigationBar = new CustomNavigationBar(MyhelpLineActivity.this, toolbar);

        logger = Logger.getInstance(MyhelpLineActivity.this);
        deprecateHandler = new DeprecateHandler(this);
        receiver = new ConnectivityChangeReceiver();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        serviceListRepository = new ServiceListRepository(MyhelpLineActivity.this);

        //Register
        NYBus.get().register(this, Channel.TWO);

        emptyView = (TextView) findViewById(R.id.empty_view);
        Connection connection = new Connection(getApplicationContext());
        String vkid = connection.getVkid();
        internetConnection = new InternetConnection(getApplicationContext());

        //GEt Data from Intent
        FROM = getIntent().getIntExtra("From", 0);

//        progress = new ProgressDialog(MyhelpLineActivity.this);
//        progress.setTitle(R.string.accountStmt);
//        progress.setMessage(getResources().getString(R.string.pleaseWait));
//        progress.setCancelable(false);
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.show();


//        AsyncgetMyHelpLine myRequest = new AsyncgetMyHelpLine();
//        myRequest.execute();


        if (vkid.toUpperCase().startsWith("VL") || vkid.toUpperCase().startsWith("VA")) {
            emptyView.setText("Sorry. This service is available for franchisee only.");
            emptyView.setTextColor(Color.RED);
            emptyView.setVisibility(View.VISIBLE);
        } else if (internetConnection.isConnectingToInternet() == false) {

            AlertDialogBoxInfo.alertDialogShow(getApplicationContext(), getResources().getString(R.string.internetCheck));


        } else {

            asyncgetMyHelpLine = new AsyncGetHelpLineListData();
            asyncgetMyHelpLine.execute();
        }


        //   CircularImageView circularImageView = (CircularImageView) findViewById(R.id.circleImge);


//        circularImageView.setBorderColor(getResources().getColor(R.color.gray));
//        circularImageView.setBorderWidth(5);
//        circularImageView.addShadow();

//        circularImageView.setShadowRadius(15);
//        circularImageView.setShadowColor(Color.BLUE);
    }

    public void onBackPressed() {

        if (FROM == 1) {
            finish();

        } else {
            Intent intent = new Intent(MyhelpLineActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (FROM == 1) {
                    finish();

                } else {
                    // customNavigationBar.openDrawer();
                    Intent intent = new Intent(MyhelpLineActivity.this, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;
        }
        return true;
    }

    private class AsyncgetMyHelpLine extends AsyncTask<Void, Void, Void> {

        //this is the method to query

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {

                Connection connection = new Connection(getApplicationContext());
                String vkid = connection.getVkid();
                String tokenId = connection.getTokenId();


                String vkidd = EncryptionUtil.encryptString(vkid, getApplicationContext());
                String TokenId = EncryptionUtil.encryptString(tokenId, getApplicationContext());

                final String deviceIdget = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String deviceid = EncryptionUtil.encryptString(deviceIdget, getApplicationContext());

                String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
                String imei = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

                String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
                String simserialnumber = EncryptionUtil.encryptString(simSerial, getApplicationContext());

                if (!TextUtils.isEmpty(vkid)) {
                    diplayServerResopnse = WebService.getMyHelpLine1(vkid);
                } else {
                    String enquiryId = CommonUtils.getEnquiryId(MyhelpLineActivity.this);
                    diplayServerResopnse = WebService.getMyHelpLine1(enquiryId);
                }

                Log.d(TAG, "WebSer...ceResponse: " + diplayServerResopnse);

            } catch (Exception e) {
                e.printStackTrace();
                logger.writeError(TAG, "Error in helpline service: " + e.toString());
                //AlertDialogBoxInfo.alertDialogShow(MyhelpLineActivity.this, getResources().getString(R.string.Warning));
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            if (TextUtils.isEmpty(diplayServerResopnse)) {
                AlertDialogBoxInfo.alertDialogShow(MyhelpLineActivity.this, getResources().getString(R.string.Warning));
                return;
            }

            try {

                String strJson = diplayServerResopnse;
                String myModelObject1 = "";
                String myModelObject2 = "";
                List<WalletData> data = new ArrayList<>();
                try {
                    //Get the instance of JSONArray that contains JSONObjects

                    Gson gson = new GsonBuilder().create();
                    List<MyHelpLineDto> helpLineList = gson.fromJson(strJson, new TypeToken<ArrayList<MyHelpLineDto>>() {
                    }.getType());
                    JSONArray jsonarray = new JSONArray(strJson);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                        WalletData constants = new WalletData();
                        constants.TYPE = jsonobject.getString("type");
                        constants.DESFIGNATION = jsonobject.getString("designation");
                        constants.NAME = jsonobject.getString("name");
                        // constants.NAME = jsonobject.getString("imagetype");
                        constants.EMAILID = jsonobject.getString("emailId");
                        constants.MOBILENUMBER = jsonobject.getString("mobileNumber");
                        constants.IMAGE = jsonobject.getString("image");


                        data.add(constants);


                    }

                    // Setup and Handover data to recyclerview
                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view_Myhelpline);
                    emptyView = (TextView) findViewById(R.id.empty_view);
                    recyclerView.setNestedScrollingEnabled(true);

                    mAdapter = new AdapterRecyclerViewMyHelpLine(getApplicationContext(), data, new AdapterRecyclerViewMyHelpLine.IActionHandler() {
                        @Override
                        public void callNow(String mobileNo) {
                            directCall(mobileNo);
                        }

                        @Override
                        public void sendSMS(String mobileNo) {
                            composeSmsMessage("",mobileNo);
                        }

                        @Override
                        public void sendEmail(String emailId) {
                            emailSend(emailId);
                        }
                    });
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);

                    if (helpLineList == null || helpLineList.size() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Error Json", e.getMessage());
                    AlertDialogBoxInfo.alertDialogShow(MyhelpLineActivity.this, getResources().getString(R.string.Warning));

                }


            } catch (Exception e) {
                e.printStackTrace();
                AlertDialogBoxInfo.alertDialogShow(MyhelpLineActivity.this, getResources().getString(R.string.Warning));
            }

        }
    }

    private class AsyncGetHelpLineListData extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;
        String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MyhelpLineActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            Connection connection = new Connection(getApplicationContext());
            String vkid = connection.getVkid();
            String enquiryId = CommonUtils.getEnquiryId(MyhelpLineActivity.this);
            String vkIdOrEnquiryId = TextUtils.isEmpty(vkid) ? enquiryId : vkid;

            response = serviceListRepository.getMyHelplineList(vkIdOrEnquiryId);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            bindData(response);
        }
    }

    private void bindData(String response) {
        try {
            if (TextUtils.isEmpty(response)) {
                AlertDialogBoxInfo.alertDialogShow(MyhelpLineActivity.this, getResources().getString(R.string.Warning));
                return;
            }

            if (response.startsWith("OKAY|")) {
                List<WalletData> data = new ArrayList<>();
                response = response.replace("OKAY|", "");

                JSONObject jsonObject = new JSONObject(response);

                String helpLineMsg = jsonObject.optString("contact_msg");
                JSONArray jsonarray = jsonObject.optJSONArray("helpline_data");
                displayContactMsg(helpLineMsg);
                //JSONArray jsonarray = new JSONArray(response);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    WalletData constants = new WalletData();
                    constants.TYPE = jsonobject.optString("designation");
                    constants.DESFIGNATION = jsonobject.optString("designation");
                    constants.NAME = jsonobject.optString("emp_name");
                    // constants.NAME = jsonobject.getString("imagetype");
                    constants.EMAILID = jsonobject.optString("email_id");
                    constants.MOBILENUMBER = jsonobject.optString("mobile_no");
                    constants.IMAGE = jsonobject.optString("pic_id");
                    constants.Is_SMS = jsonobject.optString("is_sms");
                    data.add(constants);
                }

                // Setup and Handover data to recyclerview
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view_Myhelpline);
                emptyView = (TextView) findViewById(R.id.empty_view);
                LinearLayoutManager manager = new LinearLayoutManager(MyhelpLineActivity.this);
                manager.setOrientation(RecyclerView.VERTICAL);
                mAdapter = new AdapterRecyclerViewMyHelpLine(getApplicationContext(), data, new AdapterRecyclerViewMyHelpLine.IActionHandler() {
                    @Override
                    public void callNow(String mobileNo) {
                        directCall(mobileNo);
                    }

                    @Override
                    public void sendSMS(String mobileNo) {
                        composeSmsMessage("",mobileNo);
                    }

                    @Override
                    public void sendEmail(String emailId) {
                        emailSend(emailId);
                    }
                });
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(manager);

                if (data.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            } else if (response.startsWith("ERROR|")) {
                response = response.replace("ERROR|", "");
                AlertDialogBoxInfo.alertDialogShow(MyhelpLineActivity.this, response);
            } else {
                AlertDialogBoxInfo.alertDialogShow(MyhelpLineActivity.this, getResources().getString(R.string.Warning));

            }


        } catch (JSONException e) {
            e.printStackTrace();
            AlertDialogBoxInfo.alertDialogShow(MyhelpLineActivity.this, getResources().getString(R.string.Warning));

        }

    }

    private void displayContactMsg(String msg){
        //String strCopyRight = "For any technical query or assistance please call to Technical Support Department. Contact Number <u> <a href=\"tel:022 67765178\">022 67765178</a></u> or Email to <u><a href=\"mailto:fitsd@vakrangee.in\">fitsd@vakrangee.in</a></u>.";
        String strCopyRight = msg;
        strCopyRight = strCopyRight + "<font color=#040F01></font>";

        if (Build.VERSION.SDK_INT >= 24)
            textHelpline.setText(Html.fromHtml(strCopyRight, Html.FROM_HTML_MODE_LEGACY));
        else
            textHelpline.setText(Html.fromHtml(strCopyRight));

        textHelpline.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void directCall(String mobileNo) {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE_CALL);
        } else {
            //Open call function
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + mobileNo));
            startActivity(intent);
        }
    }

    public void composeSmsMessage(String message, String phoneNumber) {
        if (null != phoneNumber) {
            Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto: " + phoneNumber));
            i.putExtra("sms_body", message);
            if (i.resolveActivity(context.getPackageManager()) != null) {
                startActivity(i);
            } else {
                Toast.makeText(context, "SMS App not found", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void emailSend(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Vakrangee Connect Helpline");
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }

    @Override
    protected void onDestroy() {
        // Unregister
        unregisterReceiver(receiver);
        NYBus.get().unregister(this, Channel.TWO);
        super.onDestroy();
        if (asyncgetMyHelpLine != null && !asyncgetMyHelpLine.isCancelled()) {
            asyncgetMyHelpLine.cancel(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        active = true;
        registerReceiver(receiver, NetworkHealthHandler.prepareIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        active = false;
    }

    @Subscribe(channelId = Channel.TWO)
    public void onEvent(EventData event) {
        String actionMsg = getString(R.string.close_text);
        NetworkHealthHandler.displaySnackBar(findViewById(android.R.id.content), event.getData(), actionMsg, deprecateHandler);
    }
}
