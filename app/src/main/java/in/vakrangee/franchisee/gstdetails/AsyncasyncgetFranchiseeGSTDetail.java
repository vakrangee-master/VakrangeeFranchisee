package in.vakrangee.franchisee.gstdetails;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.DeviceInfo;
import in.vakrangee.supercore.franchisee.utils.Logger;
import in.vakrangee.supercore.franchisee.webservice.WebService;

public class AsyncasyncgetFranchiseeGSTDetail extends AsyncTask<String, Void, String> {
    private final static String TAG = "AsyncasyncgetFranchiseeGSTDetail";
    private Context mContext;
    private DeviceInfo deviceInfo;
    private Logger logger;
    private ProgressDialog progress;
    private Callback callback;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncasyncgetFranchiseeGSTDetail(Context context, Callback callback) {
        super();
        this.mContext = context;
        this.deviceInfo = DeviceInfo.getInstance(context);
        this.logger = Logger.getInstance(context);
        this.callback = callback;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("TAG", "onPreExecute");
        // Get App MODE
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
            String response = null;
            Connection connection = new Connection(mContext);
            String tmpVkId = connection.getVkid();

            if (!TextUtils.isEmpty(tmpVkId)) {
                response = WebService.getFranchiseeGSTDetail(tmpVkId);
            } else {
                String enquiryId = CommonUtils.getEnquiryId(mContext);
                response = WebService.getFranchiseeGSTDetail(enquiryId);
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
                Log.d(TAG, "Get GSTIN Details: " + response);
            else
                Log.e(TAG, "Failed to Get GSTIN Details: " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
