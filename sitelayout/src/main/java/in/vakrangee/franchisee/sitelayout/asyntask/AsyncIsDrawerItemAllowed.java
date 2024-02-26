package in.vakrangee.franchisee.sitelayout.asyntask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.sitelayout.R;
import in.vakrangee.franchisee.sitelayout.repository.DeliveryAddressRepository;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class AsyncIsDrawerItemAllowed extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncIsDrawerItemAllowed";
    private Context mContext;
    private Logger logger;
    private ProgressDialog progress;
    private DeliveryAddressRepository deliveryAddressRepository;
    private AsyncIsDrawerItemAllowed.Callback callback;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncIsDrawerItemAllowed(Context context, AsyncIsDrawerItemAllowed.Callback icallback) {
        super();
        this.mContext = context;
        logger = Logger.getInstance(context);
        this.callback = icallback;
        deliveryAddressRepository = new DeliveryAddressRepository(mContext);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("TAG", "onPreExecute");
        progress = new ProgressDialog(mContext);
        progress.setMessage(mContext.getResources().getString(R.string.pleaseWait));
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            String enquiryId = CommonUtils.getEnquiryId(mContext);

            String response = deliveryAddressRepository.getFranchiseeAppMenuDetail(enquiryId);
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
                Log.d(TAG, "AsyncIsDrawerItemAllowed successfully. " + response);
            else
                Log.e(TAG, "Failed to AsyncIsDrawerItemAllowed. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }


}
