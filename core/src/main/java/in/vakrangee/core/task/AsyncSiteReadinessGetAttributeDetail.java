package in.vakrangee.core.task;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.core.R;
import in.vakrangee.core.utils.CommonUtils;
import in.vakrangee.core.utils.Connection;
import in.vakrangee.core.utils.Constants;
import in.vakrangee.core.utils.EncryptionUtil;
import in.vakrangee.core.utils.Logger;
import in.vakrangee.core.webservice.WebService;

public class AsyncSiteReadinessGetAttributeDetail extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncSiteReadinessGetAttributeDetail";
    private Context mContext;
    private Logger logger;
    private ProgressDialog progress;
    private AsyncSiteReadinessGetAttributeDetail.Callback callback;
    private boolean isAdhoc = false;
    private String elementType;
    private String elementDetailId;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncSiteReadinessGetAttributeDetail(Context context, String elementType, String elementDetailId, AsyncSiteReadinessGetAttributeDetail.Callback callback) {
        super();
        this.mContext = context;
        logger = Logger.getInstance(context);
        this.elementType = elementType;
        this.elementDetailId = elementDetailId;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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

            //Encrypt
            String vkId = EncryptionUtil.encryptString(tmpVkId, mContext);

            String response = null;
            if (isAdhoc) {
                if (!TextUtils.isEmpty(tmpVkId)) {
                    response = WebService.getElemenAttributetDetail(tmpVkId, elementType, elementDetailId);
                } else {
                    String enquiryId = CommonUtils.getEnquiryId(mContext);
                    response = WebService.getElemenAttributetDetail(enquiryId, elementType, elementDetailId);
                }
            }
            return response;

        } catch (Exception e) {
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
                Log.d(TAG, "Successful: Data: " + response);
            else
                Log.d(TAG, "Failed: Data: " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }

}
