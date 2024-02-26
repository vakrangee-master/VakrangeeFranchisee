package in.vakrangee.franchisee.workinprogress;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.StringTokenizer;

import in.vakrangee.supercore.franchisee.model.FranchiseeDetails;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.JSONUtils;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class AsyncNextGenWorkInProgressDetail extends AsyncTask<String, Void, FranchiseeDetails> {

    private static final String TAG = "AsyncNextGenWorkInProgressDetail";
    private Context mContext;
    private TelephonyManager telephonyManager;
    private String errorMsg;
    String tempFAId, tempnextgenSiteWorkInProgressId;
    private IGetFranchiseeDetails iGetFranchiseeDetails;
    private ProgressDialog progress;
    private boolean isAdhoc = false;

    //region Callback Interface
    public interface IGetFranchiseeDetails {
        public void getFranchiseeDetail(FranchiseeDetails franchiseeDetails, String errorMsg);
    }

    public AsyncNextGenWorkInProgressDetail(Context context, String faId, String nextgenSiteWorkInProgressId, IGetFranchiseeDetails iGetFranchiseeDetails) {
        super();
        this.mContext = context;
        telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        this.tempFAId = faId;
        this.tempnextgenSiteWorkInProgressId = nextgenSiteWorkInProgressId;
        this.iGetFranchiseeDetails = iGetFranchiseeDetails;
        errorMsg = mContext.getString(R.string.Warning);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Get App MODE
        isAdhoc = Constants.ENABLE_ADHOC_MODE || Constants.ENABLE_FRANCHISEE_MODE;

        progress = new ProgressDialog(mContext);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @SuppressLint("LongLogTag")
    @Override
    protected FranchiseeDetails doInBackground(String... strings) {
        // If you want to use 'values' string in here
        Log.i(TAG, "doInBackground");
        try {
            // Preparing Data
            Connection connection = new Connection(mContext);
            String tempVkId = connection.getVkid();

            String vkId = EncryptionUtil.encryptString(tempVkId, mContext);
            String tokenId = EncryptionUtil.encryptString(connection.getTokenId(), mContext);

            String deviceid = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            String deviceId = EncryptionUtil.encryptString(deviceid, mContext);

            String deviceIDAndroid = CommonUtils.getAndroidUniqueID(mContext);
            String imeiNo = EncryptionUtil.encryptString(deviceIDAndroid, mContext);

            String simSerial = CommonUtils.getSimSerialNumber(mContext);
            String simNo = EncryptionUtil.encryptString(simSerial, mContext);

            String nextgenFranchiseeApplicationId = EncryptionUtil.encryptString(tempFAId, mContext);
            String nextgenSiteWorkInProgressId = EncryptionUtil.encryptString(tempnextgenSiteWorkInProgressId, mContext);

            String displayServerResopnse = "";
            if (isAdhoc) {
                displayServerResopnse = WebService.getNextGenSiteWorkInProgressDetail(tempVkId, tempFAId, tempnextgenSiteWorkInProgressId);
            } else {
                displayServerResopnse = WebService.getNextGenSiteWorkInProgressDetail(vkId, tokenId, imeiNo, deviceId, simNo, nextgenFranchiseeApplicationId, nextgenSiteWorkInProgressId);
            }

            if (displayServerResopnse.startsWith("ERROR|")) {

                StringTokenizer tokens = new StringTokenizer(displayServerResopnse, "|");
                tokens.nextToken();     // Jump to next Token
                errorMsg = tokens.nextToken();

                return null;
            }

            StringTokenizer tokens1 = new StringTokenizer(displayServerResopnse, "|");
            tokens1.nextToken();    // Jump to next Token
            String franchiseeData = tokens1.nextToken();

            Log.d(TAG, "FranchiseeData: " + franchiseeData);
            String ab = franchiseeData.substring(1, franchiseeData.length() - 1);
            String franchiseeJsonData = ab.replace("\r\n", "");

            return JSONUtils.toJson(FranchiseeDetails.class, franchiseeJsonData); // returning FranchiseeDetails Object
        } catch (Exception e) {
            Log.e("TAG", "Error:in Search Vakrangee Kendra " + e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(FranchiseeDetails franchiseeDetails) {
        super.onPostExecute(franchiseeDetails);
        progress.dismiss();

        if (franchiseeDetails != null) {
            iGetFranchiseeDetails.getFranchiseeDetail(franchiseeDetails, null);

        } else {
            iGetFranchiseeDetails.getFranchiseeDetail(null, errorMsg);

        }
    }

}
