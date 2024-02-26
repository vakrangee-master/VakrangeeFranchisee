package in.vakrangee.franchisee.nextgenfranchiseeapplication.bank_statement;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.nextgenfranchiseeapplication.R;
import in.vakrangee.supercore.franchisee.utils.Connection;

public class AsyncGetAllBankStatementDetails extends AsyncTask<String, Void, String> {

    private final static String TAG = "AsyncGetAllBankStatementDetails";
    private Context mContext;
    private ProgressDialog progress;
    private Callback callback;
    private BankStatementRepository bankStatementRepo;
    private String appId;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncGetAllBankStatementDetails(Context context, String appId, Callback callback) {
        super();
        this.mContext = context;
        this.appId = appId;
        this.callback = callback;
        bankStatementRepo = new BankStatementRepository(context);
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
            response = bankStatementRepo.getAllBankStatementDetails(appId);

            return response;

        } catch (Exception e) {
            Log.e("TAG", "Error in AsyncGetAllBankStatementDetails Data: " + e.getMessage());
        }
        return null;
    }
}
