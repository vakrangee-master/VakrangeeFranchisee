package in.vakrangee.franchisee.nextgenfranchiseeapplication.newexistingfranchisee;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.nextgenfranchiseeapplication.R;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.Constants;
import in.vakrangee.supercore.franchisee.utils.FranchiseeApplicationRepository;

public class AsyncNewExistingFranchiseeChecks extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncNewExistingFranchiseeChecks";
    private Context mContext;
    private ProgressDialog progress;
    private AsyncNewExistingFranchiseeChecks.Callback callback;
    private FranchiseeApplicationRepository franchiseeAppRepo;
    private String from;
    private String data;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncNewExistingFranchiseeChecks(Context context, String FROM, String data, AsyncNewExistingFranchiseeChecks.Callback callback) {
        super();
        this.mContext = context;
        this.from = FROM;
        this.data = data;
        this.callback = callback;
        franchiseeAppRepo = new FranchiseeApplicationRepository(context);
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

    @SuppressLint("LongLogTag")
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

            String enquiryId = CommonUtils.getEnquiryId(mContext);

            if (from.equalsIgnoreCase(Constants.FROM_ISREQUIRE_CHECK))
                response = franchiseeAppRepo.IsRequireNewOrExistingCheck(enquiryId);
            else if (from.equalsIgnoreCase(Constants.FROM_AUTHENTICATE_EXISTING_FRANCHISEE))
                response = franchiseeAppRepo.authenticateExistingFranchisee(data);

            return response;

        } catch (Exception e) {
            Log.e("TAG", "Error in getting New or Existing Franchisee Checks: " + e.getMessage());

        }
        return null;
    }
}
