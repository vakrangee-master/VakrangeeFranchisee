package in.vakrangee.franchisee.networktesting.simstrength;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.supercore.franchisee.utils.Connection;

public class AsyncGetSIMNetworkTestingConfigDetails extends AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncGetSIMNetworkTestingConfigDetails";
    private Context context;
    private ProgressDialog progressDialog;
    private String response;
    private NetworkTestingRepository networkTestingRepo;
    private Callback callback;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncGetSIMNetworkTestingConfigDetails(Context context, Callback callback) {
        this.context = context;
        networkTestingRepo = new NetworkTestingRepository(context);
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        Connection connection = new Connection(context);
        String tmpVkId = connection.getVkid();

        response = networkTestingRepo.getSIMNetworkTestingConfig(tmpVkId);

        return null;
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }

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
}
