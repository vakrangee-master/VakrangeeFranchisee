package in.vakrangee.franchisee.payment_history;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.sitelayout.repository.DeliveryAddressRepository;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class AsynctaskGetPaymentHistory extends AsyncTask<String, Void, String> {

    private final static String TAG = AsynctaskGetPaymentHistory.class.getSimpleName();
    private Context mContext;
    private Logger logger;
    private ProgressDialog progress;
    private DeliveryAddressRepository deliveryAddressRepository;
    private AsynctaskGetPaymentHistory.Callback callback;
    private String vkid;

    public interface Callback {
        void onResult(String result);
    }

    public AsynctaskGetPaymentHistory(Context context, String vkid, AsynctaskGetPaymentHistory.Callback icallback) {
        super();
        this.mContext = context;
        this.vkid = vkid;
        logger = Logger.getInstance(context);
        deliveryAddressRepository = new DeliveryAddressRepository(context);
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
            String response = deliveryAddressRepository.getPaymentHistoryDetails(vkid);
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
                Log.d(TAG, "AsynctaskGetPaymentHistory  Data successfully. " + response);
            else
                Log.e(TAG, "Failed to get AsynctaskGetPaymentHistory  Data. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }

}
