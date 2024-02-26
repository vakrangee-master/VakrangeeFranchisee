package in.vakrangee.franchisee.bcadetails.pre;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.bcadetails.BCAEntryDetailsRepository;
import in.vakrangee.franchisee.bcadetails.R;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Connection;

@SuppressLint("LongLogTag")
public class AsyncValidateMobileNoUsingOTP extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncValidateMobileNoUsingOTP";
    private Context mContext;
    private ProgressDialog progress;
    private Callback callback;
    private BCAEntryDetailsRepository bcaRepo;
    private String mobileNo;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncValidateMobileNoUsingOTP(Context context, String mobileNo, Callback callback) {
        super();
        this.callback = callback;
        this.mContext = context;
        this.mobileNo = mobileNo;
        bcaRepo = new BCAEntryDetailsRepository(context);
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
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        progress.dismiss();     // Hide Progress Bar

        if (!TextUtils.isEmpty(response)) {
            Log.d(TAG, "Get Data " + response);
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

            String enquiryId = CommonUtils.getEnquiryId(mContext);
            response = bcaRepo.validateMobileNo(enquiryId, mobileNo);

            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
