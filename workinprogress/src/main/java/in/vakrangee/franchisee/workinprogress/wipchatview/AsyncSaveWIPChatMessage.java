package in.vakrangee.franchisee.workinprogress.wipchatview;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.shrikanthravi.chatview.data.Message;

import in.vakrangee.franchisee.workinprogress.R;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeviceInfo;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class AsyncSaveWIPChatMessage extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncSaveWIPChatMessage";
    private Context mContext;
    private DeviceInfo deviceInfo;
    private Logger logger;
    private ProgressDialog progress;
    private AsyncSaveWIPChatMessage.Callback callback;
    private WIPChatViewRepository wipChatViewRepo;
    private Message message;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncSaveWIPChatMessage(Context context) {
        super();
        this.mContext = context;
        deviceInfo = DeviceInfo.getInstance(context);
        logger = Logger.getInstance(context);
        wipChatViewRepo = new WIPChatViewRepository(context);
    }

    public AsyncSaveWIPChatMessage(Context context, Message message, AsyncSaveWIPChatMessage.Callback callback) {
        super();
        this.mContext = context;
        deviceInfo = DeviceInfo.getInstance(context);
        logger = Logger.getInstance(context);
        this.message = message;
        this.callback = callback;
        wipChatViewRepo = new WIPChatViewRepository(context);
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
            String enquiryId = CommonUtils.getEnquiryId(mContext);
            String vkIdOrEnquiryId = enquiryId;//TextUtils.isEmpty(tmpVkId) ? enquiryId : tmpVkId;
            String nextGenAppId = "";
            message.setVkIdOrEnquiryId(vkIdOrEnquiryId);
            message.setNextGenFranchiseeAppId(nextGenAppId);

            response = wipChatViewRepo.saveWIPChatMessage(message, vkIdOrEnquiryId);
            return response;

        } catch (Exception e) {
            Log.e("TAG", "Error in saving WIP Chat Message: " + e.getMessage());
            logger.writeError(TAG, "Error in saving WIP Chat Message: " + e.toString());
        }
        return null;
    }
}
