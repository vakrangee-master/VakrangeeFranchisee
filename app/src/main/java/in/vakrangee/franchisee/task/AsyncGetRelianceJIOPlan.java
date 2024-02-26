package in.vakrangee.franchisee.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeviceInfo;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class AsyncGetRelianceJIOPlan extends AsyncTask<String, Void, String> {

    private final static String TAG = AsyncGetRelianceJIOPlan.class.getCanonicalName();
    private Context mContext;
    private DeviceInfo deviceInfo;
    private Logger logger;
    private ProgressDialog progress;
    private Callback callback;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncGetRelianceJIOPlan(Context context) {
        super();
        this.mContext = context;
        deviceInfo = DeviceInfo.getInstance(context);
        logger = Logger.getInstance(context);
    }

    public AsyncGetRelianceJIOPlan(Context context, Callback callback) {
        super();
        this.mContext = context;
        deviceInfo = DeviceInfo.getInstance(context);
        logger = Logger.getInstance(context);
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("TAG", "onPreExecute");
        progress = new ProgressDialog(mContext);
        progress.setTitle(R.string.pleaseWait);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            Connection connection = new Connection(mContext);
            String tmpVkId = connection.getVkid();
            if (TextUtils.isEmpty(tmpVkId)) {
                Log.e(TAG, "Failed to get Reliance JIO Plan. [VKId is null].");
                return null;
            }

            String tmpTokenId = connection.getTokenId();
            if (TextUtils.isEmpty(tmpTokenId)) {
                Log.e(TAG, "Failed to get Reliance JIO Plan. [TokenId is null].");
                return null;
            }

            String mobileNumber = EncryptionUtil.encryptString(strings[0], mContext);
            String vkId = EncryptionUtil.encryptString(tmpVkId, mContext);
            String tokenId = EncryptionUtil.encryptString(tmpTokenId, mContext);
            String response = WebService.getRelianceJIOPlans(vkId, tokenId, deviceInfo.getIMEI(), deviceInfo.getDeviceId(), deviceInfo.getSimNo(), mobileNumber);

            return response;

        } catch (Exception e) {
            Log.e("TAG", "Exception: " + e.getMessage());
            logger.writeError(TAG, "Exception: " + e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        progress.dismiss();     // Hide Progress Bar

        if (!TextUtils.isEmpty(response)) {
            if (response.startsWith("OKAY"))
                Log.d(TAG, "Get Reliance JIO Plan successfully. " + response);
            else
                Log.e(TAG, "Failed to get Reliance JIO Plan. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
