package in.vakrangee.franchisee.atmloading;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class AsyncGetATMPhysicalORCashLoadingDetails extends AsyncTask<String, Void, String> {

    private final static String TAG = AsyncGetATMPhysicalORCashLoadingDetails.class.getSimpleName();
    private Context mContext;
    private Logger logger;
    private ProgressDialog progress;
    private ATMLoadingRepository atmLoadingRepository;
    private AsyncGetATMPhysicalORCashLoadingDetails.Callback callback;
    private String cashLodingId;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncGetATMPhysicalORCashLoadingDetails(Context context, String cashLodingId, AsyncGetATMPhysicalORCashLoadingDetails.Callback icallback) {
        super();
        this.mContext = context;
        logger = Logger.getInstance(context);
        this.cashLodingId = cashLodingId;
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

            String response = atmLoadingRepository.getATMPhysicalORCashLoadingDetails(vkIdOrEnquiryId, cashLodingId);
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
                Log.d(TAG, "AsyncSaveATMLoadingDetails  Data successfully. " + response);
            else
                Log.e(TAG, "Failed to get AsyncSaveATMLoadingDetails  Data. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
