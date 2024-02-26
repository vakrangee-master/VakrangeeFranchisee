package in.vakrangee.franchisee.sitelayout.sitecommencement;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeviceInfo;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class AsyncUpdateWorkCommencement extends AsyncTask<String, Void, String> {

    private final static String TAG = AsyncUpdateWorkCommencement.class.getCanonicalName();
    private Context mContext;
    private DeviceInfo deviceInfo;
    private Logger logger;
    private ProgressDialog progress;
    private Callback callback;
    private boolean isAdhoc = false;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncUpdateWorkCommencement(Context context) {
        super();
        this.mContext = context;
        deviceInfo = DeviceInfo.getInstance(context);
        logger = Logger.getInstance(context);
    }

    public AsyncUpdateWorkCommencement(Context context, Callback cbk) {
        super();
        this.mContext = context;
        deviceInfo = DeviceInfo.getInstance(context);
        logger = Logger.getInstance(context);
        callback = cbk;
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

    @Override
    protected String doInBackground(String... strings) {

        try {

            Connection connection = new Connection(mContext);
            String tmpVkId = connection.getVkid();
            String commencementData = strings[0];   // Commencement Data - JSON
            Log.e(TAG, "Testing: Commencement Data: " + commencementData);
            String vkId = EncryptionUtil.encryptString(tmpVkId, mContext);

            String response = null;
            if (isAdhoc) {

                if (!TextUtils.isEmpty(tmpVkId)) {
                    response = WebService.nextgenSiteWorkCommencementUpdate(tmpVkId, commencementData);
                } else {
                    String enquiryId = CommonUtils.getEnquiryId(mContext);
                    response = WebService.nextgenSiteWorkCommencementUpdate(enquiryId, commencementData);
                }
            } else {
                String tmpTokenId = connection.getTokenId();
                if (TextUtils.isEmpty(tmpTokenId)) {
                    Log.e(TAG, "Failed to Update Work Commencement. [TokenId is null].");
                    return null;
                }
                String tokenId = EncryptionUtil.encryptString(tmpTokenId, mContext);
                response = WebService.nextgenSiteWorkCommencementUpdate(vkId, tokenId, deviceInfo.getIMEI(), deviceInfo.getDeviceId(), deviceInfo.getSimNo(), commencementData);
            }
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
                Log.d(TAG, "Work Commencement Updated successfully. " + response);
            else
                Log.e(TAG, "Failed to Update Work Commencement. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
