package in.vakrangee.franchisee.workinprogress.wipchatview;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.workinprogress.R;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.DeviceInfo;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class AsyncGetWIPCategoryDetails extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncGetWIPCategoryDetails";
    private Context mContext;
    private DeviceInfo deviceInfo;
    private Logger logger;
    private ProgressDialog progress;
    private AsyncGetWIPCategoryDetails.Callback callback;
    private String from;
    private String categoryId;
    private WIPChatViewRepository wipChatViewRepo;
    private boolean IsProgressHide = false;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncGetWIPCategoryDetails(Context context) {
        super();
        this.mContext = context;
        deviceInfo = DeviceInfo.getInstance(context);
        logger = Logger.getInstance(context);
        wipChatViewRepo = new WIPChatViewRepository(context);
    }

    public AsyncGetWIPCategoryDetails(Context context, String FROM, String categoryId, AsyncGetWIPCategoryDetails.Callback callback) {
        super();
        this.mContext = context;
        deviceInfo = DeviceInfo.getInstance(context);
        logger = Logger.getInstance(context);
        this.from = FROM;
        this.categoryId = categoryId;
        this.callback = callback;
        wipChatViewRepo = new WIPChatViewRepository(context);
    }

    public AsyncGetWIPCategoryDetails(Context context, String FROM, String categoryId, boolean IsProgressHide, AsyncGetWIPCategoryDetails.Callback callback) {
        super();
        this.mContext = context;
        deviceInfo = DeviceInfo.getInstance(context);
        logger = Logger.getInstance(context);
        this.from = FROM;
        this.categoryId = categoryId;
        this.IsProgressHide = IsProgressHide;
        this.callback = callback;
        wipChatViewRepo = new WIPChatViewRepository(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("TAG", "onPreExecute");
        if (!IsProgressHide) {
            progress = new ProgressDialog(mContext);
            progress.setTitle(R.string.pleaseWait);
            progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
            progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (!IsProgressHide) {
            progress.dismiss();     // Hide Progress Bar
        }

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
            String enquiryId = CommonUtils.getEnquiryId(mContext);
            String vkIdOrEnquiryId = enquiryId;//TextUtils.isEmpty(tmpVkId) ? enquiryId : tmpVkId;

            if (from.equalsIgnoreCase(Constants.WIP_CHATVIEW_CATEGORY))
                response = wipChatViewRepo.getGetCategoryList(vkIdOrEnquiryId);
            else if (from.equalsIgnoreCase(Constants.WIP_CHATVIEW_SUB_CATEGORY))
                response = wipChatViewRepo.getGetSubCategoryList(vkIdOrEnquiryId, categoryId);
            else if (from.equalsIgnoreCase(Constants.WIP_CHATVIEW_GET_MESSAGES))
                response = wipChatViewRepo.getGetWIPChatMessagesData(vkIdOrEnquiryId);

            return response;

        } catch (Exception e) {
            Log.e("TAG", "Error in getting WIP ChatView Category Data: " + e.getMessage());
            logger.writeError(TAG, "Error in getting WIP ChatView Category Data: " + e.toString());
        }
        return null;
    }
}
