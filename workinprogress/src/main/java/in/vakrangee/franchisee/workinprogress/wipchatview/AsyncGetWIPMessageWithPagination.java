package in.vakrangee.franchisee.workinprogress.wipchatview;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.workinprogress.R;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class AsyncGetWIPMessageWithPagination extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncGetWIPMessageWithPagination";
    private Context mContext;
    private Logger logger;
    private ProgressDialog progress;
    private AsyncGetWIPMessageWithPagination.Callback callback;
    private WIPChatViewRepository wipChatViewRepo;
    private String pageNo;
    private String elementId, elementDetailId;
    private boolean IsFilter = false;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncGetWIPMessageWithPagination(Context context, String PageNo, String elementId, String elementDetailId, boolean IsFiltered, AsyncGetWIPMessageWithPagination.Callback callback) {
        super();
        this.mContext = context;
        logger = Logger.getInstance(context);
        this.pageNo = PageNo;
        this.elementId = elementId;
        this.elementDetailId = elementDetailId;
        this.IsFilter = IsFiltered;
        this.callback = callback;
        wipChatViewRepo = new WIPChatViewRepository(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (!TextUtils.isEmpty(pageNo) && pageNo.equalsIgnoreCase("1")) {
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
        if (!TextUtils.isEmpty(pageNo) && pageNo.equalsIgnoreCase("1")) {
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

            if (IsFilter) {
                response = wipChatViewRepo.getWIPFilterMessages(vkIdOrEnquiryId, pageNo, elementId, elementDetailId);
            } else {
                response = wipChatViewRepo.getGetWIPChatMessagesWithPaginationData(vkIdOrEnquiryId, pageNo);
            }

            return response;

        } catch (Exception e) {
            Log.e("TAG", "Error in getGetWIPChatMessagesWithPaginationData: " + e.getMessage());
            logger.writeError(TAG, "Error in getGetWIPChatMessagesWithPaginationData: " + e.toString());
        }
        return null;
    }

}
