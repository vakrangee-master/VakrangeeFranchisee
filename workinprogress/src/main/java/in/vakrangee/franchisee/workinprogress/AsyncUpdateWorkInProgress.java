package in.vakrangee.franchisee.workinprogress;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeviceInfo;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class AsyncUpdateWorkInProgress extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncUpdateWorkInProgress";
    private Context mContext;
    private DeviceInfo deviceInfo;
    private Logger logger;
    private ProgressDialog progress;
    private AsyncUpdateWorkInProgress.Callback callback;
    private boolean isAdhoc = false;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncUpdateWorkInProgress(Context context) {
        super();
        this.mContext = context;
        deviceInfo = DeviceInfo.getInstance(context);
        logger = Logger.getInstance(context);
    }

    public AsyncUpdateWorkInProgress(Context context, AsyncUpdateWorkInProgress.Callback callback) {
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
        // Get App MODE
        isAdhoc = Constants.ENABLE_ADHOC_MODE || Constants.ENABLE_FRANCHISEE_MODE;
        progress = new ProgressDialog(mContext);
        progress.setTitle(R.string.pleaseWait);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @SuppressLint("LongLogTag")
    @Override
    protected String doInBackground(String... strings) {

        try {

            Connection connection = new Connection(mContext);
            String tmpVkId = connection.getVkid();
            if (TextUtils.isEmpty(tmpVkId)) {
                Log.e(TAG, "Failed to Update Work In Progress. [VKId is null].");
                return null;
            }

            String workInProgressData = strings[0];   // Work In Progress Data - JSON
            String vkId = EncryptionUtil.encryptString(tmpVkId, mContext);

            String response = null;
            if (isAdhoc) {
                response = WebService.nextgenSiteWorkInProgressUpdate(tmpVkId, workInProgressData);
            } else {
                String tmpTokenId = connection.getTokenId();
                if (TextUtils.isEmpty(tmpTokenId)) {
                    Log.e(TAG, "Failed to Update Work In Progress. [TokenId is null].");
                    return null;
                }
                String tokenId = EncryptionUtil.encryptString(tmpTokenId, mContext);
                response = WebService.nextgenSiteWorkInProgressUpdate(vkId, tokenId, deviceInfo.getIMEI(), deviceInfo.getDeviceId(), deviceInfo.getSimNo(), workInProgressData);
            }
            return response;

        } catch (Exception e) {
            Log.e("TAG", "Exception: " + e.getMessage());
            logger.writeError(TAG, "Exception: " + e.toString());
        }
        return null;
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        progress.dismiss();     // Hide Progress Bar

        if (!TextUtils.isEmpty(response)) {
            if (response.startsWith("OKAY"))
                Log.d(TAG, "Work In Progress Updated successfully. " + response);
            else
                Log.e(TAG, "Failed to Update Work In Progress. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
