package in.vakrangee.franchisee.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.StringTokenizer;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.service.OnClearFromRecentService;
import in.vakrangee.franchisee.sitelayout.NextGenPhotoViewPager;
import in.vakrangee.franchisee.support.SupportTicketActivity;
import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.JSONUtils;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class AppNotificationActivity extends AppCompatActivity {

    private static final String TAG = AppNotificationActivity.class.getCanonicalName();
    private Context mContext;
    private Logger logger;
    private Intent intent;
    private Connection connection;

    private final static int REQUEST_CODE = 1000;
    private final static int SUPPORT_REQUEST_CODE = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);

        logger = Logger.getInstance(mContext);
        intent = getIntent();
        if (intent == null) {
            logger.writeError(TAG, "Intent can not be null.");
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            String activity = intent.getStringExtra(Constants.INTENT_KEY_FROM_ACTIVITY);
            if (activity != null && activity.equalsIgnoreCase(Constants.INTENT_VALUE_SUPPORT_TICKET)) {

                Intent intent = new Intent(AppNotificationActivity.this, LoginActivity.class);
                intent.putExtra("FROM", "APP_NOTIFICATION");
                startActivityForResult(intent, SUPPORT_REQUEST_CODE);

            } else {

                // Start Service To Clean Token if app is close via swipe.
                startService(new Intent(getBaseContext(), OnClearFromRecentService.class));

                connection = new Connection(mContext);
                connection.openDatabase();
                String action = intent.getStringExtra("action");
                String data = intent.getStringExtra("data");

                try {
                    JSONObject jsonObjPayload = new JSONObject(data);
                    if (jsonObjPayload.has("application_no")) {
                        String fappNo = jsonObjPayload.getString("application_no");
                        new AsyncNextGenFranchiseeDetails(AppNotificationActivity.this, fappNo).execute("");
                    } else {
                        Toast.makeText(AppNotificationActivity.this, getResources().getString(R.string.Warning), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //region Search Frenchisee Based on Application Number
    public class AsyncNextGenFranchiseeDetails extends AsyncTask<String, Void, FranchiseeDetails> {

        private Context mContext;
        private TelephonyManager telephonyManager;
        String tempFAId, errorMsg;

        public AsyncNextGenFranchiseeDetails(Context context, String faId) {
            super();
            this.mContext = context;
            telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            this.tempFAId = faId;
        }

        @Override
        protected FranchiseeDetails doInBackground(String... values) {
            // If you want to use 'values' string in here
            Log.i(TAG, "doInBackground");
            try {
                // Preparing Data
                String strTokenId = connection.getTokenId();
                if (TextUtils.isEmpty(strTokenId)) {
                    Log.e(TAG, "User is Logout. Need to login again.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(AppNotificationActivity.this, getResources().getString(R.string.Warning), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AppNotificationActivity.this, LoginActivity.class);
                            intent.putExtra("FROM", "APP_NOTIFICATION");
                            intent.putExtra("NEXTGEN_SITE_VISIT_APPLICATION_NO", tempFAId);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                    });
                    return null;
                } else {

                    String vkId = EncryptionUtil.encryptString(connection.getVkid(), mContext);
                    String tokenId = EncryptionUtil.encryptString(strTokenId, mContext);

                    String deviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    String deviceId = EncryptionUtil.encryptString(deviceid, getApplicationContext());

                    String deviceIDAndroid = CommonUtils.getAndroidUniqueID(getApplicationContext());
                    String imeiNo = EncryptionUtil.encryptString(deviceIDAndroid, getApplicationContext());

                    String simSerial = CommonUtils.getSimSerialNumber(getApplicationContext());
                    String simNo = EncryptionUtil.encryptString(simSerial, getApplicationContext());

                    String nextgenFranchiseeApplicationId = EncryptionUtil.encryptString(tempFAId, mContext);
                    String displayServerResopnse = WebService.myVakrangeeKendraFranchiseeDetailsNextgen(vkId, nextgenFranchiseeApplicationId, tokenId, imeiNo, deviceId, simNo);

                    if (displayServerResopnse.startsWith("ERROR|")) {

                        StringTokenizer tokens = new StringTokenizer(displayServerResopnse, "|");
                        tokens.nextToken();     // Jump to next Token
                        errorMsg = tokens.nextToken();

                        return null;
                    }

                    StringTokenizer tokens1 = new StringTokenizer(displayServerResopnse, "|");
                    tokens1.nextToken();    // Jump to next Token
                    String franchiseeData = tokens1.nextToken();

                    Log.d(TAG, franchiseeData);
                    String ab = franchiseeData.substring(1, franchiseeData.length() - 1);
                    String franchiseeJsonData = ab.replace("\r\n", "");

                    return JSONUtils.toJson(FranchiseeDetails.class, franchiseeJsonData); // returning FranchiseeDetails Object
                }
            } catch (Exception e) {
                Log.e("TAG", "Error:in Search Vakrangee Kendra " + e.getMessage());

            }
            return null;
        }

        @Override
        protected void onPostExecute(FranchiseeDetails franchiseeDetails) {

            if (franchiseeDetails != null) {

                Toast.makeText(mContext, "Franchisee Detail Found.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AppNotificationActivity.this, NextGenPhotoViewPager.class);
                intent.putExtra("FROM", "NEXT_GEN_LOCATION_DETAIL");
                intent.putExtra("FranchiseeDetails", (Serializable) franchiseeDetails);
                startActivity(intent);
                finish();
            }
        }

    }
    //endregion

    //region Activity Result

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
                String fappNo = data.getStringExtra("NEXTGEN_SITE_VISIT_APPLICATION_NO");
                if (!TextUtils.isEmpty(fappNo)) {
                    new AsyncNextGenFranchiseeDetails(AppNotificationActivity.this, fappNo).execute("");
                }
            } else if (requestCode == SUPPORT_REQUEST_CODE && resultCode == RESULT_OK) {

                Intent i = new Intent(AppNotificationActivity.this, SupportTicketActivity.class);
                intent.putExtra("IsBackVisible", false);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //endregion
}