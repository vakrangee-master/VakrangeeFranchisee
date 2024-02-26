package in.vakrangee.franchisee.support;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeviceInfo;
import in.vakrangee.supercore.franchisee.utils.EncryptionUtil;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class AsyncGetCatSubCatAndIssueType extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncGetCatSubCatAndIssueType";
    private Context mContext;
    private DeviceInfo deviceInfo;
    private Logger logger;
    private ProgressDialog progress;
    private Callback callback;
    private String from;
    private String categoryId;
    private String subCategoryId;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncGetCatSubCatAndIssueType(Context context) {
        super();
        this.mContext = context;
        deviceInfo = DeviceInfo.getInstance(context);
        logger = Logger.getInstance(context);
    }

    public AsyncGetCatSubCatAndIssueType(Context context, String FROM, String categoryId, String subCategoryId, Callback callback) {
        super();
        this.mContext = context;
        deviceInfo = DeviceInfo.getInstance(context);
        logger = Logger.getInstance(context);
        this.from = FROM;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
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
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        progress.dismiss();     // Hide Progress Bar

        if (!TextUtils.isEmpty(response)) {
            if (response.startsWith("OKAY"))
                Log.d(TAG, "Get Data " + response);
            else
                Log.e(TAG, "Failed to get Data. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String response = null;

            Connection connection = new Connection(mContext);
            String tmpVkId = connection.getVkid();
            if (TextUtils.isEmpty(tmpVkId)) {
                Log.e(TAG, "Failed to get Support Ticket Data. [VKId is null].");
                return null;
            }

            String tmpTokenId = connection.getTokenId();
            if (TextUtils.isEmpty(tmpTokenId)) {
                Log.e(TAG, "Failed to get Support Ticket Data. [TokenId is null].");
                return null;
            }

            String mobileNumber = EncryptionUtil.encryptString(strings[0], mContext);
            String vkId = EncryptionUtil.encryptString(tmpVkId, mContext);
            String tokenId = EncryptionUtil.encryptString(tmpTokenId, mContext);

            if (from.equalsIgnoreCase(Constants.SUPPORT_TICKET_CATEGORY))
                response = WebService.getSupportTicketCategories(vkId, tokenId, deviceInfo.getIMEI(), deviceInfo.getDeviceId(), deviceInfo.getSimNo());
            else if (from.equalsIgnoreCase(Constants.SUPPORT_TICKET_SUB_CATEGORY))
                response = WebService.getSupportTicketSubCategories(vkId, tokenId, deviceInfo.getIMEI(), deviceInfo.getDeviceId(), deviceInfo.getSimNo(), EncryptionUtil.encryptString(categoryId, mContext));
            else if (from.equalsIgnoreCase(Constants.SUPPORT_TICKET_ISSUE_TYPE))
                response = WebService.getSupportTicketIssueType(vkId, tokenId, deviceInfo.getIMEI(), deviceInfo.getDeviceId(), deviceInfo.getSimNo(), EncryptionUtil.encryptString(subCategoryId, mContext));

            return response;

        } catch (Exception e) {
            Log.e("TAG", "Error in getting Support Ticket Data: " + e.getMessage());
            logger.writeError(TAG, "Error in getting Support Ticket Data: " + e.toString());
        }
        return null;
    }
}
