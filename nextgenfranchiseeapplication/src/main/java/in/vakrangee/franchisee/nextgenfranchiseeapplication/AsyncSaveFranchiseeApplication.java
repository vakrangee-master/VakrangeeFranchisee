package in.vakrangee.franchisee.nextgenfranchiseeapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.supercore.franchisee.model.NextGenFranchiseeApplicationFormDto;
import in.vakrangee.supercore.franchisee.utils.CommonUtils;
import in.vakrangee.supercore.franchisee.utils.FranchiseeApplicationRepository;
import in.vakrangee.supercore.franchisee.utils.Logger;

@SuppressLint("LongLogTag")
public class AsyncSaveFranchiseeApplication extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncSaveFranchiseeApplication";
    private Context mContext;
    private Logger logger;
    private ProgressDialog progress;
    private FranchiseeApplicationRepository fapRepo;
    private AsyncSaveFranchiseeApplication.Callback callback;
    private String type;
    private NextGenFranchiseeApplicationFormDto applicationFormDto;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncSaveFranchiseeApplication(Context context, String type, NextGenFranchiseeApplicationFormDto applicationFormDto, AsyncSaveFranchiseeApplication.Callback icallback) {
        super();
        this.mContext = context;
        logger = Logger.getInstance(context);
        fapRepo = new FranchiseeApplicationRepository(context);
        this.type = type;
        this.applicationFormDto = applicationFormDto;
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
            String enquiryId = CommonUtils.getEnquiryId(mContext);
            String response = fapRepo.saveFranchiseeDetail(enquiryId, type, applicationFormDto);

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
                Log.d(TAG, "Franchisee Application Updated successfully. " + response);
            else
                Log.e(TAG, "Failed to Update Franchisee Application. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
