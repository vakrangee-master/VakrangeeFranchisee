package in.vakrangee.franchisee.franchiseelogin;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.R;
import in.vakrangee.supercore.franchisee.utils.Logger;

public class AsyncValidateUser extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncValidateUser";
    private Context mContext;
    private Logger logger;
    private ProgressDialog progress;
    private String mobEmailId;
    private FranchiseeAuthenticationRepository franchiseeAuthRepo;
    private AsyncValidateUser.Callback callback;
    private String response;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncValidateUser(Context context, String mobEmailId, AsyncValidateUser.Callback icallback) {
        super();
        this.mContext = context;
        logger = Logger.getInstance(context);
        franchiseeAuthRepo = new FranchiseeAuthenticationRepository(context);
        this.mobEmailId = mobEmailId;
        this.callback = icallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
            response = null;

            //Validate User
            response = franchiseeAuthRepo.validateUser(mobEmailId);
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
                Log.d(TAG, "Franchisee validated successfully. " + response);
            else
                Log.e(TAG, "Failed to validate Franchisee. " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
