package in.vakrangee.franchisee.atmloading;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class AsyncGetATMRoCashLoadingDetails extends AsyncTask<String, String, String> {

    private final static String TAG = AsyncGetATMRoCashLoadingDetails.class.getSimpleName();
    private Context mContext;
    private Logger logger;
    private ProgressDialog progress;
    private ATMLoadingRepository atmLoadingRepository;
    private AsyncGetATMRoCashLoadingDetails.Callback callback;
    private String atmId, statusId;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncGetATMRoCashLoadingDetails(Context context, String atmId, String statusId, AsyncGetATMRoCashLoadingDetails.Callback icallback) {
        super();
        this.mContext = context;
        logger = Logger.getInstance(context);
        this.atmId = atmId;
        this.statusId = statusId;
        atmLoadingRepository = new ATMLoadingRepository(context);
        this.callback = icallback;

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
            String enquiryId = CommonUtils.getEnquiryId(mContext);
            String vkIdOrEnquiryId = TextUtils.isEmpty(tmpVkId) ? enquiryId : tmpVkId;

            String response = atmLoadingRepository.getATMROCashDetails(vkIdOrEnquiryId, atmId, statusId);
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
                Log.d(TAG, "AsyncGetATMRoCashLoadingDetails  Data successfully. " + response);
            else
                Log.e(TAG, "Failed to get AsyncGetATMRoCashLoadingDetails  Data. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
