package in.vakrangee.franchisee.atmloading.cash_loading;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import in.vakrangee.franchisee.atmloading.ATMLoadingRepository;
import in.vakrangee.franchisee.atmloading.R;
import in.vakrangee.supercore.franchisee.utils.Connection;

@SuppressLint("LongLogTag")
public class AsyncGetSpecificCashLoadingDetails extends AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncGetSpecificCashLoadingDetails";
    private Context context;
    private ProgressDialog progressDialog;
    private String response;
    private ATMLoadingRepository atmLoadingRepo;
    private AsyncGetSpecificCashLoadingDetails.Callback callback;
    private String atmCashLoadingID;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncGetSpecificCashLoadingDetails(Context context, String atmCashLoadingID, AsyncGetSpecificCashLoadingDetails.Callback callback) {
        this.context = context;
        atmLoadingRepo = new ATMLoadingRepository(context);
        this.atmCashLoadingID = atmCashLoadingID;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(context.getString(R.string.please_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        Connection connection = new Connection(context);
        String vkId = connection.getVkid();

        response = atmLoadingRepo.getSpecificATMCashLoadingDetails(vkId, atmCashLoadingID);

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        if (!TextUtils.isEmpty(response)) {
            Log.d(TAG, "Get Data " + response);

            if (callback != null)
                callback.onResult(response);  // Send Response Back To Caller.
        } else {
            if (callback != null)
                callback.onResult(null);    // Exception Occurred or Some Issue Occurred.
        }
    }
}
