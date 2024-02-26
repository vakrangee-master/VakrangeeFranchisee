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
public class AsyncGetAllCashLoadingList extends AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncGetAllCashLoadingList";
    private Context context;
    private ProgressDialog progressDialog;
    private String response;
    private ATMLoadingRepository atmLoadingRepo;
    private AsyncGetAllCashLoadingList.Callback callback;
    private String atmId, status;

    public interface Callback {
        void onResult(String result);
    }

    public AsyncGetAllCashLoadingList(Context context, String atmId,String status, AsyncGetAllCashLoadingList.Callback callback) {
        this.context = context;
        atmLoadingRepo = new ATMLoadingRepository(context);
        this.atmId = atmId;
        this.status = status;
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
        String vkIdOrEnquiryId = connection.getVkid();

        response = atmLoadingRepo.getAllATMCashLoadingList(vkIdOrEnquiryId, atmId, status);

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
